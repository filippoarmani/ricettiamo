package cfgmm.ricettiamo.ui.navigation_drawer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.HomeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView rv_starters, rv_first, rv_second, rv_desserts;
    FloatingActionButton floatingActionButton;

    private final int[] st_i = {R.drawable.esempio_guacamole, R.drawable.esempio_mozzarella, R.drawable.esempio_antipasto, R.drawable.esempio_finger};
    private final int[] p_i = {R.drawable.esempio_pasta_forno, R.drawable.esempio_pasta_ricotta_zafferano_speck, R.drawable.esempio_risotto, R.drawable.esempio_ravioli};
    private final int[] s_i = {R.drawable.esempio_branzino_acqua_pazza, R.drawable.esempio_gateau, R.drawable.esempio_polpette, R.drawable.esempio_polpettone_carne};
    private final int[] d_i = {R.drawable.esempio_tiramisu, R.drawable.esempio_torta_magica, R.drawable.esempio_cheesecake, R.drawable.esempio_sorbetto};

    private final String[] st_t = {"Guacamole", "Mozzarella in Carrozza", "Bicchierini con formaggio e verdure", "Finger Food"};
    private final String[] p_t = {"Pasta al forno", "Pasta allo zafferano", "Risotto", "Ravioli"};
    private final String[] s_t = {"Branzino", "Gateau di patate", "Polpette", "Polpettone"};
    private final String[] d_t = {"Tiramisu", "Torta", "Cheesecake", "Sorbetto"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_m_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        floatingActionButton = view.findViewById(R.id.search_floating_button);

        rv_starters = view.findViewById(R.id.rv_starters);
        rv_first = view.findViewById(R.id.rv_first);
        rv_second = view.findViewById(R.id.rv_second);
        rv_desserts = view.findViewById(R.id.rv_dessert);

        HomeAdapter adapterSt = new HomeAdapter(view.getContext(), st_t, st_i);
        HomeAdapter adapterP = new HomeAdapter(view.getContext(), p_t, p_i);
        HomeAdapter adapterS = new HomeAdapter(view.getContext(), s_t, s_i);
        HomeAdapter adapterD = new HomeAdapter(view.getContext(), d_t, d_i);

        rv_starters.setAdapter(adapterSt);
        rv_first.setAdapter(adapterP);
        rv_second.setAdapter(adapterS);
        rv_desserts.setAdapter(adapterD);

        rv_starters.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rv_first.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rv_second.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rv_desserts.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));

        floatingActionButton.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_search_recipes));
        rv_first.addOnScrollListener(new RecyclerView.OnScrollListener() {

           /* @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isConnected = isConnected();
                if (isConnected && totalItemCount != newsViewModel.getTotalResults()) {

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (totalItemCount == visibleItemCount ||
                                    (totalItemCount <= (lastVisibleItem + threshold) &&
                                    dy > 0 &&
                                    !newsViewModel.isLoading()
                            ) &&
                                    newsViewModel.getNewsResponseLiveData().getValue() != null &&
                                    newsViewModel.getCurrentResults() != newsViewModel.getTotalResults()
                    ) {
                        MutableLiveData<Result> newsListMutableLiveData = newsViewModel.getNewsResponseLiveData();

                        if (newsListMutableLiveData.getValue() != null &&
                                newsListMutableLiveData.getValue().isSuccess()) {

                            newsViewModel.setLoading(true);
                            newsList.add(null);
                            newsRecyclerViewAdapter.notifyItemRangeInserted(newsList.size(),
                                    newsList.size() + 1);

                            int page = newsViewModel.getPage() + 1;
                            newsViewModel.setPage(page);
                            newsViewModel.fetchNews(country);
                        }
                    }
                }
            }*/
        });

    }



}