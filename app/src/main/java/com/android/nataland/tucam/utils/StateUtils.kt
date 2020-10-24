package com.android.nataland.tucam.utils

import android.os.Looper
import com.android.nataland.tucam.BuildConfig

fun <R> enforceMain(block: () -> R): R {
    return if (!BuildConfig.DEBUG || Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        throw IllegalStateException("`enforceMain(block)` check made from non-main thread " + Looper.myLooper())
    }
}
