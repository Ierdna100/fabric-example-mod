package college.andrei.mixin;

import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMessageMixin {
    @Inject(method = "onDeath", at = @At(value = "RETURN"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity killed = (ServerPlayerEntity) (Object) this;
        assert killed != null;

        Entity killer = damageSource.getAttacker();

        if (killer == null) {
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("killed", killed.getUuidAsString()));
            postParams.add(new BasicNameValuePair("msg", killed.getDamageTracker().getDeathMessage().getString()));

            Bot.sendPostInteraction(postParams, HTTPEndpoints.DEATH);
            return;
        }

        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("killer", killer.getUuidAsString()));
        postParams.add(new BasicNameValuePair("killed", killed.getUuidAsString()));
        postParams.add(new BasicNameValuePair("isByPlayer", "" + killer.isPlayer()));
        postParams.add(new BasicNameValuePair("msg", killed.getDamageTracker().getDeathMessage().getString()));

        Bot.sendPostInteraction(postParams, HTTPEndpoints.DEATH_BY_ENTITY);
    }
}
