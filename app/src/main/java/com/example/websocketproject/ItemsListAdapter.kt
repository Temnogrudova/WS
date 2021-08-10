package com.example.websocketproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class ItemsListAdapter(
    private var items: List<ItemModel?>
    //private val interactionListener: InteractionListener
) : RecyclerView.Adapter<ItemsListAdapter.BaseViewHolder>() {

//    interface InteractionListener {
//        fun onItemClick(v: View, feed: ItemModel?)
//    }
    fun setData(items: List<ItemModel?>) {
        this.items = items
        notifyDataSetChanged()
    }
//    fun setData(items: ItemModel?) {
//        this.items.add(items)
//        notifyDataSetChanged()
//    }

    private lateinit var inflater: LayoutInflater
    abstract class BaseViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class ItemViewHolder (itemView: View) : BaseViewHolder(itemView) {
        var mTitle: TextView = itemView.findViewById(R.id.title)

        fun bind(item: ItemModel?) {
            item?.price?.let {
                mTitle.text = it
            }
            itemView.background = ColorDrawable(Color.parseColor("#FF00FF00"))
//            itemView.setOnClickListener { view ->
//                interactionListener.onItemClick(view, item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val view: View = inflater.inflate(R.layout.item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(items[position])
    }
}