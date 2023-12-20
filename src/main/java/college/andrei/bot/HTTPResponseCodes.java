package college.andrei.bot;

public enum HTTPResponseCodes {
    OK(200),
    NEW_MEMBER_DATA_AVAILABLE(299);

    private final int responseCode;

    HTTPResponseCodes(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
