package org.game.game;

public class ErrorCatcher {

    public static void cellCreationFailure() {
        System.err.println("Failed to create new cell.");
        System.exit(-1);
    }

    public static void shiftFailure() {
        System.err.println("Shift failure");
        System.exit(-1);
    }

}
