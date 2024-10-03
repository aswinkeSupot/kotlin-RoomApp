package com.example.roomdbapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.roomdbapp.databinding.ActivityMainBinding
import com.example.roomdbapp.roomDatabase.Item
import com.example.roomdbapp.roomDatabase.ItemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.btnSave.setOnClickListener {
            insertItem()
        }

        displayAllRecords()
    }

    fun insertItem() {
        // Getting the text from EditText
        val name = binding.edItemName.text.toString()
        val price = binding.edItemPrice.text.toString()
        val quantity = binding.edItemQuantity.text.toString()

        // Converting String to int and double
        val intQuantity = quantity.toInt()
        val doublePrice = price.toDouble()


        // Instance of the Database
        val itemDB = ItemDatabase.getDatabase(applicationContext)

        // Instance of DAO
        val itemDao = itemDB.getItemDao()

        // Inserting data into Database
        var item: Item = Item(0,name, doublePrice, intQuantity)

        CoroutineScope(Dispatchers.IO).launch {
            itemDao.insertItem(item)
        }
    }

    fun displayAllRecords() {
        // Instance of the Database
        val itemDB = ItemDatabase.getDatabase(applicationContext)
        // Instance of DAO
        val itemDao = itemDB.getItemDao()

        itemDao.getAllItemsInDB().observe(this, Observer {
            var result = ""

            for ((index, item) in it.withIndex()){
                result = result + "${index+1}.  item = ${item.name} \n     price = ${item.price} \n     quantity = ${item.quantity} \n__________________\n"
//                result = it.joinToString("\n")
            }

            binding.tvRecords.text = result
        })


    }
}