import java.io.FileOutputStream;
import java.io.IOException;

public class ServerStorage {

    private String filePath = "C:/Users/Miquel/Desktop/UdL/Computació Distribuida/RMI/Server Storage/";
    public ServerStorage(){}

    public void saveFile(String username, byte[] buffer, String fileName) throws IOException {
        FileOutputStream f = new FileOutputStream(filePath + username + "_" + fileName);
        f.write(buffer);
    }

    public void downloadFile(String username, String fileName) {

    }
}
