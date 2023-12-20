package college.andrei.bot;

public enum HTTPEndpoints {
    MESSAGE(""), // OK
    DEATH(""), // OK
    DEATH_BY_ENTITY(""), // OK
    SERVER_STARTED(""), // OK
    SERVER_STOP_WARNING(""), // OK
    SERVER_STOPPED(""), // OK
    PLAYER_LEFT(""), // OK
    PLAYER_JOINED(""), // OK
    PLAYER_ATTEMPTED_LOGIN(""), // OK
    PLAYER_FAILED_LOGIN(""),
    ADVANCEMENT_BOOLEAN(""), // OK
    ADVANCEMENT_PROGRESSIBLE(""), // OK
    SERVER_OVERLOADED(""), // OK
    EXCEPTION(""), // NOT IMPLEMENTING
    SERVER_STARTING(""), // OK
    HEARTBEAT(""), // OK
    GET_ALLOWED_MEMBERS("allowedMembers"); // OK

    private final String endpoint;

    public String getEndpoint() {
        return this.endpoint;
    }

    HTTPEndpoints(String endpoint) {
        this.endpoint = endpoint;
    }
}
