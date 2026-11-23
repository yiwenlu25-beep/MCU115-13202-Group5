package com.example.telephonebookapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.telephonebookapp.databinding.ItemContactBinding

class ContactAdapter(
    private var contactList: MutableList<Contact>,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.binding.tvName.text = contact.name
        holder.binding.tvPhone.text = contact.phone
        holder.binding.tvEmail.text = contact.email

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(contact)
        }
    }

    override fun getItemCount(): Int = contactList.size

    fun updateData(newList: List<Contact>) {
        contactList = newList.toMutableList()
        notifyDataSetChanged()
    }
}