package com.xavisson.archcomponentstraining.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xavisson.archcomponentstraining.R

class AboutFragment : Fragment() {

    companion object {

        const val PROJECT_SOURCE_CODE = "https://github.com/erikcaffrey/Android-Architecture-Components-Kotlin"

        fun newInstance() = AboutFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.about_fragment, container, false)
        initUI(view)
        return view
    }

    private fun initUI(view: View?) {
        val showSourceText = view?.findViewById(R.id.show_me_code) as TextView
        showSourceText.setOnClickListener { startActivityActionView() }
    }


    private fun startActivityActionView() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PROJECT_SOURCE_CODE)))
    }

}