package Player;
import java.util.Map;
public class Deck{
	public String className;
	public String name;
	public Map<Integer,Float> matchups;
	public int id;
	public Deck(String name, Map<Integer,Float> matchups,int id){
		this.name=name;
		this.matchups=matchups;
		this.id=id;
	}
	public Float getWinPercentage(Deck deck){
		return this.matchups.get(deck.id);
	}
        public boolean equals(Deck d){ 
                return this.id==d.id;
        }   
}
