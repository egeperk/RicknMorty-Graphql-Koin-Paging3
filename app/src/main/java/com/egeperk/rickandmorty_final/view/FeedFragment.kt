package com.egeperk.rickandmorty_final.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.egeperk.rickandmorty_final.adapter.*
import com.egeperk.rickandmorty_final.databinding.FilterOptionItemListBinding
import com.egeperk.rickandmorty_final.databinding.FragmentFeedBinding
import com.egeperk.rickandmorty_final.model.Character
import com.egeperk.rickandmorty_final.model.CharacterProvider
import com.egeperk.rickandmorty_final.util.Constants.EMPTY
import com.egeperk.rickandmorty_final.util.Constants.MORTY
import com.egeperk.rickandmorty_final.util.Constants.RICK
import com.egeperk.rickandmorty_final.util.Constants.SELECTED_POSITION
import com.egeperk.rickandmorty_final.util.ThemePreferences
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private val viewModel by viewModel<FeedViewModel>()
    private lateinit var filterList: ArrayList<Character>
    private var charAdapter: PagedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPreferences()

        charAdapter = PagedAdapter()
        binding.recyclerView.adapter = charAdapter?.withLoadStateFooter(
            footer = ItemLoadStateAdapter { charAdapter?.retry() }
        )
        binding.recyclerView.setHasFixedSize(true)

        observeLoadState()
        observeData()

        filterList = CharacterProvider.provideCharacter()

        binding.filterBtn.setOnClickListener {
            createPopup()
        }
        binding.themeBtn.setOnClickListener {
            setMode()
        }
        binding.searchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                searchItem()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            binding.recyclerView.isVisible = true
            viewModel.getData(EMPTY).collectLatest {
                charAdapter?.submitData(it)
            }
        }
    }

    private fun observeLoadState() {

        charAdapter?.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Error) {
                binding.loadingLy.errorText.isVisible = true
                binding.loadingLy.loadStateRetry.isVisible = true
            }
            binding.loadingLy.loadStateRetry.setOnClickListener {
                charAdapter?.retry()
            }
            if (loadState.refresh !is LoadState.Error) {
                binding.loadingLy.apply {
                    loadStateRetry.isVisible = false
                    errorText.isVisible = false
                }
            }
            if (loadState.refresh !is LoadState.Error && loadState.refresh !is LoadState.Loading) {
                /*binding.shimmerView.apply {
                    isVisible = false
                    stopShimmer()
                }*/
            }
        }
    }

    private fun createPopup() {

        val dialogBinding =
            FilterOptionItemListBinding.inflate(LayoutInflater.from(context)).apply {
                item = filterList
            }
        AlertDialog.Builder(context).setView(dialogBinding.root).create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
        }
        var selectedPosition = SELECTED_POSITION

        val filterAdapter = FilterAdapter(object : FilterAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(position: Int) {

                filterList[position].isSelected = !filterList[position].isSelected

                if (!filterList[0].isSelected && !filterList[1].isSelected) {
                    lifecycleScope.launch {
                        viewModel.getData("").collectLatest {
                            charAdapter?.apply {
                                submitData(PagingData.empty())
                                submitData(it)
                            }
                        }
                    }
                }

                if (position == 0 && filterList[0].isSelected) {
                    filterList[1].isSelected = false
                    lifecycleScope.launch {
                        viewModel.getData(RICK).collectLatest {
                            charAdapter?.apply {
                                submitData(PagingData.empty())
                                submitData(it)
                            }
                        }
                    }
                }

                if (position == 1 && filterList[1].isSelected) {
                    filterList[0].isSelected = false
                    lifecycleScope.launch {
                        viewModel.getData(MORTY).collectLatest {
                            charAdapter?.apply {
                                submitData(PagingData.empty())
                                submitData(it)
                            }
                        }
                    }
                }

                if (position != selectedPosition) {
                    selectedPosition = position
                    dialogBinding.filterRecyclerview.adapter?.notifyDataSetChanged()
                } else {
                    selectedPosition = SELECTED_POSITION
                    dialogBinding.filterRecyclerview.adapter?.notifyDataSetChanged()
                }
            }
        })
        dialogBinding.filterRecyclerview.adapter = filterAdapter.apply {
            lifecycleScope.launch {
                submitData(PagingData.from(filterList))
            }
        }

    }

    private fun setMode() {
        if (!binding.themeBtn.isActivated) {
            binding.themeBtn.isActivated = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemePreferences(requireContext()).darkMode = 1
            (activity as MainActivity?)?.delegate?.applyDayNight()
        } else {
            binding.themeBtn.isActivated = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            ThemePreferences(requireContext()).darkMode = 0
            (activity as MainActivity?)?.delegate?.applyDayNight()
        }
    }

    private fun checkPreferences(){
        when(ThemePreferences(requireContext()).darkMode){
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                (activity as MainActivity?)?.delegate?.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                (activity as MainActivity?)?.delegate?.applyDayNight()
            }
        }
    }
    private fun searchItem(){
        lifecycleScope.launch {
            viewModel.getData(binding.searchEt.text.toString()).collectLatest {
                charAdapter?.submitData(it)
            }

        }
    }
}