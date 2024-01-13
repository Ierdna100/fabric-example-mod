package college.andrei.eventHandlers;

import college.andrei.CollegeMod;
import college.andrei.websocket.CustomWebSocket;
import college.andrei.websocket.WSOpcodes;
import college.andrei.websocket.WebSocketData;
import net.minecraft.server.MinecraftServer;

public class ServerStartedHandler {
    public static void onServerStarted(MinecraftServer server) {
        CollegeMod.server = server;

        CollegeMod.createWebsocket();

        CustomWebSocket.sendData(new WebSocketData<>(WSOpcodes.SERVER_STARTED.getOpcode(), null));
        CustomWebSocket.sendData(new WebSocketData<>(WSOpcodes.GET_ALLOWED_MEMBERS.getOpcode(), null));
    }
}
