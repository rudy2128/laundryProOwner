package com.anthony.laundrypro.helper;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class CodePref {
    private final SharedPreferences sp;
    private final SharedPreferences.Editor spe;
    Context context;

    @SuppressLint("CommitPrefEdits")
    public CodePref(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        sp = context.getSharedPreferences("MyData", MODE_PRIVATE);
        spe = sp.edit();
    }
    public void setOwner(String code) {
        spe.putString("code", code).commit();
    }
    public void setOutlet(String codeOutlet) {
        spe.putString("codeOutlet", codeOutlet).commit();
    }

    public String getOwner() {
        return sp.getString("code", "");
    }
    public String getOutlet() {
        return sp.getString("codeOutlet", "");
    }


    public void deleteCodePref(){
        spe.clear();
        spe.commit();
    }

}
