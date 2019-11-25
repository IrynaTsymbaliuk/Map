package com.example.tsymbaliuk_functional

/*TODO: sort by name, remove onitemclick in extension*/
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_fragment.view.*
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : Fragment(), RecyclerViewAdapter.OnItemClickListener {
    override fun onItemClick(
        adapter: RecyclerView.Adapter<*>,
        view: View?,
        position: Int,
        id: Int
    ) {
        mainVM.deleteGeopoint(mainVM.listOfGeopoints.value!![position].name!!)

    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var mainVM: MainViewModel
    private var listForAdapter = listOf<Geopoint>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        mainVM = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mainVM.listOfGeopoints.observe(this, Observer {
            if (it != null && it.size == 0) {
                view.no_points.visibility = View.VISIBLE
                view.recycler_view.visibility = View.INVISIBLE
            } else if (it != null && it.size != 0) {
                view.no_points.visibility = View.INVISIBLE
                view.recycler_view.visibility = View.VISIBLE
                onDataChange(it)
            }
        })

        recyclerView = view.recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false
       /* if (listForAdapter.isNotEmpty()) {
            listForAdapter = listForAdapter.sortedWith(compareBy { it.name?.toLowerCase(Locale.ROOT) })
        }*/
        adapter = RecyclerViewAdapter(listForAdapter)
        adapter.onItemClickListener = this
        recyclerView.adapter = adapter

        return view

    }

    private fun onDataChange(it: ArrayList<Geopoint>) {
        listForAdapter = it
        if (::adapter.isInitialized) {
           /* if (listForAdapter.isNotEmpty()) {
                listForAdapter = listForAdapter.sortedWith(compareBy { it.name?.toLowerCase(Locale.ROOT) })
            }*/
            adapter.updateData(listForAdapter)
            adapter.notifyDataSetChanged()
        }
    }

}