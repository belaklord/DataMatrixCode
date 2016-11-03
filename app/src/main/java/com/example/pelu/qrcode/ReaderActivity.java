package com.example.pelu.qrcode;

/*
DOCUMENTAR EL CÓDIGO
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Date;

import static com.example.pelu.qrcode.R.id.albaran;
import static com.example.pelu.qrcode.R.id.camion;
import static com.example.pelu.qrcode.R.id.matricula_remolque;
import static com.example.pelu.qrcode.R.id.textBox;
import static com.example.pelu.qrcode.R.id.texto22;

public class ReaderActivity extends AppCompatActivity {


    private Button scan_btn, btn_Guardar; // variable del boton //


    TextView DataMatrix;

    private TextView  contentTxt, dateContent, contentTxt2, albaranes, matriculaCamion, matriculaRemolque,textoBloque,texto2, tvIsConnected;

    final Activity activity = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_reader);


        /*
        captura de los campos del archivo xml
         */

        contentTxt = (TextView)findViewById(R.id.scan_content);
        dateContent = (TextView)findViewById(R.id.date_content);
        contentTxt2 = (TextView)findViewById(R.id.scan_content2);
        albaranes = (TextView)findViewById(albaran);
        matriculaCamion =(TextView)findViewById(camion);
        matriculaRemolque = (TextView)findViewById(matricula_remolque);
        textoBloque = (TextView)findViewById(textBox);
        texto2 = (TextView)findViewById(texto22);
        tvIsConnected = (TextView)findViewById(R.id.tvIsConnected);



        btn_Guardar = (Button) findViewById(R.id.send_btn); // boton de guardar


        btn_Guardar.setOnClickListener(new View.OnClickListener(){


            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) // version minima de android //
            @Override
            public void onClick(View v) {




                            /*
                COMPROBACION DE LA CONEXION
                            */

                if(isConnected()){

                        // se envían los datos que no se hayan envíado // (base de datos --> campo "enviado")
                }

                else{

                        //mensaje de que no hay conexion //
                }


            }


        });



        scan_btn = (Button) findViewById(R.id.scan_btn); // obtiene el noton de scan para mostrarlo //


        scan_btn.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) // version minima de android //
            @Override
            public void onClick(View v) {


                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX_TYPES); // tipod de codigos, se puede cambiar //
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();


            }
        });


        if(isConnected()){

            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("Hay conexión");
        }

        else{

            tvIsConnected.setText("No hay conexión");
        }
    }




    public boolean isConnected() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            return true;
        } else {
            return false;
        }




    }


    @RequiresApi(api = Build.VERSION_CODES.N) // requerido para la fecha y hora //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final AyudaBD ayudabd = new AyudaBD(getApplicationContext());


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String scanContent = result.getContents();






                                            /*
                            variable de escritura de la base de datos
                                            */

        SQLiteDatabase db = ayudabd.getWritableDatabase();


        String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());


        /*

                               INSERCION DATOS TABLA  CABECERA (CAMBIAR A LA CLASE DataBase)

                                                 */

           ContentValues valores = new ContentValues();

            valores.put(AyudaBD.DatosTabla.COLUMNA_ID,albaranes.getText().toString());
            valores.put(AyudaBD.DatosTabla.COLUMNA_MATRICULA1, matriculaCamion.getText().toString());
            valores.put(AyudaBD.DatosTabla.COLUMNA_MATRICULA2, matriculaRemolque.getText().toString());
            valores.put(AyudaBD.DatosTabla.FECHA, date );

            ContentValues valores2 = new ContentValues();




           valores2.put(AyudaBD.Lineas.LINEA_PEDIDO, scanContent);
           valores2.put(AyudaBD.Lineas.ID_ALBARAN, albaranes.getText().toString());


        db.insert(AyudaBD.Lineas.NOMBRE_TABLA, AyudaBD.Lineas.LINEA_PEDIDO, valores2);
        db.insert(AyudaBD.DatosTabla.NOMBRE_TABLA, AyudaBD.DatosTabla.COLUMNA_ID,  valores);


       Toast.makeText(getApplicationContext(), "Se guardo el dato: ", Toast.LENGTH_LONG).show();



                                        /*
                                CONSULTA DE LAS DOS TABLAS
                                        */

        SQLiteDatabase db2 = ayudabd.getReadableDatabase();

        textoBloque.setText(DataBase.DatosTabla(db2, AyudaBD.DatosTabla.NOMBRE_TABLA));
        contentTxt2.setText(DataBase.DatosTabla(db2, AyudaBD.Lineas.NOMBRE_TABLA));

        dateContent.setText("FECHA:" + date);


        db.close();


                                                /*
                                TRATAMIENTO DEL RESULTADO DE LOS ESCANEOS
                                                */



        if(result == null){
            Toast.makeText(this, "no hay datos", Toast.LENGTH_LONG).show();
        }
        if(result  !=null){

            if(result.getContents() == null){

                Toast.makeText(this, "Has cancelado el scanner", Toast.LENGTH_SHORT).show();
            }

            else{

                /*
                FECHA Y HORA DEL ESCANEO --> eliminar para que no salga en la pantalla
                 */




            }
        }

        else{


            super.onActivityResult(requestCode, resultCode, data);

        }

    }



}
