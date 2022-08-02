package com.egeperk.rickandmorty_final.view.episodes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.adapter.ItemLoadStateAdapter
import com.egeperk.rickandmorty_final.adapter.pagingadapter.EpisodeAdapter
import com.egeperk.rickandmorty_final.databinding.FragmentEpisodesBinding
import com.egeperk.rickandmorty_final.ui.MainActivity
import com.egeperk.rickandmorty_final.util.Constants
import com.egeperk.rickandmorty_final.viewmodel.EpisodeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodesFragment : Fragment() {

    private val episodeViewModel by viewModel<EpisodeViewModel>()
    private lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentEpisodesBinding.inflate(inflater, container, false).apply {

            episodeAdapter = EpisodeAdapter()
            episodeRv.adapter = episodeAdapter

            val v = (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.menu_nav_bar)

            episodeRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when(newState) {
                        SCROLL_STATE_IDLE -> {
                            v?.isVisible = false
                        }
                        SCROLL_STATE_DRAGGING -> {
                            v?.isVisible = true
                        }
                    }
                }
            })

            getRemoteData()


        }.root
    }

    private fun getRemoteData() =   lifecycleScope.launch {
        episodeViewModel.getEpisodeData().collectLatest {
            episodeAdapter.submitData(it)
        }
    }

}