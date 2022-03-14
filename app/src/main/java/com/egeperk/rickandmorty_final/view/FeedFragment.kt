package com.egeperk.rickandmorty_final.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.adapter.DialogAdapter
import com.egeperk.rickandmorty_final.adapter.ItemAdapter
import com.egeperk.rickandmorty_final.databinding.FilterOptionItemListBinding
import com.egeperk.rickandmorty_final.databinding.FragmentFeedBinding
import com.egeperk.rickandmorty_final.model.Character
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment(), DialogAdapter.OnItemClickListener {

    private lateinit var binding: FragmentFeedBinding
    private val charAdapter by lazy { ItemAdapter() }
    private val viewModel by viewModels<FeedViewModel>()
    private lateinit var filterList: ArrayList<Character>
    private val dialogAdapter by lazy { DialogAdapter(filterList, this) }
    private var dialogBinding: FilterOptionItemListBinding? = null
    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = charAdapter
        viewModel.queryCharList()
        charAdapter.onEndOfListReached = {
            viewModel.queryCharList()
        }
        super.onViewCreated(view, savedInstanceState)
        filterList = ArrayList<com.egeperk.rickandmorty_final.model.Character>()
        filterList.add(Character("Rick", (R.drawable.ellipse1)))
        filterList.add(Character("Morty", (R.drawable.ellipse1)))
        observeLiveData()

        binding.filterBtn.setOnClickListener(View.OnClickListener {
            createPopup()
        })
    }

    private fun observeLiveData() {
        viewModel.charactersList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.ViewState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.ViewState.Success -> {
                    if (response.value?.data?.characters?.results?.size == 0) {
                        charAdapter.submitList(emptyList())
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    val results = response.value?.data?.characters?.results
                    results?.let { charAdapter.submitList(charAdapter.currentList.plus(it)) }
                    binding.progressBar.visibility = View.GONE
                }
                is State.ViewState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    charAdapter.submitList(emptyList())
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun createPopup() {
        dialogBuilder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        dialogBinding = FilterOptionItemListBinding.inflate(layoutInflater)
        dialogBinding?.filterRecyclerview?.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding?.filterRecyclerview?.adapter = dialogAdapter
        dialogBuilder.setView(dialogBinding?.root)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onItemClick(selectedPosition: Int) {
        charAdapter.submitList(emptyList())
        viewModel.channel.trySend(Unit)
        viewModel.page = 0

        if (selectedPosition == 0) {
            viewModel.currentQuery = "Rick"
            viewModel.queryCharList()
        }
        if (selectedPosition == 1) {
            viewModel.currentQuery = "Morty"
            viewModel.queryCharList()
        }
        if (selectedPosition == 2) {
            viewModel.currentQuery = ""
            viewModel.queryCharList()
        }

    }
}