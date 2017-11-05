package com.kisita.doudoutlist.data;

/*
 * Created by HuguesKi on 25-09-17.
 */

import com.kisita.doudoutlist.ui.ItemRecyclerViewAdapter;

public class Furniture extends Item{
    private  double mPrice;
    private  int    mQuantity;
    private  String mPictureUrl;
    private  int    mPriority;

    public Furniture(String name, double mPrice, int mQuantity, String mPictureUrl, int mPriority,String key) {
        super(name,key);
        this.mPrice      = mPrice;
        this.mQuantity   = mQuantity;
        this.mPictureUrl = mPictureUrl;
        this.mPriority   = mPriority;
    }

    public Furniture(String name, String price, String quantity, String mPictureUrl, String mPriority,String key) {
        super(name,key);
        this.mPrice      = Double.valueOf(price);
        this.mQuantity   = Integer.valueOf(quantity);
        this.mPictureUrl = mPictureUrl;
        this.mPriority   = Integer.valueOf(mPriority);
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }


    public void setPictureUrl(String mPictureUrl) {

        this.mPictureUrl = mPictureUrl;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }
}
