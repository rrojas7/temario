package com.chapterntt.temario.model.mapper;

import com.chapterntt.temario.model.entity.EmployeeEntity;
import com.chapterntt.temario.model.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    EmployeeDto toDto(EmployeeEntity employeeEntity);
}
