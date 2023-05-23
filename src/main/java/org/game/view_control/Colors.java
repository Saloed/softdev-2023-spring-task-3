package org.game.view_control;

public enum Colors {

    TILE0(0, "#ccb69f"), TILE2(2, "#dacab8"), TILE4(4, "#99928b"),
    TILE8(8, "#dfa16f"), TILE16(16, "#e68f42"), TILE32(32, "#f17e56"),
    TILE64(64, "#e94832"), TILE128(128, "#f6d872"), TILE256(256, "#e5c452"),
    TILE512(512, "#dcb635"), TILE1024(1024, "#e8cb29"), TILE2048(2048, "#f5ca1f"),
    TILE4096(4096, "#f8d801"), TILE8192(8192, "#c32c08"), TILE16384(16384, "#060100");

    private final int value;
    private final String color;

    Colors(int value, String color) {
        this.value = value;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

}
