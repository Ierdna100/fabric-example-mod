package college.andrei.mixinHelpers.dto;

import college.andrei.websocket.WSOpcodes;
import college.andrei.websocket.WebSocketData;

public class Deaths {
    public static class Death {
        public final String killed;
        public final String msg;

        public Death(String killed, String msg) {
            this.killed = killed;
            this.msg = msg;
        }

        public WebSocketData<Death> toJsonable() {
            return new WebSocketData<>(WSOpcodes.DEATH.getOpcode(), this);
        }
    }

    public static class DeathByEntity {
        public final String killer;
        public final String killed;
        public final boolean isByPlayer;
        public final String msg;

        public DeathByEntity(String killer, String killed, boolean isByPlayer, String msg) {
            this.killer = killer;
            this.killed = killed;
            this.isByPlayer = isByPlayer;
            this.msg = msg;
        }

        public WebSocketData<DeathByEntity> toJsonable() {
            return new WebSocketData<>(WSOpcodes.DEATH_BY_ENTITY.getOpcode(), this);
        }
    }
}
