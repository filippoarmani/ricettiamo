package cfgmm.ricettiamo.ui.navigation_drawer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.ui.authentication.AuthenticationActivity;

public class LogoutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmation)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(builder.getContext() , AuthenticationActivity.class);
                    startActivity(intent);
                    dialog.cancel();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }

}