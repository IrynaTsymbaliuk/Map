package com.example.tsymbaliuk_functional

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_fragment.view.*

class ListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var mainVM: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        mainVM =
            ViewModelProvider(
                activity!!,
                ViewModelProvider.AndroidViewModelFactory(MyApplication())
            )
                .get(MainViewModel::class.java)

        mainVM.getGeopoints().observe(this, Observer {
            if (::viewAdapter.isInitialized) {
                viewAdapter.updateData(it)
                viewAdapter.notifyDataSetChanged()
            } else {
                setUpRecyclerView()
            }
        })

        recyclerView = view.recycler_view
        setUpRecyclerView()

        return view

    }

    private fun setUpRecyclerView() {
        viewManager = LinearLayoutManager(context)
        if (mainVM.listOfGeopoints.value != null) {
            viewAdapter = RecyclerViewAdapter(mainVM.listOfGeopoints.value!!)
            recyclerView.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }

}