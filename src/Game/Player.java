package Game;

public class Player {
	
	public Cell cell; 
	public int inventaire; 
	public int id; 
	
	public Player(Cell cell, int id, String inventaire) {
		this.cell = cell;
		if(!inventaire.equalsIgnoreCase("x"))
			this.inventaire = Integer.valueOf(inventaire);
		else 
			this.inventaire = -1;
		this.id = id;
		cell.addPlayer(this);
	}
}
