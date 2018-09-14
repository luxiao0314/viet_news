package cn.magicwindow.channelwidget.entity

/**
 * @author null
 */

class ChannelBean(var channelName: String? = "",
                  var id: String? = "",
                  var tabType: Int = 0, //0:不可编辑 2:可编辑
                  var editStatus: Int = 0)
