package by.vlfl.campos.presentation.view.authorization.signUp.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.vlfl.campos.databinding.FragmentSuccessSignUpBinding

class SuccessSignUpFragment : Fragment() {

    private var _binding: FragmentSuccessSignUpBinding? = null
    private val binding: FragmentSuccessSignUpBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSuccessSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOkButtonListener()
    }

    private fun setupOkButtonListener() = binding.bOk.setOnClickListener {
        findNavController().popBackStack()
    }
}