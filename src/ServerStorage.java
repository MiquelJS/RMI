import java.io.*;

public class ServerStorage {

    private String filePath = "C:/Users/Miquel/Desktop/UdL/Computació Distribuida/RMI/Server Storage/Client Files";
    private String clientCredentialsPath = "C:/Users/Miquel/Desktop/UdL/Computació Distribuida/RMI/Server Storage/";

    public ServerStorage(){}

    public void saveFile(String username, byte[] buffer, String fileName) throws IOException {
        FileOutputStream f = new FileOutputStream(filePath + username + "_" + fileName);
        f.write(buffer);
    }

    public void downloadFile(String username, String fileName) {

    }

    public void addClientCredentials(String username, String password) throws IOException {
        String credentials = username + "_" + password;
        BufferedWriter writer = new BufferedWriter(new FileWriter("ClientCredentials.txt",true));
        writer.append('\n');
        writer.append(credentials);
        writer.close();
    }
    public boolean checkCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("ClientCredentials.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.equals(username + "_" + password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
