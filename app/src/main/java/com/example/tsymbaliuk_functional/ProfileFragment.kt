package com.example.tsymbaliuk_functional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.profile_fragment.view.*

class ProfileFragment : Fragment() {

    lateinit var application: MyApplication
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        progressBar = view.progress_bar

        application = MyApplication.instance

        view.sign_out_button.setOnClickListener {
            signOut()
        }

        return view
    }

    private fun signOut() {
        progressBar.visibility = View.VISIBLE
        application.auth.signOut()
        application.googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {
                findNavController().navigate(R.id.signInFragment)
            }
        progressBar.visibility = View.GONE
    }

}