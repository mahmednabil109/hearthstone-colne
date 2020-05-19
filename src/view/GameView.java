package view;

import java.awt.*;

import javax.swing.*;


@SuppressWarnings("serial")
public class GameView extends JFrame implements PlayGroundListener {

	public GameView() {
		initUI();
	}

	private void initUI() {
		add(new PlayGround(this));
		setUndecorated(true);
		setResizable(false);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(() -> {
			new GameView().setVisible(true);
		});
	}

	@Override
	public void exit() {
		System.exit(0);
		
	}
}
