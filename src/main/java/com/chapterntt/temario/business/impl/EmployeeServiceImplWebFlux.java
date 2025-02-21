package com.chapterntt.temario.business.impl;

import com.chapterntt.temario.business.EmployeeServiceWebFlux;
import com.chapterntt.temario.business.UserServiceWebFlux;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.entity.EmployeeEntity;
import com.chapterntt.temario.model.exception.ResourceNotFoundException;
import com.chapterntt.temario.model.mapper.EmployeeMapper;
import com.chapterntt.temario.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImplWebFlux implements EmployeeServiceWebFlux {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserServiceWebFlux userServiceWebFlux;

    @Override
    public Mono<EmployeeDto> getById(int id) {
        return Mono.just(employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist")))
                .flatMap(employeeEntity -> Mono.just(employeeMapper.toDto(employeeEntity)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<EmployeeDto> listAll() {
        return Flux.fromIterable(employeeRepository.findAll())
                .map(employeeMapper::toDto);
    }

    @Override
    public Flux<EmployeeDto> search(LocalDate from, LocalDate to) {
        return Flux.fromIterable(employeeRepository.search(from, to))
                .map(employeeMapper::toDto);
    }

    @Override
    public Mono<EmployeeDto> getByIdOrDefault(int id) {
        return Mono.justOrEmpty(employeeRepository.findById(id))
                .switchIfEmpty(Mono.just(EmployeeEntity.builder().id(0).name("Default object").build()))
                .flatMap(employeeEntity -> Mono.just(employeeMapper.toDto(employeeEntity)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<EmployeeDto> listWithFilter(String typeFilter) {
        Flux<EmployeeDto> employees = Flux.fromIterable(employeeRepository.findAll())
                .map(employeeMapper::toDto);

        switch (typeFilter) {
            case "filter":
                return employees.filter(employeeDto -> employeeDto.getAge() < 40);
            case "take":
                return employees.take(2);
            case "takeLast":
                return employees.takeLast(2);
            case "skip":
                return employees.skip(1);
            case "skipLast":
                return employees.skipLast(1);
            default:
                return employees;
        }
    }

    @Override
    public Flux<EmployeeDto> listAllWithMap(String mapType) {
        if (mapType.equals("concatMap")) {
            return Flux.fromIterable(employeeRepository.findAll())
                    .concatMap(employeeEntity -> userServiceWebFlux.getUser(employeeEntity.getId())
                            .map(userDto -> EmployeeDto.builder().id(employeeEntity.getId())
                                    .name(employeeEntity.getName()).age(employeeEntity.getAge())
                                    .userDto(userDto).build()));
        } else {
            return Flux.fromIterable(employeeRepository.findAll())
                    .switchMap(employeeEntity -> userServiceWebFlux.getUser(employeeEntity.getId())
                            .map(userDto -> EmployeeDto.builder().id(employeeEntity.getId())
                                    .name(employeeEntity.getName()).age(employeeEntity.getAge())
                                    .userDto(userDto).build()));
        }
    }

    @Override
    public Mono<EmployeeDto> getEmployeeWithUser(int id) {
        return Mono.justOrEmpty(employeeRepository.findById(id))
                .zipWith(userServiceWebFlux.getUser(id),(employeeEntity , userDto) -> EmployeeDto.builder().id(employeeEntity.getId())
                        .name(employeeEntity.getName()).age(employeeEntity.getAge())
                        .userDto(userDto).build());
    }

}
