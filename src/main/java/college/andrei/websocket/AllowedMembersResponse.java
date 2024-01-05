package college.andrei.websocket;

import college.andrei.bot.WebSocketData;
import college.andrei.mixinHelpers.LoginHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllowedMembersResponse {
    public static Gson GSON = new GsonBuilder().create();

    public static void handle(String data) {
        WebSocketData.AllowedMemberResponse response = GSON.fromJson(data, WebSocketData.AllowedMemberResponse.class);
        LoginHelper.addNewMembers(response.data);
    }
}
