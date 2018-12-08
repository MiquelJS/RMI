import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SomeInterface extends Remote {

    void uploadFile(String username,String[] fileDescriptions, byte[] buffer) throws RemoteException;

    byte[] downloadFile(String username,String fileName) throws RemoteException;

    boolean checkCredentials(String username, String password) throws RemoteException;

    void addCredentials(String username, String password) throws IOException;

    boolean checkUser(String username) throws IOException;

    boolean checkFile(String username, String fileName) throws IOException;

    ArrayList<String> showSearch(String username, String fileName, String type) throws IOException;

    boolean changeTitle(String username, int filePosition, String newTitle) throws IOException;

    boolean deleteFile(String username, int filePosition) throws IOException;
}