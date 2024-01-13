package college.andrei.mixin;

import college.andrei.CollegeMod;
import college.andrei.websocket.CustomWebSocket;
import college.andrei.websocket.WSOpcodes;
import college.andrei.mixinHelpers.AllowedMember;
import college.andrei.mixinHelpers.LoginHelper;
import college.andrei.mixinHelpers.dto.PlayerConnectingData;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    // 14570: onPlayerConnect()
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        CustomWebSocket.sendData(new PlayerConnectingData.Success(
                player.getIp(),
                player.getDisplayName().getString(),
                player.getUuidAsString(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getSpawnPointDimension().getValue().toString())
                .toJsonable());
    }

    @Inject(method = "checkCanJoin", at = @At("HEAD"), cancellable = true)
    private void checkCanJoin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        boolean allowLogin = false;

        // Removes leading slash
        String ip = address.toString().substring(address.toString().indexOf('/') + 1, address.toString().indexOf(':'));

        CustomWebSocket.sendData(new PlayerConnectingData.Attempt(
                ip,
                profile.getName(),
                profile.getId().toString())
                .toJsonable());

        for (AllowedMember allowedMember : LoginHelper.allowedMembers) {
            if (!allowedMember.getUuid().equals(profile.getId().toString())) {
                continue;
            }

            // If user was found in allowedMembers by UUID
            boolean foundAllowedIp = false;
            for (String allowedIp : allowedMember.getAllowedIps()) {
                if (!allowedIp.equals(ip)) {
                    continue;
                }
                foundAllowedIp = true;
            }

            // If no valid IPs were found looking at allowedMember.allowedIps
            if (!foundAllowedIp) {
                CollegeMod.LOGGER.info(String.format("Player with name <%s> attempted login with IP [%s], " +
                        "though none valid were found for their username. Denying login.",
                        profile.getName(), ip));
                CustomWebSocket.sendData(new PlayerConnectingData.Fail(
                        profile.getId().toString(),
                        profile.getName(),
                        ip)
                        .toJsonable(WSOpcodes.PLAYER_FAILED_LOGIN_NO_IP));
                cir.setReturnValue(Text.literal("IP did not match any existing IPs previously used. Confirm new IP via the Discord DM sent to you."));
                cir.cancel();
                return;
            }

            allowLogin = true;
        }

        // If no users in allowedMembers were found by name
        if (!allowLogin) {
            CollegeMod.LOGGER.info(String.format("Profile name <%s> was not found as a valid whitelisted username." +
                    " Denying login",
                    profile.getName()));
            CustomWebSocket.sendData(new PlayerConnectingData.Fail(
                    profile.getId().toString(),
                    profile.getName(),
                    ip)
                    .toJsonable(WSOpcodes.PLAYER_FAILED_LOGIN_NO_USER));
            cir.setReturnValue(Text.literal("Your username is not whitelisted!" +
                    " Use /help on discord and follow the instructions provided!"));
            cir.cancel();
        }

        // Proceed to rest of login checks after return
    }
}
