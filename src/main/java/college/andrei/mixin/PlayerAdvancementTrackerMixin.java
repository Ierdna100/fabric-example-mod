package college.andrei.mixin;

import college.andrei.websocket.CustomWebSocket;
import college.andrei.mixinHelpers.dto.CustomAdvancement;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
    @Inject(method = "grantCriterion", at = @At("TAIL"))
    private void onEarnedAdvancement(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        String advancementPath = advancement.id().getPath();

        // If is a recipe
        if (advancementPath.substring(0, advancementPath.indexOf('/')).equals("recipes")) {
            return;
        }

        PlayerAdvancementTracker playerAdvancementTracker = (PlayerAdvancementTracker) (Object) this;
        AdvancementProgress advancementProgress = playerAdvancementTracker.getProgress(advancement);

        boolean isDone = advancementProgress.isDone();
        Text progressText = advancementProgress.getProgressBarFraction();
        String uuid = ((PlayerAdvancementTrackerAccessor) playerAdvancementTracker).getOwner().getUuidAsString();

        String name = Advancement.getNameFromIdentity(advancement).getString();

        // If mission is progressible
        if (progressText != null) {
            // If advancement is not even started (or revoked)
            if (advancementProgress.getProgressBarPercentage() == 0.0f) {
                return;
            }

            CustomWebSocket.sendData(new CustomAdvancement.Progressible(name, isDone, progressText.getString(), uuid).toJsonable());
        }
        // If mission is non-progressible (also referred to as boolean)
        else {
            // If advancement is not done (or revoked)
            if (!isDone) {
                return;
            }

            CustomWebSocket.sendData(new CustomAdvancement.Boolean(name, uuid).toJsonable());
        }
    }
}
