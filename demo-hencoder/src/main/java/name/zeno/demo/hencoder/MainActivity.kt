package name.zeno.demo.hencoder

import android.app.Fragment
import android.os.Bundle
import android.support.v13.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  val fragments: MutableList<Fragment> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    tabLayout.setupWithViewPager(pager)

    pager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
      override fun getItem(position: Int) = fragments[position]
      override fun getCount() = fragments.size
      override fun getPageTitle(position: Int) = (fragments[position] as? Title)?.title
    }

    fragments.add(ColorFragment())
    pager.adapter?.notifyDataSetChanged()
  }
}
