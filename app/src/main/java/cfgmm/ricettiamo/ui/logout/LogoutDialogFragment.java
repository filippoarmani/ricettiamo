package cfgmm.ricettiamo.ui.logout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;

import cfgmm.ricettiamo.LoginActivity;
import cfgmm.ricettiamo.R;

public class LogoutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmation)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(builder.getContext() , LoginActivity.class);
                    startActivity(intent);
                    dialog.cancel();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }

}