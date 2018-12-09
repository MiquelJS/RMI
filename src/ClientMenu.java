import com.sun.security.ntlm.Client;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientMenu {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static String username;
    private static String password;
    private static ClientLogicLayer clientLogicLayer = new ClientLogicLayer();
    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] argv) throws IOException, NotBoundException {
        welcomeClient();
    }

    private static void welcomeClient() throws IOException, NotBoundException {
        System.out.println("Welcome to the RMI Client:\n1.Sign in\t2.Sign up\n0.Exit");
        int userNumber = checkIfNumber(reader.nextLine());
        clientSign(userNumber);
        clientMainMenu();
    }

    private static void clientSign(int sign) throws IOException, NotBoundException {
        switch (sign) {
            case 1: // Sign in
                readUserAndPass();
                try {
                    boolean checkCred =  clientLogicLayer.checkCredentials(username,password);
                    while(!checkCred) {
                        System.out.println(ANSI_RED + "Incorrect username or password.\n" + ANSI_RESET);
                        readUserAndPass();
                        checkCred = clientLogicLayer.checkCredentials(username,password);
                    }
                } catch (RemoteException|NotBoundException|MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:// Sign up
                readUserAndPass();
                boolean checkUser = clientLogicLayer.checkUser(username);
                while(checkUser || username.length() == 0 || password.length() == 0) {
                    if (username.length() == 0 || password.length() == 0) {
                        System.out.println(ANSI_RED + "Incorrect username or password.\n" + ANSI_RESET);
                    } else {
                        System.out.println(ANSI_RED + "This user already exist.\n" + ANSI_RESET);
                    }
                    readUserAndPass();
                    checkUser = clientLogicLayer.checkUser(username);
                }
                clientLogicLayer.addCredentials(username,password);
                break;
            default:// Exit
            System.out.println("Exiting...\n");
            reader.close(); // Close the Scanner once finished
            System.exit(0);
        }
    }

    private static void clientMainMenu() throws IOException, NotBoundException {
        System.out.println( "Hello " + username + ", what do you want to do?\n" +
                "1.Upload multimedia        2.Download multimedia\n" +
                "3.Search by file           4.Search by topic\n" +
                "5.Edit/Delete multimedia   0.Logout");
        int userNumber = checkIfNumber(reader.nextLine()); // Just an error checker function
        switch (userNumber) {
            case 0:
                System.out.println("Logging out...\n");
                username = null;
                password = null;
                welcomeClient();
                break;
            case 1: // Upload case
                // fileDescriptions have: [file,title,topic]
                String[] fileDescriptions = uploadFileWithBrowser();
                clientLogicLayer.upload(username,fileDescriptions);
                break;
            case 2: // Download case
                System.out.println("What file do you want to download?\n");
                String fileName = reader.nextLine();
                clientLogicLayer.download(fileName);
                break;
            case 3: // Search case
                System.out.println("What file do you want to search?\n");
                String fileSearch = reader.nextLine();
                System.out.println("The Titles found with the file search are:");
                System.out.println(clientLogicLayer.search("",fileSearch, "ti"));
                break;
            case 4: // Subscription to topic case
                System.out.println("What topic do you want to search?\n");
                fileSearch = reader.nextLine();
                System.out.println("The Titles found with the topic search are:");
                System.out.println(clientLogicLayer.search("",fileSearch, "to"));
                break;
            case 5: // Edit/Delete case
                editDeleteCase();
                break;
            default:
                System.out.println("Enter one of the options listed above.\n");
        }
        clientMainMenu();
    }

    private static void editDeleteCase() throws IOException, NotBoundException {
        ArrayList<String> userMultimedia = clientLogicLayer.getUserTitles(username);
        if (userMultimedia == null) {
            System.out.println("This user has no uploaded media\n");
            clientMainMenu();
        }
        String showUserOptions = "What file do you want to edit/delete?\n";
        for(int i = 0; i < userMultimedia.size(); i++) {
            showUserOptions = showUserOptions + (i + 1) + "." + userMultimedia.get(i) + "\t\t";
            if((i + 1) % 3 == 0) { // prints in a new line every 3 multimedia files
                showUserOptions = showUserOptions + "\n";
            }
        }
        System.out.println(showUserOptions + "0.Exit\n");  // Displays all possible options
        int chosenOption = checkIfNumber(reader.nextLine());
        if (chosenOption < 1 || chosenOption > userMultimedia.size()) {
            clientMainMenu();
        } else {
            editDeleteFile(chosenOption - 1,userMultimedia.get(chosenOption - 1));
        }
    }

    private static void editDeleteFile(int filePosition, String title) throws IOException, NotBoundException {
        System.out.println( "Do you want to:\n" +
                            "1.Edit        2.Delete\n" +
                            "0.Exit");
        int editDelete = checkIfNumber(reader.nextLine());
        switch (editDelete) {
            case 1: // Edit file
                System.out.println("What new title would you like this file to have? \n");
                clientLogicLayer.changeTitle(username, filePosition, title, reader.nextLine());
                break;
            case 2: // Delete file
                System.out.println("Are you sure you want to delete the file with title " + title + "? [y/n]");
                if (reader.nextLine().equals("y")) {
                    clientLogicLayer.deleteFile(username, filePosition, title);
                } else {
                    clientMainMenu();
                }
                break;
            default:
                clientMainMenu();
        }
    }

    // This function is only an error checker for the user's possible outputs
    private static int checkIfNumber(String n) throws IOException, NotBoundException {
        int number = 0;
        if (isNumeric(n)) { number = Integer.parseInt(n);}
        else {
            if (username == null) {
                welcomeClient();
            } else {
                clientMainMenu();
            }
        }
        return number;
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private static String[] uploadFileWithBrowser() throws IOException, NotBoundException {
        String[] fileDescriptions = new String[3];
        System.out.println("What file do you want to upload?\n");
        //Create a file chooser
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.ICONIFIED);
        frame.setExtendedState(JFrame.NORMAL);
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        if(JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
            frame.setVisible(false);
            System.out.println("You selected the file: " + fc.getSelectedFile().getName() + "\n");
            fileDescriptions[0] = fc.getSelectedFile().getAbsolutePath();

            if(clientLogicLayer.checkFile(username, fc.getSelectedFile().getName())) {
                System.out.println("This file already exist, do you want to continue? [y/n]\n");
                if(!reader.nextLine().equals("y")) {
                    clientMainMenu();
                }
            }

            System.out.println("What title does your file have?\n");
            fileDescriptions[1] = reader.nextLine();

            System.out.println("And what topic?\n");
            fileDescriptions[2] = reader.nextLine();
            return fileDescriptions;
        } else {
            clientMainMenu();
        }
        return null;
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
