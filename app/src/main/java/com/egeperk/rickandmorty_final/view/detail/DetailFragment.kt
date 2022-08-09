package com.egeperk.rickandmorty_final.view.detail

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import coil.load
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.databinding.FragmentDetailBinding
import com.egeperk.rickandmorty_final.util.Constants.PARAM_BUNDLE_IMAGE_KEY
import com.egeperk.rickandmorty_final.util.Constants.PARAM_BUNDLE_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DetailFragment : BottomSheetDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = context?.let { BottomSheetDialog(it, theme) }
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { bottomSheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

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