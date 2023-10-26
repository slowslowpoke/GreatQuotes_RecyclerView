package com.example.greatquotes_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesAdapter(var quotesList: List<Quote>): RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun getItemCount() = quotesList.size

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvAuthor).text = quotesList[position].a
        holder.itemView.findViewById<TextView>(R.id.tvQuote).text = quotesList[position].q
    }
}