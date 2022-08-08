package fpt.capstone.service;

import fpt.capstone.repository.*;
import fpt.capstone.vo.StudentVo;
import fpt.capstone.vo.StudentDetailInformationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.entities.StudentHistory;
import fpt.capstone.entities.User;
import fpt.capstone.entities.Contact;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import fpt.capstone.form_data.StudentForm;

import fpt.capstone.payroll.ValidationException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fpt.capstone.security.services.UserDetailsImpl;
import fpt.capstone.utility.PhoneCheck;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private ContactRepository repositoryContact;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private StudentHistoryRepository repositoryStudentHistory;

    @Autowired
    private StudentRepository repositoryStudent;

    @Autowired
    private UserRepository repositoryUser;

    @Autowired
    private UserService userService;

    @Autowired
    private GradebookSubjectsDetailsRepository gradebookSubjectsDetailsRepository;


    @Override
    public ByteArrayInputStream exportTemplateExcel() {
        byte[] result;
        try {
            result = Files.readAllBytes(Paths.get("src/main/resources/importTemplate/DS_HocSinh.xlsx"));
            ByteArrayInputStream in = new ByteArrayInputStream(result);
            return in;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return null;
    }

    public Boolean isExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public Boolean isNumber(String tmp) {
        try {
            Integer.parseInt(tmp);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean isEmpty(String tmp) {
        if (tmp.trim().isEmpty() || tmp == null) {
            return true;
        } else {
            return false;
        }
    }


    //  [...]
    public Boolean isInrange(Integer start, Integer end, Integer value) {
        if ( value >= start && value <= end) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isPhoneNumber(String tmp) {
        if (!isNumber(tmp)) {
            return false;
        }
        boolean check = false;
        for (String phone : PhoneCheck.phoneHead) {
            if (tmp.startsWith(phone) && tmp.length() == 10) {
                check = true;
                break;
            }
        }
        return check;
    }

    @Override
    public ServiceResult<List<String>> importExcel(MultipartFile file, String year) throws IOException {
        if (isExcelFormat(file)) {
            Integer countSuccess = 0;
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            // Get all rows
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;

            Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
            result.put("student", new ArrayList<Integer>());
            result.put("contact", new ArrayList<Integer>());
            result.put("studenthistory", new ArrayList<Integer>());
            result.put("user", new ArrayList<Integer>());

            int processRow = 0;

            try {
                
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    String code = "";
                    String fullName = "";
                    Integer status = 0;
                    String phone = "";
                    String birthDay = "";
                    String nation = "";
                    String permanentAddress = "";
                    String socialInsuranceNumber = "";
                    String issuedDate ="";
                    Integer sex = null;
                    Integer deptId = null;
                    Integer trainingSystem = null;
                    String email = "";
                    String religion = "";
                    String homeTown = "";
                    String temporaryAddress = "";
                    String identityCard = "";
                    String issuedAddress = "";
                    String startDate = "";
                    Integer electFormat = null;
                    Integer graduationType = null;
                    Integer quanHe = null;
                    String hoTenPhuHuynh = "";
                    String soDienThoaiPhuHuynh = "";
                    Integer quanHeSecond = null;
                    String hoTenPhuHuynhSecond = "";
                    String soDienThoaiPhuHuynhSecond = "";
                    String createdTime = "";
                    String createdName = "";
                    String updateTime = "";
                    String updateName = "";
                    String classCode = "";

                    Boolean reachLast = false;


                    processRow = rowNumber;

                    if (rowNumber < 2) {
                        rowNumber++;
                        continue;
                    }
                    Boolean isFailed = false;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    
                    
                    Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

                    Boolean endLine = false;
                    List<String> errorMessage = new ArrayList<String>();
                    if (rowNumber >= 2) {
                        for(int cellIdx = 0; cellIdx <= 26; cellIdx ++){
                            Cell currentCell = currentRow.getCell(cellIdx);
                            if (currentCell  == null) {
                                currentCell = currentRow.createCell(cellIdx);
                            }
                            String tmp = "";

                            if(currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                Double tmpNum = currentCell.getNumericCellValue();
                                if ((double)tmpNum.intValue()  == tmpNum ) {
                                    tmp = "" + tmpNum.intValue();
                                } else {
                                    tmp = "" + currentCell.getNumericCellValue();
                                }
                            } else if( currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                tmp = currentCell.getStringCellValue();
                            } else if (currentCell.getCellType() == Cell.CELL_TYPE_BLANK){
                                tmp = "";
                            } else {
                                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lỗi định dạng dữ liêu khi đọc dòng "+ (processRow+1), null);
                            }
                            if (cellIdx == 0) {
                                if (isEmpty(tmp)) {
                                    endLine = true;
                                    break;
                                }
                            }
                            
                            if (cellIdx == 1) {
                                fullName = tmp;
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Tên học sinh không được để trống.");
                                } else if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Tên học sinh không được dài quá 250 ký tự.");
                                }
                            }
                            if (cellIdx == 2) {
                                code = tmp;
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add( "Mã học sinh không được để trống.");
                                } 
                                else if (tmp.length() > 50) {
                                    isFailed = true;
                                    errorMessage.add("Mã học sinh không được dài quá 50 ký tự.");
                                }
                                else if (repository.findByCode(code).size() > 0) {
                                    isFailed = true;
                                    errorMessage.add("Mã học sinh đã tồn tại.");
                                }
                            }
                            if (cellIdx == 3) {
                                classCode = tmp;
                                deptId = null;
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Mã lớp học không được để trống.");
                                } else if (tmp.length() > 50) {
                                    isFailed = true;
                                    errorMessage.add("Mã lớp học không được dài quá 50 ký tự.");
                                } else {

                                    List<Map<String, Object>> dep = classRoomRepository.getDepIdOfClassroom(tmp);

                                    if (dep.size() > 0) {
                                        deptId = Integer.parseInt("" + dep.get(0).get("dept_id"));
                                    } else {
                                        deptId =  null;
                                    }
                                }
                                if (deptId == null) {
                                    isFailed = true;
                                    errorMessage.add("Mã lớp học không tồn tại.");
                                }
                            }
                            if (cellIdx == 4) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Hệ đào tạo không được để trống.");
                                } 
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Hệ đào tạo phải là số.");
                                } else if (!isInrange(1, 6, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Hệ đào tạo phải có giá trị trong khoảng 1 đến 6.");
                                } else {
                                    trainingSystem = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 5) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Giới tính không được để trống.");
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Giới tính phải là số.");
                                } else if (!isInrange(0, 1, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Giới tính phải có giá trị là 0 hoặc 1.");
                                } else {
                                    sex = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 6) {
                                if (isEmpty(tmp)) {
                                    startDate = null;
                                }
                                else {
                                    try{
                                        LocalDateTime dateTmp = LocalDateTime.parse(tmp + " 00:00", formatter);
                                        startDate = tmp;
                                    } catch (Exception e) {
                                        isFailed = true;
                                        errorMessage.add("Ngày vào trường phải có định dạng dd-mm-yyy hoặc ngày vào trường không tồn tại.");
                                        startDate = null;
                                    }
                                }
                            }
                            if (cellIdx == 7) {
                                if (isEmpty(tmp)) {
                                    electFormat = null;
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Hình thức trúng tuyển phải là số.");
                                } else if (!isInrange(0, 1, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Hình thức trúng tuyển phải có giá trị là 0 hoặc 1.");
                                } else {
                                    electFormat = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 8) {
                                if (isEmpty(tmp)) {
                                    graduationType = null;
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại tốt nghiệp cấp dưới phải là số.");
                                } else if (!isInrange(1, 3, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Loại tốt nghiệp cấp dưới phải có giá trị từ 1 đến 3.");
                                } else {
                                    graduationType = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 9) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 1 không được để trống.");
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 1 phải là số.");
                                } else if (!isInrange(0, 5, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 1 phải có giá trị từ 0 đến 5.");
                                } else {
                                    quanHe = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 10) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Họ tên phụ huynh liên hệ 1 không được để trống.");
                                } else {
                                    hoTenPhuHuynh = tmp;
                                }
                            }
                            if (cellIdx == 11) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại phụ huynh 1 không được để trống. ");
                                }
                                else if (!isPhoneNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại phụ huynh 1 không đúng định dạng.");
                                } else {
                                    soDienThoaiPhuHuynh = tmp;
                                }
                            }
                            if (cellIdx == 12) {
                                if (isEmpty(tmp)) {
                                    quanHeSecond = null;
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 2 phải là số.");
                                } else if (!isInrange(0, 5, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 2 phải có giá trị từ 0 đến 5.");
                                } else {
                                    quanHeSecond = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 13) {
                                hoTenPhuHuynhSecond = tmp;
                            }
                            if (cellIdx == 14) {
                                if (isEmpty(tmp)) {
                                    soDienThoaiPhuHuynhSecond = "";
                                }
                                else if (!isPhoneNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại phụ huynh 2 không đúng định dạng.");
                                } else {
                                    soDienThoaiPhuHuynhSecond = tmp;
                                }
                            }
                            if (cellIdx == 15) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại không được để trống.");
                                }
                                else if (!isPhoneNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại không đúng định dạng.");
                                } else {
                                    phone = tmp;
                                }
                            } 
                            if (cellIdx == 16) {
                                if (isEmpty(tmp)) {
                                    email = "";
                                }
                                else {
                                    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(tmp);
                                    if (matcher.find()) {
                                        email =  tmp;
                                    } else if (tmp.length() > 250) {
                                        isFailed = true;
                                        errorMessage.add("Email không quá 250 ký tự.");                
                                    }
                                    else {
                                        isFailed = true;
                                        errorMessage.add("Email có định dạng không hợp lệ.");       
                                    }
                                }
                            }
                            if (cellIdx == 17) {
                                if (isEmpty(tmp)) {
                                    birthDay = null;
                                }
                                else {
                                    try{
                                        LocalDateTime dateTmp = LocalDateTime.parse(tmp + " 00:00", formatter);
                                        birthDay = tmp;
                                    } catch (Exception e) {
                                        isFailed = true;
                                        errorMessage.add("Ngày sinh phải có định dạng dd-mm-yyy hoặc ngày sinh không tồn tại.");
                                        birthDay = null;
                                    }
                                }
                            }
                            if (cellIdx == 18) {
                                religion = tmp;
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Tôn giáo không được quá 250 ký tự.");
                                } else {
                                    religion = tmp;
                                }
                            }
                            if (cellIdx == 19) {                            
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Dân tộc không được quá 250 ký tự.");
                                } else {
                                    nation = tmp;
                                }
                            }
                            if (cellIdx == 20) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Quê quán không được quá 250 ký tự.");
                                } else {
                                    homeTown = tmp;
                                }
                            }
                            if (cellIdx == 21) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Địa chỉ thường chú không được quá 250 ký tự.");
                                } else {
                                    permanentAddress = tmp;
                                }
                            }
                            if (cellIdx == 22) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Địa chỉ tạm chú không được quá 250 ký tự.");
                                } else {
                                    temporaryAddress = tmp;
                                }
                            }
                            if (cellIdx == 23) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Số CMND/CCCD không được quá 250 ký tự.");
                                } else {
                                    identityCard = tmp;
                                }
                            }
                            if (cellIdx == 24) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Nơi cấp CMND/CCCD không được quá 250 ký tự.");
                                } else {
                                    issuedAddress = tmp;
                                }
                            }
                            if (cellIdx == 25) {
                                if (isEmpty(tmp)) {
                                    issuedDate = null;
                                }
                                else {
                                    try{
                                        LocalDateTime dateTmp = LocalDateTime.parse(tmp + " 00:00", formatter);
                                        issuedDate = tmp;
                                    } catch (Exception e) {
                                        isFailed = true;
                                        errorMessage.add("Ngày cấp CMND phải có định dạng dd-mm-yyy hoặc Ngày cấp CMND không tồn tại.");
                                        issuedDate = null;
                                    }
                                }
                                
                            }

                            if (cellIdx == 26) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Số sổ BHXH không được quá 250 ký tự.");
                                } else {
                                    socialInsuranceNumber = tmp;
                                }
                            }
                            if (cellIdx == 26) {
                                reachLast = true;
                                break;
                            }
                        }
                    }
                    if (endLine) {
                        break;
                    }
                    if (isFailed || !reachLast) {
                        rollBackForStudent(result);
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Lỗi tại dòng " + (processRow+1), errorMessage);
                    } else {
                        StudentForm form = new StudentForm("", code, fullName, status, phone, birthDay, nation, permanentAddress, socialInsuranceNumber, issuedDate, sex, deptId, trainingSystem, email, religion, homeTown, temporaryAddress, identityCard, issuedAddress, startDate, electFormat, graduationType, quanHe, hoTenPhuHuynh, soDienThoaiPhuHuynh, quanHeSecond, hoTenPhuHuynhSecond, soDienThoaiPhuHuynhSecond, createdName, updateTime, updateName, classCode, year);
                        Map<String, List<Integer>> insertAStudent = insertAStudent(form);
                        if (insertAStudent == null) {
                            rollBackForStudent(result);
                        } else {
                            countSuccess += 1;
                            result.get("student").addAll(insertAStudent.get("student"));
                            result.get("contact").addAll(insertAStudent.get("contact"));
                            result.get("studenthistory").addAll(insertAStudent.get("studenthistory"));
                            result.get("user").addAll(insertAStudent.get("user"));
                        }
                    }
                    rowNumber++;
                }
            } catch (Exception e) {
                rollBackForStudent(result);
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Import file không thành công. Lỗi khi sử lý dòng "+ (processRow+1)+"\n", null);
            }
            if (countSuccess == 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File import không có dữ liệu học sinh", null);
            } else {
                return new ServiceResult<>(HttpStatus.OK, countSuccess + " học sinh đã được thêm mới", null);
            }
        } else {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File không đúng định dạng", null);
        }
    }

    @Override
    public ServiceResult<List<StudentVo>> getAllStudentByNameOrCodeInClass(String status, String class_code, String search_word, String grade_level, String years) {
        return new ServiceResult<>(HttpStatus.OK, "Student list", repository.getAllStudentByNameOrCodeInClassAllStatus(status, class_code, search_word, grade_level, years));        
    }

    @Override
    public ServiceResult<Boolean> insert(StudentForm form) {
        if (form.getCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin mã học sinh", true);
        } else {
            if (repository.findByCode(form.getCode()).size() > 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã học sinh đã tồn tại", true);
            }
        }

        if (form.getFullName().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin tên học sinh", true);
        }

        if (form.getStatus() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin trạng thái", true);
        }

        if (form.getTrainingSystem() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin hệ đào tạo", true);
        }

        if (form.getQuanHe() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin phụ huynh", true);
        }

        if (form.getHoTenPhuHuynh().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin họ tên phụ huynh", true);
        }

        if (form.getSoDienThoaiPhuHuynh().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin số điện thoại phụ huynh", true);
        }

        if (form.getClassCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin lớp học", true);
        }

        Map<String, List<Integer>> insertAStudent = insertAStudent(form);
        if (insertAStudent == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lỗi khi tạo học sinh", true);
        } else {
            return new ServiceResult<>(HttpStatus.OK, "Tạo mới học sinh thành công", true);
        }
      
    }


    private Map<String, List<Integer>> insertAStudent(StudentForm form) {
        Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
        result.put("student", new ArrayList<Integer>());
        result.put("contact", new ArrayList<Integer>());
        result.put("studenthistory", new ArrayList<Integer>());
        result.put("user", new ArrayList<Integer>());

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            String createdName = userDetails.getUsername();

            Student s = new Student(form.getCreatedTime(), createdName, form.getUpdateTime(), form.getUpdateName(), form.getFullName(), form.getCode(), form.getDeptId(), form.getStartDate(), form.getPhone(), form.getEmail(), form.getBirthDay(), form.getHomeTown(), form.getNation(), form.getPermanentAddress(), form.getSocialInsuranceNumber(), form.getIdentityCard(),form.getIssuedAddress(), form.getIssuedDate(), form.getSex(), form.getAvatar(), form.getElectFormat(), form.getGraduationType(), form.getStatus(), form.getTrainingSystem(), form.getReligion(), form.getTemporaryAddress());
            List<Student> students = new ArrayList<>();
            students.add(s);
            List<Student> resultSaveStudent = repository.saveAll(students);
            for(Student i: resultSaveStudent) {
                result.get("student").add(i.getId());
            }
            Integer studentId = resultSaveStudent.get(0).getId();
            
            Contact c_primary = new Contact("", LocalDateTime.now(), form.getHoTenPhuHuynh(), form.getSoDienThoaiPhuHuynh(), 1, form.getQuanHe(), studentId);
            List<Contact> contacts = new ArrayList<>();
            contacts.add(c_primary);

            Contact c_second = new Contact("", LocalDateTime.now(), form.getHoTenPhuHuynhSecond(), form.getSoDienThoaiPhuHuynhSecond(), 0, form.getQuanHeSecond(), studentId);
            contacts.add(c_second);

            List<Contact> resultSaveContact = repositoryContact.saveAll(contacts);

            for (Contact i : resultSaveContact) {
                result.get("contact").add(i.getId());
            }

            StudentHistory sh = new StudentHistory(form.getCode(), form.getClassCode(), form.getYear());
            List<StudentHistory> studentHistoryList = new ArrayList<>();
            studentHistoryList.add(sh);
            List<StudentHistory> resultSaveStudentHistory = repositoryStudentHistory.saveAll(studentHistoryList);
            for(StudentHistory i: resultSaveStudentHistory) {
                result.get("studenthistory").add(i.getId());
            }

            User userSaved = userService.insertUserForStudent(form.getCode(), form.getFullName(),  form.getSoDienThoaiPhuHuynh(),form.getAvatar(), form.getEmail(), null );
            if (userSaved == null) {
                throw new Exception();
            }
            result.get("user").add(userSaved.getId());
        } catch (Exception e) {
            rollBackForStudent(result);
            return null;
        }
        return result;
    }

    private void rollBackForStudent (Map<String, List<Integer>> result) {
        for(Integer i: result.get("student")) {
            try {
                repository.deleteById(i);
            } catch (Exception e) {

            }
            
        }
        for(Integer i: result.get("contact")) {
            try {
                repositoryContact.deleteById(i);
            } catch (Exception e) {
            }
        }
        for(Integer i: result.get("studenthistory")) {
            try {
                repositoryStudentHistory.deleteById(i);
            } catch (Exception e) {

            }
        }
        for(Integer i: result.get("user")) {
            try {
                repositoryUser.deleteById(i);
            } catch  (Exception e) {
            }
        }
    }

    @Override
    public ServiceResult<Boolean> update(StudentForm form) {
        if(form.getCode().length() == 0)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin mã học sinh", true);
        }

        if(form.getFullName().length() == 0)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin tên học sinh", true);
        }

        if(form.getStatus() == null)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin trạng thái", true);
        }

        if(form.getTrainingSystem() == null)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin hệ đào tạo", true);
        }

        if(form.getQuanHe() == null)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin phụ huynh", true);
        }

        if(form.getHoTenPhuHuynh().length() == 0)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Thiếu thông tin họ tên phụ huynh", true);
        }

        if (form.getSoDienThoaiPhuHuynh().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Thiếu thông tin số điện thoại phụ huynh", true);
        }

        if (form.getClassCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Thiếu thông tin lớp học", true);
        }

        Student s = new Student(form.getCreatedTime(), "", form.getUpdateTime(), form.getUpdateName(), form.getFullName(), form.getCode(), form.getDeptId(), form.getStartDate(), form.getPhone(), form.getEmail(), form.getBirthDay(), form.getHomeTown(), form.getNation(), form.getPermanentAddress(), form.getSocialInsuranceNumber(), form.getIdentityCard(),form.getIssuedAddress(), form.getIssuedDate(), form.getSex(), form.getAvatar(), form.getElectFormat(), form.getGraduationType(), form.getStatus(), form.getTrainingSystem(), form.getReligion(), form.getTemporaryAddress());
        
        repository.update(s.getCode(), s.getUpdateName(), s.getFullName(), s.getDeptId(), s.getStartDate(), s.getPhone(), s.getEmail(), s.getBirthDay(), s.getHomeTown(), s.getNation(), s.getPermanentAddress(), s.getSocialInsuranceNumber(), s.getIdentityCard(), s.getIssuedAddress(), s.getIssuedDate(), s.getSex(), s.getAvatar(), s.getElectFormat(), s.getGraduationType(), s.getStatus(), s.getTrainingSystem(), s.getReligion(), s.getTemporaryAddress());
        Integer studentId = repository.findByCode(s.getCode()).get(0).getId();

        repositoryContact.updatePrimary(form.getHoTenPhuHuynh(), form.getSoDienThoaiPhuHuynh(), form.getQuanHe(), studentId);
        repositoryContact.updateSecond(form.getHoTenPhuHuynhSecond(), form.getSoDienThoaiPhuHuynhSecond(), form.getQuanHeSecond(), studentId);

        repositoryStudentHistory.update(form.getClassCode(), form.getCode());
        // StudentHistory sh = new StudentHistory(form.getCode(), form.getClassCode(), form.getYear());
        // List<StudentHistory> studentHistoryList = new ArrayList<>();
        // studentHistoryList.add(sh);
        // repositoryStudentHistory.saveAll(studentHistoryList);

        return new ServiceResult<>(HttpStatus.OK, "Cập nhật học sinh thành công!", true);
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getStudentOfClass(String year, String classCode, Integer semester, String subjectCode) {
        ServiceResult serviceResult = new ServiceResult();
        try{
            List<Student> list = repository.getStudentOfClass(year, classCode);
            List<Map<String, Object>> listStudentEvaluated = gradebookSubjectsDetailsRepository.listStudentOfClassEvaluated(year, classCode, semester, subjectCode);
            List<Map<String, Object>> listOutput = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                Map<String, Object> output = new HashMap<>();
                output.put("code", list.get(i).getCode());
                output.put("fullName", list.get(i).getFullName());
                output.put("isEvaluated", 0);
                output.put("evaluate", null);
                for(int j = 0;j < listStudentEvaluated.size(); j++){
                    if(list.get(i).getCode().equals(listStudentEvaluated.get(j).get("code").toString())){
                        output.put("isEvaluated", 1);
                        if(listStudentEvaluated.get(j).get("evaluate").equals(null)){
                            output.put("isEvaluated", 0);
                        }else{
                            if(listStudentEvaluated.get(j).get("evaluate").toString().trim().equals("")){
                                output.put("isEvaluated", 0);
                            }else{
                                output.put("evaluate", listStudentEvaluated.get(j).get("evaluate"));
                            }
                        }
                        break;
                    }
                }
                listOutput.add(output);
            }
            serviceResult.setData(listOutput);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }catch (Exception e){
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<Student>> getStudentOfClassScreenDanhGiaHocLuc(String year, String classCode) {
        ServiceResult serviceResult = new ServiceResult();
        try{
            List<Student> list = repository.getStudentOfClassManDanhGiaHocLuc(year, classCode);
            Map<String, Object> output = new HashMap<>();
            output.put("data", list);
            serviceResult.setData(output);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }catch (Exception e){
            serviceResult.setMessage("failed");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    public ServiceResult<List<StudentDetailInformationVo>> getStudentDetailInformation(String student_code) {
        return new ServiceResult<>(HttpStatus.OK, "Student information", repository.getStudentDetailInformation(student_code, PageRequest.of(0, 1)));        
    }

    @Override
    public ServiceResult<List<Student>> getStudentManagedByClass(String classCode, String year) {
        return new ServiceResult<>(HttpStatus.OK, "students", repository.getCurrentStudentsInClass(classCode, year));
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getAllYearHistoryOfStudent(String studentCode) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", repository.getAllYearHistoryOfStudent(studentCode));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "fail", null);
        }
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getResultStudyEver(String classCode, String year, String studentCode, Integer classId) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", repository.getResultStudyEver(classCode, year, studentCode, classId));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Override
    public ServiceResult<List<StudentDetailInformationVo>> getStudentDetailInformationSideParent(String studentCode, String year) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", repository.getStudentDetailInformationSideParent(studentCode, year,PageRequest.of(0, 1)));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

}
