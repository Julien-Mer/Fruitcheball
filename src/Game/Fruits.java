package Game;

import java.util.ArrayList;

import Client.Client;

public class Fruits {

	public int type;
	public ArrayList<Cell> cells;
	
	public Fruits(int type) {
		cells = new ArrayList<Cell>();
		this.type = type;
	}
	
	public void addCell(Cell cell) {
		cells.add(cell);
	}
	
	public int getNumber() {
		return cells.size();
	}
	
	public static Cell getNearestFruit(Player p, Cell ori, int fruit) {
		ArrayList<Cell> allFruits = new ArrayList<Cell>();
		Fruits f = Client.cli.fruits[fruit];
		for(Cell cF : f.cells)
			if(!Client.cli.targetedFruits.contains(cF))
				allFruits.add(cF);
		return Cell.getNearestCellEmptyTheoric(p, ori, allFruits);
	}
	 
	public static Cell getNearestFruit(Player p, Cell ori) {
		ArrayList<Cell> allFruits = new ArrayList<Cell>();
		for(Fruits f : Client.cli.fruits) {
			if(f.type != Constants.CHATAIGNE) // Il faut être attardé pour ramasser des chataignes
				for(Cell cF : f.cells)
					if(!Client.cli.targetedFruits.contains(cF))
						allFruits.add(cF);
		}
		return Cell.getNearestCellEmptyTheoric(p, ori, allFruits);
	}
	
	public static Cell getNearestFruit2(Cell c1, Cell c2, int fruit) {
		Fruits f = Client.cli.fruits[fruit];
		ArrayList<Cell> allFruits = new ArrayList<Cell>();
		for(Cell cF : f.cells)
			if(!Client.cli.targetedFruits.contains(cF))
				allFruits.add(cF);
		return Cell.getNearestCellEmptyTheoric2(c1, c2, allFruits);
	}
	
}
