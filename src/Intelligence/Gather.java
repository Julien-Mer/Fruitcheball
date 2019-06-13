package Intelligence;

import java.util.ArrayList;
import java.util.Arrays;

import Client.Client;
import Game.*;

public class Gather {

	static Boolean[] blocked;
	static ArrayList<Cell> throwableCells;
	static Player player;
	static Cell cellThrow;
	static Cell cellThrowed;
	static String instruction;
	
	public static void addThrowableCell(int posX, int posY, int i) {
		if(Cell.isValid(posX, posY)) { // Case dans les limites du terrain
			cellThrow = Client.cli.map[posX][posY];
			if(cellThrow.wall || cellThrow.player != null && cellThrow != player.cell)
				blocked[i] = true; // On bloque la ligne de vue
			if(!blocked[i]) {
				if(cellThrow == player.cell) { // Il peut déjà attaquer
					instruction = "L" + Path.getOrientation(player.cell, cellThrowed); 
				} else {
					throwableCells.add(cellThrow); // Sinon la cell s'ajoute à la liste potentielle
				}
				
			}
		}
	}
	
	public static void throwablePossibilities(Cell c) {
		cellThrowed = c;
		blocked = new Boolean[5];
		Arrays.fill(blocked, Boolean.FALSE);
		int posX = c.posX;
		int posY = c.posY;
		for(int i = 0; i < 4; i++) { // 16 positions de jet <3
			addThrowableCell(posX-i-1, posY, 0);
			addThrowableCell(posX+i+1, posY, 1);
			addThrowableCell(posX, posY-i-1, 2);
			addThrowableCell(posX, posY+i+1, 3);
		}

	}
	
	
	public static String Gather(Player p) {
		if(p.inventaire != -1) { // S'il a bien un fruit
			throwableCells = new ArrayList<Cell>();
			instruction = null;
			player = p;
			for(Cell c : Client.cli.teams[Integer.valueOf(Client.cli.id)].cells) { // Cellules de la base
				throwablePossibilities(c);
				if(instruction != null) { // On peut déjà jeter le fruit
					return instruction;
				}
			}
			Team t = Client.cli.teams[Integer.valueOf(Client.cli.id)];
			int distNearestAlly = 999;
			Player nearestAlly = null;
			for(Player ally : t.players) { // Trouve l'allié le plus proche
				if(ally.id == p.id || ally.id == 0 && Client.cli.iterations > Client.cli.delayQuaterback && Client.cli.fruits[Constants.CHATAIGNE].getNumber() != 0)
					continue;
				int distance = Cell.distanceCellsTheoric(ally.cell, p.cell);
				if(distance < distNearestAlly) {
					 distNearestAlly = distance;
					 nearestAlly = ally;
				}
			}
			if(ThrowerState2() || t.distBase(t.players[p.id].cell) <= t.distBase(t.players[nearestAlly.id].cell)) { // Lancer précis
				cellThrow = Cell.getNearestCellTheoric(p.cell, throwableCells); // La cell de throw la plus proche
			} else { // Lancer approximatif vers le player 2
				return "L" + Path.approxThrow(p.cell, nearestAlly.cell);
			}
		} else {
			cellThrow = GatherState(p); // Recherche du fruit
			Client.cli.targetedFruits.add(cellThrow);
			if(cellThrow == p.cell) // Il est sur le fruit 
				return "P"; // Ramasse-le nom de diou
		}
		if(cellThrow == null)
			return "X";
		ArrayList<Cell> path = Path.getNearestPath(p.cell, cellThrow);
		if(path.size() > 0) // Sinon si ça crash on est disqualifié ....
			return Actions.move(p, path.get(0)); // Se dirige vers la position de jet la plus proche ou le fruit
		else {
			return "X"; // A modifier sinon on perd un tour
		}
	}
	
	public static boolean ThrowerState2() {
		if(Client.cli.iterations < 10) // Les premiers fruits peuvent être ramenés directement
			return true;
		//else if(Client.cli.iterations < 50)
		//	return false;
		else
			//return true;
			return false;
	}
	
	public static Cell GatherState(Player p) {
		Cell c = p.cell;
		if(p.id == 2) // On positionne le player 2 à son poste de récupération
			return Fruits.getNearestFruit(p, Constants.ADJA_MIDX);
		
		//if(p.id == 0 && Client.cli.iterations < 3) // LA PREMIERE FRAMBOISE PAR LE QUATERBACK
			//return Fruits.getNearestFruit(p, c, Constants.FRAMBOISE);
		//if(Client.cli.iterations < 5 && p.id != 0)
		//	return Fruits.getNearestFruit(p, c, Constants.PRUNE);
		//else if(Client.cli.iterations < 6 && p.id != 0)
			//return Fruits.getNearestFruit(p, c, Constants.MIRABELLE);
		//else 
			if(Client.cli.iterations < 59 && Client.cli.fruits[Constants.PRUNE].getNumber() > 1) // Sinon il va courir partout ...
			return Fruits.getNearestFruit(p, Constants.MID, Constants.PRUNE);
		else if(Client.cli.iterations < 59) // Plus de prunes
				return Fruits.getNearestFruit(p, Constants.MID);
		else if(Client.cli.iterations < 90 )
			return Fruits.getNearestFruit(p, Constants.ADJA_MIDY);
		else 
			return Fruits.getNearestFruit(p, p.cell);
	}
	
}
