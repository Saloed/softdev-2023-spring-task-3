package com.example.be.ui.objects

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.be.R
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.utilits.COUNT_SNAPSHOT_PLUS
import com.example.be.utilits.URL_FIRST_LECTURE
import com.example.be.utilits.URL_FOURTH_LECTURE
import com.example.be.utilits.URL_SECOND_LECTURE
import com.example.be.utilits.URL_THIRD_LECTURE
import com.example.be.ui.fragments.FirstLectureFragment
import com.example.be.utilits.identifier_first_lection
import com.example.be.utilits.identifier_forth_lection
import com.example.be.utilits.identifier_second_lection
import com.example.be.utilits.identifier_third_lection
import com.example.be.utilits.replaceFragment
import com.example.be.utilits.showToast
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import okhttp3.internal.http2.Header

class AppDrawer(val toolbar: androidx.appcompat.widget.Toolbar) {

    private lateinit var mDrawer: Drawer
    private lateinit var mDrawerLayout: DrawerLayout

    fun create() {
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)/*блокируем наш drawer в закрытом состоянии*/
        toolbar.setNavigationOnClickListener {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)/*сначала откючаем кнопку, потом включаем toggle*/
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(APP_ACTIVITY)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)/*бургер*/
            .withSelectedItem(-1)
            .withHeader(R.layout.nav_header)
            .withSliderBackgroundColor(APP_ACTIVITY.resources.getColor(R.color.brown))
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(identifier_first_lection)/*число по которому будеи находить это меню*/
                    .withIconTintingEnabled(true)/*видны иконки*/
                    .withName("Управление собой")
                    .withSelectable(false),/*выбранный или нет*/
                PrimaryDrawerItem().withIdentifier(identifier_second_lection)
                    .withIconTintingEnabled(true)
                    .withName("Самоопределение")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(identifier_third_lection)
                    .withIconTintingEnabled(true)
                    .withName("Личные границы")
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(identifier_forth_lection)
                    .withIconTintingEnabled(true)
                    .withName("Эмоциональное выгорание")
                    .withSelectable(false)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    if (position == 1) {
                        replaceFragment(FirstLectureFragment(URL_FIRST_LECTURE))
                        return false
                    }

                    if (position == 2 && COUNT_SNAPSHOT_PLUS == 2) {
                        replaceFragment(FirstLectureFragment(URL_SECOND_LECTURE))
                        return false
                    } else if (position == 2) showToast("Эта лекция будет открыта через ${2 - COUNT_SNAPSHOT_PLUS} созданных сообщения")

                    if (position == 3 && COUNT_SNAPSHOT_PLUS == 3) {
                        replaceFragment(FirstLectureFragment(URL_THIRD_LECTURE))
                        return false
                    } else if (position == 3) showToast("Эта лекция будет открыта через ${3 - COUNT_SNAPSHOT_PLUS} созданных сообщения")

                    if (position == 4 && COUNT_SNAPSHOT_PLUS == 4) {
                        replaceFragment(FirstLectureFragment(URL_FOURTH_LECTURE))
                        return false
                    } else if (position == 4) showToast("Эта лекция будет открыта через ${4 - COUNT_SNAPSHOT_PLUS} созданных сообщения")

                    return false
                }
            }).build()
    }

}