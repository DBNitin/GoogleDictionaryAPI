package com.example.googledictionary
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitInstance
{
    private const val Base_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private fun getInstance(): Retrofit
    {
            return Retrofit.Builder()
                .baseUrl(Base_URL) // Make sure Base_URL is defined
                .addConverterFactory(GsonConverterFactory.create())
                .build()
     }
    val dictionaryApi : DictionaryApi = getInstance().create(DictionaryApi::class.java)
}