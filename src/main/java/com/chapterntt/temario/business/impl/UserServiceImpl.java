package com.chapterntt.temario.business.impl;

import com.chapterntt.temario.business.UserService;
import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.model.exception.ApiException;
import com.chapterntt.temario.proxy.dao.ApiDao;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ApiDao apiDaoClient;

    /**
     * Método para consultar un usuario, uso de Single
     * y la función onErrorResumeNext para controlar un error
     * y personalizarlo con una excepción
     * @param id (en caso de querer forzar error usar 23)
     * @return Usuario del id consultado
     */
    @Override
    public Maybe<UserDto> getUser(int id) {
        return apiDaoClient.getUser(id)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(Maybe.error(new ApiException("Error calling api or user not exist")))
                .map(ResponseDto::getData);
    }

    /**
     * Método para consultar un usuario y su tema
     * utilizando el método zip para unir la invocación a cada api que devuelve cada información
     * en este caso no se utiliza el método onErrorResumeNext y por ello el error no es controlado.
     * Se utiliza los métodos doOnError y doOnSuccess
     * @param id
     * @return Cuenta del id consultado
     */
    @Override
    public Maybe<AccountDto> getAccountInformation(int id) {
        return Maybe.zip(apiDaoClient.getUser(id), apiDaoClient.getTheme(id),
                (userDtoResponseDto, themeDtoResponseDto) ->
                        AccountDto.builder().userDto(userDtoResponseDto.getData()).themeDto(themeDtoResponseDto.getData()).build())
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> log.error("Error getting account", throwable))
                .doOnSuccess(accountDto -> log.info("Account found for user {} and theme color {}", accountDto.getUserDto().getFirstName(), accountDto.getThemeDto().getColor()));
    }

}
