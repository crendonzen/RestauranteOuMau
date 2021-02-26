package com.example.myapplication.interfaz;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaMesa;
import com.example.myapplication.adaptador.AdaptadorListaMesaDesocupada;
import com.example.myapplication.mundo.Mesa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PedidosMesaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosMesaFragment extends Fragment
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private SearchView buscarMesa;
    private RecyclerView listaMesas, mesasDesocupadas;
    private AdaptadorListaMesa adaptadorListaMesa;
    private AdaptadorListaMesaDesocupada adaptadorListaMesaDesocupada;
    private ArrayList<Mesa> mesasDes;
    private ArrayList<Mesa> mesasDesAux;
    private ArrayList<Mesa> mesas;
    private ArrayList<Mesa> mesasAux;
    private ProgressDialog dialog;
    private int cantMesas ;
    private Activity actividad;
    private InterfazFragamen interfazFragamen;
    ImageButton agregarMesa;
    Dialog mDialog;
    TextView numeroMesa;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PedidosMesaFragment()
    {
        // Required empty public constructor
    }

    public static PedidosMesaFragment newInstance(String param1, String param2)
    {
        PedidosMesaFragment fragment = new PedidosMesaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_pedidos_mesa, container, false);

        this.agregarMesa = v.findViewById(R.id.AgregarPedido);
        this.buscarMesa = v.findViewById(R.id.searchBuscarPlato);
        this.listaMesas = v.findViewById(R.id.listaPlatosMesas);
        this.mesasDesocupadas= v.findViewById(R.id.listaMesasDesocupadas);
        numeroMesa=v.findViewById(R.id.numeroMesa);
        this.requestQueue = Volley.newRequestQueue(getContext());
        this.mesasDes = new ArrayList<Mesa>();
        this.mesasDesAux=new ArrayList<Mesa>();
        this.mesas = new ArrayList<Mesa>();
        this.mesasAux = new ArrayList<Mesa>();
        this.adaptadorListaMesa = new AdaptadorListaMesa(getContext (),this.mesas);
        this.listaMesas.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mesasDesocupadas.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adaptadorListaMesaDesocupada = new AdaptadorListaMesaDesocupada(getContext (),this.mesasDes);
        mesasDesocupadas.setAdapter(adaptadorListaMesaDesocupada);
        this.listaMesas.setLayoutManager(new GridLayoutManager(getContext(),3));

        new Timer ().scheduleAtFixedRate(new TimerTask ()
        {
            @Override
            public void run()
            {
                buscarlista ();
                buscarMesaDesocupada();
                System.out.println ("A Kiss after 5 seconds");
            }
        },1,5000);

        this.buscarMesa.setOnQueryTextListener (new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String query) { return false;  }
            @Override
            public boolean onQueryTextChange( String newText)
            {
                mesas.clear();
                listaMesas.setAdapter(adaptadorListaMesa);

                if(!newText.isEmpty())
                {
                    for (Mesa m: mesasAux )
                    {
                        if (m.getNumero().contains (newText)  )
                        {
                            mesas.add (m);
                        }
                    }
                }else
                {
                    mesas.addAll(mesasAux);
                }
                return false;
            }
        });
        this.adaptadorListaMesa.setOnclickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String numeroMesa = mesas.get(listaMesas.getChildAdapterPosition(v)).getNumero();


                //enviar mediante la interface el objeto seleccionado al detalle
                //se envia el objeto completo
                //se utiliza la interface como puente para enviar el objeto seleccionado
                interfazFragamen.enviarMesa(mesas.get(listaMesas.getChildAdapterPosition(v)));
                //luego en el mainactivity se hace la implementacion de la interface para implementar el metodo enviarpersona


            }
        });

        agregarMesa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarVentana(v);
            }
        });
        mDialog = new Dialog(getContext());
        return v;

    }
    public void buscarMesaDesocupada()
    {
        mesasDesAux.clear();
        Map<String,String> params= new HashMap<String, String> ();
        params.put("buscarMesasDesocupadas","Mes");
        JSONObject parameters = new JSONObject(params);
        String url="https://openm.co/consultas/pedidos.php";

        jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    mesasDesocupadas.setAdapter(adaptadorListaMesaDesocupada);
                    JSONArray datos = response.getJSONArray ("datos");
                    for (int i = 0; i < datos.length(); i++)
                    {
                        JSONObject mesa = datos.getJSONObject(i);
                        int id=mesa.getInt ("idmesas");
                        String numero=mesa.getString("numero");
                        String codigoQR=mesa.getString("codigoQR");
                        String estado=mesa.getString("estado");
                        Mesa m=new Mesa( id,  numero,  codigoQR, estado);
                        mesasDesAux.add(m);

                    }

                    if (mesasDesAux.size()!=cantMesas)
                    {
                        mesasDes.clear();
                        String numero = mesasDesAux.get (mesasDesAux.size () - 1).getNumero ();
                        //  Toast.makeText(getContext (),"Mesa "+numero+" ha sido ocupada", Toast.LENGTH_SHORT).show();
                        mesasDes.addAll(mesasDesAux);
                        cantMesas=mesasDes.size ();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener ()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {error.printStackTrace ();
            }
        });
        requestQueue.add(jsonRequest);
    }


    public void buscarlista()
    {
        mesasAux.clear();
        Map<String,String> params= new HashMap<String, String> ();
        params.put("buscarMesas","Mes");
        JSONObject parameters = new JSONObject(params);
        String url="https://openm.co/consultas/pedidos.php";
        jsonRequest=new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    listaMesas.setAdapter(adaptadorListaMesa);
                    JSONArray datos = response.getJSONArray ("datos");
                    for (int i = 0; i < datos.length(); i++)
                    {
                        JSONObject mesa = datos.getJSONObject(i);
                        int id=mesa.getInt ("idmesas");
                        String numero=mesa.getString("numero");
                        String codigoQR=mesa.getString("codigoQR");
                        String estado=mesa.getString("estado");
                        Mesa m=new Mesa( id,  numero,  codigoQR, estado);
                        mesasAux.add(m);
                    }
                    if (mesasAux.size()!=cantMesas)
                    {
                        mesas.clear();
                        String numero = mesasAux.get (mesasAux.size () - 1).getNumero ();
                      //  Toast.makeText(getContext (),"Mesa "+numero+" ha sido ocupada", Toast.LENGTH_SHORT).show();
                        mesas.addAll(mesasAux);
                        cantMesas=mesas.size ();
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener ()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {error.printStackTrace ();
            }
        });
        requestQueue.add(jsonRequest);
    }


    public void mostrarVentana(View v)
    {
        mDialog.setContentView(R.layout.registrar_mesa);
        mDialog.show();
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity)
        {
            this.actividad = (Activity) context;
            this.interfazFragamen = (InterfazFragamen) this.actividad;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}