package com.arrayani.repodatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arrayani.repodatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var dataStore: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.save.setOnClickListener{
            val name = binding.nameField.text.toString()
            val email  = binding.emailField.text.toString()
            val mobile = binding.phoneField.text.toString()

            //save to pref data store in ASYNC manner
            GlobalScope.launch {
                val user = UserData(name, email, mobile)
                dataStore.save(user)
            }
        }
        loadData()
    }

    private fun loadData() {
        GlobalScope.launch{
            val userDataFlow: Flow<UserData> = dataStore.getFromDataStore()
            val userData = userDataFlow.first()

            GlobalScope.launch(Dispatchers.Main){
                binding.nameField.setText(userData.name)
                binding.emailField.setText(userData.email)
                binding.phoneField.setText(userData.phoneNo)
            }
        }
    }
}