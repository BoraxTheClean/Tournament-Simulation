package Tournaments;
import Player.*;
import java.util.*;
public class Results{
	/*
	"I would have stored data on each player, their lineup, their path through the bracket including match/game results, and their finish
Tracking lineup: A trie structure would work best for retrieval of data.  But this is not efficient for creation and updating.  We could have a list of deck id's, then we would need to make sure these are normalized (sorted) so they would all hash to the same bucket.  We can't have [1,2,3] and [2,1,3].
Need to be able to index by player id and by placement in tournament as well.  Each of these should be done with maps, so we will need the following maps:
Player Placement Map<Place in tournament,Map<Players id,# of times finished>>
Player id Map<Player id,Map<Place,times>>
Player path Map<Player id, List<List<Matches>><
Player path Map<Place in tournament, List<List<Matches>>
and you could index by player ID and also provide an index by placement
so you could ask, what were the top 8 lineups
or did player 112 make top 8
or how many players brought zoo and how many players with zoo made top 8
"
	*/
	private Map<Integer,Integer> simpleResults=new HashMap<>();
	//Placement in a tournament P->(Player-># of times placed at P)
	private Map<Integer,HashMap<Integer,Integer>> placementMap=new HashMap<>();
	//Player id->(Placement in tourney P-># of times placed at P)
	private Map<Integer,HashMap<Integer,Integer>> playerMap=new HashMap<>();
	//Player id->List of matche histories of player in tournaments.
	private Map<Integer,List<List<Match>>> playerIDPath=new HashMap<>();
	//Placement in Tourmanet P->List of match histories players that placed at P
	private Map<Integer,List<List<Match>>> playerPlacementPath=new HashMap<>();
	//Placement in tournament->List of players who placed there (in one simulation)
	private Map<Integer,List<Player>> results;
	//Player id->Place in tournament
	private Map<Integer,Integer> playerRankings;
	//Player id->Player object
	private Map<Integer,Player> players=new HashMap<>();
	//Player id->list of bottom bracket node of tournament.
	private Map<Integer,List<BracketNode>> bracketPath=new HashMap<>();
	/*
		The idea here is to have the maps be set with the data I want
		so I'll never have to insert into the maps, all the buckets
		will be there on initialization.
	*/
	public Map<Integer,Player> getPlayers(){
		return players;
	}
	public Map<Integer,Integer> getPlayerRankings(){
		return playerRankings;
	}
	public Map<Integer,HashMap<Integer,Integer>> getPlayerMap(){
		return playerMap;
	}
	public Map<Integer,HashMap<Integer,Integer>> getPlacementMap(){
		return placementMap;
	}
	public Map<Integer,List<List<Match>>> getPlacementPathMap(){
		return playerPlacementPath;
	}
	public Map<Integer,List<BracketNode>> getBracketMap(){
		return bracketPath;
	}
	public void setPlayers(Player p){
		players.put(p.id,p);
	}	
	public void setPlacementMap(int i){
		if(placementMap.get(i)==null)
			placementMap.put(i,new HashMap<Integer,Integer>());
	}
	public void setPlayerMap(int i){
		if(playerMap.get(i)==null)
			playerMap.put(i,new HashMap<Integer,Integer>());
	}
	public void setPlayerPathMap(int i){
		if(playerIDPath.get(i)==null)
			playerIDPath.put(i,new LinkedList<List<Match>>());
	}
	public Map<Integer,List<List<Match>>> getPlayerPathMap(){
		return playerIDPath;
	}
	public void setPlacementPathMap(int i){
		if(playerPlacementPath.get(i)==null)
			playerPlacementPath.put(i,new LinkedList<List<Match>>());
	}
	public void setBracketPath(int i){
		if(bracketPath.get(i)==null)
			bracketPath.put(i, new LinkedList<BracketNode>());
	}
	/*
		I think I want to update the maps with data using a 
		Map<Ranking,List<Player>> thus I can just iterate over the keyset
		of the stored maps.  This reduces my time to the space used by the
		objects in this class.
	*/

	public void updateMaps(Map<Integer,List<Player>> map,Map<Integer,Integer> playerRankings,Map<Integer,BracketNode> brackets){
		results=map;
		this.playerRankings=playerRankings;
		updatePlacementMap();
		updatePlayerMap();
		updatePlacementPathMap();
		updatePlayerPathMap();
		updateBracketMap(brackets);
	}

	public void updateMaps(Map<Integer,List<Player>> map,Map<Integer,Integer> playerRankings){
		this.results=map;	
		this.playerRankings=playerRankings;
		updatePlacementMap();
		//updatePlacementPathMap();
		updatePlayerMap();
		//updatePlayerPathMap();
	}


	public void updateBracketMap(Map<Integer,BracketNode> map){
		for(Integer i:bracketPath.keySet()){
			if(map.get(i)!=null){
				List<BracketNode> list=bracketPath.get(i);
				list.add(map.get(i));
			}
		}
	}
	public void updatePlacementMap(){
		for(Integer i: placementMap.keySet()){
			if(results.get(i)!=null){
				Map<Integer,Integer> map=placementMap.get(i);
				for(Player p: results.get(i)){
					int id=p.id;
					if(map.get(id)!=null)
						map.put(id,map.get(id)+1);
					else
						map.put(id,1);
				}
			}
		}		
	}
	public void updatePlayerMap(){
		for(Integer i: playerMap.keySet()){
			Map<Integer,Integer> map=playerMap.get(i);
			int rank=playerRankings.get(i);
			if(map.get(rank)==null)
				map.put(rank,1);
			else
				map.put(rank,map.get(rank)+1);
		}
	}
	public void updatePlacementPathMap(){
		for(Integer i: playerPlacementPath.keySet()){
			List<List<Match>> list=playerPlacementPath.get(i);
			if(results.containsKey(i)){
				for(Player p: results.get(i)){	
					if(list==null){
						list=new LinkedList<List<Match>>();
						list.add(p.history);
					}else{
						list.add(p.history);
					}
				}
			}
		}
	}
	public void updatePlayerPathMap(){
		for(Integer i: playerIDPath.keySet()){
			List<List<Match>> list=playerIDPath.get(i);
			if(list==null)
				list=new LinkedList<List<Match>>();
			list.add(players.get(i).history);
			
		}
	}
	public void outputPlayerRankings(int numberSimulations, int cutoff){
		for(Integer i:playerMap.keySet()){
			Player p=players.get(i);
			Map<Integer,Integer> map=playerMap.get(i);
			int cutoffFinishes = 0;
			for(Integer i2:map.keySet()){
				if(i2<=cutoff)
					cutoffFinishes++;
					
			}
			System.out.println(p.name+" Finished in top cut "+cutoffFinishes+"  times.");
		}
	}
	public void updateSimpleResults(List<Player> lis,int cutoff){
		for(int i=0;i<cutoff;i++){
			Player p=lis.get(i);
			if(simpleResults.get(p.id)==null)
				simpleResults.put(p.id,1);
			else
				simpleResults.put(p.id, simpleResults.get(p.id)+1);
		}
	}
	public void getTopCut(int simulations){
		for(Integer i:simpleResults.keySet()){
			if(i==0)
				System.out.println("Player with id "+i+" Made the top cut "+simpleResults.get(i)+" times out of "+simulations+".");
		}
	}
}
