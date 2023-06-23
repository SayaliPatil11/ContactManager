package com.mangoapps.contactmanager.ui.calllogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mangoapps.contactmanager.R
import com.mangoapps.contactmanager.common.UiHelper
import com.mangoapps.contactmanager.databinding.FragmentCalllogsBinding
import com.mangoapps.contactmanager.model.CallLogModel
import com.mangoapps.contactmanager.ui.adapter.TabAdapter
import com.mangoapps.contactmanager.viewmodels.CallLogsViewModel

/* UI for Call log fragment*/
class CallLogsFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private var _binding: FragmentCalllogsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var viewPager: ViewPager2? = null
    var tabLayout: TabLayout? = null
    var selectedTab = 0
    lateinit var callLogsViewModel: CallLogsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        callLogsViewModel = ViewModelProvider(this).get(CallLogsViewModel::class.java)

        context?.let { callLogsViewModel.getCallLogInformation(it) }
        _binding = FragmentCalllogsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.viewPager2
        tabLayout = binding.slidingTabs
        setHasOptionsMenu(true)

        val adapter = activity?.supportFragmentManager?.let {
            TabAdapter(
                it,
                lifecycle
            )
        }
        viewPager?.adapter = adapter

        //Initialize tab array
        val tabArray = arrayOf(
            getString(R.string.lbl_incoming),
            getString(
                R.string.lbl_outgoing
            ),
            getString(
                R.string.lbl_missed
            )
        )

        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

        for (i in 0 until (tabLayout?.tabCount?.minus(1) ?: 0)) {
            val tab = (tabLayout?.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 50, 0)
            tab.requestLayout()
        }

        tabLayout?.addOnTabSelectedListener(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            selectedTab = tab.position
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    /* Method to handle action bar refresh button click*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuitem_refresh -> {
                context?.let { UiHelper.showToast(it, getString(R.string.lbl_refresh_call_logs)) }
                context?.let { callLogsViewModel.getCallLogInformation(it) }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.menuitem_refresh)
        item.isVisible = false
    }


}