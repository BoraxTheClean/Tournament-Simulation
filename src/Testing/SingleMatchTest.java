package Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import BanGet.NashBan;
import BanGet.NashBanLHS;
import Format.Conquest;
import Format.Format;
import Format.LastHeroStanding;
import Player.Deck;
import Player.Player;

public class SingleMatchTest {
	
	Map<Integer, Deck> decks=new HashMap<Integer, Deck>();
	List<Player> players=new ArrayList<>();
	Map<String, Deck> deckNames = new HashMap<String, Deck>();
	String deckData = "resources//austin//austin_5k_24h_sunday.csv";
	String playerData = "resources//austin//single_player_test.csv";
	
	public void getDecksFromHsReplay() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File(deckData);
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
		scan.close();
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
	
	public void getPlayersStrings() throws FileNotFoundException{
		File f=new File(playerData);
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
		deckIds.put("Big Druid", 17);
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
		deckIds.put("Midrange Shaman", 28);
		deckIds.put("Quest Mage", 29);
		deckIds.put("Secret Hunter", 30);
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
		File f=new File(playerData);
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()){
			String line=scan.nextLine();
			String[] values=line.split(",");
			String name=values[1];
			int id=Integer.valueOf(values[0]);
			List<Deck> decks=new LinkedList<>();
			for(int i=2;i<values.length;i++){
				if (!this.decks.containsKey(Integer.valueOf(values[i]))) {
					throw new RuntimeException();
				}
				decks.add(this.decks.get(Integer.valueOf(values[i])));
			}
			Player p=new Player(name,decks,id);
			players.add(p);
		}
		scan.close();

	}
	
	public void runMatch() {
		Format f = new LastHeroStanding();
		f.setBP(new NashBanLHS());
		Player playerOne = players.get(0);
		Player playerTwo = players.get(1);
		Player winner = f.play(playerOne, playerTwo);
	}
	
	public void runMatchConquest() {
		Conquest f = new Conquest();
		f.setBP(new NashBan());
		Player playerOne = players.get(0);
		Player playerTwo = players.get(1);
	}
	
	public void runMatches() {
		Format f = new LastHeroStanding();
		f.setBP(new NashBanLHS());
		for (int i=0; i <players.size(); i++) {
			Player playerOne = players.get(i);
			int wins = 0;
			for (int j = 0; j<players.size(); j++) {
				if (j==i) {
					continue;
				}
				Player playerTwo = players.get(j);
				for (int k = 0; k<5000; k++) {
					Player winner = f.play(playerOne, playerTwo);
					if (winner.id==playerOne.id) {
						wins++;
					}
				}
				
			}
			double winsdouble = (double) wins;
		}
		
	}
	
	public void runMatchesEquities() {
		Format f = new LastHeroStanding();
		f.setBP(new NashBanLHS());
		for (int i=0; i <players.size(); i++) {
			Player playerOne = players.get(i);
			double winrates = 0;
			for (int j = 0; j<players.size(); j++) {
				if (j==i) {
					continue;
				}
				Player playerTwo = players.get(j);
				winrates = winrates+f.getEquityBeforeBan(playerOne, playerTwo);
				
			}
		}
		
	}
	
	public void runMatchesConquest() {
		Format f = new Conquest();
		f.setBP(new NashBan());
		for (int i=0; i <players.size(); i++) {
			Player playerOne = players.get(i);
			int wins = 0;
			for (int j = 0; j<players.size(); j++) {
				if (j==i) {
					continue;
				}
				Player playerTwo = players.get(j);
				for (int k = 0; k<1000; k++) {
					Player winner = f.play(playerOne, playerTwo);
					if (winner.id==playerOne.id) {
						wins++;
					}
				}
				
			}
			double winsdouble = (double) wins;
		}
		
	}
	
	public void runCandidateConquest() {
		Format f = new Conquest();
		f.setBP(new NashBan());
		Player playerOne = players.get(0);
		int wins = 0;
		for (int j = 1; j<players.size(); j++) {
			Player playerTwo = players.get(j);
			for (int k = 0; k<1000; k++) {
				Player winner = f.play(playerOne, playerTwo);
				if (winner.id==playerOne.id) {
					wins++;
				}
			}

		}
		double winsdouble = (double) wins;
		
		
	}
	
	public void runCandidate() {
		Format f = new LastHeroStanding();
		f.setBP(new NashBanLHS());
		Player playerOne = players.get(0);
		int wins = 0;
		for (int j = 1; j<players.size(); j++) {
			Player playerTwo = players.get(j);
			for (int k = 0; k<1000; k++) {
				Player winner = f.play(playerOne, playerTwo);
				if (winner.id==playerOne.id) {
					wins++;
				}
			}

		}
		double winsdouble = (double) wins;
		
		
	}
	
	public void runCandidateConquestEquities() {
		Conquest f = new Conquest();
		f.setBP(new NashBan());
		Player playerOne = players.get(0);
		double equities = 0;
		for (int j = 1; j<players.size(); j++) {
			Player playerTwo = players.get(j);
			equities = equities + f.getEquityBeforeBan(playerOne, playerTwo);

			}

		
		
		
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		SingleMatchTest s=new SingleMatchTest();
		s.getDecksAndIdsFromHsReplay();
		s.getPlayersStrings();
		//s.runMatches();
		s.runCandidate();
	}

}
