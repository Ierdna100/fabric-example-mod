package college.andrei.mixin;

import college.andrei.websocket.CustomWebSocket;
import college.andrei.mixinHelpers.dto.PlayerConnectingData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void onPlayerDisconnected(Text reason, CallbackInfo ci) {
        ServerPlayNetworkHandler playNetworkHandler = (ServerPlayNetworkHandler) (Object) this;

        CustomWebSocket.sendData(new PlayerConnectingData.Disconnect(
                playNetworkHandler.player.getUuidAsString(),
                reason.getString())
                .toJsonable());
    }
}
