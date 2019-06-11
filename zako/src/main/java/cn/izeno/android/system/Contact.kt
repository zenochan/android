package cn.izeno.android.system

/**
 *
 * - [Android软件开发之获取通讯录联系人信息 + android联系人信息的存储结构 + Android联系人读取操作笔记](http://blog.csdn.net/snwrking/article/details/7601794)
 * - [Android通讯录管理（获取联系人、通话记录、短信消息）](http://www.cnblogs.com/android100/p/android-tel-book.html)
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/7/5
 */
class Contact(

    //contacts 表
    var id: Long? = null,//表的ID，主要用于其它表通过contacts 表中的ID可以查到相应的数据
    var displayName: String? = null,//联系人名称 -> zeno
    var photoId: Long? = null,//头像的ID
    var timesContacted: Int? = null,//通话记录次数
    var lastTimeContacted: Long? = null,//最后的通话时间
    var lookup: String? = null,//一个持久化的存储,用户可能会改名字，但是改不了lookup

    //data表
    var contactId: Long? = null,
    var number: String? = null,//data1 -> 187 1234 5678
    var normalizedNumber: String? = null,//data4 -> +8618712345678
    var sortKey: String? = null//首字母 -> z
)
