package view.components;

import java.util.*;
import java.awt.*;
import javax.swing.*;

@SuppressWarnings ("serial")
public class Field extends JPanel {

	ArrayList<CardView> cards;
	private int HEIGHT;
	private int WIDTH;

	public Field(int WIDTH, int HEIGHT) {
		this.HEIGHT = HEIGHT;
		this.WIDTH = WIDTH;
		this.cards = new ArrayList<CardView>();
		initUI();
	}

	private void initUI() {
		setBackground(new Color(255,255,255,0));
		setPreferredSize(new Dimension(this.WIDTH,this.HEIGHT));
	}

	public void addCard(CardView c) {
		this.cards.add(c);
		this.locateCards();
	}
	
	public void  removeCard(CardView card){
		remove(card);
		this.cards.remove(card);
		this.locateCards();
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

	private void locateCards(){
		if (cards.size() == 0) {
			repaint();
			return;
		}
		CardView tmp = cards.get(0);
		int n = cards.size();
		int over = 0;
		int shift = -30;
		int w = tmp.getWidth() * n - over * (n - 1);
		int x = this.WIDTH / 2 - w / 2;
		int y = this.HEIGHT - tmp.getHEIGHT() + shift ;
		int dx = tmp.getWIDTH() - over;
		for (CardView c : cards) {
			c.setBounds(x, y, c.getWIDTH(), c.getHEIGHT());
			x += dx;
			add(c);
		}
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	public void redraw(){
		this.repaint();
	}
}
