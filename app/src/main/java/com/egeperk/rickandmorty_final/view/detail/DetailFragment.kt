package com.egeperk.rickandmorty_final.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.*
import coil.load
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.databinding.FragmentDetailBinding
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : BottomSheetDialogFragment() {

    private val charViewModel by viewModel<FeedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner


            val bundle = arguments
            charId.text = resources.getString(R.string.id,bundle?.get("id").toString())
            charName.text = bundle?.get("name").toString()
            charGender.text = resources.getString(R.string.gender,bundle?.get("gender").toString())
            charLocation.text = resources.getString(R.string.location,bundle?.get("location").toString())
            if (bundle?.get("origin").toString() == "null" || bundle?.get("origin").toString().isEmpty() ){
                charOrigin.visibility = View.GONE
            } else {
                charOrigin.text = resources.getString(R.string.origin,bundle?.get("origin").toString())
            }
            charStatus.text = resources.getString(R.string.status,bundle?.get("status").toString())
            if (bundle?.get("type").toString().isNullOrEmpty()) {
                charType.visibility = View.GONE
            } else {
                charType.text = resources.getString(R.string.type,bundle?.get("type").toString())
            }
            charCreated.text = resources.getString(R.string.created,bundle?.get("created").toString())
            val image = bundle?.get("image")
            charImage.load(image){crossfade(true)}

            closeBtn.setOnClickListener {
                dismiss()
            }

        }.root
    }

}