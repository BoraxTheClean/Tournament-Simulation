package Tournaments;
import Format.*;
import Player.*;
import java.util.*;
public interface Tournament{
	/*
	Simulates the next round of play.
	*/
	public void processRounds();
	/*
	Once the tournament is finished, gives you standings of all players
	*/
	public void getResults();

	public void setup();
}
