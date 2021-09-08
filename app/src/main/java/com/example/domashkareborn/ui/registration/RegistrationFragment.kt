package com.example.domashkareborn.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.domashkareborn.R
import com.example.domashkareborn.screen.LoginActivity
import com.example.domashkareborn.screen.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel


open class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var goToLoginBTN: Button
    lateinit var registerBTN: Button


    lateinit var classCodeET: EditText
    lateinit var moderatorCodeET: EditText


    private val viewModel: RegistrationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar

        (getActivity() as LoginActivity).setSupportActionBar(toolbar)
        (getActivity() as LoginActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (getActivity() as LoginActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        classCodeET = view.findViewById(R.id.classCodeET)
        moderatorCodeET = view.findViewById(R.id.moderatorCodeET)
        registerBTN = view.findViewById(R.id.registerBTN)

        registerBTN.setOnClickListener {
            onClickRegister()
        }

        goToLoginBTN = view.findViewById(R.id.goToLoginBTN)
        goToLoginBTN.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_registrationFragment_to_loginFragment)
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

        viewModel.isLogining.observe(viewLifecycleOwner) {
            if (it) {
                registerBTN.isEnabled = false
                goToLoginBTN.isEnabled = false
                classCodeET.isEnabled = false
                moderatorCodeET.isEnabled = false
            } else {
                registerBTN.isEnabled = true
                goToLoginBTN.isEnabled = true
                classCodeET.isEnabled = true
                moderatorCodeET.isEnabled = true
            }
        }
    }

    public fun onClickRegister() {
        viewModel.register(classCodeET.text.toString(), moderatorCodeET.text.toString())
    }

}