package Testing;
import Tournaments.*;
import Player.*;
import java.util.*;
import org.junit.*;
public class BracketNodeTests{
	static int size=32;
	static BracketNode test;
	static BracketNode test2;
	@BeforeClass
	public static void setup(){
		List<Player> players=new LinkedList<>();
		for(int i=0;i<size;i++){
			Player p=new Player(String.valueOf(i));
			p.id=i;
			players.add(p);
		}
		test= new BracketConstruction().generateBracketHelper(players);
		for(int i=size;i<size+5;i++){
			Player p=new Player(String.valueOf(i));
			p.id=i;
			players.add(p);
		}
		test2=new BracketConstruction().generateBracketHelper(players);
	}
	@Test
	public void testNumberOfPlayers(){
		assert(countPlayers(test)==size);
		assert(countPlayers(test2)==size+5);
	}
	@Test
	public void testSpace(){
		assert(countSpace(test)==(2*size-1));
		assert(countSpace(test)==(2*size-1+5));
	}
	@Test
	public void testPlayersAtLeaves(){
		assert(playersAtLeaves(test));
		assert(playersAtLeaves(test2));
	}
	public boolean playersAtLeaves(BracketNode head){
		if(head.payload!=null){
			return head.left!=null && head.right!=null;
		}
		return playersAtLeaves(head.left) && playersAtLeaves(head.right);
	}
	public int countPlayers(BracketNode head){
		if(head.payload!=null)
			return 1;
		return countPlayers(head.left)+countPlayers(head.right);
	}
	public int countSpace(BracketNode head){
		if(head==null)
			return 0;
		return 1+countSpace(head.left)+countSpace(head.right);
	}


}
