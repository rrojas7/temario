package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.UserServiceWebFlux;
import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users/webflux")
@RequiredArgsConstructor
@Slf4j
public class UserControllerWebFlux {

    private final UserServiceWebFlux userServiceWebFlux;

    @GetMapping("/findById/{id}")
    Mono<ResponseEntity<UserDto>> findById(@PathVariable(name = "id") int id) {
        return userServiceWebFlux.getUser(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("User get {}", response.getBody().getFirstName()))
                .doOnError(ex -> log.error("Error in findById operation {}", ex.getMessage(), ex));
    }

    @GetMapping("/getAccount/{id}")
    Mono<ResponseEntity<AccountDto>> getAccount(@PathVariable(name = "id") int id) {
        return userServiceWebFlux.getAccountInformation(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(accountDto -> log.info("Account information found"))
                .doOnError(ex -> log.error("Error in getAccount operation {}", ex.getMessage(), ex));
    }




}
