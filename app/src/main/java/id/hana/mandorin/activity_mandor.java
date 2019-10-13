package id.hana.mandorin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

public class activity_mandor extends AppCompatActivity {

    /*
     * Recyclerview & Data Adapter Initialization
     */
    List<GetDataAdapter> GetDataAdapter1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    /*
     * JSON Data Initialization
     */
    String GET_JSON_DATA_HTTP_URL = "http://mandorin.site/mandorin/php/user/image.php";
    String JSON_NAMA_MANDOR = "nama_mandor";
    String JSON_NIK_MANDOR = "nik";
    String JSON_UMUR_MANDOR = "umur_mandor";
    String JSON_ALAMAT_MANDOR = "alamat_mandor";
    String JSON_FOTO_MANDOR = "foto_mandor";
    String JSON_TGL_LAHIR = "tgl_lahir";
    String JSON_TEMPAT = "tempat";
    String JSON_AGAMA = "agama";
    String JSON_LAMA_KERJA = "lama_kerja";
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;

    /*
     * Layout Component Initializations
     * Textview, Imageview, CardView & Button
     */
     private TextView con_text;
     private ImageView connection;
     private Button refresh;
     private CardView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_mandor);

        /*
         * Recyclerview Layout Initialization
         */
        GetDataAdapter1 = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        /*
         * Layout ID Initializations
         * Textview, Imageview & Button
         */
        con_text = findViewById(R.id.con_text);
        connection = findViewById(R.id.con_image);
        refresh = findViewById(R.id.refresh);
        back = findViewById(R.id.back_activity_mandor);

        /*
         * Internet Connection Module
         * Check user internet before main function run is needed to avoid
         * Null pointer access
         */
        cek_internet();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek_internet();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_mandor.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cek_internet() {
        if(internet_available()){
            connection.setVisibility(View.GONE);
            con_text.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            JSON_DATA_WEB_CALL();
        }else{
            connection.setVisibility(View.VISIBLE);
            con_text.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.VISIBLE);
        }
    }

    private boolean internet_available(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                GetDataAdapter2.setNik_mandor(json.getString(JSON_NIK_MANDOR));
                GetDataAdapter2.setNama_mandor(json.getString(JSON_NAMA_MANDOR));
                GetDataAdapter2.setUmur_mandor(json.getString(JSON_UMUR_MANDOR));
                GetDataAdapter2.setAlamat_mandor(json.getString(JSON_ALAMAT_MANDOR));
                GetDataAdapter2.setTempat(json.getString(JSON_TEMPAT));
                GetDataAdapter2.setTgl_lahir(json.getString(JSON_TGL_LAHIR));
                GetDataAdapter2.setAgama(json.getString(JSON_AGAMA));
                GetDataAdapter2.setLama_kerja(json.getString(JSON_LAMA_KERJA));
                GetDataAdapter2.setFoto_mandor(json.getString(JSON_FOTO_MANDOR));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, this);

        recyclerView.setAdapter(recyclerViewadapter);
    }
}