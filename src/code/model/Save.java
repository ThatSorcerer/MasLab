package code.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author slgreco, andreirv, ccaballe
 *
 */
public class Save {
	
	boolean initial = true;
	
	/**
	 * 
	 */
	
	String playerLine = "";
	
	/**
	 * 
	 */
	String tileLine = "";
	
	String lastIn;
	
	AbstractTile currentTile;
	/**
	 * @param players
	 * @param boardState
	 * @param lastInsertion
	 * @throws IOException 
	 */
	public Save(Player[] players, AbstractTile[][] boardState, int lastInsertion, AbstractTile tile) throws IOException {
		System.out.println("Saving");
		lastIn = "" + lastInsertion;
		currentTile = tile;
		for(int i = 0; i < players.length; i++) {
			System.out.println("Save player");
			playerLine += addNewPlayerState(i, players[i]);
			if(i<players.length-1){
				playerLine += ",";
			}
		}
		
		System.out.println(playerLine);
		for(int i = 0; i < (boardState.length * boardState.length); i++) {
		
			if (i == 0 && initial) {
				tileLine += addNewTileState(i, currentTile) + ",";
				initial = false;
				i-=1;
			}
			else {
				tileLine += addNewTileState(i, boardState[i/7][i%7]);
				if(i<(boardState.length * boardState.length)-1){
					tileLine += ",";
				}
			}
		}
		System.out.println(tileLine);
		writeSave();
	}
	
	/**
	 * @param i
	 * @param player
	 * @return
	 */
	private String addNewPlayerState(int i, Player player) {
		String newPlayerState = "[";
		
		newPlayerState += player.getName() + ",";
		newPlayerState += player.getColor() + ",";
		newPlayerState += player.getWands() + ",[";
		newPlayerState += player.getIngredient(1) + "," + player.getIngredient(2)+ ","+ player.getIngredient(3) + "],[";
		for(int p = 0; p<player.getTokens().size(); p++){
			newPlayerState += player.getTokens().get(p) + ",";
		}
		newPlayerState += "]]";
		
		/*
		 * 1) Get Name, then concatenate
		 * 2) Get Color, then concatenate
		 * 3) Get Wands, then concatenate
		 * 4.1) += "["
		 * 4.2) Get ingriedent 1, then concatenate
		 * 4.3) Get ingriedent 2, then concatenate
		 * 4.4) Get ingreident 3, then concatenate
		 * 4.5) += "]"
		 * 5.1) += "["
		 * 5.2) Get token index 0, then concatenate
		 * ...	...
		 * 5.n) Get token index n, then concatenate
		 * 5.n+1) += "]"
 		 */
		return newPlayerState;
	}
	
	/**
	 * @param n
	 * @param tile
	 * @return
	 */
	private String addNewTileState(int n, AbstractTile tile) {
		String newTileState = "[";
		newTileState += tile.getTileId() + ",";
		if(tile.hasToken()){
			newTileState += tile.getToken().getValue() + ",[";
		}
		else newTileState += 0 + ",[";
		for(int i = 0; i<tile.getPlayers().size(); i++){
			newTileState += tile.getPlayers().get(i).getColor();
			if(tile.getPlayers().size()<i){
				newTileState += ",";
			}
		}
		newTileState += "]]";
		return newTileState;
	}
	
	private void writeSave() throws IOException {
		List<String> line = Arrays.asList(playerLine, tileLine, lastIn);
		File blah = new File("mySave.mls");
		if (blah.createNewFile()){
	        System.out.println("File is created!");
	      }else{
	        System.out.println("File already exists.");
	      }
		Path file = Paths.get("mySave.mls");
		Files.write(file, line, Charset.forName("UTF-8"));
	}
	
}
