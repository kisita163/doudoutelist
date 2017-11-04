package com.kisita.doudoutlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kisita.doudoutlist.R;


import java.util.List;


public class MonthShoppingFragment extends ItemFragment {

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
