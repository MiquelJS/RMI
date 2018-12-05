import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SomeInterface extends Remote {

    void uploadFile(String username,String[] fileDescriptions, byte[] buffer) throws RemoteException;

    byte[] downloadFile(String username,String fileName) throws RemoteException;

    boolean checkCredentials(String username, String password) throws RemoteException;

    void addCredentials(String username, String password) throws IOException;

    boolean checkUser(String username) throws IOException;

    void printSearch(String fileName) throws RemoteException, IOException;
}