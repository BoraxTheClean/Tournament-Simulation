package BanGet;

import Player.Deck;
import Player.Player;

public class MockBanPicker extends BanPicker {

	@Override
	public Deck getBan(Player one, Player two) {
		return null;
	}

	@Override
	public double getEquityBeforeBan(Player one, Player two) {
		return 0;
	}

}
