package com.example.telephonebookapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.telephonebookapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val allContacts = mutableListOf<Contact>()
    private val displayedContacts = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter

    private val addContactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val name = data?.getStringExtra("EXTRA_NAME") ?: ""
            val phone = data?.getStringExtra("EXTRA_PHONE") ?: ""
            val email = data?.getStringExtra("EXTRA_EMAIL") ?: ""

            if (name.isNotEmpty()) {
                val newContact = Contact(name = name, phone = phone, email = email)
                allContacts.add(0, newContact)
                filterContacts(binding.etSearch.text.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generateDummyData()

        adapter = ContactAdapter(displayedContacts) { contactToDelete ->
            allContacts.remove(contactToDelete)
            filterContacts(binding.etSearch.text.toString())
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        filterContacts("")

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnGoToAdd.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            addContactLauncher.launch(intent)
        }
    }

    private fun generateDummyData() {
        allContacts.add(Contact(name = "Johnny", phone = "0988312345", email = "johnnyhong@gmail.com"))
        allContacts.add(Contact(name = "Jessica", phone = "0981911934", email = "jessicahuang@gmail.com"))

        for (i in 1..30) {
            allContacts.add(
                Contact(
                    name = "User $i",
                    phone = "09123456${String.format("%02d", i)}",
                    email = "user$i@example.com"
                )
            )
        }
    }

    private fun filterContacts(query: String) {
        val searchText = query.lowercase().trim()
        displayedContacts.clear()
        if (searchText.isEmpty()) {
            displayedContacts.addAll(allContacts)
        } else {
            allContacts.forEach { contact ->
                if (contact.name.lowercase().contains(searchText) ||
                    contact.phone.lowercase().contains(searchText) ||
                    contact.email.lowercase().contains(searchText)
                ) {
                    displayedContacts.add(contact)
                }
            }
        }
        adapter.updateData(displayedContacts)
    }
}