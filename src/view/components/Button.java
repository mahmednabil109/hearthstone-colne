package view.components;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

@SuppressWarnings("serial")
public class Button extends JPanel implements MouseListener {

	private Image img;
	private ButtonListener listener;
	private String optionalString;
	private int HEIGHT;
	private int WIDTH;
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean icon = true;
	public final int Margin = 47;

	public Button(int HEIGHT, int WIDTH, String imagePath, String optionalText) {
		this.HEIGHT = HEIGHT;
		this.WIDTH = WIDTH;
		this.optionalString = optionalText;
		loadImage(imagePath);
		initUI();
	}

	private void loadImage(String imagePath) {
		ImageIcon ii = new ImageIcon(imagePath);
		this.img = ii.getImage();
	}

	public void changeImage(String imagePath) {
		ImageIcon ii = new ImageIcon(imagePath);
		this.img = ii.getImage();
		this.repaint();
		this.listener.redraw();
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public void setText(String text) {
		this.optionalString = text;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}

	public void initUI() {
		// Cursor c = new Cursor(Cursor.HAND_CURSOR);
		setBackground(new Color(255, 255, 255, 0));
		// this.setCursor(c);
		// this.setSize(this.WIDTH,this.HEIGHT);
		setFocusable(true);
		addMouseListener(this);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.WIDTH, this.HEIGHT, this);
		if (this.optionalString != null) {
			Font newFont = new Font("Courier New", 1, 28);
			g.setColor(Color.WHITE);
			g.setFont(newFont);
			g.drawString(this.optionalString, this.WIDTH - 49, this.HEIGHT - 69);
		}
		if(this.data != null){
			Font newFont = new Font("Courier New", 1, 18);
			g.setColor(Color.WHITE);
			g.setFont(newFont);
			g.drawString(this.data,this.WIDTH/2-this.data.length()/2*11,this.HEIGHT/2+4);
		}
	}

	public void setListener(ButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		this.repaint();
		if (listener != null)
			this.listener.redraw();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		try {
			this.listener.onClickButton(this);
		} catch (IOException | CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (icon) {
			this.HEIGHT += 10;
			this.WIDTH += 10;
			setBounds(this.getX() - 5, this.getY() - 5, this.WIDTH, this.HEIGHT);
			repaint();
			this.listener.redraw();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (icon) {
			this.HEIGHT -= 10;
			this.WIDTH -= 10;
			setBounds(this.getX() + 5, this.getY() + 5, this.WIDTH, this.HEIGHT);
			repaint();
			this.listener.redraw();
		}
	}
}
