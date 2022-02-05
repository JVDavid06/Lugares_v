package com.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares.data.LugarDataBase
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    val getAllData : LiveData<List<Lugar>>
    private val repository: LugarRepositorio

    init{
        val lugarDao = LugarDataBase.getDatBase(application).lugarDao()
        repository = LugarRepositorio(lugarDao)
        getAllData = repository.getAllData
    }
    fun addLugar(lugar : Lugar){

        viewModelScope.launch(Dispatchers.IO) {repository.addLugar(lugar)}
    }

    fun updateLugar(lugar: Lugar){
        viewModelScope.launch(Dispatchers.IO) {repository.updateLugar(lugar)}
    }

    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {repository.deleteLugar(lugar)}
    }

}

