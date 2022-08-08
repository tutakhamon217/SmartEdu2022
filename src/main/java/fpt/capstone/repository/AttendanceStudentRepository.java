package fpt.capstone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fpt.capstone.entities.AttendanceStudent;

public interface AttendanceStudentRepository extends JpaRepository<AttendanceStudent, Integer>{
    @Query("SELECT ast.code FROM AttendanceStudent ast WHERE ast.schoolYear = :year AND ast.semester = :semester AND ast.month = :month")
    Optional<String> getCode(@Param("year") String year, @Param("semester") String semester, @Param("month") String month);
    @Override
    <S extends AttendanceStudent> List<S> saveAll(Iterable<S> entities);
}
