package com.kisita.doudoutlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kisita.doudoutlist.R;
import com.kisita.doudoutlist.data.Furniture;
import com.kisita.doudoutlist.data.Item;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public abstract class ItemFragment extends Fragment implements Parcelable {

    protected static final String ARG_COLUMN_COUNT = "column-count";
    private int                               mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Item>                   mItems;
    private ItemRecyclerViewAdapter           mAdapter;
    private RecyclerView                      mRecyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mItems = new ArrayList<>();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            mRecyclerView = (RecyclerView) view;

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            setView();
            //mAdapter = new ItemRecyclerViewAdapter(mItems, mListener);
            //recyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    abstract void setView();

    @Override
    public void onAttach(Context context) {
        Log.i("###","Save instance "+getClass().getName());
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("###","Save instance");
        super.onSaveInstanceState(outState);
        outState.putSerializable("LIST", getItems()) ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("###","Restore instance");
        if(savedInstanceState != null)
            mItems =   (ArrayList<Item>) savedInstanceState.getSerializable("LIST");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Item item);
    }


    public ArrayList<Item> getItems() {
        return mItems;
    }

    public OnListFragmentInteractionListener getListener() {
        return mListener;
    }

    public ItemRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ItemRecyclerViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
