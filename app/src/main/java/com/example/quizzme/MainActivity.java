package com.example.quizzme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzme.data.AnswerListAsyncResponse;
import com.example.quizzme.data.QuestionBank;
import com.example.quizzme.model.Question;
import com.example.quizzme.model.Score;
import com.example.quizzme.utils.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton prevButton;
    private ImageButton nextButton;
    private Button trueBtn;
    private Button falseBtn;
    private int count = 0;
    private TextView question;
    private TextView questionCount;
    private List<Question> questionList;

    private int scoreCounter=0;
    private Score score;
    private TextView scoreText;
    private TextView highScore;
    private Prefs pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();
        pref = new Prefs(MainActivity.this);

        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        trueBtn = findViewById(R.id.true_button);
        falseBtn = findViewById(R.id.false_button);
        question = findViewById(R.id.question_text);
        questionCount = findViewById(R.id.counter_question);
        scoreText = findViewById(R.id.score_view);
        highScore = findViewById(R.id.highest_score);

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        trueBtn.setOnClickListener(this);
        falseBtn.setOnClickListener(this);

        scoreText.setText("Score: " + 0);
        highScore.setText("High Score: "+ pref.getHighScore());

        count = pref.getState();

//        questionCount.setText(count +" out of "+ questionList.size());

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
//                    Log.d("inside","onResponse" + questionArrayList);
                question.setText(questionList.get(count).getAnswer());

                questionCount.setText(count +" out of "+ questionList.size());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev_button:
                if(count>0){
                    count = (count-1)%questionList.size();
                    updateQ();
                }
                break;
            case R.id.next_button:
                count = (count+1)%questionList.size();
                updateQ();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQ();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQ();
                break;
        }
    }

    private void checkAnswer(boolean selected) {
        int toastId;
        if(selected == questionList.get(count).isTrue()){
            addPts();
            fadeView();
            toastId = R.string.correct;
        }else{
            deductPts();
            shakeAnimation();
            toastId = R.string.wrong;
        }

        Toast.makeText(MainActivity.this,toastId,Toast.LENGTH_SHORT).show();
    }

    private void deductPts() {
        scoreCounter -= 5;
        score.setScore(scoreCounter);
        scoreText.setText("Score: "+ score.getScore());
    }

    private void addPts() {
        scoreCounter += 10;
        score.setScore(scoreCounter);
        scoreText.setText("Score: "+ score.getScore());
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                count = (count+1)%questionList.size();
                updateQ();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(250);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                count = (count+1)%questionList.size();
                updateQ();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        pref.saveHighScore(score.getScore());
        pref.saveState(count);
        super.onPause();
    }

    private void updateQ() {
    String ques = questionList.get(count).getAnswer();
    question.setText(ques);
    questionCount.setText(count +" out of "+ questionList.size());
    }
}