import java.io.*;

public class ServerStorage {

    private String filePath = "C:/Users/Miquel/Desktop/UdL/Computació Distribuida/RMI/Server Storage/Client Files";
    private String clientCredentialsPath = "C:/Users/Public/";

    ServerStorage(){}

    void saveFile(String username, byte[] buffer, String fileName) throws IOException {
        FileOutputStream f = new FileOutputStream(filePath + username + "_" + fileName);
        f.write(buffer);
    }

    void downloadFile(String username, String fileName) {

    }

    void addCredentials(String username, String password) throws IOException {
        String credentials = username + "_" + password;
        if(!fileExists()) System.out.println("Creating file in " + clientCredentialsPath + "ClientCredentials.txt\n");
        BufferedWriter writer = new BufferedWriter(new FileWriter(clientCredentialsPath + "ClientCredentials.txt",true));
        writer.write(credentials);
        writer.newLine();
        writer.close();
    }

    private boolean fileExists() {
        File filesDir = new File(clientCredentialsPath);
        File[] dirContents = filesDir.listFiles();
        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().equals("ClientCredentials.txt")) return true;
        }
        return false;
    }

    boolean checkCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(clientCredentialsPath + "ClientCredentials.txt"))) {
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
            try (BufferedReader br = new BufferedReader(new FileReader(clientCredentialsPath + "ClientCredentials.txt"))) {
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
