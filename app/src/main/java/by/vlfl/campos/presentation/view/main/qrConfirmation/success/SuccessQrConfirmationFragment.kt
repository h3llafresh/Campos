package by.vlfl.campos.presentation.view.main.qrConfirmation.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.databinding.FragmentSuccessQrConfirmationBinding

class SuccessQrConfirmationFragment : Fragment() {

    private var _binding: FragmentSuccessQrConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSuccessQrConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOkButtonClickListener()
    }

    private fun setupOkButtonClickListener() = binding.bOk.setOnClickListener {
        findNavController().popBackStack()
    }
}