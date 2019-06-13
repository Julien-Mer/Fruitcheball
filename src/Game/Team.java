package Game;

public class Team {

	public int id;
	public int points;
	public int[] fruits;
	public Player[] players;
	public Cell[] cells;
	
	public Team(int id, Player[] players, int[] fruits, Cell[] cells, int points) {
		this.id = id;
		this.players = players;
		this.fruits = fruits;
		this.cells = cells;
		this.points = points;
	}
	
	public int distBase(Cell c1) {
		int res = 9999;
		for(Cell c : cells) {
			int dist = Cell.distanceCellsTheoric(c1, c);
			if(dist < res)
			 res = dist;
		}
		return res;
	}
	
}
