package com.shm.shivam.typiingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {

    CountDownTimer timer;
    TextView tvTimer, tvPara;
    EditText etText;
    String para="";
    boolean timerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //para = getString(R.string.para);
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        tvPara = (TextView) findViewById(R.id.tv_para);
        etText = (EditText) findViewById(R.id.et_text);
        //tvPara.setText(para);
        timer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText(""+millisUntilFinished / 1000);
            }

            public void onFinish() {
                tvTimer.setText("60");
                evaluateScore();
            }
        };
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!timerStarted && charSequence.length()>0){
                    timer.start();
                    timerStarted = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etText.addTextChangedListener(textWatcher);
    }

    private void evaluateScore(){
        String[] textWords = etText.getText().toString().split(" ");
        String[] paraWords = tvPara.getText().toString().split(" ");
        int score  = 0;
        for(int i = 0; i < min(textWords.length, paraWords.length); i++){
            if(textWords[i].equals(paraWords[i])){
                score+=10;
            }
            else{
                score-=5;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time up");
        builder.setMessage("Your score is: "+score);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                timerStarted = false;
                etText.setText("");
            }
        });
        builder.show();
    }


}
