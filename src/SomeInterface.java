import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SomeInterface extends Remote {
    // signature of first remote method
    public String helloName(String name) throws java.rmi.RemoteException;

    public void uploadFile(String username,String fileName, byte[] buffer) throws RemoteException;

    public byte[] downloadFile(String username,String fileName) throws RemoteException;

    // signature of other remote methods may follow

}