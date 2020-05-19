package view.components;

import java.util.*;
import javax.swing.*;
import view.PlayGround;
import model.cards.*;
import model.cards.minions.*;

import java.awt.*;

@SuppressWarnings("serial")
public class Console extends JPanel {

	private ArrayList<String> data;
	public static final int PWIDTH = PlayGround.WIDTH,
			PHEIGHT = PlayGround.HEIGHT;
	int x = 5;
	int y = 22;
	int dy = 15;

	public Console() {
		data = new ArrayList<String>();
		initUI();
	}

	public void log(Minion m) {
		this.data.clear();
		String tmp = "name: " + m.getName();
		data.add(tmp);
		tmp = "attack: " + m.getAttack();
		data.add(tmp);
		tmp = "health points: " + m.getCurrentHP();
		data.add(tmp);
		tmp = "manacost: " + m.getManaCost();
		data.add(tmp);
		tmp = "rarity: " + m.getRarity();
		data.add(tmp);
		tmp = "divin: " + m.isDivine();
		data.add(tmp);
		tmp = "sleep: " + m.isSleeping();
		data.add(tmp);
		tmp = "tauntt: " + m.isTaunt();
		data.add(tmp);
		tmp = "charge: " + !m.isSleeping();
		data.add(tmp);
		tmp = "attacked: " + m.isAttacked();
		data.add(tmp);
		repaint();
	}

	public void log(Card c) {
		this.data.clear();
		String tmp = "name: " + c.getName();
		data.add(tmp);
		tmp = "manacost: " + c.getManaCost();
		data.add(tmp);
		tmp = "rarity: " + c.getRarity();
		data.add(tmp);
		repaint();
	}
	
	public void log(String s , Card c){
		this.data.clear();
		String tmp = s;
		data.add(tmp);
		tmp = "name: " + c.getName();
		data.add(tmp);
		tmp = "manacost: " + c.getManaCost();
		data.add(tmp);
		tmp = "rarity: " + c.getRarity();
		data.add(tmp);
		if(c instanceof Minion){
			Minion m = (Minion) c;
			tmp = "attack: " + m.getAttack();
			data.add(tmp);
			tmp = "health points: " + m.getCurrentHP();
			data.add(tmp);
			tmp = "divin: " + m.isDivine();
			data.add(tmp);
			tmp = "sleep: " + m.isSleeping();
			data.add(tmp);
			tmp = "tauntt: " + m.isTaunt();
			data.add(tmp);
			tmp = "charge: " + !m.isSleeping();
			data.add(tmp);
			tmp = "attacked: " + m.isAttacked();
			data.add(tmp);
		}
		repaint();
	}

	public void log(String s) {
		this.data.clear();
		String tmps[] = s.split(",");
		for (String tmp : tmps)
			data.add(tmp);
		repaint();
	}

	public void log(String... tmps) {
		this.data.clear();
		for (String tmp : tmps)
			data.add(tmp);
		repaint();
	}

	private void initUI() {
		setBackground(new Color(0, 0, 0, 200));
		setLayout(null);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (data.size() == 0)
			return;
		Font newFont = new Font("Courier New", 1, (int) (PWIDTH / 115.846)); // f = 19
		g.setColor(Color.GREEN);
		g.setFont(newFont);
		int tmpx = x;
		int tmpy = y;
		for (String s : data) {
			g.drawString(s, tmpx, tmpy);
			tmpy += dy;
		}
	}
}
