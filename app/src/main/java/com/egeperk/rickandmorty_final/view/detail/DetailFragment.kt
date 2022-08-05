package com.egeperk.rickandmorty_final.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.egeperk.rickandmorty_final.databinding.FragmentDetailBinding
import com.egeperk.rickandmorty_final.util.Constants.PARAM_BUNDLE_IMAGE_KEY
import com.egeperk.rickandmorty_final.util.Constants.PARAM_BUNDLE_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DetailFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            val list = arguments?.getStringArrayList(PARAM_BUNDLE_KEY)
            item = list

            charImage.load(arguments?.get(PARAM_BUNDLE_IMAGE_KEY)){crossfade(true)}

            closeBtn.setOnClickListener {
                dismiss()
            }

        }.root
    }
}