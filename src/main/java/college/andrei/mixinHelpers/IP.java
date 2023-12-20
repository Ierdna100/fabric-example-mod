package college.andrei.mixinHelpers;

import java.net.SocketAddress;

public class IP {
    int _1;
    int _2;
    int _3;
    int _4;

    public IP(SocketAddress socketAddress) {
        this(socketAddress.toString().substring(socketAddress.toString().indexOf('/') + 1, socketAddress.toString().indexOf(':')));
    }

    public IP(String ip) {
        int firstSeparator = ip.indexOf(".");
        int secondSeparator = ip.indexOf(".", firstSeparator + 1);
        int thirdSeparator = ip.indexOf(".", secondSeparator + 1);

        this._1 = Integer.parseInt(ip.substring(0, firstSeparator));
        this._2 = Integer.parseInt(ip.substring(firstSeparator + 1, secondSeparator));
        this._3 = Integer.parseInt(ip.substring(secondSeparator + 1, thirdSeparator));
        this._4 = Integer.parseInt(ip.substring(thirdSeparator + 1));
    }

    public IP(int _1, int _2, int _3, int _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    public String asString() {
        return String.format("%d.%d.%d.%d", _1, _2, _3, _4);
    }

    public int asInt() {
        return _1 << (8 * 3) | _2 << (8 * 2) | _3 << 8 | _4;
    }
}
