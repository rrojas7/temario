package com.chapterntt.temario.business;

import com.chapterntt.temario.model.dto.EmployeeDto;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface EmployeeServiceWebFlux {
    Mono<EmployeeDto> getById(int id);

    Flux<EmployeeDto> listAll();

    Flux<EmployeeDto> search(LocalDate from, LocalDate to);

    Mono<EmployeeDto> getByIdOrDefault(int id);

    Flux<EmployeeDto> listWithFilter(String typeFilter);

    Flux<EmployeeDto> listAllWithMap(String mapType);

    Mono<EmployeeDto> getEmployeeWithUser(int id);
}
