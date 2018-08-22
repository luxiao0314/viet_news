package com.viet.news.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.viet.news.R


/**
 * @author null
 */
class TwoFragment : LazyFragment() {
    private var tabName: String? = null
    private var mTabNameTv: TextView? = null
    private var mContainerView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabName = arguments!!.getString("tabName")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_two, container, false)
        return mContainerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTabNameTv = mContainerView!!.findViewById<View>(R.id.id_fragment_two) as TextView
        mTabNameTv!!.text = "布局2${tabName!!}"
        initNet()
    }

    override fun initNet() {

    }

    companion object {

        fun newInstance(tabTitle: String): TwoFragment {
            val twoFragment = TwoFragment()
            val bundle = Bundle()
            bundle.putString("tabName", tabTitle)
            twoFragment.arguments = bundle
            return twoFragment
        }
    }
}
