package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var movies: List<Movie>? = null
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_movies_list.layoutManager = LinearLayoutManager(this)
        rv_movies_list.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            rv_movies_list.adapter = MovieAdapter(movies, object : MovieAdapter.OnAdapterListener {
                override fun onClick(result: Movie) {
                    val intent = Intent(applicationContext, DetailMovieActivity::class.java)
                    intent.putExtra(DetailMovieActivity.EXTRA_DATA, result)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener {
            override fun onClick(result: Movie) {
                val intent = Intent(applicationContext, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_DATA, result)
                startActivity(intent)
            }
        })
        rv_movies_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                movies = response.body()!!.movies

                return callback(response.body()!!.movies)

            }
        })
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomView)
        val navController = findNavController(R.id.nav_host)
        val appAppBarConfiguration = AppBarConfiguration(setOf(R.id.favoriteFragment, R.id.movieFragment, R.id.tvShowFragment))
        setupActionBarWithNavController(navController, appAppBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)

    }
}

