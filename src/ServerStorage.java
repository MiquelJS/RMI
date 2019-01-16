import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServerStorage {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private String path = "C:/Users/Public/";
    private static ArrayList<String> listToReturn;
    private static ArrayList<String> fileToReturn;

    private WSConnectionManager wsConn = new WSConnectionManager(InetAddress.getLocalHost().getHostAddress());

    ServerStorage() throws UnknownHostException {}

    void saveFile(String username, byte[] buffer, String[] fileDescriptions) throws IOException {
        String filePath = fileDescriptions[0];
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        String title = fileDescriptions[1];
        String topic = fileDescriptions[2];

        // If username = Client1 and file to upload = file1.txt
        String newPath = createDir(path + "Server Storage/","Client Files");
        // newPath = C:/Users/Public/Server Storage/Client Files/

        newPath = createDir(newPath,username);
        // newPath = C:/Users/Public/Server Storage/Client Files/Client1/

        newPath = createDir(newPath,username + "_" + fileName.substring(0,fileName.lastIndexOf(".")));
        // newPath = C:/Users/Public/Server Storage/Client Files/Client1/Client1_file1/

        // Creating file in newPath path
        FileOutputStream f = new FileOutputStream(newPath + username + "_" + fileName);
        f.write(buffer);

        // Creating file with the title and topic in newPath path
        BufferedWriter descriptions = new BufferedWriter(
                new FileWriter(newPath + username + "_" + fileName.substring(0,fileName.lastIndexOf(".")) + "_description.txt"));
        descriptions.write("title: " + title);
        descriptions.newLine();
        descriptions.write("topic: " + topic);
        descriptions.close();
        f.close();

        // Notify the WebService that a file has been added to the RMI server
        wsConn.postFile(username, fileName, title, topic);
    }

    ArrayList<Object> downloadFile(String fileTitle) throws IOException {
        listToReturn = new ArrayList<>();
        File[] files = new File("C:/Users/Public/Server Storage/Client Files/").listFiles();
        FindFileByTitle(files, fileTitle);

        ArrayList<Object> toReturn = new ArrayList<>();
        String[] type = fileToReturn.get(0).split("\\.");

        toReturn.add(path);
        File pathToDownload = new File("C:/Users/Public/"+ "/" + fileTitle + "." + type[type.length - 1]);
        toReturn.add(pathToDownload );
        toReturn.add(fileToReturn.get(0));
        return toReturn;
    }

    File FindFileByTitle (File[] files, String fileTitle) throws IOException  {
        fileToReturn = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory() && listToReturn.size() < 1) {
                FindFileByTitle(file.listFiles(), fileTitle); // Calls same method again.
            }else {
                fileToReturn.add(String.valueOf(file));
                if ((file.getName().contains("description.txt"))){
                    String st;
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while (( st = br.readLine()) != null){
                        String[] toCompare = st.split("title: ");
                        if(String.valueOf(toCompare[1]).equals(fileTitle)){
                            listToReturn.add(String.valueOf(file));
                            return null;
                        }else{break;}
                    }
                }
            }
        }
        return null;
    }

    void addCredentials(String username, String password) throws IOException {
        String credentials = username + "_" + password;
        String newPath = path + "Server Storage/";
        // newPath = C:/Users/Public/Server Storage/
        if(!fileOrFolderExists(newPath,"ClientCredentials.txt"))
            System.out.println( ANSI_GREEN + "Creating file in " + newPath + "ClientCredentials.txt\n" + ANSI_RESET);
        BufferedWriter writer =
                new BufferedWriter(new FileWriter(newPath + "ClientCredentials.txt",true));
        writer.write(credentials);
        writer.newLine();
        writer.close();

        // Notify the WebService that a user has been added
        wsConn.postUserCredentials(username, password);
    }

    boolean checkCredentials(String username, String password) {
        String[] userCred = wsConn.getUserCredentials(username).get(0).split(",");
        return username.equals(userCred[0].substring(2,userCred[0].length() - 1)) && password.equals(userCred[1].substring(1, userCred[1].length() - 1));
    }

    boolean checkUser(String username) {
        ArrayList<String> users = wsConn.getAllUsers();
        for (String user : users) {
            if (user.equals(username))
                return true;
        }
        return false;
    }

    // Creates a folder in the path if not already exist
    private String createDir(String path, String dir) {
        if (!fileOrFolderExists(path, dir)) {
            System.out.println(ANSI_GREEN + "Creating " + path + dir + "\n" + ANSI_RESET);
            new File(path + dir).mkdirs();
        }
        return path + dir + "/";
    }

    private boolean fileOrFolderExists(String newPath,String nameToSearch) {
        File[] dirContents = new File(newPath).listFiles();
        if (dirContents == null) return false;
        for (File file : dirContents) {
            if (file.getName().equals(nameToSearch)) return true;
        }
        return false;
    }

    boolean checkFile(String username, String fileName) {
        String serverFilePath = path + "Server Storage/Client Files/" +
                                username + "/" + username + "_" +
                                fileName.substring(0,fileName.lastIndexOf(".")) + "/";
        return Files.exists(Paths.get(serverFilePath));
    }

    ArrayList<String> showMedia(String username, String search, String type) {
        return wsConn.getFiles(username, search, type);
    }

    boolean changeTitle(String username, int filePosition, String newTitle) throws IOException {
        return changeDescription(getFileFromPosition(username, filePosition), newTitle);
    }

    boolean deleteFile(String username, int filePosition) {
        File file = getFileFromPosition(username,filePosition);
        if (file.isDirectory()) {
            for (File c : file.listFiles()) {
                c.delete();
            }
        }
        return file.delete(); // Returns false if file or folder could not be deleted
    }

    private File getFileFromPosition(String username, int filePosition) {
        File[] files = new File(path + "Server Storage/Client Files/" + username + "/").listFiles();
        int position = 0;
        for (File file : files) {
            if(position == filePosition) {
                return file;
            }
            position++;
        }
        return null;
    }

    private boolean changeDescription(File file, String newTitle) throws IOException {
        if (file == null) return false;
        for (File descriptionFile : file.listFiles()) {
            if (descriptionFile.getName().equals(file.getName() + "_description.txt")) {
                List<String> lines = new ArrayList<>();
                String line;
                FileReader fr = new FileReader(descriptionFile);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    if (line.contains("title: "))
                        line = line.replace(line, "title: " + newTitle);
                    lines.add(line);
                }
                fr.close();
                br.close();
                FileWriter fw = new FileWriter(descriptionFile);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(lines.get(0)); // Writes the title
                bw.newLine();
                bw.write(lines.get(1)); // Writes the topic
                bw.flush();
                fw.close();
                bw.close();
                return true;
            }
        }
        return false;
    }
}
