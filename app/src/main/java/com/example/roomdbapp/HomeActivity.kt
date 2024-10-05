package com.example.roomdbapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aswin.kotlinroomappmvvm.adapter.ItemRecyclerAdapter
import com.example.roomdbapp.databinding.ActivityHomeBinding
import com.example.roomdbapp.roomDatabase.Item
import com.example.roomdbapp.roomDatabase.ItemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    lateinit var itemList: MutableList<Item>
    lateinit var adapter: ItemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddItemActivity::class.java)
            startActivity(intent)
        }

        //RecyclerView
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        //Post arrayList
        itemList = arrayListOf<Item>()

        displayAllRecords()
    }

    private fun displayAllRecords() {
        // Instance of the Database
        val itemDB = ItemDatabase.getDatabase(applicationContext)
        // Instance of DAO
        val itemDao = itemDB.getItemDao()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                itemDao.getAllItemsInDB().observe(this@HomeActivity, Observer {
                    if(it.isNotEmpty()){
                        var result = ""

                        for ((index, item) in it.withIndex()){
                            result += "${index + 1}.  item = ${item.name} \n     price = ${item.price} \n     quantity = ${item.quantity} \n__________________\n"

                            var itemval = Item(
                                item.id,
                                item.name,
                                item.price,
                                item.quantity
                            )
                            itemList.add(itemval)
                        }

                        // RecyclerView
                        adapter = ItemRecyclerAdapter(applicationContext,itemList, object :ItemRecyclerAdapter.OnItemClickListener{
                            override fun onItemClick(item: Item) {
                                val intent = Intent(this@HomeActivity, ItemDetailsActivity::class.java)
                                intent.putExtra("item", item) // Pass the item as Serializable
                                startActivity(intent)
                            }

                        })
                        binding.recyclerView.setAdapter(adapter)
                        adapter.notifyDataSetChanged()

                    }else{
                        binding.tvNoPost.visibility = View.VISIBLE
                        binding.tvNoPost.text = "No Records Found"
                    }
                })

                // Return true if the insert was successful
            } catch (e: Exception) {
                Toast.makeText(applicationContext,"Something Went Wrong while inserting", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                // Return false if there was an error
            }
        }
    }
}