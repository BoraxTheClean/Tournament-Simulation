package Testing;
import Tournaments.*;
import Player.*;
import Format.*;
import java.util.*;
import org.junit.*;
import java.lang.Runtime;
public class SingleElimTests{
	static SingleElim tournament;
	static SingleElim unevenBracket;
//	static Results results;
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
		for(int i=0;i<128;i++){
			players.add(new Player(String.valueOf(i),decks,i));
		}
		Format format=new Conquest();
		tournament=new SingleElim(players,format);
		tournament.processRounds();
//		results=new Results();
		Runtime run=Runtime.getRuntime();
		for(int i=128;i<133;i++){
			players.add(new Player(String.valueOf(i),decks,i));
		}
		unevenBracket=new SingleElim(players,format);
		unevenBracket.processRounds();
 	
	}
	@Test
	public void verifyBracket(){
		assert(verifyPlayers(tournament.getBracket()));
		System.out.println("Even bracket done.");
		assert(verifyPlayers(unevenBracket.getBracket()));
	}
	public boolean verifyPlayers(BracketNode head){
		Player p=head.payload;
		if(head.left==null && head.right==null)
			return true;
		if(head.left.payload.equals(p) ^ head.right.payload.equals(p)){
			return verifyPlayers(head.left) && verifyPlayers(head.right);
		}else
			return false;
	}


}
