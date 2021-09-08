 package com.example.domashkareborn.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.domashkareborn.R
import org.koin.android.viewmodel.ext.android.viewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domashkareborn.databinding.LoginFragmentBinding
import com.example.domashkareborn.screen.MainActivity

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var goToRegisterBTN:Button
    lateinit var loginBTN:Button

    lateinit var classCodeET: EditText
    lateinit var moderatorCodeET: EditText


    private val viewModel:LoginViewModel by viewModel()
    private val binding: LoginFragmentBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        return view
    }

    public fun onClickLogin(){
        viewModel.login(classCodeET.text.toString(), moderatorCodeET.text.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classCodeET = view.findViewById(R.id.classCodeET)
        moderatorCodeET = view.findViewById(R.id.moderatorCodeET)
        loginBTN = view.findViewById(R.id.loginBTN)

        goToRegisterBTN = view.findViewById(R.id.goToRegisterBTN)
        goToRegisterBTN.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registrationFragment)
        })

        loginBTN.setOnClickListener(View.OnClickListener {
            onClickLogin()
        })

        viewModel.isLogined.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it == true) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()

                }
            }
        })

        viewModel.isLogining.observe(viewLifecycleOwner){
            if (it){
                goToRegisterBTN.isEnabled = false
                loginBTN.isEnabled = false
                classCodeET.isEnabled = false
                moderatorCodeET.isEnabled = false
            }else{
                goToRegisterBTN.isEnabled = true
                loginBTN.isEnabled = true
                classCodeET.isEnabled = true
                moderatorCodeET.isEnabled = true
            }
        }

    }

}