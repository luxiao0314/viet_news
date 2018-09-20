package com.viet.news.core.dsl.adapter

/**
 *@version:
 *@FileDescription:
 *@Author:jing
 *@Since:2018/6/13
 *@ChangeList:
 */
class CommonMgr<D> {

    internal val items: ArrayList<CommonItem<D>> by lazy {
        ArrayList<CommonItem<D>>()
    }

    internal val data by lazy {
        ArrayList<D>()
    }

    fun item(create: () -> CommonItem<D>) {
        items.add(create())
    }

    fun itemDSL(create: CommonAdapterDSL<D>.() -> Unit) {
        val commonDSL = CommonAdapterDSL<D>()
        commonDSL.create()
        items.add(commonDSL.build())
    }

    fun setData(list: List<D>) {
        data.clear()
        data.addAll(list)
    }

    //增加一列数据
    fun addData(newData: List<D>) {
        data.addAll(newData)
    }

    //增加一条数据
    fun addData(list: D) {
        data.add(list)
    }

    fun getItemConfig(position: Int): Int {
        if (items.isEmpty()) {
            throw RuntimeException("item must config")
        }
        if (items.size == 1) {
            return 0
        }
        items.forEachIndexed { index, acrobatItem ->
            if (acrobatItem.isMeetData(data[position], position)) {
                return index
            }
        }
        throw RuntimeException("can't find matched item")
    }
}