package cfgmm.ricettiamo.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ProfileFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextView fullName = view.findViewById(R.id.nomeCognome);
        TextView email = view.findViewById(R.id.email);
        ImageView ph_profile = view.findViewById(R.id.user);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        Button myRecipes = view.findViewById(R.id.ric);

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
            myRecipes.setOnClickListener(v ->{
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_profile_to_nav_my_recipe2);
            });

            fullName.setText(user.getDisplayName());
            email.setText(user.getEmail());
            ph_profile.setImageURI(user.getPhotoUrl());
        }
    }
}