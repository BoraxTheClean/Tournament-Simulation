package Testing;
import Tournaments.*;
import Player.*;
import Format.*;
import java.util.*;
import java.io.*;
import BanGet.*;
//import org.junit.*;

public class SwissTest{
	List<Deck> decks=new ArrayList<>();
	List<Player> players=new ArrayList<>();
	int simulations=500;
	int numPlayers=100;
	public void getDecks() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("resources//vs_data_ungoro.csv");
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String name=values[0];
			int id=Integer.valueOf(values[1]);
			Map<Integer,Float> map=new HashMap<>();
			Deck d=new Deck(name,map,id);
			for(int i=2;i<values.length;i++){
				map.put(i-1,Float.valueOf(values[i]));
			}
			decks.add(d);
		}
	}
	
	public void getDecksFromHsReplay() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("resources//hs_replay_may_5legend.csv");
		Scanner scan=new Scanner(f);
		Map<String, Integer> deckIds = loadHsReplayDeckIds();
		Map<String, Deck> deckNames = new HashMap<String, Deck>();
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String name=values[0];
			String opponent = values[1];
			Float winrate = Float.valueOf(values[3])/100;
			int id = deckIds.get(name);
			if (!deckNames.containsKey(name)) {
				Map<Integer,Float> map=new HashMap<>();
				Deck d=new Deck(name,map,id);
				deckNames.put(name, d);
			}
			Deck deck = deckNames.get(name);
			deck.matchups.put(deckIds.get(opponent), winrate);
			
			
		}
		decks.addAll(deckNames.values());
	}
	
	public Map<String, Integer> loadHsReplayDeckIds() {
		Map<String, Integer> deckIds = new HashMap<String, Integer>();
		deckIds.put("Midrange Hunter", 1);
		deckIds.put("Pirate Warrior", 2);
		deckIds.put("Taunt Warrior", 3);
		deckIds.put("Crystal Rogue", 4);
		deckIds.put("Miracle Rogue", 5);
		deckIds.put("Murloc Paladin", 6);
		deckIds.put("Gunther Mage", 7);
		deckIds.put("Midrange Paladin", 8);
		deckIds.put("Token Druid", 9);
		deckIds.put("Elemental Shaman", 10);
		deckIds.put("Miracle Priest", 11);
		deckIds.put("Freeze Mage", 12);
		deckIds.put("Silence Priest", 13);
		deckIds.put("Dragon Priest", 14);
		deckIds.put("Zoo Warlock", 15);
		deckIds.put("Control Paladin", 16);
		deckIds.put("Ramp Druid", 17);
		deckIds.put("Jade Druid", 18);
		deckIds.put("Jade Shaman", 19);
		deckIds.put("Murloc Shaman", 20);
		deckIds.put("Handlock", 21);
		deckIds.put("Token Shaman", 22);
		deckIds.put("Water Rogue", 23);
		deckIds.put("Secret Mage", 24);
		deckIds.put("Tempo Mage", 25);
		deckIds.put("Control Shaman", 26);
		deckIds.put("Control Priest", 27);
		return deckIds;
	}
	
	public void getPlayers() throws FileNotFoundException{
		File f=new File("resources//players_list_ungoro_sorted.csv");
		f.canRead();
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String name=values[1];
			int id=Integer.valueOf(values[0]);
			List<Deck> decks=new LinkedList<>();
			for(int i=2;i<values.length;i++){
				decks.add(this.decks.get(Integer.valueOf(values[i])-1));
			}
			Player p=new Player(name,decks,id);
			players.add(p);
		}
	}
	
	public void runTournament(){
		Results r=new Results();
		Format f=new LastHeroStanding();
		f.setBP(new NashBanLHS());
		Swiss tournament=new Swiss(players,f,8,4);
		for(int i=1;i<numPlayers+1;i++){
			r.setPlayerMap(i);
		}
	//	testBracket(tournament.getBracket());
		tournament.setResults(r);
		for(int i=0;i<simulations;i++){
			tournament.processRounds();
			tournament.getResults();
			tournament.setup();
			//tournament.resetHistory();
		}
		r.outputPlayerRankings(simulations, 8);
	}
	public static void main(String[] args) throws FileNotFoundException{
		SwissTest s=new SwissTest();
		s.getDecksFromHsReplay();
		s.getPlayers();
		s.runTournament();
		
	}

}
