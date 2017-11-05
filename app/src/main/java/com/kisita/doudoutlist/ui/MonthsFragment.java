package com.kisita.doudoutlist.ui;

import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MonthsFragment extends ItemFragment {

    public static MonthShoppingFragment newInstance(int columnCount) {
        MonthShoppingFragment fragment = new MonthShoppingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    void setView() {
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(getItems(),getListener());
        setAdapter(adapter);
        getRecyclerView().setAdapter(adapter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
