package com.example.testingcertificatepinning;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add("jsonplaceholder.typicode.com", "sha256/47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=")
                    .build();
            OkHttpClient client1 = httpBuilder
                    .certificatePinner(certificatePinner)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client1)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
