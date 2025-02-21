package com.chapterntt.temario.repository;

import com.chapterntt.temario.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    @Query(value = "select * from employee where register_date between :dateFrom and :dateTo", nativeQuery = true)
    List<EmployeeEntity> search(@Param(value = "dateFrom") LocalDate from, @Param(value = "dateTo") LocalDate to);
}
