package college.andrei.bot;

import college.andrei.mixinHelpers.AllowedMember;

public class WebSocketData<T> {
    public int opcode;
    public T data;

    public WebSocketData(int opcode, T data) {
        this.opcode = opcode;
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebSocketData<T>{"
                + "opcode: " + opcode + "}";
    }

    public static class AllowedMemberResponse {
        public int opcode;
        public AllowedMember[] data;
    }
}
