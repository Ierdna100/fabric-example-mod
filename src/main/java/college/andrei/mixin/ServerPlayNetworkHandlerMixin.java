package college.andrei.mixin;

import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void onPlayerDisconnected(Text reason, CallbackInfo ci) {
        ServerPlayNetworkHandler playNetworkHandler = (ServerPlayNetworkHandler) (Object) this;

        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("player", playNetworkHandler.player.getUuidAsString()));
        postParams.add(new BasicNameValuePair("reason", reason.getString()));

        Bot.sendPostInteraction(postParams, HTTPEndpoints.PLAYER_LEFT);
    }
}
