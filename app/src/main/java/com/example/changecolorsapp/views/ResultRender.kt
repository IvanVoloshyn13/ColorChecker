package com.example.changecolorsapp.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.changecolorsapp.R
import com.example.changecolorsapp.databinding.PartResultBinding
import com.example.foundation.model.Resource
import com.example.foundation.views.BaseFragment

fun <T> BaseFragment.renderSimpleResult(
    root: ViewGroup,
    result: Resource<T>,
    onSuccess: (T) -> Unit
) {
    val binding = PartResultBinding.bind(root)
    renderResult(root = root,
        result = result,
        onLoading = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.error_container && it.id != R.id.progress_bar }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        })
}