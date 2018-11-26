import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class FootballData extends AbstractTableModel {

	List<Football> footballMatch = new ArrayList<Football>();

	public Object getValueAt(int rowIndex, int columnIndex) {
		Football match = footballMatch.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return match.getTeamHome();
		case 1:
			return match.getTeamAway();
		case 2:
			return match.getResult();
		case 3:
			return match.getOddsHome();
		case 4:
			return match.getOddsDraw();
		default:
			return match.getOddsAway();
		}
	}

	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Hazai";
		case 1:
			return "Vendég";
		case 2:
			return "Eredmény";
		case 3:
			return "H";
		case 4:
			return "X";
		default:
			return "V";
		}
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public int getRowCount() {
		return footballMatch.size();
	}

	public boolean isCellEditable(int row, int col) {
		if (col == 2)
			return true;
		return false;
	}

	public void addFootballMatch(String teamHome, String teamAway, String result, double oddsHome, double oddsDraw,
			double oddsAway) {
		footballMatch.add(new Football(teamHome, teamAway, result, oddsHome, oddsDraw, oddsAway));
		fireTableRowsInserted(0, footballMatch.size() - 1);
	}

	public void setValueAt(Object aValue, int row, int column) {
		if (column == 2) {
			footballMatch.get(row).setResult((String) aValue);
		}
	}
}
