package io.incepted.ultrafittimer.databinding

import androidx.databinding.BindingAdapter
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.incepted.ultrafittimer.adapter.PresetAdapter
import io.incepted.ultrafittimer.adapter.SummaryAdapter
import io.incepted.ultrafittimer.db.model.Preset
import io.incepted.ultrafittimer.db.tempmodel.Round
import io.incepted.ultrafittimer.util.SwipeDeleteCallback
import timber.log.Timber

object DataBindingAdapters {

    infix fun Int.with(x: Int) = this.or(x)

    @JvmStatic
    @BindingAdapter("presetData")
    fun setPrsetListData(v: androidx.recyclerview.widget.RecyclerView, data: List<Preset>) {
        val adapter: PresetAdapter = v.adapter as PresetAdapter
        adapter.replaceData(data as MutableList<Preset>)
    }

    @JvmStatic
    @BindingAdapter("roundData")
    fun setRoundData(v: RecyclerView, data: List<Round>) {
        val adapter: SummaryAdapter = v.adapter as SummaryAdapter
        adapter.replaceData(data as MutableList<Round>)
        Timber.d("from adapter ${data.size}")
    }


    @JvmStatic
    @BindingAdapter("onFocusChangedListener")
    fun setFocusListener(editText: EditText, listener: View.OnFocusChangeListener) {
        editText.onFocusChangeListener = listener
    }


    @JvmStatic
    @BindingAdapter("customized")
    fun setTextAppearance(v: TextView, isCustomized: Boolean) {
        v.text = if (isCustomized) "CUSTOMIZED" else "CUSTOMIZE"
        if (isCustomized) {
            v.paintFlags = v.paintFlags with Paint.UNDERLINE_TEXT_FLAG
        }
    }


    @JvmStatic
    @BindingAdapter("swipeDeleteListener")
    fun setSwipeDeleteListener(v: androidx.recyclerview.widget.RecyclerView, listener: SwipeDeleteCallback) {
        val itemTouchHelper = ItemTouchHelper(listener)
        itemTouchHelper.attachToRecyclerView(v)
    }


    @JvmStatic
    @BindingAdapter("positionText")
    fun setPositionText(v: TextView, dummy: Int) {
        val position: Int? = (v.parent as View).tag as Int?
        val suffix: String = when (position?.rem(10)) {
            1 -> "st."
            2 -> "nd."
            3 -> "rd."
            else -> "th."
        }
        val result = "$position$suffix"
        v.text = result
    }


    @JvmStatic
    @BindingAdapter("itemBackground")
    fun setItemBackground(v: androidx.constraintlayout.widget.ConstraintLayout, dummy: Int) {
        val position: Int? = v.tag as Int?

        v.setBackgroundColor(
                if (position?.rem(2) == 0)
                    Color.parseColor("#f8f7f7")
                else Color.WHITE)

    }

}

