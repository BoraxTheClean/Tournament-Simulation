package Tournaments;
import Player.*;
import java.util.*;
import Tournaments.BracketNode;
public class BracketConstruction{
	public  Map<Integer,BracketNode> map=new HashMap<>();
	public BracketNode generateBracket(BracketNode node, ListIterator<Player> players,int depth){
                if(depth==0){
                        BracketNode retVal=new BracketNode();
			Player p=players.next();
                        retVal.payload=p;
			map.put(p.id,retVal);
                        return retVal;
                }   
                node.left=generateBracket(new BracketNode(),players, depth-1);
                node.right=generateBracket(new BracketNode(),players,depth-1);
                return  node;
        }
        public BracketNode generateBracketHelper(List<Player> players){
                ListIterator<Player> iterator=players.listIterator();
                BracketNode bn= generateBracket(new BracketNode(),iterator,log2(players.size()));
		int distance=1;
		while(iterator.hasNext()){
			Player p=iterator.next();
			expandNode(p,distance);
			distance+=2;
		}
                return bn;

        }
	public void expandNode(Player p, int nodeNumber){
		BracketNode node=map.get(nodeNumber);
		node.left=new BracketNode(p);
		node.right=new BracketNode(node.payload);
		node.payload=null;
		map.put(p.id,node.left);
	}
	public static int log2(int n){
                if(n <= 0) throw new IllegalArgumentException();
                return 31 - Integer.numberOfLeadingZeros(n);
        }



}
