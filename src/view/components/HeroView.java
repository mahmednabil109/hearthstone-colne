package view.components;

import java.util.*;
import java.awt.*;

import javax.swing.*;

import view.PlayGround;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;
import model.heroes.Hero;
import model.cards.minions.*;

@SuppressWarnings("serial")
public class HeroView extends JPanel implements CardViewListener,
		ButtonListener {
	public int pos;
	public static final int HEIGHT =  PlayGround.HEIGHT / 2, WIDTH = PlayGround.WIDTH;
	public static final int PWIDTH = PlayGround.WIDTH , PHEIGHT = PlayGround.HEIGHT;
	private int deckSize;
	private int healthPoints;
	private final int DOWN = 1, UP = 0;
	private String name;
	private Button hero_img_btn, hero_power_btn, discard;
	private HeroViewListener hvl;
	private Field field;
	private ManaCrystalBar mcb;
	private ArrayList<CardView> hand;
	public boolean inTurn = false;
	public Hero ref;

	public HeroView(Hero ref, int pos) {
		this.ref = ref;
		this.deckSize = this.ref.getDeck().size();
		this.name = ref.getName();
		this.hero_img_btn = new Button((int)(PHEIGHT / 4.1538),(int)(PWIDTH / 6.857), PlayGround.paths.get(name),
				30 + ""); // width = 280 , height = 260
		this.hero_power_btn = new Button((int)(PHEIGHT / 6.75),(int)(PWIDTH / 11.294), PlayGround.paths.get(name
				+ "_hp_c"), null); // width = 170 , height = 280
		this.discard = new Button((int)(PHEIGHT/ 30.857), (int)(PWIDTH / 54.857)
				, "img//cross.png", null); // height = width = 35 px
		this.hero_img_btn.setListener(this);
		this.hero_power_btn.setListener(this);
		this.discard.setListener(this);
		this.pos = pos;
		this.field = new Field((int) (this.WIDTH * .7),
				(int) (this.HEIGHT * 0.4));
		this.hand = new ArrayList<CardView>();
		this.mcb = new ManaCrystalBar((pos == DOWN ? 10 : 2));
		initUI();
	}

	private void initUI() {
		setBackground(new Color(0, 100, 255, 0));
		setLayout(null);
		locateCards();
		locateComponent();
		add(discard);
		this.discard.setVisible(false);
		add(field);
		add(mcb);
		add(hero_img_btn);
		add(hero_power_btn);
		repaint();
	}

	private void locateComponent() {
		if (pos == DOWN) {
			this.field.setBounds(WIDTH / 2 - field.getWIDTH() / 2, (int)(PHEIGHT / 27),
					field.getWIDTH(), field.getHEIGHT()); // y = 40
			this.mcb.setLimit(10);
			this.mcb.setBounds(WIDTH - mcb.WIDTH - (int)(WIDTH / 11.0344), HEIGHT
					- mcb.HEIGHT / 2 - (int)(PHEIGHT/90), mcb.WIDTH, mcb.HEIGHT); // xm = 174 , ym = 12
			this.hero_img_btn.setBounds(WIDTH / 2 - hero_img_btn.getWIDTH() / 2
					+ (int)(WIDTH/64), HEIGHT - hero_img_btn.getHEIGHT() - (int)(PHEIGHT / 21.6),
					hero_img_btn.getWIDTH(), hero_img_btn.getHEIGHT()); // xm = 30 , ym = 50
			this.hero_power_btn.setBounds((int)(WIDTH/1.74545), (int)(PHEIGHT/4), hero_power_btn.getWIDTH(),
					hero_power_btn.getHEIGHT()); // x = 1100 , y = 270
		} else if (pos == UP) {
			this.field.setBounds(WIDTH / 2 - field.getWIDTH() / 2, this.HEIGHT
					- field.getHEIGHT() - (int)(PHEIGHT / 27), field.getWIDTH(),
					field.getHEIGHT());
			this.mcb.setLimit(2);
			this.mcb.setBounds(WIDTH - mcb.WIDTH - (int)(WIDTH / 8.5333), mcb.HEIGHT
					/ 2  -(int)(PHEIGHT / 216), mcb.WIDTH, mcb.HEIGHT); // ym = 5
			this.hero_img_btn.setBounds(WIDTH / 2 - hero_img_btn.getWIDTH() / 2
					+(int)(PHEIGHT / 27.6), hero_img_btn.getHEIGHT() - (int)(PHEIGHT / 6.3905),
					hero_img_btn.getWIDTH(), hero_img_btn.getHEIGHT());
			this.hero_power_btn.setBounds((int)(WIDTH / 1.7534),(int)(PHEIGHT / 7.1052), hero_power_btn.getWIDTH(),
					hero_power_btn.getHEIGHT()); // x = 1095 , y = 152
		}

	}
	
	public void updateDeckSize(){
		this.deckSize = this.ref.getDeck().size();
	}

	public void setListener(HeroViewListener hvl) {
		this.hvl = hvl;
	}
	
	public Field getField(){
		return this.field;
	}

	public void setTurn(boolean inTurn) {
		this.inTurn = inTurn;
		for (CardView c : hand)
			c.setHidden(!inTurn);
		repaint();
	}

	public void locateCards() {
		if (hand.size() == 0) {
			repaint();
			return;
		}
		CardView tmp = hand.get(0);
		int n = hand.size();
		int over = (int)(WIDTH / 21.3333); // over = 90
		int w = tmp.getWIDTH() * n - over * (n - 1), x = this.WIDTH / 2 - w / 2, y;
		if (pos == DOWN) {
			y = this.HEIGHT - tmp.getHEIGHT() + (int)(PHEIGHT / 8.3076); // ym = 130
		} else {
			y = -tmp.getHEIGHT() + (int)(PHEIGHT / 12);  //ym = 90
		}
		int dx = tmp.getWIDTH() - over;
		for (CardView c : hand) {
			c.setBounds(x, y, c.getWIDTH(), c.getHEIGHT());
			x += dx;
			add(c);
		}
		repaint();
	}

	public void changePos() {
		this.pos = (pos == UP ? DOWN : UP);
		this.initUI();
	}

	public void drawCard(CardView c) {
		this.hand.add(c);
		this.locateCards();
		this.repaint();
	}

	public void setManaCrystal(int n) {
		this.mcb.setManaCrystal(n);
		repaint();
	}

	public void setHealthPoints(int hp) {
		this.healthPoints = hp;
		this.hero_img_btn.setText("" + this.healthPoints);
		repaint();
	}

	public void setPosition(int pos) {
		this.pos = pos;
		initUI();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font newFont = new Font("Courier New", 1, (int)(PWIDTH / 73.846)); // f = 26
		g.setColor(this.deckSize > 0 ? Color.GREEN : Color.RED);
		g.setFont(newFont);
		if(pos == DOWN){
			g.drawString(this.deckSize + "" , (int)(WIDTH / 1.10982), (int)(PHEIGHT / 6.75));     // x = 1730 , y = 160
		}else if (pos == UP){
			g.drawString(this.deckSize + "" ,(int)(WIDTH / 1.10982),(int)(PHEIGHT / 2.8421 ));               // x = 1730 , y = 380
		}
	}

	@Override
	public void onPlayCard(CardView c) throws NotYourTurnException,
			NotEnoughManaException, FullFieldException {
		this.ref.playMinion((Minion) c.ref);
		c.changeImage();
		this.hand.remove(c);
		this.field.addCard(c);
		locateCards();
		this.onHoverCard();
		repaint();
	}

	public void redraw() {
		this.repaint();
		this.field.redraw();
		this.hvl.redraw();
	}

	@Override
	public void onHoverCard() {
		// this.locateCards();
		this.repaint();
		hvl.redraw();
	}

	@Override
	public void onDieCard(CardView c) {
		this.field.removeCard(c);
		this.repaint();
	}

	@Override
	public void onAttackAction(CardView c) {
		try {
			this.hvl.onAttackAction(this, c);
		} catch (NotYourTurnException | NotEnoughManaException
				| InvalidTargetException | HeroPowerAlreadyUsedException
				| FullHandException | FullFieldException
				| CloneNotSupportedException e) {
			PlayGround.console.log(e.getMessage());
			e.printStackTrace();
			this.hvl.reset();
		}
	}

	@Override
	public void onCastSpell(CardView c) throws NotYourTurnException,
			NotEnoughManaException {
		this.hvl.onCastSpell(this, c);
		c.setBackground(new Color(0, 255, 0, 100));
	}

	public void reset() {
		for (CardView card : hand) {
			card.onClickButton(null);
		}
	}

	public void removeSpell(CardView c) {
		this.remove(c);
		this.hand.remove(c);
		this.locateCards();
		hvl.redraw();
	}

	@Override
	public void onClickButton(Button btn) {
		if (btn == hero_img_btn) {
			try {
				this.hvl.onAttackHero(this);
			} catch (NotYourTurnException | NotEnoughManaException
					| HeroPowerAlreadyUsedException | FullHandException
					| FullFieldException | CloneNotSupportedException e) {
				this.hvl.reset();
				PlayGround.console.log(e.getMessage());
				e.printStackTrace();
			}
		} else if (btn == hero_power_btn && inTurn) {
			if (hero_power_btn.icon) {
				int x = hero_power_btn.getX();
				int y = hero_power_btn.getY();
				this.hero_power_btn.changeImage(PlayGround.paths.get(this.ref
						.getName() + "_hp"));
				this.hero_power_btn.setHEIGHT((int)(PHEIGHT / 3.6)); //height = 300
				this.hero_power_btn.setWIDTH((int)(WIDTH / 9.6)); // width = 200
				this.hero_power_btn.setBounds(x, y - (int)(PHEIGHT / 18),(int)(WIDTH / 9.6), (int)(PHEIGHT / 3.6)); // ym = 60
				this.discard.setBounds(x + (int)(WIDTH/384), y - (int)(PHEIGHT / 16.6153), discard.getWIDTH(),
						discard.getHEIGHT()); // xm = 5 , ym = 65
				this.discard.setVisible(true);
				this.hero_power_btn.icon = false;
				this.repaint();
				this.hvl.redraw();
			} else if(inTurn) {
				try {
					this.hvl.useHeroPower(this);
					this.redraw();
				} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
						| NotYourTurnException | FullHandException
						| FullFieldException | CloneNotSupportedException e) {
					PlayGround.console.log(e.getMessage());
					e.printStackTrace();
					this.discard();
				}
			}

		} else if (btn == discard) {
			this.discard();
		}
	}
	
	public void highLight(boolean f){
		if(f)
			hero_power_btn.setBackground(new Color(0,255,0,100));
		else
			hero_power_btn.setBackground(new Color(0,0,0,0));
	}

	public void discard() {
		if (!this.hero_power_btn.icon) {
			this.highLight(false);
			this.discard.setVisible(false);
			int x = hero_power_btn.getX();
			int y = hero_power_btn.getY();
			this.hero_power_btn.setHEIGHT((int)(PHEIGHT / 6.3529)); // height = 170
			this.hero_power_btn.setWIDTH((int)(WIDTH / 12));  //  width = 160
			this.hero_power_btn.setBounds(x, y + (int)(PHEIGHT / 18),(int)(WIDTH / 12),(int)(PHEIGHT / 6.3529)); // ym = 60
			this.hero_power_btn.changeImage(PlayGround.paths.get(this.ref
					.getName() + "_hp_c"));
			this.hero_power_btn.icon = true;
			this.repaint();
			this.hvl.redraw();
		}
	}

	@Override
	public void clearRegisters() {
		this.hvl.clearRegisters();
	}

}
