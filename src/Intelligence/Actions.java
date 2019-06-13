package Intelligence;

import Game.Cell;
import Game.Player;

public class Actions {
	
    public static String move(Player player, Cell dest) {
    	Cell ori = player.cell;
    	String instruction = "X";
    	if(ori.posX == dest.posX +1)
    		instruction = "O";
    	else if(ori.posX == dest.posX - 1)
    		instruction = "E";
    	else if(ori.posY == dest.posY +1)
    		instruction = "N";
    	else if(ori.posY == dest.posY - 1)
    		instruction = "S";
    	player.cell = dest;
    	ori.player = null;
    	dest.player = player;
    	return instruction;
    }
    
}
