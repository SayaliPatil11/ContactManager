package com.mangoapps.contactmanager.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangoapps.contactmanager.common.Constants
import com.mangoapps.contactmanager.ui.calllogs.IncomingCallFragment
import com.mangoapps.contactmanager.ui.calllogs.MissedCallFragment
import com.mangoapps.contactmanager.ui.calllogs.OutgoingCallFragment

class TabAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return Constants.TAB_NUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> OutgoingCallFragment()
            2 -> MissedCallFragment()
            else -> IncomingCallFragment()

        }


    }
}