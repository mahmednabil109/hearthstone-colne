	package view.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;
import view.PlayGround;
import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.Spell;

@SuppressWarnings("serial")
public class CardView extends JPanel implements MouseListener, ButtonListener {

	private static int nshow = 0;
	public static final int PWIDTH = PlayGround.WIDTH , PHEIGHT = PlayGround.HEIGHT;
	private String image2Path;
	private boolean isPlayed, isSelected, isSpell, wait, hide, show = true;
	private int healthPoints;
	private int attackPoints;
	private int HEIGHT;
	private int WIDTH;
	private int oldx, oldy;
	private CardViewListener listener;
	private Button discard;
	private int HOVER = (int)(PHEIGHT / 36), EXTRA = (int)(PHEIGHT / 108), SHIFT = 0;           // hover = 30 , extra = 10 , shift=0;
	private Color background;
	public Card ref;
	private Image img;

	public CardView(Card ref, String imagePath, String image2Path) {
		this.background = new Color(0,0,0,0);
		loadImage(imagePath);
		if (ref instanceof Minion) {
			Minion m = (Minion) ref;
			this.attackPoints = m.getAttack();
			this.healthPoints = m.getCurrentHP();
		} else if (ref instanceof Spell) {
			isSpell = true;
		}
		this.discard = new Button((int)(PHEIGHT/ 30.857), (int)(PWIDTH / 54.857), "img//cross.png", null); //width = height = 35
		this.discard.setListener(this);
		this.ref = ref;
		this.image2Path = image2Path;
		initUI();
		addMouseListener(this);
	}

	private void loadImage(String imagePath) {
		ImageIcon ii = new ImageIcon(imagePath);
		this.img = ii.getImage();
		this.WIDTH = (int) (PWIDTH / 12.9729 );  // width = 148
		this.HEIGHT = (int) (PHEIGHT / 5.2941 ); // height = 204
	}

	public void changeImage() {
		ImageIcon ii = new ImageIcon(image2Path);
		this.img = ii.getImage();
		this.WIDTH = (int) (PWIDTH / 11.2941 ); // width = 170
		this.HEIGHT = (int) (PHEIGHT / 7.5 ); // height = 144
		this.initUI();
		this.repaint();
	}

	private void initUI() {
		setBackground(background);
		setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
		setFocusable(true);
	}

	public void setListener(CardViewListener listener) {
		this.listener = listener;
	}

	public boolean isPlayed() {
		return isPlayed;
	}

	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
		if(isPlayed){
			this.HOVER = (int)(PHEIGHT / 216); // hover = 5;
			this.EXTRA = 0;
			this.SHIFT = 0;
		}
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public void setHidden(boolean hide) {
		this.hide = hide;
		if (hide == true)
			this.loadImage(PlayGround.paths.get("hidden"));
		else
			this.loadImage(PlayGround.paths.get(this.ref.getName()));
		repaint();

	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if (isSelected == false)
			this.mouseExited(null);
	}

	public int getAttackPoints() {
		return attackPoints;
	}

	public void setAttackPoints(int attackPoints) {
		this.attackPoints = attackPoints;
		this.repaint();
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
		if (this.healthPoints <= 0)
			this.listener.onDieCard(this);
		this.repaint();
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}
	
	public void addEffect(){
		Minion m = (Minion)this.ref;
		if(m.isDivine())
			this.setBackground(new Color(200,100,0,100));
		else if(m.isSleeping())
			this.setBackground(new Color(0,0,0,100));
	}
	public void setSleeping(boolean sleeping){
		if(!sleeping)
			this.setBackground(new Color(0,0,0,0));
		else
			this.setBackground(new Color(0,0,0,100));
	}
	
	public void setDivine(boolean isDivine){
		if(isDivine)
			this.setBackground(new Color(100,100,0,100));
		else
			this.setBackground(new Color(0,0,0,100));
	}
	
	public void setName(String name){
		this.image2Path = PlayGround.paths.get(name+"_m");
		this.changeImage();
	}

	@Override
	public void setBackground(Color color) {
		this.background = color;
		super.setBackground(background);
		repaint();
	}
	
	public void resetBackground(){
		this.setBackground(background);
		repaint();
	}
	
	public void Heighlight(){
		Color tmp = background;
		this.setBackground(new Color(0,255,0,100));
		background = tmp;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.WIDTH, this.HEIGHT, this);
		if (isPlayed) {
			Font newFont = new Font("Courier New", 1, (int)(PWIDTH / 73.846)); // f = 26
			g.setColor(Color.WHITE);
			g.setFont(newFont);
			g.drawString(this.healthPoints + "", this.WIDTH / 2 + (int)(PWIDTH / 34.9090),
					this.HEIGHT / 2 + (int)(PHEIGHT / 17.1428)); // xm = 55 , ym = 63
			g.drawString(this.attackPoints + "",(int)(PWIDTH / 147.692), this.HEIGHT / 2 + (int)(PHEIGHT / 17.1428));
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (!hide) {
			if (!isPlayed && show) {
				if (nshow != 0)
					return;
				nshow++;
				WIDTH  += (int)(PWIDTH / 21.333); // w = 90
				HEIGHT += (int)(PHEIGHT / 12); // h = 90
				oldx = this.getX();
				oldy = this.getY();
				setBounds((int)(PWIDTH / 5.4857),PHEIGHT/2 - HEIGHT, WIDTH, HEIGHT); // x = 350
				this.discard.setBounds(0, 0, (int)(PWIDTH / 54.857),(int)(PHEIGHT/ 30.857)); // width = height = 35  
				add(discard);
				setBackground(new Color(0, 0, 0, 100));
				repaint();
				this.listener.onHoverCard();
				show = false;
				PlayGround.console.log(this.ref);
			} else if (!isPlayed && !isSpell && !show) {
				WIDTH  -= (int)(PWIDTH / 21.333);
				HEIGHT -= (int)(PHEIGHT / 12);
				nshow--;
				setBounds(oldx, oldy, WIDTH, HEIGHT);
				this.remove(this.discard);
				setBackground(new Color(0, 0, 0, 0));
				repaint();
				show = true;
				this.listener.onHoverCard();
				try {
					this.listener.onPlayCard(this);
					this.isPlayed = true;
					this.addEffect();
					this.HOVER = (int)(PHEIGHT / 216); // hover = 5
					this.EXTRA = 0;
					this.SHIFT = 0;
				} catch (Exception e) {
					PlayGround.console.log("Error : ",e.getMessage());
					e.printStackTrace();
				}
			}else if(!isPlayed && isSpell){ 
				System.out.println("starting to cast");
				try {
					this.listener.onCastSpell(this);
					nshow--;
				} catch (NotYourTurnException | NotEnoughManaException e) {
					PlayGround.console.log("Error : ",e.getMessage());
					e.printStackTrace();
					show = true;
					WIDTH -= (int)(PWIDTH / 21.333);
					HEIGHT -= (int)(PHEIGHT / 12);
					nshow--;
					setBounds(oldx, oldy, WIDTH, HEIGHT);
					this.remove(this.discard);
					setBackground(new Color(0, 0, 0, 0));
					repaint();
					this.listener.onHoverCard();
				}
			}else {
				isSelected = true;
				this.listener.onAttackAction(this);
				this.listener.onHoverCard();
				PlayGround.console.log((Minion) this.ref);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (!isSelected && show) {
			this.HEIGHT += HOVER;
			this.WIDTH += HOVER;
			setBounds(this.getX() - HOVER / 2 - SHIFT, this.getY() - HOVER / 2
					- EXTRA - SHIFT, this.WIDTH, this.HEIGHT);
			repaint();
			this.listener.onHoverCard();
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (!isSelected && !wait && show) {
			this.HEIGHT -= HOVER;
			this.WIDTH -= HOVER;
			setBounds(this.getX() + HOVER / 2 + SHIFT, this.getY() + HOVER / 2
					+ EXTRA + SHIFT, this.WIDTH, this.HEIGHT);
			repaint();
			this.listener.onHoverCard();
		}
		if (wait == true)
			wait = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void onClickButton(Button btn) {
		if(!show){
			WIDTH -= (int)(PWIDTH / 21.333);
			HEIGHT -= (int)(PHEIGHT / 12);
			nshow--;
			nshow = Math.max(nshow,0);
			setBounds(oldx, oldy, WIDTH, HEIGHT);
			this.HEIGHT -= HOVER;
			this.WIDTH -= HOVER;
			setBounds(this.getX() + HOVER / 2 + SHIFT, this.getY() + HOVER / 2
					+ EXTRA + SHIFT, this.WIDTH, this.HEIGHT);
			this.remove(this.discard);
			setBackground(new Color(0, 0, 0, 0));
			repaint();
			this.listener.onHoverCard();
			show = true;
			this.listener.clearRegisters();
		}
	}

	@Override
	public void redraw() {
		this.repaint();
		this.listener.onHoverCard();
	}
}
