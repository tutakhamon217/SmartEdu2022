package fpt.capstone.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fpt.capstone.entities.AttendanceStudentDetail;

public interface AttendanceStudentDetailRepository extends JpaRepository<AttendanceStudentDetail, Integer>{
    @Query("SELECT asd FROM AttendanceStudent ast JOIN AttendanceStudentDetail asd ON ast.code = asd.parentCode JOIN StudentHistory sh ON asd.studentCode = sh.studentCode WHERE sh.year = :year AND sh.currentClassCode = :classCode AND ast.month = :month")
    List<AttendanceStudentDetail> getAttendance(@Param("month") String month, @Param("year") String year, @Param("classCode") String classCode);
    
    @Query("SELECT ast FROM AttendanceStudentDetail ast WHERE ast.studentCode = :studentCode AND ast.date = :date")
    Optional<AttendanceStudentDetail> getAttendanceByDate(@Param("studentCode") String studentCode, @Param("date") LocalDate date);

    @Modifying
    @Query("UPDATE AttendanceStudentDetail ast SET ast.checkDate = :checkDate WHERE ast.id = :id")
    void updateAttendace(@Param("id") Integer id, @Param("checkDate") String checkDate);
    
    @Override
    <S extends AttendanceStudentDetail> List<S> saveAll(Iterable<S> entities);
}
