package com.chapterntt.temario.proxy.dao;

import com.chapterntt.temario.model.dto.ThemeDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import reactor.core.publisher.Mono;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiWebFluxDao {

    @GET("users/{id}")
    Mono<ResponseDto<UserDto>> getUser(@Path("id") int id);

    @GET("unknown/{id}")
    Mono<ResponseDto<ThemeDto>> getTheme(@Path("id") int id);

}
