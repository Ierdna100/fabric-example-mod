package college.andrei;

import college.andrei.bot.CustomWebSocket;
import college.andrei.eventHandlers.ServerStoppingHandler;
import net.fabricmc.api.DedicatedServerModInitializer;
import college.andrei.eventHandlers.ServerStartedHandler;
import college.andrei.eventHandlers.ChatMessageHandler;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.*;

import java.net.URI;

/*
In case of bot lack of response, send directly via webhook, then immediately stop the server from attempting to heartbeat or do else
 */
public class CollegeMod implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("collegemod");
	public static final String BACKUP_WEBHOOK_URI = "https://discord.com/api/webhooks/1192680060281425970/AVLb0VpWpbusihw-6ApGd218z2ABEHLO6oAYL7u5-Fh0gzXWgsMG2Kx6ew5r5P1Rh0rK";
	public static MinecraftServer server;

	@Override
	public void onInitializeServer() {
		ServerLifecycleEvents.SERVER_STARTED.register(ServerStartedHandler::onServerStarted);
		ServerMessageEvents.CHAT_MESSAGE.register(ChatMessageHandler::onChatMessage);
		ServerLifecycleEvents.SERVER_STOPPING.register(ServerStoppingHandler::onServerStopping);
		ServerLifecycleEvents.SERVER_STOPPED.register(ServerStoppingHandler::onServerStop);
	}

	public static void createWebsocket() {
		ClientManager client = ClientManager.createClient();
		try {
			URI uri = new URI("ws://localhost:5000/");
			client.connectToServer(CustomWebSocket.class, uri);
		} catch (Exception ignored) {}
	}
}
