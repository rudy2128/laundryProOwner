package com.anthony.laundrypro.helper;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPref {
    private final SharedPreferences sp;
    private final SharedPreferences.Editor spe;
    Context context;

    @SuppressLint("CommitPrefEdits")
    public MyPref(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        sp = context.getSharedPreferences("MyData", MODE_PRIVATE);
        spe = sp.edit();
    }

    public void setPhone(String phone) {
        spe.putString("phone", phone).commit();
    }
    public void setName(String name) {
        spe.putString("name", name).commit();
    }



    public String getPhone() {
        return sp.getString("phone", "");
    }
    public String getName() {
        return sp.getString("name", "");
    }

    public void deleteMyPref(){
        spe.clear();
        spe.commit();
    }
}
