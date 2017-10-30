import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.repositories

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  extra["support_version"] = "26.1.0"
  extra["build_tools_version"] = "26.0.2"
  extra["kotlin_version"] = "1.1.51"
  extra["butterknif_version"] = "9.0.0-SNAPSHOT"


  repositories {
    jcenter()
    google()
    mavenCentral()

    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
  }
  dependencies {
    classpath("com.android.tools.build:gradle:3.0.0")
    classpath("com.jakewharton:butterknife-gradle-plugin:${extra["butterknif_version"]}")

    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlin_version"]}")
    classpath("org.jetbrains.kotlin:kotlin-noarg:${extra["kotlin_version"]}")
    classpath("org.jetbrains.kotlin:kotlin-allopen:${extra["kotlin_version"]}")

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}

allprojects {
  repositories {
    jcenter()
    google()
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("http://maven.mjtown.cn") }
    maven { setUrl("https://maven.google.com") }

    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }

    // 阿里百川 HotFix
    maven { setUrl("http://repo.baichuan-android.taobao.com/content/groups/BaichuanRepositories") }
  }
}

task("clean", Delete::class) {
  delete(rootProject.buildDir)
}
