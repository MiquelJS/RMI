import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SomeInterface extends Remote {

    void uploadFile(String username,String[] fileDescriptions, byte[] buffer) throws RemoteException;

    ArrayList<Object> downloadFile(String fileTitle) throws RemoteException;

    boolean checkCredentials(String username, String password) throws RemoteException;

    void addCredentials(String username, String password) throws IOException;

    boolean checkUser(String username) throws IOException;

    ArrayList<String> showSearch(String fileName, String type) throws RemoteException, IOException;
}