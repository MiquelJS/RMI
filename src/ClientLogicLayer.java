import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientLogicLayer {

    private int portNum = 1099;
    private String registryURL = "rmi://localhost:" + portNum + "/some";

    ClientLogicLayer() {}

    public void upload(String username,String[] fileDescriptions) throws IOException, NotBoundException {
        String fileName = fileDescriptions[0];
        File file = new File(fileName);
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        byte buffer[] = new byte[(int)file.length()];
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
            input.read(buffer, 0, buffer.length);
            input.close();
            fi.uploadFile(username, fileDescriptions, buffer);
            System.out.println(fileName + " uploaded successfully!\n");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    public void download(String username,String fileName) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        byte[] fileData = fi.downloadFile(username,fileName);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileName));
        output.write(fileData,0,fileData.length);
        output.flush();
        output.close();
        System.out.println(fileName + " downloaded successfully!\n");
    }

    public void search(String username, String fileName, String type) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        fi.showSearch(username, fileName, type);
    }

    public boolean checkCredentials(String username, String password) throws RemoteException, NotBoundException, MalformedURLException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        return fi.checkCredentials(username,password);
    }

    public boolean checkUser(String username) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        return fi.checkUser(username);
    }

    public void addCredentials(String username, String password) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        fi.addCredentials(username,password);
        System.out.println("User " + username + " created successfully!\n");
    }
}
