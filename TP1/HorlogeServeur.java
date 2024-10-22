import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HorlogeServeur {

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Le serveur d'horloge est démarré et écoute sur le port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connexion acceptée de " + clientSocket.getInetAddress());
                    String clientMessage = "";

                    // Obtenir les flux d'entrée et sortie
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // Tant que le client n'envoie pas "CLOSE", on reste en écoute
                    while (!clientMessage.equals("CLOSE")) {
                        clientMessage = input.readLine(); // Lire le message du client

                        if (clientMessage == null || clientMessage.equals("CLOSE")) {
                            System.out.println("Connexion fermée par le client");
                            break; // Sortir de la boucle si le client envoie "CLOSE" ou ferme la connexion
                        }

                        System.out.println("Message reçu du client : " + clientMessage);

                        switch (clientMessage) {
                            case "HOUR":
                                send("HH:mm:ss", clientSocket);
                                break;
                            case "DAYS":
                                send("yyyy-MM-dd", clientSocket);
                                break;

                            case "FULL":
                                send("yyyy-MM-dd HH:mm:ss", clientSocket);
                                break;

                            default:
                                break;
                        }

                    }

                } catch (IOException e) {
                    System.out.println("Erreur lors de la communication avec le client : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du démarrage du serveur : " + e.getMessage());
        }
    }

    public static void send(String format, Socket clientSocket) {
        OutputStream outputStream;
        try {
            String currentDate = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(format));

            outputStream = clientSocket.getOutputStream();
            outputStream.write((currentDate + "\n").getBytes());
            outputStream.flush(); // S'assurer que le message est bien envoyé
            System.out.println("Date envoyé : " + currentDate);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
