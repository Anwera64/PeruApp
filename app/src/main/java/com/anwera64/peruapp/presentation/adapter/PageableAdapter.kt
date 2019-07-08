package com.anwera64.peruapp.presentation.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class PageableAdapter<B, T : RecyclerView.ViewHolder> internal constructor(
    private val delegate: Delegate
) : RecyclerView.Adapter<T>() {

    private var actualPage: Int = 0
    private var isRequestingPage: Boolean = false
    private var isLastPage: Boolean = false
    protected var itemList: ArrayList<B> = ArrayList()

    val isEmpty: Boolean
        get() = itemList.isEmpty()

    init {
        this.actualPage = -1
        this.isRequestingPage = false
        this.isLastPage = false
    }

    fun update(actualPage: Int, nextPageItems: List<B>, isLastPage: Boolean) {
        if (actualPage == 0) resetAdapter()
        isRequestingPage = false
        this.isLastPage = isLastPage
        if (actualPage == this.actualPage) return
        this.actualPage = actualPage
        itemList.addAll(nextPageItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemList.size

    private fun resetAdapter() {
        this.actualPage = -1
        itemList = ArrayList()
        notifyDataSetChanged()
    }

    internal fun checkForPageEnd(i: Int) {
        if (i == itemList.size - 2 && !isRequestingPage && !isLastPage) {
            isRequestingPage = true
            delegate.requestNewPage(actualPage + 1)
        }
    }

    interface Delegate {
        fun requestNewPage(page: Int)
    }
}