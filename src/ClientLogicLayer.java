import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientLogicLayer {

    private int portNum = 1099;
    private String registryURL = "rmi://localhost:" + portNum + "/some";

    ClientLogicLayer() {}

    public void upload(String username,String[] fileDescriptions) throws IOException, NotBoundException {
        String filePath = fileDescriptions[0];
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        File file = new File(filePath);
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        byte buffer[] = new byte[(int)file.length()];
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath));
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

    public boolean checkFile(String username, String fileName) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        return fi.checkFile(username, fileName);
    }

    public ArrayList<String> getUserTitles(String username) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        return fi.showSearch(username,"","ti");
    }

    public void changeTitle(String username, int filePosition, String oldTitle, String newTitle) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        if(fi.changeTitle(username, filePosition, newTitle)) {
            System.out.println("Changed title from " + oldTitle + " to " + newTitle + ".\n");
        } else {
            System.out.println("Some error has occurred.\n");
        }
    }

    public void deleteFile(String username,int filePosition, String oldTitle) throws IOException, NotBoundException {
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        if (fi.deleteFile(username, filePosition)) {
            System.out.println("File with title " + oldTitle + " deleted successfully!\n");
        } else {
            System.out.println("Cannot delete file or file does not exist.\n");
        }

    }
}
