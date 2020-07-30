package com.example.quizzme.model;

import androidx.annotation.NonNull;

public class Question {
    private String answer;
    private boolean isTrue;

    public Question(){

    }

    public Question(String answer, boolean isTrue) {
        this.answer = answer;
        this.isTrue = isTrue;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", isTrue=" + isTrue +
                '}';
    }
}
