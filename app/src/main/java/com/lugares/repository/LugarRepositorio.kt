package com.lugares.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Update
import com.lugares.data.LugarDao
import com.lugares.model.Lugar


class LugarRepositorio(private val lugarDao : LugarDao) {

    val getAllData : MutableLiveData<List<Lugar>> = lugarDao.getLugares()

    suspend fun addLugar(lugar : Lugar){
        lugarDao.saveLugar(lugar)
    }

    suspend fun updateLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }

    suspend fun deleteLugar(lugar: Lugar){
        lugarDao.deleteLugar(lugar)
    }
}