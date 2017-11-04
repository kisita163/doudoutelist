package com.kisita.doudoutlist.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private static final String TAG = "**** Doudoute list" ;
    private Fragment     monthShoppingFragment;
    private Fragment     furnituresShoppingFragment;
    private FirebaseAuth mAuth;
    private String       monthKey;

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
                    Log.i(TAG,"month");
                    String month    = getToday();
                    String monthKey = getFireBaseKey("month");
                    addMonthToFireBase(month,monthKey);
                    ((MonthShoppingFragment)monthShoppingFragment).getItems().add(new Month(month,monthKey));
                    ((MonthShoppingFragment)monthShoppingFragment).getAdapter().notifyDataSetChanged();
                }

                if(furnituresShoppingFragment != null && furnituresShoppingFragment.isVisible()){
                    Log.i(TAG,"furniture");
                    Furniture f = new Furniture("Tata",10.2,2,"",3,"3");
                    addFurnitureToFirebase(f,"");
                    ((MonthShoppingFragment)furnituresShoppingFragment).getItems().add(f);
                    ((MonthShoppingFragment)furnituresShoppingFragment).getAdapter().notifyDataSetChanged();
                }
            }
        });
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
        monthShoppingFragment = MonthShoppingFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, monthShoppingFragment ,"Title")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            furnituresShoppingFragment = MonthShoppingFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.content_frame, furnituresShoppingFragment, "Title")
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
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(presentTime_Date);
    }

    public DatabaseReference getDb() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
