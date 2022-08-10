package com.egeperk.rickandmorty_final.view.episodes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.adapter.ItemLoadStateAdapter
import com.egeperk.rickandmorty_final.adapter.pagingadapter.GenericPagingDataAdapter
import com.egeperk.rickandmorty_final.databinding.FragmentEpisodesBinding
import com.egeperk.rickandmorty_final.ui.MainActivity
import com.egeperk.rickandmorty_final.util.bottomBarScrollState
import com.egeperk.rickandmorty_final.util.hasInternetConnection
import com.egeperk.rickandmorty_final.viewmodel.MainViewModel
import com.example.rnm_mvvm.EpisodesQuery
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EpisodesFragment : Fragment() {

    private val episodeViewModel by sharedViewModel<MainViewModel>()
    private var episodeAdapter: GenericPagingDataAdapter<EpisodesQuery.Result>? = null
    private lateinit var binding: FragmentEpisodesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEpisodesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            val v =
                (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.menu_nav_bar)
            v?.let { episodeRv.bottomBarScrollState(it) }
        }
        setupRv()

        getRemoteData()

        return binding.root
    }

    private fun getRemoteData() {

        if (activity?.hasInternetConnection() == true) {

            episodeAdapter?.submitData(lifecycle,episodeViewModel.episodeResult.value)

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
                        getRemoteData()
                    }
                }
            }
        }
    }

        private fun setupRv() {

            episodeAdapter = GenericPagingDataAdapter(R.layout.episode_item_row) {}
            binding.episodeRv.adapter =
                episodeAdapter?.withLoadStateFooter(footer = ItemLoadStateAdapter { episodeAdapter?.retry() })

            episodeAdapter?.addLoadStateListener { combinedLoadStates ->
                if (combinedLoadStates.source.append is LoadState.Loading) {
                    binding.loadingLy.apply {
                        isVisible = true
                        bringToFront()
                    }
                } else {
                    binding.loadingLy.isVisible = false
                }
            }
        }
    }