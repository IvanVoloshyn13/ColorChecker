package com.example.changecolorsapp.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.changecolorsapp.databinding.FragmentCurrentColorBinding
import com.example.changecolorsapp.views.onTryAgain
import com.example.changecolorsapp.views.renderSimpleResult
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel


class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }
        binding.askPermissionsButton.setOnClickListener {
            viewModel.requestPermission()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }


}