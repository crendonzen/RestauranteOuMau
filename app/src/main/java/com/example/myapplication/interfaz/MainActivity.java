package com.example.myapplication.interfaz;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.Fragments.DetallePlatoFragment;
import com.example.myapplication.Fragments.PlatosFragment;
import com.example.myapplication.R;
import com.example.myapplication.mundo.Mesa;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InterfazFragamen
{
    Button cerrarSesion;
    private AppBarConfiguration mAppBarConfiguration;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    TextView nombreUsuario;
    ImageButton menu,agregarPedido,pedidos;

    private MenuPlatosFragment menuPlatosFragment;


    @Override


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cerrarSesion=findViewById(R.id.botonCerrarSesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences preferences= getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent intent= new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nombreUsuario=findViewById(R.id.textView3);

        recuperarPreferencias();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        menu=findViewById(R.id.BotonInicioMenu);

        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                irMenu();
            }
        });

        agregarPedido = findViewById(R.id.botonInicioPlatos);

        agregarPedido.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               irPlatos();
            }
        });

        pedidos=findViewById(R.id.botonInicioMesas);
        pedidos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                irPedidos();
            }
        });


    }

    private void irMenu()
    {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
        fragmentTransaction.commit();
    }

    private void irPlatos()
    {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new PlatosFragment());
        fragmentTransaction.commit();
    }
    private void irPedidos() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new PedidosFragment());
        fragmentTransaction.commit();
    }

    private void recuperarPreferencias()
    {
        SharedPreferences preferences= getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        boolean sesion=preferences.getBoolean("sesion",false);
        if(sesion)
        {
            String nick=preferences.getString("nick", "No hay nada");
            nombreUsuario.setText (nick);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
/*
    public void enviarPlato(Platos platos) {

        detallePlatoFragment = new DetallePlatoFragment();
        //objeto bundle para transportar la informacion
        Bundle bundleEnvio = new Bundle();
        //se manda el objeto que le esta llegando:
        bundleEnvio.putSerializable("objetoPlato", platos);
        detallePlatoFragment.setArguments(bundleEnvio);

        //CArgar fragment en el activity
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detallePlatoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.nav_home)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            fragmentTransaction.commit();
        }
        return false;
    }


    @Override
    public void enviarMesa(Mesa mesa)
    {
        menuPlatosFragment = new MenuPlatosFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("mesa", mesa);
        menuPlatosFragment.setArguments(bundleEnvio);


        //CArgar fragment en el activity
       fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, menuPlatosFragment);

        fragmentTransaction.commit();
    }
}