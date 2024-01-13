package college.andrei.mixinHelpers.dto;

import college.andrei.websocket.WSOpcodes;
import college.andrei.websocket.WebSocketData;

public class PlayerConnectingData {
    public static class Fail {
        public final String uuid;
        public final String displayName;
        public final String ip;

        public Fail(String uuid, String displayName, String ip) {
            this.uuid = uuid;
            this.displayName = displayName;
            this.ip = ip;
        }

        public WebSocketData<Fail> toJsonable(WSOpcodes specificOpcode) {
            return new WebSocketData<>(specificOpcode.getOpcode(), this);
        }
    }

    public static class Attempt {
        public final String ip;
        public final String displayName;
        public final String uuid;

        public Attempt(String ip, String displayName, String uuid) {
            this.ip = ip;
            this.displayName = displayName;
            this.uuid = uuid;
        }

        public WebSocketData<Attempt> toJsonable() {
            return new WebSocketData<>(WSOpcodes.PLAYER_ATTEMPTED_LOGIN.getOpcode(), this);
        }
    }

    public static class Success {
        public final String ip;
        public final String displayName;
        public final String uuid;
        public final double spawnPosX;
        public final double spawnPosY;
        public final double spawnPosZ;
        public final String spawnDimension;

        public Success(String ip, String displayName, String uuid, double spawnPosX, double spawnPosY, double spawnPosZ, String spawnDimension) {
            this.ip = ip;
            this.displayName = displayName;
            this.uuid = uuid;
            this.spawnPosX = spawnPosX;
            this.spawnPosY = spawnPosY;
            this.spawnPosZ = spawnPosZ;
            this.spawnDimension = spawnDimension;
        }

        public WebSocketData<Success> toJsonable() {
            return new WebSocketData<>(WSOpcodes.PLAYER_JOINED.getOpcode(), this);
        }
    }

    public static class Disconnect {
        public String uuid;
        public String reason;

        public Disconnect(String uuid, String reason) {
            this.uuid = uuid;
            this.reason = reason;
        }

        public WebSocketData<Disconnect> toJsonable() {
            return new WebSocketData<>(WSOpcodes.PLAYER_LEFT.getOpcode(), this);
        }
    }
}
