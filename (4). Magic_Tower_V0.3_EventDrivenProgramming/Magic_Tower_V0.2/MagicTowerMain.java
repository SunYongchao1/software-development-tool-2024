import java.io.*;
import java.util.Scanner;

public class MagicTowerMain {
	static int L, H, W, currentLevel=0;
	static int pX=3, pY=3, heroHealth=105, keyNum=0;
	static int [][][]map;
	static String[] menu = {"Menu",
		"Restart or Quit",
		"Save or Load",
		"Restart",
		"Quit",
		"Save",
		"Load"
	};
	public static void MagicTowerMain(String[] args) {
		// TODO Auto-generated method stub
		map = readMapFromFile("Map.in");
		printMap(currentLevel,map);
		Scanner keyboardInput = new Scanner(System.in); 
		while(true)
		{	
			String input=keyboardInput.next();
			if(input.length()!=1||
				(input.charAt(0)!='a'&&input.charAt(0)!='s'
					&&input.charAt(0)!='d'&&input.charAt(0)!='w'
					&&input.charAt(0)!='0'))
				continue;
			if(input.charAt(0)=='0')
				menuOperation(0);
			else
				handleInput(input.charAt(0),map);
			printMap(currentLevel,map);
		}
	}

	public static int[][][] readMapFromFile(String filePath) {
		int[][][] map=null;
		try {
			Scanner in = new Scanner(new File(filePath));
			L = in.nextInt();
			H = in.nextInt();
			W = in.nextInt();
			map = new int[L][H][W];
			for (int i = 0; i < L; i++)
				for (int j = 0; j < H; j++)
					for (int k = 0; k < W; k++)
						map[i][j][k] = in.nextInt();
		} catch (IOException e) {
			System.out.println("Error with files:" + e.toString());
		}
		return map;
	}
	
	public static void printMap(int level, int [][][]map) {
		char C[] = { 'W', '_', 'K', 'D', 'S', 'E', 'H' };
		for (int j = 0; j < H; j++) {
			for (int k = 0; k < W; k++) {
				if (map[level][j][k] < 0)
					System.out.print("M ");
				else if (map[level][j][k] > 10)
					System.out.print("P ");
				else
					System.out.print(C[map[level][j][k]] + " ");
			}
			System.out.print("\n\n");
		}
		System.out.print("Health:"+Integer.toString(heroHealth)+
				"  KeyNum:"+Integer.toString(keyNum)
				+"  Menu:press 0\n");
	}
	public static void handleInput(char inC, int map[][][])
	{
		int tX=0, tY=0;
		if(inC=='a') {tX = pX; tY = pY-1;}
		if(inC=='s') {tX = pX+1; tY = pY;}
		if(inC=='d') {tX = pX; tY = pY+1;}
		if(inC=='w') {tX = pX-1; tY = pY;}
		if(map[currentLevel][tX][tY]==2) {
			keyNum++; 
			map[currentLevel][pX][pY]=1;
			map[currentLevel][tX][tY]=6; 
			pX=tX; pY=tY;
		}
		else if(map[currentLevel][tX][tY]==3&&keyNum>0){
			keyNum--; 
			map[currentLevel][pX][pY]=1;
			map[currentLevel][tX][tY]=6; 
			pX=tX; pY=tY;
		}
		else if(map[currentLevel][tX][tY]==4) {
			map[currentLevel][pX][pY]=1;
			currentLevel++;
			for(int i=0;i<H;i++)
				for(int j=0;j<W;j++)
					if(map[currentLevel][i][j]==6) {
						pX=i;pY=j;
					}
		}
		else if(map[currentLevel][tX][tY]==5) {
			System.out.print("You Win!!");
			System.exit(0);
		}
		else if(map[currentLevel][tX][tY]>10) {
			heroHealth+=map[currentLevel][tX][tY];
			map[currentLevel][pX][pY]=1;
			map[currentLevel][tX][tY]=6;
			pX=tX; pY=tY;
		}
		else if(map[currentLevel][tX][tY]==1) {
			map[currentLevel][pX][pY]=1;
			map[currentLevel][tX][tY]=6;
			pX=tX; pY=tY;
		}
		else if(map[currentLevel][tX][tY]<0) {
			if(map[currentLevel][tX][tY]+heroHealth<=0) {
				System.out.print("That monster has " + 
			Integer.toString(-map[currentLevel][tX][tY]) + " power, You Lose!!");
				System.exit(0);
			}
			else {
				heroHealth+=map[currentLevel][tX][tY];
				map[currentLevel][pX][pY]=1;
				map[currentLevel][tX][tY]=6;
				pX=tX; pY=tY;
			}
		}
	}
	public static void menuOperation(int selection)
	{
		if(selection == 3)
			restartGame(); 
		else if(selection == 4)
			quitGame();
		else if(selection == 5)
			saveGame();
		else if(selection == 6)
			loadGame();
		else {
			while(true){	
				System.out.println("***"+menu[selection]+"***");
				System.out.println(Integer.toString(selection*2+1)
						+"."+menu[selection*2+1]);
				System.out.println(Integer.toString(selection*2+2)
						+"."+menu[selection*2+2]);
				System.out.println("0.Back to UpperMenu");
				Scanner keyboardInput = new Scanner(System.in); 
				String input=keyboardInput.next();
				if(Integer.parseInt(input)==0)
					return;
				else if(Integer.parseInt(input)==selection*2+1
						||Integer.parseInt(input)==selection*2+2)
					menuOperation(Integer.parseInt(input));
			}
		}
	}

	private static void restartGame() {
		pX=3; pY=3; heroHealth=105; keyNum=0;currentLevel=0;
		map=readMapFromFile("Map.in");
		System.out.println("Game Restarted!!");
	}

	private static void quitGame() {
		System.exit(0);		
	}
	
	private static void saveGame() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("SavedMap.in"));
            bw.write(Integer.toString(L));
            bw.write(" "+Integer.toString(H));
            bw.write(" "+Integer.toString(W)+"\n");
            for(int i=0;i<L;i++)
            	for(int j=0;j<H;j++) {
            		for(int k=0;k<W;k++)
            			bw.write(Integer.toString(map[i][j][k])+" ");
            		bw.write("\n");
            	}
            bw.close();
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("SavedHero.in"));
            bw1.write(Integer.toString(currentLevel));
            bw1.write(" "+Integer.toString(pX));
            bw1.write(" "+Integer.toString(pY));
            bw1.write(" "+Integer.toString(heroHealth));
            bw1.write(" "+Integer.toString(keyNum)+"\n");
            bw1.close();
            System.out.println("Game Saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private static void loadGame() {
		map=readMapFromFile("SavedMap.in");
		try {
			Scanner in = new Scanner(new File("SavedHero.in"));
			currentLevel=in.nextInt();
			pX=in.nextInt();
            pY=in.nextInt();
            heroHealth=in.nextInt();
            keyNum=in.nextInt();
            System.out.println("Game Loaded!");
		}catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}
