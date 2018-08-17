package com.kb.criminalitent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kb.criminalitent.fragment.CrimeFragment;
import com.kb.criminalitent.model.Crime;
import com.kb.criminalitent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

import com.kb.criminalitent.R;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_UUID = "com.kb.criminalitent.extra_crime_uuid";
    private static final String EXTRA_CLASS_NAME = "com.kb.criminalitent.extra_class_name";
    public static final String EXTRA_SUB_TITLE_VISIBLE = "com.kb.criminalitent.sub_title_visible";
    private ViewPager mViewPager;
    private boolean mSubTitleVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_UUID);
        mSubTitleVisible = getIntent().getBooleanExtra(EXTRA_SUB_TITLE_VISIBLE, false);
        final List<Crime> mCrimes = CrimeLab.getCrimeLab(this).getCrimes();

        mViewPager = findViewById(R.id.crime_view_pager);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                UUID uuid = mCrimes.get(position).getId();
                return CrimeFragment.newInstance(uuid);
            }

            @Override
            public int getCount() {
                return mCrimes == null ? 0 : mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(uuid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        //return super.getParentActivityIntent();
        String className = getIntent().getStringExtra(EXTRA_CLASS_NAME);
        Intent intent = null;
        try {
            intent = new Intent(CrimePagerActivity.this, Class.forName(className));
            intent.putExtra(EXTRA_SUB_TITLE_VISIBLE, mSubTitleVisible);
            return intent;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return intent;

    }

    public static Intent newIntent(Context mContext, UUID uuid, boolean subTitleVisible, String className) {
        Intent intent = new Intent(mContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_UUID, uuid);
        intent.putExtra(EXTRA_CLASS_NAME, className);
        intent.putExtra(EXTRA_SUB_TITLE_VISIBLE, subTitleVisible);
        return intent;
    }
}
