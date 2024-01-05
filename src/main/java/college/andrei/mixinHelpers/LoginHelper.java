package college.andrei.mixinHelpers;

import college.andrei.CollegeMod;
import org.apache.commons.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginHelper {
    public static AllowedMember[] allowedMembers = {};

    public static void addNewMembers(AllowedMember[] allowedMembers) {
        int newLength = LoginHelper.allowedMembers.length;

        CollegeMod.LOGGER.info("1: " + Arrays.toString(LoginHelper.allowedMembers));
        for (AllowedMember allowedMember1 : allowedMembers) {
            boolean exists = false;
            for (AllowedMember allowedMember2 : LoginHelper.allowedMembers) {
                if (allowedMember1.getUuid().equals(allowedMember2.getUuid())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                newLength++;
            }
        }

        AllowedMember[] temp = new AllowedMember[newLength];

        for (int i = 0; i < LoginHelper.allowedMembers.length; i++) {
            temp[i] = LoginHelper.allowedMembers[i];
        }

        int nextIdx = LoginHelper.allowedMembers.length;
        CollegeMod.LOGGER.info("2: " + Arrays.toString(temp));

        for (AllowedMember allowedMember1 : allowedMembers) {
            boolean exists = false;
            for (AllowedMember allowedMember2 : LoginHelper.allowedMembers) {
                if (allowedMember1.getUuid().equals(allowedMember2.getUuid())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                temp[nextIdx++] = allowedMember1;
            }
        }

        CollegeMod.LOGGER.info("3: " + Arrays.toString(temp));
        LoginHelper.allowedMembers = temp;
        CollegeMod.LOGGER.info("NEW IPS: " + Arrays.toString(LoginHelper.allowedMembers));
    }
}
