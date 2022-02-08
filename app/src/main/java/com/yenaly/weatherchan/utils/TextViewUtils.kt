package com.yenaly.weatherchan.utils

import android.text.TextUtils
import android.widget.TextView

/**
 * @ProjectName : Weather-chan
 * @Author : Yenaly Liew
 * @Time : 2022/02/08 008 21:20
 * @Description : TextView的工具库。
 */

object TextViewUtils {

    /**
     * 将TextView中的text跑马灯化。
     *
     * @param textView TextView。
     * @param marqueeRepeatLimit 跑马灯滚动次数，默认值为-1（无限循环）。
     */
    fun makeTextMarquee(textView: TextView, marqueeRepeatLimit: Int = -1) {
        textView.isFocusable = true
        textView.isFocusableInTouchMode = true
        textView.isSelected = true
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.marqueeRepeatLimit = marqueeRepeatLimit
    }

}