package com.viet.mine.fragment

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.viet.mine.R
import com.viet.mine.viewmodel.MineWalletViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.core.ui.TabFragmentAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_mine_wallet_in_out.*
import javax.inject.Inject

/**
 * @Description
 * @author null
 * @date 2018/9/8
 * @Email zongjia.long@merculet.io
 * @Version
 */
class AccountInAndOutFragment : RealVisibleHintBaseFragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var adapter: TabFragmentAdapter
    private val model by viewModelDelegate(MineWalletViewModel::class)

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_wallet_in_out
    }

    override fun initView(view: View) {
        adapter.setTitles(model.subTitles)
        adapter.setFragment(model.subFragments)
        viewpager.adapter = adapter
        tablayout_wallet.setViewPager(viewpager)
    }


    @Inject
    internal lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun onAttach(context: Context) {
        //使用的Fragment 是V4 包中的，不然就是AndroidInjection.inject(this)
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}