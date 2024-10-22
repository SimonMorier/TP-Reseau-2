import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
public class ClientUDP {

    public  static long ADJUSTEDTIME = 0; 

    public static void send(String envoie, DatagramSocket client) throws IOException {
        byte[] buffer = envoie.getBytes();
        InetAddress adresse = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, 2345);
        client.send(packet);
    }

    public static DatagramPacket receive(DatagramSocket client) throws IOException {
        byte[] buffer2 = new byte[8196];
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
        client.receive(packet2);
        return packet2;
    }

    public static String getDate(String format) {
        Date currentDate = new Date();
        long adjustedMillis = currentDate.getTime() + ADJUSTEDTIME;
        Date adjustedDate = new Date(adjustedMillis); 
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(adjustedDate);
    }

      public static long getDeltaTime(String dates) {
        // Séparation des chaînes de temps par le point-virgule
        String[] times = dates.split(";");
        String T1 = times[0];
        String T1Prime = times[1];
        String T2Prime = times[2];
        String T2 = times[3];

        // Format du temps hh:mm:ss:SSS
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        try {
            // Convertir les chaînes de temps en objets Date
            Date dateT1 = sdf.parse(T1);
            Date dateT1Prime = sdf.parse(T1Prime);
            Date dateT2Prime = sdf.parse(T2Prime);
            Date dateT2 = sdf.parse(T2);

            // Convertir les temps en millisecondes
            long T1Millis = dateT1.getTime();
            long T1PrimeMillis = dateT1Prime.getTime();
            long T2PrimeMillis = dateT2Prime.getTime();
            long T2Millis = dateT2.getTime();

            // Calculer les différences de temps
            long delta1 = T2Millis - T1Millis;
            long delta2 = T2PrimeMillis - T1PrimeMillis;

            long teta = ((T2PrimeMillis + T1PrimeMillis) / 2) - ((T2Millis + T1Millis)/2);


            // Calculer deltaT = (T2 - T1) - (T2' - T1')
            long deltaT = delta1 - delta2;

            System.out.println("delais :"+ deltaT + "\nteta : "+teta);

            return teta ; 

        } catch (ParseException e) {
            e.printStackTrace();
            return (Long) null;
        }
    }

    public static void main(String[] args) {
        try (DatagramSocket client = new DatagramSocket()) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Entrez votre commande : \n");
                String message = scanner.nextLine();
                ClientUDP.send(message, client);

                // Attendre la réponse du serveur
                DatagramPacket responsePacket = ClientUDP.receive(client);
                String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println("Le serveur a répondu : " + response);


                switch (response) {
                    case "exo2":
                        ClientUDP.send(getDate("hh:mm:ss:SSS"), client);
                        DatagramPacket responsePacket2 = ClientUDP.receive(client);
                        String response2 = new String(responsePacket2.getData(), 0, responsePacket2.getLength());
                        String t2 = getDate("hh:mm:ss:SSS");
                        ADJUSTEDTIME = getDeltaTime(response2+";"+t2);
                        System.out.println("Heure ajustée.");
                        ClientUDP.send("fin exo 2", client);
                        break;

                    case "1":
                        break;
                    case "2":
                        System.out.println("SUCE");
                        break;
                    default:
                        break;
                }
            }

         

           

          
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}