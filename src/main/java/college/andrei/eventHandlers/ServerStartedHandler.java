package college.andrei.eventHandlers;

import college.andrei.CollegeMod;
import college.andrei.bot.CustomWebSocket;
import college.andrei.bot.WSOpcodes;
import college.andrei.bot.WebSocketData;
import net.minecraft.server.MinecraftServer;
import org.glassfish.tyrus.client.ClientManager;

import java.net.URI;

public class ServerStartedHandler {
    public static void onServerStarted(MinecraftServer server) {
        CollegeMod.server = server;

        CollegeMod.createWebsocket();

        CustomWebSocket.sendData(new WebSocketData<>(WSOpcodes.SERVER_STARTED.getOpcode(), null));
        CustomWebSocket.sendData(new WebSocketData<>(WSOpcodes.GET_ALLOWED_MEMBERS.getOpcode(), null));
    }
}
