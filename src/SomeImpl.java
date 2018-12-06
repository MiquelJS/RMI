import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SomeImpl extends UnicastRemoteObject implements SomeInterface {

    public SomeImpl() throws RemoteException {
        super();
    }
    public void uploadFile(String username,String[] fileDescriptions, byte[] buffer){
        try {
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileDescriptions[0]));
            output.write(buffer,0,buffer.length);
            new ServerStorage().saveFile(username,buffer,fileDescriptions);
            output.flush();
            output.close();
        } catch(Exception e) {
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public byte[] downloadFile(String username,String fileName) {
        try {
            File file = new File(fileName);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
            input.read(buffer,0,buffer.length);
            new ServerStorage().downloadFile(buffer);
            input.close();
            return(buffer);
        } catch(Exception e){
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }

    public void addCredentials(String username, String password) throws IOException {
        ServerStorage credentials = new ServerStorage();
        credentials.addCredentials(username,password);
    }

    public boolean checkUser(String username) {
        ServerStorage credentials = new ServerStorage();
        return credentials.checkUser(username);
    }

    public ArrayList<String> showSearch(String fileName, String type) throws IOException {
        ServerStorage credentials = new ServerStorage();
        return credentials.showMedia(fileName, type);
    }

    public boolean checkCredentials(String username, String password) {
        ServerStorage credentials = new ServerStorage();
        return credentials.checkCredentials(username,password);
    }
}

