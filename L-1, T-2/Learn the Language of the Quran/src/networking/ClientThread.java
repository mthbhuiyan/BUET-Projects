package networking;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread implements Runnable {

    NetworkUtil net;
    String fileName;
    String parentName;

    public ClientThread(String ipAddress, int port, String fileName, String parentName) {
        this.net = new NetworkUtil(ipAddress, port, parentName);
        this.fileName = fileName;
        this.parentName = parentName;

        new Thread(this).start();
    }

    @Override
    public void run() {
        net.write(fileName);
        if ((boolean) net.read()) {
            net.readAllAbsentFiles();
            net.readAllAbsentFiles();

            net.readFile();
        } else {
            System.out.println("No resource found");
        }
        net.closeConnection();
    }

}
