package com.example.roomdbapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.roomdbapp.databinding.ActivityMainBinding
import com.example.roomdbapp.roomDatabase.Item
import com.example.roomdbapp.roomDatabase.ItemDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.btnSave.setOnClickListener {
            insertItem()
        }
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

        itemDao.insertItem(item)
    }
}