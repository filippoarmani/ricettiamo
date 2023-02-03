package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class RankingFragment extends Fragment {

    private UserViewModel userViewModel;

    public RankingFragment() {
        // Required empty public constructor
    }


    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_ranking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        CircularProgressIndicator progressIndicator = view.findViewById(R.id.ra_progress_circular);

        CardView[] cardViews = {
                view.findViewById(R.id.card1),
                view.findViewById(R.id.card2),
                view.findViewById(R.id.card3),
                view.findViewById(R.id.card4),
                view.findViewById(R.id.card5),
                view.findViewById(R.id.card6),
                view.findViewById(R.id.card7),
                view.findViewById(R.id.card8),
                view.findViewById(R.id.card9),
                view.findViewById(R.id.card10)
        };

        TextView[] displayNameTextViews = {
                view.findViewById(R.id.displayName1),
                view.findViewById(R.id.displayName2),
                view.findViewById(R.id.displayName3),
                view.findViewById(R.id.displayName4),
                view.findViewById(R.id.displayName5),
                view.findViewById(R.id.displayName6),
                view.findViewById(R.id.displayName7),
                view.findViewById(R.id.displayName8),
                view.findViewById(R.id.displayName9),
                view.findViewById(R.id.displayName10)
        };

        TextView[] starsTextViews = {
                view.findViewById(R.id.star1),
                view.findViewById(R.id.star2),
                view.findViewById(R.id.star3),
                view.findViewById(R.id.star4),
                view.findViewById(R.id.star5),
                view.findViewById(R.id.star6),
                view.findViewById(R.id.star7),
                view.findViewById(R.id.star8),
                view.findViewById(R.id.star9),
                view.findViewById(R.id.star10)
        };

        userViewModel.getTopTen().observe(getViewLifecycleOwner(), result -> {
            progressIndicator.setVisibility(View.VISIBLE);
            if(result.isSuccess()) {
                User[] topTen = ((Result.TopTenResponseSuccess) result).getData();
                for(int i=0; i<10; i++) {
                    if(topTen[i] != null) {
                        cardViews[i].setVisibility(View.VISIBLE);
                        displayNameTextViews[i].setText(topTen[i].getDisplayName());
                        String star = "" + topTen[i].getTotalStars();
                        starsTextViews[i].setText(star);
                    } else {
                        cardViews[i].setVisibility(View.GONE);
                    }
                }
            } else {
                Result.Error error = ((Result.Error) result);
                Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, v -> userViewModel.getTopTen())
                        .show();

                for(int i=0; i<10; i++) {
                    cardViews[i].setVisibility(View.GONE);
                }
            }
            progressIndicator.setVisibility(View.GONE);
        });
    }
}