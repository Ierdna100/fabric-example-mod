package college.andrei.mixin;

import college.andrei.mixinHelpers.dto.DeathMessageReturn;
import college.andrei.websocket.CustomWebSocket;
import college.andrei.mixinHelpers.dto.Deaths;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.*;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMessageMixin {
    @Inject(method = "onDeath", at = @At(value = "HEAD"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity killed = (ServerPlayerEntity) (Object) this;
        assert killed != null;

        DeathMessageReturn deathMessageData = getDeathData(killed);
        String key = deathMessageData.key();
        String killedUuid = killed.getUuidAsString();

        if (deathMessageData.killer() == null) {
            CustomWebSocket.sendData(new Deaths.Death(key, killedUuid).toJsonable());
        } else {
            String killerUuid = deathMessageData.killer().getUuidAsString();
            if (deathMessageData.killItem() == null) {
                String entityType = EntityType.getId(deathMessageData.killer().getType()).toString();
                boolean killerIsPlayer = deathMessageData.killer().isPlayer();
                CustomWebSocket.sendData(new Deaths.DeathByEntity(key, killedUuid, killerUuid, killerIsPlayer, entityType).toJsonable());
            } else {
                String itemType = deathMessageData.killItem().getTranslationKey();
                String itemName = deathMessageData.killItem().getName().getString();
                CustomWebSocket.sendData(new Deaths.DeathWithItem(key, killedUuid, killerUuid, itemType, itemName).toJsonable());
            }
        }
    }

    private DeathMessageReturn getDeathData(LivingEntity killed) {
        DamageTracker damageTracker = killed.getDamageTracker();
        List<DamageRecord> recentDamage = ((DamageTrackerMixin) damageTracker).getRecentDamage();

        if (recentDamage.isEmpty()) {
            return new DeathMessageReturn("death.attack.generic", null, null);
        } else {
            DamageSource lastDamageSource = recentDamage.get(recentDamage.size() - 1).damageSource();
            DamageRecord biggestFallDamage = ((DamageTrackerMixin) damageTracker).getBiggestFall();
            DeathMessageType deathMessageType = lastDamageSource.getType().deathMessageType();

            if (deathMessageType == DeathMessageType.FALL_VARIANTS && biggestFallDamage != null) {
                return getFallDeathKey(biggestFallDamage, lastDamageSource.getAttacker());
            } else if (deathMessageType == DeathMessageType.INTENTIONAL_GAME_DESIGN) {
                return new DeathMessageReturn("death.attack." + lastDamageSource.getName() + ".message", null, null);
            } else {
                return getDeathKey(lastDamageSource, killed);
            }
        }
    }

    private DeathMessageReturn getFallDeathKey(DamageRecord fallDamageRecord, @Nullable Entity lastDamageSourceAttacker) {
        DamageSource fallDamageSource = fallDamageRecord.damageSource();
        if (!fallDamageSource.isIn(DamageTypeTags.IS_FALL) && !fallDamageSource.isIn(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL)) {
            Text lastDamageName = getDisplayName(lastDamageSourceAttacker);
            Entity fallDamageEntity = fallDamageSource.getAttacker();
            Text fallDamageEntName = getDisplayName(fallDamageEntity);
            if (fallDamageEntName != null && !fallDamageEntName.equals(lastDamageName)) {
                return attackWithOrWithoutItem(fallDamageEntity, "death.fell.assist.item", "death.fell.assist");
            } else {
                if (lastDamageName != null) {
                    return attackWithOrWithoutItem(lastDamageSourceAttacker, "death.fell.finish.item", "death.fell.finish");
                } else {
                    return new DeathMessageReturn("death.fell.killer", null, null);
                }
            }
        } else {
            FallLocation fallLocation = Objects.requireNonNullElse(fallDamageRecord.fallLocation(), FallLocation.GENERIC);
            return new DeathMessageReturn(fallLocation.getDeathMessageKey(), null, null);
        }
    }

    private DeathMessageReturn attackWithOrWithoutItem(Entity attacker, String itemDeathKey, String normalDeathKey) {
        ItemStack item = ItemStack.EMPTY;
        if (attacker instanceof LivingEntity livingEntity) {
            item = livingEntity.getMainHandStack();
        }

        return (!item.isEmpty() && item.hasCustomName())
                ? new DeathMessageReturn(itemDeathKey, attacker, item)
                : new DeathMessageReturn(normalDeathKey, attacker, null) ;
    }

    @Nullable
    private static Text getDisplayName(@Nullable Entity entity) {
        return entity == null ? null : entity.getDisplayName();
    }

    private DeathMessageReturn getDeathKey(DamageSource self, LivingEntity killed) {
        String key = "death.attack." + self.getType().msgId();
        if (self.getAttacker() == null && self.getSource() == null) {
            LivingEntity likelyKiller = killed.getPrimeAdversary();
            if (likelyKiller != null) {
                return new DeathMessageReturn(key + ".player", likelyKiller, null);
            } else {
                return new DeathMessageReturn(key, null, null);
            }
        } else {
            Entity killer = self.getAttacker();
            ItemStack killItem = ItemStack.EMPTY;
            if (killer instanceof LivingEntity livingEntity) {
                killItem = livingEntity.getMainHandStack();
            }

            if (!killItem.isEmpty() && killItem.hasCustomName()) {
                // `killer` in the source is not actually directly that. It gets the name of this.source if
                // this.attacker is null? Check later
                return new DeathMessageReturn(key + ".item", killer, killItem);
            } else {
                return new DeathMessageReturn(key, killer, null);
            }
        }
    }
}
