package Game;

import java.util.ArrayList;

import Client.Client;
import Intelligence.Path;

public class Cell {

	public boolean wall;
	public boolean empty;
	public Player player;
	public int fruit;
	public int posX;
	public int posY;
	
	public Cell(int posX, int posY, String value) {
		this.posX = posX;
		this.posY = posY;
		if(value.equalsIgnoreCase("x"))
			this.wall = true;
		else if(value.equalsIgnoreCase(".")) {
			if(player == null)
				this.empty = true;
		} else {
			this.fruit = Integer.valueOf(value);
			Client.cli.fruits[this.fruit].addCell(this);
		}
	}
	
	public void addPlayer(Player player) {
		this.player = player;
	}
	
    public static int distanceCellsTheoric(Cell ori, Cell dest) { // Nombre de cases à parcourir entre 2 cases (sans obstacles)
	    int diffX = (ori.posX - dest.posX);
		int diffY = (ori.posY - dest.posY);
		if(diffX < 0) diffX=-diffX;
		if(diffY < 0) diffY=-diffY;
		return diffX + diffY;
    }
    
	public static Cell getNearestCellTheoric(Cell ori, ArrayList<Cell> cells) {
		int dist = 9999;
		int dist2;
		Cell res = null;
		for(Cell c : cells) {
			dist2 = distanceCellsTheoric(ori, c);
			if(dist2 < dist) {
				dist = dist2; 
				res = c;
			}
				
		}
		return res;
	}
	
	public static Cell getNearestCellEmptyTheoric(Player p, Cell ori, ArrayList<Cell> cells) {
		int dist = 9999;
		int dist2;
		Cell res = null;
		for(Cell c : cells) {
			if(c.player == null || c.player != null && c.player == p) {
				dist2 = distanceCellsTheoric(ori, c);
				if(dist2 < dist) {
					dist = dist2; 
					res = c;
				}
			}
				
		}
		return res;
	}
	
	public static Cell getNearestCellEmptyTheoric2(Cell c1, Cell c2, ArrayList<Cell> cells) {
		int dist = 9999;
		int dist2;
		Cell res = null;
		for(Cell c3 : cells) {
			if(c3.player == null) {
				dist2 = distanceCellsTheoric(c1, c3) + distanceCellsTheoric(c2, c3);
				if(dist2 < dist) {
					dist = dist2; 
					res = c3;
				}
			}
				
		}
		return res;
	}
	
	public static boolean isValid(int posX, int posY) {
		return posX >= 0 && posX < Client.cli.largeur && posY >= 0 && posY < Client.cli.longueur;
	}
	
}
