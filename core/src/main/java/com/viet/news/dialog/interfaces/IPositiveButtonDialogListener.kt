package com.viet.news.dialog.interfaces

/**
 * Implement this interface in Activity or Fragment to react to positive dialog buttons.
 *
 * @author Tomáš Kypta
 * @since 2.1.0
 */
interface IPositiveButtonDialogListener : DialogListener {

    fun onPositiveButtonClicked(requestCode: Int)
}
