package com.chapterntt.temario.business;

import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserServiceWebFlux {

    Mono<UserDto> getUser(int id);

    Mono<AccountDto> getAccountInformation(int id);
}
