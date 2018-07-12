package com.gbsoft.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStart;
    private LinearLayout linearLayout;
    private TextView tvStatus;
    private char justShownWhat, randChar;
    private int charArr[];
    private boolean isFirst = true;
    private int count = 0;
    private char winner;
    private Animation btnAnimation, imgAnimation;
    private RelativeLayout relLayout;
    private ImageButton ib[] = new ImageButton[9];
    private int imageBtnIds[] = {R.id.imageButton, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4,
            R.id.imageButton5, R.id.imageButton6, R.id.imageButton7, R.id.imageButton8,
            R.id.imageButton9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < ib.length; i++) {
            ib[i] = findViewById(imageBtnIds[i]);
            ib[i].setOnClickListener(this);
        }

        tvStatus = findViewById(R.id.tvStatus);
        tvStatus.setText("Game has not still been started.");

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        linearLayout = findViewById(R.id.linearLayout);
        charArr = new int[10];

        relLayout = findViewById(R.id.relLayout);
        btnAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btn_anim);
        imgAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.img_btn_anim);

    }

    private void showAnimation(View v, Animation anim) {
        anim.initialize(v.getWidth(), v.getHeight(), relLayout.getWidth(), relLayout.getHeight());
        v.setAnimation(anim);
        anim.start();
    }

    private void doWhenClicked(ImageButton ib) {
        if (isFirst) {
            isFirst = false;
            randChar = randCharGen();
            if (randChar == 'O') {
                ib.setImageResource(R.drawable.o);
                justShownWhat = 'O';
                charArr[Integer.parseInt(ib.getTag().toString())] = 'O';
            } else {
                ib.setImageResource(R.drawable.x);
                justShownWhat = 'X';
                charArr[Integer.parseInt(ib.getTag().toString())] = 'X';
            }
        } else {
            if (charArr[Integer.parseInt(ib.getTag().toString())] == 0) {
                if (justShownWhat == 'O') {
                    ib.setImageResource(R.drawable.x);
                    justShownWhat = 'X';
                    charArr[Integer.parseInt(ib.getTag().toString())] = 'X';
                } else {
                    ib.setImageResource(R.drawable.o);
                    justShownWhat = 'O';
                    charArr[Integer.parseInt(ib.getTag().toString())] = 'O';
                }

            } else {
                showAlertDialog();
            }
        }
        if (!isAnyCharZero()) {
            doAtGameOver(10, 10, 10);
            tvStatus.setText("The game has been drawn");
        }
        gameOverChecker();
    }

    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            isFirst = true;
            count = 0;
            btnStart.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            resetImageButtons(9, 9, 9);
            tvStatus.setText("Game is ON!!");
        } else {
            doWhenClicked((ImageButton) view);
        }
    }

    private void gameOverChecker() {
        final int sum1 = 79 * 3, sum2 = 88 * 3;
        for (int i = 1; i < 10; i++) {
            if (i == 1 || i == 4 || i == 7) {
                int sum = sum(i, i + 1, i + 2);
                if (sum == sum1 || sum == sum2) {
                    winner = (char) (sum / 3);
                    doAtGameOver(i, i + 1, i + 2);
                    break;
                }
            }

            if (i == 3) {
                int sum = sum(i, i + 2, i + 4);
                if (sum == sum1 || sum == sum2) {
                    winner = (char) (sum / 3);
                    doAtGameOver(i, i + 2, i + 4);
                    break;
                }
            }

            if (i == 1 || i == 2 || i == 3) {
                int sum = sum(i, i + 3, i + 6);
                if (sum == sum1 || sum == sum2) {
                    winner = (char) (sum / 3);
                    doAtGameOver(i, i + 3, i + 6);
                    break;
                }
            }

            if (i == 1) {
                int sum = sum(i, i + 4, i + 8);
                if (sum == sum1 || sum == sum2) {
                    winner = (char) (sum / 3);
                    doAtGameOver(i, i + 4, i + 8);
                    break;
                }
            }
        }

    }

    private int sum(int i, int j, int k) {
        return charArr[i] + charArr[j] + charArr[k];
    }

    private char randCharGen() {
        int randNum = new Random().nextInt(2);
        if (randNum == 0)
            return 'O';
        else
            return 'X';
    }

    private void doAtGameOver(int i, int j, int k) {
        btnStart.setVisibility(View.VISIBLE);
        count = 0;
        tvStatus.setText("Game has been over!! Congratulations Player \"" + winner + "\" for your win.");
        btnStart.setText("Restart the game!!");
        resetImageButtons(i - 1, j - 1, k - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.INVISIBLE);
            }
        }, 3000);
        setCharZero();
        showAnimation(btnStart, btnAnimation);
    }

    // send 9 as each parameter if we want to reset it the first time.
    private void resetImageButtons(int i, int j, int k) {
        for (int index = 0; index < ib.length; index++) {
            if (i == 9) {
                ib[index].setVisibility(View.VISIBLE);
                ib[index].setImageResource(R.drawable.img);
            } else if (!(index == i || index == j || index == k)) {
                ib[index].setVisibility(View.INVISIBLE);
            } else {
                showAnimation(ib[index], imgAnimation);
            }
        }

    }

    private void setCharZero() {
        for (int i = 0; i < charArr.length; i++) {
            charArr[i] = 0;
        }
    }

    private boolean isAnyCharZero() {
        boolean yesZero = false;
        for (int i = 1; i < charArr.length; i++) {
            if (charArr[i] == 0) {
                yesZero = true;
            }
        }
        return yesZero;
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog,
                null, false);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        Button btnOkay = dialogView.findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvStatus.setText(getResources().getString(R.string.txt_for_fun));

                    }
                }, 10000);
                tvStatus.setText("Game is ON!!");
            }
        });
        dialog.show();

    }
}
