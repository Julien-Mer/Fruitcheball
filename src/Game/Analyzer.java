package Game;

import java.io.IOException;
import java.util.ArrayList;

import Client.*;
import Intelligence.*;

public class Analyzer {

	public static void map(Connection co, String[] mapInfos) {
    	if(Client.cli.map == null) {
    		System.out.println("Initialisation de la carte");
    		String[] mapSize = mapInfos[0].split(":");
    		Client.cli.longueur = Integer.valueOf(mapSize[0]);
    		Client.cli.largeur = Integer.valueOf(mapSize[1]);
    		Client.cli.map = new Cell[Client.cli.longueur][Client.cli.largeur];
    		System.out.println("[Map] " + Client.cli.longueur + "x" + Client.cli.largeur);
    	}
    	Client.cli.fruits = new Fruits[5];
    	for(int i = 0; i < Client.cli.fruits.length; i++)
    		Client.cli.fruits[i] = new Fruits(i);
    	System.out.println("[Map] Obstacles et fruits");
    	for(int lo = 0; lo < Client.cli.longueur; lo++) {
    		for(int la = 0; la < Client.cli.largeur; la++) {
    			Cell cell = new Cell(la, lo, String.valueOf(mapInfos[lo+1].charAt(la)));
    			Client.cli.map[la][lo] = cell;
    		}
    	}
	}
	
	
    public static void analyse(Connection co) throws IOException {
    	Client.cli.targetedFruits = new ArrayList<Cell>();
    	String[] infos = co.received.split("_");
		String[] mapInfos = infos[2].split(",");
		Analyzer.map(co, mapInfos);
		Analyzer.teams(co, infos);
		co.send = "";
		Team team = Client.cli.teams[Integer.valueOf(Client.cli.id)];
		for(Player player : team.players) {
			if(player.id == 0) { // C'est le quaterback
				System.out.println("[Action] Quaterback");
				if(Client.cli.fruits[Constants.CHATAIGNE].getNumber() > 0 && Client.cli.iterations > Client.cli.delayQuaterback || player.inventaire == Constants.CHATAIGNE)
					co.send += Attack.attackTeam(player);
				else
					co.send += Gather.Gather(player);
			} else {
				System.out.println("[Action] Joueur");
				co.send += Gather.Gather(player);
				//co.send += "X";
			} 
			if(player.id != 2)
				co.send += "-";
		}
		co.send += "\n";
		co.send();
    }
	


public static void teams(Connection co, String[] infos) {
	int nbrTeams = Integer.valueOf(infos[1]); 
	System.out.println("[Teams] Refresh " + nbrTeams + " Teams");
	Client.cli.teams = new Team[nbrTeams];
	
	for(int i = 0; i< nbrTeams; i++) {
		String[] teamInfos = infos[3 + i].split(",");
		
		Player[] players = new Player[3];
		for(int p = 0; p < 3; p++) {
			String[] playerInfos = teamInfos[2+p].split(":");
			Cell cell = Client.cli.map[Integer.valueOf(playerInfos[1])][Integer.valueOf(playerInfos[2])];
			players[p] = new Player(cell, p, playerInfos[3]);
		}
		
		Cell[] cells = new Cell[3];
		for(int c = 0; c < 3; c++) {
			String[] cellsInfos = teamInfos[6+c].split(":");
			cells[c] = Client.cli.map[Integer.valueOf(cellsInfos[1])][Integer.valueOf(cellsInfos[2])];
		}
		
		int[] fruits = new int[5];
		for(int f = 0; f < 5; f++) {
			String[] fruitInfos = teamInfos[12+f].split(":");
			fruits[f] = Integer.valueOf(fruitInfos[1]);
		}
		
		Client.cli.teams[i] = new Team(i, players, fruits, cells, Integer.valueOf(teamInfos[10]));
	}
	if(Constants.MID == null) {
		System.out.println("[Constantes] Initialisation repères");
		Team t = Client.cli.teams[Integer.valueOf(Client.cli.id)];
		for(Player p : t.players)
			if(p.id == 2) // Le joueur 2 est toujours sur la case centrale, repère ;)
				Constants.initCells(p.cell);
	}
}

}

/** Exemple de données reçues
0 => Id du joueur

_4 => Nombre de joueurs

_13:13, => Taille de la map
XXXXXXXXXXXXX, => Premiere ligne de la map: X = mur, . = vide, numéro = fruit
X......01...X,
X...XX.XX...X,
X.X......4X.X,
X1X.......X1X,
X.....3.....X,
X...0...2...X,
X0....3....0X,
X1X.......X.X,
X.X4.....4X.X,
X...XX.XX...X,
X...10.0....X,
XXXXXXXXXXXXX

_Equipe0, => Entete équipe et son numéro
P, => Membres de  l'équipes
P0:3:5:4,				P0 => Quaterback      3:5 => Position (3,5)    4 => Inventaire								
P1:5:1:x,					P: 0 => Quaterback / 1 => Lanceur1 / 2 => Lanceur2
P2:4:6:2,
Z, => Cases de départ de l'équipe
Z0:2:1,					Z0 => Case 0		2:1 => Position(2,1)
Z1:1:1,
Z2:1:2,
G, => Score de l'équipe
2, 						2 => Score de l'équipe         10
F, => Fruits rapportés
F0:1,					0 => Mirabelle 		1 => 1 mirabelle raportée
F1:1,
F2:0,
F3:0,
F4:0

_Equipe1,
P,
P0:8:11:x,
P1:9:8:x,
P2:11:8:x,
Z,
Z0:11:10,
Z1:11:11,
Z2:10:11,
G,
2,
F,
F0:0,
F1:2,
F2:0,
F3:0,
F4:1

_Equipe2,
P,P0:1:11:x,
P1:4:11:x,
P2:3:8:x,
Z,
Z0:2:11,
Z1:1:10,
Z2:1:11,
G,
-3,
F,
F0:0,
F1:0,
F2:0,F3:0,
F4:2

_Equipe3,
P,
P0:11:5:0,
P1:7:5:x,
P2:10:1:x,
Z,
Z0:11:1,
Z1:10:1,
Z2:11:2,
G,
0,
F,
F0:0,
F1:0,
F2:0,
F3:0,
F4:0
*/