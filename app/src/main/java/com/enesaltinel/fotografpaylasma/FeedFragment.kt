package com.enesaltinel.fotografpaylasma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import com.enesaltinel.fotografpaylasma.databinding.FragmentFeedBinding
import com.enesaltinel.fotografpaylasma.databinding.FragmentKullaniciBinding


class FeedFragment : Fragment(),PopupMenu.OnMenuItemClickListener {  // Bu sınıf, PopupMenu içindeki öğelere tıklanma olayını dinlemeye hazır.
    // bu interface’in içindeki onMenuItemClick() fonksiyonunu override etmek zorundasın

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { floatingButtonTiklandi(it) }
    }

    fun floatingButtonTiklandi(view: View){
        val popup = PopupMenu(requireContext(),binding.floatingActionButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.my_popup_menu,popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener(this)


    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.yuklemeItem){
            val action = FeedFragmentDirections.actionFeedFragmentToYuklemeFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }else if (item?.itemId == R.id.cikisItem){
            // çıkış yapma işlemi
            val action = FeedFragmentDirections.actionFeedFragmentToKullaniciFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        return true
    }


}