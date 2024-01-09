package com.example.memo

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.memo.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity(), FolderFragmentListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(ChatFragment())

        binding.navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_chat -> replaceFragment(ChatFragment())
                R.id.tab_folder -> replaceFragment(FolderListFragment.newInstance())
                R.id.tab_setting -> replaceFragment(SettingFragment())
                else -> {
                }
            }
            true
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameContainer.id, fragment)
        fragmentTransaction.commit()
    }

    fun HideBottomNavi(state: Boolean) {
        if (state) binding.navBar.visibility = View.GONE else binding.navBar.visibility = View.VISIBLE
    }

    //edit도 연관되어 있음.
    override fun onDeleteButtonClicked(folderId: Int) {
            val folderListFragment = supportFragmentManager.findFragmentById(R.id.frame_container) as? FolderListFragment
            val currentFolderId = folderListFragment?.getCurrentFolderId()
            currentFolderId?.let(folderListFragment::deleteFolder)
    }
}
