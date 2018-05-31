```xml
<resources>
  <style name="BaseAppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorPrimary</item>
    <item name="android:screenOrientation">portrait</item>
    <item name="android:launchMode">singleTop</item>

    <item name="android:windowBackground">@color/ss_background</item>
    <item name="vpiCirclePageIndicatorStyle">@style/vi</item>
    <item name="md_dark_theme">false</item>
    <item name="md_background_color">#ffffff</item>
    <item name="md_icon">@drawable/ic_churgo</item>
    <item name="md_title_color">@color/z.black_a87</item>
    <item name="md_content_color">@color/z.black_a87</item>
    <item name="md_link_color">#673AB7</item>
    <item name="md_positive_color">@color/colorPrimary</item>
    <item name="md_neutral_color">@color/z.black_a87</item>
    <item name="md_negative_color">@color/z.black_a54</item>
    <item name="md_widget_color">@color/colorPrimary</item>
    <item name="md_item_color">@color/z.black_a87</item>
    <item name="md_divider_color">@color/z.black_a54</item>
    <item name="md_btn_ripple_color">#E91E63</item>
    <item name="md_title_gravity">start</item>
    <item name="md_content_gravity">start</item>
    <item name="md_items_gravity">start</item>
    <item name="md_buttons_gravity">start</item>
    <item name="md_btnstacked_gravity">end</item>
  </style>

  <!-- Base application theme. -->
  <style name="AppTheme" parent="BaseAppTheme">
  </style>

  <style name="AppTheme.Alipay">
    <item name="colorPrimaryDark">#0b111d</item>
  </style>

  <style name="AppTheme.full">
    <item name="android:windowFullscreen">true</item>
    <item name="android:windowNoTitle">true</item>
  </style>


  <style name="AppTheme.Transparent">
    <item name="android:windowBackground">@android:color/transparent</item>
    <item name="android:windowIsTranslucent">true</item>
    <item name="android:windowAnimationStyle">@android:style/Animation.Translucent</item>
  </style>

  <style name="AppTheme.full.Welcome">
    <!-- 启动时直接显示背景-->
    <item name="android:windowBackground">@drawable/img_launch</item>
  </style>
</resources>
```