package com.example.greatquotes_recyclerview

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greatquotes_recyclerview.databinding.ActivityMainBinding
import com.example.greatquotes_recyclerview.retrofit.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val TAG = "GreatQuotesApp"
    private lateinit var binding: ActivityMainBinding
    private var quotesList: MutableList<Quote> = mutableListOf()
    private lateinit var quotesAdapter: QuotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.isVisible = false
        setupRecyclerView()
        lifecycleScope.launch {

            binding.progressBar.isVisible = true

            val retrofitResponse = try {
                RetrofitInstance.api.getAllQuotes()
            } catch (e: IOException) {
                Log.d(TAG, "IOException, check your internet")
                binding.progressBar.isVisible = false
                return@launch
            } catch (e: HttpException) {
                Log.d(TAG, "HttpException, request unsuccessfull")
                binding.progressBar.isVisible = false
                return@launch
            }

            if (retrofitResponse.isSuccessful) {
                binding.progressBar.isVisible = false
                quotesList.addAll(retrofitResponse.body()!!)
                quotesAdapter.differ.submitList(quotesList)
            } else Toast.makeText(
                this@MainActivity,
                "Wrong response from server",
                Toast.LENGTH_SHORT
            ).show()

        }

        binding.btnAddQuote.setOnClickListener { addQuote() }
        quotesAdapter.setOnItemClickListener {deleteQuote(it) }

    }


    private fun setupRecyclerView() {
        quotesAdapter = QuotesAdapter()
        binding.rvQuotesList.apply {
            adapter = quotesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


    private fun addQuote() {
        val newQuoteText = binding.edQuote.text.toString()
        if (newQuoteText == "") return
        val newQuoteAuthor = "Amina, the smartest philosopher"
        val newQuote = Quote(newQuoteText, newQuoteAuthor, "", "")
        quotesList.add(newQuote)
        quotesAdapter.differ.submitList(quotesList)
        binding.rvQuotesList.scrollToPosition(quotesList.size - 1)
        binding.edQuote.setText("")

    }


    private fun deleteQuote(quote: Quote) {
        AlertDialog.Builder(this)
            .setMessage("Delete this deep quote by ${quote.a}?")
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                val index = quotesList.indexOf(quote)
                Log.d(TAG, "Index of the quote: $index")

                val newList = quotesList.toMutableList().apply {
                    remove(quote)
                }
                quotesAdapter.differ.submitList(newList)
            }
            .create().show()
    }

}