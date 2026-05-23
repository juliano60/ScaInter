package com.nanoporetech.scainter.data

import android.content.Context
import com.nanoporetech.scainter.R

object Medicine {
    fun load(context: Context): List<String> {
        return context.resources
            .openRawResource(R.raw.medicines)
            .bufferedReader()
            .useLines { lines ->
                lines
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toList()
            }
    }
}