package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.UserService;
import com.chapterntt.temario.business.UserServiceWebFlux;
import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.dto.ThemeDto;
import com.chapterntt.temario.model.dto.UserDto;
import io.reactivex.Maybe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(UserControllerWebFlux.class)
public class UserControllerWebFluxTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    UserServiceWebFlux userService;

    private UserDto userDto;
    private ThemeDto themeDto;
    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId("1");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        themeDto = new ThemeDto();
        themeDto.setId("1");
        themeDto.setName("Material");
        themeDto.setColor("Magenta");

        accountDto = new AccountDto(userDto, themeDto);

    }

    @Test
    void getUser() {
        when(userService.getUser(anyInt()))
                .thenReturn(Mono.just(userDto));

        webTestClient.get()
                .uri("/users/webflux/findById/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(response -> assertEquals("John", response.getFirstName()));
    }

    @Test
    void getAccountInformation() {
        when(userService.getAccountInformation(anyInt()))
                .thenReturn(Mono.just(accountDto));

        webTestClient.get()
                .uri("/users/webflux/getAccount/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountDto.class)
                .value(response -> assertEquals("John", response.getUserDto().getFirstName()));

    }
}
