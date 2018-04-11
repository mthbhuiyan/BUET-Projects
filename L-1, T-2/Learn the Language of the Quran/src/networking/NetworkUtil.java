package networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkUtil {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private String filePath;

    public NetworkUtil(String s, int port, String filePath) {
        try {
            this.socket = new Socket(s, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            this.filePath = filePath;
        } catch (Exception e) {
            System.out.println("In NetworkUtil : " + e.toString());
        }
    }

    public NetworkUtil(Socket s) {
        try {
            this.socket = s;
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("In NetworkUtil : " + e.toString());
        }
    }

    public Object read() {
        Object o = null;
        try {
            o = ois.readObject();
        } catch (Exception e) {
            //System.out.println("Reading Error in network : " + e.toString());
        }
        return o;
    }

    public void write(Object o) {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            System.out.println("Writing  Error in network : " + e.toString());
        }
    }

    public String readString() {
        String message = null;
        try {
            message = ois.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

    public void writeString(String s) {
        try {
            oos.writeUTF(s);
        } catch (IOException ex) {
            Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readFile() {
        FileOutputStream fos = null;
        try {
            String fileName = String.valueOf(read());
            File file = new File(filePath, fileName);
            fos = new FileOutputStream(file);
            try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                int length = (Integer) read();
                byte[] mybytearray = new byte[length + 1];
                int bytesRead;
                int current = 0;
                bytesRead = ois.read(mybytearray, 0, mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead = ois.read(mybytearray, current, (mybytearray.length - current));
                    if (bytesRead >= 0) {
                        current += bytesRead;
                    }
                } while (mybytearray.length - current > 1);

                bos.write(mybytearray, 0, current);
                bos.flush();

            } catch (IOException ex) {
                Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void writeFile(String fileName) {
        FileInputStream fis = null;
        try {
            write(fileName);
            File file = new File(filePath ,fileName);System.out.println(file.toString());
            fis = new FileInputStream(file);
            try (BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] mybytearray = new byte[(int) file.length()];
                write(mybytearray.length);
                bis.read(mybytearray, 0, mybytearray.length);

                oos.write(mybytearray, 0, mybytearray.length);
                oos.flush();
            } catch (IOException ex) {
                Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void readAbsentFile() {
        String fileName = String.valueOf(read());
        File file = new File(filePath,fileName);
        boolean isAbsent = !file.exists();
        write(isAbsent);
        if (isAbsent) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                    int length = (Integer) read();
                    byte[] mybytearray = new byte[length + 1];
                    int bytesRead;
                    int current = 0;
                    bytesRead = ois.read(mybytearray, 0, mybytearray.length);
                    current = bytesRead;

                    do {
                        bytesRead = ois.read(mybytearray, current, (mybytearray.length - current));
                        if (bytesRead >= 0) {
                            current += bytesRead;
                        }
                    } while (bytesRead > -1);

                    bos.write(mybytearray, 0, current);
                    bos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void writeAbsentFile(String fileName) {
        write(fileName);
        boolean isAbsent = (boolean) read();
        if (isAbsent) {
            FileInputStream fis = null;
            try {
                File file = new File(filePath,fileName);
                fis = new FileInputStream(file);
                try (BufferedInputStream bis = new BufferedInputStream(fis)) {
                    byte[] mybytearray = new byte[(int) file.length()];
                    write(mybytearray.length);
                    bis.read(mybytearray, 0, mybytearray.length);

                    oos.write(mybytearray, 0, mybytearray.length);
                    oos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void readAllFiles() {
        int noFiles = (Integer) read();

        for (int i = 0; i < noFiles; i++) {
            readFile();
        }
    }

    public void writeAllFiles(String folderName) {
        File folder = new File(folderName);
        File[] files = folder.listFiles();

        write(files.length);

        for (File file : files) {
            writeFile(file.toString());
        }
    }

    public void readAllAbsentFiles() {
        int noFiles = (Integer) read();

        for (int i = 0; i < noFiles; i++) {
            readAbsentFile();
        }
    }

    public void writeAllAbsentFiles(String folderName) {
        File folder = new File(folderName);
        File[] files = folder.listFiles();

        write(files.length);

        for (File file : files) {
            writeAbsentFile(file.toString());
        }
    }

    public void closeConnection() {
        try {
            ois.close();
            oos.close();
        } catch (Exception e) {
            System.out.println("Closing Error in network : " + e.toString());
        }
    }
}
