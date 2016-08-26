package Player;
import Format.*;
import BanGet.*;

public class SimulateMatch{
	private final static int simNumbers=10000;

	public static double simulateMatch(Player playerOne, Player playerTwo, Format f){
		int winCount=0;
		Player winner;
		double winRate;

		//Use f to play them a bunch, see who wins, keep track, return result

		for(int i=0; i<simNumbers; i++){
			winner = f.play(playerOne, playerTwo);
			if(winner.equals(playerOne)){
				winCount++;
			}
		} 
		winRate = winCount/simNumbers;
		System.out.println("Player One won " +winCount+ " out of " +simNumbers+ " matches, for a winrate of " +winRate+ ".");
		System.out.println("Player Two won " +(simNumbers-winCount)+ " out of " +simNumbers+ " matches, for a winrate of " +(1.0-winRate)+ ".");
	
		//return winrate of playerOne. playerTwo would be 1-winrate of playerOne.
		return winRate;
	}


}
