package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET

interface MovieApiInterface {

    @GET("/3/movie/popular?api_key=8c23d8f7de35b3c32e59b2a4ad697fc1")
    fun getMovieList(): Call<MovieResponse>
}

