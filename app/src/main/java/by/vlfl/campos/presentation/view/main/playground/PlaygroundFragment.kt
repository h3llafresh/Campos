package by.vlfl.campos.presentation.view.main.playground

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.vlfl.campos.appComponent
import by.vlfl.campos.databinding.FragmentPlaygroundBinding
import javax.inject.Inject

class PlaygroundFragment : Fragment() {

    private val args: PlaygroundFragmentArgs by navArgs()

    private var _binding: FragmentPlaygroundBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: PlaygroundViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().appComponent.mainComponent().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewDataFromModel()
    }

    private fun setViewDataFromModel() {
        val model = args.model
        with(binding) {
            tvPlaygroundName.text = model.name
            tvPlaygroundAddress.text = model.address
            tvPlaygroundCategory.text = model.category
        }
    }
}