package com.example.blackout.ui


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.blackout.R
import com.example.blackout.databinding.FragmentFullSizeImageBinding

/**
 * A simple [Fragment] subclass.
 */
class FullSizeImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* Data binding object */
        val binding: FragmentFullSizeImageBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_full_size_image, container, false)

        /* Args */
        val args = FullSizeImageFragmentArgs.fromBundle(arguments!!)

        /* Set Image */
        binding.fullSizeImageView.setImageURI(Uri.parse(args.imageUriStr))

        return binding.root
    }

}
