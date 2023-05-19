package com.task.codematch.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemMarginDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        if (position == 0) {
            outRect.set(margin / 2, margin / 2, margin / 2, margin / 4)
        } else if (position == itemCount - 1) {
            outRect.set(margin / 2, margin / 4, margin / 2, margin)
        } else {
            outRect.set(margin / 2, margin / 4, margin / 2, margin / 4)
        }
    }
}