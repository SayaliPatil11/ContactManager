package com.mangoapps.contactmanager.ui.calllogs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.R
import com.mangoapps.contactmanager.common.IListItemClick
import com.mangoapps.contactmanager.common.UiHelper
import com.mangoapps.contactmanager.databinding.FragmentIncomingCallsBinding
import com.mangoapps.contactmanager.model.CallLogModel
import com.mangoapps.contactmanager.ui.adapter.CallLogListAdapter
import com.mangoapps.contactmanager.viewmodels.CallLogsViewModel

class IncomingCallFragment : Fragment(), IListItemClick {
    private val callLogsViewModel: CallLogsViewModel by activityViewModels()
    private var _binding: FragmentIncomingCallsBinding? = null
    private lateinit var listItemClickListener: IListItemClick

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var manager: RecyclerView.LayoutManager

    private var loadingPB: ProgressBar? = null
    var isProgressVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIncomingCallsBinding.inflate(inflater, container, false)
        manager = LinearLayoutManager(activity)
        listItemClickListener = this
        loadingPB = binding.progressBar



        binding.swiperefresh.setOnRefreshListener {

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            context?.let { callLogsViewModel.getCallLogInformation(it) }

        }
        callLogsViewModel.incomingCallLogs.observe(viewLifecycleOwner) {
            binding.rlIncomingCalls.apply {
                adapter = it?.let { it1 ->
                    CallLogListAdapter(it1, context, listItemClickListener)

                }
                binding.swiperefresh.isRefreshing = false
                dismissProgressBar()
                layoutManager = manager
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (context?.let { UiHelper.isAllPermissionGranted(it) } == true) {
            showProgressBar()

            context?.let { callLogsViewModel.getCallLogInformation(it) }
        } else {
            context?.let { UiHelper.showToast(it, getString(R.string.lbl_permission_error)) }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onListItemClick(position: Int) {
    }

    /* Function to initiate call process*/
    private fun call(number: String) {
        var result = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CALL_PHONE
            )
        }
        if (result == PackageManager.PERMISSION_GRANTED) {
            context?.let { callLogsViewModel.initiateCall(number, it) }
        } else {
            context?.let { UiHelper.showToast(it, getString(R.string.lbl_permission_error)) }
        }


    }

    /*Display progress bar in UI*/
    private fun showProgressBar() {
        if (!isProgressVisible) {
            isProgressVisible = true
            binding.progressBar.visibility = View.VISIBLE

        }
    }

    /* Close progress dialog on UI*/
    private fun dismissProgressBar() {
        if (isProgressVisible) {
            isProgressVisible = false
            binding.progressBar.visibility = View.GONE
        }

    }


}