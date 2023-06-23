package com.mangoapps.contactmanager.ui.sms

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.R
import com.mangoapps.contactmanager.common.UiHelper
import com.mangoapps.contactmanager.databinding.FragmentSmsBinding
import com.mangoapps.contactmanager.ui.adapter.SmsListAdapter
import com.mangoapps.contactmanager.viewmodels.SmsViewModel

/* Class for displaying SMS list UI*/
class SmsFragment : Fragment() {

    private var _binding: FragmentSmsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var smsViewModel: SmsViewModel
    private var loadingPB: ProgressBar? = null
    var isProgressVisible = false
    private lateinit var manager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        smsViewModel =
            ViewModelProvider(this)[SmsViewModel::class.java]

        _binding = FragmentSmsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        manager = LinearLayoutManager(activity)

        loadingPB = binding.progressBar
        setHasOptionsMenu(true)
        smsViewModel.smsList.observe(viewLifecycleOwner) {
            binding.rlSmsList.apply {
                adapter = it?.let { it1 ->
                    SmsListAdapter(it1, context)

                }
                binding.swiperefresh.isRefreshing = false
                dismissProgressBar()
                layoutManager = manager
            }
        }
        binding.swiperefresh.setOnRefreshListener {


            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            getSmsList()
        }
        return root
    }

    /*Method to fetch sms list from backend*/
    private fun getSmsList() {
        if(context?.let { UiHelper.isAllPermissionGranted(it) } == true){
            showProgressBar()
            context?.let { smsViewModel.getAllSms(it) }
        }else{
            context?.let { UiHelper.showToast(it, getString(R.string.lbl_permission_error))  }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuitem_refresh -> {
                context?.let { UiHelper.showToast(it, getString(R.string.lbl_refresh_sms)) }
                binding.swiperefresh.isRefreshing = true

                getSmsList()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* Display progress bar on UI*/
    private fun showProgressBar() {
        if (!isProgressVisible) {
            isProgressVisible = true
            binding.progressBar.visibility = View.VISIBLE

        }
    }

    /* Close progress bar on UI*/
    private fun dismissProgressBar() {
        if (isProgressVisible) {
            isProgressVisible = false
            binding.progressBar.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        getSmsList()

    }

}