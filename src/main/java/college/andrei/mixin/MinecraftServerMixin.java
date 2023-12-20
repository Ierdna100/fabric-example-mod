package college.andrei.mixin;

import college.andrei.CollegeMod;
import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
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
        long tickStartTimeNanos = ((MinecraftServerAccessor) CollegeMod.server).getTickStartTimeNanos();

        long millisecondsBehind = (Util.getMeasuringTimeNano() - tickStartTimeNanos) / TimeHelper.MILLI_IN_NANOS;
        long ticksBehind = millisecondsBehind / CollegeMod.server.getTickManager().getNanosPerTick();

        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("ms", "" + millisecondsBehind));
        postParams.add(new BasicNameValuePair("ticks", "" + ticksBehind));

        Bot.sendPostInteraction(postParams, HTTPEndpoints.SERVER_OVERLOADED);
    }
}
