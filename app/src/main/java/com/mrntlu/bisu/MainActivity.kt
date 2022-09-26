package com.mrntlu.bisu

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mrntlu.bisu.databinding.ActivityMainBinding
import com.mrntlu.bisu.util.Constants
import com.mrntlu.bisu.viewmodel.FavouritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private val favsViewModel: FavouritesViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var prefs: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favsViewModel.setAnonymousUser()

        val bottomNavigationView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favourites
            )
        )
        setSupportActionBar(binding.topAppbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        setObservers()
        setListeners()
        setBadgeListener()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        prefs = getSharedPreferences(Constants.THEME_PREF, 0)
        viewModel.setThemeCode(prefs.getInt(Constants.THEME_PREF, Constants.LIGHT_THEME))

        AppCompatDelegate.setDefaultNightMode(if (viewModel.isLightTheme()) MODE_NIGHT_NO else MODE_NIGHT_YES)

        return super.onCreateView(name, context, attrs)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            it.getItem(0).icon = ContextCompat.getDrawable(
                baseContext,
                if (viewModel.isLightTheme()) R.drawable.ic_dark_theme_24 else R.drawable.ic_light_theme_24
            )
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setObservers() {
        val observer = Observer<Int> {
            AppCompatDelegate.setDefaultNightMode(if (it == Constants.LIGHT_THEME) MODE_NIGHT_NO else MODE_NIGHT_YES)
            setThemePref(it)
        }

        viewModel.themeCode.observe(this, observer)
    }

    private fun setListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.topAppbar.menu.getItem(0).isVisible = destination.id != R.id.newsDetailScreen
        }

        binding.topAppbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.topAppbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.toggleTheme -> {
                    viewModel.toggleTheme()
                    it.icon = ContextCompat
                        .getDrawable(
                            baseContext,
                            if (viewModel.isLightTheme()) R.drawable.ic_dark_theme_24 else R.drawable.ic_light_theme_24
                        )
                    true
                }
                else -> false
            }
        }
    }

    private fun setBadgeListener() {
        binding.navView.getChildAt(0)
        favsViewModel.setFavsCountListener().observe(this) {
            setBadge(it)
        }
    }

    private fun setBadge(count: Int) {
        val badge = binding.navView.getOrCreateBadge(R.id.navigation_favourites)
        badge.isVisible = count > 0
        badge.number = count
    }

    private fun setThemePref(value: Int){
        val editor = prefs.edit()
        editor.putInt(Constants.THEME_PREF, value)
        editor.apply()
    }

    override fun onDestroy() {
        favsViewModel.removeListeners(this)

        if (viewModel.themeCode.hasObservers())
            viewModel.themeCode.removeObservers(this)
        super.onDestroy()
    }
}