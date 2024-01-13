package com.example.memo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat


class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val darkSwitch = view.findViewById<SwitchCompat>(R.id.darkSwitch)

        darkSwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("night", isChecked)
            editor.apply()
        }

        return view
    }
}