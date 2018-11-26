import java.util.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class BetData extends AbstractTableModel {

	List<Bet> betList = new ArrayList<Bet>();

	/**
	 * Az AbstractTableModel miatt implemet�lni kell, itt adom meg az oszlopok neveit.
	 */
	
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Hazai";
		case 1:
			return "Vend�g";
		case 2:
			return "V�lasztott";
		default:
			return "Odds";
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return betList.size();
	}

	/**
	 * Fogad�st lehet hozz�adni, majd friss�ti is egyb�l a t�bl�zatot.
	 * 
	 * @param teamHome
	 * @param teamAway
	 * @param choice
	 * @param odds
	 */
		
	public void addBet(String teamHome, String teamAway, char choice, double odds) {
		betList.add(new Bet(teamHome, teamAway, choice, odds));
		fireTableRowsInserted(0, betList.size() - 1);
	}

	/**
	 * Az AbstractTableModel miatt implement�lni kell. Ez a met�dus t�lti fel a tbl�zatba a megfelel� �rt�keket.
	 */
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Bet b = betList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return b.getTeamHome();
		case 1:
			return b.getTeamAway();
		case 2:
			return b.getChoice();
		default:
			return b.getOdds();
		}
	}

}
