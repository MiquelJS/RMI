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
            System.out.println("Trying to connect to Web Service...\n");
            test();

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


    // -------------------------- Web Service connection ---------------
    public static void test(){
        // https://stackoverflow.com/questions/12916169/how-to-consume-rest-in-java
        try {
            String test_URL = "http://9c806146.ngrok.io/mytubeWeb/rest/text/";
            URL url = new URL(test_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/plain"); //application/json
            if(conn.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null){
                System.out.println(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}