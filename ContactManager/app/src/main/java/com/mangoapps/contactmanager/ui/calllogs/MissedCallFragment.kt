package com.mangoapps.contactmanager.ui.calllogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.common.IListItemClick
import com.mangoapps.contactmanager.databinding.FragmentMissedCallsBinding
import com.mangoapps.contactmanager.ui.adapter.CallLogListAdapter
import com.mangoapps.contactmanager.viewmodels.CallLogsViewModel

class MissedCallFragment : Fragment(), IListItemClick {
    private val callLogsViewModel: CallLogsViewModel by activityViewModels()
    private var _binding: FragmentMissedCallsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var listItemClickListener: IListItemClick
    private lateinit var manager: RecyclerView.LayoutManager

    private var loadingPB: ProgressBar? = null
    var isProgressVisible = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMissedCallsBinding.inflate(inflater, container, false)
        manager = LinearLayoutManager(activity)
        listItemClickListener = this
        loadingPB = binding.progressBar
        showProgressBar()
        binding.swiperefresh.setOnRefreshListener {

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            context?.let { callLogsViewModel.getCallLogInformation(it) }

        }
        callLogsViewModel.missedCallLogs.observe(viewLifecycleOwner) {

            binding.rlMissedCalls.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onListItemClick(position: Int) {


    }

    /* Display progress bar on UI*/
    private fun showProgressBar() {
        if (!isProgressVisible) {
            isProgressVisible = true
            binding.progressBar.visibility = View.VISIBLE

        }
    }

    /*Close progress bar on UI*/
    private fun dismissProgressBar() {
        if (isProgressVisible) {
            isProgressVisible = false
            binding.progressBar.visibility = View.GONE
        }

    }

}