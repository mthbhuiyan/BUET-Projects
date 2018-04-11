package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost());
        Scanner scan = new Scanner(System.in);
        int port;
        if (args.length < 1) {
            System.out.println("Server port:");
            port = scan.nextInt();
        } else {
            port = Integer.valueOf(args[0]);
        }
        try (ServerSocket servsocket = new ServerSocket(port);) {
            while (true) {
                new ServingThread(servsocket.accept());
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
