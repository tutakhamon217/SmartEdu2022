<template>
<v-container class="StudentLearningRate ma-0 pa-0" fluid height="100%">
    <v-row class="fill-height">
        <!-- <v-spacer></v-spacer> -->
        <v-col cols="12" class="ma-4">
            <v-row class="mb-3">
                <v-card width="98%">
                    <v-row class="mx-2">
                        <v-col>
                            <v-select :items="hocKyList" label="Học kỳ" v-model="hocKy" item-value="id" item-text="name"></v-select>
                        </v-col>
                        <v-col>
                            <v-select :items="thangList" label="Tháng" v-model="thang" item-value="id" item-text="name"></v-select>
                        </v-col>
                        <v-spacer></v-spacer>
                        <v-col cols="6" class="font-weight-bold" style="text-align: right;">
                            <h3 class="mt-5">Học sinh: {{studentCode}} - {{studentName}}</h3>
                        </v-col>
                    </v-row>
                </v-card>
            </v-row>
            <v-row class="mt-3">
                <v-card width="98%">

                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin điểm danh</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>

                    <v-expand-transition>
                        <v-data-table v-show="timeTable.length > 0" :headers="this.headersTimeTable" :items="this.timeTable" :items-per-page="10" disable-sort hide-default-footer>
                            <template slot="no-data">
                                Thông tin điểm danh rỗng
                            </template>
                            <template v-slot:item.t0="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t0.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t0.checked === 'P'" color="primary">
                                            {{ item.t0.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t0.checked === 'C'" color="green">
                                            {{ item.t0.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t0.checked === 'K'" color="red">
                                            {{ item.t0.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t0.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                            <template v-slot:item.t1="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t1.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t1.checked === 'P'" color="primary">
                                            {{ item.t1.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t1.checked === 'C'" color="green">
                                            {{ item.t1.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t1.checked === 'K'" color="red">
                                            {{ item.t1.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t1.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                            <template v-slot:item.t2="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t2.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t2.checked === 'P'" color="primary">
                                            {{ item.t2.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t2.checked === 'C'" color="green">
                                            {{ item.t2.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t2.checked === 'K'" color="red">
                                            {{ item.t2.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t2.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                            <template v-slot:item.t3="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t3.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t3.checked === 'P'" color="primary">
                                            {{ item.t3.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t3.checked === 'C'" color="green">
                                            {{ item.t3.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t3.checked === 'K'" color="red">
                                            {{ item.t3.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t3.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                            <template v-slot:item.t4="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t4.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t4.checked === 'P'" color="primary">
                                            {{ item.t4.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t4.checked === 'C'" color="green">
                                            {{ item.t4.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t4.checked === 'K'" color="red">
                                            {{ item.t4.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t4.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                            <template v-slot:item.t5="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t5.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t5.checked === 'P'" color="primary">
                                            {{ item.t5.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t5.checked === 'C'" color="green">
                                            {{ item.t5.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t5.checked === 'K'" color="red">
                                            {{ item.t5.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t5.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                            <template v-slot:item.t6="{ item }">
                                <v-col class="ma-2">
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <p>{{item.t6.day}}</p>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                    <v-row>
                                        <v-spacer> </v-spacer>
                                        <v-chip v-if="item.t6.checked === 'P'" color="primary">
                                            {{ item.t6.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t6.checked === 'C'" color="green">
                                            {{ item.t6.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t6.checked === 'K'" color="red">
                                            {{ item.t6.checked }}
                                        </v-chip>
                                        <v-chip v-if="item.t6.checked === ''">
                                        </v-chip>
                                        <v-spacer> </v-spacer>
                                    </v-row>
                                </v-col>
                            </template>
                        </v-data-table>
                    </v-expand-transition>
                </v-card>
            </v-row>
            <v-row class="mt-8">
                <v-spacer></v-spacer>
                <v-card width="50%">
                    <v-expand-transition>
                        <v-data-table :headers="this.chuThichHeader" :items="this.chuThich" :items-per-page="10" disable-sort hide-default-footer>
                            <template slot="no-data">
                                Thống kê rỗng
                            </template>
                        </v-data-table>
                    </v-expand-transition>
                </v-card>
                <v-spacer></v-spacer>
            </v-row>
        </v-col>
        <v-spacer></v-spacer>
    </v-row>
    <SchoolDepartmentDialog ref="SchoolDepartmentDialog"></SchoolDepartmentDialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <Loading ref="Loading"></Loading>
</v-container>
</template>

<script>
import {
    schoolYearMixin
} from "@/mixin/SchoolYearMixin";
import SchoolDepartmentDialog from "@/views/Dialogs/SchoolDepartmentDialog.vue";
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";

export default {
    name: "AttendanceSiteParent",
    components: {
        SchoolDepartmentDialog,
        ToastMessage,
        Loading
    },
    mixins: [schoolYearMixin],
    mounted() {
        this.loadData()
    },
    data: () => ({
        studentCode: "",
        studentName: "",
        hocKyList: [],
        thangList: [],
        hocKy: 1,
        thang: "",
        start: "",
        headersTimeTable: [{
                text: "",
                value: "df",
                sortable: false
            },
            {
                text: "Thứ hai",
                value: "t1",
                sortable: false,
                align: 'center'
            },
            {
                text: "Thứ ba",
                value: "t2",
                sortable: false,
                align: 'center'
            },
            {
                text: "Thứ tư",
                value: "t3",
                sortable: false,
                align: 'center',
            },
            {
                text: "Thứ năm",
                value: "t4",
                sortable: false,
                align: 'center'
            },
            {
                text: "Thứ sáu",
                value: "t5",
                sortable: false,
                align: 'center'
            },
            {
                text: "Thứ bảy",
                value: "t6",
                sortable: false,
                align: 'center'
            },
            {
                text: "Chủ nhật",
                value: "t0",
                sortable: false,
                align: 'center'
            },
        ],
        timeTable: [],
        chuThichHeader: [{
                text: "P - Nghỉ có phép",
                value: "p",
                sortable: false,
                align: 'center'
            },
            {
                text: "K - Nghỉ không phép",
                value: "k",
                sortable: false,
                align: 'center'
            },
            {
                text: "C - Có đi học",
                value: "c",
                sortable: false,
                align: 'center'
            },
            {
                text: "Tổng số",
                value: "tt",
                sortable: false,
                align: 'center'
            }
        ],
        chuThich: []
    }),
    computed: {},
    methods: {
        loadData() {
            this.studentCode = this.$store.getters["user"].username
            this.studentName = this.$store.getters["user"].fullName
            if (this.$store.getters["year"] === null) {
                return
            }
            this.loadHocKyThang()
        },
        async loadHocKyThang() {
            this.$refs["Loading"].open();
            this.hocKy = ""
            await AppService.getAmountOfSemester(this.$store.getters["year"]).then((data) => {
                this.hocKyList = []
                for (let i of [...Array(data.data.semesterAmount).keys()]) {
                    this.hocKyList.push({
                        name: "Học kỳ " + (i + 1),
                        id: i + 1
                    })
                }
                if (this.hocKyList.length > 0) {
                    this.hocKy = this.hocKyList[0].id
                }
            }).catch(() => {})

            await AppService.getSchoolYear({
                "year": this.$store.getters["year"],
                "semester": this.hocKy
            }).then((data) => {
                var startDate = data.data.data.fromDate.split('T')[0];
                var toDate = data.data.data.toDate.split('T')[0];
                this.thangList = []
                for (let i of this.dateRange(startDate, toDate)) {
                    this.thangList.push({
                        id: i.month,
                        name: "Tháng " + i.month,
                        totaldate: i.totalDate,
                        year: i.year
                    })
                }
                this.thang = ""
                if (this.thangList.length > 0) {
                    this.thang = this.thangList[0].id
                }

            })
            this.$refs["Loading"].close();
        },

        search() {
            this.timeTable = []
            let currentYear = this.$store.getters["year"].slice(0, this.$store.getters["year"].indexOf("-")).trim()
            AppService.getAttendanceInformation(this.$store.getters["year"], this.thang, this.studentCode).then((res) => {
                this.timeTable = []
                let attendanceChecked = {}
                let ttC = 0
                let ttK = 0
                let ttP = 0
                for (let i of res.data.data) {
                    let dd = parseInt(i.date.slice(i.date.lastIndexOf("-") + 1, i.date.length))
                    attendanceChecked[dd-1] = i.checkDate
                    if (i.checkDate === 'C') ttC += 1
                    if (i.checkDate === 'K') ttK += 1
                    if (i.checkDate === 'P') ttP += 1
                }
                let totalDate = new Date(currentYear, this.thang - 1, 0).getDate();
                let firstDay = new Date(currentYear, this.thang - 1, 1).getDay()
                let tmpWeek = {
                    "t0": {
                        day: "",
                        checked: ""
                    },
                    "t1": {
                        day: "",
                        checked: ""
                    },
                    "t2": {
                        day: "",
                        checked: ""
                    },
                    "t3": {
                        day: "",
                        checked: ""
                    },
                    "t4": {
                        day: "",
                        checked: ""
                    },
                    "t5": {
                        day: "",
                        checked: ""
                    },
                    "t6": {
                        day: "",
                        checked: ""
                    },
                }
                for (let i = 0; i < totalDate; i++) {
                    let checked = ""
                    if (attendanceChecked[i] !== undefined) {
                        checked = attendanceChecked[i]
                    }
                    if ((i + firstDay) % 7 === 0) {
                        tmpWeek["t0"].day = (i + 1) +"/"+this.thang
                        tmpWeek["t0"]["checked"] = checked
                        this.timeTable.push(tmpWeek)
                        tmpWeek = {
                            "t0": {
                                day: "",
                                checked: ""
                            },
                            "t1": {
                                day: "",
                                checked: ""
                            },
                            "t2": {
                                day: "",
                                checked: ""
                            },
                            "t3": {
                                day: "",
                                checked: ""
                            },
                            "t4": {
                                day: "",
                                checked: ""
                            },
                            "t5": {
                                day: "",
                                checked: ""
                            },
                            "t6": {
                                day: "",
                                checked: ""
                            },
                        }
                        continue
                    }

                    tmpWeek["t" + (i + firstDay) % 7]["checked"] = checked
                    tmpWeek["t" + (i + firstDay) % 7].day = (i + 1) +"/"+this.thang
                    if (i === totalDate - 1) {
                        this.timeTable.push(tmpWeek)
                    }
                }
                this.chuThich = [{
                    'p': ttP,
                    'k': ttK,
                    'c': ttC,
                    'tt': totalDate
                }]
            })

        }
    },
    watch: {
        '$store.state.year'() {
            this.loadHocKyThang()
        },
        '$store.state.user'() {
            this.studentCode = this.$store.getters["user"].username
            this.studentName = this.$store.getters["user"].fullName
        },
        hocKy() {
            AppService.getSchoolYear({
                "year": this.$store.getters["year"],
                "semester": this.hocKy
            }).then((data) => {
                var startDate = data.data.data.fromDate.split('T')[0];
                var toDate = data.data.data.toDate.split('T')[0];
                this.thangList = []
                for (let i of this.dateRange(startDate, toDate)) {
                    this.thangList.push({
                        id: i.month,
                        name: "Tháng " + i.month,
                        totaldate: i.totalDate,
                        year: i.year
                    })
                }
                this.thang = ""
                if (this.thangList.length > 0) {
                    this.thang = this.thangList[0].id
                }

            })
        },
        thang() {
            this.search()
        }

    }
};
</script>

<style scoped lang="scss">
</style>
