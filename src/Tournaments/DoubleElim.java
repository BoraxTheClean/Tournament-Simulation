package Tournaments;
import Format.*;
import Player.*;
import java.util.*;

public class DoubleElim extends SingleElim{

	SingleElim losersBracket;
	public DoubleElim(List<Player> players,Format format){
		super(players,format);
	}
	public void processRounds(){
		super.processRounds();
		List<Player> losers=new LinkedList<Player>();
		getLosers(this.bracket,losers);
	}
	public void getLosers(BracketNode head,List<Player> players){
		if(head.left==null && head.right==null)
			return;
		Player p=head.payload;
		if(p.id==head.left.payload.id){
			players.add(head.right.payload);
			getLosers(head.right,players);
		}else{
			players.add(head.left.payload);
			getLosers(head.left,players);
		}
	}
	public void getResults(){
	}
	public void setup(){
		super.setup();
	}
}
