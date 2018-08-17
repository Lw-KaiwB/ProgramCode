package com.kb.geoquiz.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.kb.geoquiz.R;

public class CheatActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final String EXTRA_ANSWER = "com.kb.geoquiz.extra_answer";
    public static final String EXTRA_USER_OPERATION = "com.kb.geoquiz.extra_user_operation";
    private static final String KEY_SHOW_ANSWER = "com.kb.geoquiz.key_show_answer";
    private boolean mExtraAnswer;
    private TextView mShowAnswerTextView;
    private Button mShowAnswerButton;
    private boolean isShowAnswer;

    public static Intent newIntent(Context mContext, boolean isTrue) {
        Intent intent = new Intent(mContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER, isTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isShowAnswer = savedInstanceState.getBoolean(KEY_SHOW_ANSWER);
        }
        setContentView(R.layout.activity_cheat);

        mExtraAnswer = this.getIntent().getBooleanExtra(EXTRA_ANSWER, false);

        mShowAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowAnswerTextView.setText(mExtraAnswer ? R.string.true_btn : R.string.false_btn);
                isShowAnswer = true;

                int x = mShowAnswerButton.getWidth() / 2 + (int) mShowAnswerButton.getX();
                int y = mShowAnswerButton.getHeight() / 2 + (int) mShowAnswerButton.getY();
                float radius = mShowAnswerButton.getWidth();
                Log.e(TAG, "radius=" + radius+" x="+x+" y="+y);
                if (Build.VERSION.SDK_INT >= 21) {
                    Animator ani = ViewAnimationUtils.createCircularReveal(mShowAnswerTextView, x, y, radius, 0);
                    ani.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    ani.setDuration(3000);
                    ani.start();
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SHOW_ANSWER, isShowAnswer);
    }

    @Override
    public void onBackPressed() {
        setAnswerShowResult(isShowAnswer);
        super.onBackPressed();
    }

    private void setAnswerShowResult(boolean isCheat) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_OPERATION, isCheat);
        setResult(RESULT_OK, intent);
    }


}
