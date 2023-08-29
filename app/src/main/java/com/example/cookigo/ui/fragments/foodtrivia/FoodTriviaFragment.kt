package com.example.cookigo.ui.fragments.foodtrivia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.cookigo.R
import com.example.cookigo.databinding.FragmentFoodTriviaBinding
import com.example.cookigo.util.Constants.Companion.API_KEY
import com.example.cookigo.util.NetworkResult
import com.example.cookigo.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodTriviaFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var _binding: FragmentFoodTriviaBinding? = null
    private val binding get() = _binding!!
    private var foodTrivia = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodTriviaBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.food_trivia_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.shareFoodTriviaMenu) {
                    val shareIntent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        this.putExtra(Intent.EXTRA_TEXT, foodTrivia)
                        this.type = "text/plain"
                    }
                    startActivity(shareIntent)
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mainViewModel.getFoodTrivia(API_KEY)
        mainViewModel.foodTriviaResponse.observe(viewLifecycleOwner) {response ->
            when(response){
                is NetworkResult.Success -> {
                    binding.foodTriviaTextView.text = response.data?.text
                    if(response.data != null){
                        foodTrivia = response.data.text
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("FoodJokeFragment", "Loading")
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}