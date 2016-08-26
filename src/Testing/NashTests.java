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
import Format.Format;
import Format.LastHeroStanding;
import Player.Deck;
import Player.Player;
import Tournaments.Results;
import Tournaments.Swiss;

public class NashTests {
	List<Deck> decks=new ArrayList<>();
	List<Player> players=new ArrayList<>();
	int simulations=10000;
	int numPlayers=8;
	NashBanLHS nb=new NashBanLHS();
	public void setup() throws FileNotFoundException{
		getDecks();
		getPlayers();
	}
	public void getDecks() throws FileNotFoundException{
		System.out.println(System.getProperty("user.dir"));
		File f=new File("break_miracle.csv");
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
	public void getPlayers() throws FileNotFoundException{
		File f=new File("Players.csv");
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
		tournament.setResults(r);
		for(int i=0;i<simulations;i++){
			tournament.processRounds();
			tournament.getResults();
			tournament.setup();
		}
		r.outputPlayerRankings(simulations, 2);
		assert(true);
	}
	public void testBans(){
		int i=0;
		for(Player p:players){
			for(Player p2: players){
				if(p.equals(p2))
					continue;
					System.out.println(i++);
					System.out.println(nb.getBan(p, p2).name);
					System.out.println(nb.getBan(p2, p).name);
			}
		}
	}
	public static void main(String[] args) throws FileNotFoundException{
		NashTests s=new NashTests();
		s.getDecks();
		s.getPlayers();
		//s.runTournament();
		s.testBans();
	}
}
