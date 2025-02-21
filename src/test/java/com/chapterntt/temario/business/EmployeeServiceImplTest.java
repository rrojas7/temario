package com.chapterntt.temario.business;


import com.chapterntt.temario.business.impl.EmployeeServiceImpl;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.dto.UserDto;
import com.chapterntt.temario.model.entity.EmployeeEntity;
import com.chapterntt.temario.model.exception.ResourceNotFoundException;
import com.chapterntt.temario.model.mapper.EmployeeMapper;
import com.chapterntt.temario.repository.EmployeeRepository;
import io.reactivex.Maybe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Mock
    private UserService userService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeEntity employeeEntity;
    private EmployeeDto employeeDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1);
        employeeEntity.setName("John Doe");

        employeeDto = new EmployeeDto();
        employeeDto.setId(1);
        employeeDto.setName("John Doe");

        userDto = new UserDto();
        userDto.setId("1");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
    }

    @Test
    void getById_Success() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        var result = employeeService.getById(1).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getId() == 1);
    }

    @Test
    void getById_NotFound() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> {
            var result = employeeService.getById(1).test();
            result.awaitTerminalEvent();
        });
    }

    @Test
    void listAll() {
        List<EmployeeEntity> employees = Collections.singletonList(employeeEntity);
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        var result = employeeService.listAll().test();
        result.awaitTerminalEvent();
        result.assertValueCount(1);
    }

    @Test
    void search() {
        List<EmployeeEntity> employees = Collections.singletonList(employeeEntity);
        when(employeeRepository.search(any(LocalDate.class), any(LocalDate.class))).thenReturn(employees);
        when(employeeMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        var result = employeeService.search(LocalDate.now(), LocalDate.now()).test();
        result.awaitTerminalEvent();
        result.assertValueCount(1);
    }

    @Test
    void getByIdOrDefault_Existing() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        var result = employeeService.getByIdOrDefault(1).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getId() == 1);
    }

    @Test
    void getByIdOrDefault_Default() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        var result = employeeService.getByIdOrDefault(1).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getName().equals("Default object"));
    }

    @ParameterizedTest
    @CsvSource({"filter", "take", "takeLast", "other"})
    void listWithFilter(String typeFilter) {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employeeEntity));
        when(employeeMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        var result = employeeService.listWithFilter(typeFilter).test();
        result.awaitTerminalEvent();
        result.assertValueCount(1);
    }

    @ParameterizedTest
    @CsvSource({"skip", "skipLast"})
    void listWithFilterSkip(String typeFilter) {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employeeEntity, employeeEntity, employeeEntity));
        when(employeeMapper.toDto(employeeEntity)).thenReturn(employeeDto);

        var result = employeeService.listWithFilter(typeFilter).test();
        result.awaitTerminalEvent();
        result.assertValueCount(2);
    }

    @ParameterizedTest
    @CsvSource({"concatMap", "switchMap"})
    void listAllWithMap(String mapType) {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employeeEntity));
        when(userService.getUser(anyInt())).thenReturn(Maybe.just(userDto));

        var result = employeeService.listAllWithMap(mapType).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getUserDto().getFirstName().equals("John"));
    }

    @Test
    void getEmployeeWithUser() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employeeEntity));
        when(userService.getUser(anyInt())).thenReturn(Maybe.just(userDto));

        var result = employeeService.getEmployeeWithUser(1).test();
        result.awaitTerminalEvent();
        result.assertValue(value -> value.getUserDto().getLastName().equals("Doe"));
    }
}
