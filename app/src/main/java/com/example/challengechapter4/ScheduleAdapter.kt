package com.example.challengechapter4

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.challengechapter4.databinding.ItemScheduleBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ScheduleAdapter(private val listStudent: List<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    class ViewHolder (val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        return ViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = listStudent[position].id.toString()
        holder.binding.tvDate.text = listStudent[position].date
        holder.binding.tvDesc.text = listStudent[position].desc
    }

    override fun getItemCount(): Int {
        return listStudent.size
    }
}