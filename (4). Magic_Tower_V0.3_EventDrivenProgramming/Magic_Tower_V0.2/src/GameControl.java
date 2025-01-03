import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameControl {
	GameData gameData;
	Menu menu;
	GUI gui;
	GameControl(GameData gameData, Menu menu, GUI gui) {
		this.gameData = gameData;
		this.menu = menu;
		this.gui = gui;
	}
	void gameStart() {

		gui.f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				 char c = e.getKeyChar();
				 if(c=='a'||c=='s'||c=='d'||c=='w') {
					 handleInput(c);
					 gameData.printMap();
					 gui.refreshGUI();
				 }
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		gui.f.addMouseListener(new MouseListener() {
			@Override public void mouseClicked(MouseEvent e) {
				if(e.getButton()==e.BUTTON1){
					int mX = 0;
					int mY = 0;
					for(int i = 0; i < 5; i++) {
						if(e.getX() - 100*i - 6 <= 100) {
							mY = i;
							break;
						}
					}
					for(int i = 0; i < 5; i++) {
						if(e.getY() - 100*i - 30 <= 100) {
							mX = i;
							break;
						}
					}
					if(mX == gameData.pX && mY == gameData.pY - 1) {
						handleInput('a');
						gameData.printMap();
						gui.refreshGUI();
					}
					if(mX == gameData.pX + 1 && mY == gameData.pY) {
						handleInput('s');
						gameData.printMap();
						gui.refreshGUI();
					}
					if(mX == gameData.pX && mY == gameData.pY + 1) {
						handleInput('d');
						gameData.printMap();
						gui.refreshGUI();
					}
					if(mX == gameData.pX - 1 && mY == gameData.pY) {
						handleInput('w');
						gameData.printMap();
						gui.refreshGUI();
					}
				}
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		Scanner keyboardInput = new Scanner(System.in);
		
		while (true) {
			String input = keyboardInput.next();
			if (input.length() != 1 || (input.charAt(0) != 'a' && input.charAt(0) != 's' && input.charAt(0) != 'd'
					&& input.charAt(0) != 'w' && input.charAt(0) != '0')) {
				System.out.println("Wrong Input.");
				continue;
			}
			if (input.charAt(0) == '0')
				menu.enterMenu();
			else
				handleInput(input.charAt(0));
			gameData.printMap();
			gui.refreshGUI();
		}
	}
	
	void handleInput(char inC) {
		int tX = 0, tY = 0;
		if (inC == 'a') {
			tX = gameData.pX;
			tY = gameData.pY - 1;
		}
		if (inC == 's') {
			tX = gameData.pX + 1;
			tY = gameData.pY;
		}
		if (inC == 'd') {
			tX = gameData.pX;
			tY = gameData.pY + 1;
		}
		if (inC == 'w') {
			tX = gameData.pX - 1;
			tY = gameData.pY;
		}
		
		if (gameData.map[gameData.currentLevel][tX][tY] == 2) {
			gameData.keyNum++;
			moveHero(tX, tY);
		} else if (gameData.map[gameData.currentLevel][tX][tY] == 3 && gameData.keyNum > 0) {
			gameData.keyNum--;
			moveHero(tX, tY);
		} else if (gameData.map[gameData.currentLevel][tX][tY] == 4) {
			gameData.map[gameData.currentLevel][gameData.pX][gameData.pY] = 1;
			gameData.currentLevel++;
			for (int i = 0; i < gameData.H; i++)
				for (int j = 0; j < gameData.W; j++)
					if (gameData.map[gameData.currentLevel][i][j] == 6) {
						gameData.pX = i;
						gameData.pY = j;
					}
		} else if (gameData.map[gameData.currentLevel][tX][tY] == 5) {
			System.out.print("You Win!!");
			System.exit(0);
		} else if (gameData.map[gameData.currentLevel][tX][tY] > 10) {
			gameData.heroHealth += gameData.map[gameData.currentLevel][tX][tY];
			moveHero(tX, tY);
		} else if (gameData.map[gameData.currentLevel][tX][tY] == 1) {
			moveHero(tX, tY);
		} else if (gameData.map[gameData.currentLevel][tX][tY] < 0) {
			if (gameData.map[gameData.currentLevel][tX][tY] + gameData.heroHealth <= 0) {
				System.out.print("That monster has " + Integer.toString(-gameData.map[gameData.currentLevel][tX][tY])
						+ " power, You Lose!!");
				System.exit(0);
			} else {
				gameData.heroHealth += gameData.map[gameData.currentLevel][tX][tY];
				moveHero(tX, tY);
			}
		}
	}
	void moveHero(int tX, int tY) {
		gameData.map[gameData.currentLevel][gameData.pX][gameData.pY] = 1;
		gameData.map[gameData.currentLevel][tX][tY] = 6;
		gameData.pX = tX;
		gameData.pY = tY;
	}
}
