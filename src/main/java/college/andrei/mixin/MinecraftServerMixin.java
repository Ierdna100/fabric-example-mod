package college.andrei.mixin;

import college.andrei.CollegeMod;
import college.andrei.bot.CustomWebSocket;
import college.andrei.mixinHelpers.dto.TickData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.Util;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    private void onServerOverloaded(CallbackInfo ci) {
        long tickStartTimeNanos = ((MinecraftServerAccessor) CollegeMod.server).getTimeReference();

        long msBehind = Util.getMeasuringTimeMs() - tickStartTimeNanos;
        long ticksBehind = msBehind / 50L;

        CustomWebSocket.sendData(new TickData(msBehind, ticksBehind).toJsonable());
    }
}
