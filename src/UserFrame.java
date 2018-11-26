import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public class UserFrame extends JFrame {

	private JFrame itself = this;
	private FootballData data;
	private BetData data2;
	private JButton b1, b2;
	private JTable t1, t2;
	private JPanel p1, p2;
	private JScrollPane sc1, sc2;
	private JTextField tf1;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;
	private Object[] hxv = { 'H', 'X', 'V' };
	private JLabel l1, l2, l3, l4;
	double balance, expected = 0, sum = 0;

	/**
	 * Inicializ�lja az ablak kin�zet�t �s szerkezet�t.
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	UserFrame() {
		super("JAVABet");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setFootballReadClose();
		setBetReadClose();
		balanceRead();
		checkBets();
		setMenu();
		setSize(1150, 500);
		cb = new JComboBox(hxv);
		t1 = new JTable(data);
		t2 = new JTable(data2);
		tf1 = new JTextField("0", 7);
		l1 = new JLabel("�sszeg:");
		l2 = new JLabel("V�rhat� nyerem�ny: " + Double.parseDouble(tf1.getText()) * sumOdds() + " Ered� odds:");
		l3 = new JLabel(sumOdds() + "");
		l4 = new JLabel("Egyenleged: " + balance);
		p1 = new JPanel();
		p2 = new JPanel();
		b1 = new JButton("Meccs hozz�ad�sa");
		b2 = new JButton("Egyenleg felt�lt�se");
		setRowSorterByResult();
		b1.addActionListener(new makeBetListener());
		b2.addActionListener(new uploadBalanceListener());
		sc1 = new JScrollPane(t1);
		sc2 = new JScrollPane(t2);
		add(sc1, BorderLayout.WEST);
		add(sc2, BorderLayout.EAST);
		p1.add(cb);
		p1.add(b1);
		p2.add(l1);
		p2.add(tf1);
		p2.add(l2);
		p2.add(l3);
		p2.add(l4);
		p2.add(b2);
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.SOUTH);

	}
	
	/**
	 * Itt t�rt�nik a fogad�sok megvizsg�l�sa, azaz, ha a meccsre fogadtunk �s k�zben lett hozz� be�rva
	 * eredm�ny, azt itt friss�ti. Az els� ciklus a meccseken megy v�gig, a m�sodik pedig a fogad�sokon.
	 * �gy ellen�riz, hogy ha a hazai csapat neve egyezik a k�t elemnek, akkor megy tov�bb.
	 */
	
	public void checkBets() {
		int w = 0;
		int l = 0;
		for(Football f : data.footballMatch) {
			if(!f.getResult().equals("NA")) {
				for(Bet b : data2.betList) {
					if(f.getTeamHome().equals(b.getTeamHome())) {
						String s = f.getResult();
						String[] st = s.split("-");
						int h = 0, v = 0;
						h = Integer.parseInt(st[0]);
						v = Integer.parseInt(st[1]);
						if(h > v && b.getChoice() == 'H' || h < v && b.getChoice() == 'V' || h == v && b.getChoice() == 'X') {
							w++;
						}
						l++;
					}
				}
			}
		}	
		if(w == data2.betList.size() || l == data2.betList.size()) {
			balance = balance + expected;	
			data2.betList.clear();
		}		
	}

	/**
	 * A be�rt �sszeget szorozza meg az ered� odds-cal, �gy kapjuk meg az ered� szorz�t.
	 * @return Ered� szorz�
	 */
	
	public double sumOdds() {
		if (t2.getRowCount() != 0) {
			sum = Double.parseDouble(t2.getValueAt(0, 3) + "");
			for (int i = 1; i < t2.getRowCount(); i++) {
				sum = sum * Double.parseDouble(t2.getValueAt(i, 3) + "");
			}
			return sum;
		}
		return 0;
	}

	/**
	 * Az egyenleg beolvas�sa valamint annak ki�r�sa sz�vegf�jlba.
	 */
	
	public void balanceRead() {
		try {
			FileReader fr = new FileReader("balance.txt");
			BufferedReader br = new BufferedReader(fr);
			balance = Double.parseDouble(br.readLine());
			expected = Double.parseDouble(br.readLine());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					FileWriter fw = new FileWriter("balance.txt");
					PrintWriter pw = new PrintWriter(fw);
					pw.println(balance);
					expected = Double.parseDouble(tf1.getText()) * sumOdds();
					pw.println(expected);
					pw.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
	}

	/**
	 * A t�bl�zat elemei k�z�l kiv�logatja, amelyikhez nincs eredm�ny be�rva.
	 */
	
	public void setRowSorterByResult() {
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(t1.getModel());
		sorter.setRowFilter(RowFilter.regexFilter("[a-zA-Z]", 2));
		t1.setRowSorter(sorter);
	}

	/**
	 * A focimeccseket olvassa be �s �rja ki f�jlba szerializ�l�ssal.
	 */
	
	public void setFootballReadClose() {
		try {
			data = new FootballData();
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("matches.dat"));
			data.footballMatch = (List<Football>) ois.readObject();
			ois.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("matches.dat"));
					oos.writeObject(data.footballMatch);
					oos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
	}

	/**
	 * A fogad�sokat olvassa be �s menti ki szerializ�ssal.
	 */
	
	public void setBetReadClose() {
		try {
			data2 = new BetData();
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("bets.dat"));
			data2.betList = (List<Bet>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("bets.dat"));
					oos.writeObject(data2.betList);
					oos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * A men�t inicializ�lja, amelyben tal�lhat� a seg�ts�g dial�gusablak.
	 */
	
	public void setMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Eszk�z�k");
		menuBar.add(menu);
		setJMenuBar(menuBar);
		JMenuItem help = new JMenuItem("Seg�ts�g", KeyEvent.VK_N);
		help.addActionListener(new HelpListener());
		menu.add(help);
	}
	
	
	public class makeBetListener implements ActionListener {
		
		/**
		 * Fogad�s hozz�ad�sa gomb, mely seg�ts�g�vel friss�teni lehet a lenti mez�ket, valamint elmenteni egy fogad�st.
		 */
		
		public void actionPerformed(ActionEvent e) {
			int c;
			if ((char) cb.getSelectedItem() == 'H')
				c = 3;
			else if ((char) cb.getSelectedItem() == 'X')
				c = 4;
			else
				c = 5;
			data2.addBet((String) t1.getValueAt(t1.getSelectedRow(), 0), (String) t1.getValueAt(t1.getSelectedRow(), 1),
					(char) cb.getSelectedItem(), (double) t1.getValueAt(t1.getSelectedRow(), c));
			sumOdds();
			l2.setText("V�rhat� nyerem�ny: " + Double.parseDouble(tf1.getText()) * sumOdds() + "Ered� odds: ");
			l3.setText(sumOdds() + "");
			balance = balance - Double.parseDouble(tf1.getText());
			l4.setText(balance + "");
		}
	}

	public class uploadBalanceListener implements ActionListener {
		
		/**
		 * Az egyenleg felt�lt�se ablakot nyitja meg.
		 */
		
		public void actionPerformed(ActionEvent arg0) {
			String s = (String) JOptionPane.showInputDialog(itself, "�sszeg", "Egyenleg felt�lt�se",
					JOptionPane.PLAIN_MESSAGE);
			balance += Double.parseDouble(s);
			l4.setText(balance - Double.parseDouble(tf1.getText()) + "");
		}
	}

	public class HelpListener implements ActionListener {
		
		/**
		 * A seg�ts�g dial�gusablakot nyitja meg.
		 */
		
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(itself, "Fogad�s t�tele: Sor kiv�laszt�sa r�klikkel�ssel.\n A gy�ztes csapat kiv�laszt�sa a leg�rd�l� f�lben.\n Ha t�bb meccsre szeretn�nk fogadni, akkor ugyanezt ism�telve.\n Az utols� fogad�s sor�n a feltett �sszeget be kell �rni az �sszeg mez�be, majd Meccs hozz�ad�sa gomb.\n Egyenleg felt�lt�se: Egyenleg felt�lt�se gombra kattintva.\n �sszeg be�r�sa, majd OK gomb.", "Seg�ts�g", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
}
