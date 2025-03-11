package com.example.cookigo.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.cookigo.R
import com.example.cookigo.databinding.FragmentInstructionsBinding
import com.example.cookigo.models.Result
import com.example.cookigo.util.Constants
import com.example.cookigo.util.retrieveParcelable


class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args!!.retrieveParcelable(Constants.RECIPE_RESULT_KEY) as Result?

        if(myBundle != null){
            binding.instructionWebView.webViewClient = object : WebViewClient(){}
            val websiteUrl: String = myBundle.sourceUrl ?: ""
            binding.instructionWebView.loadUrl(websiteUrl)
        }


        return binding.root
    }

}