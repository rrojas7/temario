package com.chapterntt.temario.expose.web;

import com.chapterntt.temario.business.EmployeeService;
import com.chapterntt.temario.business.EmployeeServiceWebFlux;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(EmployeeControllerWebFlux.class)
class EmployeeControllerWebFluxTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeServiceWebFlux employeeService;

    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto();
        employeeDto.setId(1);
        employeeDto.setName("John Doe");
    }

    @Test
    void findById() {
        when(employeeService.getById(anyInt())).thenReturn(Mono.just(employeeDto));

        webTestClient.get()
                .uri("/employees/webflux/findById/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .value(response -> assertEquals("John Doe", response.getName()));
    }

    @Test
    void listAll() {
        when(employeeService.listAll())
                .thenReturn(Flux.fromIterable(Collections.singletonList(employeeDto)));

        webTestClient.get()
                .uri("/employees/webflux/listAll")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .hasSize(1)
                .value(response -> assertEquals("John Doe", response.get(0).getName()));
    }

    @Test
    void search() {
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDateFrom("01/01/2023");
        filterRequest.setDateTo("31/12/2023");

        when(employeeService.search(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Flux.fromIterable(Collections.singletonList(employeeDto)));

        webTestClient.post()
                .uri("/employees/webflux/search")
                .bodyValue(filterRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .hasSize(1)
                .value(response -> assertEquals("John Doe", response.get(0).getName()));
    }

    @Test
    void findEmployeeOrDefault() {
        when(employeeService.getByIdOrDefault(anyInt())).thenReturn(Mono.just(employeeDto));

        webTestClient.get()
                .uri("/employees/webflux/findEmployeeOrDefault/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .value(response -> assertEquals("John Doe", response.getName()));
    }

    @Test
    void listFilter() {
        when(employeeService.listWithFilter(anyString()))
                .thenReturn(Flux.fromIterable(Collections.singletonList(employeeDto)));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/employees/webflux/listFilter")
                        .queryParam("filter", "filter")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .hasSize(1)
                .value(response -> assertEquals("John Doe", response.get(0).getName()));
    }

    @Test
    void listAllWithMap() {
        when(employeeService.listAllWithMap(anyString()))
                .thenReturn(Flux.fromIterable(Collections.singletonList(employeeDto)));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/employees/webflux/listAllWithMap")
                        .queryParam("mapType", "concatMap")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .hasSize(1)
                .value(response -> assertEquals("John Doe", response.get(0).getName()));
    }

    @Test
    void findEmployeeAndUserWithId() {
        when(employeeService.getEmployeeWithUser(anyInt())).thenReturn(Mono.just(employeeDto));

        webTestClient.get()
                .uri("/employees/webflux/findEmployeeAndUser/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .value(response -> assertEquals("John Doe", response.getName()));
    }
}