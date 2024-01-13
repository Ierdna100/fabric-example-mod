package college.andrei.mixinHelpers;

import college.andrei.CollegeMod;

public class LoginHelper {
    public static AllowedMember[] allowedMembers = {};

    public static void updateAllowedMembers(AllowedMember[] allowedMembers) {
        LoginHelper.allowedMembers = allowedMembers;
        CollegeMod.LOGGER.info("Successfully received auth data!");
    }
}
