package view.components;

import exceptions.FullFieldException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;

public interface CardViewListener {

	public void onPlayCard(CardView c) throws NotYourTurnException, NotEnoughManaException, FullFieldException;

	public void onAttackAction(CardView c);
	
	public void onCastSpell(CardView c) throws NotYourTurnException, NotEnoughManaException;

	public void clearRegisters();
	
	public void onHoverCard();

	public void onDieCard(CardView c);
}
