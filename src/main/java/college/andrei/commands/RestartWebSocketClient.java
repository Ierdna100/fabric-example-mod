package college.andrei.commands;

import college.andrei.CollegeMod;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

public class RestartWebSocketClient {
    public RestartWebSocketClient() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("restartWebsocketConnection").executes(this::callback)
        )));
    }

    private int callback(CommandContext<ServerCommandSource> context) {
        boolean executorIsAuthed = context.getSource().getPlayer().hasPermissionLevel(4);

        if (!executorIsAuthed) {
            context.getSource().sendFeedback(() -> Text.literal("Your permission level is too low!"), true);
            return 1;
        }

        CollegeMod.createWebsocket();
        context.getSource().sendFeedback(() -> Text.literal("Websocket created!"), true);
        return 1;
    }
}
