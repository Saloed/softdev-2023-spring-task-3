package Sprites;


import static GameEngine.Game.Scale;

public class Constants {
    public static final float Gravity = 0.04f * Scale;

    public static class Background {
        public static final int BigCloudsWidth = 155;
        public static final int BigCloudsHeight = 54;
        public static final int SmallCloudsWidth = 82;
        public static final int SmallCloudsHeight = 36;

        public static final int BigCloudsWidthScaled = (int) (BigCloudsWidth * Scale);
        public static final int BigCloudsHeightScaled = (int) (BigCloudsHeight * Scale);
        public static final int SmallCloudsWidthScaled = (int) (SmallCloudsWidth * Scale);
        public static final int SmallCloudsHeightScaled = (int) (SmallCloudsHeight * Scale);
    }

    public static class UI {
        public static class Buttons {
            public static final int ButtonWidth = 222;
            public static final int ButtonHeight = 108;
            public static final int ButtonWidthScaled = (int) ((ButtonWidth - 30) * Scale);
            public static final int ButtonHeightScaled = (int) ((ButtonHeight - 30) * Scale);
        }

        public static class UrmButtons {
            public static final int UrmWidth = 102;
            public static final int UrmHeight = 108;
            public static final int UrmWidthScaled = (int) (UrmWidth * Scale);
            public static final int UrmHeightScaled = (int) (UrmHeight * Scale);
        }
    }

    public static class Enemy {
        public static final int Octopus = 0;

        public static final int Idle = 0;
        public static final int Run = 1;
        public static final int Attack = 2;
        public static final int Hit = 3;
        public static final int Dead = 4;

        public static final int OctopusWidth = 72;
        public static final int OctopusHeight = 32;
        public static final int OctopusWidthScaled = (int) (OctopusWidth * Scale);
        public static final int OctopusHeightScaled = (int) (OctopusHeight * Scale);

        public static final int OctopusOffsetX = (int) (26 * Scale);
        public static final int OctopusOffsetY = (int) (9 * Scale);

        public static int AmountOfFrames(int enemyType, int enemyAction) {
            switch (enemyType) {
                case Octopus:
                    switch (enemyAction) {
                        case Run:
                        case Dead:
                            return 6;
                        case Idle:
                            return 9;
                        case Attack:
                            return 4;
                        case Hit:
                            return 5;
                        default:
                            return 1;
                    }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType) {
            switch (enemyType) {
                case Octopus:
                    return 10;
                default:
                    return 1;
            }
        }

        public static int GetAmountOfDamage(int enemyType) {
            switch (enemyType) {
                case Octopus:
                    return 10;
                default:
                    return 0;
            }
        }
    }

    public static class MainCharacter {
        public static final int Idle = 0;
        public static final int Run = 1;
        public static final int Jump = 2;
        public static final int Attack = 3;
        public static final int Hit = 4;
        public static final int Dead = 5;

        public static int AmountOfFrames(int mainCharacterAction) {
            switch (mainCharacterAction) {
                case Run:
                    return 5;
                case Idle:
                    return 4;
                case Attack:
                case Hit:
                case Dead:
                    return 3;
                case Jump:
                default:
                    return 1;
            }
        }

        public static class Health {
            public static final int StatusBarWidth = (int) (192 * Scale);
            public static final int StatusBarHeight = (int) (30 * Scale);
            public static final int StatusBarX = (int) (10 * Scale);
            public static final int StatusBarY = (int) (30 * Scale);

            public static final int HealthBarWidth = (int) (152 * Scale);
            public static final int HealthBarHeight = (int) (23 * Scale);
            public static final int HealthBarX = (int) (34 * Scale);
            public static final int HealthBarY = (int) (5 * Scale);
        }
    }

    public static class Directions {
        public static final int Left = 0;
        public static final int Right = 1;
    }
}