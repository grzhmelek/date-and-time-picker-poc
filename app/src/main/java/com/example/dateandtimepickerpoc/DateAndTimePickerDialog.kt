package com.example.dateandtimepickerpoc

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.dateandtimepickerpoc.databinding.DateAndTimePickerBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DateAndTimePickerDialog : DialogFragment(), View.OnClickListener {

    lateinit var binding: DateAndTimePickerBinding

    lateinit var dates: Array<String>

    private var rightNow: Calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            binding = DateAndTimePickerBinding.inflate(layoutInflater)
            AlertDialog.Builder(requireContext(), R.style.Theme_DateAndTimePicker).apply {
                setView(binding.root)
                setTitle("Select date and time")
                setPositiveButton("Done") { dialog, whichButton ->
                    //do something
                }
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_DateAndTimePicker)
        dates = datesFromCalender
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.customPickerDateFull.minValue = 0
        binding.customPickerDateFull.maxValue = dates.size - 1
        binding.customPickerDateFull.setFormatter { value -> dates[value] }
        binding.customPickerDateFull.displayedValues = dates
        binding.customPickerDateFull.setDividerColor(getParsedColor(R.color.colorPrimary))

        binding.customPickerHour.minValue = 0
        binding.customPickerHour.maxValue = 23
        binding.customPickerHour.value = rightNow.get(Calendar.HOUR_OF_DAY)
        binding.customPickerHour.setDividerColor(getParsedColor(R.color.colorPrimary))

        binding.customPickerMinute.minValue = 0
        binding.customPickerMinute.maxValue = 59
        binding.customPickerMinute.value = rightNow.get(Calendar.MINUTE)
        binding.customPickerMinute.setDividerColor(getParsedColor(R.color.colorPrimary))

        return binding.root
    }

    private val datesFromCalender: Array<String>
        get() {
            val c1: Calendar = Calendar.getInstance()
            val c2: Calendar = Calendar.getInstance()
            val dates: MutableList<String> = ArrayList()
            val dateFormat: DateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
            dates.add("Today")
            for (i in 0..59) {
                c1.add(Calendar.DATE, 1)
                dates.add(dateFormat.format(c1.time))
            }
            c2.add(Calendar.DATE, -61)
            for (i in 0..59) {
                c2.add(Calendar.DATE, 1)
                dates.add(dateFormat.format(c2.time))
            }
            return dates.toTypedArray()
        }

    override fun onClick(v: View) {
//        val args = Bundle()
//        if (v.id === R.id.doneButton) {
//            //Handle selected time and date
//            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
//        } else if (v.id === R.id.currentTimeButton) {
//        }
    }

    private fun getParsedColor(colorRes: Int): Int {
        return Color.parseColor(
            "#${
                Integer.toHexString(
                    ContextCompat.getColor(
                        binding.customPickerDateFull.context,
                        colorRes
                    )
                )
            }"
        )
    }

    private fun NumberPicker.setDividerColor(color: Int) {
        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    val colorDrawable = ColorDrawable(color)
                    pf[this] = colorDrawable
                } catch (e: java.lang.IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
    }
}