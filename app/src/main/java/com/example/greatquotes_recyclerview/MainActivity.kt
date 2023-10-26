package com.example.greatquotes_recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greatquotes_recyclerview.databinding.ActivityMainBinding
import com.example.greatquotes_recyclerview.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "GreatQuotesApp"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var quotesList: MutableList<Quote> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.isVisible = false
        binding.rvQuotesList.adapter = QuotesAdapter(listOf())

        lifecycleScope.launch {

            binding.progressBar.isVisible = true

            val retrfofitResponse = try {
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

            if (retrfofitResponse.isSuccessful) {
                binding.progressBar.isVisible = false
                quotesList.addAll(retrfofitResponse.body()!!)
                binding.rvQuotesList.apply {
                    adapter = QuotesAdapter(quotesList)
                    layoutManager = LinearLayoutManager(this.context)
                }
            } else Toast.makeText(
                this@MainActivity,
                "Wrong response from server",
                Toast.LENGTH_SHORT
            ).show()

        }

        binding.btnAddQuote.setOnClickListener {addItem()}

    }




    private fun addItem() {
        val newQuoteText = binding.edQuote.text.toString()
        if (newQuoteText == "") return
        val newQuoteAuthor = "Amina, the smartest philosopher"
        val newQuote = Quote(newQuoteText, newQuoteAuthor, "", "")
        quotesList.add(newQuote)
        binding.rvQuotesList.adapter!!.notifyItemInserted(quotesList.size - 1)
        binding.rvQuotesList.scrollToPosition(quotesList.size - 1)
        binding.edQuote.setText("")

    }

}