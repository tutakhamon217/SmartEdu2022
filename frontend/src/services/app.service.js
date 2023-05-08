import $http from "@/plugins/axios";

const AppService = {
    getAllYear() {
        return $http.get("/school_years/all")
    },
    getAllKhoi() {
        return $http.get('/gradelevel/dropdown')
    },
    getAllDept() {
        return $http.get('/department/all')
    },
    getAllSchoolYearInformation() {
        return $http.get('/school_years/all/paging', {
            params: {
                "pageIndex": 0,
                "pageSize": 0
            }
        })
    },
    getAllSchoolSubject() {
        return $http.get('/subjects/all/load')
    },
    getSchoolSubjectPaging(data) {
        // data.gradeLevel = parseInt(data.gradeLevel)
        return $http.get('/subjects/all/paging', {
            params: {
                "pageSize": data.pageSize,
                "pageIndex": data.pageIndex,
                "deptId": data.deptId,
                "code": data.code,
                "gradeLevel": data.gradeLevel,
                "name": data.name
            }
        })
    },
    saveSchoolSubject(data) {
        const formData = new FormData();
        formData.append('code', data.code)
        formData.append('name', data.name)
        formData.append('gradeLevel', data.gradeLevel)
        formData.append('deptId', data.deptId)
        formData.append('type', data.type)
        formData.append('subType', data.subType)
        formData.append('description', data.description)
        return $http.post("/subjects/add", formData)
    },
    updateSchoolSubject(data) {
        let index = data.khoi.indexOf(" ");
        let resultKhoi = data.khoi.substring(index, data.khoi.length).trim()
        const formData = new FormData();
        let deptId = []
        data.khoa_ban_id.forEach(x => {
            deptId.push(x.dept_id)
        })
        formData.append('code', data.ma_mon_hoc)
        formData.append('name', data.ten_mon_hoc)
        formData.append('gradeLevel', resultKhoi)
        formData.append('deptId', deptId)
        formData.append('type', data.loai_mon_id)
        formData.append('subType', data.kieu_mon_id)
        formData.append('description', data.mo_ta)
        return $http.put(`/subjects/update?id=${data.id}`, formData)
    },
    deleteSchoolSubject(id) {
        return $http.delete('/subjects/delete?id=' + id)
    },
    getStructureOfGroup() {
        return $http.get("/department/all")
    },
    saveSchoolYear(data) {
        return $http.post("/school_years/save", data)
    },
    searchKhoaBan(code, name) {
        return $http.post("/department/search", { "code": code, "name": name })
    },
    getClassByKhoiAndKhoaBan(gradeLevel, deptId, year) {
        return $http.get('/subjectClass/class', {
            params: {
                'gradeLevel': gradeLevel,
                'deptId': deptId,
                'years': year,
            }
        })
    },
    //search of subjectforclass screen (Thanh)
    getInfoSubjectBySearch(classId, nameCode, gradeLevel, deptId) {
        return $http.get('/subjectClass/all', {
            params: {
                classId,
                nameCode,
                gradeLevel,
                deptId
            }
        })
    },
    saveSubjectForClass(data) {
        return $http.post('/subjectClass/save', data)
    },
    getAmountOfSemester(year) {
        return $http.get('/school_years/semesterAmount?years=' + year)
    },
    checkSemesterStartEnd(years, semester) {
        return $http.get('/school_years/checkRange?years=' + years + "&semester=" + semester)
    },
    getAllDepartments() {
        return $http.get("/department/dropdown")
    },
    getAllDepartmentWithType() {
        return $http.get("/department/dropdown_with_type")
    },
    getAllDepartmentType() {
        return $http.get("/ap_param/dropdown/DEPT")
    },
    getTeacherOfDepartment(departmentId) {
        return $http.get("/teacher/dropdown/" + departmentId)
    },
    getTeachersOfRootParent(departmentId) {
        return $http.get("/teacher/dropdown_root/" + departmentId)
    },
    addNewDepartment(department) {
        return $http.post("/department/insert",
            {
                "name": department.name,
                "code": department.code,
                "parentID": department.parentID,
                "position": department.position,
                "employeeID": department.employeeID,
                "description": department.description,
                "type": department.type
            }
        )
    },
    updateDepartment(department) {
        return $http.post("/department/update",
            {
                "id": department.id,
                "name": department.name,
                "code": department.code,
                "parentID": department.parentID,
                "position": department.position,
                "employeeID": department.employeeID,
                "description": department.description,
                "type": department.type
            }
        )
    },
    deleteDepartment(departmentID) {
        return $http.delete("/department/delete/" + departmentID)
    },
    checkDisableUpdateParentAndTypeDepartment(departmentID) {
        return $http.delete("/department/check/" + departmentID)
    },
    addClass(classInformation) {
        return $http.post("/classroom/insert",
            {
                "years": classInformation.years,
                "code": classInformation.classCode,
                "name": classInformation.className,
                "gradeLevel": classInformation.khoiId,
                "deptID": classInformation.khoaID,
                "specialize": classInformation.monChuyenID,
                "teacher": classInformation.teacherID,
            }
        )
    },
    updateClass(classInformation) {
        return $http.post("/classroom/update",
            {
                "years": classInformation.years,
                "code": classInformation.classCode,
                "name": classInformation.className,
                "gradeLevel": classInformation.khoiId,
                "deptID": classInformation.khoaID,
                "specialize": classInformation.monChuyenID,
                "teacher": classInformation.teacherID,
            }
        )
    },
    deleteClass(classCode) {
        return $http.delete("/classroom/delete/" + classCode)
    },
    getAllGradeLevel() {
        return $http.get("/gradelevel/dropdown")
    },
    getAllDepartmentForClass() {
        return $http.get("/department/dropdown/classroom")
    },
    searchClass(years, code, name, gradeLevel, deptID) {
        let data = {
            "years": years,
            "code": code,
            "name": name,
            "gradeLevel": gradeLevel,
            "deptID": deptID,
            "pageIndex": 0,
            "pageSize": 10,
        }
        return $http.post("/classroom/search", data)
    },
    getMonChuyen(departmentID, gradeId) {
        return $http.get("/subjects/dropdown/department?deptID=" + departmentID + "&gradeId=" + gradeId)
    },
    getGiaoVienChuNhiem() {
        return $http.get("/teacher/gvcn/")
    },
    getTeachingAssignment(nameCodeTeacher, nameCodeSubject, classId) {
        return $http.get('/teachingAssignment/all', {
            params: {
                nameCodeSubject,
                nameCodeTeacher,
                classId
            }
        })
    },
    getAllTeacher() {
        return $http.get('/teacher/all')
    },
    getClassByGradeLevel(gradeLevel, years) {
        return $http.get('/classroom/gradeLevelAndYears', {
            params: {
                gradeLevel,
                years
            }
        })
    },
    updateTeachingAssignment(data) {
        return $http.put('/teachingAssignment/update', {
            "teachingAssignmentId": data.teachingAssignmentId,
            "teacherId": data.teacherId,
            "semester1": data.semester1 ? 1 : 0,
            "semester2": data.semester2 ? 1 : 0,
            "applyAllSemester": data.applyAllSemester,
            "subjectCode": data.subjectCode,
            "classCode": data.classCode
        })
    },
    deleteTeachingAssignment(id) {
        return $http.delete(`/teachingAssignment/delete/${id}`)
    },
    getSubjectByDeptAndSupType(grade_level, type, year, grade_id, semester) {
        return $http.get('/subject/dept-type', {
            params: {
                grade_level,
                type,
                year,
                grade_id,
                semester
            }
        })
    },
    getConfigScoreSubject(parent_code) {
        return $http.get('/subject/config-score-subject', {
            params: {
                parent_code
            }
        })
    },
    getConfigGradingDetails(parent_code) {
        return $http.get('/subject/config-grading-details', {
            params: {
                parent_code
            }
        })
    },
    addConfigScoreDetail(data) {
        return $http.post('/config-score-detail', data)
    },
    deleteConfigScoreDetail(data) {
        return $http.delete('/config-score-detail', { data })
    },
    updateChangeConfigScoreDetail(data) {
        return $http.put('/config-score-detail', data)
    },
    addConfigGradeDetail(data) {
        let temp = []
        data.forEach(element => {
            temp.push(JSON.parse(JSON.stringify(element)))
        });
        temp.forEach(x => {
            x.type_choose = x.type_choose ? 1 : 0
        })
        return $http.post('/config-grade-detail', temp)
    },
    deleteConfigGradeDetail(data) {
        return $http.delete('/config-grade-detail', { data })
    },
    updateChangeConfigGradeDetail(data) {
        let temp = []
        data.forEach(element => {
            temp.push(JSON.parse(JSON.stringify(element)))
        });
        temp.forEach(x => {
            x.type_choose = x.type_choose ? 1 : 0
        })
        return $http.put('/config-grade-detail', temp)
    },
    getCodeOfConfScoreSubject(data) {
        return $http.get(`/config-score-subject/${data}`)
    },
    getGradeByYearInConfigScoreboard(year) {
        return $http.get('/config-scoreboard', {
            params: {
                year
            }
        })
    },
    getSubjectsNotYetConfigScoreboard(grade_level, sup_type, year, grade_id, semester) {
        return $http.get('/subject/subject-not-yet-in-scoreboard', {
            params: {
                grade_level,
                sup_type,
                year,
                grade_id,
                semester
            }
        })
    },
    configFromBeginnigScoreboardScore(data) {
        return $http.post('/scoreboard-score', data)
    },
    configFromBeginnigScoreboardGrading(data) {
        let temp = []
        data.configGradeDetailForms.forEach(element => {
            temp.push(JSON.parse(JSON.stringify(element)))
        });
        temp.forEach(x => {
            x.type_choose = x.type_choose ? 1 : 0
        })
        data.configGradeDetailForms = temp
        return $http.post('/scoreboard-grading', data)
    },
    getClassByGradeAndYears(grade, years, deptID) {
        return $http.get("/classroom/school_years?gradeLevel=" + grade + "&years=" + years + "&deptID=" + deptID)
    },
    getClassByGradeAndYearsAllDept(grade, years) {
        return $http.get("/classroom/school_years_alldept?gradeLevel=" + grade + "&years=" + years)
    },
    searchStudentInClassWithNameOrStudentCode(maKhoi, status, classCode, searchWord, year, pageIndex) {
        return $http.get("/student/search?status=" + status + "&class_code=" + classCode + "&search_word=" + searchWord + "&grade_level=" + maKhoi + "&years=" + year+"&pageIndex="+pageIndex+"&pageSize=10")
    },
    addStudent(studentForm) {
        return $http.post("/student/insert", studentForm)
    },
    updateStudent(studentForm) {
        return $http.post("/student/update", studentForm)
    },
    getStudentDetailInformationByYear(student_code, year) {
        return $http.post("/student/detail", { id: student_code, year: year })
    },
    login(data) {
        return $http.post("/api/auth/signin", data)
    },
    changePassword(data){
        return $http.post("/api/auth/change_password", data)
    },
    getDropdownDonVi() {
        return $http.get("/department/dropdown/parent")
    },
    getDropdownKhoaBanTeacher(id) {
        return $http.get(`/department/dropdown/${id}`)
    },
    getAllChucVu() {
        return $http.get('/authority/all')
    },
    getInfoTeacherBySearch(data) {
        return $http.get('/teacher/all/search', {
            params: data
        })
    },
    getDropdownKhoaBanForTruongKhoa(id) {
        return $http.get(`/department/parentDepartment/${id}`)
    },
    //by Thành, do not modify
    getCurrentUser() {
        return $http.get('/api/auth/current-user')
    },
    getInforTeacher(id) {
        return $http.get(`/teacher/${id}`)
    },
    addTeacher(data) {
        return $http.post('/teacher/add', data)
    },
    updateTeacher(id, data) {
        return $http.put(`/teacher/update/${id}`, data)
    },
    getClassroomByTeacherIdAndYear(id, year) {
        return $http.get('/classroom/teacher-year', {
            params: {
                id,
                year
            }
        })
    },
    getClassroomByCurrentTeacherAndYear(year) {
        return $http.get('/classroom/teacher?years=' + year)
    },
    getInfoConductOfClass(data) {
        return $http.get('/assess-student-conduct-detail', { params: data })
    },
    getEvaluateOfTeacher(data) {
        return $http.get('/assess-student-conduct-detail/conduct-of-teacher', { params: data })
    },
    getAllStudentOfClass(data) {
        return $http.get('/student/student-of-class', { params: data })
    },
    getAllStudentOfClassDanhGiaHocLuc(data) {
        return $http.get('/student/student-danh-gia-hoc-luc', { params: data })
    },
    updateAssessStudentConductDetail(data) {
        return $http.put('/assess-student-conduct-detail/update', data)
    },
    checkAuthToken() {
        return $http.get('/api/auth/checkAuthToken')
    },
    getReportTheResultOfCompetition(data) {
        return $http.get('/assess-student-conduct-detail/get-report-the-result-of-competition', { params: data })
    },
    getReportTheResultOfEachClass(data) {
        return $http.get('/assess-student-conduct-detail/get-report-the-result-of-each-class', {
            params: data
        })
    },
    //Bắt đầu màn sổ điểm
    getListClassRoomByLoginUser(data) {
        return $http.post('/gradebooks/classroom-list', data)
    },
    getListSubjectByLoginUser(data) {
        return $http.post('/gradebooks/subject-list', data)
    },
    searchSoDiem(data) {
        return $http.post('/gradebooks/search', data)
    },
    saveSoDiem(data, username){
        return $http.post(`/gradebooks/save?login=${username}`, data)
    },
    getTimetableTeachingAssignment(data) {
        return $http.get("/timetable/dropdown",
            {
                params: data
            })
    },
    getTimetable(data) {
        return $http.get("/timetable", {
            params: data
        })
    },
    exportTimeTable(data) {
        return $http.get("/timetable/export",
        {
            params: data,
            responseType: 'arraybuffer',
            headers: {
                'Accept': 'application/octet-stream',
                'Content-Type': 'application/json'
            },
        }
        )
    },
    updateTimeTable(data) {
        return $http.post("/timetable/update", data)
    },
    downloadExcelTeachingAssignment(year) {
        return $http.get(`/teachingAssignment/download?year=${year}`, {
            responseType: 'arraybuffer',
            headers: {
                'Accept': 'application/octet-stream',
            }
        }
        )
    },
    importExcelTeachingAssignment(file, years) {
        let formData = new FormData()
        formData.append('file', file)
        formData.append('years', years)
        return $http.post('/teachingAssignment/upload', formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
            })
    },
    getSubjectInClassInsemester(class_code, semester) {
        return $http.get("/subject/subject-of-class?class_code=" + class_code + "&semester=" + semester)
    },
    getSchoolYear(data) {
        return $http.get("/school_years", {
            params: data
        })
    },
    getClassManagedBy(data) {
        return $http.get("/classroom/manage", {
            params: data
        })
    },
    getStudentsInClass(data) {
        return $http.get('/student/class', {
            params: data
        })
    },
    getAttendance(data) {
        return $http.get('/attendance', {
            params: data
        })
    },
    postUpdateAttendance(data) {
        return $http.post("/attendance/update", data)
    },
    getHoliday() {
        return $http.get('/ap_param/holiday')
    },
    exportExcelGradeBookScore(data) {
        return $http.post(`/gradebooks/score/download?year=${data.year}&subjectCode=${data.subjectCode}&classCode=${data.classCode}&semester=${data.semester}`,
            { 'confScoreDetailsIdList': data.confScoreDetailsIdList },
            {
                responseType: 'arraybuffer',
                headers: {
                    'Accept': 'application/octet-stream',
                    'Content-Type': 'application/json'
                },
            }
        )
    },
    exportExcelGradeBookGrading(data) {
        return $http.post(`/gradebooks/grade/download?year=${data.year}&subjectCode=${data.subjectCode}&classCode=${data.classCode}&semester=${data.semester}`,
            { 'confGradeDetailsIdList': data.confGradeDetailsIdList.sort() },
            {
                responseType: 'arraybuffer',
                headers: {
                    'Accept': 'application/octet-stream',
                    'Content-Type': 'application/json'
                },
            }
        )
    },
    importExcelGradeBook(data, type) {
        let formData = new FormData()
        formData.append('file', data.file)
        formData.append('subjectCode', data.subjectCode)
        formData.append('classCode', data.classCode)
        formData.append('schoolYear', data.schoolYear)
        formData.append('semester', data.semester)
        formData.append('login', data.login)
        if(type == 'grade'){
            formData.append('confGradeDetailsIdList', data.confScoreDetailsIdList) 
        }else{
            formData.append('confScoreDetailsIdList', data.confScoreDetailsIdList) 
        }
        return $http.post(`/gradebooks/${type}/upload`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
        })
    },


    // man danh gia hoc luc
    getSubjectClassByClassIdAndClassCodeSemester(data) {
        return $http.get('/subjectClass/danh-gia-hoc-luc/semester', {
            params: data
        })
    },
    getAvgScore(data) {
        return $http.get('/gradebook/avg-score', {
            params: data
        })
    },
    getXepLoai(data) {
        return $http.get('/gradebook/xep-loai', {
            params: data
        })
    },
    getAllHocLuc() {
        return $http.get("/ap_param/dropdown/ABILITY")
    },
    getAllStudentRated(data) {
        return $http.get('/gradebook/student-rate', {
            params: data
        })
    },
    updateStudentRate(data) {
        return $http.post('/gradebook/update-student-rate', data)
    },
    getAllYearHistoryOfStudent(studentCode) {
        return $http.get('/student/history-year', {
            params: {
                studentCode
            }
        }
        )
    },
    getTableScoreForIndi(data) {
        return $http.get('/gradebooks/subject-score-of-student', { params: data })
    },
    getResultStudyEver(data) {
        return $http.get('/student/get-result-study-ever', {
            params: data
        })
    },
    getSemesterOf(data) {
        return $http.get('/school_year/of', {
            params: data
        })
    },
    getTimetableDropdown(data) {
        return $http.get('/timetable/dropdown', {
            params: data
        })
    },
    getStudentDetailInformationSideParent(data) {
        return $http.get('/student/detail-side-parent', {
            params: data
        })
    },
    getSchoolYearOfHistoryStudent(data) {
        return $http.get('/school_year/history', {
            params: {
                studentCode: data
            }
        })
    },
    checkUpdateConduct(semester) {
        return $http.get('/assess-student-conduct-detail/check-update', {
            params: {
                semester
            }
        })
    },
    getObjCurrentYear() {
        return $http.get('/school_year/obj_current_year')
    },
    getClassByGradeAndYear(data) {
        return $http.get('/classroom/gradeLevelAndYears', { params: data })
    },
    getAttendanceInformation(year, month, studentCode) {
        return $http.get("/attendance/student", {
            params: {
                year: year,
                month: month,
                studentCode: studentCode,
            }
        })
    },
    searchTransferStudent(data) {
        return $http.post('/transfer-students/search', data)
    },
    getCurrentYearAndNextYear() {
        return $http.get('/school_year/CurrentAndNextSchoolYear')
    },
    updateListTransferStudent(data){
        return $http.post('/transfer-students/transfer', data)
    },
    exportKetChuyenFile(data, className, currentYear, newYear){
        return $http.post(`/transfer-students/export?className=${className}&currentYear=${currentYear}&newYear=${newYear}`,
        data,
        {
            responseType: 'arraybuffer',
            headers: {
                'Accept': 'application/octet-stream',
                'Content-Type': 'application/json'
            },
        }
        )
    },
    updateOneTransferStudent(data){
        return $http.post('/transfer-students/transfer1student/update', data)
    },
    getTeacherBySubjectId(id) {
        return $http.get('/teacher/teachingAssignment', { params: { subjectId: id } })
    },

    // otp by trong chien
    sendOTP(username) {
        return $http.post('/api/auth/forgot_password', { login: username })
    },
    checkOTP(username, otp) {
        return $http.post('/api/auth/check_otp', { otp: otp, login: username })
    },
    //school information
    getAllDistrict(data){
        return $http.get('/district', {
            params: {
                province: data
            }
        })
    },
    getAllProvince(){
        return $http.get('/province')
    },
    getSchoolInfo(){
        return $http.get('/school-infor')
    },
    saveSchool(data){
        return $http.post('/school-infor/save', data)
    },

    // import student by chien
    downloadExcelImportStudent() {
        return $http.get(`/student/download`, {
            responseType: 'arraybuffer',
            headers: {
                'Accept': 'application/octet-stream',
            }
        }
        )
    },

    importExcelStudent(file, year) {
        let formData = new FormData()
        formData.append('file', file)
        formData.append('year', year)
        return $http.post('/student/upload', formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
            })
    },

    downloadTimetableTemplateFile(years, semester, gradeLevel) {
        return $http.get(`/timetable/template?years=${years}&semester=${semester}&gradeLevel=${gradeLevel}`, {
            responseType: 'arraybuffer',
            headers: {
                'Accept': 'application/octet-stream',
                'Content-Type': 'application/json'
            }
        }
        )
    },
    uploadTimetable(file, year, semester, gradeLevel, applyDate) {
        let formData = new FormData()
        formData.append('file', file)
        formData.append('year', year)
        formData.append('semester', semester)
        formData.append('gradeLevel', gradeLevel)
        formData.append('applyDate', applyDate)
        return $http.post(`/timetable/upload`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
        })

    },

    // ket chuyen by chien 
    getAllCompetititon() {
        return $http.get("/ap_param/dropdown/COMPETITION")
    },
    evaluateSubjectByTeacher(data){
        let form = new FormData()
        form.append("year", data.year)
        form.append("classCode", data.classCode)
        form.append("semester", data.semester)
        form.append("listStudentCode", data.listStudentCode)
        form.append("evaluate", data.evaluate)
        form.append("subjectCode", data.subjectCode)
        return $http.post('/gradebook-subject-detail/evaluate', form, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    },
    getTeachingTimeTableForTeacher(data){
        return $http.get('/timetable/teacher', {
            params: data
        })
    },
    getRangeOfSemester(data){
        return $http.get('/school_year/get-range', {
            params: data
        })
    },
    findByCodeTeacher(code){
        return $http.get('/teacher/find-by-code', {
            params: {
                code : code
            }
        })
    },
    checkCanChangeClass(year, studentCode) {
        return $http.get('/gradebooks/check-change-class',{
            params: {
                year : year,
                studentCode: studentCode,
            }
            })
    }
}


export default AppService