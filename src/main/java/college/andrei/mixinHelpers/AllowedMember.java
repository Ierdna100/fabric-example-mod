package college.andrei.mixinHelpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllowedMember {
    private String uuid;
    private String[] allowedIps;

    public AllowedMember(String uuid, String[] allowedIps) {
        this.uuid = uuid;
        this.allowedIps = allowedIps;
    }

    public String getUuid() {
        return uuid;
    }

    public String[] getAllowedIps() {
        return allowedIps;
    }

    @Override
    public String toString() {
        return "{name: " + uuid + ", allowedIps: " + Arrays.toString(this.allowedIps) + "}";
    }
}
