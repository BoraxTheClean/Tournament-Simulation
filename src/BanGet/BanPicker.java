package BanGet;
import Player.*;
public abstract class BanPicker{
	//Player one delivers a ban out of player two's decks.
	public abstract Deck getBan(Player one,Player two);
	public abstract double getEquityBeforeBan(Player one, Player two);


}
