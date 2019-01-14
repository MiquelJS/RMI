import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SomeServer {

    private static final int RMIPortNum = 1099;
    private static final int portNum = 1099;

    public static void main(String args[]) {
        try {
            // code for port number value to be supplied
            SomeImpl exportedObj = new SomeImpl();
            startRegistry(RMIPortNum);
            // register the object under the name "some"
            String registryURL = "rmi://localhost:" + portNum + "/some";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server ready.\n");

        }// end try
        catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private static void startRegistry(int RMIPortNum)
            throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list( );
            // The above call will throw an exception if the registry does not already exist
        }
        catch (RemoteException ex) {
            // No valid registry at that port.
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    } // end startRegistry
}