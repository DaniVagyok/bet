import java.io.Serializable;

public class Bet implements Serializable {

	private String teamHome;
	private String teamAway;
	private char choice;
	private double odds;

	public Bet(String teamHome, String teamAway, char choice, double odds) {
		this.teamHome = teamHome;
		this.teamAway = teamAway;
		this.choice = choice;
		this.odds = odds;
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

	public char getChoice() {
		return choice;
	}

	public void setChoice(char choice) {
		this.choice = choice;
	}

	public double getOdds() {
		return odds;
	}

	public void setOdds(double odds) {
		this.odds = odds;
	}

}
