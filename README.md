# About

Hearthstone's Last Hero Standing {LHS) tournament format is deceptively complicated.  The [ruleset](https://liquipedia.net/hearthstone/Last_Hero_Standing) is simple.  But analyzing a single matchup to determine which player is favored and which decks to ban or to start with with requires computing as many as 17 Nash equilibria for different payoff matrices, as well as finding optimal paths through many game trees.  Preparing for a tournament could require analyzing hundreds or thousands of different matchups.

In the early years of competitive Hearthstone, most players used heuristics to estimate who was favored in an LHS matchup, or didn't run calculations at all.  We believed we could gain a significant edge in tournaments by writing a program to quickly and accurately calculate matchup winrates - not just against one player, but against the metagame as a whole.  In 2017, we proved it by winning Dreamhack Austin.

# Last Hero Standing

The Last Hero Standing format works as follows:
1. Matches are played in a best of five or best of seven format.
2. Each player brings the same number of decks.
3. There is an optional ban phase, in which both players simultaneously choose one of their opponent's decks to not be played.
4. For the first game, both players simultaneously pick one of their remaining decks to play with.
5. A game is played.  The losing player's deck is eliminated, and they must select a new one for the next game, while the winner must keep using the same deck they won with. 
6. Step 5 is repeated until one player has no more decks remaining, and that player loses the match.

The most common Last Hero Standing format in practice was best of five with a ban phase, with each player bringing four decks.

# Algorithm Description

Given data on the winrates of individual decks against other decks, our algorithm calculates the winrate of a Last Hero Standing lineup against another lineup.

In a best of five match with ban phase, Player 1 will have Decks A, B, C, and D, and Player 2 will have Decks E, F, G, and H.  First, the algorithm represents the simultaneous ban phase as a payoff matrix.  For example, if Player 1 bans Deck G while Player 2 bans Deck B, Player 1 will have a certain probability to win the series.  If we find those probabilities for all such deck pairs, we can calculate the Nash equilibrum and find Player 1 and 2's optimal ban strategies, as well as each player's probability of winning the series given those strategies.

To find the win probability for each ban pair, we use another payoff matrix, this time for deck choices for Game 1.  If Player 1 is left with Decks A, C, and D, and Player 2 has decks E, F, and H, Player 1 might choose to start with Deck A while Player 2 starts with Deck F.  These starting choices also affect the players' chances of winning the series, and once we find the winrate for each starting deck pair, we can calculate the Nash equilibrium of this post-ban payoff matrix to figure out each player's optimal strategy for choosing a starting deck and their resulting winrate.

Finally, to find the win probability for each starting deck pair, we represent the rest of the match as a game tree.  Players no longer make simultaneous choices - each player only has to choose which deck to use once their current deck has lost.  Starting from a Game 1 of Deck A versus Deck F, we refer to individual deck winrate to figure out how likely Deck A is to win, and we calculate both player's optimal choices for deck selection for the rest of the match to figure out (e.g.) whether Player 1 should play Deck C or D once Deck A is eliminated.  Solving this game tree yields each player's probability of winning the match given the starting deck pair of Deck A versus Deck F.  This lets us populate and solve the deck choice payoff matrix, which lets us populate and solve the ban phase payoff matrix, which yields optimal ban and deck choice strategies as well as winrates for the match as a whole.

# History and Usage

We began work on this codebase in 2016, as an attempt to predict outcomes for a major Conquest tournament.  At the time, we did not have a mathematical solution for Last Hero Standing - Conquest was a much easier format to work with.

In 2017, we started to work on the Last Hero standing code as part of Shoop's preparation for Dreamhack Austin.  We got the core algorithm for lineup versus lineup winrate working in time for deck submission, but didn't have more advanced tournament tools ready.  Shoop calculated winrates for several Last Hero Standing lineups he was considering against a field of ~10 lineups that he thought represented the expected metagame, and submitted a lineup that had ~56% winrate against the field.  He won the tournament, going 9-1 in games during the top 8 playoffs.

Later in 2017 and 2018, we added tooling to prepare for tournaments.  We wrote a tournament simulator to investigate winner's metagames (e.g. the decks and lineups with a 5-0 record after five rounds) and see whether they significantly impacted lineup performance; we concluded that the differences were small and the additional computing time needed was too much of a cost.  We also wrote a search function to test all possible lineups against an expected metagame, rather than having to guess which lineups looked good.  For Dreamhack Austin 2018, Shoop found two promising lineups using this codebase, and picked one.  He was eliminated early, but Amnesiac (coincidentally) brought the other lineup he was considering, and won the tournament.

Several other players and teams have achieved success with similar algorithms.  MannySkull implemented a Last Hero Standing algorithm independently, and Shoop helped MegaManMusic build his own version of this algorithm, which was used by Tempo Storm players and by the Canadian practice group with Monsanto, Luker, etc.  Though there haven't been open Hearthstone tournaments over the past two years to use this algorithm in, we believe our individual success and the popularity of this algorithm at its heyday have proved our point:  we were able to use our understanding of game theory and our data and programming skills to gain a significant competitive advantage in Hearthstone.

# Authors

David Steinberg (Shoop) and [Owen Collier-Ridge](https://owen.dev).
