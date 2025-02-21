package com.chapterntt.temario.config;

import com.chapterntt.temario.proxy.dao.ApiDao;
import com.chapterntt.temario.proxy.dao.ApiWebFluxDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    @Bean
    public Retrofit retrofitWebFlux() {
        return new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(ReactorCallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    @Bean
    public ApiDao apiDaoClient(Retrofit retrofit) {
        return retrofit.create(ApiDao.class);
    }

    @Bean
    public ApiWebFluxDao apiWebFluxDaoClient(Retrofit retrofitWebFlux) {
        return retrofitWebFlux.create(ApiWebFluxDao.class);
    }

}

