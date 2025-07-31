package com.enesaltinel.fotografpaylasma

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.enesaltinel.fotografpaylasma.databinding.FragmentFeedBinding
import com.enesaltinel.fotografpaylasma.databinding.FragmentYuklemeBinding
import com.google.android.material.snackbar.Snackbar


class YuklemeFragment : Fragment() {

    private var _binding: FragmentYuklemeBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var secilenGorsel : Uri?=null
    var secilenBitmap : Bitmap?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLaunchers()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYuklemeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.yukleButton.setOnClickListener { yukleTiklandi(it) }
        binding.imageView.setOnClickListener { gorselSec(it) }

    }

    fun yukleTiklandi(view: View){

    }

    fun gorselSec(view: View){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            // read media images
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                // izin yok
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)){
                    // izin mantığını kullanıcıya göstermemiz lazım
                    Snackbar.make(view,"Galeriye gitmeniz için izin vermeniz gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver",View.OnClickListener {
                       // izin istememiz lazım
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) // aşağıda permissionlauncherı yaptıktan sonra ekledik buraya
                    }).show()
                }else{
                    // izin istememiz lazım
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) // aşağıda permissionlauncherı yaptıktan sonra ekledik buraya
                }
            }else{
                // izin var
                // galeriye gitme kodunu yazıcaz.
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)  // bu 2 satırı direkt aşağıdan kopyaladık.
                activityResultLauncher.launch(intentToGallery)
            }

        }else{
            // read external storage
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                // izin yok
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                    // izin mantığını kullanıcıya göstermemiz lazım
                    Snackbar.make(view,"Galeriye gitmeniz için izin vermeniz gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver",View.OnClickListener {
                        // izin istememiz lazım
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) // aşağıda permissionlauncherı yaptıktan sonra ekledik buraya
                    }).show()
                }else{
                    // izin istememiz lazım
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) // aşağıda permissionlauncherı yaptıktan sonra ekledik buraya
                }
            }else{
                // izin var
                // galeriye gitme kodunu yazıcaz.
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)  // bu 2 satırı direkt aşağıdan kopyaladık.
                activityResultLauncher.launch(intentToGallery)
            }

        }


    }



    private fun registerLaunchers(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    secilenGorsel = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >=28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(secilenBitmap)

                        }else{
                            secilenBitmap =MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->

            if (result){
                // izin verildi
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                // kullanıcı izni reddetti
                Toast.makeText(requireContext(),"İzni reddettiniz,izne ihtiyacımız var",Toast.LENGTH_LONG).show()
            }

        }

    }









    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}