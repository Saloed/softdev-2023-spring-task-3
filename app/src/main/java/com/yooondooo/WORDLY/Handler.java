package com.yooondooo.WORDLY;


import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Handler {
    public boolean checki = false;
    private boolean isChecki = true;
    private Map<String, Button> alfButtons;
    private int height;
    private int lenght;
    private final int countLetters;
    public String word;
    private final PlayScene context;
    private String clueWord;
    private String[] wordly = new String[5];
    private String alf = "абвгдежзийклмнопрстуфхцчшщъыьэюя";
    private final TextView[][] textView;
    private final Resources resources;
    private final List<String> file = new ArrayList<>();
    private final int dictSize = 6963;
    private String noDelete = "";
    private int countColorButtons;
    private final List<String> copyWords = new ArrayList<>();
    private final String[] yellowLetters = new String[5];
    private boolean active = true;
    private String forBut = "";


    public Handler(TextView[][] textView, Map<String, Button> alfButtons, PlayScene context) {
        this.textView = textView;
        this.alfButtons = alfButtons;
        clueWord = ".....";
        height = 0;
        lenght = 0;
        AssetManager assetManager = context.getAssets();
        this.context = context;
        this.resources = new Resources(assetManager, new DisplayMetrics(), new Configuration());
        Random random = new Random();
        countLetters = random.nextInt(dictSize);
        word = ret();
        for (int i = 0; i < 5; i++) {
            yellowLetters[i] = "";
        }
    }

    public void addLetter(String letter) {
        if (lenght < 5 && height < 6 && !checki) {
            textView[height][lenght].setText(letter);
            wordly[lenght] = letter;
            lenght++;
        }
        if (lenght == 5) {
            lenght = 0;
            if (check()) {
                if (isChecki) {
                    dialog(resources.getString(R.string.win), false);
                }
                checki = true;
            }
            wordly = new String[5];
            height++;
        }
        if (height == 6 && !checki) {
            if (isChecki) {
                dialog(resources.getString(R.string.lose), true);
                checki = true;
                isChecki = false;
            }
        }
    }

    private void dialog(String string, boolean bols) {
        String adString = "";
        String messege = resources.getString(R.string.good);
        if (bols) {
            adString += "\n" + resources.getString(R.string.corWord) + word;
            messege = resources.getString(R.string.res);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(string + adString)
                .setCancelable(false)
                .setPositiveButton(messege, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void delete() {
        if (lenght > 0) {
            lenght--;
            textView[height][lenght].setText("");
        }
    }

    public String clue() {
        String clueW = resources.getString(R.string.noneWord);
        if (countColorButtons > 2 && active) {
            clueW = withActive(clueW);
            if (clueW.equals(resources.getString(R.string.noneWord))) {
                clueW = noneActive(clueW);
            }
        } else {
            clueW = noneActive(clueW);
        }
        return clueW;
    }

    private String noneActive(String clueW) {
        for (String line : copyWords) {
            if (line.matches(clueWord) && checkAlf(line) && checkNoDelete(line)) {
                clueW = line;
                break;
            }
        }
        return clueW;
    }

    private String withActive(String clueW) {
        for (String line : copyWords) {
            if (checkForDop(line)) {
                clueW = line;
                break;
            }
        }
        active = false;
        return clueW;
    }

    private boolean checkForDop(String line) {
        String[] let = line.split("");
        boolean checkClue = true;
        for (int i = 0; i < 5; i++) {
            if (!(alf.contains(let[i]) && !noDelete.contains(let[i]))) {
                checkClue = false;
                break;
            }
        }
        return checkClue;
    }

    private boolean checkAlf(String line) {
        boolean checkClue = true;
        Map<String, String> map = new HashMap<>();
        String[] let = line.split("");
        for (int i = 0; i < 5; i++) {
            if (!alf.contains(let[i]) || yellowLetters[i].contains(let[i])) {
                checkClue = false;
                break;
            }
            if (map.get(let[i]) != null && !noDelete.contains(let[i])) {
                checkClue = false;
                break;
            } else {
                map.put(let[i], let[i]);
            }
        }
        return checkClue;
    }

    private boolean checkNoDelete(String line) {
        boolean checkClue = true;
        String[] letters = noDelete.split("");
        for (String letter : letters) {
            if (!line.contains(letter)) {
                checkClue = false;
                break;
            }
        }
        return checkClue;
    }

    public String ret() {
        InputStream inFile;
        String wordQ = "";
        inFile = resources.openRawResource(R.raw.dict_7000);
        BufferedReader br = new BufferedReader(new InputStreamReader(inFile));
        for (int i = 0; i < dictSize; i++) {
            try {
                file.add(br.readLine());
                copyWords.add(file.get(i));
                if (i == countLetters) {
                    wordQ = file.get(i);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return wordQ;
    }

    public boolean check() {
        int countGreen = 0;
        if (correctWord()){
        String[] wordForCheck = word.split("");
        char[] chars = clueWord.toCharArray();
        for (int i = 0; i < 5; i++) {
            if (wordForCheck[i].equals(wordly[i].toLowerCase())) {
                textView[height][i].setBackgroundResource(R.color.green);
                wordForCheck[i] = "*";
                chars[i] = wordly[i].toLowerCase().charAt(0);
                if (!noDelete.contains(wordly[i])) {
                    noDelete += wordly[i].toLowerCase();
                }
                Button button = alfButtons.get(wordly[i]);
                if (button != null){
                    button.setBackgroundColor(resources.getColor(R.color.green));
                    forBut+=wordly[i].toUpperCase();
                }
                wordly[i] = "-";
                countColorButtons++;
                countGreen++;

            } else {
                yellowLetters[i] += wordly[i].toLowerCase();
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (wordly[i].toLowerCase().equals(wordForCheck[j])) {
                    textView[height][i].setBackgroundResource(R.color.yellow);
                    Button button = alfButtons.get(wordly[i].toUpperCase(Locale.ROOT));
                    if (!forBut.contains(wordly[i].toUpperCase()) && button != null ){
                        button.setBackgroundColor(resources.getColor(R.color.YELLOW));
                        forBut+=wordly[i].toLowerCase();
                    }
                    wordForCheck[j] = "*";
                    yellowLetters[i] += wordly[i].toLowerCase();
                    countColorButtons++;
                    if (!noDelete.contains(wordly[i])) {
                        noDelete += wordly[i].toLowerCase();
                    }
                    break;
                } else {
                    wordly[i] = wordly[i].toLowerCase();
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (alf.contains(wordly[i]) && !noDelete.contains(wordly[i])) {
                alf = alf.replace(wordly[i], "");
                Button button = alfButtons.get(wordly[i].toUpperCase(Locale.ROOT));
                if (!forBut.contains(wordly[i]) && button != null ){
                    button.setBackgroundColor(resources.getColor(R.color.grey));
                }
            }
        }
        if (height != 5 && !active) {
            active = true;
        }
        clueWord = String.valueOf(chars);
        }else{
            dialogForUncorretWord();
        }
        return countGreen == 5;
    }
    private boolean correctWord(){
        String wordWorly = "";
        for (int i =0;i < 5;i++){
            wordWorly += wordly[i].toLowerCase();
        }
        System.out.println(wordWorly);
        for (String line: copyWords){
            System.out.println(line);
            if (line.equals(wordWorly)) return true;
        }
        return false;
    }
    private void dialogForUncorretWord(){
        lenght = 5;
        for (int i = 0; i < 5; i ++){
            delete();
        }
        height--;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(resources.getString(R.string.iDontKnowWord))
                .setMessage(resources.getString(R.string.tryAgain))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.tryOne), (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
