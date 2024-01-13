package college.andrei.websocket;

public enum WSOpcodes {
    MESSAGE(0),
    DEATH(1),
    DEATH_BY_ENTITY(2),
    SERVER_STARTED(3),
    SERVER_STOP_WARNING(4),
    SERVER_STOPPED(5),
    PLAYER_LEFT(6),
    PLAYER_JOINED(7),
    PLAYER_ATTEMPTED_LOGIN(8),
    PLAYER_FAILED_LOGIN_NO_IP(9),
    PLAYER_FAILED_LOGIN_NO_USER(10),
    ADVANCEMENT_BOOLEAN(11),
    ADVANCEMENT_PROGRESSIBLE(12),
    SERVER_OVERLOADED(13),
    SERVER_STARTING(14),
    GET_ALLOWED_MEMBERS(15),
    GLOBAL_DATA(16);

    private final int opcode;

    public int getOpcode() {
        return this.opcode;
    }

    WSOpcodes(int opcode) {
        this.opcode = opcode;
    }
}
