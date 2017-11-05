package com.kisita.doudoutlist.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.doudoutlist.R;
import com.kisita.doudoutlist.data.Furniture;
import com.kisita.doudoutlist.data.Item;
import com.kisita.doudoutlist.data.Month;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private static final String TAG = "**** Doudoute list" ;
    private Fragment     monthShoppingFragment;
    private Fragment     monthsFragment;
    private FirebaseAuth mAuth;
    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null ){
            mAuth.signInWithEmailAndPassword("kisita2002@yahoo.fr", "Kinshasa162")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.i(TAG,user.toString());
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //updateUI(null);
                            }
                        }
                    });
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(monthShoppingFragment != null && monthShoppingFragment.isVisible()){
                    Log.i(TAG,"furniture");
                    getBarCode();
                }

                if(monthsFragment != null && monthsFragment.isVisible()){
                    Log.i(TAG,"month");
                    String month    = getToday();
                    // Is the month already entered?
                    if(checkMonth(month)) {
                        String monthKey = getFireBaseKey("month");
                        addMonthToFireBase(month, monthKey);
                    }else{
                        // Tell the user the month already exist
                        Toast.makeText(getApplicationContext(), R.string.currentMonth,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean checkMonth(String month) {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.dlMonth),
                Context.MODE_PRIVATE);
        String currMonth = sharedPref.getString(getResources().getString(R.string.dlMonthValue),"");

        if(currMonth.equalsIgnoreCase("")){ // No month saved at all. Save this one
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.dlMonthValue),month);
            editor.commit();
        }

        if(currMonth.equalsIgnoreCase(month)){
            return false;
        }
        return true;
    }

    private void addMonthToFireBase(String month,String key) {
        Map<String, Object> childUpdates = new HashMap<>();

        Log.i(TAG,key);

        childUpdates.put("/months/" + key + "/name",month);

        Log.i(TAG,childUpdates.toString());

        getDb().updateChildren(childUpdates);
    }


    /**
     * @param furniture The item that need to be add
     * @param month     The key of the month where this furniture will be added
     */
    private void addFurnitureToFirebase(Furniture furniture, String month){
        Map<String, Object> childUpdates = new HashMap<>();

        String furnitureKey = getFireBaseKey("furniture");

        childUpdates.put("/months/" + month + "/items/" + furnitureKey + "/name"    ,furniture.getName());
        childUpdates.put("/months/" + month + "/items/" + furnitureKey + "/price"   ,furniture.getPrice());
        childUpdates.put("/months/" + month + "/items/" + furnitureKey + "/quantity",furniture.getQuantity());
        childUpdates.put("/months/" + month + "/items/" + furnitureKey + "/picture" ,furniture.getPictureUrl());
        childUpdates.put("/months/" + month + "/items/" + furnitureKey + "/priority",furniture.getPriority());

        Log.i(TAG,childUpdates.toString());

        getDb().updateChildren(childUpdates);
    }


    private String getFireBaseKey(String s){
        return getDb().child(s).push().getKey();
    }

    private void setFragment() {
        monthsFragment = MonthsFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, monthsFragment ,"Title")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getBarCode(){
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); // TODO
        intent.putExtra(BarcodeCaptureActivity.UseFlash,true);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Item item) {
        Log.i(TAG,item.getClass().toString());

        if(item instanceof Month) {
            monthShoppingFragment = MonthShoppingFragment.newInstance(1,item.getKey());
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.content_frame, monthShoppingFragment, "Title")
                    .commit();
        }

        if(item instanceof Furniture) {

        }
    }

    /**
     * @return the name of the current month
     */
    public static String getToday(){
        Date presentTime_Date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        return dateFormat.format(presentTime_Date).toUpperCase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    if(checkFurniture(barcode.displayValue)) {
                        addFurnitureToFirebase(new Furniture(barcode.displayValue, ""), ((MonthShoppingFragment) monthShoppingFragment).getMonth());
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.furnitureExists,Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Log.d(TAG, "Failed to read anything...");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean checkFurniture(String displayValue) {
        if(monthShoppingFragment != null){
            for(Item f  : ((MonthShoppingFragment) monthShoppingFragment).getItems()){
                if(f.getName().equalsIgnoreCase(displayValue)){
                    return false;
                }
            }
        }
        return true;
    }

    public DatabaseReference getDb() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
