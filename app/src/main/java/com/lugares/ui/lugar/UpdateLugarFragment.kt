package com.lugares.ui.lugar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class UpdateLugarFragment : Fragment() {
    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateLugarFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)
        binding.tvLatitud.text=args.lugar.latitud.toString()
        binding.tvLongitud.text=args.lugar.longitud.toString()
        binding.tvAltura.text=args.lugar.altura.toString()

        binding.btUpdateLugar.setOnClickListener{ actualizarLugar() }
        binding.btEmail.setOnClickListener{ enviarCorreo() }
        binding.btPhone.setOnClickListener{ hacerLlamada() }
        binding.btWhatsapp.setOnClickListener{ enviarWhatsapp() }
        binding.btWeb.setOnClickListener{ verWeb() }
        binding.btLocation.setOnClickListener{ verMapa() }

        setHasOptionsMenu(true) //este lo que dice es fragmento tiene un menu adicional
        return binding.root
    }

    private fun enviarCorreo() {
        val correo = binding.etCorreo.text.toString() //se extrae la cuenta del correo del lugar
        if (correo.isNotEmpty()){ //podemos usar el recurso
            val intent = Intent(Intent.ACTION_SEND)  //se envia algo desde el app
            intent.type="message/rfc822" //Se va a enviar un correo electronico, el message es para indicarle al sistema que se le madan un correo

            //se define el destinatario
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))

            //se define el asunto
            intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.msg_saludos)+ " "+binding.etNombre.text)

            //se define el cuerpo del correo inicial
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.msg_correo))

            //Se solicita el recurso del correo para que se envie
            startActivity(intent)

        }else{  //no podemos usar el recurso
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun hacerLlamada() {
        val telefono = binding.etTelefono.text.toString() //se extrae el tel  del lugar
        if (telefono.isNotEmpty()){ //podemos usar el recurso
            val intent = Intent(Intent.ACTION_CALL)  //se hace la accion de llamar
            intent.data= Uri.parse("tel:$telefono")  //asi entiende el android que lo que sigue es un tel

            //se procede a validar si hay o no los permisos otorgados por el usuario
            if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED){

                //se le solicita el permiso al usuario, el 105 es solo un numero ligado, puede ser cualquiera
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
            }else{
                requireActivity().startActivity(intent)
            }

        }else{  //si se han otorgado los permisos anteriormente
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun enviarWhatsapp() {
        val telefono = binding.etTelefono.text.toString() //se extrae el tel  del lugar
        if (telefono.isNotEmpty()){ //podemos usar el recurso
            val intent = Intent(Intent.ACTION_VIEW)  //se hace la accion que significa ver la aplicacion
            //usamos la aplicacion de whsp con el texto precargado de saludos
            val uri="whatsapp://send?phone=506$telefono&text="+getString(R.string.msg_saludos)

            //se establece el app a usar
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(uri) //se carga la info
            startActivity(intent)

        }else{  //si se han otorgado los permisos anteriormente
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun verWeb() {
        val sitio = binding.etWeb.text.toString() //se extrae el link  del lugar
        if (sitio.isNotEmpty()){ //podemos usar el recurso
            val web = Uri.parse("http://$sitio")
            val intent = Intent(Intent.ACTION_VIEW,web)  //se hace la accion que es abrir el sitio

            startActivity(intent) //se abre el visor web

        }else{  //si se han otorgado los permisos anteriormente
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    private fun verMapa() {
        val latitud = binding.tvLatitud.text.toString().toDouble() //se extrae la latitud y se pasa a double
        val longitud = binding.tvLongitud.text.toString().toDouble() //se extrae la longitud y se pasa a double
        if (latitud.isFinite() && longitud.isFinite()){ //podemos usar el recurso
            val location = Uri.parse("geo://$latitud,$longitud?z=18") //la z=18 es el zoom
            val intent = Intent(Intent.ACTION_VIEW,location)  //se ve el mapa del lugar

            startActivity(intent) //se abre el visor del lugar

        }else{  //si se han otorgado los permisos anteriormente
            Toast.makeText(requireContext(),getString(R.string.msg_datos),Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)  //me agrega el logo al menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // se es delete se llama al metodo para que despliegue el menu
        if(item.itemId==R.id.delete_menu){
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actualizarLugar() {
        val nombre=binding.etNombre.text.toString()
        if (nombre.isNotEmpty()){
            val correo=binding.etCorreo.text.toString()
            val telefono=binding.etTelefono.text.toString()
            val web=binding.etWeb.text.toString()
            val lugar= Lugar(args.lugar.id,nombre,correo,telefono,web,0.0,0.0,0.0,"","")
            lugarViewModel.updateLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_update),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_updateLugar_to_nav_lugar)
        }
    }

    private fun deleteLugar(){
        val builder = AlertDialog.Builder(requireContext())  //dialogo de alerta
        builder.setTitle(R.string.menu_delete)
        builder.setMessage(getString(R.string.msg_seguroBorrar)+ " ${args.lugar.nombre} ?")
        builder.setNegativeButton(getString(R.string.no)){_,_->} //esto ultimo es paara no hacer nada
        builder.setPositiveButton(getString(R.string.si)){_,_->
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_nav_updateLugar_to_nav_lugar)
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}