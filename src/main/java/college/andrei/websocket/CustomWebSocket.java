package college.andrei.websocket;

import college.andrei.CollegeMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.*;

import java.io.IOException;

/**
 * <a href="https://medium.com/swlh/how-to-build-a-websocket-applications-using-java-486b3e394139">
 *     Link to page used to write this</a>
 */
@ClientEndpoint
public class CustomWebSocket {
    public static Session session;
    public static final Gson GSON = new GsonBuilder().create();

    public static void sendData(WebSocketData data) {
        if (CustomWebSocket.session == null) {
            return;
        }

        try {
            CustomWebSocket.session.getBasicRemote().sendText(GSON.toJson(data));
        } catch (IOException ignored) {}
    }

    @OnOpen
    public void onOpen(Session session) {
        CollegeMod.LOGGER.info("Websocket created!");
        CustomWebSocket.session = session;
    }

    @OnMessage
    public void onMessage(String data, Session session) {
        WebSocketData parsedData = GSON.fromJson(data, WebSocketData.class);

        switch (parsedData.opcode) {
            case 15 -> // GET_ALLOWED_MEMBERS
                    AllowedMembersResponse.handle(data);
            case 16 -> // GLOBAL_DATA
                    GlobalData.handle();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws IOException {
        CustomWebSocket.session = null;
        CollegeMod.LOGGER.warn("Websocket closed with code: " + closeReason.getCloseCode().getCode() + " - " + closeReason.getReasonPhrase());

        if (closeReason.getCloseCode().getCode() != 1000) {
            Webhook.sendWebhookWarning();
        }
    }
}
