package view.components;

import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;

public interface HeroViewListener {
	public void redraw();

	public void onAttackAction(HeroView hero, CardView card)
			throws NotYourTurnException, NotEnoughManaException,
			InvalidTargetException, HeroPowerAlreadyUsedException, FullHandException, FullFieldException, CloneNotSupportedException;

	public void clearRegisters();

	public void onAttackHero(HeroView hero) throws NotYourTurnException,
			NotEnoughManaException, HeroPowerAlreadyUsedException, FullHandException, FullFieldException, CloneNotSupportedException;

	public void onCastSpell(HeroView hero, CardView card)
			throws NotYourTurnException, NotEnoughManaException;
	
	public void reset();
	
	public void useHeroPower (HeroView hero) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException;
}
