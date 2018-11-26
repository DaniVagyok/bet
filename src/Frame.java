import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private FootballData data;
	private JFrame itself = this;
	private JTextField teamHome, teamAway, result, oddsHome, oddsDraw, oddsAway;
	private JScrollPane sc, sc2;
	private JButton b, b2;
	private JPanel southPanel, centerPanel;
	private JTable t, t2;

	/**
	 * Az ablakot inicializálja.
	 */
	
	Frame() {
		super("JAVABet");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setFileReadClose();
		setSize(1150, 500);
		setMenu();
		setSouthComponents();
		setCenterComponents();
		t = new JTable(data);
		t2 = new JTable(data);
		setRowSorterByResult();
		sc = new JScrollPane(t);
		sc2 = new JScrollPane(t2);
		add(sc, BorderLayout.WEST);
		add(sc2, BorderLayout.EAST);
	}

	/**
	 * A középsõ komponenseket inicializálja.
	 */
	
	public void setCenterComponents() {
		b2 = new JButton("Mentés");
		b2.addActionListener(new SaveButtonActionListener());
		centerPanel = new JPanel();
		centerPanel.add(b2);
		add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * Az alsó komponenseket inicializálja.
	 */
	
	public void setSouthComponents() {
		b = new JButton("Hozzáadás");
		southPanel = new JPanel();
		b.addActionListener(new AddListener());
		teamHome = new JTextField("", 20);
		teamAway = new JTextField("", 20);
		result = new JTextField("NA", 5);
		oddsHome = new JTextField("", 5);
		oddsDraw = new JTextField("", 5);
		oddsAway = new JTextField("", 5);
		southPanel.add(new JLabel("Hazai:"));
		southPanel.add(teamHome);
		southPanel.add(new JLabel("Vendég:"));
		southPanel.add(teamAway);
		southPanel.add(new JLabel("Eredmény:"));
		southPanel.add(result);
		southPanel.add(new JLabel("Odds H:"));
		southPanel.add(oddsHome);
		southPanel.add(new JLabel("Odds X:"));
		southPanel.add(oddsDraw);
		southPanel.add(new JLabel("Odds V:"));
		southPanel.add(oddsAway);
		southPanel.add(b);
		result.setEditable(false);
		add(southPanel, BorderLayout.SOUTH);
	}

	/**
	 * A menüt inicializálja, amelyben található a segítség dialógusablak.
	 */
	
	public void setMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Eszközök");
		menuBar.add(menu);
		setJMenuBar(menuBar);
		JMenuItem help = new JMenuItem("Segítség", KeyEvent.VK_N);
		help.addActionListener(new HelpListener());
		menu.add(help);
	}

	class AddListener implements ActionListener {
		
		/**
		 * Ez a gomb segítségével lehet meccset hozzáadni.
		 */
		
		public void actionPerformed(ActionEvent ae) {
			data.addFootballMatch(teamHome.getText(), teamAway.getText(), result.getText(),
					Double.parseDouble(oddsHome.getText()), Double.parseDouble(oddsDraw.getText()),
					Double.parseDouble(oddsAway.getText()));
		}
	}
	
	class SaveButtonActionListener implements ActionListener {
		
		/**
		 * Frissíti a táblázatot.
		 */
		
		public void actionPerformed(ActionEvent ae) {
			data.fireTableDataChanged();
		}
	}

	/**
	 * A táblázat elemei közül kiválogatja, amelyikhez nincs, valamint amelyikekhez van eredmény beírva. Ezeket
	 * két külön táblázatba rakja.
	 */
	
	public void setRowSorterByResult() {
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(t.getModel());
		sorter.setRowFilter(RowFilter.regexFilter("[a-zA-Z]", 2));
		t.setRowSorter(sorter);

		TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(t2.getModel());
		sorter2.setRowFilter(RowFilter.regexFilter("[0-9]", 2));
		t2.setRowSorter(sorter2);
	}

	/**
	 * A focimeccseket olvassa be és írja ki fájlba szerializálással.
	 */
	
	@SuppressWarnings("unchecked")
	public void setFileReadClose() {
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
	
	public class HelpListener implements ActionListener {
		
		/**
		 * A segítség dialógusablakot nyitja meg.
		 */
		
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(itself, "Meccs hozzáadása: Mezõk kitöltése az ablak alsó felében, majd Hozzáadás gomb.\nEredmény beírása: Eredmény mezõben ráklikkelni az NA-ra és eredmény beírása X-Y formátumban, majd Mentés gomb.", "Segítség", JOptionPane.PLAIN_MESSAGE);
		}
	}

}
