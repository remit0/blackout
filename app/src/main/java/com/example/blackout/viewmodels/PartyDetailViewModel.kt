package com.example.blackout.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.blackout.database.Party
import com.example.blackout.database.PartyDatabaseDao
import com.example.blackout.database.PartyImage
import kotlinx.coroutines.*


const val PARTY_DURATION_MILLI: Long = 1000 * 3600 * 12


class PartyDetailViewModel(private val database: PartyDatabaseDao, application: Application,
                           partyId: Long)
    : AndroidViewModel(application) {

    /* Properties */
    private val app = application
    private var _party = MutableLiveData<Party>()
    private var _partyButtonVisible = MutableLiveData<Int>()
    private var _nameTextViewVisible = MutableLiveData<Int>()
    private var _nameEditViewVisible = MutableLiveData<Int>()
    private var weight: Int? = null
    private var sex: String? = null

    /* Restrict properties usage */
    val partyButtonVisible: LiveData<Int> get() = _partyButtonVisible
    val nameTextViewVisible: LiveData<Int> get() = _nameTextViewVisible
    val nameEditViewVisible: LiveData<Int> get() = _nameEditViewVisible

    /* Coroutines */
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    /* Showing on screen */
    val beerCount: LiveData<Int> = Transformations.map(_party){ party -> party?.beerCount ?: 0 }
    val wineCount: LiveData<Int> = Transformations.map(_party){ party -> party?.wineCount ?: 0 }
    val hardCount: LiveData<Int> = Transformations.map(_party){ party -> party?.hardCount ?: 0 }
    val score: LiveData<Double> = Transformations.map(_party){ party -> party?.score ?: 0.0 }
    val name: LiveData<String> = Transformations.map(_party){ party -> party?.name ?: ""}
    val party: LiveData<Party> get() = _party

    /* images support */
    private var _images = MutableLiveData<List<PartyImage>>()
    val images: LiveData<List<PartyImage>> get() = _images

    init {
        retrieveUserDetailsFromSharedPref()
        _nameTextViewVisible.value = View.VISIBLE
        _nameEditViewVisible.value = View.INVISIBLE
        initializeParty(partyId)
    }

    private fun retrieveUserDetailsFromSharedPref(){
        val sharedPref =  app.getSharedPreferences("shared", Context.MODE_PRIVATE)
        weight = sharedPref.getInt(WEIGHT_KEY, -1)
        sex = sharedPref.getString(SEX_KEY, "M")
    }

    private fun initializeParty(partyId: Long){
        uiScope.launch {
            if (partyId == -1L) {
                _party.value = getLatestPartyFromDatabase()
                if (_party.value == null) {
                    val newParty = Party()
                    _party.value = newParty
                    insert(newParty)
                }
            } else {
                _party.value = getPartyFromDatabase(partyId)
            }
            _images.value = getImagesFromDatabase(_party.value!!.partyId)
            _partyButtonVisible.value = if(isActive(_party.value!!)) View.VISIBLE else View.INVISIBLE
        }
    }

    private suspend fun getImagesFromDatabase(partyId: Long): List<PartyImage> {
        return withContext(Dispatchers.IO){
            database.getImagesFromParty(partyId)
        }
    }

    private suspend fun getPartyFromDatabase(partyId: Long): Party {
        return withContext(Dispatchers.IO){
            database.get(partyId)!!
        }
    }

    private suspend fun getLatestPartyFromDatabase():  Party? {
        return withContext(Dispatchers.IO){
            var currParty = database.getLatestParty()
            currParty?.let {
                if (!isActive(it)){
                    currParty = null
                }
            }
            currParty
        }
    }

    private fun updateParty(){
        uiScope.launch{
            val currParty: Party = _party.value!!
            update(currParty)
        }
    }

    private suspend fun insert(party: Party){
        return withContext(Dispatchers.IO){
            database.insert(party)
        }
    }

    private suspend fun update(currParty: Party){
        return withContext(Dispatchers.IO){
            database.update(currParty)
        }
    }

    private fun updateScore() {
        /*
        according to https://levinpourtous.com/comment-calculer-rapidement-votre-taux-dalcoolemie/
        */
        val sexAdjustment = if (sex == "M") 0.7 else 0.6
        val ingestedAlcohol = _party.value!!.wineCount * 13 + _party.value!!.beerCount * 24 + _party.value!!.hardCount * 16
        _party.value!!.score = ingestedAlcohol / (weight!! * sexAdjustment)
    }

    fun addBeer(){
        _party.value!!.beerCount = _party.value!!.beerCount.plus(1)
        onChangeCounters()
    }

    fun subBeer(){
        if (_party.value!!.beerCount != 0){
            _party.value!!.beerCount = (_party.value!!.beerCount).minus(1)
            onChangeCounters()
        }
    }

    fun addWine(){
        _party.value!!.wineCount = _party.value!!.wineCount.plus(1)
        onChangeCounters()
    }

    fun subWine(){
        if (_party.value!!.wineCount != 0){
            _party.value!!.wineCount = _party.value!!.wineCount.minus(1)
            onChangeCounters()
        }
    }

    fun addHard(){
        _party.value!!.hardCount = _party.value!!.hardCount.plus(1)
        onChangeCounters()
    }

    fun subHard(){
        if (_party.value!!.hardCount != 0){
            _party.value!!.hardCount = _party.value!!.hardCount.minus(1)
            onChangeCounters()
        }
    }

    private fun onChangeCounters(){
        updateScore()
        _party.value = _party.value // hack for liveData so that it triggers observers
        updateParty()
    }

    fun updateImages(){
        uiScope.launch {
            _images.value = getImagesFromDatabase(_party.value!!.partyId)
        }
    }

    fun insertPicture(partyId: Long, uriStr: String){
        uiScope.launch {
            val image = PartyImage(partyId = partyId, uri = uriStr)
            insertPictureDatabase(image)
        }
    }

    private suspend fun insertPictureDatabase(image: PartyImage){
        return withContext(Dispatchers.IO){
            database.insert(image)
        }
    }

    fun editName(name: String){
        val editViewVisibilityState = _nameEditViewVisible.value
        val textViewVisibilityState = _nameTextViewVisible.value
        _party.value!!.name = name
        _party.value = _party.value
        _nameEditViewVisible.value = textViewVisibilityState
        _nameTextViewVisible.value = editViewVisibilityState
        updateParty()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun isActive(party_: Party): Boolean {
        return ((System.currentTimeMillis() - party_.timeMilli) <= PARTY_DURATION_MILLI)
    }
}
