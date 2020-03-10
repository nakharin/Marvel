package com.nakharin.marvel.utils.extension

import android.graphics.Color
import android.graphics.Paint
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

fun TextView.showTextIf(
    content: String?,
    condition: (() -> Boolean)?,
    visibility: Int = View.GONE
) {
    if (condition?.invoke() == true) {
        this.visibility = View.VISIBLE
        this.text = content
    } else {
        this.text = ""
        this.visibility = visibility
    }
}

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

internal fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan, callback: (String) -> Unit) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : ClickableSpan() {
        override fun onClick(view: View) {
            callback.invoke(span.url)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}

fun TextView.setTextViewHTML(html: String, callback: (String) -> Unit) {
    val sequence = Html.fromHtml(html)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
        makeLinkClickable(strBuilder, span, callback)
    }
    this.text = strBuilder
    this.highlightColor = Color.TRANSPARENT
    this.movementMethod = LinkMovementMethod.getInstance()
}
