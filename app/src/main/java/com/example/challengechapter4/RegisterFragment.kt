package com.example.challengechapter4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.challengechapter4.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class RegisterFragment : Fragment() {
    private var mDb: ScheduleDatabase? = null
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState:Bundle?){
        mDb = ScheduleDatabase.getInstance(requireContext())

        binding.btnRegister.setOnClickListener {
            val objectUser = User(
                null,
                binding.etUsername.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString()
            )
            GlobalScope.async {
                val result =mDb?.scheduleDao()?.addUser(objectUser)
                runBlocking(Dispatchers.Main) {
                    if (result != 0.toLong()){
                        Toast.makeText(activity, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }else{
                        Toast.makeText(activity, "Pendaftaran gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
