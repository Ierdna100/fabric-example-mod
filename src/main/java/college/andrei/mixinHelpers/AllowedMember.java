package college.andrei.mixinHelpers;

import java.util.Arrays;
import java.util.List;

public class AllowedMember {
    private String name;
    private List<IP> allowedIps;

    public AllowedMember(String name, List<IP> allowedIps) {
        this.name = name;
        this.allowedIps = allowedIps;
    }

    public String getName() {
        return name;
    }

    public List<IP> getAllowedIps() {
        return allowedIps;
    }

    public static class JSON {
        private String name;
        private String[] allowedIps;

        public String getName() {
            return name;
        }

        public String[] getAllowedIps() {
            return allowedIps;
        }

        @Override
        public String toString() {
            return "{name: " + name + ", allowedIps: " + Arrays.toString(allowedIps) + "}";
        }
    }
}
