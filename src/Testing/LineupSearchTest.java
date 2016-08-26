package Testing;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import BanGet.NashBan;
import BanGet.NashBanLHS;
import Format.Conquest;
import Format.Format;
import Format.LastHeroStanding;
import Player.Deck;
import Player.Player;
import Tools.LineupTools;

public class LineupSearchTest {
	Map<Integer, Deck> decks=new HashMap<Integer, Deck>();
	Map<String, Deck> deckNames = new HashMap<String, Deck>();
	List<Player> players=new ArrayList<>();
	List<Deck> listOfDecks=new ArrayList<>();
	String replayPlayers = "resources//austin//best_decks_morning.csv";
	String vSPlayers = "resources//valencia/dh_valencia_vs_ids.csv";
	String fieldOutputFile = "output//best_output_morning.csv";
	
	public void getDecksFromVs() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("resources//valencia//vs_data_atlanta.csv");
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
			decks.put(d.id, d);
		}
	}
	public void getDecksFromHsReplay() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("..//resources//may_withshaman.csv");
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
		Map<Integer, String> inverted = invertIds(deckIds);
		for (int i = 1; i <= deckIds.keySet().size(); i++) {
			if (deckNames.containsKey(inverted.get(i))) {
				decks.put(i, deckNames.get(inverted.get(i)));
			}
		}
	}
	
	public float fixWinrate(float winrate, String name1, String name2) {
		float newWinrate = winrate;
		if (name1.equals("Pirate Warrior")) {
			newWinrate=newWinrate+0.0075f;
		}
		if (name2.equals("Pirate Warrior")) {
			newWinrate=newWinrate-0.0075f;
		}
		if (name1.equals("Aggro Druid")) {
			newWinrate=newWinrate+0.012f;
		}
		if (name2.equals("Aggro Druid")) {
			newWinrate=newWinrate-0.012f;
		}
		
		return newWinrate;
	}
	
	public void getDecksAndIdsFromHsReplay() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("resources//austin//austin_5k_24h_sunday.csv");
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			Integer idOne=Integer.valueOf(values[0]);
			String nameOne=values[1];
			Integer idTwo=Integer.valueOf(values[2]);
			String nameTwo=values[3];
			Float winrate = Float.valueOf(values[4])/100;
			if (!deckNames.containsKey(nameOne)) {
				Map<Integer,Float> map=new HashMap<>();
				Deck d=new Deck(nameOne,map,idOne);
				deckNames.put(nameOne, d);
			}
			Deck deckOne = deckNames.get(nameOne);
			deckOne.matchups.put(idTwo, winrate);
			if (!deckNames.containsKey(nameTwo)) {
				Map<Integer,Float> map=new HashMap<>();
				Deck d=new Deck(nameTwo,map,idTwo);
				deckNames.put(nameTwo, d);
			}
			Deck deckTwo = deckNames.get(nameTwo);
			deckTwo.matchups.put(idOne, 1.0f-winrate);
			
			
		}
		for (Deck d: deckNames.values()) {
			decks.put(d.id, d);
		}
		scan.close();

	}
	
	public void getDecksFromMegaman() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("resources//austin//austin_5k_24h_sunday.csv");
		Scanner scan=new Scanner(f);
		String firstLine = scan.nextLine();
		String[] decks = firstLine.split(",");
		for (int i=1; i < decks.length; i++) {
			String deckName = decks[i];
			Map<Integer,Float> map=new HashMap<>();
			Deck d = new Deck(deckName, map, i);
			listOfDecks.add(d);
			deckNames.put(deckName, d);
		}
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String deckname = values[0];
			Deck ourDeck = deckNames.get(deckname);
			for (int i=1; i < values.length; i++) {
				if (values[i].equals("")) {
					continue;
				}
				Deck oppDeck = listOfDecks.get(i);
				Float winrate = Float.valueOf(values[i])/100;
				ourDeck.matchups.put(oppDeck.id, winrate);
				oppDeck.matchups.put(ourDeck.id, 1.0f-winrate);
				
			}
		}
		scan.close();

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
		deckIds.put("Hand Warlock", 21);
		deckIds.put("Token Shaman", 22);
		deckIds.put("Water Rogue", 23);
		deckIds.put("Secret Mage", 24);
		deckIds.put("Tempo Mage", 25);
		deckIds.put("Control Shaman", 26);
		deckIds.put("Control Priest", 27);
		deckIds.put("Token Shaman", 28);
		deckIds.put("Evolve Shaman", 29);
		return deckIds;
	}
	
	public Map<Integer, String> invertIds (Map<String, Integer> ids) {
		Map<Integer, String> inverted = new HashMap<Integer, String>();
		for (Map.Entry<String, Integer> entry: ids.entrySet()) {
			inverted.put(entry.getValue(), entry.getKey());
		}
		return inverted;
	}
	
	


	public void getPlayers() throws FileNotFoundException{
		File f=new File(replayPlayers);
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String name=values[1];
			int id=Integer.valueOf(values[0]);
			List<Deck> decks=new LinkedList<>();
			for(int i=2;i<values.length;i++){
				if (!this.decks.containsKey(Integer.valueOf(values[i]))) {
					System.out.println(Integer.valueOf(values[i]));
					throw new RuntimeException();
				}
				decks.add(this.decks.get(Integer.valueOf(values[i])));
			}
			Player p=new Player(name,decks,id);
			players.add(p);
		}
		scan.close();

	}
	
	public void getPlayersStrings() throws FileNotFoundException{
		File f=new File(replayPlayers);
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String name=values[1];
			int id=Integer.valueOf(values[0]);
			List<Deck> decks=new LinkedList<>();
			for(int i=2;i<values.length;i++){
				if (!this.deckNames.containsKey(values[i])) {
					System.out.println(values[i]);
					throw new RuntimeException();
				}
				decks.add(this.deckNames.get(values[i]));
			}
			Player p=new Player(name,decks,id);
			players.add(p);
		}
		scan.close();

	}
	
	public void testSearch(){
		listOfDecks.addAll(decks.values());
		Format f=new Conquest();
		f.setBP(new NashBan());
		LineupTools.searchLineupSpaceOverTournament(listOfDecks, players, f, 7, 6, 4, 10);
	}
	
	public void testLHSFieldSearch() throws FileNotFoundException {
		listOfDecks.addAll(decks.values());
		removeUnwantedDecks();
		Format f=new LastHeroStanding();
		f.setBP(new NashBanLHS());
		LineupTools.searchLineupSpaceOverField(listOfDecks, players, f, 4, fieldOutputFile);
	}
	
	public void removeUnwantedDecks() {
		Set<String> wantedNames = new HashSet<String>();
		wantedNames.add("Devilsaur Druid");
		wantedNames.add("Token Druid");
		wantedNames.add("Taunt Druid");
		wantedNames.add("Spiteful Druid");
		wantedNames.add("Spell Hunter");
		wantedNames.add("Recruit Hunter");
		wantedNames.add("Tempo Mage");
		wantedNames.add("Bigspell Mage");
		//wantedNames.add("Freeze Mage");
		wantedNames.add("Murloc Paladin");
		wantedNames.add("Odd Paladin");
		wantedNames.add("Control Priest");
		//wantedNames.add("Combo Priest");
		wantedNames.add("Quest Priest");
		wantedNames.add("Odd Rogue");
		wantedNames.add("Miracle Rogue");
		wantedNames.add("Quest Rogue");
		//wantedNames.add("Tempo Rogue");
		wantedNames.add("Even Shaman");
		wantedNames.add("Shudderwock Shaman");
		wantedNames.add("Even Warlock");
		wantedNames.add("Control Warlock");
		wantedNames.add("Quest Warrior");
		wantedNames.add("Cube Warlock");
		//wantedNames.add("Recruit Warrior");
		//wantedNames.add("Control Warrior");
		//wantedNames.add("Odd Warrior");
		
		
		List<Deck> newDecks = new ArrayList<Deck>();
		for (Deck deck: listOfDecks) {
			if (wantedNames.contains(deck.name)) {
				newDecks.add(deck);
			}	
		}
		listOfDecks = newDecks;
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		LineupSearchTest l=new LineupSearchTest();
		l.getDecksAndIdsFromHsReplay();
		l.getPlayersStrings();
		l.testLHSFieldSearch();
	}
	
}
