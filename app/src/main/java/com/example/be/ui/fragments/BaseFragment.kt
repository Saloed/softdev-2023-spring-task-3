package com.example.be.ui.fragments

import androidx.fragment.app.Fragment
import com.example.be.utilits.APP_ACTIVITY

open class BaseFragment(layout: Int) : Fragment(layout) {

    /*lateinit var mRootView: View *//*сам макет где установленны остальные view*//*

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(lauout, container, false)
        return mRootView
    }*/

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }



}