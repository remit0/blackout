package com.example.blackout.ui


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.blackout.R
import com.example.blackout.database.PartyDatabase
import com.example.blackout.database.PartyImage
import com.example.blackout.databinding.FragmentDetailPartyBinding
import com.example.blackout.utils.PICTURE_INTENT_REQUEST_CODE
import com.example.blackout.utils.PictureHandler
import com.example.blackout.adapters.ImageAdapter
import com.example.blackout.adapters.ImageItemListener
import com.example.blackout.viewmodels.PartyDetailViewModel
import com.example.blackout.factories.PartyDetailViewModelFactory


const val INTENT_PICTURE_EXTRA_KEY = "uri"


class PartyDetailFragment : Fragment() {

    /* Hack? */
    lateinit var uri: String

    /* View model */
    lateinit var detailViewModel: PartyDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /* Application reference for accessing database */
        val application = requireNotNull(this.activity).application

        /* Data source */
        val dataSource = PartyDatabase.getInstance(application).partyDatabaseDao

        /* Data binding object */
        val binding: FragmentDetailPartyBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail_party, container, false)

        /* Get safeargs arguments */
        val args = PartyDetailFragmentArgs.fromBundle(arguments!!)

        /* ViewModel interactions & hook-up binding */
        val viewModelFactory =
            PartyDetailViewModelFactory(
                dataSource,
                application,
                args.partyId
            )
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(PartyDetailViewModel::class.java)
        binding.partyViewModel = detailViewModel
        binding.lifecycleOwner = this

        /* Add support for the name button (couldn't make it work with on click lambdas ?) */
        binding.nameModifyButton.setOnClickListener {
            detailViewModel.editName(binding.nameEditText.text.toString())
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }

        /* Take Picture */
        binding.imageButton.setOnClickListener {
            val pictureHandler = PictureHandler(
                detailViewModel.party.value!!.partyId, this.context!!, (this.activity as MainActivity)
            )
            val intent = pictureHandler.getPictureIntent()
            uri = pictureHandler.imageUri.toString()
            startActivityForResult(intent, PICTURE_INTENT_REQUEST_CODE)

        }

        /* recycler view */
        val manager = GridLayoutManager(activity, 2)
        binding.imageList.layoutManager = manager
        val adapter =
            ImageAdapter(ImageItemListener { image ->
                navigateToImageFullSize(image)
            })

        binding.imageList.adapter = adapter
        detailViewModel.images.observe(viewLifecycleOwner, Observer {
            it?.let{adapter.submitList(it)}
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICTURE_INTENT_REQUEST_CODE && resultCode == RESULT_OK){
            detailViewModel.insertPicture(detailViewModel.party.value!!.partyId, uri)
            detailViewModel.updateImages()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToImageFullSize(image: PartyImage){
        this.findNavController().navigate(PartyDetailFragmentDirections.
            actionPartyDetailFragmentToFullSizeImageFragment(image.partyId, image.uri))
    }
}
