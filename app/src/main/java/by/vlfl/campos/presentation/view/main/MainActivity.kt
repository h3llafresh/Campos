package by.vlfl.campos.presentation.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.vlfl.campos.NavGraphMainDirections
import by.vlfl.campos.R
import by.vlfl.campos.databinding.ActivityMainBinding
import by.vlfl.campos.presentation.view.main.profile.ProfileModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val model by lazy { intent.getParcelableExtra<ProfileModel>(PROFILE_MODEL_KEY) }

    private lateinit var mainNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavController()
        setupBottomNavigation()
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_nav_host_main) as NavHostFragment
        mainNavController = navHostFragment.navController
    }

    private fun setupBottomNavigation() {
        with(binding.mainBottomNav) {
            setupWithNavController(mainNavController)

            setOnItemReselectedListener {}

            setOnItemSelectedListener { item ->
                return@setOnItemSelectedListener when (item.itemId) {
                    R.id.action_profile -> {
                        mainNavController.navigate(NavGraphMainDirections.navigateToProfileFragment())
                        true
                    }
                    R.id.action_map -> {
                        mainNavController.navigate(NavGraphMainDirections.navigateToMapFragment())
                        true
                    }
                    else -> throw NotImplementedError("Unknown bottom menu item ID")
                }
            }
        }
    }

    companion object {
        private const val PROFILE_MODEL_KEY = "PROFILE_MODEL"

        fun create(context: Context, model: ProfileModel): Intent = Intent(context, MainActivity::class.java).apply {
            putExtras(bundleOf(PROFILE_MODEL_KEY to model))
        }
    }
}