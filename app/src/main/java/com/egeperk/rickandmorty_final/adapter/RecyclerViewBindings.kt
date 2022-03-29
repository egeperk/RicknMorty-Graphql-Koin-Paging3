package com.egeperk.rickandmorty_final.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.egeperk.rickandmorty_final.BR
import com.egeperk.rickandmorty_final.R


object RecyclerViewBindings {


    interface OnItemClickListener {
        fun onItemClick(index: Int)
    }

    interface OnRecyclerItemClick {
        fun onRecyclerItemClick(selectedPosition: Int)
    }

    interface LayoutSelector {
        fun onSelectLayout(item: Any?): Int
    }

    interface ItemIdGetter {
        fun onGetItemId(data: List<*>, position: Int): Long
    }

    interface HeaderIdGetter {
        fun onGetHeaderItemId(data: List<*>, position: Int): Long
    }

    interface OnLoadMoreListener {
        fun onLoadMore(listSize: Int)
    }

    data class LoadingItem(val size: Int)

    @JvmStatic
    @BindingAdapter(
        value = [
            "entries",
            "layout",
            "onItemClick",
            "itemIdGetter",
            "lifecycleOwner",
            "numItems",
            "fastBind",
            "headerIdGetter",
            "headerLayoutId"
        ], requireAll = false
    )
    fun RecyclerView.entries(
        array: List<Any>?,
        layoutId: Int,
        listener: OnItemClickListener?,
        itemIdGetter: ItemIdGetter?,
        lifecycleOwner: LifecycleOwner?,
        numItems: Int?,
        fastBind: Boolean,
        headerIdGetter: HeaderIdGetter?,
        headerLayoutId: Int?
    ) {
        entries(
            array,
            object : LayoutSelector {
                override fun onSelectLayout(item: Any?): Int {
                    return if (item != null && item is LoadingItem) {
                        R.layout.item_loading
                    } else {
                        layoutId
                    }
                }
            },
            listener,
            itemIdGetter,
            lifecycleOwner,
            numItems,
            fastBind,
            headerIdGetter,
            headerLayoutId
        )
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "entries",
            "layout",
            "onItemClick",
            "itemIdGetter",
            "lifecycleOwner",
            "numItems",
            "fastBind",
            "headerIdGetter",
            "headerLayoutId"
        ], requireAll = false
    )
    fun RecyclerView.entries(
        array: ArrayList<Any>?,
        layoutId: Int,
        listener: OnItemClickListener?,
        itemIdGetter: ItemIdGetter?,
        lifecycleOwner: LifecycleOwner?,
        numItems: Int?,
        fastBind: Boolean,
        headerIdGetter: HeaderIdGetter?,
        headerLayoutId: Int?
    ) {
        entries(
            array,
            object : LayoutSelector {
                override fun onSelectLayout(item: Any?): Int {
                    return if (item != null && item is LoadingItem) {
                        R.layout.item_loading
                    } else {
                        layoutId
                    }
                }
            },
            listener,
            itemIdGetter,
            lifecycleOwner,
            numItems,
            fastBind,
            headerIdGetter,
            headerLayoutId
        )
    }

    @JvmStatic
    @BindingAdapter(
        value = ["entries", "layout", "onItemClick", "itemIdGetter", "lifecycleOwner", "numItems", "fastBind", "headerIdGetter", "headerLayoutId"],
        requireAll = false
    )
    fun RecyclerView.entries(
        array: List<Any>?,
        layoutSelector: LayoutSelector,
        listener: OnItemClickListener?,
        itemIdGetter: ItemIdGetter?,
        lifecycleOwner: LifecycleOwner?,
        numItems: Int?,
        fastBind: Boolean,
        headerIdGetter: HeaderIdGetter?,
        headerLayoutId: Int?
    ) {
        val a = adapter
        if (a != null && a is RecyclerViewAdapter) {
            a.setData(array)
        } else {
            adapter = RecyclerViewAdapter(
                array?.toMutableList() ?: mutableListOf(),
                layoutSelector,
                listener,
                itemIdGetter,
                lifecycleOwner,
                numItems ?: -1,
                fastBind,
                headerIdGetter,
                headerLayoutId
            )
            //addItemDecoration(StickyHeaderDecoration(adapter as? RecyclerViewAdapter))
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["entries", "layout", "onItemClick", "itemIdGetter", "lifecycleOwner", "numItems", "fastBind", "tabLayout", "tabTexts", "headerIdGetter", "headerLayoutId"],
        requireAll = false
    )
    fun ViewPager2.entries(
        array: List<Any>?,
        layoutId: Int,
        listener: OnItemClickListener?,
        itemIdGetter: ItemIdGetter?,
        lifecycleOwner: LifecycleOwner?,
        numItems: Int?,
        fastBind: Boolean,
        tabLayout: TabLayout?,
        tabTexts: List<String>?,
        headerIdGetter: HeaderIdGetter?,
        headerLayoutId: Int?
    ) {
        adapter = RecyclerViewAdapter(
            array?.toMutableList() ?: mutableListOf(),
            object : LayoutSelector {
                override fun onSelectLayout(item: Any?): Int {
                    return layoutId
                }
            },
            listener,
            itemIdGetter,
            lifecycleOwner,
            numItems ?: -1,
            fastBind,
            headerIdGetter,
            headerLayoutId
        )
        tabLayout?.let {
            TabLayoutMediator(it, this) { _, _ -> }.attach()
            tabTexts?.forEachIndexed { index, s ->
                it.getTabAt(index)?.text = s
            }
            //it.setSelectedTabToBold()
        }

    }

    internal class RecyclerViewAdapter(
        private val data: MutableList<Any>,
        private val layoutSelector: LayoutSelector,
        private val listener: OnItemClickListener?,
        private val itemIdGetter: ItemIdGetter?,
        private val viewLifecycleOwner: LifecycleOwner?,
        private var numItems: Int,
        private val fastBind: Boolean,
        private val headerIdGetter: HeaderIdGetter?,
        private val headerLayoutId: Int?,
    ) : Adapter<RecyclerView.ViewHolder>() {


        init {
            setHasStableIds(itemIdGetter != null)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == R.layout.item_loading) {
                LoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                )
            } else {

                RecyclerViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        viewType,
                        parent,
                        false
                    ),
                    listener
                )
            }
        }

        private fun getIndexFromPosition(dataSize: Int, position: Int): Int {
            return if (numItems == dataSize) position else if (position != 0) position % dataSize else position
        }

        override fun getItemViewType(position: Int): Int {
            return when (data.size) {
                0 -> layoutSelector.onSelectLayout(null)
                else -> layoutSelector.onSelectLayout(
                    data[getIndexFromPosition(
                        data.size,
                        position
                    )]
                )
            }
        }

        fun setData(data: List<Any>?) {
            /*if (itemIdGetter != null) {
                DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return itemIdGetter.onGetItemId(this@RecyclerViewAdapter.data, oldItemPosition) == itemIdGetter.onGetItemId(data.orEmpty(), newItemPosition)
                    }

                    override fun getOldListSize() = this@RecyclerViewAdapter.data.size

                    override fun getNewListSize() = data?.size ?: 0

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        println(this@RecyclerViewAdapter.data[oldItemPosition])
                        println(data.orEmpty()[newItemPosition])
                        println("areContentsTheSame: " + this@RecyclerViewAdapter.data[oldItemPosition] == data.orEmpty()[newItemPosition])
                        return this@RecyclerViewAdapter.data[oldItemPosition] == data.orEmpty()[newItemPosition]
                    }
                }).dispatchUpdatesTo(this)
            } else {
                this.data.clear()
                if (data != null) {
                    this.data.addAll(data)
                }
                notifyDataSetChanged()
            }*/

            //this.data.clear()
            if (data != null) {
                this.data.addAll(data)
            }
            notifyDataSetChanged()
        }

        fun getData() = data

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (data.size != 0 && holder is RecyclerViewHolder) {
                val index = getIndexFromPosition(data.size, position)
                holder.bind(data[index], index)

              }

        }

        override fun getItemCount(): Int {
            return if (numItems == -1) data.size else numItems
        }

        fun getActualItemCount(): Int {
            return data.size
        }

        override fun getItemId(position: Int): Long {
            return itemIdGetter?.onGetItemId(data, getIndexFromPosition(data.size, position))
                ?: super.getItemId(position)
        }

        fun removeItemAt(index: Int): Any? {
            return if (data is ArrayList) {
                val item = data.removeAt(index)
                notifyItemRemoved(index)
                item
            } else {
                null
            }
        }

        fun restoreItem(index: Int, item: Any) {
            if (data is ArrayList) {
                data.add(index, item)
                notifyItemInserted(index)
            }
        }

        inner class RecyclerViewHolder(
            private val binding: ViewDataBinding,
            private val listener: OnItemClickListener?
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: Any?, index: Int) {
                binding.setVariable(BR.index, index)
                binding.setVariable(BR.item, item)
                binding.setVariable(BR.listener, listener)
//                binding.setVariable(BR.lifecycleOwner, viewLifecycleOwner)
                binding.lifecycleOwner = viewLifecycleOwner
                if (fastBind) {
                    binding.executePendingBindings()
                }
            }
        }

        inner class StickyHeaderViewHolder(private val binding: ViewDataBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Any?) {
                binding.apply {
                    if (item !is LoadingItem) {
                        setVariable(BR.item, item)
                    }
                    lifecycleOwner = viewLifecycleOwner
                    executePendingBindings()
                }
            }
        }

        private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        fun getHeaderId(position: Int) =
            headerIdGetter?.onGetHeaderItemId(data, getIndexFromPosition(data.size, position))
                ?: super.getItemId(position)

        fun onCreateHeaderViewHolder(parent: ViewGroup?): StickyHeaderViewHolder? =
            headerLayoutId?.let {
                StickyHeaderViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(
                            parent?.context
                        ), it, parent, false
                    )
                )
            }

        fun onBindHeaderViewHolder(holder: StickyHeaderViewHolder?, position: Int) {
            if (data.size != 0 && holder is StickyHeaderViewHolder) {
                val index = getIndexFromPosition(data.size, position)
                holder.bind(data[index])
            }
        }

    }

    @JvmStatic
    @BindingAdapter("scrollPosition")
    fun RecyclerView.setScrollPosition(position: Int) {
        smoothScrollToPosition(position)
    }

    @JvmStatic
    @BindingAdapter("divider")
    fun RecyclerView.setDivider(divider: Drawable) {
        addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                val childCount = parent.childCount
                for (i in 0 until childCount) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + divider.intrinsicHeight
                    divider.setBounds(left, top, right, bottom)
                    divider.draw(c)
                }
            }

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) == 0) return
                outRect.top = divider.intrinsicHeight
            }
        })
    }

    @JvmStatic
    @BindingAdapter("snap")
    fun RecyclerView.setSnap(snap: String) {
        when (snap) {
            "linear" -> LinearSnapHelper().attachToRecyclerView(this)
            "pager" -> PagerSnapHelper().attachToRecyclerView(this)
            else -> throw IllegalArgumentException("Invalid snap type of \"$snap\". Must be either \"linear\" or \"pager\"")
        }
    }

    @JvmStatic
    @BindingAdapter("nestedScrolling")
    fun RecyclerView.nestedScrolling(enabled: Boolean) {
        isNestedScrollingEnabled = enabled
    }

    /*
    @JvmStatic
    @BindingAdapter("gravitySnap")
    fun RecyclerView.setGravitySnap(enabled: Boolean) {
        if (enabled)
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
    }

     */

    @JvmStatic
    @BindingAdapter("scrollListener")
    fun RecyclerView.setScrollListener(scrollListener: RecyclerView.OnScrollListener) {
        addOnScrollListener(scrollListener)
    }

    @JvmStatic
    @BindingAdapter("infiniteScrollLoading")
    fun RecyclerView.infiniteScrollLoading(loading: Boolean) {
        if (!loading && adapter != null) {
            val lastItem = (adapter as RecyclerViewAdapter).getData().lastOrNull()
            val listSize = adapter?.itemCount ?: 0
            if (lastItem != null && lastItem is LoadingItem && listSize > 0) {
                (adapter as RecyclerViewAdapter).removeItemAt(listSize - 1)
            }
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["onLoadMore", "infiniteScroll"], requireAll = false)
    fun RecyclerView.onLoadMoreInfiniteScroll(
        onLoadMoreListener: OnLoadMoreListener?,
        infiniteScroll: Boolean?
    ) {
        if (onLoadMoreListener != null) {
            if (infiniteScroll == false) {
                clearOnScrollListeners()
                infiniteScrollLoading(false)
            } else {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                        val listSize = adapter?.itemCount ?: 0
                        val realSize = listSize - 1
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == realSize) {
                            if (listSize > 0) {
                                val lastItem = (adapter as RecyclerViewAdapter).getData().last()
                                if (lastItem !is LoadingItem) {
                                    (adapter as RecyclerViewAdapter).restoreItem(
                                        listSize,
                                        LoadingItem(listSize)
                                    )
                                    scrollToPosition(listSize)
                                    onLoadMoreListener.onLoadMore(listSize)
                                }
                            }
                        }
                    }
                })
            }
        }
    }

}

@BindingAdapter("disableAllTouches")
fun RecyclerView.setDisableAllTouches(disableAllTouches: Boolean?) {
    if (disableAllTouches == true) {
        setOnTouchListener { _, _ -> true }
    } else {
        setOnTouchListener(null)
    }
}

