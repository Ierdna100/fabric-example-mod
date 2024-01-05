package college.andrei.mixinHelpers.dto;

import college.andrei.bot.WSOpcodes;
import college.andrei.bot.WebSocketData;

public class TickData {
    private final long millisecondsBehind;
    private final long ticksBehind;

    public TickData(long millisecondsBehind, long ticksBehind) {
        this.millisecondsBehind = millisecondsBehind;
        this.ticksBehind = ticksBehind;
    }

    public WebSocketData<TickData> toJsonable() {
        return new WebSocketData<>(WSOpcodes.SERVER_OVERLOADED.getOpcode(), this);
    }
}
