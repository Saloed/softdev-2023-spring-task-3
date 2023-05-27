package com.yooondooo.WORDLY;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayScene extends AppCompatActivity {
    private Handler handler;
    @SuppressLint({"MissingInflatedId", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_scene);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        TextView[][] textView = new TextView[6][5];
        textView[0][0] = findViewById(R.id.text1_1);
        textView[0][1] = findViewById(R.id.text1_2);
        textView[0][2] = findViewById(R.id.text1_3);
        textView[0][3] = findViewById(R.id.text1_4);
        textView[0][4] = findViewById(R.id.text1_5);
        textView[1][0] = findViewById(R.id.text2_1);
        textView[1][1] = findViewById(R.id.text2_2);
        textView[1][2] = findViewById(R.id.text2_3);
        textView[1][3] = findViewById(R.id.text2_4);
        textView[1][4] = findViewById(R.id.text2_5);
        textView[2][0] = findViewById(R.id.text3_1);
        textView[2][1] = findViewById(R.id.text3_2);
        textView[2][2] = findViewById(R.id.text3_3);
        textView[2][3] = findViewById(R.id.text3_4);
        textView[2][4] = findViewById(R.id.text3_5);
        textView[3][0] = findViewById(R.id.text4_1);
        textView[3][1] = findViewById(R.id.text4_2);
        textView[3][2] = findViewById(R.id.text4_3);
        textView[3][3] = findViewById(R.id.text4_4);
        textView[3][4] = findViewById(R.id.text4_5);
        textView[4][0] = findViewById(R.id.text5_1);
        textView[4][1] = findViewById(R.id.text5_2);
        textView[4][2] = findViewById(R.id.text5_3);
        textView[4][3] = findViewById(R.id.text5_4);
        textView[4][4] = findViewById(R.id.text5_5);
        textView[5][0] = findViewById(R.id.text6_1);
        textView[5][1] = findViewById(R.id.text6_2);
        textView[5][2] = findViewById(R.id.text6_3);
        textView[5][3] = findViewById(R.id.text6_4);
        textView[5][4] = findViewById(R.id.text6_5);
        handler = new Handler(textView, this);
        Button buttonQ = findViewById(R.id.buttonQ);
        Button buttonW = findViewById(R.id.buttonW);
        Button buttonE = findViewById(R.id.buttonE);
        Button buttonR = findViewById(R.id.buttonR);
        Button buttonT = findViewById(R.id.buttonT);
        Button buttonY = findViewById(R.id.buttonY);
        Button buttonU = findViewById(R.id.buttonU);
        Button buttonI = findViewById(R.id.buttonI);
        Button buttonO = findViewById(R.id.buttonO);
        Button buttonP = findViewById(R.id.buttonP);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button buttonA = findViewById(R.id.buttonA);
        Button buttonS = findViewById(R.id.buttonS);
        Button buttonD = findViewById(R.id.buttonD);
        Button buttonF = findViewById(R.id.buttonF);
        Button buttonG = findViewById(R.id.buttonG);
        Button buttonH = findViewById(R.id.buttonH);
        Button buttonJ = findViewById(R.id.buttonJ);
        Button buttonK = findViewById(R.id.buttonK);
        Button buttonL = findViewById(R.id.buttonL);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button buttonZ = findViewById(R.id.buttonZ);
        Button buttonX = findViewById(R.id.buttonX);
        Button buttonC = findViewById(R.id.buttonC);
        Button buttonV = findViewById(R.id.buttonV);
        Button buttonB = findViewById(R.id.buttonB);
        Button buttonN = findViewById(R.id.buttonN);
        Button buttonM = findViewById(R.id.buttonM);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        click(buttonQ,"Й");
        click(buttonW, "Ц");
        click(buttonE, "У");
        click(buttonR, "К");
        click(buttonT, "Е");
        click(buttonY, "Н");
        click(buttonU, "Г");
        click(buttonI, "Ш");
        click(buttonO, "Щ");
        click(buttonP, "З");
        click(button1, "Х");
        click(button2, "Ъ");
        click(buttonA, "Ф");
        click(buttonS, "Ы");
        click(buttonD, "В");
        click(buttonF, "А");
        click(buttonG, "П");
        click(buttonH, "Р");
        click(buttonJ, "О");
        click(buttonK, "Л");
        click(buttonL, "Д");
        click(button3, "Ж");
        click(button4, "Э");
        click(buttonZ, "Я");
        click(buttonX, "Ч");
        click(buttonC, "С");
        click(buttonV, "М");
        click(buttonB, "И");
        click(buttonN, "Т");
        click(buttonM, "Ь");
        click(button5, "Б");
        click(button6, "Ю");
        clickDelete(buttonDelete);
    }

    public void click(Button button, String letter){
        button.setOnClickListener(v -> handler.addLetter(letter));
    }
    public void clickDelete(Button button){
        button.setOnClickListener(v -> handler.delete());
    }
    public void butReturn(View v){
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

    public void newGame(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Вы хотите начать новую игру")
                .setMessage("Данные не будут сохранены")
                .setCancelable(false)
                .setPositiveButton("Да, я уверен", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (handler.checki){
                            retNewGame(PlayScene.this);
                        }
                        else{
                            newGameNext();
                        }
                    }
                })
                .setNegativeButton("Я еще подумаю", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void clue(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Подсказка: " + handler.clue())
                .setCancelable(false)
                .setNegativeButton("ок", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void newGameNext(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Вы хотите узнать загаданное слово?")
                .setCancelable(false)
                .setPositiveButton("Да", (dialog, which) -> word())
                .setNegativeButton("Нет, новая игра", (dialog, which) -> retNewGame(this));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void ret () {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
        finish();
    }
    private void word(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayScene.this);
        builder.setTitle("Загаданное слово: " + handler.word)
                .setCancelable(false)
                .setPositiveButton("Новая игра", (dialog, which) -> retNewGame(this));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void retNewGame (Context context) {
        Intent intent = new Intent(context, PlayScene.class);
        startActivity(intent);
        finish();
    }
    public void info(View view){
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