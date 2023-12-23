package com.example.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.foundation.model.ErrorResource
import com.example.foundation.model.LoadingResource
import com.example.foundation.model.Resource
import com.example.foundation.model.SuccessResource
import com.example.foundation.views.activity.ActivityDelegateHolder

abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should be re-rendered
     */
    fun notifyScreenUpdates() {
        (requireActivity() as ActivityDelegateHolder).delegate.notifyScreenUpdates()
    }

    fun <T> renderResult(
        root: ViewGroup, result: Resource<T>,
        onLoading: () -> Unit,
        onError: (Exception) -> Unit,
        onSuccess: (T) -> Unit
    ) {

        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResource -> onSuccess(result.data)
            is ErrorResource -> onError(result.exception)
            is LoadingResource -> onLoading()
        }
    }
}