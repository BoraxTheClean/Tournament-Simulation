package Player;
import java.util.*;
public class Match{
	public Player winner;
	public Player loser;
	public List<Game> games;
	
	public Match(Player winner, Player loser, List<Game> games){
		this.winner = winner;
		this.loser = loser;
		this.games = games;
	}

}
