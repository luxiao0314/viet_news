package com.viet.mine.fragment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.magicwindow.core.ui.ItemClickSupport
import com.viet.mine.adapter.LanguageAdapter
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.domain.RefreshSettingInfoEvent
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.LanguageUtil
import com.viet.news.core.utils.RxBus
import com.viet.news.core.utils.SPHelper

/**
 * @Description 语言选择
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(Config.ROUTER_MINE_SETTING_LANGUAGE_FRAGMENT)
class LanguageFragment : BaseFragment() {
    private lateinit var adapter: LanguageAdapter

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting_language
    }

    override fun initView(view: View) {
        val rvLanguage = view.findViewById<RecyclerView>(R.id.rv_language)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = OrientationHelper.VERTICAL
        rvLanguage.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_1dp)!!)
        rvLanguage.addItemDecoration(dividerItemDecoration)
        adapter = LanguageAdapter()
        rvLanguage.adapter = adapter
        adapter.addData(resources.getStringArray(R.array.language).toList())

        ItemClickSupport.addTo(rvLanguage).addOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                setLanguage(position)
                if (LanguageUtil.needChange(position)) {
                    LanguageUtil.saveSelectLanguage(context!!, position)
                    LanguageUtil.routToMainForce(activity)
                } else {
                    //当不需要切换语言时，判断是否是【跟随】 与【系统语言】之间的切换
                    val localIndex = SPHelper.create().getInt(Config.SELECTED_LANGUAGE)
                    if ((localIndex == 0 || position == 0) && localIndex != position) {
                        SPHelper.create().putInt(Config.SELECTED_LANGUAGE, position)
                    }
                    (activity!! as BaseActivity).finishWithAnim()
                }
            }
        })
    }

    fun setLanguage(position: Int) {
        SPHelper.create(context!!).putInt("language", position)
        RxBus.get().post(RefreshSettingInfoEvent())
    }
}