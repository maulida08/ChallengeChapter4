package com.example.challengechapter4

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.challengechapter4.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var scheduleDatabase: ScheduleDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleDatabase = ScheduleDatabase.getInstance(requireContext())

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val login = scheduleDatabase?.scheduleDao()?.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
                runBlocking(Dispatchers.Main){
                    when {
                        binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty() -> {
                            Toast.makeText(requireContext(), "Form tidak boleh Kosong!", Toast.LENGTH_SHORT).show()
                        }
                        login == true-> {
//                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                            editor.putString("username", binding.etUsername.text.toString())
//                            editor.putString("password", binding.etPassword.text.toString())
//                            editor.apply()
                            Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
                        }
                        else -> {
                            Toast.makeText(requireContext(), "Username/Password Salah!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

}