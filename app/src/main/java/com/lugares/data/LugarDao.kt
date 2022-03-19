package com.lugares.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
//import androidx.room.*
import com.lugares.model.Lugar

class LugarDao {

    private val coleccion1 = "LugaresAPP"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private val coleccion2 = "misLugares"

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    fun getLugares() : MutableLiveData<List<Lugar>> {  //el mutable es por si otro usuario con otro celular puede modificar los datos en la nube
    val listaLugares = MutableLiveData<List<Lugar>>()

        firestore.collection(coleccion1).document(usuario).collection(coleccion2).addSnapshotListener{ instantanea, e -> //intantanea es una foto o una recuperacion y la e es una expecion
            if(e != null){ //validamos si hay un error recuperando la info
                return@addSnapshotListener
            }
            if(instantanea != null){ //Si hay informacion recuperada entoncces
                val lista = ArrayList<Lugar>()

                //Recorro los documentos de la ruta
                instantanea.documents.forEach{
                    val lugar = it.toObject(Lugar::class.java)
                    if(lugar != null){ //Si se pudo convertir el documento e un lugar
                        lista.add(lugar) //se agrega el lugar a la lista temporal
                    }
                }
                listaLugares.value = lista
            }
        }

        return listaLugares
    }

    suspend fun saveLugar(lugar : Lugar){
        val documento : DocumentReference

        if(lugar.id.isEmpty()){ //Si id esta vacio es un documento nuevo
            documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document()
            lugar.id = documento.id
        }else{//El documento ya existe
            documento = firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id)
        }
        //Efectivamente se actualiza la informacion del lugar en firestore
        documento.set(lugar).addOnSuccessListener { Log.d("saveLugar", "Lugar agregado/modificado") }
            .addOnCanceledListener { Log.e("saveLugar","Error, Lugar no agergado/modificado") }
    }


    suspend fun deleteLugar(lugar: Lugar){
    //Valida si el lugar existe en la coleccion (si tiene id)
     if(lugar.id.isNotEmpty()){
         firestore.collection(coleccion1).document(usuario).collection(coleccion2).document(lugar.id).delete()
             .addOnSuccessListener { Log.d("deleteLugar", "Lugar eliminado") }
             .addOnCanceledListener { Log.e("deleteLugar","Lugar no eliminado") }
        }

    }
}


/*@Dao
interface LugarDao {

    //Funcion para obtener la lista de lugares
    @Query("select * from LUGAR")
    fun getAllData() : LiveData<List<Lugar>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar : Lugar)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateLugar(lugar: Lugar)

    @Delete
    suspend fun deleteLugar(lugar: Lugar)
}*/