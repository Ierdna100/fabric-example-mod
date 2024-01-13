package college.andrei.eventHandlers;

import college.andrei.websocket.CustomWebSocket;
import college.andrei.websocket.WSOpcodes;
import college.andrei.websocket.WebSocketData;
import net.minecraft.server.MinecraftServer;

public class ServerStoppingHandler {
    public static void onServerStopping(MinecraftServer server) {
        CustomWebSocket.sendData(new WebSocketData<>(WSOpcodes.SERVER_STOP_WARNING.getOpcode(), null));
    }

    public static void onServerStop(MinecraftServer server) {
        CustomWebSocket.sendData(new WebSocketData<>(WSOpcodes.SERVER_STOPPED.getOpcode(), null));
    }
}
