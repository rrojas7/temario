package com.chapterntt.temario.business;

import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.web.FilterRequest;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.time.LocalDate;
import java.util.logging.Filter;

public interface EmployeeService {
    Single<EmployeeDto> getById(int id);

    Flowable<EmployeeDto> listAll();

    Flowable<EmployeeDto> search(LocalDate from, LocalDate to);

    Maybe<EmployeeDto> getByIdOrDefault(int id);

    Flowable<EmployeeDto> listWithFilter(String typeFilter);

    Flowable<EmployeeDto> listAllWithMap(String mapType);

    Maybe<EmployeeDto> getEmployeeWithUser(int id);
}
