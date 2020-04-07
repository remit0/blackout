package com.example.blackout.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.example.blackout.R
import com.example.blackout.databinding.FragmentUserDetailBinding
import com.example.blackout.viewmodels.UserDetailViewModel

class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentUserDetailBinding
    lateinit var viewModel: UserDetailViewModel
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* application */
        val application = requireNotNull(this.activity).application

        /* binding */
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_detail, container, false)

        /* viewModel */
        viewModel = UserDetailViewModel(application)
        binding.userDetailViewModel = viewModel

        /* redirections */
        navController = this.findNavController()
        /* We have already logged in and just want to go straight to homeFragment */
        if (viewModel.hasAlreadyInputDetails() &&
            this.findNavController().graph.startDestination !=  R.id.partyHomeFragment){
            navigateToHome()
        }
        /* We come from the burger menu and want to edit details */
        if (viewModel.hasAlreadyInputDetails() &&
            this.findNavController().graph.startDestination ==  R.id.partyHomeFragment){
            autofillDetails()
        }

        /* Start Button */
        binding.startButton.setOnClickListener { onClickStart() }

        return binding.root
    }

    private fun onClickStart(){

        val weightString = binding.weightText.text.toString()
        val chipId = binding.genderChipGroup.checkedChipId

        if (viewModel.areDetailsValid(weightString, chipId)){
            viewModel.saveUserDetail(weightString, chipId)
            if (binding.ageText.text.toString().toInt() < 18){
                Toast.makeText(this.context, "Go Home Kid!", Toast.LENGTH_SHORT).show()
            } else {
                navigateToHome()
            }
        } else {
            Toast.makeText(this.context, "Fill your details!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome(){
        navController.graph.startDestination = R.id.partyHomeFragment
        (activity as MainActivity).refreshNavigation()
        navController.navigate(
            UserDetailFragmentDirections.actionUserDetailFragmentToPartyHomeFragment()
        )
    }

    private fun autofillDetails(){
        binding.weightText.setText(viewModel.getWeight().toString())
        binding.genderChipGroup.check(if (viewModel.getSex() == "M") R.id.m_chip else R.id.f_chip)
        binding.startButton.text = resources.getString(R.string.edit)
    }
}
