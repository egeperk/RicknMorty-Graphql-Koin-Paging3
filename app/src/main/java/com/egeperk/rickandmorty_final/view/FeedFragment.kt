package com.egeperk.rickandmorty_final.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.BaseApi
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.adapter.*
import com.egeperk.rickandmorty_final.databinding.FilterOptionItemListBinding
import com.egeperk.rickandmorty_final.databinding.FragmentFeedBinding
import com.egeperk.rickandmorty_final.databinding.OptionRowBinding
import com.egeperk.rickandmorty_final.model.Character
import com.egeperk.rickandmorty_final.util.Constants.MORTY
import com.egeperk.rickandmorty_final.util.Constants.RICK
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import com.example.rnm_mvvm.CharactersQuery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private val viewModel by viewModels<FeedViewModel>()
    private lateinit var filterList: ArrayList<Character>
    private var selectedPosition = -1

    private var dialogBinding: FilterOptionItemListBinding? = null
    private var opBinding: OptionRowBinding? = null
    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private var charAdapter: PagedAdapter? = null
    private var page = 0
    private lateinit var channel: Channel<Unit>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false).apply {



        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charAdapter = PagedAdapter()
        binding.recyclerView.adapter = charAdapter?.withLoadStateFooter(
            footer = ItemLoadStateAdapter()
        )


        observeLoadState()
        //observeLiveData()
        observeData()

        filterList = ArrayList<com.egeperk.rickandmorty_final.model.Character>()
        filterList.add(Character("Rick", (R.drawable.ellipse1), false))
        filterList.add(Character("Morty", (R.drawable.ellipse1), false))

        binding.filterBtn.setOnClickListener(View.OnClickListener {
            createPopup()
        })

    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getData().collectLatest {
                charAdapter?.submitData(it)
            }
        }
    }

    private fun observeLoadState() {
        charAdapter?.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            if (loadState.refresh is LoadState.Error) {
                binding.progressBar.isVisible = true
            }
        }
    }

   /* private fun observeLiveData() {


        viewModel.charactersList.observe(viewLifecycleOwner) { response ->

            when (response) {
                is State.ViewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.ViewState.Success -> {
                    if (response.value?.data?.characters?.results?.size == 0) {
                        charAdapter
                            ?.submitList(emptyList())
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    val results = response.value?.data?.characters?.results

                    results?.let {
                        charAdapter?.submitList(viewModel.characters)
                        page = response.value.data?.characters?.info?.next!!
                    }




                    binding.progressBar.visibility = View.GONE
                }
                is State.ViewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    charAdapter?.submitList(emptyList())
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }

    }*/

    private fun createPopup() {

        dialogBuilder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        dialogBinding = FilterOptionItemListBinding.inflate(layoutInflater)
        dialogBinding?.item = filterList
        opBinding = OptionRowBinding.inflate(layoutInflater)
        val optionAdapter = OptionAdapter(object : OptionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()

                filterList[position].isSelected = !filterList[position].isSelected

                if (!filterList[0].isSelected && !filterList[1].isSelected) {
                    viewModel.apply {
                        //charAdapter?.submitList(emptyList())
                        //charAdapter?.notifyDataSetChanged()
                        //queryCharList(page, "")
                    }
                }

                if (position == 0 && filterList[0].isSelected) {
                    filterList[1].isSelected = false
                    viewModel.apply {
                        //charAdapter?.submitList(emptyList())
                        //charAdapter?.notifyDataSetChanged()
                        //queryCharList(page, RICK)
                    }
                }

                if (position == 1 && filterList[1].isSelected) {
                    filterList[0].isSelected = false
                    viewModel.apply {
                        //charAdapter?.submitList(emptyList())
                        //charAdapter?.notifyDataSetChanged()
                        //queryCharList(page, MORTY)
                    }
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }

                if (position != selectedPosition) {
                    selectedPosition = position
                    dialogBinding?.filterRecyclerview?.adapter?.notifyDataSetChanged()
                } else {
                    selectedPosition = -1
                    dialogBinding?.filterRecyclerview?.adapter?.notifyDataSetChanged()
                }
            }

        })
        dialogBinding?.filterRecyclerview?.adapter = optionAdapter
        optionAdapter.submitList(filterList)

        dialogBuilder.setView(dialogBinding?.root)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}