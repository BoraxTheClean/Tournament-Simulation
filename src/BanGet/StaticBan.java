package BanGet;
import Player.*;
import java.util.*;
public class StaticBan extends BanPicker{
	private Map<List<Deck>,HashMap<List<Deck>,Deck>> banMap=new HashMap<>();
	StaticBan(Map<List<Deck>,HashMap<List<Deck>,Deck>> map){
		banMap=map;
	}
	public Deck getBan(Player one,Player two){
		return banMap.get(one.getDecks()).get(two.getDecks());
	
	}
	@Override
	public double getEquityBeforeBan(Player one, Player two) {
		return 0;
	}


}
