package com.kb.geoquiz.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kb.geoquiz.R;
import com.kb.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0x001;
    private final String TAG = this.getClass().getSimpleName();
    private final String CURRENT_INDEX = "current_index";
    private final String SHOW_ANSWER_KEY = "show_answer_key";
    private Context mContext;
    private TextView mTextView;
    private Button mBeforeBtn, mNextBtn, mTrueBtn, mFalseBtn, mCheatButton;
    private boolean isShowTheAnswer;

    private int mCurrentIndex = 0;
    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mContext = this;

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX);
            isShowTheAnswer = savedInstanceState.getBoolean(SHOW_ANSWER_KEY);
        }

        mTextView = findViewById(R.id.question_text);
        mBeforeBtn = findViewById(R.id.before_btn);
        mNextBtn = findViewById(R.id.next_btn);
        mTrueBtn = findViewById(R.id.true_btn);
        mFalseBtn = findViewById(R.id.false_btn);
        mCheatButton = findViewById(R.id.cheat_button);


        mTextView.setOnClickListener(this);
        mBeforeBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mTrueBtn.setOnClickListener(this);
        mFalseBtn.setOnClickListener(this);
        mCheatButton.setOnClickListener(this);
        updateQuestion();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
        outState.putBoolean(SHOW_ANSWER_KEY, isShowTheAnswer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                isShowTheAnswer = data.getBooleanExtra(CheatActivity.EXTRA_USER_OPERATION, false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.before_btn:
                mCurrentIndex = ((mQuestions.length + (mCurrentIndex - 1)) % mQuestions.length);
                updateQuestion();
                break;
            case R.id.question_text:
            case R.id.next_btn:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
                break;

            case R.id.true_btn:
                checkAnswer(true);
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
                break;

            case R.id.false_btn:
                checkAnswer(false);
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
                break;
            case R.id.cheat_button:
                startActivityForResult(CheatActivity.newIntent(QuizActivity.this, mQuestions[mCurrentIndex].ismAnswerTrue()), REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void updateQuestion() {
        isShowTheAnswer = false;
        int mTextResId = mQuestions[mCurrentIndex].getmTextResId();
        mTextView.setText(mTextResId);
    }

    private void checkAnswer(boolean userPressTrue) {
        int toastMessageResId;

        boolean answer = mQuestions[mCurrentIndex].ismAnswerTrue();
        if (isShowTheAnswer) {
            toastMessageResId = R.string.judgment_toast;
        } else if (answer == userPressTrue) {
            toastMessageResId = R.string.you_right;
        } else {
            toastMessageResId = R.string.you_wrong;
        }
        Toast toast = Toast.makeText(mContext, toastMessageResId, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        toast.show();
    }
}
