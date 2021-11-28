package by.vlfl.campos.presentation.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    private val profileModel by lazy { intent.extras?.getParcelable<ProfileModel>(PROFILE_MODEL_KEY) }

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
        mainNavController.setGraph(R.navigation.nav_graph_main, intent.extras)
    }

    private fun setupBottomNavigation() {
        with(binding.mainBottomNav) {
            setupWithNavController(mainNavController)

            setOnItemReselectedListener {}

            setOnItemSelectedListener { item ->
                return@setOnItemSelectedListener when (item.itemId) {
                    R.id.action_profile -> {
                        mainNavController.navigate(NavGraphMainDirections.navigateToProfileFragment(profileModel!!))
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
        const val PROFILE_MODEL_KEY = "model"

        fun create(context: Context, model: ProfileModel): Intent = Intent(context, MainActivity::class.java).apply {
            putExtra(PROFILE_MODEL_KEY, model)
        }
    }
}