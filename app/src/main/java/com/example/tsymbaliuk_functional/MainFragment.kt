package com.example.tsymbaliuk_functional
/*TODO: select home fragment*/
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment: Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var application: MyApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)


        application = MyApplication.instance

        val currentUser = application.auth.currentUser
        updateUI(currentUser)

        viewPager = view.view_pager
        setupViewPager(viewPager)

        tabLayout = view.tabs
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(fragmentManager!!)
        adapter.addFragment(ListFragment(), "Список")
        adapter.addFragment(MapFragment(), "Карта")
        adapter.addFragment(ProfileFragment(), "Профиль")
        viewPager.adapter = adapter
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        } else {
            findNavController().navigate(R.id.action_mainFragment_to_signInFragment)
        }
    }

}