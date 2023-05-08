<template>
<div>
    <v-card v-show="isShow" class="md-3">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title>Thông tin tìm kiếm</v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>
        <v-card class="pa-6" elevation="0">
            <v-form ref="form-search">
                <v-row>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field :rules="[(value) => { if(value===null) {return false} else {return !!value || 'Bắt buộc'}}]" v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Học kỳ" readonly v-model="chosenSemesterText">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll;">
                                <v-list-item v-for="semester in semesterAmount" :key="semester" @click="chooseSemester(semester)">
                                    <v-list-item-title>Học kỳ {{ semester }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field :rules="[(value) => { if(value===null) {return false} else {return !!value || 'Bắt buộc'}}]" v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Tháng" readonly v-model="chosenMonthText">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll;">
                                <v-list-item v-for="month in monthList" :key="month.month" @click="chooseMonth(month)">
                                    <v-list-item-title>{{month.month}} - {{month.year}}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field :rules="[(value) => { if(value===null) {return false} else {return !!value || 'Bắt buộc'}}]" width="100%" append-icon="mdi-chevron-down" label="Lớp học" readonly v-on="on" v-bind="attrs" v-model="chosenClass.name">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll;">
                                <v-list-item v-for="item in classList" :key="item.id" @click="chooseClass(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-container>
                            <v-row>
                                <v-menu offset-y transition="scale-transition" min-width="auto">
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-text-field :rules="[(value) => { if(value===null) {return false} else {return !!value || 'Bắt buộc'}}]" width="100%" label="Điểm danh cho ngày" readonly append-icon="mdi-calendar" v-on="on" v-bind="attrs" v-model="dateTimeDisplay">
                                        </v-text-field>
                                    </template>
                                    <v-date-picker locale="vi-VN" v-model="attendanceDate" :allowed-dates="allowedDates" @change="changeDate"></v-date-picker>
                                </v-menu>
                                <v-btn color="primary" @click="changeAttendanceDate" :disabled="disableAttendance">
                                    Điểm danh
                                </v-btn>
                            </v-row>
                        </v-container>
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
    <v-card v-show="isShow" class="mt-4" width="100%">
        <v-form ref="form">
            <v-data-table :header-props="{ sortIcon: null }" :custom-sort="sortByName" :headers="headers" :items="attendance" :items-per-page="50" :hide-default-footer="true" :hide-default-header="true"  class="elevation-1">
                <template slot="no-data">
                  Danh sách học sinh rỗng
                </template>
                
                <template v-slot:top>
                    <v-toolbar flat dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title>Bảng điểm danh: {{ chosenClass.name }}</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                </template>
                <template v-slot:header="{ props: { headers } }">
                    <thead class="v-data-table-header">
                        <tr>
                            <th :style="{ 'min-width': header.width, width: header.width }" v-for="header, index in headers" :key="index" :class="{ 'text-center': true, 'stt-col': header.value === 'stt', 'code-col': header.value === 'code', 'name-col': header.value === 'name' }">
                                <template v-if="!header.attendable">
                                    <div v-if="!header.isNow">
                                        <span >{{ header.text }}</span><br>
                                        <span v-if="header.dayOfWeek">{{ header.dayOfWeek }}</span>
                                    </div>
                                    <div v-if="header.isNow">
                                        <span style="color:green">{{ header.text }}</span><br>
                                        <span style="color:green" v-if="header.dayOfWeek">{{ header.dayOfWeek }}</span>
                                    </div>
                                </template>
                                <template v-else>
                                    <div v-if="!header.enable">
                                        <span>{{ header.text }}</span><br>
                                        <span v-if="header.dayOfWeek">{{ header.dayOfWeek }}</span>
                                    </div>
                                    <template v-else>
                                        <div v-if="!header.isNow">
                                            <span style="color:#1976d2">{{ header.text }}</span><br>
                                            <span style="color:#1976d2" v-if="header.dayOfWeek">{{ header.dayOfWeek }}</span>
                                        </div>
                                        <div v-if="header.isNow">
                                            <span style="color:green">{{ header.text }}</span><br>
                                            <span style="color:green" v-if="header.dayOfWeek">{{ header.dayOfWeek }}</span>
                                        </div>
                                    </template>

                                </template>

                            </th>
                        </tr>
                    </thead>
                </template>
                <template #item="{ item }">
                    <tr>
                        <td class="stt-col text-center">{{ item.stt }}</td>
                        <td class="code-col text-center">{{ item.code }}</td>
                        <td class="name-col text-center">{{ item.name }}</td>
                        <td class="text-center" v-for="a, aIndex in chosenMonth.totalDate" :key="aIndex">
                            <!-- {{headers[aIndex + 3].dayOfWeek}} -->
                            <v-menu offset-y v-if="item.status == 0 && headers[aIndex + 3].attendable" >
                                <template v-slot:activator="{ on, attrs }">
                                    <v-text-field :disabled="!headers[aIndex + 3].enable" :class="headers[aIndex + 3].isNow ? 'text-input-green-attendance':''" width="100%" readonly v-on="on" v-bind="attrs" v-model="item.attendance[aIndex]">
                                    </v-text-field>
                                    
                                </template>
                                <v-list style="max-height: 50vh; overflow-y: scroll;" v-if="headers[aIndex + 3].enable">
                                    <v-list-item v-for="option, i in options" :key="i" @click="item.attendance[aIndex] = option">
                                        <v-list-item-title>{{ option }}</v-list-item-title>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                            <v-text-field v-if="item.status != 0 && headers[aIndex + 3].attendable" disabled v-model="item.attendance[aIndex]">
                            </v-text-field>
                        </td>
                    </tr>
                </template>
            </v-data-table>
            <p class="font-weight-bold" style="margin-top: 20px; margin-left: 20px;">Ghi chú: Nhập: P - Nghỉ có phép, K - Nghỉ không phép, C - Có đi học</p>
            <v-row v-if="attendance.length > 0" align="center" justify="center">
                <v-btn color="primary" @click="save" style="margin-bottom: 20px">
                    Lưu lại
                </v-btn>
            </v-row>
        </v-form>
    </v-card>
    <ConfirmDialog ref="confirmDialog" txtTitle="Lưu điểm danh" question="Xác nhận cập nhật điểm danh"/>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <Loading ref="loading" />
</div>
</template>

<script>
import {
    schoolYearMixin
} from "@/mixin/SchoolYearMixin";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";
export default {
    name: "Attendance",
    mixins: [schoolYearMixin],
    components: {
        Loading,
        ToastMessage,
        ConfirmDialog
    },
    data() {
        return {
            isShow: true,
            options: [
                "P", "K", "C"
            ],
            chosenSemesterText: "",
            chosenSemester: null,
            chosenMonth: {
                month: null
            },
            chosenClass: {
                name: null
            },
            monthList: [],
            classList: [],
            headers: [{
                    text: 'Mã học sinh',
                    value: 'code'
                },
                {
                    text: 'Tên học sinh',
                    value: 'name'
                }
            ],
            attendance: [],
            chosenMonthText: "",
            holidays: [],
            attendanceDate: null, //(new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
            dateTimeDisplay: null, //"",
            semesterAmount: [],
            disableAttendance: false,
            startDate: null,
            toDate: null
        }
    },
    methods: {
        sortByName(items, index, isDescending) {
          items.sort((a, b) => {
            return a.name.split(" ").slice(-1)[0].toLowerCase().localeCompare(b.name.split(" ").slice(-1)[0].toLowerCase());
          });
          let countI = 0
          for(let ii of items) {
            countI += 1
            ii.stt = countI
          }
          return items;
        },
        chooseSemester(semester) {
            this.chosenSemester = semester
            AppService.getSchoolYear({
                "year": this.$store.getters["year"],
                "semester": this.chosenSemester
            }).then((data) => {
                var startDate = data.data.data.fromDate.split('T')[0];
                var toDate = data.data.data.toDate.split('T')[0];
                this.monthList = this.dateRange(startDate, toDate)
                if (this.monthList.length > 0)
                    this.chooseMonth(this.monthList[0])
            })
        },
        chooseMonth(month) {
            this.chosenMonth = month
            this.chosenMonthText = month.month + " - " + month.year
            this.headers = [{
                    text: 'Mã học sinh',
                    value: 'code'
                },
                {
                    text: 'Tên học sinh',
                    value: 'name'
                },
            ]
            this.updateHeader()
            if (this.chosenClass.name) {
                this.search()
            }
        },
        chooseClass(item) {
            this.chosenClass = item

        },
        updateHeader() {
            var t = new Array(parseInt(this.chosenMonth.totalDate) + 3)
            for (var i = 3; i < t.length; ++i) {
                var day = new Date(this.chosenMonth.year, this.chosenMonth.month - 1, i - 2).getDay()
                var dateString = new Date(this.chosenMonth.year, this.chosenMonth.month - 1, i - 1).toISOString().split('T')[0]
                var dayString = day != 0 ? 'T' + (day + 1).toString() : 'CN'
                var attendable = day != 0 && this.holidays.filter(holiday => holiday === dateString.substring(dateString.indexOf('-') + 1)).length == 0 && (i - 2 >= this.chosenMonth.startDate) && (i - 2 <= this.chosenMonth.endDate)
                t[i] = {
                    text: i - 2,
                    dayOfWeek: dayString,
                    attendable: attendable,
                    isNow: new Date().getDate() === i - 2,
                    enable: ((new Date()).getTime() - new Date(this.chosenMonth.year, this.chosenMonth.month - 1, i - 2).getTime()) <= 7 * 24 * 3600 * 1000 && new Date(this.chosenMonth.year, this.chosenMonth.month - 1, i - 2).getTime() < new Date().getTime()
                }
            }
            t[0] = {
                text: 'STT',
                value: 'stt',
                width: '70px'
            }
            t[1] = {
                text: 'Mã học sinh',
                value: 'code',
                width: '100px'
            }
            t[2] = {
                text: 'Tên học sinh',
                value: 'name',
                width: '150px'
            }
            this.headers = t
        },
        changeAttendanceDate() {
            AppService.getSemesterOf({
                date: this.attendanceDate
            }).then((data) => {
                var d = data.data.data
                this.$store.dispatch('updateYear', d.years)
                this.chosenSemester = d.semester
                var startDate = d.fromDate.split('T')[0];
                var toDate = d.toDate.split('T')[0];
                this.monthList = this.dateRange(startDate, toDate)
                var months = this.monthList.filter(m => m.month == parseInt(this.attendanceDate.split('-')[1]))
                if (months.length > 0) {
                    this.chosenMonth = months[0]
                    this.updateHeader()
                    this.search(() => {
                        this.attendance.forEach(a => {
                            var day = this.attendanceDate.split('-')[2]
                            if(a.status == 0)
                                a.attendance[parseInt(day) - 1] = "C"
                        })
                    })
                } else {
                    this.$refs["ToastMessage"].open("Không thể điểm danh", true);
                }
            })
        },
        search(callback = new Function()) {
            if (!this.$refs["form-search"].validate()) return
            this.$refs['loading'].open()
            this.attendance = []
            AppService.getAttendance({
                month: this.chosenMonth.month,
                year: this.$store.getters["year"],
                classCode: this.chosenClass.code
            }).then((data) => {
                var d = data.data.data
                var aValue = {}
                d.forEach((element) => {
                    var day = element.date.split('-')[2]
                    if (!aValue[element.studentCode]) {
                        aValue[element.studentCode] = {}
                    }
                    aValue[element.studentCode][parseInt(day) - 1] = element.checkDate
                })
                AppService.getStudentsInClass({
                    classCode: this.chosenClass.code,
                    year: this.$store.getters["year"],
                }).then(data => {
                    //console.log('Here: ', data.data.data)
                    this.attendance = []
                    data.data.data.forEach((element, index) => {
                        var t = {
                            stt: index + 1,
                            code: element.code,
                            name: element.fullName,
                            status: element.status,
                            attendance: {}
                        }
                        if (aValue[element.code]) {
                            t.attendance = aValue[element.code]
                        }
                        this.attendance.push(t)
                    })
                    callback()
                    this.$refs['loading'].close()
                }).catch(() => {
                    this.$refs['loading'].close()
                })
            }).catch(() => {
                this.$refs['loading'].close()
            })
        },
        update() {
            let pro1 = AppService.getAmountOfSemester(this.$store.getters["year"])
            pro1.then((data) => {
                this.semesterAmount = []
                for (var i = 0; i < data.data.semesterAmount; ++i) {
                    this.semesterAmount.push(i + 1)
                }
                if (this.semesterAmount > 0) {
                    this.chosenSemester = 1
                }

            })
            let pro2 = AppService.getClassManagedBy({
                "teacherCode": this.$store.getters["user"].username,
                "year": this.$store.getters["year"]
            })
            pro2.then((data) => {
                this.chosenClass = {
                    name: null
                }
                this.classList = data.data.data
                if (this.classList.length > 0) {
                    this.chooseClass(this.classList[0])
                } else {
                    return
                }
                this.chosenSemester = null
                this.chooseSemester(1)
            })

            Promise.all([pro1, pro2]).then(() => {})
        },
        save() {
            this.$refs['confirmDialog'].open().then(() => {
                this.$refs['loading'].open()
                var data = {
                    classCode: this.chosenClass.code,
                    year: this.$store.getters["year"],
                    attendances: []
                }

                this.attendance.forEach((element) => {
                    var days = Object.getOwnPropertyNames(element.attendance).filter((e) => e != "__ob__")
                    days.forEach((day) => {
                        data.attendances.push({
                            code: element.code,
                            checkDate: element.attendance[day],
                            date: new Date(this.chosenMonth.year, this.chosenMonth.month - 1, parseInt(day) + 2).toISOString().split('T')[0]
                        })
                    })
                })

                AppService.postUpdateAttendance(data).then((res) => {
                    if (res.data.data) //this response's always true, just because res param must be used
                    {
                        this.$refs["ToastMessage"].open("Cập nhật thành công", false, true);
                        this.$refs['loading'].close()
                    }

                }, (err) => {
                    this.$refs["ToastMessage"].open(err.response.data.message, true);
                    this.$refs['loading'].close()
                }).catch(() => {
                    this.$refs['loading'].close()
                })
            })
        },
        allowedDates: val => {
            var date = new Date(val);
            var dateString = date.toISOString().split('T')[0]
            //return this.holidays.filter(holiday => holiday === dateString.substring(dateString.indexOf('-') + 1)).length == 0 
            return date.getDay() != 0 && (new Date()).getTime() - date.getTime() <= 7 * 24 * 3600 * 1000 && ( date.getYear() === new Date().getYear() && date.getMonth() === new Date().getMonth() && date.getDate() <= new Date().getDate())
        },
        formatDate(date) {
            if (!date) return null

            const [year, month, day] = date.split('-')
            return `${day}/${month}/${year}`
        },
        changeDate() {
            this.dateTimeDisplay = this.formatDate(this.attendanceDate)
        }
    },
    watch: {
        '$store.state.year'() {
            this.update()
        },
        chosenSemester(newValue) {
            if (newValue == null) {
                this.chosenSemesterText = null
            } else {
                this.chosenSemesterText = "Học kỳ " + newValue
            }
        },
        attendanceDate(newValue) {
            var date = new Date(newValue);
            var dateString = date.toISOString().split('T')[0]
            if (this.holidays.filter(holiday => holiday === dateString.substring(dateString.indexOf('-') + 1)).length != 0) {
                this.disableAttendance = true;
                return
            }
            if (this.startDate == null && this.toDate == null) {
                AppService.getSemesterOf({
                    date: this.attendanceDate
                }).then((data) => {
                    this.startDate = data.data.data.fromDate.split('T')[0];
                    this.toDate = data.data.data.toDate.split('T')[0];
                }).catch((res) => {
                    this.startDate = 0
                    this.toDate = 0
                })
            }

            if (this.startDate != this.toDate && (date.getTime() < new Date(this.startDate).getTime() || date.getTime() > new Date(this.toDate).getTime())) {
                this.disableAttendance = true;
                return
            }
            // this.disableAttendance = false;
            this.disableAttendance = !this.allowedDates(newValue)
        }
    },
    mounted() {
        if (!this.$store.getters['user'].roles.includes('ROLE_GVCN')) {
            this.$refs["ToastMessage"].open("Bạn không phải là giáo viên chủ nhiệm", true);
            this.isShow = false
            return
        }
        this.isShow = true
        this.$nextTick(() => {
            this.attendanceDate = (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10)

        })
        let pro1 = AppService.getAmountOfSemester(this.$store.getters["year"])
        pro1.then((data) => {
            this.semesterAmount = []
            for (var i = 0; i < data.data.semesterAmount; ++i) {
                this.semesterAmount.push(i + 1)
            }
            if (this.semesterAmount > 0) {
                this.chosenSemester = 1
            }

        })
        this.dateTimeDisplay = this.formatDate((new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10))
        let pro2 = AppService.getHoliday()
        pro2.then((data) => {
            this.holidays = data.data.data
            this.update()
        })

        Promise.all([pro1, pro2]).then(() => {

        })
    }
}
</script>

<style >

.text-input-green-attendance .v-text-field__slot input {
   color: green !important;
   font-weight: bold  !important;
}

.stt-col {
    position: sticky;
    left: 0;
    top: auto;
    background-color: white;
    z-index: 2;
}

.code-col {
    position: sticky;
    left: 70px;
    top: auto;
    background-color: white;
    z-index: 2;
    text-align: center;
}

.name-col {
    position: sticky;
    left: 170px;
    top: auto;
    background-color: white;
    z-index: 2;
}
</style>
