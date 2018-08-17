package com.kb.criminalitent.activity;

import android.support.v4.app.Fragment;

import com.kb.criminalitent.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
