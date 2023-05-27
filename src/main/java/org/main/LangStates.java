package org.main;

public enum LangStates { //Для переключения языков
    ENGLISH, RUSSIAN;

    public static LangStates langState = RUSSIAN;

    public static void SetLangState(LangStates langState) {
        LangStates.langState = langState;
    }

}
