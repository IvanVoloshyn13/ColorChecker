package com.example.changecolorsapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.changecolorsapp.views.currentcolor.CurrentColorFragment
import com.example.foundation.ActivityScopeViewModel
import com.example.foundation.navigator.StackFragmentNavigator

class MainActivity : AppCompatActivity() {
    private lateinit var navigator: StackFragmentNavigator
    private val activityViewModel by viewModels<ActivityScopeViewModel> {
        ViewModelProvider.AndroidViewModelFactory(
            application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragment_container,
            initialScreenCreator = { CurrentColorFragment.Screen() }
        )
        navigator.onCreate(savedInstanceState)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher
        return true
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.whenActivityActive.resource = this
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.whenActivityActive.resource = null
    }


}