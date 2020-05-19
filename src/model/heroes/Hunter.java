package model.heroes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import view.PlayGround;
import view.components.*;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;
import model.cards.Card;
import model.cards.Rarity;
import model.cards.minions.Minion;
import model.cards.spells.KillCommand;
import model.cards.spells.MultiShot;

public class Hunter extends Hero {

	public Hunter() throws IOException, CloneNotSupportedException {
		super("Rexxar");
	}

	@Override
	public void buildDeck() throws IOException, CloneNotSupportedException {
		ArrayList<Minion> neutrals = getNeutralMinions(
				getAllNeutralMinions("neutral_minions.csv"), 15);
		getDeck().addAll(neutrals);
		for (int i = 0; i < 2; i++) {
			getDeck().add(new KillCommand());
			getDeck().add(new MultiShot());

		}
		Minion krush = (new Minion("King Krush", 9, Rarity.LEGENDARY, 8, 8,
				false, false, true));
		getDeck().add(krush);
		listenToMinions();
		Collections.shuffle(getDeck());
		for (Card card : getDeck()) {
			CardView cardView = new CardView(card, PlayGround.paths.get(card
					.getName()), PlayGround.paths.get(card.getName() + "_m"));
			cardView.setListener(this.ref);
			card.ref = cardView;
		}
	}

	public void useHeroPower() throws NotEnoughManaException,
			HeroPowerAlreadyUsedException, NotYourTurnException,
			FullHandException, CloneNotSupportedException, FullFieldException {
		super.useHeroPower();
		getListener().damageOpponent(2);
	}

}
