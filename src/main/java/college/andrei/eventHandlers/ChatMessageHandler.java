package college.andrei.eventHandlers;

import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageHandler {
    public static void onChatMessage(SignedMessage message, ServerPlayerEntity sender, Parameters params) {
        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("timestamp", message.getTimestamp().toString()));
        postParams.add(new BasicNameValuePair("message", message.getContent().getString()));
        postParams.add(new BasicNameValuePair("sender", message.getSender().toString()));

        Bot.sendPostInteraction(postParams, HTTPEndpoints.MESSAGE);
    }
}
