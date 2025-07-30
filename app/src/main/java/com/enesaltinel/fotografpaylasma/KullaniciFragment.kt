package com.enesaltinel.fotografpaylasma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.enesaltinel.fotografpaylasma.databinding.FragmentKullaniciBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class KullaniciFragment : Fragment() {

    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth // Firebase Authentication (kimlik doğrulama) işlemleri için gerekli olan FirebaseAuth nesnesini oluşturduk.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth // initalize ettik.

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kayitButton.setOnClickListener {kayitOl(it)}
        binding.girisButton.setOnClickListener { girisYap(it) }

        val guncelKullanici = auth.currentUser // bu sayede giriş yapmış isek direkt sayfa açılıyor.
        if (guncelKullanici != null){
            // kullanıcı daha önceden giriş yapmış
            val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }




    }

    fun kayitOl(view: View){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){ // Hem e-posta hem de şifre  eğer ikisi de doluysa devam ediyor.
            // Firebase Authentication servisi kullanılarak, girilen bilgilerle yeni bir kullanıcı kaydı oluşturuluyor.
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){ // kullanıcı başarıyla oluşturulursa
                    // kullanıcı oluşturuldu
                    val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            // Eğer kullanıcı oluşturulurken bir hata olursa (örneğin e-posta zaten kullanılmışsa), hata mesajı bir Toast ile ekrana gösteriliyor.
            }.addOnFailureListener { exception->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    fun girisYap(view: View){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener { // 	Firebase Authentication kullanılarak kullanıcı girişi yapılır.
                val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                Navigation.findNavController(view).navigate(action)

            }.addOnFailureListener { exception->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }



    }










    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}