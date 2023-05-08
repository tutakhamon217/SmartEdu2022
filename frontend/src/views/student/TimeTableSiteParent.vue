<template>
<div>
    <v-card class="md-3">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title class="text-center">Thông tin tìm kiếm</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn v-show="hasData" :loading="exporting" @click="exportFile()" color="white" class="float-right" dark>
                <v-icon color="primary" class="mr-1" >mdi-book-arrow-down</v-icon>
                <p class="ma-0" style="color: black">Export</p>
                <template v-slot:loader>
                    <span class="custom-loader">
                        <v-icon light color="primary">mdi-loading</v-icon>
                    </span>
                </template>
            </v-btn>
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
                            <v-date-picker locale="vi-VN" :min="startDate" :max="endDate" v-model="chosenDate" :allowed-dates="allowedDates"></v-date-picker>
                        </v-menu>
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
        <v-expand-transition>
            <div v-show="hasData">
                <v-col md="12">
                    <div class="d-flex" style="height: 65px">
                        <div style="width: 12.5%; padding: 20px; height:100%;" class="text-center borderTTheader">
                            <span>Buổi</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px; height:100%;" class="text-center borderTTheader">
                            <span>Tiết học</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px;height:100%;" class="text-center borderTTheader">
                            <span>Thứ 2</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px;height:100%;" class="text-center borderTTheader">
                            <span>Thứ 3</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px;height:100%;" class="text-center borderTTheader">
                            <span>Thứ 4</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px;height:100%;" class="text-center borderTTheader">
                            <span>Thứ 5</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px;height:100%;" class="text-center borderTTheader">
                            <span>Thứ 6</span>
                        </div>
                        <div style="width: 12.5%; padding: 20px;height:100%;" class="text-center borderTTheader">
                            <span>Thứ 7</span>
                        </div>
                    </div>
                    <div class="d-flex">
                        <div style="width: 12.5%">
                            <div style="height: 60px;" class="text-center border-rv-bottom align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%">SÁNG</p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>

                            <div style="height: 60px;" class="text-center border-rv-bottom align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%">CHIỀU</p>
                            </div>
                            <div style="height: 60px;" class="text-center border-lr align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                            <div style="height: 60px; " class="text-center border-lr border-rv-top align-center d-flex">
                                <p class="ma-0" style="width: 100%"></p>
                            </div>
                        </div>
                        <div style="width: 12.5%">
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">1</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">2</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">3</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">4</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">5</p>
                            </div>

                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">1</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">2</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">3</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">4</p>
                            </div>
                            <div style="height: 60px;" class="text-center borderTT align-center d-flex slotClass">
                                <p class="ma-0" style="width: 100%">5</p>
                            </div>
                        </div>
                        <div v-for="(day, index) in this.timeTableData" :key="index + 'a'" style="width: 12.5%">
                            <div v-for="(ele, id) in day.data" :key="id + 'b'" style="height: 60px" class="text-center borderTT align-center d-flex">
                                <div style="width: 100%;">
                                    <v-tooltip bottom>
                                        <template v-slot:activator="{ on, attrs }">
                                            <p style="font-weight:bold; color: #1976d2 " v-bind="attrs" v-on="on" class="ma-0">
                                                {{ formatTextTooltip(ele.subjectName, 13) }}
                                            </p>
                                        </template>
                                        <span class="ma-0" style="width: 100%">{{ ele.subjectName }}</span>
                                    </v-tooltip>
                                    <p class="ma-0" style="width: 100%">{{ ele.teacherName }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </v-col>
            </div>
        </v-expand-transition>
        <v-card-text v-show="!hasData" style="display: flex">
            <v-spacer></v-spacer>
            <div style="color: rgba(0, 0, 0, 0.6); font-weight: bold; font-size:17px"> Không tìm thấy thời khóa biểu</div>
            <v-spacer></v-spacer>
        </v-card-text>
    </v-card>

    <Loading ref="Loading"></Loading>
    <ToastMessage ref="ToastMessage"> </ToastMessage>

</div>
</template>

<script>
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";
export default {
    name: "TimetableSiteParent",
    components: {
        ToastMessage,
        Loading
    },
    data() {
        return {
            exporting: false,
            currentDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            hasData: false,
            valid: true,
            rule: {
                required: value => !!value || 'Bắt buộc',
            },
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
            chosenDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            studentClassroom: "",
            studentGrade: 10,
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
            startDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            endDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10)
        }
    },
    methods: {
        formatTextTooltip(item, max) {
            if (item === null || item === undefined) {
                return ""
            }
            if (item.length >= max) {
                return item.substring(0, max) + "...";
            }
            return item;
        },
        async search() {
            this.valid = true
            this.valid = this.$refs.form.validate()
            if (this.valid === false) {
                return
            }
            if((new Date(this.chosenDate)).getDay() !== 1) {
                this.$refs["ToastMessage"].open("Ngày áp dụng phải là thứ 2", true);
                return
            }

            this.hasData = false
            this.$refs["Loading"].open();
            await AppService.getStudentDetailInformationByYear(this.$store.getters["user"].username, this.$store.getters["year"]).then((res) => {
                if (res.data.status === "OK") {
                    this.studentClassroom = res.data.data[0].class_code
                    this.studentGrade = res.data.data[0].grade_level
                } else {
                    this.$refs["ToastMessage"].open("Không tìm thấy thông tin lớp học trong thời gian đã chọn", true);
                    return
                }
            }).catch(() => {
                this.$refs["ToastMessage"].open("Không tìm thấy thông tin lớp học trong thời gian đã chọn", true);
                return;
            })

            this.timeTableData = []
            for (let thu of this.thuList) {
                let thuData = []
                for (let tiet of this.tietList) {
                    thuData.push({
                        type: "0",
                        lesson: tiet,
                        subjectName: "",
                        teacherName: ""
                    })
                }
                for (let tiet of this.tietList) {
                    thuData.push({
                        type: "1",
                        lesson: tiet,
                        subjectName: "",
                        teacherName: ""
                    })
                }
                this.timeTableData.push({
                    name: thu.id,
                    data: thuData
                })
            }

            let pro2 = AppService.getTimetable({
                year: this.$store.getters["year"],
                semester: this.chosenSemester,
                gradeLevel: this.studentGrade,
                className: this.studentClassroom,
                applyDate: this.chosenDate
            })
            pro2.then((res) => {
                if (res.data.status === "OK") {
                    let tmpDataSearch = {}

                    for (let data_case of res.data.data) {
                        let lesson = data_case.lesson
                        let subject_id = data_case.subject.id
                        let subject_name = data_case.subject.name
                        let teacher_name = data_case.teacher.name
                        let teacher_id = data_case.teacher.id
                        let type = data_case.type
                        let week_day = data_case.weekDay
                        this.oldApplyDate = data_case.applyDate
                        tmpDataSearch[week_day + "@1@" + type + "@2@" + lesson] = {
                            subjectName: subject_name,
                            teacherName: teacher_name
                        }
                    }
  
                    for (let thuTemp of this.timeTableData) {
                        for (let lessonTemp of thuTemp.data) {
                            let inforCell = tmpDataSearch[thuTemp.name + "@1@" + lessonTemp.type + "@2@" + lessonTemp.lesson]
                            if (inforCell !== undefined)
                                lessonTemp.subjectName = inforCell.subjectName
                            lessonTemp.teacherName = inforCell.teacherName
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
            Promise.all([pro2]).then(() => {
                this.$refs["Loading"].close();
            })

        },

        allowedDates: val => {
            var date = new Date(val);
            return date.getDay() == 1
        },
        exportFile() {
            this.exporting = true
            AppService.exportTimeTable({
                year: this.$store.getters["year"],
                semester: this.chosenSemester,
                gradeLevel: this.studentGrade,
                className: this.studentClassroom,
                applyDate: this.chosenDate
                })
                .then((res) => {
                    this.exporting = false
                    var a = window.document.createElement('a');
                    a.href = window.URL.createObjectURL(new Blob([res.data], {
                        type: 'application/octet-stream'
                    }));
                    a.download = "THOIKHOABIEU_" + this.studentClassroom +".xlsx";
                    //  // Append anchor to body.
                    document.body.appendChild(a)
                    a.click();

                    //  // Remove anchor from body
                    document.body.removeChild(a)
                })
                .catch(() => {
                    this.exporting = false
                    this.$refs['toastMessage'].open("Tải thời khóa biểu lỗi", true)
                })
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

        Promise.all([pro1]).then(() => {
            this.$refs["Loading"].close();
        })
    },
    watch: {
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

            Promise.all([pro2]).finally(() => {
                this.$refs["Loading"].close();
            })

        },
        allowedDates: val => {
            var date = new Date(val);
            return date.getDay() == 1
        }
    }
}
</script>

<style>

.custom-loader {
  animation: loader 1s infinite;
  display: flex;
}
@-moz-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-webkit-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-o-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
.centerTT {
    margin: auto;
    border: 3px solid rgba(0, 0, 0, 0.12);
    padding: 20px;
}

.slotClass {
    font-weight: bold;
    color: #1976d2;
}

.borderTT {
    border: thin solid rgba(0, 0, 0, 0.12);
}

.borderTTheader {
    border: thin solid rgba(0, 0, 0, 0.22);
    background-color: #1976d2;
    color: white;
    font-size: 1rem;
    font-weight: bold;
    height: 48px;
}

.border-rv-bottom {
    color: #1976d2;
    border-top: thin solid rgba(0, 0, 0, 0.22);
    border-right: thin solid rgba(0, 0, 0, 0.22);
    border-left: thin solid rgba(0, 0, 0, 0.22);
}

.border-rv-top {
    color: #1976d2;
    border-bottom: thin solid rgba(0, 0, 0, 0.22);
    border-right: thin solid rgba(0, 0, 0, 0.22);
    border-left: thin solid rgba(0, 0, 0, 0.22);
}

.border-lr {
    color: #1976d2;
    font-size: 1rem;
    font-weight: bold;
    height: 48px;
    border-right: thin solid rgba(0, 0, 0, 0.22);
    border-left: thin solid rgba(0, 0, 0, 0.22);
}
</style>
