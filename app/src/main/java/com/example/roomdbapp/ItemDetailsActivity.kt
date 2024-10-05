package com.example.roomdbapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.roomdbapp.databinding.ActivityItemDetailsBinding
import com.example.roomdbapp.roomDatabase.Item
import com.example.roomdbapp.roomDatabase.ItemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_details)

        // Retrieve the passed item
        val item: Item? = intent.getSerializableExtra("item") as? Item

        item?.let { it ->
            binding.edItemName.text = Editable.Factory.getInstance().newEditable(it.name)
            binding.edItemPrice.text = Editable.Factory.getInstance().newEditable(it.price.toString())
            binding.edItemQuantity.text = Editable.Factory.getInstance().newEditable(it.quantity.toString())
        }

        binding.btnUpdate.setOnClickListener {
            if (item != null) {
                updateItem(item.id)
            }
        }

        binding.btnDelete.setOnClickListener {
            if (item != null) {
                deleteItem(item)
            }
        }
    }

    private fun deleteItem(item: Item) {

        // Instance of the Database
        val itemDB = ItemDatabase.getDatabase(applicationContext)

        // Instance of DAO
        val itemDao = itemDB.getItemDao()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                itemDao.deleteItem(item)
                Toast.makeText(applicationContext,"Successfully Updated", Toast.LENGTH_LONG).show()
                val intent = Intent(this@ItemDetailsActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
                // Return true if the insert was successful
            } catch (e: Exception) {
                Toast.makeText(applicationContext,"Something Went Wrong while inserting", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                // Return false if there was an error
            }
        }
    }

    private fun updateItem(id : Int) {
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
        var item: Item = Item(id,name, doublePrice, intQuantity)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                itemDao.updateItem(item)
                Toast.makeText(applicationContext,"Successfully Updated", Toast.LENGTH_LONG).show()
                val intent = Intent(this@ItemDetailsActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
                // Return true if the insert was successful
            } catch (e: Exception) {
                Toast.makeText(applicationContext,"Something Went Wrong while inserting", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                // Return false if there was an error
            }
        }
    }
}