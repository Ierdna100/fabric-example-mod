package college.andrei.eventHandlers;

import college.andrei.CollegeMod;
import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.server.MinecraftServer;

public class ServerStartedHandler {
    public static void onServerStarted(MinecraftServer server) {
        CollegeMod.server = server;

        Bot.sendPostInteraction(HTTPEndpoints.SERVER_STARTED);
        Bot.sendGetInteraction(HTTPEndpoints.GET_ALLOWED_MEMBERS);
    }
}
