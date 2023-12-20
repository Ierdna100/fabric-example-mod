package college.andrei;

import college.andrei.eventHandlers.ServerStoppingHandler;
import college.andrei.eventHandlers.ServerTickingHandler;
import net.fabricmc.api.DedicatedServerModInitializer;
import college.andrei.eventHandlers.ServerStartedHandler;
import college.andrei.eventHandlers.ChatMessageHandler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
LEFT TODO:
/allow-idling
/warn-day

In case of bot lack of response, send directly via webhook, then immediately stop the server from attempting to heartbeat or do else
 */
public class CollegeMod implements DedicatedServerModInitializer {
	public static final double HTTP_TIMEOUT_SECONDS = 1;
    public static final Logger LOGGER = LoggerFactory.getLogger("collegemod");
	public static final int SECONDS_BETWEEN_HEARTBEAT = 10;
	public static CloseableHttpClient client;
	public static MinecraftServer server;

	@Override
	public void onInitializeServer() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout((int) (HTTP_TIMEOUT_SECONDS * 1000))
				.setConnectionRequestTimeout((int) (HTTP_TIMEOUT_SECONDS * 1000))
				.setSocketTimeout((int) (HTTP_TIMEOUT_SECONDS * 1000)).build();

		CollegeMod.client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

		ServerLifecycleEvents.SERVER_STARTED.register(ServerStartedHandler::onServerStarted);
		ServerMessageEvents.CHAT_MESSAGE.register(ChatMessageHandler::onChatMessage);
		ServerLifecycleEvents.SERVER_STOPPING.register(ServerStoppingHandler::onServerStopping);
		ServerLifecycleEvents.SERVER_STOPPED.register(ServerStoppingHandler::onServerStop);
		ServerTickEvents.START_SERVER_TICK.register(ServerTickingHandler::onTick);
	}
}
