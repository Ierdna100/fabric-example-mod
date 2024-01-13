package college.andrei.websocket;

import college.andrei.CollegeMod;

public class GlobalData {
    public static void handle() {
        String seed = CollegeMod.server.getSaveProperties().getGeneratorOptions().getSeed() + "";
        int maxPlayers = CollegeMod.server.getMaxPlayerCount();
        int currentPlayers = CollegeMod.server.getCurrentPlayerCount();
        String version = CollegeMod.server.getVersion();
        String MOTD = CollegeMod.server.getServerMotd();

        CustomWebSocket.sendData(new GlobalData.AsJson(seed, maxPlayers, currentPlayers, version, MOTD).toJsonable());
    }

    public static class AsJson {
        public final String ip = "winter2024.andreimc.net";
        public final String seed;
        public final int maxPlayers;
        public final int currentPlayers;
        public final String version;
        public final String MOTD;

        public AsJson(String seed, int maxPlayers, int currentPlayers, String version, String MOTD) {
            this.seed = seed;
            this.maxPlayers = maxPlayers;
            this.currentPlayers = currentPlayers;
            this.version = version;
            this.MOTD = MOTD;
        }

        public WebSocketData<GlobalData.AsJson> toJsonable() {
            return new WebSocketData<>(WSOpcodes.GLOBAL_DATA.getOpcode(), this);
        }
    }
}
