package college.andrei.bot;

import college.andrei.CollegeMod;
import college.andrei.mixinHelpers.AllowedMember;
import college.andrei.mixinHelpers.IP;
import college.andrei.mixinHelpers.LoginHelper;
import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    public static boolean botRunning = true;
    public static final String HTTPURI = "http://localhost:3500/";

    public static void sendPostInteraction(HTTPEndpoints endpoint) {
        Bot.sendPostInteraction(null , endpoint);
    }

    public static void sendPostInteraction(List<NameValuePair> data, HTTPEndpoints endpoint) {
        if (!botRunning) {
            return;
        }

        HttpPost postReq = new HttpPost(HTTPURI + endpoint.getEndpoint().toLowerCase());

        try {
            if (data != null) {
                postReq.setEntity(new UrlEncodedFormEntity(data));
            }

            CloseableHttpResponse res = CollegeMod.client.execute(postReq);
            int resCode = res.getStatusLine().getStatusCode();
            res.close();

            // New data available to fetch
            if (resCode == HTTPResponseCodes.NEW_MEMBER_DATA_AVAILABLE.getResponseCode()) {
                Bot.sendGetInteraction(HTTPEndpoints.GET_ALLOWED_MEMBERS);
            }

            // If not a 2xx code
            if (resCode / 100 != 2) {
                botRunning = false;
                throw new Exception("Server responded with error code " + resCode + ". Stopping HTTP Client");
            }
        }
        catch (Exception e) {
            CollegeMod.LOGGER.error(e.toString());
        }
    }

    public static void sendGetInteraction(HTTPEndpoints endpoint) {
        HttpGet getReq = new HttpGet("http://localhost:3500/" + endpoint.getEndpoint().toLowerCase());

        try {
            CloseableHttpResponse res = CollegeMod.client.execute(getReq);
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            res.close();

            Gson gson = new Gson();
            AllowedMember.JSON[] allowedMembersRaw = gson.fromJson(response.toString(), AllowedMember.JSON[].class);

            LoginHelper.allowedMembers = new ArrayList<>();
            for (AllowedMember.JSON allowedMemberRaw : allowedMembersRaw) {
                List<IP> allowedIps = new ArrayList<>();

                for (String allowedIpAsString : allowedMemberRaw.getAllowedIps()) {
                    allowedIps.add(new IP(allowedIpAsString));
                }

                LoginHelper.allowedMembers.add(new AllowedMember(allowedMemberRaw.getName(), allowedIps));
            }
        }
        catch (Exception e) {
            CollegeMod.LOGGER.error(e.toString());
        }
    }
}
