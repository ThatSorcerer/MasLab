package code.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Restore {
	/**
	 * @author slgreco, andreirv
	 */
	private String dir = "";
	private String path = "mySave.mls";
	private File restoreFile = new File(dir + path);
	/**
	 * @author slgreco, andreirv
	 */
	private String playerLine = null; // This saves the line that contains Player data
	/**
	 * @author slgreco, andreirv
	 */
	private String currentPlayerData = ""; // This saves the current Player being restored
	
	/**
	 * @author slgreco, andreirv
	 */
	private String tileLine = null;
	
	/**
	 * @author slgreco, andreirv
	 */
	private String currentTileData = "";
	
	private String lastInsertLine;
	
	/**
	 * @author slgreco, andreirv
	 */ 
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Player[] restoredPlayers;
	
	public AbstractTile[] boardState = new AbstractTile[50];
	
//	public Restore(){
//		players =  new ArrayList();
//		if(saveExists()){
//			sendPlayers();
//		}
//	}
	
	private void getRestorableLines() {
		try {
		
			FileReader fileReader = new FileReader(dir + path);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			System.out.println("Begining Restore of Players");
			playerLine = bufferedReader.readLine();
			System.out.println("Restoring Player from: " + dir + path + " | " + playerLine);
			
			System.out.println("Begining Restore of boardState");
			tileLine = bufferedReader.readLine();
			System.out.println("Restoring Board State from: " + dir + path + " | " + tileLine);
			
			lastInsertLine = bufferedReader.readLine();
			
			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println(
	                "Unable to open file '" + 
	                dir + path + "'");  
		}
		catch (IOException e) {
			System.out.println(
	                "Error reading file '" 
	                + dir + path + "'");                  
	            // Or we could just do this: 
	            // e.printStackTrace();
		}		
	}
	
	/**
	 * @author slgreco, andreirv
	 */
	private void getCurrentPlayerData(int shift) {
		currentPlayerData = "";
		int pos = shift;
		System.out.println(">>" +shift);
		char ch0 = '[',
			 ch1 = 'n',
			 ch2 = 'n';
//		System.out.println(playerLine.length()-1 + "||" + playerLine.charAt(playerLine.length()-1));
		while(true) {
			ch0 = playerLine.charAt(pos);
			
			if(pos > 0) {ch1 = playerLine.charAt(pos-1);}
			if(pos < playerLine.length()-1) {
				ch2 = playerLine.charAt(pos+1);
				} 
			else { 
				ch2 = ','; 
				}
			if (ch1 != ']' || ch0 != ']' || ch2 != ',') {
//				System.out.println(pos+"|"+currentPlayerData);
				currentPlayerData += ch0;
				pos++;
			}
			else {
				currentPlayerData += ch0;
				shift = pos + 2;
				break;
			}
			
		}
	//	currentPlayerData += "]";
		System.out.println(">>Begining Restore for: " + currentPlayerData);
		restorePlayer();
		
	}
	
	/**
	 * @author slgreco, ccaballe
	 */
	int shift2 = 0;
	private void getCurrentTileData() {
		
		int pos = shift2;
		currentTileData = "";
		char ch0 = '[',
			 ch1 = 'n';
		
		while( ch0 != ']' && ch1 != ']') {
			//Update char @ pos and pos +1
			ch0 = tileLine.charAt(pos);
			ch1 = tileLine.charAt(pos+1);
			pos++;
			currentTileData = currentTileData + ch0;
		}
		currentTileData += "]]";
		System.out.println("Begining Restore for: " + currentTileData);
	}
	
	/**
	 * @author slgreco
	 */
	int shift = 0;
	private void restorePlayer() {
		System.out.println("Begins");
		int pos = 0;
		String myName = "";
		String myColor = "";
		int ing1, ing2, ing3;
		String in1="", in2="", in3="";
		ArrayList<Token> myTokens = new ArrayList<Token>();
		int myWands;
		char ch;
		while(true) {
			pos+=1;
			ch = currentPlayerData.charAt(pos);
			if (ch != ',') {
				myName += ch;
			}
			else {
				System.out.println("Name Done");
				pos += 1;
				break;
			}
		}
		while(true) {
			ch = currentPlayerData.charAt(pos);
			if (ch != ',') {
				pos+=1;
				myColor += ch;
			}
			else {
				pos += 1;
				System.out.println("Color Done");
				break;
			}
		}
		ch = currentPlayerData.charAt(pos);
		myWands = Character.getNumericValue(ch);
		pos+=3;
		ch = currentPlayerData.charAt(pos);
		while(true) {
			ch = currentPlayerData.charAt(pos);
			if (ch != ',') {
				pos+=1;
				in1 += ch;
			}
			else {
				pos += 1;
				break;
			}
		}
		while(true) {
			ch = currentPlayerData.charAt(pos);
			if (ch != ',') {
				pos+=1;
				in2 += ch;
			}
			else {
				pos += 1;
				break;
			}
		}
		while(true) {
			ch = currentPlayerData.charAt(pos);
			if (ch != ']') {
				pos+=1;
				in3 += ch;
			}
			else {
				pos += 1;
				break;
			}
		}
		GenericFormulaCard myCard = new GenericFormulaCard(Integer.parseInt(in1), Integer.parseInt(in2), Integer.parseInt(in3));
		pos += 2;
		System.out.println(pos + " |" +currentPlayerData.charAt(pos)+  "|" + "Card Done");
		
		while(true) {
			ch = currentPlayerData.charAt(pos);
			if (ch != ']') {
				pos+=1;
				if (ch == ',') {
					pos+= 1; 
				}
				myTokens.add(new Token(Character.getNumericValue(ch), "Blah"));
			}
			else {
				pos+=3;
				break;
			}
		}
		
		Player me = new Player(myName, myColor, myWands, myTokens, myCard);
		System.out.println(me.getCard());
		players.add(me);
		shift += pos;
		if (shift< playerLine.length()) { getCurrentPlayerData(pos); }
	}

	/**
	 * @author slgreco
	 */
	int tileFocus = 0; // This is the position atwhich the Tile will be added to the boardState[]
	
	public AbstractTile[] restoreBoard() {
		
		int pos = 0;
		//currentTileData.length()-2;
		
		Token token;
		
		char ch = currentTileData.charAt(pos+=1);
		System.out.println(ch);
		char chRot = currentTileData.charAt(pos+=1);
		
		int x = pos%7;
		int y = pos/7;
		if (x%2 == 0 && y%2 == 0) {
			boardState[tileFocus] = new FixedTile(""+ch);	
			boardState[tileFocus].rotate(Character.getNumericValue(chRot)*90);
		} else {
			boardState[tileFocus] = new MoveableTile(""+ch);
			boardState[tileFocus].rotate(Character.getNumericValue(chRot)*90);
		}
		
		ch = currentTileData.charAt(pos+=2);
		int value = Character.getNumericValue(ch);
		if (value != 0) {
			token = new Token(value, "NAME");
			boardState[tileFocus].setToken(token);
		}
		
		pos+=3;
		
		while(ch != ']') {
			//pos+=1
			ch = currentTileData.charAt(pos);
			char ch2 = currentTileData.charAt(pos+1);
			String color = "";
			
			while(ch != ',' && (ch != ']' || ch2 != ']')) {
				color = ch + color;
				ch = currentTileData.charAt(pos+=1);
				ch2 = currentTileData.charAt(pos+1);
			}
			for(int i = 0; i < restoredPlayers.length; i++) {
				if (restoredPlayers[i].getColor() == color) { boardState[tileFocus].addPlayer(restoredPlayers[i]); }
			}
			pos++;
		}
		pos+=2;
		tileFocus++;
		System.out.println(tileFocus + "| "+ shift2 + " | " + tileLine.length() + "|| " + (shift >= currentTileData.length()));
		if (tileFocus < boardState.length-1) {
			System.out.println("Returning");
			return boardState;
		}else {
			shift2+=pos;
			System.out.println("NewTile");
			getCurrentTileData();
			restoreBoard();	
		}
		return boardState;
	}
	
	/**
	 * @author slgreco, ccaballe
	 */
	public Restore restoreSave() {
		
		getRestorableLines();
		
//		getPlayerLine();
		System.out.println("----");
		getCurrentPlayerData(0);
		System.out.println("----");
		//restorePlayer();
		
		restoredPlayers = new Player[players.size()];
		for (int i = 0; i < players.size(); i++) {
			restoredPlayers[i] = players.get(i);
		}
		
		players.clear();
		
//		getTilesLine();
		
		getCurrentTileData();
		restoreBoard();
		
		return this;
		
	}
	
	/**
	 * @author slgreco, andreirv
	 */ boolean saveExists(){
		return restoreFile.exists() && restoreFile.isFile();
	}
}
