import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SomeImpl extends UnicastRemoteObject implements SomeInterface {

    public SomeImpl() throws RemoteException {
        super( );
    }

    public String helloName(String name){
        return "Hello " + name;
    }

    public void uploadFile(String username,String fileName, byte[] buffer){
        try {
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileName));
            output.write(buffer,0,buffer.length);
            ServerStorage storage = new ServerStorage();
            storage.saveFile(username,buffer,fileName);
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
            ServerStorage storage = new ServerStorage();
            storage.downloadFile(username,fileName);
            input.close();
            return(buffer);
        } catch(Exception e){
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }
}

