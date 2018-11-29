import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SomeInterface extends Remote {
    // signature of first remote method
    public void uploadFile(String username,String fileName, byte[] buffer) throws RemoteException;

    public byte[] downloadFile(String username,String fileName) throws RemoteException;

    boolean checkCredentials(String username, String password);

    // signature of other remote methods may follow

}