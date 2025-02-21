package com.chapterntt.temario.business;

import com.chapterntt.temario.business.impl.UserServiceImpl;
import com.chapterntt.temario.business.impl.UserServiceImplWebFlux;
import com.chapterntt.temario.model.dto.ThemeDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.proxy.dao.ApiDao;
import com.chapterntt.temario.proxy.dao.ApiWebFluxDao;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import io.reactivex.Maybe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceWebFluxImplTest {

    @Mock
    ApiWebFluxDao apiDao;

    @InjectMocks
    UserServiceImplWebFlux userService;

    private ResponseDto<UserDto> responseUserDto;
    private ResponseDto<ThemeDto> responseThemeDto;
    private UserDto userDto;
    private ThemeDto themeDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId("1");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        responseUserDto = new ResponseDto<>();
        responseUserDto.setData(userDto);

        themeDto = new ThemeDto();
        themeDto.setId("1");
        themeDto.setName("Material");
        themeDto.setColor("Magenta");

        responseThemeDto = new ResponseDto<>();
        responseThemeDto.setData(themeDto);
    }

    @Test
    void getUser() {
        when(apiDao.getUser(anyInt()))
                .thenReturn(Mono.just(responseUserDto));

        var result = userService.getUser(1);
        StepVerifier.create(result)
                        .assertNext(value -> assertEquals("John", value.getFirstName()))
                                .verifyComplete();
    }

    @Test
    void getAccountInformation() {
        when(apiDao.getUser(anyInt()))
                .thenReturn(Mono.just(responseUserDto));

        when(apiDao.getTheme(anyInt()))
                .thenReturn(Mono.just(responseThemeDto));

        var result = userService.getAccountInformation(1);
        StepVerifier.create(result)
                .assertNext(value -> assertEquals("Magenta", value.getThemeDto().getColor()))
                .verifyComplete();
    }
}
