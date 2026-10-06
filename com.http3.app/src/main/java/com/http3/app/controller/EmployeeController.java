package com.http3.app.controller;

import com.http3.app.model.Employee;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class EmployeeController {

    @GetMapping(
            value = "/employees",
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<Employee> employees() {

        return Flux.interval(Duration.ofMillis(50)).take(100).map( rec->
                        new Employee(rec, "name "+rec, "Engineering - "+rec)
                );
    }
}