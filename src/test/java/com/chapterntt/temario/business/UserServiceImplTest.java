package com.chapterntt.temario.business;

import com.chapterntt.temario.business.impl.UserServiceImpl;
import com.chapterntt.temario.model.dto.ThemeDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.proxy.dao.ApiDao;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import io.reactivex.Maybe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    ApiDao apiDao;

    @InjectMocks
    UserServiceImpl userService;

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
                .thenReturn(Maybe.just(responseUserDto));

        var result = userService.getUser(1).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getFirstName().equals("John"));
    }

    @Test
    void getAccountInformation() {
        when(apiDao.getUser(anyInt()))
                .thenReturn(Maybe.just(responseUserDto));

        when(apiDao.getTheme(anyInt()))
                .thenReturn(Maybe.just(responseThemeDto));

        var result = userService.getAccountInformation(1).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getThemeDto().getColor().equals("Magenta"));
    }
}
