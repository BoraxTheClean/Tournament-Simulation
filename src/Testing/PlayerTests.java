package Testing;
import java.util.*;
import java.util.ArrayList;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import BanGet.MockBanPicker;
import Player.*;
public class PlayerTests{
	List<Deck> decks;
	Player david;
	Player owen;
	@Before
	public void before(){
		Map<Integer,Float> map=new HashMap<>();
		map.put(0,.3f);
		map.put(1,.7f);
		map.put(2,.4f);
		Deck renoMage=new Deck("Reno Mage",map,0);
		Deck comboDruid=new Deck("Combo Druid",map,1);
		Deck turboWarrior=new Deck("C'thun Warrior",map,2);
		decks=new ArrayList<>();
		decks.add(renoMage);
		decks.add(comboDruid);
		decks.add(turboWarrior);
		david=new Player("David 'BirthdayLion' Steinberg",decks,1);
		owen=new Player("StarGazer",decks,2);
	}
	@Test
	public void deckCheck(){
		for(int i=0;i<decks.size();i++){
			assert(david.hasDecks());
			assert(owen.hasDecks());	
			assert(owen.setDeckToUsed(decks.get(i)));
			assert(david.setDeckToUsed(decks.get(i)));
		}
		assert(!owen.hasDecks());	
		assert(!david.hasDecks());	
		owen.resetDecks();
		david.resetDecks();
		assert(owen.hasDecks());
		assert(david.hasDecks());
	}	
	@Test
	public void banCheck(){
		assert(owen.getDecks().size()==3);
		assert(david.getDecks().size()==3);
		owen.setDeckToUsed(david.getBan(owen, new MockBanPicker()));
		david.setDeckToUsed(david.getBan(owen, new MockBanPicker()));
		assert(owen.getDecks().size()==2);
		assert(david.getDecks().size()==2);
	}

}
