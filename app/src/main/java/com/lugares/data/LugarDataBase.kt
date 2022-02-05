package com.lugares.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lugares.model.Lugar
import java.security.AccessControlContext
import kotlin.contracts.Returns


@Database(entities = [Lugar::class], version = 1, exportSchema = false)
//aqui se crea la bd o se crea la conexion
abstract class LugarDataBase : RoomDatabase() {
    abstract fun lugarDao() : LugarDao

    //companion object es una serie de instrucciones que se definen una unica vez, tipo static en java
    companion object{
        @Volatile //esta anotacion es para decir que la bd va a existir y tiene acceso directo a la bd y si no esta creada es nula
        private var INSTANCE: LugarDataBase? = null

        fun getDatBase(context: android.content.Context) : LugarDataBase{   //devuelve la bd
            var instance = INSTANCE
            if (instance != null){
                return instance
            }
             synchronized(this){   //se sincroniza para evitar intererrores
                 //se crea la bd
                 val basedatos = Room.databaseBuilder(
                     context.applicationContext,
                     LugarDataBase::class.java,
                      "Lugar_database"
                 ).build()
                 INSTANCE = basedatos
                 return basedatos
             }
        }
    }

}