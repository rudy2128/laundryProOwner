package com.anthony.laundrypro.helper

import com.anthony.laundrypro.entity.Outlet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

class FirebaseHelPer(val db:DatabaseReference) {
    private lateinit var firebaseHelperCallback: FirebaseHelperCallback

    fun setFirebaseHelperCallback(firebaseHelperCallback: FirebaseHelperCallback) {
        this.firebaseHelperCallback = firebaseHelperCallback
    }
    private fun fetchData(snapshot: DataSnapshot, listOutlets: ArrayList<String>) {
        listOutlets.clear()
        val name: Outlet? = snapshot.getValue(Outlet::class.java)
        listOutlets.add(listOutlets.toString())
        firebaseHelperCallback.dataRetrievedFromDB(listOutlets)
    }


}