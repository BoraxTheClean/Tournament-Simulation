package BanGet;

import Player.Deck;
import Player.Player;

public class N1Ban extends BanPicker {

	@Override
	public Deck getBan(Player one, Player two) {
		Deck currentBest = BanUtils.bestAgainst(one.getDecks(), two.getDecks());
		Deck currentTheirBest = BanUtils.bestAgainst(two.getDecks(), one.getDecks());
		Deck bestAfterBan = BanUtils.bestAgainst(BanUtils.remove(one.getDecks(), currentTheirBest), two.getDecks());
		Deck theirBestAfterBan = BanUtils.bestAgainst(BanUtils.remove(two.getDecks(), currentBest), one.getDecks());
		if (currentBest==bestAfterBan&&currentTheirBest==theirBestAfterBan) {
			return currentBest;
		}
		else if (currentBest==bestAfterBan) {
			Deck bestTheirChange = BanUtils.bestAgainst(BanUtils.remove(one.getDecks(), theirBestAfterBan), two.getDecks());
			if (bestTheirChange==bestAfterBan) {
				return bestTheirChange;
			}
		}
		else if (currentTheirBest==theirBestAfterBan) {
			Deck bestOurChange = BanUtils.bestAgainst(BanUtils.remove(two.getDecks(),  bestAfterBan), one.getDecks());
			if (bestOurChange==theirBestAfterBan) {
				return bestOurChange;
		}
	}
		throw new RuntimeException("We need a nash solver.");

}

	@Override
	public double getEquityBeforeBan(Player one, Player two) {
		return 0;
	}
}
