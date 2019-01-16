import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WSConnectionManager {

    private String serverAddress;
    private String ngrokWSAddress = "http://59656c41.ngrok.io/mytubeWeb/rest/";

    public WSConnectionManager(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void postUserCredentials(String username, String password) {
        String json = createJSon(Arrays.asList( "username", username,
                                                "password", password,
                                                "serverAddress", serverAddress));
        post("users", json);
    }

    private String createJSon(List<Object> params) {
        String json = "{";
        for (int i = 0; i < params.size(); i++) {
            json += "\"" + params.get(i) + "\"";
            if (i % 2 == 0) {
                json += ":";
            } else {
                json += ",";
            }
        }
        json = json.substring(0, json.length() - 1).concat("}");
        System.out.println(json);
        return json;
    }

    public void postFile(String username, String fileName, String title, String topic) {
        String json = createJSon(Arrays.asList( "username", username,
                                                "fileName", fileName,
                                                "title", title,
                                                "topic", topic,
                                                "serverAddress", serverAddress));
        post("contents/" + fileName, json);
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = get("users/all");
        System.out.println("\nUsers:" + Arrays.toString(users.toArray()) + "\n");
        return users;
    }

    public ArrayList<String> getUserCredentials(String username) {
        ArrayList<String> users = get("users/all/" + username);
        System.out.println("\nUsers Cred:" + Arrays.toString(users.toArray()) + "\n");
        return users;
    }

    public ArrayList<String> getFiles(String username, String filename, String type) {
        // type = "ti" -> search by title
            // -> username = "" -> search all files
            // -> username != "" -> search this user's files
        // type = "to" -> search by topic
            // -> username = "" -> search all files
            // -> username != "" -> search this user's files
        ArrayList<String> filesNames;
        if (type.equals("ti"))
            filesNames = searchFilesByTitle(username, filename);
        else
            filesNames = searchFilesByTopic(username, filename);
        return filesNames;
    }

    private ArrayList<String> searchFilesByTitle(String username, String filename) {

        return null;
    }

    private ArrayList<String> searchFilesByTopic(String username, String filename) {

        return null;
    }

    private ArrayList<String> get(String path) {
        ArrayList<String> getArray = new ArrayList<>();
        try {
            URL url = new URL(ngrokWSAddress + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", "application/json"); //application/json
            if(conn.getResponseCode() < 200 || conn.getResponseCode() > 200){
                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null){
                getArray.add(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getArray;
    }

    private void post(String path, String json) {
        try {
            URL url = new URL(ngrokWSAddress + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json"); //application/json
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            if(conn.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null){
                System.out.println(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
