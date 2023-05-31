package com.yooondooo.WORDLY;

public class HandlerTest {
    private String s1;
    private String s2;
    public HandlerTest(String s1, String s2){
        this.s1 = s1;
        this.s2 = s2;
    }

    public boolean check(){
        String[] wordForCheck = s1.split("");
        String[] wordly = s2.split("");
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (wordForCheck[i].equals(wordly[i].toLowerCase())) {
                wordForCheck[i] = "*";
                wordly[i] = "-";
                count++;
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (wordly[i].toLowerCase().equals(wordForCheck[j])) {
                    wordForCheck[j] = "*";
                    break;
                }
            }
        }
        return count == 5;
    }
}
