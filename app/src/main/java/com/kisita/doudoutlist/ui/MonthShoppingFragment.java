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
import com.kisita.doudoutlist.data.Furniture;
import com.kisita.doudoutlist.data.Month;


public class MonthShoppingFragment extends ItemFragment {
    private DatabaseReference mDatabase;
    private String mMonth = null;
    private static String MONTH_KEY     = "month";

    public static MonthShoppingFragment newInstance(int columnCount , String month) {
        MonthShoppingFragment fragment = new MonthShoppingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(MONTH_KEY,month);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mMonth = getArguments().getString(MONTH_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    void setView() {
        Log.i(TAG,"Setting Months Shopping fragment...");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Query itemsQuery = getItemsQuery(mDatabase);
        itemsQuery.addChildEventListener(this);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(getItems(),getListener());
        setAdapter(adapter);
        getRecyclerView().setAdapter(adapter);
    }

    public String getMonth() {
        return mMonth;
    }

    public Query getItemsQuery(DatabaseReference databaseReference) {

        return databaseReference.child("months").child(getMonth()).child("items");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(TAG,"child added  : "+dataSnapshot.getValue());
        //Furniture(String name, double mPrice, int mQuantity, String mPictureUrl, int mPriority,String key) {
        String name     = dataSnapshot.child("name").getValue().toString();
        String key      = dataSnapshot.getKey().toString();
        String price    = dataSnapshot.child("price").getValue().toString();
        String quantity = dataSnapshot.child("quantity").getValue().toString();
        String picture  = dataSnapshot.child("picture").getValue().toString();
        String priority = dataSnapshot.child("priority").getValue().toString();

        getItems().add(new Furniture(name,price,quantity,picture,priority,key));
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
