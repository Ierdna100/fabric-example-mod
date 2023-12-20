package college.andrei.mixin;

import college.andrei.CollegeMod;
import college.andrei.bot.Bot;
import college.andrei.bot.HTTPEndpoints;
import college.andrei.mixinHelpers.AllowedMember;
import college.andrei.mixinHelpers.IP;
import college.andrei.mixinHelpers.LoginHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        String playerIp = player.getIp();
        String playerDisplayName = player.getDisplayName().getString();
        String Uuid = player.getUuidAsString();
        double spawnPosX = player.getX();
        double spawnPosY = player.getY();
        double spawnPosZ = player.getZ();
        String spawnDimension = player.getSpawnPointDimension().getValue().toString();

        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("ip", playerIp));
        postParams.add(new BasicNameValuePair("displayName", playerDisplayName));
        postParams.add(new BasicNameValuePair("uuid", Uuid));
        postParams.add(new BasicNameValuePair("spawnPosX", "" + spawnPosX));
        postParams.add(new BasicNameValuePair("spawnPosY", "" + spawnPosY));
        postParams.add(new BasicNameValuePair("spawnPosZ", "" + spawnPosZ));
        postParams.add(new BasicNameValuePair("spawnDimension", spawnDimension));

        Bot.sendPostInteraction(postParams, HTTPEndpoints.PLAYER_JOINED);
    }

    @Inject(method = "checkCanJoin", at = @At("HEAD"), cancellable = true)
    private void checkCanJoin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        boolean allowLogin = false;

        IP ip = new IP(address);

        Bot.sendPostInteraction(HTTPEndpoints.PLAYER_ATTEMPTED_LOGIN);

        for (AllowedMember allowedMember : LoginHelper.allowedMembers) {
            if (!allowedMember.getName().equals(profile.getName())) {
                continue;
            }

            // If user was found in allowedMembers by name
            boolean foundAllowedIp = false;
            for (IP allowedIp : allowedMember.getAllowedIps()) {
                if (allowedIp.asInt() != ip.asInt()) {
                    continue;
                }
                foundAllowedIp = true;
            }

            // If no valid IPs were found looking at allowedMember.allowedIps
            if (!foundAllowedIp) {
                CollegeMod.LOGGER.info(String.format("Player with name <%s> attempted login with IP [%s], though none valid were found for their username. Denying login.",
                        profile.getName(), ip.asString()));
                Bot.sendPostInteraction(HTTPEndpoints.PLAYER_FAILED_LOGIN);
                cir.setReturnValue(Text.literal("IP did not match any existing IPs previously used. Confirm new IP via the Discord DM sent to you."));
                cir.cancel();
                return;
            }

            allowLogin = true;
        }

        // If no users in allowedMembers were found by name
        if (!allowLogin) {
            CollegeMod.LOGGER.info(String.format("Profile name <%s> was not found as a valid whitelisted username. Denying login", profile.getName()));
            Bot.sendPostInteraction(HTTPEndpoints.PLAYER_FAILED_LOGIN);
            cir.setReturnValue(Text.literal("Your username is not whitelisted! Use /help on discord and follow the instructions provided!"));
            cir.cancel();
        }

        // Proceed to rest of login checks after return
    }
}
