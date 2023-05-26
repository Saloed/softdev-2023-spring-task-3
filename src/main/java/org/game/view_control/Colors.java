package org.game.view_control;

public enum Colors {

    TILE0("transparent"), TILE2("#dacab8"), TILE4("#99928b"),
    TILE8("#dfa16f"), TILE16("#e68f42"), TILE32("#f17e56"),
    TILE64("#e94832"), TILE128("#f6d872"), TILE256("#e5c452"),
    TILE512("#dcb635"), TILE1024("#e8cb29"), TILE2048("#f5ca1f"),
    TILE4096("#f8d801"), TILE8192("#c32c08"), TILE16384("#060100");

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
