package cz.vsb;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.IOException;

public class Application {

    public void createRequest(){
        JFrame frame = new JFrame("Server request");
        HttpPost post = new HttpPost("http://" + PropertyLoader.loadProperty("webAppIP") + "/update_files");
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            JOptionPane.showMessageDialog(frame, EntityUtils.toString(response.getEntity()), "Server response", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "There is a problem with connection!", "Server response", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }
    }

}
