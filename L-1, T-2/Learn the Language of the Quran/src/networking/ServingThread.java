package networking;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServingThread implements Runnable {

    NetworkUtil net;

    public ServingThread(Socket socket) {
        this.net = new NetworkUtil(socket);

        new Thread(this).start();
    }

    @Override
    public void run() {
        String fileName = String.valueOf(net.read());
        if (new File(fileName).exists()) {
            net.write(true);
            net.writeAllAbsentFiles(fileName + "/Image");
            net.writeAllAbsentFiles(fileName + "/Audio");

            net.writeFile(fileName + "/" + fileName+".wlrn");
        } else {
            net.write(false);
        }

        net.closeConnection();
    }

}
