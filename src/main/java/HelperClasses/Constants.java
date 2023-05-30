package HelperClasses;


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
        public static final int Crab = 0;
        public static final int Octopus = 1;

        public static final int Idle = 0;
        public static final int Run = 1;
        public static final int Attack = 2;
        public static final int Hit = 3;
        public static final int Dead = 4;

        public static final int CrabWidth = 72;
        public static final int CrabHeight = 32;
        public static final int CrabWidthScaled = (int) (CrabWidth * Scale);
        public static final int CrabHeightScaled = (int) (CrabHeight * Scale);

        public static final int CrabOffsetX = (int) (26 * Scale);
        public static final int CrabOffsetY = (int) (9 * Scale);

        public static final int OctopusWidth = 32;
        public static final int OctopusHeight = 32;
        public static final int OctopusWidthScaled = (int) (OctopusWidth * Scale * 1.85);
        public static final int OctopusHeightScaled = (int) (OctopusHeight * Scale * 1.85);

        public static final int OctopusOffsetX = (int) (16 * Scale);
        public static final int OctopusOffsetY = (int) (14 * Scale);

        public static int AmountOfFrames(int enemyType, int enemyAction) {
            switch (enemyType) {
                case Crab:
                    switch (enemyAction) {
                        case Run:
                            return 6;
                        case Dead:
                            return 5;
                        case Idle:
                            return 9;
                        case Attack:
                            return 7;
                        case Hit:
                            return 4;
                        default:
                            return 1;
                    }
                case Octopus:
                    switch (enemyAction) {
                        case Attack:
                        case Dead:
                            return 7;
                        case Hit:
                            return 6;
                        case Run:
                        case Idle:
                            return 4;
                        default:
                            return 1;
                    }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType) {
            switch (enemyType) {
                case Crab:
                    return 20;
                case Octopus:
                    return 30;
                default:
                    return 1;
            }
        }

        public static int GetAmountOfDamage(int enemyType) {
            switch (enemyType) {
                case Crab:
                    return 15;
                case Octopus:
                    return 30;
                default:
                    return 0;
            }
        }
    }

    public static class Objects {
        public static final int Money = 0;
        public static final int Spike = 1;

        public static final int Value = 1;
        public static final int MoneySize = 16;
        public static final int MoneySizeScaled = (int) (MoneySize * Scale);

        public static final int SpikeWidth = 32;
        public static final int SpikeHeight = 32;
        public static final int SpikeWidthScaled = (int) (SpikeWidth * Scale);
        public static final int SpikeHeightScaled = (int) (SpikeHeight * Scale);

        public static int AmountOfFrames(int objectType) {
            switch (objectType) {
                case Money:
                    return 6;
                case Spike:
                default:
                    return 1;
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

        public static class StatusBar {
            public static final int StatusBarWidth = (int) (192 * Scale);
            public static final int StatusBarHeight = (int) (60 * Scale);
            public static final int StatusBarX = (int) (10 * Scale);
            public static final int StatusBarY = (int) (30 * Scale);

            public static final int HealthBarWidth = (int) (152 * Scale);
            public static final int HealthBarHeight = (int) (20 * Scale);
            public static final int HealthBarX = (int) (34 * Scale);
            public static final int HealthBarY = (int) (6 * Scale);

            public static final int MoneyBarWidth = (int) (158 * Scale);
            public static final int MoneyBarHeight = (int) (15 * Scale);
            public static final int MoneyBarX = (int) (28 * Scale);
            public static final int MoneyBarY = (int) (38 * Scale);
        }
    }

    public static class Directions {
        public static final int Left = 0;
        public static final int Right = 1;
    }
}