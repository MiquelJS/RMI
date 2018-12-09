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

    public ArrayList<Object> downloadFile(String fileTitle) {
        try {
            ArrayList<Object> toReturn = new ServerStorage().downloadFile(fileTitle);
            File file = new File(String.valueOf(toReturn.get(2)));
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            input.read(buffer,0,buffer.length);
            input.close();
            toReturn.add(buffer);
            return(toReturn);
        } catch(Exception e){
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }

    public void addCredentials(String username, String password) throws IOException {
        new ServerStorage().addCredentials(username,password);
    }

    public boolean checkUser(String username) {
        return new ServerStorage().checkUser(username);
    }

    public boolean checkFile(String username, String fileName) {
        return new ServerStorage().checkFile(username, fileName);
    }

    public boolean checkCredentials(String username, String password) {
        return new ServerStorage().checkCredentials(username,password);
    }

    public ArrayList<String> showSearch(String username, String fileName, String type) throws IOException {
        return new ServerStorage().showMedia(username, fileName, type);
    }

    public boolean changeTitle(String username, int filePosition, String newTitle) throws IOException {
        return new ServerStorage().changeTitle(username, filePosition, newTitle);
    }

    public boolean deleteFile(String username, int filePosition) {
        return new ServerStorage().deleteFile(username, filePosition);
    }
}

