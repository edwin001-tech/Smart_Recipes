package com.tdbsoftrecipesappv1.smartrecipe.ui.howItWorks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tdbsoftrecipesappv1.smartrecipe.R

class HowItWorks : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.how_it_works_fragment, container, false)

    }



}