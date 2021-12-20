package com.kks.nimbletest.ui.base

import android.os.Bundle
import android.transition.ChangeImageTransform
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.kks.nimbletest.R

/*
 * In Activity
 * source : https://chetangupta.net/viewbinding/
 * Author : ChetanGupta.net
 */
abstract class BaseViewBindingActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setup()
    }

    abstract fun setup()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected open fun setupToolbar(toolbar: Toolbar, isChild: Boolean) {

        if (isChild) {
            /*final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.colorTextColorPrimary), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);*/
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationIcon(R.drawable.ic_back)

        }
    }

    protected open fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // click on 'up' button in the action bar, handle it here
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}