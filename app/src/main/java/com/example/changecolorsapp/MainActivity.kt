package com.example.changecolorsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.changecolorsapp.views.currentcolor.CurrentColorFragment
import com.example.foundation.ActivityScopeViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.StackFragmentNavigator
import com.example.foundation.uiactions.AndroidUiActionsImpl
import com.example.foundation.util.viewModelCreator
import com.example.foundation.views.FragmentsHolder

class MainActivity : AppCompatActivity(), FragmentsHolder {
    private lateinit var navigator: StackFragmentNavigator
    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUiActionsImpl(applicationContext),
            navigator = IntermediateNavigator()
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
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null)
    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }

    override fun notifyScreenUpdates() {
        navigator.notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }


}