package by.vlfl.campos.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.vlfl.campos.R

class DefaultPermissionDeniedDialog : DialogFragment() {
    var message: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setMessage(checkNotNull(message) {
                "Permission denied dialog was created without message value"
            })
            .setPositiveButton(R.string.common_action_ok) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()
    }
}