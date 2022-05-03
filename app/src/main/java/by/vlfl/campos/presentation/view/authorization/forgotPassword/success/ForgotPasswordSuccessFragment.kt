package by.vlfl.campos.presentation.view.authorization.forgotPassword.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.databinding.FragmentSuccessForgotPasswordBinding

class ForgotPasswordSuccessFragment : Fragment() {

    private var _binding: FragmentSuccessForgotPasswordBinding? = null
    private val binding: FragmentSuccessForgotPasswordBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSuccessForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnOkClickListener()
    }

    private fun setupOnOkClickListener() = binding.bOk.setOnClickListener {
        findNavController().popBackStack()
    }
}