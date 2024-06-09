package college.andrei.mixinHelpers.dto;

import college.andrei.websocket.WSOpcodes;
import college.andrei.websocket.WebSocketData;

public class Deaths {
    public record Death(String key, String killed) {
        public WebSocketData<Death> toJsonable() {
            return new WebSocketData<>(WSOpcodes.DEATH.getOpcode(), this);
        }
    }

    public record DeathByEntity(String key, String killed, String killer, boolean killerIsPlayer, String killerType) {
        public WebSocketData<DeathByEntity> toJsonable() {
            return new WebSocketData<>(WSOpcodes.DEATH_BY_ENTITY.getOpcode(), this);
        }
    }

    // Possible bug: Can killer *not* be a player?
    public record DeathWithItem(String key, String killed, String killer, String itemType, String itemName) {
        public WebSocketData<DeathWithItem> toJsonable() {
            return new WebSocketData<>(WSOpcodes.DEATH_BY_ENTITY_WITH_ITEM.getOpcode(), this);
        }
    }
}
