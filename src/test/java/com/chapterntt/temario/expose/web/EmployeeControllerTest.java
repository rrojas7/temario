package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.EmployeeService;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.web.FilterRequest;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto();
        employeeDto.setId(1);
        employeeDto.setName("John Doe");
    }

    @Test
    void findById() {
        when(employeeService.getById(anyInt())).thenReturn(Single.just(employeeDto));

        var response = employeeController.findById(1).test();
        response.awaitTerminalEvent();
        response.assertValue(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK);
        response.assertValue(responseEntity -> responseEntity.getBody().getId() == 1);
    }

    @Test
    void listAll() {
        when(employeeService.listAll())
                .thenReturn(Flowable.fromIterable(Collections.singletonList(employeeDto)));

        var response = employeeController.listAll().test();
        response.awaitTerminalEvent();
        response.assertValueCount(1);
    }

    @Test
    void search() {
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDateFrom("01/01/2023");
        filterRequest.setDateTo("31/12/2023");

        when(employeeService.search(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Flowable.fromIterable(Collections.singletonList(employeeDto)));

        var response = employeeController.search(filterRequest).test();
        response.awaitTerminalEvent();
        response.assertValueCount(1);
    }

    @Test
    void findEmployeeOrDefault() {
        when(employeeService.getByIdOrDefault(anyInt())).thenReturn(Maybe.just(employeeDto));

        var response = employeeController.findEmployeeOrDefault(1).test();
        response.awaitTerminalEvent();
        response.assertValue(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK);
        response.assertValue(responseEntity -> responseEntity.getBody().getId() == 1);
    }

    @Test
    void listFilter() {
        when(employeeService.listWithFilter(anyString()))
                .thenReturn(Flowable.fromIterable(Collections.singletonList(employeeDto)));

        var response = employeeController.listFilter("filter").test();
        response.awaitTerminalEvent();
        response.assertValueCount(1);
    }

    @Test
    void listAllWithMap() {
        when(employeeService.listAllWithMap(anyString()))
                .thenReturn(Flowable.fromIterable(Collections.singletonList(employeeDto)));

        var response = employeeController.listAllWithMap("concatMap").test();
        response.awaitTerminalEvent();
        response.assertValue(value -> value.getName().equals("John Doe"));
    }

    @Test
    void findEmployeeAndUserWithId() {
        when(employeeService.getEmployeeWithUser(anyInt())).thenReturn(Maybe.just(employeeDto));

        var response = employeeController.findEmployeeAndUserWithId(1).test();
        response.awaitTerminalEvent();
        response.assertValue(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK);
        response.assertValue(responseEntity -> responseEntity.getBody().getId() == 1);
    }
}