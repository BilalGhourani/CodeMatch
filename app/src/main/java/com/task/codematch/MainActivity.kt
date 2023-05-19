package com.task.codematch

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.task.codematch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        var isAnimatedRecyclerView: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.usersFragment, R.id.favoritesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.navView.setOnItemSelectedListener() { menuItem ->
            val currentFragment = navController.currentDestination?.id

            if (currentFragment == R.id.userDetailFragment) {
                onBackPressed()
            }
            when (menuItem.itemId) {
                R.id.usersFragment -> {
                    // Handle click on item 1
                    navController.navigate(R.id.usersFragment)
                    true
                }
                R.id.favoritesFragment -> {
                    // Handle click on item 2
                    navController.navigate(R.id.favoritesFragment)
                    true
                }
                // Add cases for other menu items
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle back button press
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun hideBottomNavigation() {
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.navView.visibility = View.VISIBLE
    }

    /* override fun onBackPressed() {
         val currentFragment = navController.currentDestination?.id

         // Check if the current fragment is Fragment C
         if (currentFragment == R.id.favoritesFragment) {
             // Navigate back to Fragment A instead of going back to Fragment C
             navController.navigate(R.id.usersFragment)
         } else {
             super.onBackPressed()
         }
     }*/
}