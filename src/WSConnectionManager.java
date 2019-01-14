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
    private String ngrokWSAddress = "http://88bf2891.ngrok.io/mytubeWeb/rest/";

    public WSConnectionManager(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void postUserCredentials(String username, String password) {
        try {
            URL url = new URL(ngrokWSAddress + "users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json"); //application/json
            String postToWS = createJSon(Arrays.asList( "username", username,
                                                        "password", password,
                                                        "serverAddress", serverAddress));
            OutputStream os = conn.getOutputStream();
            os.write(postToWS.getBytes());
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
        try {
            URL url = new URL(ngrokWSAddress + "file/" + fileName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json"); //application/json
            String postToWS = createJSon(Arrays.asList( "username", username,
                                                        "fileName", fileName,
                                                        "title", title,
                                                        "topic", topic,
                                                        "serverAddress", serverAddress));
            OutputStream os = conn.getOutputStream();
            os.write(postToWS.getBytes());
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

    public ArrayList<String> getUsers(String username) {
        ArrayList<String> users = new ArrayList<>();
        try {
            URL url = new URL(ngrokWSAddress + "users/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json"); //application/json
            if(conn.getResponseCode() < 200 || conn.getResponseCode() > 200){
                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null){
                users.add(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nUsers:" + Arrays.toString(users.toArray()) + "\n");
        return users;
    }

    public ArrayList<String> getFiles(String username, String filename, String type) {
        // type = "ti" -> search by title
            // -> username = "" -> search all files
            // -> username != "" -> search this user's files
        // type = "to" -> search by topic
            // -> username = "" -> search all files
            // -> username != "" -> search this user's files
        ArrayList<String> filesNames = new ArrayList<>();
        if (type.equals("ti"))
            filesNames = searchFilesByTitle(username);
        else
            filesNames = searchFilesByTopic(username);
        return filesNames;
    }

    private ArrayList<String> searchFilesByTitle(String username) {

        return null;
    }

    private ArrayList<String> searchFilesByTopic(String username) {

        return null;
    }

}
