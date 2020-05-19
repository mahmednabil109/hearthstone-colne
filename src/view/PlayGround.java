package view;

import java.util.*;
import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import model.cards.minions.Minion;
import model.cards.spells.*;
import model.heroes.*;
import engine.Game;
import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import view.components.*;
import view.components.Button;

@SuppressWarnings("serial")
public class PlayGround extends JPanel implements HeroViewListener,
		ButtonListener, GameListener {

	private Game game;
	private Hero hero1, hero2;
	private CardView registerC, spellRegister;
	private HeroView registerH, HeroPowerRegister;
	private HeroView tmp, currentHero, opponentHero, _hero1, _hero2, winner;
	private Image introimg, playGround;
	private HashMap<String, Hero> herorefs;
	private ArrayList<Button> heroList;
	private boolean intro = true, gameOver = false, select = false;
	private final static Dimension d = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private final String PATH1 = "img//intro.jpg",
			PATH2 = "img//hearthstone.jpg";
	private Button exitButton, endTurnButton, startPlay, list1;
	private PlayGroundListener listener;
	public static final HashMap<String, String> paths = new HashMap<String, String>();
	public static final int DOWN = 1, UP = 0, UNKWON = -1;
	public static final int HEIGHT = (int) d.getHeight(), WIDTH = (int) d.getWidth();
	//public static final int HEIGHT = 864  , WIDTH = 1536;
	public static final Console console = new Console();

	public PlayGround(PlayGroundListener listener) {
		System.out.println(WIDTH);
		System.out.println(HEIGHT);
		initPaths();
		loadImages();
		this.exitButton = new Button((int) (HEIGHT / 22.9),
				(int) (WIDTH / 10.78), "img//Quit.png", null); // height = 47 ,
																// width = 178;
		this.exitButton.setBounds(20, 20, exitButton.getWIDTH(), // don't care
				exitButton.getHEIGHT());
		this.exitButton.setListener(this);
		console.setBounds((int)(WIDTH/192),HEIGHT/2 - (int)(HEIGHT/10.8),(int)(WIDTH/6.4),(int)(HEIGHT/5.4));      // x =10 , h = 200 , w = 300
		try {
			initHeroes();
			this.listener = listener;
		} catch (FullHandException | CloneNotSupportedException | IOException e) {
			console.log("Error : ",e.getMessage());
			e.printStackTrace();
		}
	}

	private void showAvailableHeroes() {
		int y = HEIGHT / 2 + HEIGHT / 9, dx = (int) (WIDTH / 7.38), x = WIDTH
				/ 2 - (dx * 5) / 2; // m = 120 , dx = 260
		for (Button btn : heroList) {
			btn.setBounds(x, y, btn.getWIDTH() + 4, btn.getHEIGHT() + 4);
			x += dx;
			add(btn);
		}
	}

	private void cancelSelected() {
		for (Button btn : heroList) {
			btn.setBackground(new Color(0, 0, 0, 0));
		}
		System.out.println("canceld");
		this.hero1 = null;
		this.hero2 = null;
	}

	private void loadHeros() throws IOException, CloneNotSupportedException {
		this.heroList = new ArrayList<Button>();
		this.herorefs = new HashMap<String, Hero>();
		String heroes[] = { "Rexxar", "Jaina Proudmoore", "Uther Lightbringer",
				"Anduin Wrynn", "Gul'dan" };
		herorefs.put(heroes[0], new Hunter());
		herorefs.put(heroes[1], new Mage());
		herorefs.put(heroes[2], new Paladin());
		herorefs.put(heroes[3], new Priest());
		herorefs.put(heroes[4], new Warlock());
		for (String hero : heroes) {
			Button tmp = new Button((int) (HEIGHT / 22.9), WIDTH / 8,
					"img//blank.png", null); // height = 47 , width= 240
			tmp.setListener(this);
			tmp.setData(hero);
			heroList.add(tmp);
		}

	}

	private void initHeroes() throws IOException, CloneNotSupportedException,
			FullHandException {
		removeAll();
		loadHeros();
		System.out.println("Started");
		this.list1 = new Button((int) (HEIGHT / 22.9), (int) (WIDTH / 10.78),
				"img//select.png", null); // height = 47 , width = 178;
		this.startPlay = new Button((int) (HEIGHT / 22.9),
				(int) (WIDTH / 10.78), "img//playnow.PNG", null); // height = 47,width = 178;
		this.list1.setListener(this);
		this.startPlay.setListener(this);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.list1.setBounds(
				WIDTH / 2 - list1.getWIDTH() / 2 - (int) (WIDTH / 9.6), // mw =
																		// 200 ,
																		// mh =
																		// 200
				HEIGHT / 2 + (int) (HEIGHT / 5.4), list1.getWIDTH(),
				list1.getHEIGHT());
		this.startPlay.setBounds(WIDTH / 2 - startPlay.getWIDTH() / 2
				+ (int) (WIDTH / 9.6), HEIGHT / 2 + (int) (HEIGHT / 5.4),
				startPlay.getWIDTH(), startPlay.getHEIGHT());
		add(list1);
		add(exitButton);
		add(startPlay);
		repaint();
	}

	private void startGame() {
		removeAll();
		add(console);
		console.log("hello world !");
		this.currentHero = game.getCurrentHero().ref;
		this.currentHero.ref = game.getCurrentHero();
		this.opponentHero = (_hero1 == currentHero) ? _hero2 : _hero1;
		this.opponentHero.ref = (hero1 == currentHero.ref) ? hero2 : hero1;

		this.currentHero.setPosition(DOWN);
		this.opponentHero.setPosition(UP);

		this.currentHero.setTurn(true);
		this.opponentHero.setTurn(false);

		this.currentHero.setListener(this);
		this.opponentHero.setListener(this);
		this.endTurnButton = new Button(110, 260, "img//EndTurn_btn.png", null);
		this.endTurnButton.setListener(this);
		this.exitButton.setListener(this);
		initUI();
	}

	private void gameOver() {
		removeAll();
		this.hero1 = null;
		this.hero2 = null;
		this.select = false;
		add(exitButton);
		this.startPlay = new Button(47, 178, "img//playnow.PNG", null);
		this.startPlay.setListener(this);
		this.startPlay.setBounds(WIDTH / 2 - startPlay.getWIDTH() / 2,
				HEIGHT - (int)(HEIGHT/4.32), startPlay.getWIDTH(), startPlay.getHEIGHT()); // mh = 250
		add(startPlay);
		repaint();

	}

	private void loadImages() {
		ImageIcon ii = new ImageIcon(PATH1);
		this.introimg = ii.getImage();
		ii = new ImageIcon(PATH2);
		this.playGround = ii.getImage();
	}

	private void initPaths() {
		String tmpPath = "img//cards//";
		String minions[] = { "Sheep", "hidden", "Silver Hand Recruit",
				"Wilfred Fizzlebang", "Prophet Velen", "Tirion Fordring",
				"Kalycgos", "King Krush", "Goldshire Footman",
				"Stonetusk Boar", "Bloodfen Raptor", "Frostwolf Grunt",
				"Wolfrider", "Chilwind Yeti", "Boulderfist Ogre", "Core Hound",
				"Argent Commander", "Sunwalker", "Chromaggus", "The LichKing",
				"Icehowl", "Colossus of the Moon" };
		String spells[] = { "Curse of Weakness", "Divine Spirit",
				"Flamestrike", "Holy Nova", "Kill Command", "Level Up!",
				"Multi-Shot", "Polymorph", "Pyroblast", "Seal of Champions",
				"Shadow Word_Death", "Siphon Soul", "Twisting Nether", "sheep" };
		String heroes[] = { "Rexxar", "Jaina Proudmoore", "Anduin Wrynn",
				"Uther Lightbringer", "Gul'dan" };
		for (String minion : minions)
			paths.put(minion, tmpPath + minion + ".png");
		tmpPath = "img//minions//";
		for (String minion : minions)
			paths.put(minion + "_m", tmpPath + minion + ".png");
		tmpPath = "img//heroes//";
		for (String hero : heroes)
			paths.put(hero, tmpPath + hero + ".png");
		tmpPath = "img//hero_powers//";
		for (String hero : heroes)
			paths.put(hero + "_hp", tmpPath + hero + "-HP" + ".png");
		for (String hero : heroes)
			paths.put(hero + "_hp_c", tmpPath + hero + "-HP-C" + ".png");
		tmpPath = "img//spells//";
		for (String spell : spells)
			paths.put(spell, tmpPath + spell + ".png");
		paths.put("Shadow Word: Death", tmpPath + "Shadow Word_Death" + ".png");
	}

	private void initUI() {
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		locateHeros();
		endTurnButton.setBounds(WIDTH - endTurnButton.getWIDTH()
				- endTurnButton.Margin - (int)(WIDTH/30),                        
				HEIGHT / 2 - endTurnButton.getHEIGHT() / 2 - (int)(HEIGHT/18),
				endTurnButton.getWIDTH(), endTurnButton.getHEIGHT());  // mw = 64 , mh = 60
		add(currentHero);
		add(opponentHero);
		add(endTurnButton);
		add(exitButton);
		repaint();
	}

	public void locateHeros() {
		this.currentHero.setBounds(0, HEIGHT / 2 - (int) (HEIGHT/ 15.42), WIDTH, HEIGHT / 2 + (int) (HEIGHT/ 15.42));         // mh = 70
		this.opponentHero.setBounds(0, 0, WIDTH, HEIGHT / 2 - (int) (HEIGHT/ 15.42));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!intro)
			g.drawImage(this.playGround, 0, 0, WIDTH, HEIGHT, this);
		else
			g.drawImage(this.introimg, 0, 0, WIDTH, HEIGHT, this);
		if (gameOver) {
			String p = paths.get(this.winner.ref.getName());
			System.out.println("image for winner hero " + p);
			g.drawImage((new ImageIcon(p)).getImage(), WIDTH / 2 - (int)(WIDTH/9.6),
					HEIGHT / 2 - (int)(HEIGHT/3.6),(int)(WIDTH/4.8),(int)(HEIGHT/2.7), this);                       // mw = 200 , mh = 300 ,width = height = 400
			Font newFont = new Font("Courier New", 1, 35);
			g.setColor(Color.WHITE);
			g.setFont(newFont);
			g.drawString("WINNER!", WIDTH / 2 - 100, HEIGHT / 2 + 150);

		}
	}

	@Override
	public void redraw() {
		this.repaint();
		// this.currentHero.redraw();
		// this.opponentHero.redraw();
	}

	@Override
	public void onClickButton(Button btn) throws IOException, CloneNotSupportedException {
		System.out.println(btn.getData());
		if (btn == endTurnButton) {
			this.currentHero.reset();
			this.currentHero.discard();
			this.currentHero.changePos();
			this.opponentHero.changePos();
			this.tmp = currentHero;
			currentHero = opponentHero;
			opponentHero = tmp;	
			this.currentHero.setTurn(true);
			this.opponentHero.setTurn(false);
			this.clearRegisters();
			try {
				this.game.endTurn();
			} catch (FullHandException | CloneNotSupportedException e) {
				console.log("Error : ",e.getMessage());
				if(e instanceof FullHandException){
					console.log("Burnt card : ",((FullHandException) e).getBurned());
				}
				e.printStackTrace();
			}
			locateHeros();
			repaint();
		} else if (btn == exitButton) {
			this.listener.exit();
		} else if (btn == startPlay) {
			if (!gameOver && hero1 != null && hero2 != null) {
				intro = false;
				try {
					this.game = new Game(hero1, hero2);
				} catch (FullHandException | CloneNotSupportedException e) {
					console.log("Error : ",e.getMessage());
					e.printStackTrace();
				}
				this.game.setListener(this);
				startGame();
			} else if (gameOver) {
				intro = true;
				try {
					gameOver = false;
					initHeroes();
				} catch (FullHandException | IOException
						| CloneNotSupportedException e) {
					console.log("Error : ",e.getMessage());					
					e.printStackTrace();
				}
			}
		} else if (btn == list1) {
			if (!select) {
				showAvailableHeroes();
				select = true;
			} else {
				select = false;
				cancelSelected();
			}

		} else if (herorefs.get(btn.getData()) != null) {
			System.out.println(herorefs.get(btn.getData()));
			if (hero1 == null) {
				hero1 = herorefs.get(btn.getData());
				_hero1 = hero1.ref;
				this.loadHeros();
				this.showAvailableHeroes();
			} else {
				hero2 = herorefs.get(btn.getData());
				_hero2 = hero2.ref;
				this.loadHeros();
				this.showAvailableHeroes();
			}
			btn.setBackground(new Color(0, 0, 255, 100));
		}
		this.repaint();
	}

	@Override
	public void onAttackAction(HeroView hero, CardView card)
			throws NotYourTurnException, NotEnoughManaException,
			InvalidTargetException, HeroPowerAlreadyUsedException,
			FullHandException, FullFieldException, CloneNotSupportedException {
		// hero attack with minion with ref card
		System.out.println("select or attack hero " + card.ref);
		if (HeroPowerRegister == null) {
			if (spellRegister == null) {
				if (this.registerC == null) {
					System.out.println(" try to select");
					this.registerC = card;
					card.Heighlight();
					;
				} else {
					try {
						System.out.println("try to attack");
						this.currentHero.ref.attackWithMinion(
								(Minion) registerC.ref, (Minion) card.ref);
					} catch (CannotAttackException | NotYourTurnException
							| TauntBypassException | InvalidTargetException
							| NotSummonedException e) {
						console.log("Error : ",e.getMessage());
						System.out.println("Error2 " + e);
					}
					this.registerC.setSelected(false);
					registerC.resetBackground();
					card.setWait(true);
					card.setSelected(false);
					registerC = null;
				}
			} else {
				System.out.println("casting spell on a Minion");
				if (spellRegister.ref instanceof LeechingSpell) {
					this.registerH.ref.castSpell(
							(LeechingSpell) spellRegister.ref,
							(Minion) card.ref);
				} else if (spellRegister.ref instanceof MinionTargetSpell) {
					this.registerH.ref.castSpell(
							(MinionTargetSpell) spellRegister.ref,
							(Minion) card.ref);
				} else
					return;
				registerH.removeSpell(spellRegister);
				this.spellRegister = null;
				this.registerH = null;
			}
		} else {
			if (HeroPowerRegister.ref instanceof Priest)
				((Priest) HeroPowerRegister.ref)
						.useHeroPower((Minion) card.ref);
			else if (HeroPowerRegister.ref instanceof Mage)
				((Mage) HeroPowerRegister.ref).useHeroPower((Minion) card.ref);
			else
				return;
			HeroPowerRegister.highLight(false);
			HeroPowerRegister = null;
		}
	}

	@Override
	public void onAttackHero(HeroView hero) throws NotYourTurnException,
			NotEnoughManaException, HeroPowerAlreadyUsedException,
			FullHandException, FullFieldException, CloneNotSupportedException {
		System.out.println("try to attack hero :" + HeroPowerRegister + " "
				+ spellRegister + " " + registerC);
		if (HeroPowerRegister == null) {
			if (spellRegister == null) {
				if (registerC == null)
					return;
				try {
					System.out.println("try to attack a hero");
					this.currentHero.ref.attackWithMinion(
							(Minion) registerC.ref, hero.ref);
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | InvalidTargetException
						| NotSummonedException e) {
					console.log("Error : ",e.getMessage());
					System.out.println("Error2 " + e);
				}
				this.registerC.setSelected(false);
				registerC.setBackground(new Color(0, 0, 0, 0));
				registerC = null;
			} else {
				System.out.println("try to cast spell on a hero");
				if (spellRegister.ref instanceof HeroTargetSpell)
					this.registerH.ref.castSpell(
							(HeroTargetSpell) spellRegister.ref, hero.ref);
				else
					return;
				registerH.removeSpell(spellRegister);
				this.spellRegister = null;
				this.registerH = null;
			}
		} else {
			if (HeroPowerRegister.ref instanceof Priest)
				((Priest) HeroPowerRegister.ref).useHeroPower(hero.ref);
			else if (HeroPowerRegister.ref instanceof Mage)
				((Mage) HeroPowerRegister.ref).useHeroPower(hero.ref);
			else
				return;
			HeroPowerRegister.highLight(false);
			HeroPowerRegister = null;

		}
	}

	@Override
	public void onCastSpell(HeroView hero, CardView card)
			throws NotYourTurnException, NotEnoughManaException {
		// TODO handle casting spell
		System.out.println("cast spell");
		if (card.ref instanceof FieldSpell) {
			hero.ref.castSpell((FieldSpell) card.ref);
			hero.removeSpell(card);
		} else if (card.ref instanceof AOESpell) {
			hero.ref.castSpell((AOESpell) card.ref,
					this.opponentHero.ref.getField());
			hero.removeSpell(card);
		} else {
			System.out.println("on right path");
			this.spellRegister = card;
			this.registerH = hero;
		}
	}

	@Override
	public void clearRegisters() {
		this.spellRegister = null;
		this.registerH = null;
		this.registerC = null;
		this.HeroPowerRegister = null;
	}

	@Override
	public void onGameOver() {
		System.out.println("Game Over");
		intro = false;
		gameOver = true;
		winner = (currentHero.ref.getCurrentHP() != 0 ? currentHero
				: opponentHero);
		System.out.println(winner.ref);
		this.gameOver();
	}

	@Override
	public void useHeroPower(HeroView hero) throws NotEnoughManaException,
			HeroPowerAlreadyUsedException, NotYourTurnException,
			FullHandException, FullFieldException, CloneNotSupportedException {
		System.out.println("useing Hero power of " + hero.ref);
		if (hero.ref instanceof Hunter)
			((Hunter) hero.ref).useHeroPower();
		else if (hero.ref instanceof Paladin)
			((Paladin) hero.ref).useHeroPower();
		else if (hero.ref instanceof Warlock)
			((Warlock) hero.ref).useHeroPower();
		else {
			hero.highLight(true);
			this.HeroPowerRegister = hero;
		}
	}

	@Override
	public void reset() {
		this.currentHero.reset();
		this.currentHero.discard();
	}

}
