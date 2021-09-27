package com.carterjfowler.carterjfowler_a3.History

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carterjfowler.carterjfowler_a3.Data.LocationData
import com.carterjfowler.carterjfowler_a3.R


//, private val clickListener: (LocationData) -> Unit
//TODO: May need to change the history holder to something else as thats not what it is in the other program
class HistoryListAdapter(private val historyListViewModel: HistoryListViewModel) :
    PagedListAdapter<LocationData, HistoryHolder>(DIFF_UTIL),
    SwipeToDeleteHelper.ItemTouchHelperAdapter {

    private lateinit var attachedRecyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.history_page_item, parent, false)
        return HistoryHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val location = getItem(position)
        if (location != null) {
            holder.bind(location)
        } else {
            holder.clear()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedRecyclerView = recyclerView
    }

    override fun onItemDismiss(position: Int) {
        val context = attachedRecyclerView.context
        val location = getItem(position)
        if (location != null) {
            AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete)
                .setMessage(context.resources.getString(R.string.confirm_delete_message))
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    historyListViewModel.deleteLocation(
                        location.longitude,
                        location.latitude,
                        location.time
                    )
                }
                .setNegativeButton(android.R.string.no) { _, _ -> notifyItemChanged(position) }
                .show()
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<LocationData>() {
            override fun areContentsTheSame(oldItem: LocationData, newItem: LocationData): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: LocationData, newItem: LocationData): Boolean =
                oldItem.id == newItem.id
        }
    }
}