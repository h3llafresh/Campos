package by.vlfl.campos.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.vlfl.campos.R

object PermissionUtils {
    class LocationPermissionDeniedDialog() : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity)
                .setMessage(R.string.permission_denied_dialog__permission_required_message)
                .setPositiveButton(R.string.permission_denied_dialog__positive_button_text) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                .create()
        }
    }
}