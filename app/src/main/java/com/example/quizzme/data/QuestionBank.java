package com.example.quizzme.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.quizzme.controller.AppController;
import com.example.quizzme.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> questionList = new ArrayList<>();

    public List<Question> getQuestions(final AnswerListAsyncResponse callback){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,(JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Log.d("JSON ","onResponse "+ response);
                
                for(int i=0; i<response.length(); i++){
                    try {
                        Question question = new Question();
                        question.setAnswer(response.getJSONArray(i).get(0).toString());
                        question.setTrue(response.getJSONArray(i).getBoolean(1));
                        questionList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(callback != null){
                    callback.processFinished(questionList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionList;
    }
}
