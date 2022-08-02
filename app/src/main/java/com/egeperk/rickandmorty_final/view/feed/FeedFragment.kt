package com.egeperk.rickandmorty_final.view.feed

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.egeperk.rickandmorty_final.adapter.*
import com.egeperk.rickandmorty_final.adapter.pagingadapter.FilterAdapter
import com.egeperk.rickandmorty_final.adapter.pagingadapter.CharacterAdapter
import com.egeperk.rickandmorty_final.databinding.FilterOptionItemListBinding
import com.egeperk.rickandmorty_final.databinding.FragmentFeedBinding
import com.egeperk.rickandmorty_final.model.Character
import com.egeperk.rickandmorty_final.model.CharacterProvider
import com.egeperk.rickandmorty_final.ui.MainActivity
import com.egeperk.rickandmorty_final.util.Constants.EMPTY
import com.egeperk.rickandmorty_final.util.Constants.MORTY
import com.egeperk.rickandmorty_final.util.Constants.MORTY_POSITION
import com.egeperk.rickandmorty_final.util.Constants.RICK
import com.egeperk.rickandmorty_final.util.Constants.RICK_POSITION
import com.egeperk.rickandmorty_final.util.Constants.SELECTED_POSITION
import com.egeperk.rickandmorty_final.util.ThemePreferences
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private val charViewModel by viewModel<FeedViewModel>()
    private lateinit var filterList: ArrayList<Character>
    private var charAdapter: CharacterAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = charViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPreferences()

        charAdapter = CharacterAdapter()

        observeData()

        filterList = CharacterProvider.provideCharacter()

        binding.apply {
            recyclerView.adapter = charAdapter?.withLoadStateFooter(
                footer = ItemLoadStateAdapter { charAdapter?.retry() }
            )
            recyclerView.setHasFixedSize(true)
            filterBtn.setOnClickListener {
                createPopup()
            }
            themeBtn.setOnClickListener {
                setMode()
            }
            searchEt.doOnTextChanged { _, _, _, _ ->
                searchItem()
            }
        }
    }

    private fun observeData() {
        if (hasInternetConnection()){
            lifecycleScope.launch {
                binding.recyclerView.isVisible = true
                charViewModel.getData(EMPTY).collectLatest {
                    charAdapter?.submitData(it)
                }
            }
            binding.apply {
                noConnectionTv.isVisible = false
                loadStateRetry.isVisible = false
            }
        } else {
            binding.apply {
                noConnectionTv.isVisible = true
                loadStateRetry.apply {
                    isVisible = true
                    setOnClickListener {
                        observeData()
                    }
                }
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
        }.show()

        var selectedPosition = SELECTED_POSITION

        val filterAdapter = FilterAdapter(object : FilterAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(position: Int) {

                filterList[position].isSelected = !filterList[position].isSelected

                if (!filterList[RICK_POSITION].isSelected && !filterList[MORTY_POSITION].isSelected) {
                    lifecycleScope.launch {
                        charViewModel.getData("").collectLatest {
                            charAdapter?.apply {
                                submitData(PagingData.empty())
                                submitData(it)
                            }
                        }
                    }
                }

                if (position == RICK_POSITION && filterList[RICK_POSITION].isSelected) {
                    filterList[MORTY_POSITION].isSelected = false
                    lifecycleScope.launch {
                        charViewModel.getData(RICK).collectLatest {
                            charAdapter?.apply {
                                submitData(PagingData.empty())
                                submitData(it)
                            }
                        }
                    }
                }

                if (position == MORTY_POSITION && filterList[MORTY_POSITION].isSelected) {
                    filterList[RICK_POSITION].isSelected = false
                    lifecycleScope.launch {
                        charViewModel.getData(MORTY).collectLatest {
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

    private fun checkPreferences() {
        when (ThemePreferences(requireContext()).darkMode) {
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

    private fun searchItem() {
        charViewModel.search.observe(viewLifecycleOwner) { text ->
            lifecycleScope.launch {
                charViewModel.getData(text).collectLatest {
                    charAdapter?.submitData(PagingData.from(emptyList()))
                    charAdapter?.submitData(it)
                    if (text == EMPTY) {
                        charAdapter?.submitData(PagingData.from(emptyList()))
                        charAdapter?.submitData(it)
                    }
                }

            }
        }

    }

    private fun hasInternetConnection() : Boolean {
        val connectivityManager = activity?.application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val hasConnection = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(hasConnection) ?: return false

        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}