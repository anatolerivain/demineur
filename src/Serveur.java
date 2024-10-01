import java.net.*; // Sockets
import java.io.*; // Streams

public class Serveur {
    private Champ champ;

    public Serveur(App app) {
        this.champ = new Champ(app); // Initialiser le champ avec l'application
        startServer();
    }

    private void startServer() {
        System.out.println("Démarrage du serveur");
        try {
            ServerSocket gestSock = new ServerSocket(10000);
            while (true) { // Boucle pour accepter plusieurs clients
                Socket socket = gestSock.accept(); // attente
                new ClientHandler(socket, champ).start(); // Crée un nouveau thread pour chaque client
            }
        } catch (IOException e) {
            System.out.println("Premiere IOSException");
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private Champ champ;

    public ClientHandler(Socket socket, Champ champ) {
        this.socket = socket;
        this.champ = champ;
    }

    @Override
    public void run() {
        try (DataInputStream entree = new DataInputStream(socket.getInputStream());
                DataOutputStream sortie = new DataOutputStream(socket.getOutputStream())) {

            // Lecture du nom du joueur
            String nomJoueur = entree.readUTF();
            System.out.println(nomJoueur + " est connecté");

            // Envoi d'une donnée (exemple : 0 pour le score)
            sortie.writeInt(0);

            // Boucle de traitement des commandes du client
            String commande;
            while ((commande = entree.readUTF()) != null) {
                // Traitez les commandes du client ici
                // Exemple : commande pour révéler une case
                if (commande.startsWith("REVEAL")) {
                    String[] parts = commande.split(" ");
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    // Logique pour révéler une case, par exemple
                    boolean result = champ.reveal(x, y); // Implémentez cette méthode dans Champ
                    sortie.writeBoolean(result); // Envoyez le résultat au client
                }
            }
        } catch (IOException e) {
            System.out.println("deuxieme exception");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Troisieme exception");

                e.printStackTrace();
            }
        }
    }
}
