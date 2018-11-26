import java.util.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class BetData extends AbstractTableModel {

	List<Bet> betList = new ArrayList<Bet>();

	/**
	 * Az AbstractTableModel miatt implemetálni kell, itt adom meg az oszlopok neveit.
	 */
	
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Hazai";
		case 1:
			return "Vendég";
		case 2:
			return "Választott";
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
	 * Fogadást lehet hozzáadni, majd frissíti is egybõl a táblázatot.
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
	 * Az AbstractTableModel miatt implementálni kell. Ez a metódus tölti fel a tblázatba a megfelelõ értékeket.
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
