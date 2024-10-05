package com.aswin.kotlinroomappmvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapp.databinding.ItemRowBinding
import com.example.roomdbapp.roomDatabase.Item

/**
 * Created by Aswin on 04-10-2024.
 */
class ItemRecyclerAdapter(val context: Context,
                          private var itemList:List<Item>,
                          private val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<ItemRecyclerAdapter.MyViewHolder>()
{
    lateinit var binding : ItemRowBinding

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    //View Holder
    class MyViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, clickListener: OnItemClickListener){
            binding.item = item

            // Set click listener
            binding.root.setOnClickListener {
                clickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.journal_row,parent,false)

        binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int = itemList.size
    /**OR**/
//    override fun getItemCount(): Int {
//        return journalList.size
//    }


}