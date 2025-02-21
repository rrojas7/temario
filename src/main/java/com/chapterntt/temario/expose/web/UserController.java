package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.UserService;
import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.UserDto;
import io.reactivex.Maybe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/findById/{id}")
    Maybe<ResponseEntity<UserDto>> findById(@PathVariable(name = "id") int id) {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("User get {}", response.getBody().getFirstName()))
                .doOnError(ex -> log.error("Error in findById operation {}", ex.getMessage(), ex));
    }

    @GetMapping("/getAccount/{id}")
    Maybe<ResponseEntity<AccountDto>> getAccount(@PathVariable(name = "id") int id) {
        return userService.getAccountInformation(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(accountDto -> log.info("Account information found"))
                .doOnError(ex -> log.error("Error in getAccount operation {}", ex.getMessage(), ex));
    }




}
