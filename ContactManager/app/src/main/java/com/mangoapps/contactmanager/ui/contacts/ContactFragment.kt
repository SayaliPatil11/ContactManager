package com.mangoapps.contactmanager.ui.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.R
import com.mangoapps.contactmanager.common.IListItemClick
import com.mangoapps.contactmanager.common.UiHelper
import com.mangoapps.contactmanager.databinding.FragmentContactBinding
import com.mangoapps.contactmanager.model.ContactModel
import com.mangoapps.contactmanager.ui.adapter.ContactListAdapter
import com.mangoapps.contactmanager.viewmodels.ContactViewModel

/*UI for displaying contact list*/
class ContactFragment : Fragment(), IListItemClick {

    private var _binding: FragmentContactBinding? = null
    private lateinit var listItemClickListener: IListItemClick
    private lateinit var manager: RecyclerView.LayoutManager
    private var loadingPB: ProgressBar? = null
    var isProgressVisible = false
    var contactList = mutableListOf<ContactModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var contactViewModel: ContactViewModel;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        contactViewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        _binding = FragmentContactBinding.inflate(inflater, container, false)

        val root: View = binding.root
        listItemClickListener = this
        loadingPB = binding.progressBar

        manager = LinearLayoutManager(activity)


        setHasOptionsMenu(true)

        contactViewModel.contactList.observe(viewLifecycleOwner) {

            binding.rlContactList.apply {
                if (it != null) {
                    contactList = it
                }
                adapter = it?.let { it1 ->
                    ContactListAdapter(it1, context, listItemClickListener)

                }
                dismissProgressBar()
                binding.swiperefresh.isRefreshing = false
                layoutManager = manager


            }
        }




        binding.swiperefresh.setOnRefreshListener {

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            getContactList()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        getContactList()

    }


    /* Method to fetch contact list from backend*/
    private fun getContactList() {
        if (context?.let { UiHelper.isAllPermissionGranted(it) } == true) {
            showProgressBar()
            context?.let { contactViewModel.getContactList(it) }
        } else {
            context?.let { UiHelper.showToast(it, getString(R.string.lbl_permission_error)) }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuitem_refresh -> {
                context?.let { UiHelper.showToast(it, getString(R.string.lbl_refresh_contacts)) }
                binding.swiperefresh.isRefreshing = true

                getContactList()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* show progress bar on UI*/
    private fun showProgressBar() {
        if (!isProgressVisible) {
            isProgressVisible = true
            binding.progressBar.visibility = View.VISIBLE

        }
    }

    /* close progress bar on UI*/
    private fun dismissProgressBar() {
        if (isProgressVisible) {
            isProgressVisible = false
            binding.progressBar.visibility = View.GONE
        }

    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.menuitem_refresh)
        item.isVisible = false
    }


    override fun onListItemClick(position: Int) {
        showDialog(position)
    }

    /* Show Alert dialog on click of list item*/
    private fun showDialog(position: Int) {
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(context)
        if (!contactList.isNullOrEmpty() && contactList.size >= position) {

            val contactName = contactList.get(position).name
            val contactNumber = contactList.get(position).number

            // Set the message show for the Alert time
            builder.setMessage(getString(R.string.msg_contact_dialog, contactName))

            // Set Alert Title
            builder.setTitle(getString(R.string.lbl_action))

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false)


            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton(getString(R.string.btn_call),
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    call(contactNumber)
                    dialog?.cancel()
                } as DialogInterface.OnClickListener)

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton(getString(R.string.btn_sms),
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    // If user click no then dialog box is canceled.
                    context?.let {
                        contactViewModel.sendSms(number = contactNumber, it)
                    }
                    Toast.makeText(context, getString(R.string.lbl_sending_sms), Toast.LENGTH_SHORT)
                        .show()

                    dialog.cancel()
                } as DialogInterface.OnClickListener)
            // Create the Alert dialog
            val alertDialog = builder.create()
            alertDialog.setCancelable(true)
            // Show the Alert Dialog box
            alertDialog.show()
        }
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
            context?.let { contactViewModel.initiateCall(number, it) }
        } else {
            context?.let { UiHelper.showToast(it, getString(R.string.lbl_permission_error)) }
        }


    }

}