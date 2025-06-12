package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ProfileAdapter(private val list: List<Person>) : BaseAdapter() {

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val customView = view ?: LayoutInflater.from(viewGroup?.context)
            .inflate(R.layout.custom_item_view, viewGroup, false)

        val textView = customView.findViewById<TextView>(R.id.profileNameTextView)
        textView.text = list[position].name

        Log.d("ProfileAdapter", "Rendering view at position $position with name: ${list[position].name}")

        return customView
    }
}
