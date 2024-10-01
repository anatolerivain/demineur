import java.util.*;

/**
 * Classe Champ du démineur
 * 
 * @author Anatole
 * @version 0.0
 */

public class Champ {
    private int[] tabSize = { 5, 10, 15 };
    private int[] tabNbMines = { 3, 7, 9 };

    private boolean[][] map = new boolean[tabSize[0]][tabSize[0]];
    private Case[][] cases; // Pour stocker les références des cases
    private Random aleatoire = new Random();

    public Champ(App app) {
        map = new boolean[tabSize[0]][tabSize[0]];
        cases = new Case[tabSize[0]][tabSize[0]]; // Initialiser le tableau de cases
    }

    /**
     * Place des mines aléatoirement
     * 
     * @param xDepart
     * @param yDepart
     */

    public void init(int xDepart, int yDepart, int indexLevel, App app) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = false; // Aucune mine par défaut
                cases[i][j] = new Case(app); // Initialiser les cases
            }
        }

        // Placer les mines
        for (int n = tabNbMines[indexLevel]; n != 0;) {
            int x = aleatoire.nextInt(map.length);
            int y = aleatoire.nextInt(map[0].length);
            if (!(map[x][y] || (x == xDepart && y == yDepart))) {
                map[x][y] = true;
                n--;
            }
        }
    }

    /**
     * @param i
     * @param j
     * @return true si il y a une mine
     */

    public boolean isMine(int i, int j) {
        return map[i][j];
    }

    /**
     * Affiche le champ de mines
     */
    public void display() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++)
                // System.out.print(map[i][j]?'X':'0'); //affichage avec une condition 'if'
                // (affiche 'X' si vrai)
                if (map[i][j])
                    System.out.print('X');
                else
                    System.out.print(nbMinesAround(i, j));
            System.out.println();
        }
    }

    /**
     * Calcul le nombre de mines autour
     * 
     * @param x
     * @param y
     * @return le nombre de mines autour
     */
    public int nbMinesAround(int x, int y) {
        int n = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i != -1 && i != map.length && j != -1 && j != map[0].length && map[i][j])
                    n++;
            }
        }
        return n;
    }

    public boolean reveal(int x, int y) {
        // Logique pour révéler une case à (x, y)
        // Retournez true si réussi, sinon false
        // Vous pouvez également gérer les exceptions ici
        return true; // Remplacez cela par la logique réelle
    }

    public int getLong() {
        return map.length;
    }

    public int getHaut() {
        return map[0].length;
    }

    public void newPartie(int indexLevel, App app) {
        map = new boolean[tabSize[indexLevel]][tabSize[indexLevel]];
        cases = new Case[tabSize[indexLevel]][tabSize[indexLevel]]; // Réinitialiser le tableau de cases
        init(0, 1, indexLevel, app);
        display();
    }

    public Case getCase(int neighborX, int neighborY) {
        if (neighborX >= 0 && neighborX < cases.length && neighborY >= 0 && neighborY < cases[0].length) {
            return cases[neighborX][neighborY];
        }
        return null;
    }
}
