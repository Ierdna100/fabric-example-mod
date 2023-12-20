package college.andrei.eventHandlers;

import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.server.MinecraftServer;

public class ServerStoppingHandler {
    public static void onServerStopping(MinecraftServer server) {
        Bot.sendPostInteraction(HTTPEndpoints.SERVER_STOP_WARNING);
    }

    public static void onServerStop(MinecraftServer server) {
        Bot.sendPostInteraction(HTTPEndpoints.SERVER_STOPPED);
    }
}
