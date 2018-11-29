import java.io.*;

public class ServerStorage {

    private String filePath = "C:/Users/Miquel/Desktop/UdL/Computació Distribuida/RMI/Server Storage/Client Files";
    private String clientCredentialsPath = "C:/Users/Miquel/Desktop/UdL/Computació Distribuida/RMI/Server Storage/";

    ServerStorage(){}

    void saveFile(String username, byte[] buffer, String fileName) throws IOException {
        FileOutputStream f = new FileOutputStream(filePath + username + "_" + fileName);
        f.write(buffer);
    }

    void downloadFile(String username, String fileName) {

    }

    void addCredentials(String username, String password) throws IOException {
        String credentials = username + "_" + password;
        BufferedWriter writer = new BufferedWriter(new FileWriter(clientCredentialsPath + "ClientCredentials.txt",true));
        writer.append('\n');
        writer.append(credentials);
        writer.close();
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
}
