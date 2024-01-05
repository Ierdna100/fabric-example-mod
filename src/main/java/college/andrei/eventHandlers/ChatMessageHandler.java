package college.andrei.eventHandlers;

import college.andrei.bot.CustomWebSocket;
import college.andrei.bot.WSOpcodes;
import college.andrei.bot.WebSocketData;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageHandler {
    public static void onChatMessage(SignedMessage message, ServerPlayerEntity sender, Parameters params) {
        CustomWebSocket.sendData(new ChatMessage(message.getContent().getString(), message.getSender().toString()).toJsonable());
    }

    public static class ChatMessage {
        private final String message;
        private final String sender;

        public ChatMessage(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }

        public WebSocketData<ChatMessage> toJsonable() {
            return new WebSocketData<>(WSOpcodes.MESSAGE.getOpcode(), this);
        }
    }
}
