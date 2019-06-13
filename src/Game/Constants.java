package Game;

import Client.Client;

public class Constants {
	
	public static final int MIRABELLE = 0;
	public static final int PRUNE = 1;
	public static final int CERISE = 2;
	public static final int FRAMBOISE = 3;
	public static final int CHATAIGNE = 4;
	public static Cell MID;
	public static Cell OPPOS_MIDX;
	public static Cell ADJA_MIDX;
	public static Cell OPPOS_MIDY;
	public static Cell ADJA_MIDY;
	
	public static void initCells(Cell c) {
		Client cli = Client.cli;
		MID = cli.map[(int)(cli.largeur/2)][(int)(cli.longueur/2)];
		if(c.posY == 1) {
			OPPOS_MIDX = cli.map[(int)(cli.largeur/2)][cli.longueur-2];
			ADJA_MIDX = cli.map[cli.largeur/2][1];
		} else {
			OPPOS_MIDX = cli.map[cli.largeur/2][1];
			ADJA_MIDX = cli.map[(int)(cli.largeur/2)][cli.longueur-2];
		}
		if(c.posX == 1) {
			OPPOS_MIDY = cli.map[cli.largeur-2][(int)(cli.longueur/2)];
			ADJA_MIDY = cli.map[1][(int)(cli.longueur/2)];
		} else {
			OPPOS_MIDY = cli.map[1][(int)(cli.longueur/2)];
			ADJA_MIDY = cli.map[cli.largeur-2][(int)(cli.longueur/2)];
		}
	}
	
}
