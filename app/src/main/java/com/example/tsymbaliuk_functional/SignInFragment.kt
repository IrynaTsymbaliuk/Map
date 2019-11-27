package com.example.tsymbaliuk_functional

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment : Fragment() {

    companion object {
        private const val TAG = "GoogleActivity"
        const val RC_SIGN_IN = 9001
    }

    lateinit var application: MyApplication
    lateinit var progressBar: ProgressBar
    lateinit var signInButton: SignInButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_in_fragment, container, false)

        progressBar = view.progress_bar
        signInButton = view.sign_in_button

        application = MyApplication.instance

        val currentUser = application.auth.currentUser
        updateUI(currentUser)

        view.sign_in_button.setOnClickListener {
            signIn()
        }

        return view
    }

    private fun signIn() {
        val signInIntent = application.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            Log.e(TAG, "Google sign in failed ${e.message}", e)
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        progressBar.visibility = View.VISIBLE

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        MyApplication.instance.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = MyApplication.instance.auth.currentUser
                    updateUI(user)
                } else {
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                progressBar.visibility = View.GONE
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        progressBar.visibility = View.GONE
        if (user != null) {
            signInButton.visibility = View.GONE
           findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        } else {
            signInButton.visibility = View.VISIBLE
        }
    }

}