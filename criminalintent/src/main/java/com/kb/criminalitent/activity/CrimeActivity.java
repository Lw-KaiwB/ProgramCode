package com.kb.criminalitent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kb.criminalitent.fragment.CrimeFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_UUID = "com.kb.criminaliten.extra_crime_uuid";

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_UUID);
        return CrimeFragment.newInstance(uuid);
    }

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_UUID, uuid);
        return intent;
    }
}
