package com.kb.criminalitent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kb.criminalitent.R;
import com.kb.criminalitent.activity.CrimePagerActivity;
import com.kb.criminalitent.model.Crime;
import com.kb.criminalitent.model.CrimeLab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private static final int NORMAL_LAYOUT = 0x001;
    private static final String SAVE_SUB_TITLE_STATE = "save_sub_title_state";
    private RecyclerView mRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubTitleVisible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSubTitleVisible = savedInstanceState.getBoolean(SAVE_SUB_TITLE_STATE, false);
        }
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mSubTitleVisible = intent.getBooleanExtra(CrimePagerActivity.EXTRA_SUB_TITLE_VISIBLE, false);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem menuItem = menu.findItem(R.id.show_subtitle);
        if (mSubTitleVisible) {
            menuItem.setTitle(R.string.hide_subtitle);
        } else {
            menuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId(), mSubTitleVisible, "com.kb.criminalitent.activity.CrimeListActivity");
                startActivity(intent);
                return true;

            case R.id.show_subtitle:
                mSubTitleVisible = !mSubTitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubTitle();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_SUB_TITLE_STATE, mSubTitleVisible);
    }

    private void updateUI() {
        List<Crime> mCrimes = CrimeLab.getCrimeLab(getActivity()).getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(mCrimes);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(mCrimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubTitle();
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.mCrimes = crimes;
        }

        public void setCrimes(List<Crime> cirmes) {
            this.mCrimes = cirmes;
        }

        @Override
        public int getItemViewType(int position) {
            //return super.getItemViewType(position);
            if (mCrimes == null) return NORMAL_LAYOUT;
            else {
                return mCrimes.get(position).isRequiredPolice() ? -1 : NORMAL_LAYOUT;
            }
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            if (viewType == NORMAL_LAYOUT) {
                return new CrimeHolder(layoutInflater.inflate(R.layout.list_item_crime, parent, false));
            } else {
                return new CrimeHolder(layoutInflater.inflate(R.layout.list_item_crime_police, parent, false));
            }

        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            holder.bind(mCrimes.get(position));
        }

        @Override
        public int getItemCount() {
            return mCrimes != null ? mCrimes.size() : 0;
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleText;
        private TextView mDateText;
        private Crime mCrime;

        public CrimeHolder(View view) {
            super(view);

            mTitleText = itemView.findViewById(R.id.crime_title);
            mDateText = itemView.findViewById(R.id.crime_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //startActivity(CrimeActivity.newIntent(getActivity(), mCrime.getId()));
            startActivity(CrimePagerActivity.newIntent(getActivity(), mCrime.getId(), mSubTitleVisible, "com.kb.criminalitent.activity.CrimeListActivity"));
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleText.setText(crime.getTitle());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
            mDateText.setText(dateFormat.format(crime.getDate()));
        }
    }

    private void updateSubTitle() {
        List<Crime> mCrimes = CrimeLab.getCrimeLab(getActivity()).getCrimes();
        int size = mCrimes.size();
        //String title = getString(R.string.subtitle_format, size);
        String title = getResources().getQuantityString(R.plurals.subtitle_plural, size, size);
        Log.e(TAG, "size=" + size + " title=" + title);
        if (!mSubTitleVisible) {
            title = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(title);
    }
}
