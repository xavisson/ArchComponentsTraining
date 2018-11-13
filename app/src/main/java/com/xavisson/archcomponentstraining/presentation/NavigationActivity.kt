package com.xavisson.archcomponentstraining.presentation

import android.os.Bundle
import com.xavisson.archcomponentstraining.R
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class NavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(CurrencyFragment.newInstance())
                return true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(AboutFragment.newInstance())
                return true
            }
        }
        return false
    }

    private fun initUI() {
        replaceFragment(CurrencyFragment.newInstance())
        initNavigation()
    }

    private fun initNavigation() {
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(this)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit()
    }
}
