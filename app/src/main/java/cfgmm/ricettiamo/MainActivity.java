package cfgmm.ricettiamo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cfgmm.ricettiamo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cfgmm.ricettiamo.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuLaterale.toolbar);
        /*binding.appBarMenuLaterale.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_frigorifero,
                R.id.nav_listaDellaSpesa,
                R.id.nav_preferiti,
                R.id.nav_impostazioni,
                R.id.nav_upgrade,
                R.id.nav_logout,
                R.id.nav_profilo
                )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView nome = header.findViewById(R.id.nh_nome);
        ImageView photo = header.findViewById(R.id.nh_foto);

        Button login = header.findViewById(R.id.nh_login);

        if(user != null) {
            login.setVisibility(View.GONE);
            nome.setVisibility(View.VISIBLE);

            nome.setText(user.getDisplayName());
            photo.setImageURI(user.getPhotoUrl());
        } else {
            menu.removeGroup(R.id.with_login);
            login.setVisibility(View.VISIBLE);
            nome.setVisibility(View.GONE);

            login.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}