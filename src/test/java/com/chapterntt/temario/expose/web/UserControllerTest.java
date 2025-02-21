package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.UserService;
import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.ThemeDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.proxy.dto.ResponseDto;
import io.reactivex.Maybe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

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
                .thenReturn(Maybe.just(userDto));

        var response = userController.findById(1).test();
        response.awaitTerminalEvent();
        response.assertValue(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK);
        response.assertValue(value -> value.getBody().getFirstName().equals("John"));
    }

    @Test
    void getAccountInformation() {
        when(userService.getAccountInformation(anyInt()))
                .thenReturn(Maybe.just(accountDto));

        var response = userController.getAccount(1).test();
        response.awaitTerminalEvent();
        response.assertValue(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK);
        response.assertValue(value -> value.getBody().getThemeDto().getColor().equals("Magenta"));
    }
}
