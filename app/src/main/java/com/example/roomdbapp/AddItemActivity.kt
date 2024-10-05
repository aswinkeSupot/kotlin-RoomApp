package com.example.roomdbapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.roomdbapp.databinding.ActivityAddItemBinding
import com.example.roomdbapp.roomDatabase.Item
import com.example.roomdbapp.roomDatabase.ItemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_item)

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

        CoroutineScope(Dispatchers.Main).launch {
            try {
                itemDao.insertItem(item)
                Toast.makeText(applicationContext,"Successfully Inserted an Item",Toast.LENGTH_LONG).show()
                val intent = Intent(this@AddItemActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
                // Return true if the insert was successful
            } catch (e: Exception) {
                Toast.makeText(applicationContext,"Something Went Wrong while inserting",Toast.LENGTH_LONG).show()
                e.printStackTrace()
                // Return false if there was an error
            }
        }
    }
}