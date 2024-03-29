package cfgmm.ricettiamo.ui.navigation_drawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.ActivityMainBinding;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;
import cfgmm.ricettiamo.ui.authentication.AuthenticationActivity;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private UserViewModel userViewModel;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        cfgmm.ricettiamo.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuLaterale.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_fridge,
                R.id.nav_shoppingList,
                R.id.nav_favourites,

                R.id.nav_profile,
                R.id.nav_add_new_recipe,
                R.id.nav_settings
                )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);

        TextView nome = header.findViewById(R.id.nh_nome);
        ImageView photoProfile = header.findViewById(R.id.nh_foto);

        Button login = header.findViewById(R.id.nh_login);

        userViewModel.getCurrentPhotoLiveData().observe(this, result -> {
            if(result.isSuccess()) {
                Uri photo = ((Result.PhotoResponseSuccess) result).getData();
                try {
                    Glide.with(this)
                            .load(photo)
                            .circleCrop()
                            .placeholder(R.drawable.user)
                            .into(photoProfile);
                } catch (Exception e) { }
            }
        });

        userViewModel.getCurrentUserLiveData().observe(this, result -> {
            if(userViewModel.isLoggedUser() && result.isSuccess()) {
                User currentuser = ((Result.UserResponseSuccess) result).getData();
                menu.setGroupVisible(R.id.with_login, true);

                login.setVisibility(View.GONE);
                nome.setVisibility(View.VISIBLE);

                nome.setText(currentuser.getDisplayName());
            } else {
                menu.setGroupVisible(R.id.with_login, false);

                login.setVisibility(View.VISIBLE);
                nome.setVisibility(View.GONE);

                login.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), AuthenticationActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}