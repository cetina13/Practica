package com.app.practicaguiada;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static final String BTHOOTH = Manifest.permission.BLUETOOTH;
    public static final int REQUEST_CODE = 100;
    private Context context;
private Activity activity;
private TextView tvVersionAndroid;
private TextView tvNivelBateria;
IntentFilter batteryFiller;
private BluetoothAdapter btnAdapter;
private boolean bandera = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicio();
        context = getApplicationContext();
        activity = MainActivity.this;
        batteryFiller = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(bateriaCarga,batteryFiller);

    }

    //inicio
    private void inicio(){
        tvVersionAndroid = findViewById(R.id.tvVersionAndroind);
        tvNivelBateria = findViewById(R.id.tvNivelBateria);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVeresionAndroid();
    }

    // Version Android
    private void setVeresionAndroid(){
        String versionSO = Build.VERSION.RELEASE;
        int versionSDK = Build.VERSION.SDK_INT ;
        tvVersionAndroid.setText("Sistema Operativo: "+versionSO+". SDK: "+versionSDK);
    }

    //bateria
    BroadcastReceiver bateriaCarga = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nivelActual = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1  );

            ///para cambiar la escala del nivel de bateria.
            int escala = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1  );

            tvNivelBateria.setText("Nivel de batería: "+nivelActual+"% .");
        }
    };

    private boolean statusPermiso(){
        int response =ContextCompat.checkSelfPermission(this.context, BTHOOTH);
        if(response== PackageManager.PERMISSION_GRANTED) return true;
        else  return false;
    }

    //en caso de que el permiso no esté dado, se debe solicitar permiso

    private void solicitarPermisoBTOOTH(){
        Boolean response = ActivityCompat.shouldShowRequestPermissionRationale(this.activity, BTHOOTH);
        if (!response) ActivityCompat.requestPermissions(activity, new String[]
            {BTHOOTH}, REQUEST_CODE);
        }

    //Habilitar bluetooth

    public void habilitarBluetooth(View view){
        estatusAdapter();
        if (btnAdapter == null){
            Toast.makeText(this, "El dispositivo no tiene bluetooth",Toast.LENGTH_SHORT).show();
        } else{
            if(btnAdapter.isEnabled()){
                Toast.makeText(this, "El bluetooth ya está habilitado",Toast.LENGTH_SHORT).show();
            }else{
                if(!statusPermiso()){
                    solicitarPermisoBTOOTH();
                    Intent habilitar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                      startActivityForResult(habilitar, 110);
                } else{
                    Toast.makeText(this, "El bluetooth ya está habilitado",Toast.LENGTH_SHORT).show();

                }
                }
        }
    }


    private void estatusAdapter(){
        if (!bandera){
            btnAdapter = BluetoothAdapter.getDefaultAdapter();
            bandera = true;
        }
    }

}