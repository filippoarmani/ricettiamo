package cfgmm.ricettiamo.ui.profilo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cfgmm.ricettiamo.LoginActivity;
import cfgmm.ricettiamo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfiloFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfiloFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView fullName;
    private TextView email;
    private ImageView ph_profile;

    private Button buttonLogin;

    public ProfiloFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfiloFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfiloFragment newInstance(String param1, String param2) {
        ProfiloFragment fragment = new ProfiloFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profilo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        fullName = view.findViewById(R.id.nomeCognome);
        email = view.findViewById(R.id.email);
        ph_profile = view.findViewById(R.id.user);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            buttonLogin.setVisibility(View.VISIBLE);
            fullName.setVisibility(View.GONE);
            email.setVisibility(View.GONE);

            buttonLogin.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            buttonLogin.setVisibility(View.GONE);
            fullName.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);

            fullName.setText(user.getDisplayName());
            email.setText(user.getEmail());
            ph_profile.setImageURI(user.getPhotoUrl());
        }


    }
}