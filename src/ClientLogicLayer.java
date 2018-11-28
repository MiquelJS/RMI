import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientLogicLayer {

    private int portNum = 1099;

    ClientLogicLayer() {}

    public void upload(String username,String fileName) throws IOException, NotBoundException {
        File file = new File(fileName);
        String registryURL = "rmi://localhost:" + portNum + "/some";
        SomeInterface fi = (SomeInterface) Naming.lookup(registryURL);
        byte buffer[] = new byte[(int)file.length()];
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file.getName()));
        input.read(buffer,0,buffer.length);
        input.close();
        fi.uploadFile(username,fileName,buffer);
        System.out.println(fileName + " uploaded successfully!\n");
    }

    public void download(String username,String fileName) throws IOException, NotBoundException {
        File file = new File(fileName);
        String name = "rmi://localhost:" + portNum + "/some";
        SomeInterface fi = (SomeInterface) Naming.lookup(name);
        byte[] fileData = fi.downloadFile(username,fileName);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
        output.write(fileData,0,fileData.length);
        output.flush();
        output.close();
        System.out.println(fileName + " downloaded successfully!\n");
    }
}
