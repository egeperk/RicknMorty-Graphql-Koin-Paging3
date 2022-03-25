package com.egeperk.rickandmorty_final.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.adapter.*
import com.egeperk.rickandmorty_final.databinding.FilterOptionItemListBinding
import com.egeperk.rickandmorty_final.databinding.FragmentFeedBinding
import com.egeperk.rickandmorty_final.databinding.OptionRowBinding
import com.egeperk.rickandmorty_final.model.Character
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private val charAdapter by lazy { ItemAdapter() }
    private val viewModel by viewModels<FeedViewModel>()
    private lateinit var filterList: ArrayList<Character>


    //private val dialogAdapter by lazy { DialogAdapter(filterList, this) }
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
        binding.lifecycleOwner = this

/*
        binding.layoutId = R.layout.char_row
        binding.dataset = viewModel

 */
        binding.dataSet = viewModel
        viewModel.queryCharList()

        /*
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = charAdapter

         */

        /*
        charAdapter.onEndOfListReached = {
            viewModel.queryCharList()
        }

         */

        binding.loadMore = object : RecyclerViewBindings.OnLoadMoreListener {
            override fun onLoadMore(listSize: Int) {
                viewModel.queryCharList()
            }
        }

        super.onViewCreated(view, savedInstanceState)
        filterList = ArrayList<com.egeperk.rickandmorty_final.model.Character>()
        filterList.add(Character("Rick", (R.drawable.ellipse1)))
        filterList.add(Character("Morty", (R.drawable.ellipse1)))
        //observeLiveData()

        binding.filterBtn.setOnClickListener(View.OnClickListener {
            createPopup()
        })
    }
/*
    private fun observeLiveData() {
        viewModel.charactersList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.ViewState.Loading -> {
                    //binding.progressBar.visibility = View.VISIBLE
                }
                is State.ViewState.Success -> {
                    if (response.value?.data?.characters?.results?.size == 0) {
                        charAdapter
                            .submitList(emptyList())
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    } else {
                        //binding.progressBar.visibility = View.VISIBLE
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

 */


    private fun createPopup() {


        dialogBuilder = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val opBinding = OptionRowBinding.inflate(layoutInflater)
        dialogBinding = FilterOptionItemListBinding.inflate(layoutInflater)

        //dialogBinding?.layoutIdDialog = R.layout.option_row
        dialogBinding?.item = filterList

        dialogBinding?.listener = object : RecyclerViewBindings.OnItemClickListener {
            override fun onItemClick(view: View, vararg data: Any) {
                Toast.makeText(context, "xxxx", Toast.LENGTH_SHORT).show()
            }

        }

        /*
       xBinding.listener = object: RecyclerViewBindings.OnItemClickListener{
           override fun onItemClick(view: View, vararg data: Any) {
               xBinding.root.setOnClickListener {
                   Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
               }
           }

       }

         */


        /*
         dialogBinding?.filterRecyclerview?.layoutManager = LinearLayoutManager(requireContext())
         dialogBinding?.filterRecyclerview?.adapter = dialogAdapter
        */

        dialogBuilder.setView(dialogBinding?.root)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun onFilterClick(selectedPosition: Int) {
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