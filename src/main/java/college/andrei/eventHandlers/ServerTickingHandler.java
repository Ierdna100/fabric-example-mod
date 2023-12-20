package college.andrei.eventHandlers;

import college.andrei.CollegeMod;
import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.server.MinecraftServer;

public class ServerTickingHandler {
    private static int ticksSinceUpdate = 0;

    public static void onTick(MinecraftServer server) {
        if (ticksSinceUpdate++ > CollegeMod.SECONDS_BETWEEN_HEARTBEAT * 20) {
            Bot.sendPostInteraction(HTTPEndpoints.HEARTBEAT);
            ticksSinceUpdate = 0;
        }
    }
}
