package com.chapterntt.temario.business.impl;

import com.chapterntt.temario.business.UserServiceWebFlux;
import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.model.exception.ApiException;
import com.chapterntt.temario.proxy.dao.ApiWebFluxDao;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplWebFlux implements UserServiceWebFlux {

    private final ApiWebFluxDao apiWebFluxDaoClient;

    @Override
    public Mono<UserDto> getUser(int id) {
        return apiWebFluxDaoClient.getUser(id)
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(err -> Mono.error(new ApiException("Error calling api or user not exist")))
                .map(ResponseDto::getData);
    }

    @Override
    public Mono<AccountDto> getAccountInformation(int id) {
        return Mono.zip(apiWebFluxDaoClient.getUser(id), apiWebFluxDaoClient.getTheme(id),
                (userDtoResponseDto, themeDtoResponseDto) ->
                        AccountDto.builder().userDto(userDtoResponseDto.getData()).themeDto(themeDtoResponseDto.getData()).build())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(throwable -> log.error("Error getting account", throwable))
                .doOnSuccess(accountDto -> log.info("Account found for user {} and theme color {}", accountDto.getUserDto().getFirstName(), accountDto.getThemeDto().getColor()));
    }

}
