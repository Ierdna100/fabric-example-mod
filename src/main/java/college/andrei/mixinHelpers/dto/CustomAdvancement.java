package college.andrei.mixinHelpers.dto;

import college.andrei.websocket.WSOpcodes;
import college.andrei.websocket.WebSocketData;

public class CustomAdvancement {
    public static class Boolean {
        public final String name;
        public final String uuid;

        public Boolean(String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        public WebSocketData<CustomAdvancement.Boolean> toJsonable() {
            return new WebSocketData<>(WSOpcodes.ADVANCEMENT_BOOLEAN.getOpcode(), this);
        }
    }

    public static class Progressible {
        public final String name;
        public final boolean isDone;
        public final String progress;
        public final String uuid;

        public Progressible(String name, boolean isDone, String progress, String uuid) {
            this.name = name;
            this.isDone = isDone;
            this.progress = progress;
            this.uuid = uuid;
        }

        public WebSocketData<CustomAdvancement.Progressible> toJsonable() {
            return new WebSocketData<>(WSOpcodes.ADVANCEMENT_PROGRESSIBLE.getOpcode(), this);
        }
    }
}
