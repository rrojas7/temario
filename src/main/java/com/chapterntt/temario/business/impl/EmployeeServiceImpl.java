package com.chapterntt.temario.business.impl;

import com.chapterntt.temario.business.EmployeeService;
import com.chapterntt.temario.business.UserService;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.entity.EmployeeEntity;
import com.chapterntt.temario.model.exception.ResourceNotFoundException;
import com.chapterntt.temario.model.mapper.EmployeeMapper;
import com.chapterntt.temario.repository.EmployeeRepository;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    /**
     * Función que obtiene un empleado desde la base de datos
     * en caso no exista se lanza una excepción personalizada. Uso de método
     * flatMap para convertir la respuesta devolviendo un objeto de tipo Single
     * @param id (se tiene hasta el id 4 de prueba)
     * @return Empleado del id consultado
     */
    @Override
    public Single<EmployeeDto> getById(int id) {
        return Single.just(employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist")))
                .flatMap(employeeEntity -> Single.just(employeeMapper.toDto(employeeEntity)))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Función que consulta todos los empleados de la base de datos.
     * Uso del método de creación de Flowable a partir de una lista
     * @return Todos los empleados existentes
     */
    @Override
    public Flowable<EmployeeDto> listAll() {
        return Flowable.fromIterable(employeeRepository.findAll())
                .map(employeeMapper::toDto);
    }

    /**
     * Función que permite consultar los empleados registrados desde un rango de fechas.
     * Uso del método de creación de Flowable a partir de una lista
     * @param from Fecha desde
     * @param to Fecha hasta
     * @return Lista de empleados que cumplen la condición
     */
    @Override
    public Flowable<EmployeeDto> search(LocalDate from, LocalDate to) {
        return Flowable.fromIterable(employeeRepository.search(from, to))
                .map(employeeMapper::toDto);
    }

    /**
     * Función que permite consultar un empleado pero en caso no exista
     * devolver un objeto por default utilizando el método switchIfEmpty
     * @param id Id del empleado a consultar (se tiene hasta el id 4 de prueba)
     * @return EmployeeDto
     */
    @Override
    public Maybe<EmployeeDto> getByIdOrDefault(int id) {
        return getEmployeeEntity(id)
                .switchIfEmpty(Maybe.just(defaultEntity()))
                .flatMap(employeeEntity -> Maybe.just(employeeMapper.toDto(employeeEntity)))
                .subscribeOn(Schedulers.io());
    }

    private EmployeeEntity defaultEntity() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(0);
        employee.setName("Default object");
        return employee;
    }

    /**
     * Función que devuelve la lista de empleados de acuerdo al filtro enviado
     * en donde cada uno representa un tipo de operación de filtrado que se puede
     * aplicar sobre un Flowable
     * @param typeFilter Tipo de filtro
     * @return Lista filtrada
     */
    @Override
    public Flowable<EmployeeDto> listWithFilter(String typeFilter) {
        Flowable<EmployeeDto> employees = Flowable.fromIterable(employeeRepository.findAll())
                .map(employeeMapper::toDto);
        switch (typeFilter) {
            case "filter":
                return employees.filter(employeeDto -> employeeDto.getAge()<40);
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

    /**
     * Función que permite utilizar los operadores concatMap y switchMap
     * @param mapType Tipo de operador
     * @return Lista de empleados
     */
    @Override
    public Flowable<EmployeeDto> listAllWithMap(String mapType) {
        if (mapType.equals("concatMap")) {
            return Flowable.fromIterable(employeeRepository.findAll())
                    .concatMap(employeeEntity -> userService.getUser(employeeEntity.getId()).toFlowable()
                            .map(userDto -> EmployeeDto.builder().id(employeeEntity.getId())
                                    .name(employeeEntity.getName()).age(employeeEntity.getAge())
                                    .userDto(userDto).build()));
        } else {
            return Flowable.fromIterable(employeeRepository.findAll())
                    .switchMap(employeeEntity -> userService.getUser(employeeEntity.getId()).toFlowable()
                            .map(userDto -> EmployeeDto.builder().id(employeeEntity.getId())
                                    .name(employeeEntity.getName()).age(employeeEntity.getAge())
                                    .userDto(userDto).build()));
        }

    }

    @Override
    public Maybe<EmployeeDto> getEmployeeWithUser(int id) {
        return getEmployeeEntity(id)
                .zipWith(userService.getUser(id), (employeeEntity , userDto) -> EmployeeDto.builder().id(employeeEntity.getId())
                        .name(employeeEntity.getName()).age(employeeEntity.getAge())
                        .userDto(userDto).build());
    }

    /**
     * Función que permite crear un objeto Maybe
     * a partir de la evaluación del valor Optional que devuelve el repository
     * @param id Id a consultar (se tiene hasta el id 4 de prueba)
     * @return Maybe del objeto EmployeeEntity
     */
    private Maybe<EmployeeEntity> getEmployeeEntity(int id) {
        return Maybe.create(emitter -> {
            Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
            if(employeeEntity.isPresent()) {
                emitter.onSuccess(employeeEntity.get());
            }else {
                emitter.onComplete();
            }
        });
    }
}
