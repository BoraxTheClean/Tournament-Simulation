package Format;
import Player.*;
import BanGet.*;
public interface Format{
	public void setBP(BanPicker thisBP);
	public Player play(Player playerOne,Player playerTwo);
	public double getEquityBeforeBan(Player playerOne, Player playerTwo);

}
