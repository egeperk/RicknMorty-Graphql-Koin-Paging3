package com.egeperk.rickandmorty_final.view.feed

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.adapter.*
import com.egeperk.rickandmorty_final.adapter.pagingadapter.GenericPagingDataAdapter
import com.egeperk.rickandmorty_final.databinding.FilterOptionItemListBinding
import com.egeperk.rickandmorty_final.databinding.FragmentFeedBinding
import com.egeperk.rickandmorty_final.model.Character
import com.egeperk.rickandmorty_final.model.CharacterProvider
import com.egeperk.rickandmorty_final.ui.MainActivity
import com.egeperk.rickandmorty_final.util.Constants.EMPTY
import com.egeperk.rickandmorty_final.util.Constants.MORTY
import com.egeperk.rickandmorty_final.util.Constants.POS1
import com.egeperk.rickandmorty_final.util.Constants.RICK
import com.egeperk.rickandmorty_final.util.Constants.POS0
import com.egeperk.rickandmorty_final.util.Constants.SELECTED_POSITION
import com.egeperk.rickandmorty_final.util.ThemePreferences
import com.egeperk.rickandmorty_final.util.bottomBarScrollState
import com.egeperk.rickandmorty_final.util.hasInternetConnection
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import com.example.rnm_mvvm.CharactersQuery
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private val charViewModel by viewModel<FeedViewModel>()
    private lateinit var filterList: ArrayList<Character>
    private var charAdapter: GenericPagingDataAdapter<CharactersQuery.Result>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = charViewModel
            fragment = this@FeedFragment


            val v =
                (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.menu_nav_bar)
            v?.let { recyclerView.bottomBarScrollState(it) }

            filterList = CharacterProvider.provideCharacter()

        }
        checkPreferences()
        setupRv()
        observeData()

        return binding.root
    }

    private fun setupRv() {

        charAdapter = GenericPagingDataAdapter(R.layout.char_row) {
            val bundle = Bundle()
            bundle.apply {
                putString("id", charAdapter?.snapshot()?.items?.get(it)?.id)
                putString("name", charAdapter?.snapshot()?.items?.get(it)?.name)
                putString("location", charAdapter?.snapshot()?.items?.get(it)?.location?.name)
                putString("image", charAdapter?.snapshot()?.items?.get(it)?.image)
                putString("status", charAdapter?.snapshot()?.items?.get(it)?.status)
                putString("gender", charAdapter?.snapshot()?.items?.get(it)?.gender)
                putString("origin", charAdapter?.snapshot()?.items?.get(it)?.origin?.dimension)
                putString("type", charAdapter?.snapshot()?.items?.get(it)?.type)
                putString("created", charAdapter?.snapshot()?.items?.get(it)?.created)

            }

            charViewModel.name.value = charAdapter?.snapshot()?.items?.get(it)?.name.toString()
            findNavController().navigate(R.id.action_feedFragment2_to_detailFragment,bundle)
        }

        binding.recyclerView.apply {
            adapter = charAdapter?.withLoadStateFooter(
                footer = ItemLoadStateAdapter { charAdapter?.retry() }
            )
            setHasFixedSize(true)
        }

        charAdapter?.addLoadStateListener { loadState ->
            if (loadState.source.append is LoadState.Loading) {
                binding.apply {
                    loadingLy.apply {
                        isVisible = true
                        bringToFront()
                    }
                    searchEt.alpha = 0.4f
                }
            } else {
                binding.apply {
                    loadingLy.apply {
                        isVisible = false
                        bringToFront()
                    }
                    searchEt.alpha = 1f
                }
            }
        }
    }

    private fun observeData() {
        if (activity?.hasInternetConnection() == true) {
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

    fun createPopup() {

        val dialogBinding =
            FilterOptionItemListBinding.inflate(LayoutInflater.from(context)).apply {
                item = filterList
            }
        AlertDialog.Builder(context).setView(dialogBinding.root).create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }.show()

        var selectedPosition = SELECTED_POSITION

        val optionAdapter = GenericPagingDataAdapter<Character>(R.layout.option_row){
            filterList[it].isSelected = !filterList[it].isSelected

            if (!filterList[POS0].isSelected && !filterList[POS1].isSelected) {
                lifecycleScope.launch {
                    charViewModel.getData(EMPTY).collectLatest {
                        charAdapter?.apply {
                            submitData(PagingData.empty())
                            submitData(it)
                        }
                    }
                }
            }

            if (it == POS0 && filterList[POS0].isSelected) {
                filterList[POS1].isSelected = false
                lifecycleScope.launch {
                    charViewModel.getData(RICK).collectLatest {
                        charAdapter?.apply {
                            submitData(PagingData.empty())
                            submitData(it)
                        }
                    }
                }
            }

            if (it == POS1 && filterList[POS1].isSelected) {
                filterList[POS0].isSelected = false
                lifecycleScope.launch {
                    charViewModel.getData(MORTY).collectLatest {
                        charAdapter?.apply {
                            submitData(PagingData.empty())
                            submitData(it)
                        }
                    }
                }
            }

            if (it != selectedPosition) {
                selectedPosition = it
                dialogBinding.filterRecyclerview.adapter?.notifyDataSetChanged()
            } else {
                selectedPosition = SELECTED_POSITION
                dialogBinding.filterRecyclerview.adapter?.notifyDataSetChanged()
            }
        }

        dialogBinding.filterRecyclerview.adapter = optionAdapter.apply {
            lifecycleScope.launch {
                submitData(PagingData.from(filterList))
            }
        }

    }

    fun setMode() {
        if (!binding.themeBtn.isActivated) {
            binding.themeBtn.isActivated = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemePreferences(requireContext()).darkMode = POS1
            (activity as MainActivity?)?.delegate?.applyDayNight()
        } else {
            binding.themeBtn.isActivated = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            ThemePreferences(requireContext()).darkMode = POS0
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

    fun searchItem() {
        charViewModel.search.observe(viewLifecycleOwner) { text ->
            lifecycleScope.launch {

                charViewModel.getData(text).collectLatest {
                    charAdapter?.submitData(PagingData.from(emptyList()))
                    charAdapter?.submitData(it)
                    if (text == EMPTY) {
                        charAdapter?.submitData(PagingData.from(emptyList()))
                        charAdapter?.submitData(it)
                    }
                    if (charAdapter?.snapshot()?.items?.size == 0) {
                        charAdapter?.submitData(PagingData.from(emptyList()))
                    }
                }
            }
        }
    }
}