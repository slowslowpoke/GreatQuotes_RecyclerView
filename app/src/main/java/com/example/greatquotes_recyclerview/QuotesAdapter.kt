package com.example.greatquotes_recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.greatquotes_recyclerview.databinding.ItemQuoteBinding

class QuotesAdapter : RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder>() {
    val quotesList: MutableList<Quote> = mutableListOf()

    inner class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.apply {
                binding.tvQuote.text = quote.q
                binding.tvAuthor.text = quote.a
                binding.btnDelete.setOnClickListener {
                    showDeleteConfirmationDialog(quote)
                }
            }
        }

        private fun showDeleteConfirmationDialog(quote: Quote) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.apply {
                setMessage("Delete this smart quote by ${quote.a}?")
                setPositiveButton("Yes") { _, _ ->
                    deleteQuote(quote)
                }
                setNegativeButton("Cancel") { dialog, _ ->
                }
                show()
            }
        }

    }


    fun deleteQuote(quote: Quote) {
        quotesList.remove(quote)
        differ.submitList(quotesList.toList())
    }

    private val differCallback = object : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.q == newItem.q
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuoteViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = differ.currentList[position]
        holder.bind(quote)
    }
}