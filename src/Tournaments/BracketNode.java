package Tournaments;
import java.util.*;
import Player.*;
public class BracketNode{
	public BracketNode left;
	public BracketNode right;
	public BracketNode parent;
	public Player payload=null;
	public BracketNode(Player p){
		payload=p;
	}
	public BracketNode(){

	}
	/*
		Given a list of players and a head node with null pointers, generate a
		complete bracket where all the players are leaf nodes of the bracket.
		Note this only works for lists of size power of 2.
	*/
	public boolean hasPayload(){
		return payload!=null;
	}
}
