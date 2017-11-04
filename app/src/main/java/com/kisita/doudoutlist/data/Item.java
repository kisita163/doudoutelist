package com.kisita.doudoutlist.data;

/**
 * Created by HuguesKi on 26-09-17.
 */

public abstract class Item {

    private  String mName;

    private String mKey;

    public Item(String mName,String key) {
        this.mName = mName;
        this.mKey  = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }
}
