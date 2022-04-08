package com.example.challengechapter4

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.challengechapter4.databinding.AddScheduleBinding
import com.example.challengechapter4.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var scheduleDatabase: ScheduleDatabase? = null

    companion object{
        const val SHAREDFILE = "kotlinsharedreferences"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleDatabase = ScheduleDatabase.getInstance(requireContext())
        fetchData()

        binding.fabAddNote.setOnClickListener {
            val dialogBinding = AddScheduleBinding.inflate(LayoutInflater.from(requireContext()))
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setView(dialogBinding.root)
            val dialog = dialogBuilder.create()
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.btnCreate.setOnClickListener{
                val myDB = ScheduleDatabase.getInstance(requireContext())
                val noteSchedule = Schedule(
                    null,
                    dialogBinding.etSchedule.text.toString(),
                    dialogBinding.etDate.text.toString(),
                    dialogBinding.etDesc.text.toString(),
                )
                lifecycleScope.launch(Dispatchers.IO){
                    val result = myDB?.scheduleDao()?.insertSchedule(noteSchedule)
                    runBlocking(Dispatchers.Main){
                        if (result != 0.toLong()){
                            Toast.makeText(
                                requireContext(),
                                "${noteSchedule.judul} Berhasil Di tambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                            fetchData()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "${noteSchedule.judul} Gagal Di tambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                    }
                }
            }
            dialog.show()
        }

    }
    @SuppressLint("NewApi")
    private fun getDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy (HH:mm)")

        return current.format(formatter)
    }

    private fun fetchData() {
        lifecycleScope.launch(Dispatchers.IO){
            val listSchedule = scheduleDatabase?.scheduleDao()?.getAllSchedule()

            activity?.runOnUiThread {

                listSchedule?.let {
                    if (ScheduleAdapter(it, {}, {}, {}).itemCount == 0){
                        binding.recyclerView.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                    }
                    val adapter = ScheduleAdapter(
                        it,
                        detail = { schedule ->
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage(schedule.desc)
                                setTitle(schedule.judul)
                                show()
                            }
                        },
                        delete = { schedule ->
                            AlertDialog.Builder(requireContext())
                                .setPositiveButton("Iya"){_,_ ->
                                    val mDb = ScheduleDatabase.getInstance(requireContext())
                                    lifecycleScope.launch(Dispatchers.IO){
                                        val result = mDb?.scheduleDao()?.deleteSchedule(schedule)
                                        activity?.runOnUiThread {
                                            if(result != 0){
                                                Toast.makeText(
                                                    requireContext(),
                                                    "${schedule.judul} berhasil dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "${schedule.judul} gagal dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        fetchData()
                                    }
                                }
                                .setNegativeButton("Tidak"){ dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setMessage("Apakah anda yakin ingin menghapus ${schedule.judul}")
                                .setTitle("Konfirmasi Hapus")
                                .create()
                                .show()
                        },
                        update = { schedule ->
                            val sharedPreferences = requireContext().getSharedPreferences(SHAREDFILE, Context.MODE_PRIVATE)
                            val nama = sharedPreferences.getString("username", "default_username")
                            val dialogBinding = AddScheduleBinding.inflate(LayoutInflater.from(requireContext()))
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogBinding.root)
                            val dialog = dialogBuilder.create()
                            dialog.setCancelable(false)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialogBinding.tvTitle.text = getString(R.string.title)

                            dialogBinding.btnCreate.setOnClickListener{
                                val myDB = ScheduleDatabase.getInstance(requireContext())
                                val dataSchedule = Schedule(
                                    schedule.id,
                                    dialogBinding.etSchedule.text.toString(),
                                    dialogBinding.etDate.text.toString(),
                                    dialogBinding.etDesc.text.toString()
                                )
                                lifecycleScope.launch(Dispatchers.IO){
                                    val result = myDB?.scheduleDao()?.updateSchedule(dataSchedule)
                                    runBlocking(Dispatchers.Main){
                                        if (result != 0){
                                            Toast.makeText(
                                                requireContext(),
                                                "${dataSchedule.judul} Berhasil Di Update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            fetchData()
                                            dialog.dismiss()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "${dataSchedule.judul} Gagal Di update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                        }
                                    }
                                }
                            }
                            dialog.show()
                        }
                    )
                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        scheduleDatabase.destroyInstance()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}