package fpt.capstone.service;

import fpt.capstone.entities.GradeLevel;
import fpt.capstone.entities.ScheduleGradeClass;
import fpt.capstone.entities.ScheduleSubjectDetails;
import fpt.capstone.entities.ScheduleTimetable;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Subjects;
import fpt.capstone.entities.Teacher;
import fpt.capstone.entities.WeekDetails;
import fpt.capstone.form_data.TimetableForm;
import fpt.capstone.helper.TimetableExcelHelper;
import fpt.capstone.payroll.SubjectNotFoundException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.utility.TimetableUtility;
import fpt.capstone.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Null;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private ScheduleTimetableRepository scheduleTimetableRepository;

    @Autowired
    private ScheduleGradeClassRepository scheduleGradeClassRepository;

    @Autowired
    private WeekDetailsRepository weekDetailsRepository;

    @Autowired
    private ScheduleSubjectDetailsRepository scheduleSubjectDetailsRepository;

    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private GradeLevelRepositpry gradeLevelRepositpry;

    @Autowired
    private ClassRoomRepository classroomRespository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Override
    public ServiceResult<List<TimeTableVoV2>> getScheduleTimetableByYearAndSemester(String year, String semester,
            String gradeLevel, String className, LocalDate applyDate) {

        Optional<String> code = scheduleGradeClassRepository.getLatestScheduleGradeClassCode(applyDate, gradeLevel,
                className, semester);
        if (!code.isPresent()) {
            return new ServiceResult<List<TimeTableVoV2>>(HttpStatus.OK, "Không tìm thấy thời khóa biểu",
                    TimetableUtility.generateEmptyTimetable());
        }
        List<TimeTableVoV2> values = new ArrayList<>();
        List<Map<String, Object>> results = teachingAssignmentRepository.getLatestTimetable(code.get()); 

        for(Map<String, Object> result : results)
        {
            java.sql.Date date = (java.sql.Date)(result.get("applyDate"));
            String weekDay = (String)(result.get("weekDay"));
            String lession = (String)(result.get("lesson"));
            String type = (String)(result.get("type"));
            Integer subjectId = (Integer)(result.get("subjectID"));
            String subjectName = (String)(result.get("subjectName"));
            String subjectCode = (String)(result.get("subjectCode"));
            Integer teacherID = (Integer)(result.get("teacherID"));
            String teacherName = (String)(result.get("teacherName"));
            String teacherCode = (String)(result.get("teacherCode"));
                        
            values.add(new TimeTableVoV2(date.toLocalDate(), weekDay, lession, type, subjectId, subjectName, subjectCode, teacherID, teacherName, teacherCode));
        }
        
        return new ServiceResult<>(HttpStatus.OK, "Schedule timetable", values);//teachingAssignmentRepository.getLatestTimetable(code.get()));
    }

    @Override
    public ServiceResult<List<TimeTableDropDown>> getTimeTableDropdown(String year, String semester, String className) {
        return new ServiceResult<>(HttpStatus.OK, "Timetable dropdown",
        subjectClassRepository.getTimeTableDropdown(year, semester, className));
    }

    @Override
    public ServiceResult<Boolean> updateTimeTable(TimetableForm form) {
        // Validate
        for (UploadTimetableVO element : form.getTimetable()) {
            if (element.getSubject() == null) {
                continue;
            }
            Subjects assignedSubject = subjectsRepository.findById(element.getSubject())
                    .orElseThrow(() -> new SubjectNotFoundException(element.getSubject().toString()));
            
            if(!subjectClassRepository.isValidForTimetable(form.getYear(), form.getSemester(), form.getClassName(), assignedSubject.getCode()))
            {
                throw new ValidationException("SubjectCode", "Không thể thêm môn học có code là " + assignedSubject.getCode() + " cho lớp " + form.getClassName());
            }
        }

        LocalDate applyDate = LocalDate.parse(form.getApplyDate());

        if (applyDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new ValidationException("dayOfWeek", "Ngày áp dụng phải là thứ Hai");
        }

        if (!scheduleTimetableRepository.isExisted(form.getYear(), form.getSemester())) {
            ScheduleTimetable st = new ScheduleTimetable(
                    1000,
                    "timetable_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                    form.getSemester(),
                    LocalDate.now(),
                    form.getYear(),
                    LocalDate.now(),
                    "",
                    null);

            scheduleTimetableRepository.save(st);
        }

        String schduleTimetableCode = scheduleTimetableRepository.getCodeByYearAndSemester(form.getYear(),
                form.getSemester());

        GradeLevel gl = null;
        try {
            gl = gradeLevelRepositpry.getById(Integer.parseInt(form.getGradeLevel()));
        } catch (Exception ex) {
            throw new ValidationException("gradeLevel", "Khối không tồn tại");
        }

        if (!scheduleGradeClassRepository.isExist(schduleTimetableCode, gl.getCode(), form.getClassName(), applyDate)) {
            ScheduleGradeClass sgc = new ScheduleGradeClass(1000,
                    "code_grade_class_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                    gl.getCode(),
                    schduleTimetableCode,
                    null,
                    applyDate,
                    form.getClassName(),
                    LocalDate.now(),
                    "");

            scheduleGradeClassRepository.save(sgc);
        }

        String scheduelGradeClassCode = scheduleGradeClassRepository.getCode(schduleTimetableCode, gl.getCode(),
                form.getClassName(), applyDate);

        // Generate null timetable
        for (int weekDay = 2; weekDay <= 7; ++weekDay) {
            String weekDayString = "t" + String.valueOf(weekDay);
            for (int period = 0; period <= 1; ++period) {
                String type = period == 0 ? "morning" : "afternoon";
                if (!weekDetailsRepository.isExist(scheduelGradeClassCode, String.valueOf(period), weekDayString)) {
                    WeekDetails wd = new WeekDetails(1000,
                            "week_detail_" + type
                                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                            String.valueOf(period),
                            weekDayString,
                            scheduelGradeClassCode);

                    weekDetailsRepository.save(wd);
                }

                String weekdetailCode = weekDetailsRepository.getCode(scheduelGradeClassCode, String.valueOf(period), weekDayString);

                for (int lession = 1; lession <= 5; ++lession) {
                    if (!scheduleSubjectDetailsRepository.isExist(weekdetailCode, String.valueOf(lession))) {
                        ScheduleSubjectDetails ssd = new ScheduleSubjectDetails(
                                1000,
                                "subject_details_"
                                        + type
                                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                                "",
                                weekdetailCode,
                                String.valueOf(lession));

                        scheduleSubjectDetailsRepository.save(ssd);
                    }
                }
            }
        }

        // Update timetable
        for (UploadTimetableVO timetable : form.getTimetable()) {
            
            String weekdetailCode = weekDetailsRepository.getCode(scheduelGradeClassCode,
            timetable.getType(), timetable.getWeekDay());
            String subjectCode = timetable.getSubject() == null ? "" : subjectsRepository.findById(timetable.getSubject()).orElse(null).getCode();

            ScheduleSubjectDetails entity = scheduleSubjectDetailsRepository.getEntity(weekdetailCode, timetable.getLesson()).orElse(null);

            entity.setSubjectCode(subjectCode);

            scheduleSubjectDetailsRepository.save(entity);
        }
        

        return new ServiceResult<>(HttpStatus.OK, "updated", true);
    }

    @Override
    public ByteArrayInputStream exportToExcel(String year, String semester, String gradeLevel, String className,
            LocalDate applyDate) {
        ByteArrayInputStream in = TimetableExcelHelper.export(year, semester, className,
                applyDate.format(DateTimeFormatter.ISO_DATE), this.getScheduleTimetableByYearAndSemester(year, semester, gradeLevel, className, applyDate).getData());
        return in;
    }

    public ByteArrayInputStream downloadTemplateFile(String years, String semester, String gradeLevel)
    {
        return TimetableExcelHelper.downloadTemplateFile(years, semester, gradeLevel, classroomRespository.getAllClassRoomByYearsAndGradeAllDept(years, Integer.parseInt(gradeLevel)));
    }

    @Override
    public ServiceResult<Boolean> upload(String year, String semester, String gradeLevel,
            String applyDate, MultipartFile file) {
        if (TimetableExcelHelper.isExcelFormat(file)) {
            try {
                HashMap<String, TimetableForm> timetableExcels = TimetableExcelHelper.parseFromExcel(file.getInputStream(), year, semester, Integer.parseInt(gradeLevel), applyDate, classroomRespository, subjectClassRepository, subjectsRepository);
                
                for(Map.Entry<String, TimetableForm> entry : timetableExcels.entrySet())
                {
                    this.updateTimeTable(entry.getValue());
                }

                return new ServiceResult<>(HttpStatus.OK, "updated", true);
            } catch (IOException e) {
                throw new ValidationException("file", "Unable to read " + file.getOriginalFilename());
            }
        }

        return new ServiceResult<Boolean>(HttpStatus.BAD_REQUEST, "faild to upload", false);
    }
}
