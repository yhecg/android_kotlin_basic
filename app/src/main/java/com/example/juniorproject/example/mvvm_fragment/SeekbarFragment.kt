package com.example.juniorproject.example.mvvm_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.juniorproject.R
import com.example.juniorproject.databinding.FragmentSeekbarBinding

class SeekbarFragment : Fragment() {

    init {

    }

    private lateinit var binding:FragmentSeekbarBinding

    private lateinit var viewModel: VmSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seekbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(VmSharedViewModel::class.java)

        binding = DataBindingUtil.bind(view)!!

        binding.lifecycleOwner = requireActivity()

        binding.vm = viewModel

        binding.sb.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.progress.value = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

    }

}