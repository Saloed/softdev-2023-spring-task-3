package org.main;

public class IncorrectLevelStructure extends IllegalArgumentException {
    public IncorrectLevelStructure(String errorMessage) {
        super(errorMessage);
    }
}
