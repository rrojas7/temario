package com.chapterntt.temario.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountDto {

    @JsonProperty("user")
    private UserDto userDto;

    @JsonProperty("theme")
    private ThemeDto themeDto;
}
