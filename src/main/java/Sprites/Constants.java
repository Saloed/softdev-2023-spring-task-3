package Sprites;


public class Constants {
    public static class MainCharacter {
        public static final int idle = 0;
        public static final int run = 1;
        public static final int jump = 2;
        public static final int attack = 3;

        public static int AmountOfFrames(int mainCharacterAction) {
            switch (mainCharacterAction) {
                case run:
                    return 5;
                case idle:
                    return 4;
                case attack:
                    return 2;
                case jump:
                default:
                    return 1;
            }
        }
    }

    public static class Directions {
        public static final int left = 0;
        public static final int down = 1;
        public static final int right = 2;
        public static final int up = 3;
    }

}