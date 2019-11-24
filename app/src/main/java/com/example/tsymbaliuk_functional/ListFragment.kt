package com.example.tsymbaliuk_functional

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        MyApplication.instance.database.collection("oblast_centers_in_ukraine")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("DATABASE", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DATABASE", "Error getting documents.", exception)
            }

        return view

    }

}