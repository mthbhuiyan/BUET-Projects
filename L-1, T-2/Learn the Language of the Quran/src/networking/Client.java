package networking;

import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Server Ip:");
        String ip = scan.nextLine();
        System.out.println("Server Port:");
        int port = scan.nextInt();
        System.out.println("Name of the file to update:");
        String file = scan.nextLine();
        System.out.println("Path of the file to update:");
        String path = scan.nextLine();
        new ClientThread(ip, port, file, path);
    }
}
