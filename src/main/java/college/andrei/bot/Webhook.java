package college.andrei.bot;

import college.andrei.CollegeMod;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Webhook {
    public static void sendWebhookWarning() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(CollegeMod.BACKUP_WEBHOOK_URI);

        String content = "{\"content\":\"<@337662083523018753> **Discord server crashed!**\"}";
        httpPost.setHeader("Content-Type", "application/Json");
        httpPost.setEntity(new StringEntity(content));

        try {
            httpClient.execute(httpPost);
        } catch (IOException e) {
            CollegeMod.LOGGER.error("Could not execute httpClient for backup message!");
        }

        httpClient.close();
    }
}
