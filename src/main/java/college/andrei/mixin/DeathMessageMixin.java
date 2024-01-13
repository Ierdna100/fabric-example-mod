package college.andrei.mixin;

import college.andrei.websocket.CustomWebSocket;
import college.andrei.mixinHelpers.dto.Deaths;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMessageMixin {
    @Inject(method = "onDeath", at = @At(value = "RETURN"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity killed = (ServerPlayerEntity) (Object) this;
        assert killed != null;

        Entity killer = damageSource.getAttacker();

        String paramKilled = killed.getUuidAsString();
        String msg = killed.getDamageTracker().getDeathMessage().getString();
        // get proper death message

        if (killer == null) {
            CustomWebSocket.sendData(new Deaths.Death(paramKilled, msg).toJsonable());
            return;
        }

        String paramKiller = killer.getUuidAsString();
        // Add killer type
        boolean isByPlayer = killer.isPlayer();

        CustomWebSocket.sendData(new Deaths.DeathByEntity(paramKiller, paramKilled, isByPlayer, msg).toJsonable());
    }
}
