package com.example.myapplication.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.MenuPlatosFragment;
import com.example.myapplication.mundo.Plato;

import java.util.ArrayList;

public class AdaptadorListaPlatos extends  RecyclerView.Adapter<AdaptadorListaPlatos.ViewHolder> {

    private Plato plato;
    private ArrayList<Plato> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;

    public AdaptadorListaPlatos(Context conexto, ArrayList<Plato> lista)
    {
        this.list=lista;
        inflater = (LayoutInflater ) conexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.fragment_lista_menu_platos, null);
        return new AdaptadorListaPlatos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        plato = this.list.get(position);
        holder.txtNombrePlato.setText(list.get(position).getNombre ());

        Glide.with(inflater.getContext ())
                .load(list.get(position).getImage())
                .into(holder.imgPlatos);

    }

    @Override
    public int getItemCount() {
            return list.size();
    }
    public void setFragment(MenuPlatosFragment buscarPlato)
    {
        this.buscarPlato=buscarPlato;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNombrePlato,txtPrecioPlato,txtCategoriaPlato,txtDescripcionPlato;
        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombrePlato=(TextView) itemView.findViewById(R.id.txtNombrePlatoMenu);
            txtPrecioPlato=(TextView) itemView.findViewById(R.id.txtPrecioPlato);
            txtCategoriaPlato=(TextView) itemView.findViewById(R.id.txtCategoriaPlatoMenu);
            txtDescripcionPlato=(TextView) itemView.findViewById(R.id.txtPrecioMenu);
            imgPlatos=(ImageView) itemView.findViewById(R.id.imgPlatosMenu);
        }
    }

}
