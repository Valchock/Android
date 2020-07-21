package com.project.Pictionary.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.Pictionary.AppUtils;
import com.project.Pictionary.Modal.PictionaryModel;
import com.project.Pictionary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    @BindView(R.id.question_img)
    ImageView questionImg;
    @BindView(R.id.answer_et)
    EditText answerEt;
    @BindView(R.id.submit)
    Button submit;
    int currentDifficultyLevel = 3;
    int level = 0;
    @BindView(R.id.game_round_tv)
    TextView gameRoundTv;
    PictionaryModel currentpictionaryObj;
    HashMap<Integer, PictionaryModel> pictionaryDataMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        context = GameActivity.this;
        Gson gson = new Gson();
        String jsonString = AppUtils.getInstance().loadJSONFromAsset(this);
        ArrayList<PictionaryModel> pictionaryArrayList = gson.fromJson(jsonString, new TypeToken<ArrayList<PictionaryModel>>() {
        }.getType());
        pictionaryDataMap = new HashMap<Integer, PictionaryModel>();
        for (int i = 0; i < pictionaryArrayList.size(); i++) {
            pictionaryDataMap.put(pictionaryArrayList.get(i).getDifficulty(), pictionaryArrayList.get(i));
        }
        loadPictionaryData();
        submit.setOnClickListener(this);
    }

    private void loadPictionaryData() {
        answerEt.setText("");
        currentpictionaryObj = pictionaryDataMap.get(currentDifficultyLevel);
        Picasso.with(context).load(currentpictionaryObj.getImageUrl()).fit().into(questionImg);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            validateResponseAndMoveToNextDifficultyLevel();
        }
    }

    private void validateResponseAndMoveToNextDifficultyLevel() {
        String userInput = answerEt.getText().toString();
        if (userInput.equalsIgnoreCase(currentpictionaryObj.getAnswer())) {
            level = level + 1;
            gameRoundTv.setText(getResources().getString(R.string.round) + level + getResources().getString(R.string.five));
            currentDifficultyLevel = currentDifficultyLevel + 1;
            if (currentDifficultyLevel >= 5) {
                currentDifficultyLevel = 5;
            }
            if (level < 5) {
                loadPictionaryData();
            } else if (level == 5) {
                Toast.makeText(this, getResources().getString(R.string.win) + level, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            currentDifficultyLevel = currentDifficultyLevel - 1;
            if (currentDifficultyLevel == 0) {
                Toast.makeText(this, getResources().getString(R.string.lose) + level, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                loadPictionaryData();
            }
        }
    }
}
