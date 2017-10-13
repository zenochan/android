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

  public Contact() {}

  public Long getId()
  {return this.id;}

  public String getDisplayName()
  {return this.displayName;}

  public Long getPhotoId()
  {return this.photoId;}

  public Integer getTimesContacted()
  {return this.timesContacted;}

  public Long getLastTimeContacted()
  {return this.lastTimeContacted;}

  public String getLookup()
  {return this.lookup;}

  public Long getContactId()
  {return this.contactId;}

  public String getNumber()
  {return this.number;}

  public String getNormalizedNumber()
  {return this.normalizedNumber;}

  public String getSortKey()
  {return this.sortKey;}

  public void setId(Long id)
  {this.id = id; }

  public void setDisplayName(String displayName)
  {this.displayName = displayName; }

  public void setPhotoId(Long photoId)
  {this.photoId = photoId; }

  public void setTimesContacted(Integer timesContacted)
  {this.timesContacted = timesContacted; }

  public void setLastTimeContacted(Long lastTimeContacted)
  {this.lastTimeContacted = lastTimeContacted; }

  public void setLookup(String lookup)
  {this.lookup = lookup; }

  public void setContactId(Long contactId)
  {this.contactId = contactId; }

  public void setNumber(String number)
  {this.number = number; }

  public void setNormalizedNumber(String normalizedNumber)
  {this.normalizedNumber = normalizedNumber; }

  public void setSortKey(String sortKey)
  {this.sortKey = sortKey; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof Contact)) return false;
    final Contact other = (Contact) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$id  = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
    final Object this$displayName  = this.getDisplayName();
    final Object other$displayName = other.getDisplayName();
    if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName))
      return false;
    final Object this$photoId  = this.getPhotoId();
    final Object other$photoId = other.getPhotoId();
    if (this$photoId == null ? other$photoId != null : !this$photoId.equals(other$photoId))
      return false;
    final Object this$timesContacted  = this.getTimesContacted();
    final Object other$timesContacted = other.getTimesContacted();
    if (this$timesContacted == null ? other$timesContacted != null : !this$timesContacted.equals(other$timesContacted))
      return false;
    final Object this$lastTimeContacted  = this.getLastTimeContacted();
    final Object other$lastTimeContacted = other.getLastTimeContacted();
    if (this$lastTimeContacted == null ? other$lastTimeContacted != null : !this$lastTimeContacted.equals(other$lastTimeContacted))
      return false;
    final Object this$lookup  = this.getLookup();
    final Object other$lookup = other.getLookup();
    if (this$lookup == null ? other$lookup != null : !this$lookup.equals(other$lookup))
      return false;
    final Object this$contactId  = this.getContactId();
    final Object other$contactId = other.getContactId();
    if (this$contactId == null ? other$contactId != null : !this$contactId.equals(other$contactId))
      return false;
    final Object this$number  = this.getNumber();
    final Object other$number = other.getNumber();
    if (this$number == null ? other$number != null : !this$number.equals(other$number))
      return false;
    final Object this$normalizedNumber  = this.getNormalizedNumber();
    final Object other$normalizedNumber = other.getNormalizedNumber();
    if (this$normalizedNumber == null ? other$normalizedNumber != null : !this$normalizedNumber.equals(other$normalizedNumber))
      return false;
    final Object this$sortKey  = this.getSortKey();
    final Object other$sortKey = other.getSortKey();
    if (this$sortKey == null ? other$sortKey != null : !this$sortKey.equals(other$sortKey))
      return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $id    = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $displayName = this.getDisplayName();
    result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
    final Object $photoId = this.getPhotoId();
    result = result * PRIME + ($photoId == null ? 43 : $photoId.hashCode());
    final Object $timesContacted = this.getTimesContacted();
    result = result * PRIME + ($timesContacted == null ? 43 : $timesContacted.hashCode());
    final Object $lastTimeContacted = this.getLastTimeContacted();
    result = result * PRIME + ($lastTimeContacted == null ? 43 : $lastTimeContacted.hashCode());
    final Object $lookup = this.getLookup();
    result = result * PRIME + ($lookup == null ? 43 : $lookup.hashCode());
    final Object $contactId = this.getContactId();
    result = result * PRIME + ($contactId == null ? 43 : $contactId.hashCode());
    final Object $number = this.getNumber();
    result = result * PRIME + ($number == null ? 43 : $number.hashCode());
    final Object $normalizedNumber = this.getNormalizedNumber();
    result = result * PRIME + ($normalizedNumber == null ? 43 : $normalizedNumber.hashCode());
    final Object $sortKey = this.getSortKey();
    result = result * PRIME + ($sortKey == null ? 43 : $sortKey.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof Contact;}

  public String toString()
  {return "Contact(id=" + this.getId() + ", displayName=" + this.getDisplayName() + ", photoId=" + this.getPhotoId() + ", timesContacted=" + this.getTimesContacted() + ", lastTimeContacted=" + this.getLastTimeContacted() + ", lookup=" + this.getLookup() + ", contactId=" + this.getContactId() + ", number=" + this.getNumber() + ", normalizedNumber=" + this.getNormalizedNumber() + ", sortKey=" + this.getSortKey() + ")";}
}
