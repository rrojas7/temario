package com.chapterntt.temario.business;

import com.chapterntt.temario.model.dto.AccountDto;
import com.chapterntt.temario.model.dto.UserDto;
import io.reactivex.Maybe;

public interface UserService {

    Maybe<UserDto> getUser(int id);

    Maybe<AccountDto> getAccountInformation(int id);
}
