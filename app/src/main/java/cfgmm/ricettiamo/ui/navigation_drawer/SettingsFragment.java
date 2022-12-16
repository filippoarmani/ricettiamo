package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cfgmm.ricettiamo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputLayout change_display_name;
    private TextInputLayout change_description;
    private TextInputLayout change_password_current;
    private TextInputLayout change_password_new;
    private TextInputLayout change_password_new2;
    private TextInputLayout change_email_current;
    private TextInputLayout change_email_new;
    private TextInputLayout change_email_new2;

    private ImageButton change_photo;
    private Uri image_profile;
    private Bitmap bitmap;

    private FirebaseUser user;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        change_photo = view.findViewById(R.id.change_user);
        image_profile = user.getPhotoUrl();
        change_photo.setImageURI(image_profile);

        change_display_name = view.findViewById(R.id.i_dName_layout);
        change_description = view.findViewById(R.id.modificaDescr);
        change_password_current = view.findViewById(R.id.i_cPassword_layout);
        change_password_new = view.findViewById(R.id.i_nPassword1_layout);
        change_password_new2 = view.findViewById(R.id.i_nPassword2_layout);
        change_email_current = view.findViewById(R.id.i_cEmail_layout);
        change_email_new = view.findViewById(R.id.i_nEmail1_layout);
        change_email_new2 = view.findViewById(R.id.i_nEmail2_layout);

        Button save = view.findViewById(R.id.salva);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), user.getPhotoUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        change_photo.setOnClickListener(v -> mGetContent.launch("image/*"));

        save.setOnClickListener(v -> {
            ProgressDialog progress = new ProgressDialog(v.getContext());
            progress.setTitle(R.string.progress_title);
            progress.setMessage(String.valueOf(R.string.progress_message));
            progress.setCancelable(false);
            progress.show();

            String displayName = change_display_name.getEditText().getText().toString().trim();

            String description = change_description.getEditText().getText().toString().trim();

            String cPassword = change_password_current.getEditText().getText().toString().trim();
            String nPassword1 = change_password_new.getEditText().getText().toString().trim();
            String nPassword2 = change_password_new2.getEditText().getText().toString().trim();

            String cEmail = change_email_current.getEditText().getText().toString().trim();
            String nEmail1 = change_email_new.getEditText().getText().toString().trim();
            String nEmail2 = change_email_new2.getEditText().getText().toString().trim();

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(getImageUri(view.getContext(), bitmap))
                    .build();

            user.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(v.getContext(), "Image changed",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Image not changed",
                            Toast.LENGTH_SHORT).show();
                }
            });

            if(!(isEmpty(displayName))) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .setPhotoUri(getImageUri(view.getContext(), bitmap))
                        .build();

                user.updateProfile(profileUpdates);
            }

            if(!isEmpty(description)) {
                //TODO
            }

            if(!(isEmpty(cPassword) || isEmpty(nPassword1) || isEmpty(nPassword2))) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), cPassword);

                try {
                    user.reauthenticate(credential)
                            .addOnCompleteListener(task -> {

                                if(task.isSuccessful()) {
                                    Log.d(TAG, "User re-authenticated");
                                    if(nPassword1.equals(nPassword2)) {
                                        user.updatePassword(nPassword1).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "User password updated.");
                                            }
                                        });
                                    } else {
                                        change_password_new.setError("Passwords not equal");
                                        change_password_new2.setError("Passwords not equal");
                                    }

                                } else {
                                    change_password_current.setError("Wrong Password");
                                }
                            });
                } catch(Exception e) {
                    Toast.makeText(v.getContext(), "Error",
                            Toast.LENGTH_SHORT).show();
                }

            }

            if(!(isEmpty(cEmail) || isEmpty(nEmail1) || isEmpty(nEmail2))) {
                if(nEmail1.equals(nEmail2)) {
                    if(cEmail.equals(user.getEmail())) {
                        if(EmailValidator.getInstance().isValid(nEmail1))
                            user.updateEmail(nEmail1).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                }
                            });
                        else
                            change_email_new.setError("Email not Valid");
                    } else {
                        change_email_current.setError("Wrong Email");
                    }
                } else {
                    change_email_new.setError("Emails not equal");
                    change_email_new2.setError("Emails not equal");
                }
            }

            progress.dismiss();
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        bitmap = getCroppedBitmap(bitmap);
                        change_photo.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage,
                user.getUid()+"_photo",
                null);
        return Uri.parse(path);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}