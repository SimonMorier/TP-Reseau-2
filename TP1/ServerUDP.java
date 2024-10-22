import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {

    public static DatagramPacket receive(DatagramSocket server) throws IOException {
        byte[] buffer = new byte[8192];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        server.receive(packet);
        return packet;
    }

    public static void send(DatagramSocket server, String str, DatagramPacket dp) throws IOException {
        byte[] buffer2 = (str).getBytes();
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, dp.getAddress(), dp.getPort());
        server.send(packet2);
    }

    public static String getDate(String format){
        java.util.Date date = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static void main(String[] args) {
        try (DatagramSocket server = new DatagramSocket(2345, InetAddress.getLocalHost())) {
            while (true) {
                DatagramPacket dp = ServerUDP.receive(server);
                String str = new String(dp.getData(), 0, dp.getLength());
                System.out.println(str);
    
                switch (str) {
                    case "connect":
                        System.out.println("Nouveau Client");
                        System.out.print("Reçu de la part de " + dp.getAddress() + " sur le port " + dp.getPort() + " : "); 
                        ServerUDP.send(server, "Choisissez exo : 1, 2, 3, 4, 5", dp);
                        break;
                    case "1":
                        ServerUDP.send(server, getDate("hh:mm:ss"), dp);
                        break;
                    case "2":
                        System.out.println("exo 2");
                        ServerUDP.send(server, "exo2", dp);
                        while(true){
                            DatagramPacket dp2 = ServerUDP.receive(server);
                            String t1prim = getDate("hh:mm:ss:SSS");
                            String str2 = new String(dp2.getData(), 0, dp2.getLength());
                            if(str2.equals("fin exo 2")){
                                break; 
                            }

                            ServerUDP.send(server, str2+";"+t1prim +";"+getDate("hh:mm:ss:SSS"), dp2);

                        }
                        break;
                    default:
                        break;
                }

              
                
        
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}