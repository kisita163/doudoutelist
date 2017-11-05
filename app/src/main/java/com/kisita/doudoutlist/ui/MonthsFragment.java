package com.kisita.doudoutlist.ui;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisita.doudoutlist.data.Month;


public class MonthsFragment extends ItemFragment {

    private DatabaseReference mDatabase;

    public static MonthsFragment newInstance(int columnCount) {
        MonthsFragment fragment = new MonthsFragment();
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
        Log.i(TAG,"Setting Months fragment...");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Query itemsQuery = getMonthsQuery(mDatabase);
        itemsQuery.addChildEventListener(this);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(getItems(),getListener());
        setAdapter(adapter);
        getRecyclerView().setAdapter(adapter);
    }

    public Query getMonthsQuery(DatabaseReference databaseReference) {

        return databaseReference.child("months");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s){
        Log.i(TAG,"string is  : "+ dataSnapshot.child("name").getValue());
        if(dataSnapshot.child("name").getValue() != null){
            String name  = dataSnapshot.child("name").getValue().toString();
            String key   = dataSnapshot.getKey().toString();
            Log.i(TAG,"key is  : " + key);
            Month month  = new Month(name,key);
            getItems().add(month);
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG,"string is  : "+ s);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.i(TAG,"string is  : "+ dataSnapshot.toString());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG,"string is  : "+ s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(TAG,"string is  : "+ databaseError.getMessage());
    }
}
