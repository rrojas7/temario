package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.EmployeeServiceWebFlux;
import com.chapterntt.temario.model.dto.EmployeeDto;
import com.chapterntt.temario.model.web.FilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/employees/webflux")
@RequiredArgsConstructor
@Slf4j
public class EmployeeControllerWebFlux {

    private final EmployeeServiceWebFlux employeeServiceWebFlux;

    @GetMapping("/findById/{id}")
    Mono<ResponseEntity<EmployeeDto>> findById(@PathVariable(name = "id") int id) {
        return employeeServiceWebFlux.getById(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Employee get {}", response.getBody().getName()))
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

    @GetMapping("/listAll")
    Flux<EmployeeDto> listAll() {
        return employeeServiceWebFlux.listAll()
                .doOnComplete(() -> log.info("Complete list all"));
    }

    @PostMapping("/search")
    Flux<EmployeeDto> search(@RequestBody FilterRequest filterRequest) {

        LocalDate dateFrom = LocalDate.parse(filterRequest.getDateFrom(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dateTo = LocalDate.parse(filterRequest.getDateTo(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return employeeServiceWebFlux.search(dateFrom, dateTo)
                .doOnComplete(() -> log.info("Complete search"));
    }

    @GetMapping("/findEmployeeOrDefault/{id}")
    Mono<ResponseEntity<EmployeeDto>> findEmployeeOrDefault(@PathVariable(name = "id") int id) {
        return employeeServiceWebFlux.getByIdOrDefault(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Employee get {}", response.getBody().getName()))
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

    @GetMapping("/listFilter")
    Flux<EmployeeDto> listFilter(@RequestParam(name = "filter") String filter) {
        return employeeServiceWebFlux.listWithFilter(filter)
                .doOnComplete(() -> log.info("Complete list filter"));
    }

    @GetMapping("/listAllWithMap")
    Flux<EmployeeDto> listAllWithMap(@RequestParam(name = "mapType") String mapType) {
        return employeeServiceWebFlux.listAllWithMap(mapType)
                .doOnComplete(() -> log.info("Complete list all"));
    }

    @GetMapping("/findEmployeeAndUser/{id}")
    Mono<ResponseEntity<EmployeeDto>> findEmployeeAndUserWithId(@PathVariable(name = "id") int id) {
        return employeeServiceWebFlux.getEmployeeWithUser(id)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Employee get {}", response.getBody().getName()))
                .doOnError(ex -> log.error(ex.getMessage(), ex));
    }

}
