import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SomeInterface extends Remote {

    void uploadFile(String username,String[] fileDescriptions, byte[] buffer) throws RemoteException;

    ArrayList<Object> downloadFile(String fileTitle) throws RemoteException;

    boolean checkCredentials(String username, String password) throws RemoteException, UnknownHostException;

    void addCredentials(String username, String password) throws IOException;

    boolean checkUser(String username) throws IOException;

    boolean checkFile(String username, String fileName) throws IOException;

    ArrayList<String> showSearch(String username, String search, String type) throws IOException;

    boolean changeTitle(String username, int filePosition, String newTitle) throws IOException;

    boolean deleteFile(String username, int filePosition) throws IOException;
}