package com.yooondooo.WORDLY;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class PlayScene extends AppCompatActivity {
    private Handler handler;

    @SuppressLint({"MissingInflatedId", "SourceLockedOrientationActivity", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_scene);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        handler = new Handler(forText(), forButtons(),this);
        Button buttonDelete = findViewById(R.id.buttonDelete);
//        button5.setBackgroundColor(getResources().getColor(R.color.green));
        clickDelete(buttonDelete);
    }
    private Map<String, Button> forButtons(){
        Map<String, Button> alfButtons = new HashMap<>();
        Integer[] buuttion = new Integer[]{
                R.id.buttonQ, R.id.buttonW, R.id.buttonE, R.id.buttonR, R.id.buttonT,
                R.id.buttonY, R.id.buttonU, R.id.buttonI, R.id.buttonO, R.id.buttonP,
                R.id.button1, R.id.button2, R.id.buttonA, R.id.buttonS, R.id.buttonD,
                R.id.buttonF, R.id.buttonG, R.id.buttonH, R.id.buttonJ, R.id.buttonK,
                R.id.buttonL, R.id.button3, R.id.button4, R.id.buttonZ, R.id.buttonX,
                R.id.buttonC, R.id.buttonV, R.id.buttonB, R.id.buttonN, R.id.buttonM,
                R.id.button5, R.id.button6
        };
        String[] forAlf = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ".split("");
        Button[] buttons = new Button[32];
        for (int i = 0; i < 32; i++){
            buttons[i] = findViewById(buuttion[i]);
            click(buttons[i], forAlf[i]);
            alfButtons.put(forAlf[i],buttons[i]);
        }
        return alfButtons;
    }
    private TextView[][] forText(){
        TextView[][] textView = new TextView[6][5];
        Integer[] texts = new Integer[]{
                R.id.text1_1, R.id.text1_2, R.id.text1_3, R.id.text1_4, R.id.text1_5,
                R.id.text2_1, R.id.text2_2, R.id.text2_3, R.id.text2_4, R.id.text2_5,
                R.id.text3_1, R.id.text3_2, R.id.text3_3, R.id.text3_4, R.id.text3_5,
                R.id.text4_1, R.id.text4_2, R.id.text4_3, R.id.text4_4, R.id.text4_5,
                R.id.text5_1, R.id.text5_2, R.id.text5_3, R.id.text5_4, R.id.text5_5,
                R.id.text6_1, R.id.text6_2, R.id.text6_3, R.id.text6_4, R.id.text6_5};
        int lol = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                textView[i][j] = findViewById(texts[lol]);
                lol++;
            }
        }
        return textView;
    }
    public void click(Button button, String letter) {
        button.setOnClickListener(v -> handler.addLetter(letter));
    }

    public void clickDelete(Button button) {
        button.setOnClickListener(v -> handler.delete());
    }

    public void butReturn(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Вы хотите в меню?")
                .setMessage("Данные не будут сохранены")
                .setCancelable(false)
                .setPositiveButton("Да, я уверен", (dialog, which) -> ret())
                .setNegativeButton("Я еще подумаю", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void newGame(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Вы хотите начать новую игру")
                .setMessage("Данные не будут сохранены")
                .setCancelable(false)
                .setPositiveButton("Да, я уверен", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (handler.checki) {
                            retNewGame(PlayScene.this);
                        } else {
                            newGameNext();
                        }
                    }
                })
                .setNegativeButton("Я еще подумаю", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Подсказка: " + handler.clue())
                .setCancelable(false)
                .setNegativeButton("ок", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void newGameNext() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Вы хотите узнать загаданное слово?")
                .setCancelable(false)
                .setPositiveButton("Да", (dialog, which) -> word())
                .setNegativeButton("Нет, новая игра", (dialog, which) -> retNewGame(this));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ret() {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
        finish();
    }

    private void word() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Загаданное слово: " + handler.word)
                .setCancelable(false)
                .setPositiveButton("Новая игра", (dialog, which) -> retNewGame(this));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void retNewGame(Context context) {
        Intent intent = new Intent(context, PlayScene.class);
        startActivity(intent);
        finish();
    }

    public void info(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Правила:")
                .setMessage("Вам нужно отгадать слово из 5 букв\n\n" +
                        "Если буква подсвечивается желтым, то она есть в слове, но стоит не на своем месте\n\n" +
                        "Если буква подсвечивается зеленым, то вы угадали расположение буквы\n\n" +
                        "Удачи :)")
                .setCancelable(false)
                .setPositiveButton("Понятно", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}