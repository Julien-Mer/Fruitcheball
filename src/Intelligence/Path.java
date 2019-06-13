package Intelligence;

import java.util.ArrayList;

import Client.Client;
import Game.*;

public class Path {
	public static ArrayList<Cell> bestPath;
	public static ArrayList<Cell> newPath;
	public static ArrayList<Cell> dodge;
	public static int best;
	
	public static ArrayList<Cell> getNearestPath(Cell ori, Cell dest) {
		best = Cell.distanceCellsTheoric(ori, dest) + 4; // On admet 4 déplacements en plus si il y a des obstacles
		bestPath = new ArrayList<Cell>();
		newPath = new ArrayList<Cell>();
		dodge = new ArrayList<Cell>();
		getNearestPath2(ori, dest);
		/* Affichage des résultats (DEBUG)
		System.out.println("Déplacement");
		System.out.println("Best: " + best);
		for(Cell cell : bestPath) {
			System.out.print("["+cell.posX +","+cell.posY+"] ");
		}
		System.out.print("\n");
		*/
		return bestPath;
	}
	
	public static String approxThrow(Cell ori, Cell dest) { // Lancer approximatif
		String posX = "X";
		String posY = "X";
		int diffX = (ori.posX - dest.posX);
		int diffY = (ori.posY - dest.posY);
		if(diffX < 0) {
			diffX=-diffX;
			posX = "E";
		} else {
			posX = "O";
		}
		if(diffY < 0) {
			diffY=-diffY;
			posY = "S";
		} else {
			posY = "N";
		}
		if(diffY > diffX) return posY;
		else return posX;
	}
	
    public static void getNearestPath2(Cell ori, Cell dest) {
	        if(ori == dest) return;
	        ArrayList<Cell> cellsNear = new ArrayList<>();
	        int posX = ori.posX;
	        int posY = ori.posY;
	        Cell cell = null;
	        int[][] nears = { {posX-1, posY}, {posX+1, posY}, {posX, posY+1}, {posX, posY-1} };
	        for(int[] coord : nears) {
	            if(Cell.isValid(coord[0], coord[1]))
	                cell = Client.cli.map[coord[0]][coord[1]];
	                if(!dodge.contains(cell) && !newPath.contains(cell) && cell.player == null && !cell.wall) { 
	                    cellsNear.add(cell);
	                }
	        }
	        for(Cell nextCell : cellsNear) {
	            newPath.add(nextCell);
	            if(newPath.size() < best) {
	                getNearestPath2(nextCell, dest);
	                if(newPath.size() > 0 && newPath.size() < best && newPath.contains(dest)) {
	                    bestPath = (ArrayList<Cell>) newPath.clone();
	                    best = newPath.size();
	                }
	            }
	            newPath.remove(nextCell);
	        }
	    return;
    }
    
    public static String getOrientation(Cell ori, Cell dest) {
    	String res = "X";
    	if(ori.posX < dest.posX)
    		res = "E";
    	else if(ori.posX > dest.posX)
    		res = "O";
    	else if(ori.posY < dest.posY)
    		res = "S";
    	else if(ori.posY > dest.posY)
    		res = "N";
    	return res;
    }
    
}