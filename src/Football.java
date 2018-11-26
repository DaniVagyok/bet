import java.io.Serializable;

public class Football implements Serializable {

	private String teamHome;
	private String teamAway;
	private String result;
	private double oddsHome;
	private double oddsDraw;
	private double oddsAway;

	public Football(String teamHome, String teamAway, String result, double oddsHome, double oddsDraw,
			double oddsAway) {
		this.teamHome = teamHome;
		this.teamAway = teamAway;
		this.result = result;
		this.oddsHome = oddsHome;
		this.oddsDraw = oddsDraw;
		this.oddsAway = oddsAway;
	}

	public String getTeamHome() {
		return teamHome;
	}

	public void setTeamHome(String teamHome) {
		this.teamHome = teamHome;
	}

	public String getTeamAway() {
		return teamAway;
	}

	public void setTeamAway(String teamAway) {
		this.teamAway = teamAway;
	}

	public double getOddsHome() {
		return oddsHome;
	}

	public void setOddsHome(double oddsHome) {
		this.oddsHome = oddsHome;
	}

	public double getOddsDraw() {
		return oddsDraw;
	}

	public void setOddsDraw(double oddsDraw) {
		this.oddsDraw = oddsDraw;
	}

	public double getOddsAway() {
		return oddsAway;
	}

	public void setOddsAway(double oddsAway) {
		this.oddsAway = oddsAway;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
