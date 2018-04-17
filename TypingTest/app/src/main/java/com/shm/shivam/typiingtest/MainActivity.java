package com.shm.shivam.typiingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.inputmethod.EditorInfo;
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
        para = getString(R.string.para);
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        tvPara = (TextView) findViewById(R.id.tv_para);
        etText = (EditText) findViewById(R.id.et_text);
        etText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        tvPara.setText(para);
        final String[] paraWords = para.split(" ");
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
                String[] words = charSequence.toString().split(" ");
                int  occ = words.length-1;
                String paraText = "<font color='#bdbdbd'>";
                for(int n = 0; n<occ;n++){
                    paraText+=paraWords[n] + " ";
                }
                String color = "#ff0000";
                if(paraWords[occ].indexOf(words[occ])==0){
                    color = "#4caf50";
                }
                paraText+="</font><font color='"+color+"'>"+paraWords[occ]+"</font> ";
                for(int n = occ+1; n<paraWords.length;n++){
                    paraText+=paraWords[n] + " ";
                }
                tvPara.setText(Html.fromHtml(paraText));
                String wordToLookFor="";
                if(occ>0){
                    wordToLookFor+=" ";
                }
                wordToLookFor+= paraWords[occ];
                int indexOfWord = tvPara.getText().toString().indexOf(wordToLookFor);
                tvPara.setMovementMethod(new ScrollingMovementMethod());
                final int lineNumber = tvPara.getLayout().getLineForOffset(indexOfWord);
                if(lineNumber>2 && occ>0){
                    tvPara.post(new Runnable() {
                        @Override
                        public void run() {
                            int y = tvPara.getLayout().getLineTop(lineNumber-1);
                            tvPara.scrollTo(0, y);
                        }
                    });
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
        //int score  = 0;
        int correctWords = 0;
        int wrongWords = 0;
        String wrongMessage="";
        for(int i = 0; i < min(textWords.length, paraWords.length); i++){
            if(textWords[i].equals(paraWords[i])){
                //score+=10;
                correctWords++;
            }
            else{
                //score-=5;
                wrongWords++;
                wrongMessage+="\n'"+paraWords[i]+"' typed as '"+textWords[i] +"'";
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time up");
        builder.setMessage("Your score is: "+(10*correctWords-5*wrongWords)+"\n Correct words: "+correctWords + "\n Wrong Words: "+wrongWords+wrongMessage);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                timerStarted = false;
                etText.setText("");
                tvPara.post(new Runnable() {
                    @Override
                    public void run() {
                        int y = tvPara.getLayout().getLineTop(0);
                        tvPara.scrollTo(0, y);
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

}
