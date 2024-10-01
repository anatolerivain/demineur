import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Graphical User Interface
 * 
 * @author Anatole
 * @version 0.0
 */

public class Gui extends JPanel implements ActionListener {
    private App app; // private App appli;
    private JButton boutonQuit;
    private JButton boutonNew;
    private JComboBox selectLevel;
    private JPanel panelMines = new JPanel();
    private JLabel labelScore = new JLabel();
    private Champ champ;
    private JMenuItem mQuitter;
    private JMenuItem mNouvPartie;
    private Case[][] tabCase;
    private int revealedCases;
    private int totalNonMineCases;

    Gui(Champ champ, App app) {
        System.out.println("Initializing GUI...");
        setLayout(new BorderLayout());

        this.champ = champ;
        this.app = app;
        this.tabCase = new Case[champ.getHaut()][champ.getLong()];

        // Les menus
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPartie = new JMenu("Partie");
        menuBar.add(menuPartie);

        mNouvPartie = new JMenuItem("Nouvelle Partie");
        menuPartie.add(mNouvPartie);
        mNouvPartie.addActionListener(this);

        mQuitter = new JMenuItem("Quitter");
        menuPartie.add(mQuitter);
        mQuitter.addActionListener(this);

        app.setJMenuBar(menuBar); // app étant la JFrame

        // Panel en haut de l'interface
        JPanel panelNort = new JPanel();
        panelNort.setBackground(Color.DARK_GRAY);

        JLabel label = new JLabel("Score : ");
        labelScore = new JLabel("0");
        panelNort.add(label);
        panelNort.add(labelScore);

        selectLevel = new JComboBox<>(Level.values());
        panelNort.add(selectLevel);

        add(panelNort, BorderLayout.NORTH);

        // Panel des mines au centre
        add(panelMines, BorderLayout.CENTER);

        // Panel du bas de l'interface
        JPanel panelSud = new JPanel();
        panelSud.setBackground(Color.DARK_GRAY);

        boutonNew = new JButton("Nouvelle Partie");
        boutonNew.setBackground(Color.GREEN);
        boutonNew.setFont(new Font("Arial", Font.BOLD, 14));
        boutonNew.addActionListener(this);
        panelSud.add(boutonNew);

        boutonQuit = new JButton("Quitter");
        boutonQuit.setBackground(Color.RED);
        boutonQuit.setFont(new Font("Arial", Font.BOLD, 14));
        boutonQuit.addActionListener(this);
        panelSud.add(boutonQuit);

        add(panelSud, BorderLayout.SOUTH);
        newPartie(0);
    }

    public void revealedCases() {
        System.out.println("Revealing all cases...");
        for (int i = 0; i < champ.getLong(); i++) {
            for (int j = 0; j < champ.getHaut(); j++) {
                tabCase[i][j].setFill(false);
                tabCase[i][j].repaint();
            }
        }
    }

    public void propagation(int x, int y) {
        System.out.println("Propagation called with coordinates: (" + x + ", " + y + ")");

        if (x >= 0 && x < champ.getHaut() && y >= 0 && y < champ.getLong()) { // Check if the case is in the field
            Case currentCase = tabCase[x][y];
            System.out.println("Current case: " + currentCase);

            if (!currentCase.getIsMine() && currentCase.getIsFill()) { // Check if the case is not a mine and is not
                                                                       // already revealed
                System.out.println("Revealing case at: (" + x + ", " + y + ")");
                currentCase.setFill(false);
                currentCase.setFlag(false);
                currentCase.repaint();
                revealedCases++;
                System.out.println("Revealed cases count: " + revealedCases);

                // Check win condition
                if (revealedCases == totalNonMineCases) {
                    System.out.println("All non-mine cases revealed. Player wins!");
                    app.winGame();
                }

                if (currentCase.getNbMinesAround() == 0) {
                    // Check if there are no mines around
                    System.out.println("No mines around case at: (" + x + ", " + y + "). Propagating...");
                    // Propagate if no mines around
                    propagation(x - 1, y); // Up
                    propagation(x + 1, y); // Down
                    propagation(x, y - 1); // Left
                    propagation(x, y + 1); // Right
                    propagation(x - 1, y - 1); // Top-left
                    propagation(x - 1, y + 1); // Top-right
                    propagation(x + 1, y - 1); // Bottom-left
                    propagation(x + 1, y + 1); // Bottom-right
                } else {
                    System.out.println("Mines are around case at: (" + x + ", " + y + "). No propagation.");
                }
            } else {
                System.out.println("Case at: (" + x + ", " + y + ") is either a mine or already revealed.");
            }
        } else {
            System.out.println("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonNew || e.getSource() == mNouvPartie) {
            System.out.println("Starting new game at level: " + selectLevel.getSelectedItem());
            app.newGame(selectLevel.getSelectedIndex());
        } else if (e.getSource() == boutonQuit || e.getSource() == mQuitter) {
            System.out.println("Exiting the game...");
            app.quit();
        }
    }

    public void newPartie(int indexLevel) {
        System.out.println("Initializing new game at level: " + indexLevel);
        labelScore.setText("0");
        revealedCases = 0;
        majPanelMines();
        app.pack();
    }

    public void majPanelMines() {
        System.out.println("Refreshing mines panel...");
        panelMines.removeAll();
        panelMines.setLayout(new GridLayout(champ.getHaut(), champ.getLong()));

        for (int i = 0; i < champ.getHaut(); i++) {
            for (int j = 0; j < champ.getLong(); j++) {
                Case newCase = new Case(app);
                newCase.setCoordinates(i, j);
                newCase.setMine(champ.isMine(i, j));
                int minesAround = champ.nbMinesAround(i, j);
                newCase.setNbMinesAround(minesAround);
                System.out.println("Mines around (" + i + ", " + j + "): " + minesAround);
                tabCase[i][j] = newCase;
                panelMines.add(newCase);
            }
        }
        panelMines.revalidate();
        panelMines.repaint();
    }

}
