package com.example.testingcertificatepinning;

import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CheckBox chkNSC, chkOkHttp, chkHttpUrlConnection, chkRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        chkNSC = findViewById(R.id.chkNSC);
        chkNSC.setChecked(false);

        chkHttpUrlConnection = findViewById(R.id.chkHttpUrlConnection);
        chkHttpUrlConnection.setChecked(false);

        chkOkHttp = findViewById(R.id.chkOkHttp);
        chkOkHttp.setChecked(false);

        chkRetrofit = findViewById(R.id.chkRetrofit);
        chkRetrofit.setChecked(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onClickBtn(View v) {
        chkNSC.setChecked(testNSC());
        chkOkHttp.setChecked(testOkHttp());
        chkHttpUrlConnection.setChecked(testHttpUrlConnection());
        testRetrofit();
    }

    public boolean testNSC(){

        InputStream in = null;
        boolean exito = false;
        try {

            URL url = new URL("https://www.google.com/?id=12");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            in = urlConnection.getInputStream();
            BufferedReader inBR = new BufferedReader(new InputStreamReader(
                    in));
            String inputLine;
            while ((inputLine = inBR.readLine()) != null)
                System.out.println(inputLine);
            in.close();
            exito = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return exito;
    }


    public boolean testOkHttp() {
        boolean exito = false;
        String hostname = "example.org";
        CertificatePinner certificatePinner = new CertificatePinner.Builder().add(hostname, "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=").build();
        OkHttpClient client = new OkHttpClient.Builder().certificatePinner(certificatePinner).build();

        Request request = new Request.Builder().url("https://" + hostname).build();
        try {
            client.newCall(request).execute();
            exito = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exito;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean testHttpUrlConnection() {
        HttpUrlConnectPinner httpUrlConnectPinner = new HttpUrlConnectPinner();
        return httpUrlConnectPinner.testPinning();
    }

    public void testRetrofit() {
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<List<RetroUsers>> call = service.getAllUsers();
        call.enqueue(new Callback<List<RetroUsers>>() {

            @Override
            public void onResponse(Call<List<RetroUsers>> call, Response<List<RetroUsers>> response) {
                chkRetrofit.setChecked(true);
            }

            @Override
            public void onFailure(Call<List<RetroUsers>> call, Throwable throwable) {
                chkRetrofit.setChecked(false);
            }
        });
    }
}
