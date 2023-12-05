package com.dhy.stableimagesourceDemo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

class IncludeLayout : FrameLayout {
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private var mLayoutResource = 0
    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.IncludeLayout)
        mLayoutResource = a.getResourceId(R.styleable.IncludeLayout_exLayout, 0)
        a.recycle()
        addView()
    }

    private fun addView() {
        if (mLayoutResource != 0) {
            val inflater = LayoutInflater.from(context)
            addView(inflater.inflate(mLayoutResource, null, false))
        }
    }

    private var executed = false
    private val util = ExecutableLayoutUtil(this)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isInEditMode) {
            util.start()
        } else if (!executed) {
            util.start()
            executed = true
        }
    }
}