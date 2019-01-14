import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WSConnectionManager {

    private String serverAddress;
    private String ngrokWSAddress = "http://9c806146.ngrok.io/mytubeWeb/rest/";

    public WSConnectionManager(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void postUserCredentials(String username, String password) {
        try {
            String test_URL = ngrokWSAddress + "users/";
            URL url = new URL(test_URL);
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

    private String createJSon(List<String> params) {
        String json = "{";
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).getClass().getName().equals("String")) {
                json.concat("\"" + params.get(i) + "\"");
            } else {
                json.concat(params.get(i));
            }
            if (i % 2 == 0) {
                json.concat(",");
            } else {
                json.concat(":");
            }
        }
        return json.substring(json.length() - 2).concat("}");
    }

    public void postFile(String username, String fileName, String title, String topic) {
        try {
            String test_URL = ngrokWSAddress + "users/";
            URL url = new URL(test_URL);
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
}
