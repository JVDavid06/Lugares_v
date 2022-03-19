package com.lugares.model

import android.os.Parcelable
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lugar(
    var id: String,
    val nombre : String,
    val correo : String?,
    val telefono : String?,
    val web : String?,
    val latitud : Double?,
    val longitud : Double?,
    val altura : Double?,
    val rutaAudio : String?,
    val rutaImagen : String?
) : Parcelable{
    constructor():
            this("","","","","",0.0, 0.0, 0.0, "", "")
}




/*@Entity(tableName = "Lugar") //esta clase mapea una tabla de bd y se llama lugar
data class Lugar(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "nombre")
    val nombre : String,
    @ColumnInfo(name = "correo")
    val correo : String?,
    @ColumnInfo(name = "telefono")
    val telefono : String?,
    @ColumnInfo(name = "web")
    val web : String?,
    @ColumnInfo(name = "latitud")
    val latitud : Double?,
    @ColumnInfo(name = "longitud")
    val longitud : Double?,
    @ColumnInfo(name = "altura")
    val altura : Double?,
    @ColumnInfo(name = "rutaAudio")
    val rutaAudio : String?,
    @ColumnInfo(name = "rutaImagen")
    val rutaImagen : String?
) : Parcelable*/