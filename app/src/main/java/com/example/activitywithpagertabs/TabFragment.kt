package com.example.activitywithpagertabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TabFragment : Fragment() {
    companion object {
        private const val ARG_TITLE = "title"

        @JvmStatic
        fun newInstance(title: String): TabFragment {
            return TabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(ARG_TITLE) ?: "Unknown"
        view.findViewById<TextView>(R.id.tvContent).text = "Content for $title"
    }

    override fun onResume() {
        super.onResume()
        Log.d("TabFragment", "${arguments?.getString(ARG_TITLE)} is visible")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TabFragment", "${arguments?.getString(ARG_TITLE)} is invisible")
    }
}
