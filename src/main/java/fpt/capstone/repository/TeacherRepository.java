package fpt.capstone.repository;

import fpt.capstone.entities.SubjectClass;
import fpt.capstone.entities.Teacher;
import fpt.capstone.vo.DropDownVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    @Query("SELECT new fpt.capstone.vo.DropDownVo(teacher.id, teacher.fullName, teacher.code) FROM Teacher teacher WHERE teacher.deptID = :depID and  (teacher.status = 0 or teacher.status = 3)")
    List<DropDownVo> getDropDownValues(@Param("depID") Integer depID);

    @Query("SELECT COUNT(teacher) > 0 FROM Teacher teacher WHERE teacher.deptID = :depID AND teacher.id = :id")
    Boolean isExistByDeptID(@Param("depID") Integer depID, @Param("id") Integer id);

    @Query("SELECT COUNT(teacher) > 0 FROM Teacher teacher WHERE teacher.id = :id")
    Boolean hasTeacherById(@Param("id") Integer id);

    @Query("SELECT COUNT(teacher) > 0 FROM Teacher teacher WHERE teacher.deptID = :depID")
    Boolean hasTeacherInDeptID(@Param("depID") Integer depID);

    @Query(nativeQuery = true, value = "select t.id,t.code,t.full_name from teachers t join user u on t.code=u.login join user_authority ua on u.id=ua.user_id where ua.authority_code='ROLE_GVBM' and t.status=0")
    List<Map<String, Object>> getAllTeacher();

    @Query(nativeQuery = true, value = "select id,code,full_name from teachers where status =0 or status = 3")
    List<Map<String, Object>> getAllTeacherDangLamOrTamNghi();


    @Query(nativeQuery = true, value = "select  t.id as teacherId,t.code,t.full_name as teacherName , d.name as deptName,t.sex,  group_concat(a.name ,' ')\n" +
            "AS Authorities, group_concat(a.code ,' ')AS AuthoritiesCode,t.contract_type as contractType from teachers t  join departments d on t.dept_id = d.id \n" +
            " join user u on t.code=u.login join user_authority ua on u.id=ua.user_id  join authority a on ua.authority_code=a.code where ((?1 is null or upper(t.code) Like UPPER('%' ?1 '%')) or (?1 is null or upper(t.full_name) Like UPPER('%' ?1 '%')))\n" +
            "and (?3 is null or d.id=?3) GROUP BY t.id having \n" +
            " (?2 is null or upper(AuthoritiesCode) Like UPPER('%' ?2 '%')) order by t.full_name")
    List<Map<String, Object>> getAllTeacherByCodeOrFullNameAndAuthority(String codeName, String authorityCode, Integer deptId);

    @Query(nativeQuery = true, value = "select t.avatar,t.id as teacherId,t.full_name,t.code,SUBSTRING(t.start_date,1,10) as start_date,t.phone, t.status, t.contract_type,t.email,d3.id as deptId3,d3.name as nameDept3,d2.id as deptId2,d2.name as nameDept2,d1.id as deptId1,d1.name as nameDept1, SUBSTRING(t.birth_day,1,10) as birth_day,t.religion,t.nation,t.home_town,t.permanent_address,\n" +
            "t.temporary_address,t.social_insurance_number,t.identity_card,t.issued_address,SUBSTRING( t.issued_date,1,10) as issued_date,t.marriage_status,t.sex,group_concat(a.code ,'')\n" +
            "AS Authorities, group_concat(a.name ,'') as name_authorities from teachers t join departments d1 on t.dept_id=d1.id join departments d2 on d1.parent_id=d2.id join departments d3 on d2.parent_id=d3.id \n" +
            "join user u on t.code =u.login join user_authority ua on u.id=ua.user_id join authority a on ua.authority_code=a.code where t.id=?1 GROUP BY t.id order by t.full_name asc ")
    Map<String, Object> getTeacherDetailById(Integer teacherId);

    List<Teacher> findByCode(String code);

    @Override
    Teacher getById(Integer id);

    @Query("SELECT t FROM Teacher t WHERE t.code = :code")
    Optional<Teacher> findByCodeOptional(@Param("code") String code);

    @Query(nativeQuery = true, value = "select t.id,t.code,t.full_name from subjects s join subject_dept sd on s.id=sd.subject_id join departments d on sd.dept_id=d.id join teachers t on d.id=t.dept_id where t.status=0 and s.id=?1")
    List<Map<String, Object>> getAllTeacherForTeachingAssignment(Integer subjectId);

    @Query("SELECT COUNT(teacher) > 0 FROM Subjects s join SubjectDept sd on s.id=sd.subjectId join Departments d on sd.deptId=d.id join Teacher teacher on d.id=teacher.deptID where teacher.code= :teacherCode and s.code=:subjectCode and teacher.status=0")
    Boolean isExistBySubjectCodeAndTeacherCode(@Param("teacherCode") String teacherCode, @Param("subjectCode") String subjectCode);

}
