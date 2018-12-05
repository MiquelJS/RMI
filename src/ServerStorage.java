import java.io.*;

public class ServerStorage {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private String path = "C:/Users/Public/";

    ServerStorage(){}

    void saveFile(String username, byte[] buffer, String[] fileDescriptions) throws IOException {
        String fileName = fileDescriptions[0];
        String title = fileDescriptions[1];
        String topic = fileDescriptions[2];

        // If username = Client1 and file to upload = file1.txt
        String newPath = createDir(path + "Server Storage/","Client Files");
        // newPath = C:/Users/Public/Server Storage/Client Files/

        newPath = createDir(newPath,username);
        // newPath = C:/Users/Public/Server Storage/Client Files/Client1/

        newPath = createDir(newPath,username + "_" + fileName.substring(0,fileName.indexOf(".")));
        // newPath = C:/Users/Public/Server Storage/Client Files/Client1/Client1_file1/

        // Creating file in newPath path
        FileOutputStream f = new FileOutputStream(newPath + username + "_" + fileName);
        f.write(buffer);

        // Creating file with the title and topic in newPath path
        BufferedWriter descriptions = new BufferedWriter(
                new FileWriter(newPath + username + "_" + fileName.substring(0,fileName.indexOf(".")) + "_description.txt"));
        descriptions.write("title: " + title);
        descriptions.newLine();
        descriptions.write("topic: " + topic);
        descriptions.close();
    }

    void downloadFile(String username, String fileName) {

    }

    void showMedia(String filename, String type) throws IOException {
        File[] files = new File("C:/Users/Public/Server Storage/Client Files/").listFiles();
        readFiles(files, filename, type);
    }

    public static void readFiles(File[] files, String filename, String type) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                readFiles(file.listFiles(), filename, type); // Calls same method again.
            } else {
                String lookingFor;
                if(type.equals("ti")) {
                    lookingFor = "title: ".concat(filename);
                } else {
                    lookingFor = "topic: ".concat(filename);
                }
                String st;
                BufferedReader br = new BufferedReader(new FileReader(file));
                while (( st = br.readLine()) != null){
                    if (st.equals(lookingFor)){
                        System.out.println("We found a match! in :" + file);
                    }
                }
            }
        }
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
    }


    boolean checkCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(path + "Server Storage/ClientCredentials.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.equals(username + "_" + password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;


    }

    public boolean checkUser(String username) {
        String newPath = createDir(path,"Server Storage");
        try (BufferedReader br = new BufferedReader(new FileReader(newPath + "ClientCredentials.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("_");
                String part1 = parts[0];
                if(part1.equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private boolean fileOrFolderExists(String path,String nameToSearch) {
        File filesDir = new File(path);
        File[] dirContents = filesDir.listFiles();
        if (dirContents == null) return false;
        for (int i = 0; i < dirContents.length - 1; i++) {
            if (dirContents[i].getName().equals(nameToSearch)) return true;
        }
        return false;
    }
}
