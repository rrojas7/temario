package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.EmployeeService;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.web.FilterRequest;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/findById/{id}")
    Single<ResponseEntity<EmployeeDto>> findById(@PathVariable(name = "id") int id) {
        return employeeService.getById(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Employee get {}", response.getBody().getName()))
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

    @GetMapping("/listAll")
    Flowable<EmployeeDto> listAll() {
        return employeeService.listAll()
                .doOnComplete(() -> log.info("Complete list all"));
    }

    @PostMapping("/search")
    Flowable<EmployeeDto> search(@RequestBody FilterRequest filterRequest) {

        LocalDate dateFrom = LocalDate.parse(filterRequest.getDateFrom(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateTo = LocalDate.parse(filterRequest.getDateTo(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return employeeService.search(dateFrom, dateTo)
                .doOnComplete(() -> log.info("Complete search"));
    }

    @GetMapping("/findEmployeeOrDefault/{id}")
    Maybe<ResponseEntity<EmployeeDto>> findEmployeeOrDefault(@PathVariable(name = "id") int id) {
        return employeeService.getByIdOrDefault(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Employee get {}", response.getBody().getName()))
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

    @GetMapping("/listFilter")
    Flowable<EmployeeDto> listFilter(@RequestParam(name = "filter") String filter) {
        return employeeService.listWithFilter(filter)
                .doOnComplete(() -> log.info("Complete list filter"));
    }

    @GetMapping("/listAllWithMap")
    Flowable<EmployeeDto> listAllWithMap(@RequestParam(name = "mapType") String mapType) {
        return employeeService.listAllWithMap(mapType)
                .doOnComplete(() -> log.info("Complete list all"));
    }

    @GetMapping("/findEmployeeAndUser/{id}")
    Maybe<ResponseEntity<EmployeeDto>> findEmployeeAndUserWithId(@PathVariable(name = "id") int id) {
        return employeeService.getEmployeeWithUser(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Employee get {}", response.getBody().getName()))
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

}
