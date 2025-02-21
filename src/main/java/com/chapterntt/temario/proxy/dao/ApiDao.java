package com.chapterntt.temario.proxy.dao;


import com.chapterntt.temario.model.dto.ThemeDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiDao {

    @GET("users/{id}")
    Maybe<ResponseDto<UserDto>> getUser(@Path("id") int id);

    @GET("unknown/{id}")
    Maybe<ResponseDto<ThemeDto>> getTheme(@Path("id") int id);
}
