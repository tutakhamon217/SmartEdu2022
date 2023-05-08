<template>
<div>
    <v-card class="md-3">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title class="text-center">Thông tin tìm kiếm</v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>
        <v-card class="pa-6" elevation="0">
            <v-form ref="form" v-model="valid" lazy-validation>
                <v-row>
                    <v-col sm="12" md="6">
                        <v-select :items="semesterList" label="Học kỳ" v-model="chosenSemester" item-value="id" item-text="name" :rules="[this.rule.required]">
                        </v-select>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu transition="scale-transition" offset-y min-width="auto">
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field append-icon="mdi-calendar" readonly v-on="on" v-bind="attrs" label="Ngày áp dụng" v-model="chosenDate">
                                </v-text-field>
                            </template>
                            <v-date-picker locale="vi-VN" :min="startDate" :max="endDate" v-model="chosenDate"></v-date-picker>
                        </v-menu>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col sm="12" md="6">
                        <v-select :items="gradeList" label="Khối" v-model="chosenGrade" item-value="id" item-text="name" :rules="[this.rule.required]">
                        </v-select>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-select :items="classList" label="Lớp" v-model="chosenClassroom" item-value="id" item-text="name" :rules="[this.rule.required]">
                        </v-select>
                    </v-col>
                </v-row>
                <v-row align="center" justify="center">
                    <v-btn color="primary" @click="search">
                        <v-icon>mdi-magnify</v-icon>
                        Tìm kiếm
                    </v-btn>
                </v-row>
            </v-form>
        </v-card>
    </v-card>

    <v-card class="mt-4" width="100%">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title style="width: 100%;align-items: center;" class="d-flex">
                <span style="color: white">Thông tin thời khóa biểu</span>
                <v-spacer></v-spacer>
                <v-btn class="mr-2" v-if="!isUpdating && hasData" dark color="white" @click="update()">
                    <p class="ma-0" style="color: black">Cập nhật</p>
                </v-btn>
                <v-btn class="mr-2" v-if="isUpdating && hasData" color="white" @click="save()">
                    <p class="ma-0" style="color: black">Lưu lại</p>
                </v-btn>
                <v-btn class="mr-2" v-if="isUpdating && hasData" color="white" @click="cancelUpdate()">
                    <p class="ma-0" style="color: black">Hủy</p>
                </v-btn>
                <v-btn color="white" class="float-right" dark @click="openImportFile">
                    <v-icon color="primary">mdi-book-plus</v-icon>
                    <p class="ma-0" style="color: black">Import File</p>
                </v-btn>
            </v-toolbar-title>
        </v-toolbar>
        <v-expand-transition>
            <div v-show="hasData">
                <div class="table-header" style="color: rgba(0, 0, 0, 0.6); font-weight: bold; font-size:17px">
                    <div class="table-cell py-3" style="width: 7%; color: #488fef">Thứ ngày</div>
                    <div class="table-cell py-3" style="width: 7% ; color: #488fef">Buổi</div>
                    <div class="table-cell py-3" style="width: 7% ; color: #488fef">Tiết</div>
                    <div class="table-cell py-3" style="width: 40% ; color: #488fef">Tên môn học</div>
                    <div class="table-cell py-3" style="width: 37% ; color: #488fef">Giáo viên giảng dạy</div>
                </div>
                <div class="table-data" v-if="updated">
                    <div v-for="thu in timeTableData" class="table-row" :key="thu.id">
                        <div class="table-cell" style="width: 7%; color: #488fef; font-weight: bold;  font-size:17px">
                            {{ thu.name }}
                        </div>
                        <div class="table-cell info-table" style="width: 92%">
                            <div class="day">
                                <div class="day-info table-cell" style="width: 7.5%">
                                    Sáng
                                </div>
                                <div class="lession-container" style="width: 91.4%">
                                    <div class="lession table-cell" v-for="tiet in thu.sang" :key="'sang' + tiet.id">
                                        <div class="lession-table table-cell" style="width: 8.3% ; display: flex; justify-content: center; align-items: center;">
                                            <p> Tiết {{ tiet.id }} </p>
                                        </div>
                                        <div class="lession-table table-cell" style="width: 47.8%">
                                            <v-select class="ma-0 pa-0 px-2 select-mon" :items="subjectList" v-model="tiet.subject_id" item-value="subject_id" item-text="subject_name" :disabled="!isUpdating" @input="handleInput(tiet)">
                                                <template v-slot:no-data>
                                                    <p>Chưa có môn học cho lớp này</p>
                                                </template>
                                            </v-select>
                                        </div>
                                        <div class="lession-table table-cell" style="width: 43.9%">
                                            {{ tiet.teacher_name }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="day">
                                <div class="day-info table-cell" style="width: 7.5%">
                                    Chiều
                                </div>
                                <div class="lession-container" style="width: 91.4%">
                                    <div class="lession table-cell" v-for="tiet in thu.chieu" :key="'chieu' + tiet.id">
                                        <div class="lession-table table-cell" style="width: 8.3% ; display: flex; justify-content: center; align-items: center;">
                                            Tiết {{ tiet.id }}</div>
                                        <div class="lession-table table-cell" style="width: 47.8%">
                                            <v-select class="ma-0 pa-0 px-2 select-mon" :items="subjectList" v-model="tiet.subject_id" item-value="subject_id" item-text="subject_name" :disabled="!isUpdating" @input="handleInput(tiet)">
                                            </v-select>
                                        </div>
                                        <div class="lession-table table-cell" style="width: 43.9%">
                                            {{ tiet.teacher_name }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </v-expand-transition>
        <v-card-text v-show="!hasData" style="display: flex">
            <v-spacer></v-spacer>
            <div style="color: rgba(0, 0, 0, 0.6); font-weight: bold; font-size:17px"> Không tìm thấy thời khóa
                biểu</div>
            <v-spacer></v-spacer>
        </v-card-text>
        <v-card-actions v-show="hasData">
            <v-spacer></v-spacer>

            <v-spacer></v-spacer>
        </v-card-actions>
    </v-card>

    <v-dialog v-model="confirmUpdate" max-width="800">
        <v-card>
            <v-card-title style="background-color: rgb(26 118 207 / 1); color: white" class="justify-center py-2">
                Xác nhận cập nhật
            </v-card-title>
            <v-card-title class="justify-center">
                <v-form ref="form-applydate">
                    <v-row>
                        <v-spacer></v-spacer>
                        <v-col cols="8" style="word-wrap: break-word !important;"><p class="mt-5">Chọn ngày áp dụng cho cấu hình vừa cập nhật</p> </v-col>
                        <v-col cols="4">
                            <v-menu min-width="auto" offset-y transition="scale-transition">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field :rules="[value => !!value || 'Chọn ngày áp dụng']" append-icon="mdi-calendar" readonly v-on="on" v-bind="attrs" label="Ngày áp dụng" v-model="submitApplyDate">
                                    </v-text-field>
                                </template>
                                <v-date-picker :show-current="currentDate" locale="vi-VN" v-model="submitApplyDate" :min="startDate" :max="endDate" :allowed-dates="allowedDates">
                                </v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-spacer></v-spacer>
                    </v-row>
                </v-form>
            </v-card-title>
            <v-card-actions class="justify-center pb-5">
                <v-btn class="mr-2" elevation="2" color="warning darken-1" text @click="cancel()">
                    Hủy bỏ
                </v-btn>
                <v-btn class="ml-2" elevation="2" color="primary darken-1" text @click="confirm()">
                    Xác nhận
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>

    <v-dialog v-model="importFile" max-width="900">
        <v-card>
            <v-card-title style="background-color: rgb(26 118 207 / 1); color: white" class="justify-center py-2">
                IMPORT FILE THỜI KHÓA BIỂU
            </v-card-title>
            <v-card-title class="justify-center">
                Thực hiện khai báo thời khóa biểu mới
            </v-card-title>
            <v-card-title class="justify-center">
                <v-container>
                    <v-row>
                        <v-spacer></v-spacer>
                        <v-col sm="12" md="6">
                            <v-text-field v-model="$store.getters['year']" label="Năm học" disabled></v-text-field>
                        </v-col>
                        <v-col sm="12" md="6">
                            <v-select :items="semesterList" label="Học kỳ" v-model="chosenSemester" item-value="id" item-text="name" :rules="[this.rule.required]">
                            </v-select>
                        </v-col>
                        <v-col sm="12" md="6">
                            <v-select :items="gradeList" label="Khối" v-model="chosenGrade" item-value="id" item-text="name" :rules="[this.rule.required]">
                            </v-select>
                        </v-col>
                        <v-col sm="12" md="6">
                            <v-menu min-width="auto" offset-y transition="scale-transition">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field append-icon="mdi-calendar" readonly v-on="on" v-bind="attrs" label="Ngày áp dụng" v-model="chosenDate" :min="startDate" :max="endDate">
                                    </v-text-field>
                                </template>
                                <v-date-picker locale="vi-VN" :min="startDate" :max="endDate" :allowed-dates="allowedDates" v-model="chosenDate"></v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-col sm="12">
                            <v-card-title class="justify-left" style="opacity: 0.6">
                                <v-spacer></v-spacer>

                                <span>Tải file mẫu</span>
                                <v-btn text style="color: rgb(25 118 210)" @click="downloadSampleFile"> [Bấm để tải]
                                </v-btn>
                            </v-card-title>
                            <v-card-action class="justify-center pb-5">
                                <div class="mx-5" style="border: 1px solid black;border-style: dotted;width: 100%;">
                                    <div class="text-center my-3">
                                        <v-btn color="primary" @click="openFile">
                                            <v-icon>mdi-cloud-upload</v-icon>
                                            Tải file
                                        </v-btn>
                                    </div>
                                    <div v-if="importedFile" class="text-center my-3">
                                        <p>Tên file: {{ importedFile.name }}</p>
                                        <p>Dung lượng file: {{ (importedFile.size / 1048576).toFixed(2) }} MB</p>
                                    </div>

                                    <div class="text-center my-3">
                                        Yêu cầu định dạng file mẫu, có dung lượng nhỏ hơn 5 MB
                                    </div>
                                    <div v-if="hasErrors && errorsList.length > 0" class="text-center justify-center d-flex flex-row-reverse" width="70%" style="color: red">
                                        <v-spacer></v-spacer>
                                        <v-virtual-scroll bench="10" :items="errorsList" height="100" min-height="80" width="50%" item-height="50">
                                            <template v-slot:default="{ item }">
                                                <v-list-item :key="item.stt">
                                                    <v-list-item-content>
                                                        <v-list-item-title style="color: red">
                                                            <v-tooltip bottom>
                                                                <template v-slot:activator="{ on, attrs }">
                                                                    <span v-bind="attrs" v-on="on">{{formatTextTooltip(item, 50)}}</span>
                                                                </template>
                                                                <span>{{item}}</span>
                                                            </v-tooltip>
                                                        </v-list-item-title>
                                                    </v-list-item-content>
                                                </v-list-item>
                                                <v-divider></v-divider>
                                            </template>
                                        </v-virtual-scroll>
                                        <v-spacer></v-spacer>
                                    </div>
                                    <v-form ref="form-import">
                                        <v-file-input :rules="rulesFile" v-show="false" ref="inputFile" v-model="importedFile" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
                                        </v-file-input>
                                    </v-form>
                                </div>
                            </v-card-action>
                            <v-card-actions class="justify-center pb-5">
                                <v-btn class="mr-2" elevation="2" color="warning darken-1" text @click="cancelImport">
                                    Hủy bỏ
                                </v-btn>
                                <v-btn :disabled="!importedFile" class="ml-2" elevation="2" color="primary darken-1" text @click="importFileHandle()">
                                    Lưu lại
                                </v-btn>
                            </v-card-actions>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card-title>
        </v-card>
    </v-dialog>
    <Loading ref="Loading"></Loading>
    <ToastMessage ref="ToastMessage"> </ToastMessage>

</div>
</template>

<script>
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";
export default {
    name: "Timetable",
    components: {
        ToastMessage,
        Loading
    },
    data() {
        return {
            currentDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            importFile: false,
            confirmUpdate: false,
            isUpdating: false,
            hasData: false,
            valid: true,
            updated: true,
            rule: {
                required: value => !!value || 'Bắt buộc',
            },
            rulesFile: [
                value => !value || value.size <= 5242880 || 'Dung lượng file phải nhỏ hơn 5 MB',
            ],
            headers: [{
                    name: "Thứ ngày"
                },
                {
                    name: "Buổi"
                },
                {
                    name: "Tiết"
                },
                {
                    name: "Tên môn học"
                },
                {
                    name: "Giáo viên giảng dạy"
                }
            ],
            semesterList: [],
            chosenSemester: -1,
            chosenGrade: "",
            chosenDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            submitApplyDate: "",
            chosenClassroom: "",
            subjectList: [],
            gradeList: [],
            classList: [],
            teachingList: [],
            timeTableData: [],
            thuList: [{
                    id: "t2",
                    name: "Thứ hai"
                },
                {
                    id: "t3",
                    name: "Thứ ba"
                },
                {
                    id: "t4",
                    name: "Thứ tư"
                },
                {
                    id: "t5",
                    name: "Thứ năm"
                },
                {
                    id: "t6",
                    name: "Thứ sáu"
                },
                {
                    id: "t7",
                    name: "Thứ bảy"
                },
            ],
            tietList: ["1", "2", "3", "4", "5"],
            oldApplyDate: "",
            searchInforSubmit: "",
            importedFile: null,
            hasErrors: false,
            errorsList: [],
            startDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            endDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10)
        }
    },
    methods: {
        handleInput(lession) {
            var s = this.subjectList.filter(subject => lession.subject_id == subject.subject_id)[0]
            lession.teacher_id = s.teacher_id
            lession.teacher_name = s.teacher_name
            this.updated = false
            this.$nextTick(() => {
                this.updated = true
            })
        },
        search() {
            this.valid = true
            this.valid = this.$refs.form.validate()
            if (this.valid === false) {
                return
            }
            this.hasData = false
            this.$refs["Loading"].open();
            this.isUpdating = false
            this.timeTableData = []
            for (let thu of this.thuList) {
                let sangList = []
                for (let tiet of this.tietList) {
                    sangList.push({
                        id: tiet,
                        subject: "",
                        teacher: "Trống"
                    })
                }
                let chieuList = []
                for (let tiet of this.tietList) {
                    chieuList.push({
                        id: tiet,
                        subject: "",
                        teacher: "Trống"
                    })
                }
                this.timeTableData.push({
                    id: thu.id,
                    name: thu.name,
                    sang: sangList,
                    chieu: chieuList
                })
            }

            let pro1 = AppService.getTimetableDropdown({
                year: this.$store.getters["year"],
                semester: this.chosenSemester,
                className: this.chosenClassroom
            })
            pro1.then((data) => {
                this.subjectList = []
                for (let i of data.data.data) {

                    this.subjectList.push({
                        subject_id: i.subject.id,
                        subject_name: i.subject.name,
                        teacher_id: i.teacher.id,
                        teacher_name: i.teacher.name
                    })

                }
            })

            let pro2 = AppService.getTimetable({
                year: this.$store.getters["year"],
                semester: this.chosenSemester,
                gradeLevel: this.chosenGrade,
                className: this.chosenClassroom,
                applyDate: this.chosenDate
            })
            pro2.then((res) => {
                if (res.data.status === "OK") {
                    if (res.data.message === "Không tìm thấy thời khóa biểu") {
                        this.$refs["ToastMessage"].open("Lớp học chưa được cấu hình thời khóa biểu", true);
                    }
                    for (let data_case of res.data.data) {
                        let lesson = data_case.lesson
                        let subject_id = data_case.subject.id
                        let subject_name = data_case.subject.name
                        let teacher_name = data_case.teacher.name
                        let teacher_id = data_case.teacher.id
                        let type = data_case.type
                        let week_day = data_case.weekDay
                        this.oldApplyDate = data_case.applyDate
                        for (let thu of this.timeTableData) {
                            if (thu.id === week_day) {
                                if (type === "0") {
                                    for (let tiet of thu.sang) {
                                        if (tiet.id === lesson) {
                                            tiet.subject_id = subject_id
                                            tiet.teacher_name = teacher_name
                                            tiet.teacher_id = teacher_id
                                            tiet.subject_name = subject_name
                                        }
                                    }
                                } else if (type === "1") {
                                    for (let tiet of thu.chieu) {
                                        if (tiet.id === lesson) {
                                            tiet.subject_id = subject_id
                                            tiet.subject_name = subject_name
                                            tiet.teacher_name = teacher_name
                                            tiet.teacher_id = teacher_id
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.hasData = true
                } else {
                    this.hasData = false
                    this.$refs["ToastMessage"].open(res.data.message, true);
                }

            }).catch(() => {
                this.hasData = false;
                this.$refs["ToastMessage"].open("Lấy dữ liệu thời khóa biểu lỗi", true);
                this.$refs["Loading"].close();
            })
            Promise.all([pro1, pro2]).then(() => {
                this.$refs["Loading"].close();
                this.searchInforSubmit = {
                    year: this.$store.getters["year"],
                    semester: this.chosenSemester,
                    gradeLevel: this.chosenGrade,
                    className: this.chosenClassroom,
                    applyDate: this.oldApplyDate
                }
            })

        },
        update() {
            this.isUpdating = true;
        },
        cancelUpdate() {
            this.isUpdating = false;
        },
        cancel() {
            this.$refs["form-applydate"].resetValidation()
            this.confirmUpdate = false
        },
        save() {
            this.confirmUpdate = true
        },
        resetError() {
            this.hasErrors = false
            this.errorsList = []
        },
        confirm() {
            if (!this.$refs["form-applydate"].validate()) {
                return
            }
            this.$refs["Loading"].open();
            this.confirmUpdate = false
            let timeTableSubmit = []
            for (let thu of this.timeTableData) {

                for (let tiet of thu.sang) {
                    timeTableSubmit.push({
                        weekDay: thu.id,
                        lesson: tiet.id,
                        type: 0,
                        subject: tiet.subject_id,
                        teacher: null
                    })
                }

                for (let tiet of thu.chieu) {
                    timeTableSubmit.push({
                        weekDay: thu.id,
                        lesson: tiet.id,
                        type: 1,
                        subject: tiet.subject_id,
                        teacher: null
                    })
                }

            }

            let dataSubmit = {
                year: this.searchInforSubmit.year,
                semester: this.searchInforSubmit.semester,
                gradeLevel: this.searchInforSubmit.gradeLevel,
                className: this.searchInforSubmit.className,
                applyDate: this.submitApplyDate,
                timetable: timeTableSubmit,
            }
            AppService.updateTimeTable(dataSubmit).then(() => {
                this.$refs["ToastMessage"].open("Cập nhật thành công", false)
                this.isUpdating = false;
                this.$refs["Loading"].close();
                this.chosenDate = this.submitApplyDate
            }).catch((error) => {
                this.$refs["ToastMessage"].open(error.response.data.message, true)
                this.$refs["Loading"].close();
            })
        },
        openImportFile() {
            this.importFile = true
            this.hasErrors = false
            this.errorsList = []
        },
        downloadSampleFile() {
            this.hasErrors = false
            this.errorsList = []
            AppService.downloadTimetableTemplateFile(this.$store.getters['year'], this.chosenSemester, this.chosenGrade)
                .then((res) => {
                    var a = window.document.createElement('a');
                    a.href = window.URL.createObjectURL(new Blob([res.data], {
                        type: 'application/octet-stream'
                    }));
                    a.download = "DS_TKB.xlsx"; //fName was the file name portion of the key what was passed in as part of the key value within params. 

                    //  // Append anchor to body.
                    document.body.appendChild(a)
                    a.click();

                    //  // Remove anchor from body
                    document.body.removeChild(a)
                })
                .catch((res) => {
                    this.$refs['ToastMessage'].open("Có lỗi xảy ra", true)
                })
        },
        openFile() {
            this.$refs["inputFile"].$refs["input"].click();
        },
        importFileHandle() {
            if (!this.$refs['form-import'].validate()) {
                this.$refs['ToastMessage'].open('Dung lượng file đã quá 5MB', true)
                return
            }

            if (new Date(this.chosenDate).getDay() !== 1) {
                this.$refs['ToastMessage'].open('Ngày áp dụng phải là thứ 2', true)
                return
            }

            this.$refs["Loading"].open()
            AppService.uploadTimetable(this.importedFile, this.$store.getters["year"], this.chosenSemester, this.chosenGrade, this.chosenDate)
                .then((res) => {
                    if (res.data.status === "OK") {
                        this.$refs['ToastMessage'].open("Cập nhật thành công", false)
                        this.importFile = false
                    } else {
                        this.$refs['ToastMessage'].open(res.data.message, true)
                        this.importFile = false
                    }
                }).catch((res) => {
                    var status = res.response.status
                    if (status == 400) {
                        this.$refs['ToastMessage'].open(res.response.data.message, true)
                    } else if (status == 412) {
                        this.hasErrors = true
                        this.errorsList = res.response.data.data
                    } else {
                        this.$refs['ToastMessage'].open("Có lỗi xảy ra", true)
                    }
                }).finally(() => {
                    this.$refs["Loading"].close()
                })
        },
        cancelImport() {
            this.importedFile = null
            this.importFile = false
            this.hasErrors = false
            this.errorsList = []
        },
        allowedDates: val => {
            var date = new Date(val);
            return date.getDay() == 1
        },
        formatTextTooltip(item, max) {
            if (item === null || item === undefined) {
                return ""
            }
            if (item.length >= max) {
                return item.substring(0, max) + "...";
            }
            return item;
        }
    },
    mounted() {
        this.valid = true
        this.$refs["Loading"].open();
        let pro1 = AppService.getAmountOfSemester(this.$store.getters["year"])
        pro1.then((data) => {
            for (let i of [...Array(data.data.semesterAmount).keys()]) {
                this.semesterList.push({
                    name: "Học kỳ " + (i + 1),
                    id: i + 1
                })
            }

            if (this.semesterList.length > 0) {
                this.chosenSemester = this.semesterList[0].id
            }
            this.$refs["Loading"].close();
        })
        let pro2 = AppService.getAllKhoi()
        pro2.then((data) => {
            this.gradeList = []
            for (let i of data.data.data) {
                this.gradeList.push({
                    id: i.id,
                    name: i.name,
                    code: i.code
                })
            }
            if (this.gradeList.length > 0) {
                this.chosenGrade = this.gradeList[0].id
            }
        })

        Promise.all([pro1, pro2]).then(() => {
            this.timeTableData = []
            for (let thu of this.thuList) {
                let sangList = []
                for (let tiet of this.tietList) {
                    sangList.push({
                        id: tiet,
                        subject_id: "",
                        subject_name: "",
                        teacher_id: "",
                        teacher_name: "Trống"
                    })
                }
                let chieuList = []
                for (let tiet of this.tietList) {
                    chieuList.push({
                        id: tiet,
                        subject_id: "",
                        subject_name: "",
                        teacher_id: "",
                        teacher_name: "Trống"
                    })
                }
                this.timeTableData.push({
                    id: thu.id,
                    name: thu.name,
                    sang: sangList,
                    chieu: chieuList
                })
            }
            this.$refs["Loading"].close();
        })
    },
    watch: {
        importedFile() {
            this.resetError()
        },
        '$store.state.year'() {
            AppService.getAmountOfSemester(this.$store.getters["year"]).then((data) => {
                for (let i of [...Array(data.data.semesterAmount).keys()]) {
                    this.semesterList.push({
                        name: "Học kỳ " + (i + 1),
                        id: i + 1
                    })
                }
            }).then(() => {
                this.chosenSemester = 0
                this.$nextTick(() => {
                    if (this.semesterList.length > 0) {
                        this.chosenSemester = this.semesterList[0].id
                    }
                })

            })

        },
        chosenSemester(newSemester) {
            if (newSemester == 0) {
                return
            }
            this.$refs["Loading"].open();
            this.resetError()
            let pro1 = AppService.getAllKhoi()
            pro1.then((data) => {
                this.gradeList = []
                for (let i of data.data.data) {
                    this.gradeList.push({
                        id: i.id,
                        name: i.name,
                        code: i.code
                    })
                }
                if (this.gradeList.length > 0) {
                    this.chosenGrade = this.gradeList[0].id
                }
            })

            let pro2 = AppService.getSchoolYear({
                "year": this.$store.getters["year"],
                "semester": newSemester
            })

            pro2.then((data) => {

                this.startDate = data.data.data.fromDate.split('T')[0] //new Date(data.data.data.fromDate)
                this.endDate = data.data.data.toDate.split('T')[0]
                if (new Date().getTime() <= new Date(data.data.data.fromDate).getTime()) {
                    this.chosenDate = this.startDate
                    // this.submitApplyDate = this.startDate
                } else if (new Date().getTime() >= new Date(data.data.data.toDate).getTime()) {
                    this.chosenDate = this.endDate
                    // this.submitApplyDate = this.endDate
                } else {
                    this.chosenDate = new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000).toISOString().substr(0, 10)
                    // this.submitApplyDate = new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000).toISOString().substr(0, 10)
                }

            })

            Promise.all([pro1, pro2]).finally(() => {
                this.chosenGrade = 0
                this.$refs["Loading"].close();
                this.$nextTick(() => {
                    this.chosenGrade = this.gradeList[0].id
                })
            })

        },
        chosenDate(newDate) {
            this.resetError()
        },
        chosenGrade(newGrade) {
            if (newGrade == 0) {
                return
            }

            this.resetError()
            this.$refs["Loading"].open();
            AppService.getClassByGradeAndYearsAllDept(newGrade, this.$store.getters["year"]).then((data) => {
                this.classList = []
                for (let i of data.data.data) {
                    this.classList.push({
                        id: i.code,
                        name: i.name
                    })
                }
                this.chosenClassroom = ""
                this.$nextTick(() => {
                    if (this.classList.length > 0) {
                        this.chosenClassroom = this.classList[0].id
                    }
                })

                this.$refs["Loading"].close();
            }, () => {
                this.$refs["Loading"].close();
            })
        },
        chosenClassroom(newClassRoom) {
            if (newClassRoom.trim() === "") {
                this.timeTableData = []
            } else {
                this.$nextTick(() => {
                    this.search()
                })

            }

        }

    }
}
</script>

<style>
.table-cell {
    display: inline-block;
    width: 20%;
    border: thin solid rgba(0, 0, 0, 0.12);
    text-align: center;
}

.table-row {
    display: flex;
}

.info-table {
    width: 80%;
}

.day {
    width: 100%;
    display: flex;
}

.day-info {
    width: 25%;
}

.lession-container {
    width: 75%;
}

.lession {
    width: 100%;
    display: flex;
}

.lession-table {
    width: 33.33%;
}

.thu {
    width: 7%;
}

.subject-select {
    width: 100%;
}

.select-mon .v-text-field__details {
    display: none
}

.import-header {
    width: 100%;
    text-align: center;
}

.error-container {
    width: 100%;
    height: fit-content;
    max-height: 150px;
    overflow: auto;
}
</style>
