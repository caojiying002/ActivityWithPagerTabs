package com.example.activitywithpagertabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val tabTitles = listOf("Home", "Profile", "Settings", "More")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        setupViewPager(savedInstanceState)
        setupTabLayout()
    }

    private fun setupViewPager(savedInstanceState: Bundle?) {
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = tabTitles.size
            override fun createFragment(position: Int) = TabFragment.newInstance(tabTitles[position])
        }
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateFragmentLifecycle(position)
            }
        })

        if (savedInstanceState == null) {
            // 只在首次创建时设置当前页，避免覆盖恢复的状态
            viewPager.setCurrentItem(0, false)
        }
    }

    private fun updateFragmentLifecycle(position: Int) {
        val fragmentManager = supportFragmentManager
        var fragmentTransaction: FragmentTransaction? = null

        for (i in 0 until tabTitles.size) {
            val fragment = fragmentManager.findFragmentByTag("f$i") ?: continue
            if (fragmentTransaction == null)
                fragmentTransaction = fragmentManager.beginTransaction()

            if (i == position) {
                fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
            } else {
                fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
            }
        }

        fragmentTransaction?.commitNowAllowingStateLoss()
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.customView = createTabView(tab.view, tabTitles[position])
        }.attach()


        // Update tab styles when selected/unselected
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateTabStyle(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                updateTabStyle(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Set initial tab style
        for (i in 0 until tabLayout.tabCount) {
            updateTabStyle(tabLayout.getTabAt(i)!!, i == 0)
        }
    }

    private fun createTabView(parent: ViewGroup, title: String): View {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_tab, parent, false)
        view.findViewById<TextView>(R.id.tvTabTitle).text = title
        return view
    }

    private fun updateTabStyle(tab: TabLayout.Tab, isSelected: Boolean) {
        val view = tab.customView ?: return
        val tvTitle = view.findViewById<TextView>(R.id.tvTabTitle)
        val iconView = view.findViewById<View>(R.id.ivTabIcon)

        if (isSelected) {
            tvTitle.setTextColor(getColor(R.color.tab_selected_text))
            iconView.setBackgroundResource(R.drawable.tab_selected_background)
        } else {
            tvTitle.setTextColor(getColor(R.color.tab_unselected_text))
            iconView.setBackgroundResource(R.drawable.tab_unselected_background)
        }
    }
}