import javax.swing.*;

//A faire
//Mapper indexLevel avec Level
//Faire le compteur
//Faire le Client Serveur

/**
 * Démineur
 * 
 * @author Anatole
 * @version 0.0
 */

public class App extends JFrame {
    private Champ champ;
    private Gui gui;
    private int score;
    private int indexLevel = 0;

    public App() {
        super("Demineur");
        initializeGame();
        configureWindow();
        new Serveur(this); // Démarre le serveur avec une référence à l'application
    }

    private void initializeGame() {
        champ = new Champ(this);
        champ.init(3, 2, 1, this);
        champ.display();
        gui = new Gui(champ, this);
    }

    private void configureWindow() {
        setContentPane(gui);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Démineur");
        new App();
    }

    /**
     * Permet de lancer une nouvelle partie
     */

    public void newGame(int indexLevel) {
        score = 0;
        champ.newPartie(indexLevel, this);
        gui.newPartie(0);
    }

    /**
     * Permet de quitter l'application
     */
    public void quit() {
        System.exit(0);
    }

    public void gameOver() {
        gui.revealedCases();
    }

    public void propagation(int x, int y) {
        gui.propagation(x, y);
        System.out.println("propagation");

    }

    public void winGame() {
        gui.revealedCases();
    }
}
