package Testing;
import Tournaments.*;
import Player.*;
import Format.*;

import java.util.*;

import org.junit.*;

import java.lang.Runtime;

public class ResultsTests{
	static SingleElim tournament;
	static Results results;
	static int simulations=10000;
	static int size=154;
	@BeforeClass
	public static void setup(){
		Map<Integer,Float> matchUpsOne=new HashMap<>();
                matchUpsOne.put(0,.5f);
                matchUpsOne.put(1,.5f);
                matchUpsOne.put(2,.4f); 
                Map<Integer,Float> matchUpsTwo=new HashMap<>();
                matchUpsTwo.put(0,.5f);
                matchUpsTwo.put(1,.5f);
                matchUpsTwo.put(2,.4f);
                Map<Integer,Float> matchUpsThree=new HashMap<>();
                matchUpsThree.put(0,.6f);
                matchUpsThree.put(1,.6f);
                matchUpsThree.put(2,.5f);
                List<Deck> decks=new LinkedList<>();
                decks.add(new Deck("Deck zero",matchUpsOne,0));
                decks.add(new Deck("Deck one",matchUpsTwo,1));
                decks.add(new Deck("Deck two",matchUpsThree,2));
                List<Player> players=new LinkedList<>();
		results=new Results();
		for(int i=0;i<size;i++){
			Player p=new Player(String.valueOf(i),decks,i);
			results.setPlacementMap(i);
			results.setPlayerMap(i);
			results.setPlayerPathMap(i);
			results.setPlacementPathMap(i);
			results.setPlayers(p);
			results.setBracketPath(i);
			players.add(p);
		}
		tournament=new SingleElim(players,new LastHeroStanding());
		tournament.setResults(results);
		for(int i=0;i<simulations+1;i++){
			tournament.processRounds();
			tournament.getResults();
			tournament.setup();
			tournament.resetHistory();
		}
		Runtime run=Runtime.getRuntime();
		System.out.println(run.totalMemory()-run.freeMemory());
	}
	/* For each getter in results, we need to make sure the maps are properly
	populated.  So we need a test for that.  We hould also verify that the right
	data is placed in the maps, by using the results, playerRankings, and players
	maps in the results object.*/
	@Test
	public void testPlayers(){
		assert(countPlayers()==size);
	}
	public int countPlayers(){
		Map<Integer,Player> players = results.getPlayers();
		return players.size();
	}
	@Test
	public void testPlayerRankings(){
		assert(countRanking()==size);
	}
	public int countRanking(){
		Map<Integer,Integer> playerRankings = results.getPlayerRankings();
		return playerRankings.size();
	}
	/* 
		The sum of the inner Map's values should be equal to size*# of simulations.
	*/
	@Test
	public void testPlacementMap(){
//		Map<Integer,HashMap<Integer,Integer>> map=results.getPlacementMap();
		assert(countPlacementMap()==size);
//		int total=0;
	}
	public int countPlacementMap(){
		Map<Integer,HashMap<Integer,Integer>> placementMap = results.getPlacementMap();
		return placementMap.size();
	}
	/*
		The sum of the inner Map's values should be equal to size*# of simulations.
	*/
	@Test
	public void testPlayerMap(){
		assert(countPlayerMap()==size);
	}
	public int countPlayerMap(){
		Map<Integer,HashMap<Integer,Integer>> playerMap = results.getPlayerMap();
		return playerMap.size();
	}
	/*
		The sum of the List<Match>s (LinkedList<>'s size) should equal size*$ of simulations.
	*/
	@Test
	public void testPlacementPathMap(){
		assert(countPlacementPathMap()==size);
	}
	public int countPlacementPathMap(){
		Map<Integer,List<List<Match>>> placementPathMap = results.getPlacementPathMap();
		return placementPathMap.size();
	}
	/* Added bracketmap obtainable by results. Need test for size*simulation of the lists.*/
	//@Test
	public void testBracketMap(){

	}
	
}
