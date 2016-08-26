package Tournaments;
import java.lang.reflect.Array;
import java.util.*;

import Format.Format;
import Player.Player;
public class Swiss implements Tournament{
	List<Player> players;
	Format format;
	int numRounds;
	int cutoff;
	Map<Integer, List<Player>> standings;
	Results results;
	
	public Swiss(List<Player> players, Format format, int numRounds, int cutoff) {
		this.format = format;
		this.players=players;
		setup();
		this.numRounds = numRounds;
		this.cutoff = cutoff;
	}
	
	@Override
	public void processRounds() {
		Player holdoverPlayer = null;
		for (int i = 0; i < numRounds; i++) {
			Map<Integer, List<Player>> newStandings = new HashMap<Integer, List<Player>>();
			for (int j = i; j>=0; j--) {
				List<Player> currentPlayers = null;
				if(standings.get(j)==null){
					continue;
				}
				if (standings.get(j).size()%2==1) {
					if (holdoverPlayer==null) {
						currentPlayers = standings.get(j).subList(0, standings.get(j).size()-1);
						holdoverPlayer = standings.get(j).get(standings.get(j).size()-1);
					}
					else {
						currentPlayers = standings.get(j);
						currentPlayers.add(holdoverPlayer);
						holdoverPlayer = null;
				}
					
			}
				else {
					if (holdoverPlayer==null) {
						currentPlayers = standings.get(j);
					}
					else {
						currentPlayers = new ArrayList<Player>();
						currentPlayers.addAll(standings.get(j).subList(0, standings.get(j).size()-1));
						currentPlayers.add(holdoverPlayer);
						holdoverPlayer = standings.get(j).get(standings.get(j).size()-1);
					}
				}
				int numMatches = currentPlayers.size()/2;
				for (int k=0; k<numMatches; k++) {
					Player player1 = currentPlayers.get(k);
					Player player2 = currentPlayers.get(k+numMatches);
					Player winner = format.play(player1, player2);
					Player loser = (winner.id==player1.id ? player2 : player1);
					if (!newStandings.containsKey(j)) {
						newStandings.put(j, new ArrayList<Player>());
					}
					newStandings.get(j).add(loser);
					if (!newStandings.containsKey(j+1)) {
						newStandings.put(j+1, new ArrayList<Player>());
					}
					newStandings.get(j+1).add(winner);
				}			
			}
		standings=newStandings;
	}
		
	}
	public void setResults(Results r){
		results=r;
		for(Player p: players)
			r.setPlayers(p);
	}
	@Override
	public void getResults() {
		results.updateSimpleResults(simpleResults(), cutoff);
	}
	public List<Player> simpleResults(){
		Map<Integer,List<Player>> retVal=new HashMap<>();
		for(Player p: players){
			p.computeTiebreaker();
		}
		Collections.sort(players);
		Collections.reverse(players);
		return players;
	}
	public Map<Integer,List<Player>> getFinalStandings(){
		Map<Integer,List<Player>> retVal=new HashMap<>();
		for(Player p: players){
			p.computeTiebreaker();
		}
		Collections.sort(players);
		Collections.reverse(players);
		int rank=1;
		for(Player p: players){
			List<Player> hack=new LinkedList<>();
			hack.add(p);
			retVal.put(rank++,hack);
		}
		return retVal;
	}


	public Map<Integer,Integer> getPlayerRankings(Map<Integer,List<Player>> finalStandings){
		Map<Integer,Integer> retVal=new HashMap<>();
		for(Integer i:finalStandings.keySet()){
			for(Player p:finalStandings.get(i)){
				retVal.put(p.id,i);
			}
		}
		return retVal;
	}
	@Override
	public void setup() {
		Map<Integer, List<Player>> initialStandings = new HashMap<Integer, List<Player>>();
		
		List<Player> shuffleable = new LinkedList<Player>(players);
		for(Player p: shuffleable){
			p.resetTiebreaker();
		}
		Collections.shuffle(shuffleable);
		initialStandings.put(0, shuffleable);
		standings = initialStandings;
		
		
	}

}
