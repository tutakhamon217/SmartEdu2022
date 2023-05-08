<template>
<div>
    <v-card v-if="isShow" class="px-1">
        <v-row>
            <v-col md="3" sm="6">
                <v-row>
                    <v-col>
                        <v-img style="border-radius: 5px;" :src="rawImg ? rawImg : defaultImg" height="250"></v-img>
                    </v-col>
                </v-row>
            </v-col>
            <v-col md="3" class="mt-2" sm="6">
                <v-row>
                    <v-col>
                        <v-text-field class="font-weight-bold" v-model="teacher.name" readonly></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field label="Mã cán bộ" v-model="teacher.code" readonly></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field label="Trạng thái" v-model="teacher.status" readonly></v-text-field>
                    </v-col>
                </v-row>
            </v-col>
            <v-col md="3" sm="6">
                <v-row>
                    <v-col>
                        <v-text-field flat solo value="" disabled></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field label="Ngày vào trường" v-model="teacher.joinSchoolDay" readonly></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field label="Loại hợp đồng" v-model="teacher.typeContact" readonly></v-text-field>
                    </v-col>
                </v-row>
            </v-col>
            <v-col md="3" sm="6">
                <v-row>
                    <v-col>
                        <v-text-field flat solo value="" disabled></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field label="Số điện thoại" v-model="teacher.phone" readonly></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-text-field label="Email" v-model="teacher.email" readonly></v-text-field>
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
    </v-card>
    <v-row v-if="isShow" class="mt-4">
        <v-col sm="12" md="6">
            <v-select :items="dataSemester" label="Học kỳ" item-text="name" item-value="value" no-data-text="Hiện tại đang không trong năm học" v-model="semester">
            </v-select>
        </v-col>
        <v-col sm="12" md="6">
            <v-menu min-width="auto" offset-y ref="ngayApDungRef" transition="scale-transition" v-model="ngayApDungRef" :close-on-content-click="false">
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-calendar" label="Ngày áp dụng" readonly placeholder="dd/mm/yyyy" v-model="ngayApDungFormatted">
                    </v-text-field>
                </template>
                <v-date-picker style="width: 100%" v-model="ngayApDung" locale="vi-VN" :active-picker.sync="activeNgayApDung" @change="ngayApDungSave" :min="fromDate" :max="toDate" :allowed-dates="allowedDates"></v-date-picker>
            </v-menu>
        </v-col>
    </v-row>
    <v-row v-if="isShow">
        <v-col md="12">
            <div class="d-flex" style="height: 65px">
                <div style="width: 8%; padding: 20px 0px 20px 0px; height:100%;" class="text-center borderTTheader">
                    <span>Buổi</span>
                </div>
                <div style="width: 8%; padding: 20px 0px 20px 0px; height:100%;" class="text-center borderTTheader">
                    <span>Tiết học</span>
                </div>
                <div style="width: 14%; padding: 6px;height:100%;" class="text-center borderTTheader">
                    <p class="ma-0">Thứ 2</p>
                    <p>{{ thu2 }}</p>
                </div>
                <div style="width: 14%; padding: 6px;height:100%;" class="text-center borderTTheader">
                    <p class="ma-0">Thứ 3</p>
                    <p>{{ thu3 }}</p>
                </div>
                <div style="width: 14%; padding: 6px;height:100%;" class="text-center borderTTheader">
                    <p class="ma-0">Thứ 4</p>
                    <p>{{ thu4 }}</p>
                </div>
                <div style="width: 14%; padding: 6px;height:100%;" class="text-center borderTTheader">
                    <p class="ma-0">Thứ 5</p>
                    <p>{{ thu5 }}</p>
                </div>
                <div style="width: 14%; padding: 6px;height:100%;" class="text-center borderTTheader">
                    <p class="ma-0">Thứ 6</p>
                    <p>{{ thu6 }}</p>
                </div>
                <div style="width: 14%; padding: 6px;height:100%;" class="text-center borderTTheader">
                    <p class="ma-0">Thứ 7</p>
                    <p>{{ thu7 }}</p>
                </div>
            </div>
            <div class="d-flex">
                <div style="width: 8%">
                    <div :style="{height: heightTable[0] * 60 + 'px'}" class="text-center border-rv-bottom align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                    <div :style="{height: heightTable[1] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                    <div :style="{height: heightTable[2] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%">SÁNG</p>
                    </div>
                    <div :style="{height: heightTable[3] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                    <div :style="{height: heightTable[4] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>

                    <div :style="{height: heightTable[5] * 60 + 'px'}" class="text-center border-rv-bottom align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                    <div :style="{height: heightTable[6] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                    <div :style="{height: heightTable[7] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%">CHIỀU</p>
                    </div>
                    <div :style="{height: heightTable[8] * 60 + 'px'}" class="text-center border-lr align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                    <div :style="{height: heightTable[9] * 60 + 'px'}" class="text-center border-lr border-rv-top align-center d-flex">
                        <p class="ma-0" style="width: 100%"></p>
                    </div>
                </div>
                <div style="width: 8%">
                    <div :style="{height: heightTable[0] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">1</p>
                    </div>
                    <div :style="{height: heightTable[1] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">2</p>
                    </div>
                    <div :style="{height: heightTable[2] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">3</p>
                    </div>
                    <div :style="{height: heightTable[3] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">4</p>
                    </div>
                    <div :style="{height: heightTable[4] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">5</p>
                    </div>

                    <div :style="{height: heightTable[5] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">1</p>
                    </div>
                    <div :style="{height: heightTable[6] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">2</p>
                    </div>
                    <div :style="{height: heightTable[7] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">3</p>
                    </div>
                    <div :style="{height: heightTable[8] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">4</p>
                    </div>
                    <div :style="{height: heightTable[9] * 60 + 'px'}" class="text-center borderTT align-center d-flex slotClass">
                        <p class="ma-0" style="width: 100%">5</p>
                    </div>
                </div>
                <div v-for="(day, index) in this.dataTable" :key="index + 'a'" style="width: 14%">
                    <div v-for="(ele, id) in day" :key="id + 'b'" :style="{height: heightTable[id] * 60 + 'px'}" class="text-center borderTT align-center d-flex">
                        <div style="width: 100%;">
                            <div v-for="s, index2 in ele.subjects" :key="index2 + 'c'">
                                <v-tooltip bottom >
                                    <template v-slot:activator="{ on, attrs }">
                                        <p v-bind="attrs" v-on="on" class="ma-0" style="font-weight: bold;" :style="{ color: ele.subjects.length > 1 ? 'red' : 'rgb(25, 118, 210);' }">
                                            {{ formatTextTooltip(s.subjectName, 15) }}
                                        </p>
                                    </template>
                                    <span class="ma-0" style="width: 100%;">{{ s.subjectName }}</span>
                                </v-tooltip>
                                <p class="ma-0" style="width: 100%">{{ s.className }}</p>
                                <v-divider class="mx-3" v-if="ele.subjects.length > 1 && (ele.subjects.length - 1) != index2"></v-divider>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </v-col>
    </v-row>
    <ToastMessage ref="toastMessage" />
    <Loading ref="loading" />
</div>
</template>

<script>
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";

export default {
    components: {
        ToastMessage,
        Loading,
    },
    data() {
        return {
            isShow: true,
            rawImg: null,
            defaultImg: "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png",
            teacher: {},
            dataTable: [],
            dataSemester: [],
            date: null,
            currentYear: null,
            currentSemester: null,
            ngayApDung: null,
            activeNgayApDung: null,
            ngayApDungRef: false,
            ngayApDungFormatted: null,
            semester: null,
            fromDate: null,
            toDate: null,
            dataTrangThai: [{
                    id: 0,
                    name: "Đang làm việc",
                },
                {
                    id: 1,
                    name: "Đã nghỉ việc",
                },
                {
                    id: 2,
                    name: "Đã nghỉ hưu",
                },
                {
                    id: 3,
                    name: "Tạm nghỉ",
                },
            ],
            dataLoaiHopDong: [{
                    id: 0,
                    name: "Hợp đồng",
                },
                {
                    id: 1,
                    name: "Biên chế",
                },
            ],
            thu2: '',
            thu3: '',
            thu4: '',
            thu5: '',
            thu6: '',
            thu7: '',
            heightTable: [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
        };
    },
    async mounted() {
        await this.$store.dispatch('getCurrentUser')
            .then(async () => {
                let id = this.$store.getters['user'].id
                if (this.$store.getters['user'].roles.includes('ROLE_GVBM')) {
                    this.isShow = true
                    this.$refs["loading"].open();
                    await this.findByCodeTeacher()
                    let today = new Date();
                    let dd = String(today.getDate()).padStart(2, "0");
                    let mm = String(today.getMonth() + 1).padStart(2, "0"); //January is 0!
                    let yyyy = today.getFullYear();

                    today = yyyy + "-" + mm + "-" + dd;
                    this.ngayApDung = today;
                    await this.getObjCurrentYear();
                    await this.getRangeOfSemester();
                    await this.getAmountOfSemester();
                    await this.getTeachingTimeTableForTeacher();
                    this.$watch('semester', async () => {
                        this.$refs['loading'].open()
                        await this.getRangeOfSemester()
                        this.ngayApDung = this.fromDate
                        this.$refs['loading'].close()
                    })
                    this.$refs["loading"].close();
                } else {
                    this.isShow = false
                    this.$refs["toastMessage"].open("Bạn không phải là giáo viên bộ môn", true);
                    return
                }
            })

    },
    methods: {
        getTeachingTimeTableForTeacher() {

            return AppService.getTeachingTimeTableForTeacher({
                    applyDate: this.ngayApDung,
                    teacherCode: this.$store.getters["user"].username,
                })
                .then((res) => {
                    this.dataTable = []
                    if (res.data.status === "OK") {
                        this.heightTable = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
                        this.dataTable = res.data.data
                        for(let i = 0; i < this.dataTable.length; i++){
                            for(let j = 0; j < 10; j++){
                                if(this.dataTable[i][j].subjects.length > this.heightTable[j]){
                                    this.heightTable[j] = this.dataTable[i][j].subjects.length
                                }
                            }
                        }
                    } else {
                        this.dataTable = []
                        this.$refs["toastMessage"].open(res.data.message, true);
                    }
                })
                .catch((res) => {
                    this.dataTable = []
                    this.$refs["toastMessage"].open(res.message, true);
                });
        },
        formatTextTooltip(item, max) {
            if (item === null || item === '') {
                return null
            }
            if (item.length > max) {
                return item.substring(0, max) + "...";
            }
            return item;
        },
        getAmountOfSemester() {
            return AppService.getAmountOfSemester(this.currentYear)
                .then((result) => {
                    let amountOfSemester = result.data.semesterAmount;
                    this.dataSemester = [];
                    if (amountOfSemester == 1) {
                        this.dataSemester.push({
                            name: "Học kỳ I",
                            value: 1,
                        });
                        this.semester = 1
                    } else if (amountOfSemester == 2) {
                        this.dataSemester.push(
                            ...[{
                                    name: "Học kỳ I",
                                    value: 1,
                                },
                                {
                                    name: "Học kỳ II",
                                    value: 2,
                                },
                            ]
                        );
                        if (this.currentSemester == 1) {
                            this.semester = 1
                        }
                        if (this.currentSemester == 2) {
                            this.semester = 2
                        }
                    } else {
                        this.semester = 0
                    }
                })
                .catch(() => {});
        },
        getObjCurrentYear() {
            return AppService.getObjCurrentYear()
                .then((res) => {
                    if (res.data.status === "OK") {
                        this.currentYear = res.data.data.years;
                        this.currentSemester = res.data.data.semester
                        this.semester = res.data.data.semester
                    } else {
                        this.$refs["toastMessage"].open("Đang không trong năm học", true);
                    }
                })
                .catch((res) => {
                    this.$refs["toastMessage"].open(res.message, true);
                });
        },
        ngayApDungSave(date) {
            this.$refs.ngayApDungRef.save(date);
        },
        formatDate(date) {
            if (!date) return null;
            const [year, month, day] = date.split("-");
            return `${day}/${month}/${year}`;
        },
        getRangeOfSemester() {
            return AppService.getRangeOfSemester({
                    year: this.currentYear,
                    semester: this.semester
                })
                .then((res) => {
                    if (res.data.status !== 'OK') {
                        this.$refs['toastMessage'].open(res.data.message, true)
                    } else {
                        let from_date = new Date(res.data.data[0].from_date).toLocaleString()
                        let to_date = new Date(res.data.data[0].to_date).toLocaleString()
                        from_date = from_date.substring(10, from_date.length)
                        to_date = to_date.substring(10, to_date.length)
                        const [dayFrom, monthFrom, yearFrom] = from_date.split("/");
                        const [dayTo, monthTo, yearTo] = to_date.split("/");
                        this.fromDate = yearFrom + '-' + (monthFrom < 10 ? '0' + monthFrom : monthFrom) + '-' + (dayFrom < 10 ? '0' + dayFrom : dayFrom)
                        this.toDate = yearTo + '-' + (monthTo < 10 ? '0' + monthTo : monthTo) + '-' + (dayTo < 10 ? '0' + dayTo : dayTo)
                    }
                })
                .catch((res) => {
                    this.$refs['toastMessage'].open(res.message, true)
                })
        },
        findByCodeTeacher() {
            return AppService.findByCodeTeacher(this.$store.state.user.username)
                .then((res) => {
                    if (res.data.status === 'OK') {
                        this.teacher = res.data.data[0]
                        this.rawImg = this.teacher.avatar
                        this.teacher.name = this.teacher.fullName
                        this.teacher.joinSchoolDay = new Date(this.teacher.startDate).toLocaleString()
                        this.teacher.joinSchoolDay = this.teacher.joinSchoolDay.substring(10, this.teacher.joinSchoolDay.length)
                        this.teacher.status = this.dataTrangThai.find(x => x.id == this.teacher.status).name
                        this.teacher.typeContact = this.dataLoaiHopDong.find(x => x.id == this.teacher.contractType).name
                    } else {
                        this.$refs['toastMessage'].open(res.data.message, true)
                    }
                })
                .catch(() => {
                    this.$refs['toastMessage'].open('Không tồn tại giáo viên này', true)
                })
        },
        allowedDates: val => {
            var date = new Date(val);
            return date.getDay() == 1
        }
    },
    watch: {
        async ngayApDung() {
            this.$refs['loading'].open()
            this.ngayApDungFormatted = this.formatDate(this.ngayApDung);
            await this.getTeachingTimeTableForTeacher()
            let today = new Date(this.ngayApDung)
            if(today.getDay() != 0){
                this.thu2 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + ( -1 * today.getDay() + 1)).toLocaleDateString()
                this.thu3 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + ( -1 * today.getDay() + 2)).toLocaleDateString()
                this.thu4 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + ( -1 * today.getDay() + 3)).toLocaleDateString()
                this.thu5 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + ( -1 * today.getDay() + 4)).toLocaleDateString()
                this.thu6 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + ( -1 * today.getDay() + 5)).toLocaleDateString()
                this.thu7 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + ( -1 * today.getDay() + 6)).toLocaleDateString()
            }else{
                this.thu2 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + (-6)).toLocaleDateString()
                this.thu3 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + (-5)).toLocaleDateString()
                this.thu4 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + (-4)).toLocaleDateString()
                this.thu5 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + (-3)).toLocaleDateString()
                this.thu6 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + (-2)).toLocaleDateString()
                this.thu7 = new Date(today.getFullYear(), today.getMonth(), today.getDate() + (-1)).toLocaleDateString()
            }
            this.$refs['loading'].close()
        },
        ngayApDungRef(val) {
            val && setTimeout(() => (this.activeNgayApDung = "YEAR"));
        }
    },
};
</script>

<style>
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
