package com.example.flippcardapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.flippcardapp.databinding.ActivityMainBinding
import com.example.flippcardapp.ui.FlashcardViewModel
import com.example.flippcardapp.util.adjustForNightMode
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: FlashcardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.listFragment, R.id.quizFragment, R.id.addFlashcardFragment -> {
                    viewModel.currentSet.value?.let { set ->
                        updateAppBar(set.name, set.color)
                    } ?: resetAppBar()
                }
                else -> {
                    resetAppBar()
                }
            }
        }

        viewModel.currentSet.observe(this) { set ->
            val currentDestinationId = navController.currentDestination?.id
            if ((currentDestinationId == R.id.listFragment || 
                 currentDestinationId == R.id.quizFragment || 
                 currentDestinationId == R.id.addFlashcardFragment) && set != null) {
                updateAppBar(set.name, set.color)
            }
        }
    }

    private fun updateAppBar(name: String, color: Int) {
        binding.toolbar.title = name
        
        val adjustedColor = color.adjustForNightMode(this)
        
        val params = binding.appBarLayout.layoutParams
        params.height = resources.getDimensionPixelSize(R.dimen.app_bar_expanded_height)
        binding.appBarLayout.layoutParams = params

        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setBottomLeftCornerSize(resources.getDimension(R.dimen.app_bar_corner_radius))
            .setBottomRightCornerSize(resources.getDimension(R.dimen.app_bar_corner_radius))
            .build()

        binding.appBarLayout.background = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(adjustedColor)
        }
        binding.appBarLayout.backgroundTintList = null

        val isLight = ColorUtils.calculateLuminance(adjustedColor) > 0.5
        val contentColor = if (isLight) Color.BLACK else Color.WHITE
        binding.toolbar.setTitleTextColor(contentColor)
        binding.toolbar.navigationIcon?.setTint(contentColor)

        // Dostosowanie ikon paska stanu do koloru tła
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
    }

    private fun resetAppBar() {
        val params = binding.appBarLayout.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.appBarLayout.layoutParams = params

        binding.appBarLayout.background = null
        val primaryColor = com.google.android.material.color.MaterialColors.getColor(binding.appBarLayout, com.google.android.material.R.attr.colorSurface)
        binding.appBarLayout.backgroundTintList = ColorStateList.valueOf(primaryColor)

        val onSurfaceColor = com.google.android.material.color.MaterialColors.getColor(binding.appBarLayout, com.google.android.material.R.attr.colorOnSurface)
        binding.toolbar.setTitleTextColor(onSurfaceColor)
        binding.toolbar.navigationIcon?.setTint(onSurfaceColor)

        // Resetowanie paska stanu do domyślnego trybu jasny/ciemny motyw
        val isNightMode = resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK == 
                android.content.res.Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !isNightMode
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
