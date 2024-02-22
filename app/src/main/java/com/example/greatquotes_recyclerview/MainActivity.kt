package com.example.greatquotes_recyclerview

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greatquotes_recyclerview.databinding.ActivityMainBinding
import com.example.greatquotes_recyclerview.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val TAG = "GreatQuotesApp"
    private lateinit var binding: ActivityMainBinding
    private lateinit var quotesAdapter: QuotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        binding.progressBar.isVisible = false
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
                val newQuotesList = retrofitResponse.body()!!
                quotesAdapter.quotesList.addAll(newQuotesList)
                quotesAdapter.differ.submitList(newQuotesList.toList())
            } else Toast.makeText(
                this@MainActivity,
                "Wrong response from server",
                Toast.LENGTH_SHORT
            ).show()

        }
        binding.btnAddQuote.setOnClickListener { addQuote() }
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

        with(quotesAdapter) {
            quotesList.add(newQuote)
            differ.submitList(quotesList.toList())
            binding.rvQuotesList.scrollToPosition(differ.currentList.size - 1)
        }
        binding.edQuote.setText("")

    }
}