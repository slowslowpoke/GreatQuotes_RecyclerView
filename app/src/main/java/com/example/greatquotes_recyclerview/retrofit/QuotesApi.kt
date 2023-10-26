package com.example.greatquotes_recyclerview.retrofit

import com.example.greatquotes_recyclerview.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("quotes")
    suspend fun getAllQuotes(): Response<List<Quote>>
}