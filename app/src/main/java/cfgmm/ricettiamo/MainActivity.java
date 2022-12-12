package cfgmm.ricettiamo;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

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

        /*TextView nome = findViewById(R.id.nh_nome);
        TextView email = findViewById(R.id.nh_email);
        ImageView photo = findViewById(R.id.nh_foto);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Button login = findViewById(R.id.nh_login);

        if(user != null) {
            login.setVisibility(View.GONE);
            nome.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);

            nome.setText(user.getDisplayName());
            email.setText(user.getEmail());
            photo.setImageURI(user.getPhotoUrl());
        } else {
            login.setVisibility(View.VISIBLE);
            nome.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}