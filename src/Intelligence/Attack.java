package Intelligence;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import Client.Client;
import Game.*;

public class Attack {
	
	static Boolean[] blocked;
	static ArrayList<Cell> attackableCells;
	static Player player;
	static Cell cellAttack;
	static Cell cellAttacked;
	static String instruction;
	
	public static Team getBestTeam() {
		int best = -5000; // Pour attaquer les équipes en négatif aussi, c'est important de les enfoncer :)
		Team bT = null;
		for(Team t : Client.cli.teams) {
			if(t.id != Integer.valueOf(Client.cli.id)) { // Parce qu'il faut etre sacrément con pour s'attaquer soi-même
				int nbr = t.fruits[Constants.CERISE] + t.fruits[Constants.FRAMBOISE] + t.fruits[Constants.PRUNE] + t.fruits[Constants.MIRABELLE];
				if(t.points >= best || nbr > 3) {
					bT = Client.cli.teams[t.id]; // Trouvé <3
					best = t.points;
				}
			}
		}
		return bT; // L'heureux gagnant
	}
	
	public static void addAttackableCell(int posX, int posY, int i) {
		if(Cell.isValid(posX, posY)) { // Case dans les limites du terrain
			cellAttack = Client.cli.map[posX][posY];
			if(cellAttack.wall || cellAttack.player != null && cellAttack.player.id == 0 && cellAttack != player.cell)
				blocked[i] = true; // On bloque la ligne de vue
			if(!blocked[i]) {
				if(cellAttack == player.cell) { // Il peut déjà attaquer
					instruction = "L" + Path.getOrientation(player.cell, cellAttacked); 
				} else {
					attackableCells.add(cellAttack); // Sinon la cell s'ajoute à la liste potentielle
				}
				
			}
		}
	}
	
	public static void attackablePossibilities(Cell c) {
		cellAttacked = c;
		blocked = new Boolean[5];
		Arrays.fill(blocked, Boolean.FALSE);
		int posX = c.posX;
		int posY = c.posY;
		for(int i = 0; i < 4; i++) { // 16 positions d'attaque <3
			addAttackableCell(posX-i-1, posY, 0);
			addAttackableCell(posX+i+1, posY, 1);
			addAttackableCell(posX, posY-i-1, 2);
			addAttackableCell(posX, posY+i+1, 3);
		}

	}
	
	public static String attackTeam(Player p) {
		if(p.inventaire == Constants.CHATAIGNE) { // On va pas brasser de l'air non plus ..
			Team victim = getBestTeam();
			attackableCells = new ArrayList<Cell>();
			instruction = null;
			player = p;
			 
			for(Cell c : victim.cells) { // Attaque de la base
				attackablePossibilities(c);
				if(instruction != null) { // Evite d'exécuter le code inutile après (performance <3)
					return instruction;
				}
			}
			for(Player v : victim.players) {
				if(v.id != 0) { // On ne va pas attaquer leur quaterback gneugneugneu
					attackablePossibilities(v.cell);
					if(instruction != null) {
						return instruction;
					}
				}
			}
			cellAttack = Cell.getNearestCellTheoric(p.cell, attackableCells); // La cell attaquable la plus proche
		} else if(p.inventaire != -1) { // Il reste des fruits du mode Gather ?
				return Gather.Gather(p); // On relance Gather, il va le jeter
		} else {
			cellAttack = Fruits.getNearestFruit(p, p.cell, Constants.CHATAIGNE);
			if(cellAttack == p.cell) // Il est sur une chataigne 
				return "P"; // Ramasse-la nom de diou
		}
		if(cellAttack == null)
			return "X";
		ArrayList<Cell> path = Path.getNearestPath(p.cell, cellAttack);
		if(path.size() > 0) // Sinon si ça crash on est disqualifié ....
			return Actions.move(p, path.get(0)); // Se dirige vers la position d'attaque la plus proche ou la chataigne
		else {
			return "X"; // A modifier sinon on perd un tour
		}
	}
	
}
