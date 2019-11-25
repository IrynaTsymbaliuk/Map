package com.example.tsymbaliuk_functional

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private var dataset: List<Geopoint>) :
    RecyclerView.Adapter<RecyclerViewAdapter.RvViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(adapter: RecyclerView.Adapter<*>, view: View?, position: Int, id: Int)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerViewAdapter.RvViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return RvViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {

        holder.name.text = dataset[position].name
        holder.latitude.text = dataset[position].latitude.toString()
        holder.longitude.text = dataset[position].longitude.toString()
    }

    override fun getItemCount() = dataset.size

    fun updateData(newdataset: List<Geopoint>){
        dataset = newdataset
    }

    inner class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(view: View) {
          onItemClickListener?.onItemClick(this@RecyclerViewAdapter, view, adapterPosition, view.id)
        }

        init {
            itemView.setOnClickListener(this)
        }

        val name = itemView.findViewById<TextView>(R.id.name)
        val latitude = itemView.findViewById<TextView>(R.id.latitude)
        val longitude = itemView.findViewById<TextView>(R.id.longitude)
        val arrow = itemView.findViewById<ImageView>(R.id.arrow)
    }

}