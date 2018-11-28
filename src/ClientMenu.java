import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class ClientMenu {

    public static void main(String[] argv) throws IOException, NotBoundException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Welcome to the RMI Client:\n1.Sign in\t2.Sign up\n");
        int sign = reader.nextInt();
        String userPass = sign(sign);
        String username = getUsername(userPass);
        String password = getPassword(userPass);
        reader = new Scanner(System.in);  // Reading from System.in
        System.out.println( "Hello " + username + ", what do you want to do?\n" +
                            "1.Upload multimedia    2.Download multimedia\n" +
                            "3.Search               4.Subscribe\n" +
                            "0.Exit");
        int n = reader.nextInt(); // Scans the next token of the input as an int.
        String fileName;
        switch (n) {
            case 0:
                System.out.println("Exiting...\n");
                System.exit(0);
            case 1: // Upload case
                reader = new Scanner(System.in);
                System.out.println("What file do you want to upload? (File must be inside the project)\n");
                fileName = reader.nextLine();
                ClientLogicLayer upload = new ClientLogicLayer();
                upload.upload(username,fileName);
                break;
            case 2: // Download case
                reader = new Scanner(System.in);
                System.out.println("What file do you want to download?\n");
                fileName = reader.nextLine();
                ClientLogicLayer download = new ClientLogicLayer();
                download.download(username,fileName);
                break;
            case 3: // Search case
                System.out.println("Search: " + n);
                break;
            case 4: // Subscription to topic case
                System.out.println("Subscribe: " + n);
                break;
            default:
                System.out.println("Enter one of the options listed above.\n");
                System.exit(0);
        }
        // Close the Scanner once finished
        reader.close();
    }

    private static String sign(int sign) {
        if(sign == 1) {
            Scanner reader = new Scanner(System.in);
            System.out.println("user: ");
            String username = reader.nextLine();
            reader = new Scanner(System.in);
            System.out.println("password: ");
            String pass = reader.nextLine();
            return username + "_" + pass;
        } else {
            return "ok";
        }
    }

    private static String getUsername(String userPass) {
        return userPass.substring(0,userPass.indexOf("_"));
    }

    private static String getPassword(String userPass) {
        return userPass.substring(userPass.indexOf("_") + 1);
    }
}
