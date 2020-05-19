package view.components;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import view.PlayGround;

@SuppressWarnings("serial")
public class ManaCrystalBar extends JPanel {
	
	public Image bground, crystal;
	public static final int PWIDTH = PlayGround.WIDTH , PHEIGHT = PlayGround.HEIGHT;
	public static final int WIDTH = (int) (PWIDTH / 4.3537),HEIGHT = (int)(PHEIGHT/15.8823);
	public int limit = 10;
	public int CW = (int)(PWIDTH / 60); // CW =32
	public int CH = (int)(PHEIGHT / 33.75); // CH =32
	public int CX = (int)(WIDTH /4.9); //x = 90
	public int CY = (int)(HEIGHT /4);   // y = 10
	public int CM = (int)(PWIDTH / 400);
	public int CDX = CW + CM;
	public int nMana = 0;
	
	public ManaCrystalBar(int limit){
		this.limit = limit;
		loadImages();
		initUI();
	}
	
	private void loadImages(){
		ImageIcon ii = new ImageIcon(
				"img\\crystal.png");
		this.crystal = ii.getImage();
		ii = new ImageIcon("img\\health_power.png");
		this.bground = ii.getImage();
		repaint();
	}
	
	private void initUI(){
		setBackground(new Color(0,0,0,0));
		setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
	}
	
	public void incCrystal(int n) {
		nMana += n;
		repaint();
	}

	public void decCrystal(int n) {
		nMana -= n;
		repaint();
	}
	
	public void setManaCrystal(int n){
		this.nMana = n;
		repaint();
	}
	
	public void setLimit(int limit){
		this.limit = limit;
	}
	
	@Override
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		for (int i = 0; i < nMana && i < limit; i++) {
			g.drawImage(this.crystal, CX + i * CDX, CY, this.CW, this.CH, this);
		}
		Font newFont = new Font("Courier New", 1,(int)(PWIDTH / 73.846));
		g.setColor(Color.WHITE);
		g.setFont(newFont);
		g.drawString(this.nMana + "/10",0,this.HEIGHT/2 + 10);
	}

}
