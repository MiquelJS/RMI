import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMenu {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    /*
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    */
    private static String username;
    private static String password;
    private static ClientLogicLayer clientUserPass = new ClientLogicLayer();

    public static void main(String[] argv) throws IOException, NotBoundException {
        welcomeClient();
    }

    private static void welcomeClient() throws IOException, NotBoundException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Welcome to the RMI Client:\n1.Sign in\t2.Sign up\n0.Exit");
        clientSign(reader.nextInt());
        clientMainMenu();
    }

    private static void clientSign(int sign) throws IOException, NotBoundException {
        if (sign == 1) { // Sign in
            readUserAndPass();
            try {
                boolean checkCred =  clientUserPass.checkCredentials(username,password);
                while(!checkCred) {
                    System.out.println(ANSI_RED + "Incorrect username or password.\n" + ANSI_RESET);
                    readUserAndPass();
                    checkCred = clientUserPass.checkCredentials(username,password);
                }
            } catch (RemoteException|NotBoundException|MalformedURLException e) {
                e.printStackTrace();
            }
        } else if(sign == 2) { // Sign up
            readUserAndPass();
            boolean checkUser = clientUserPass.checkUser(username);
            while(checkUser) {
                System.out.println(ANSI_RED + "This user " + username + " already exist.\n" + ANSI_RESET);
                readUserAndPass();
                checkUser = clientUserPass.checkUser(username);
            }
            clientUserPass.addCredentials(username,password);
        } else { // Exit
            System.out.println("Exiting...\n");
            System.exit(0);
        }
    }

    private static void clientMainMenu() throws IOException, NotBoundException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
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
                // fileDescriptions have: [file,title,topic]
                String[] fileDescriptions = uploadFileDescriptions();
                ClientLogicLayer upload = new ClientLogicLayer();
                upload.upload(username,fileDescriptions);
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

    private static String[] uploadFileDescriptions() {
        String[] fileDescriptions = new String[3];
        Scanner reader = new Scanner(System.in);
        System.out.println("What file do you want to upload? (File must be inside the project)\n");
        fileDescriptions[0] = reader.nextLine();

        reader = new Scanner(System.in);
        System.out.println("What title does your file have?\n");
        fileDescriptions[1] = reader.nextLine();

        reader = new Scanner(System.in);
        System.out.println("And what topic?\n");
        fileDescriptions[2] = reader.nextLine();

        return fileDescriptions;
    }

    private static void readUserAndPass() {
        Scanner reader = new Scanner(System.in);
        System.out.println("user: ");
        username = reader.nextLine();
        reader = new Scanner(System.in);
        System.out.println("password: ");
        password = reader.nextLine();
    }
}
