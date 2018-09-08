package com.viet.mine.activity

import android.os.Bundle
import com.viet.mine.R
import com.viet.mine.adapter.CardRvAdapter
import com.viet.news.core.ui.InjectActivity
import kotlinx.android.synthetic.main.activity_mine_wallet.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.viet.mine.adapter.CustomSnapHelper


class MineWalletActivity : InjectActivity() {

    lateinit var adapter: CardRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_wallet)
        initView()
        initData()
    }

    private fun initView() {
        val list = ArrayList<Entity>()
        list.add(Entity("总资产(MB钻)", "10,000.00"))
        list.add(Entity("POWER", "25,000.00"))
        adapter = CardRvAdapter(this, list)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
        val mMySnapHelper = CustomSnapHelper()
        mMySnapHelper.attachToRecyclerView(rv)
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val childCount = rv.childCount

                val location = IntArray(2)
                for (i in 0 until childCount) {
                    val v = rv.getChildAt(i)
                    v.getLocationOnScreen(location)
                    val recyclerViewCenterX = rv.left + rv.width / 2
                    val itemCenterX = location[0] + v.width / 2

                    // 两边的图片缩放比例
                    val scale = 0.85f
                    //某个item中心X坐标距recyclerview中心X坐标的偏移量
                    val offX = Math.abs(itemCenterX - recyclerViewCenterX)
                    //在一个item的宽度范围内，item从1缩放至scale，那么改变了（1-scale），从下列公式算出随着offX变化，item的变化缩放百分比

                    val percent = offX * (1 - scale) / v.width
                    //取反哟
                    var interpretateScale = 1 - percent


                    //                    这个if不走的话，得到的是多级渐变模式
                    if (interpretateScale < scale) {
                        interpretateScale = scale
                    }
                    v.scaleX = interpretateScale
                    v.scaleY = interpretateScale

                }
            }
        })
//        adapter =  ItemAdapter(this,list)
//        vp_card.offscreenPageLimit = 3
//        vp_card.adapter = adapter
//        vp_card.setPageTransformer(false, ScaleTransformer())
    }

    private fun initData() {

    }

    data class Entity(val name: String, val count: String)

}