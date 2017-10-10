package name.zeno.android.system;

/**
 * Create Date: 16/7/5
 *
 * @author 陈治谋 (513500085@qq.com)
 * @see <a href='http://blog.csdn.net/snwrking/article/details/7601794'>
 * Android软件开发之获取通讯录联系人信息 + android联系人信息的存储结构 + Android联系人读取操作笔记</a>
 * @see <a href='http://www.cnblogs.com/android100/p/android-tel-book.html'>
 * Android通讯录管理（获取联系人、通话记录、短信消息）</a>
 */
public class Contact
{
  //contacts 表
  private Long    id;//表的ID，主要用于其它表通过contacts 表中的ID可以查到相应的数据
  private String  displayName;//联系人名称 -> zeno
  private Long    photoId;//头像的ID
  private Integer timesContacted;//通话记录次数
  private Long    lastTimeContacted;//最后的通话时间
  private String  lookup;//一个持久化的存储,用户可能会改名字，但是改不了lookup

  //data表
  private Long   contactId;
  private String number;//data1 -> 187 1234 5678
  private String normalizedNumber;//data4 -> +8618712345678
  private String sortKey;//首字母 -> z

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public Long getPhotoId()
  {
    return photoId;
  }

  public void setPhotoId(Long photoId)
  {
    this.photoId = photoId;
  }

  public Integer getTimesContacted()
  {
    return timesContacted;
  }

  public void setTimesContacted(Integer timesContacted)
  {
    this.timesContacted = timesContacted;
  }

  public Long getLastTimeContacted()
  {
    return lastTimeContacted;
  }

  public void setLastTimeContacted(Long lastTimeContacted)
  {
    this.lastTimeContacted = lastTimeContacted;
  }

  public String getLookup()
  {
    return lookup;
  }

  public void setLookup(String lookup)
  {
    this.lookup = lookup;
  }

  public Long getContactId()
  {
    return contactId;
  }

  public void setContactId(Long contactId)
  {
    this.contactId = contactId;
  }

  public String getNumber()
  {
    return number;
  }

  public void setNumber(String number)
  {
    this.number = number;
  }

  public String getNormalizedNumber()
  {
    return normalizedNumber;
  }

  public void setNormalizedNumber(String normalizedNumber)
  {
    this.normalizedNumber = normalizedNumber;
  }

  public String getSortKey()
  {
    return sortKey;
  }

  public void setSortKey(String sortKey)
  {
    this.sortKey = sortKey;
  }
}
