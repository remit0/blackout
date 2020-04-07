package com.example.blackout.ui


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.blackout.R
import com.example.blackout.database.PartyDatabase
import com.example.blackout.databinding.FragmentHomePartyBinding
import com.example.blackout.factories.PartyHomeViewModelFactory
import com.example.blackout.viewmodels.*
import com.example.blackout.adapters.PartyAdapter
import com.example.blackout.adapters.PartyItemListener

class PartyHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomePartyBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_party, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = PartyDatabase.getInstance(application).partyDatabaseDao

        val viewModelFactory =
            PartyHomeViewModelFactory(
                dataSource,
                application
            )

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PartyHomeViewModel::class.java)

        val adapter =
            PartyAdapter(PartyItemListener { partyId ->
                navigateToPartyDetail(partyId)
            })

        binding.partyHomeViewModel = viewModel

        binding.lifecycleOwner = this

        binding.partyList.adapter = adapter

        viewModel.parties.observe(viewLifecycleOwner, Observer {
            it?.let{adapter.submitList(it)}
        })

        /* navigation to add party */
        binding.addPartyButton.setOnClickListener {
            this.findNavController().navigate(
                PartyHomeFragmentDirections.actionHomeFragmentToDetailFragment(-1L)
            )
        }

        return binding.root
    }

    private fun navigateToPartyDetail(partyId: Long) {
        this.findNavController().navigate(
            PartyHomeFragmentDirections.actionHomeFragmentToDetailFragment(partyId)
        )
    }

}
