import java.io.*;

public class ServerStorage {
    private String path = "C:/Users/Public/";

    ServerStorage(){}

    void saveFile(String username, byte[] buffer, String[] fileDescriptions) throws IOException {
        String fileName = fileDescriptions[0];
        String title = fileDescriptions[1];
        String topic = fileDescriptions[2];
        // If username = Client1 and file to upload = file1.txt
        String newPath = createDir(path,"Client Files");
        // newPath = C:/Users/Public/Client Files/
        newPath = createDir(newPath + "/",username);
        // newPath = C:/Users/Public/Client Files/Client1
        createDir(newPath + "/",username + "_" + fileName.substring(0,fileName.length() - 3));
        /*
            So: if username = Client1 and file to upload = file1.txt, the hierarchy would be:
            C:/Users/Public/Client Files/Client1/Client1_file1
        */

        FileOutputStream f = new FileOutputStream(path + username + "_" + fileName);
        f.write(buffer);
    }

    // Creates a folder in the path if not already exist
    private String createDir(String path, String dir) {
        if (!fileExists(path, "Client Files")) {
            System.out.println("Creating folder in " + path + dir);
            new File(path + "Client Files").mkdirs();
        }
        return path + dir + "/";
    }

    void downloadFile(String username, String fileName) {

    }

    void addCredentials(String username, String password) throws IOException {
        String credentials = username + "_" + password;
        if(!fileExists(path,"ClientCredentials.txt"))
            System.out.println("Creating file in " + path + "ClientCredentials.txt\n");
        BufferedWriter writer =
                new BufferedWriter(new FileWriter(path + "ClientCredentials.txt",true));
        writer.write(credentials);
        writer.newLine();
        writer.close();
    }

    private boolean fileExists(String path,String nameToSearch) {
        File filesDir = new File(path);
        File[] dirContents = filesDir.listFiles();
        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().equals(nameToSearch)) return true;
        }
        return false;
    }

    boolean checkCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(path + "ClientCredentials.txt"))) {
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
            try (BufferedReader br = new BufferedReader(new FileReader(path + "ClientCredentials.txt"))) {
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
}
