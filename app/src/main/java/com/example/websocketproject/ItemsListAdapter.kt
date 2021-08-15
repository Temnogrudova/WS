package com.example.websocketproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView


class ItemsListAdapter(
    private var items: List<GroceryModel?>,
    private val interactionListener: InteractionListener
) : RecyclerView.Adapter<ItemsListAdapter.BaseViewHolder>() {

    interface InteractionListener {
        fun onItemClick(v: View, feed: GroceryModel?)
    }
    fun setData(items: List<GroceryModel?>) {
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
        var mWeight: TextView = itemView.findViewById(R.id.weight)
        var mImage: CircleImageView = itemView.findViewById(R.id.image)

        fun bind(item: GroceryModel?) {
            item?.name?.let {
                mTitle.text = it
            }
            item?.weight?.let {
                mWeight.text = it
            }
            item?.bagColor?.let {
                mImage.setImageDrawable(ColorDrawable(Color.parseColor(it)))
            }
            itemView.setOnClickListener { view ->
                interactionListener.onItemClick(view, item)
            }
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