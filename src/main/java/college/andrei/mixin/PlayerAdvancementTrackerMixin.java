package college.andrei.mixin;

import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import net.minecraft.advancement.*;
import net.minecraft.text.Text;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

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

        String name = Advancement.getNameFromIdentity(advancement).getString();

        // If mission is progressible
        if (progressText != null) {
            // If advancement is not even started (or revoked)
            if (advancementProgress.getProgressBarPercentage() == 0.0) {
                return;
            }

            List<NameValuePair> postParams = new ArrayList<>();

            String progress = progressText.getString();

            postParams.add(new BasicNameValuePair("name", name));
            postParams.add(new BasicNameValuePair("isDone", "" + isDone));
            postParams.add(new BasicNameValuePair("progress", progress));

            Bot.sendPostInteraction(postParams, HTTPEndpoints.ADVANCEMENT_PROGRESSIBLE);
        }
        // If mission is non-progressible (also referred to as boolean)
        else {
            // If advancement is not done (or revoked)
            if (!isDone) {
                return;
            }

            List<NameValuePair> postParams = new ArrayList<>();

            postParams.add(new BasicNameValuePair("name", name));

            Bot.sendPostInteraction(postParams, HTTPEndpoints.ADVANCEMENT_BOOLEAN);
        }
    }
}
