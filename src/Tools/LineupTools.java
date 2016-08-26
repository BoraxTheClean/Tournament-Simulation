package Tools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import BanGet.NashBanLHS;
import Format.Format;
import Player.Deck;
import Player.Player;
import Tournaments.Results;
import Tournaments.Swiss;

public class LineupTools{
	static int numOfDecks;
	static List<List<Deck>> lineupCombos=new LinkedList<>();
	private static void comboLineups(List<Deck> decks, List<Deck> currentLineup,int index){
		if(currentLineup.size()==numOfDecks){
			lineupCombos.add(currentLineup);
		}
		for(int i=index;i<decks.size();i++){
			if(!check(currentLineup,decks.get(i))){
				index++;
				continue;
			}
			currentLineup.add(decks.get(i));
			List<Deck> tempList=new LinkedList<>(currentLineup);
			comboLineups(decks,tempList,++index);
			currentLineup.remove(decks.get(i));
		}
	}
	public static boolean check(List<Deck> lis, Deck d){
		String clazz=d.name.split("\\s")[1];
		for(Deck deck:lis){
			String deckClass=deck.name.split("\\s")[1];
			if(clazz.equals(deckClass)){
				return false;
			}
		}
		return true;
	}
	public static List<List<Deck>> getAllLineups(List<Deck> lis,int length){
		if(length>lis.size()){
			System.out.println("You messed up and gave me too few decks.");
			return null;
		}
		numOfDecks=length;
		lineupCombos=new LinkedList<>();
		List<Deck> temp=new LinkedList<>();
		comboLineups(lis,temp,0);
		return lineupCombos;
	}
	public static void searchLineupSpaceOverTournament(List<Deck> lineupSpace,List<Player> players, Format format, int numRounds, int cutoff,int lineupLength,int simulations){
		getAllLineups(lineupSpace,lineupLength);
		for(List<Deck> lineup:lineupCombos){
			print(lineup);
			Player p=new Player("HERO",lineup,0);
			players.add(p);
			Swiss s=new Swiss(players,format,numRounds,cutoff);
			Results r=new Results();
			for(Player player:players){
				r.setPlayerMap(player.id);
			}
			s.setResults(r);
			for(int i=0;i<simulations;i++){
				s.processRounds();
				s.getResults();
				s.setup();
			}
			r.getTopCut(simulations);
			players.remove(p);
			
		}
	}
	
	public static void searchLineupSpaceOverField(List<Deck> lineupSpace,List<Player> players, Format format, int lineupLength, String outfilePath) throws FileNotFoundException{
		getAllLineups(lineupSpace,lineupLength);
		PrintWriter writer = new PrintWriter(outfilePath);
		int numberLineupsRead = 0;
		for(List<Deck> lineup:lineupCombos){
			numberLineupsRead++;
			if (numberLineupsRead%400==0) {
				System.out.println(numberLineupsRead);
			}
			Player p=new Player("HERO",lineup,0);
			double winrates = 0.0;
			for (Player opponent:players) {
				winrates = winrates+format.getEquityBeforeBan(p, opponent);
			}
			winrates=winrates/players.size();
			writer.println(NashBanLHS.decksToSortedNames(lineup) + classColumns(lineup)+ String.valueOf(winrates));
			
		}
		writer.close();
	}
	
	private static String classColumns(List<Deck> decks) {
		List<String> alphabetClasses = new ArrayList<>();
		alphabetClasses.add("Druid");
		alphabetClasses.add("Hunter");
		alphabetClasses.add("Mage");
		alphabetClasses.add("Paladin");
		alphabetClasses.add("Priest");
		alphabetClasses.add("Rogue");
		alphabetClasses.add("Shaman");
		alphabetClasses.add("Warlock");
		alphabetClasses.add("Warrior");
		String output = "";
		String answer;
		for (int i=0; i<9; i++) {
			answer = ",";
			for (Deck deck: decks) {
				if (deck.name.split("\\s")[1].equals(alphabetClasses.get(i))) {
					answer = deck.name.split("\\s")[0] + ",";
				}
			}
			output = output + answer;
		}
		return output;
	}
	private static void print(List<Deck> lis){
		for(Deck d:lis){
			System.out.print(d.name+" ");
		}
		System.out.println();
	}

	public static void main(String[] args){
		List<Deck> test=new ArrayList<>();
		test.add(new Deck(null,null,1));
		test.add(new Deck(null,null,2));
		test.add(new Deck(null,null,3));
		test.add(new Deck(null,null,4));
		test.add(new Deck(null,null,5));
		test.add(new Deck(null,null,6));
		test.add(new Deck(null,null,7));
		test.add(new Deck(null,null,8));
		test.add(new Deck(null,null,9));
		test.add(new Deck(null,null,10));
		List<Deck> temp=new LinkedList<>();
		numOfDecks=4;
		comboLineups(test,temp,0);
		
	}

}
