<template>
<div>
    <v-card class="mb-3">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title class="pa-0">
                <span>Thông tin tìm kiếm</span>
            </v-toolbar-title>
        </v-toolbar>
        <v-card class="pa-6" elevation="0">
            <v-form ref="form-search">
                <v-row>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Lựa chọn khối (*)" readonly v-model="choosedKhoi" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataKhoi" :key="index" @click="chooseKhoi(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Lựa chọn khoa/ban (*)" readonly v-model="choosedKhoaBan" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataKhoaBan" :key="index" @click="chooseKhoaBan(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Học kỳ" readonly v-model="choosedHocKy" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll" v-if="amountOfSemester">
                                <v-list-item @click="chooseHocKy(0)">
                                    <v-list-item-title>{{ formatKyHoc(0) }}</v-list-item-title>
                                </v-list-item>
                                <v-list-item v-for="(item, index) in amountOfSemester" :key="index" @click="chooseHocKy(item)">
                                    <v-list-item-title>{{
                      formatKyHoc(item)
                    }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" :label="
                      dataLop.length == 0
                        ? 'Không có lớp nào'
                        : 'Lựa chọn lớp (*)'
                    " :disabled="dataLop.length == 0" readonly v-model="choosedLop" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll" v-if="dataLop.length != 0">
                                <v-list-item v-for="(item, index) in dataLop" :key="index" @click="chooseLop(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
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
    <v-card class="mb-3 mt-3">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title style="width: 100%; align-items: center" class="d-flex">
                Kết quả thi đua
                <v-spacer></v-spacer>
            </v-toolbar-title>
        </v-toolbar>
        <v-data-table disable-sort :items="desserts" :headers="headers" :items-per-page="10" show-expand item-key="stt" :expanded.sync="expanded" :single-expand="false" :footer-props="{
          'items-per-page-text': 'Số dòng mỗi trang:',
        }" class="customHeaderReportCompetition">
            <template slot="no-data">
              Danh sách kết quả thi đua rỗng
            </template>
            <template v-slot:expanded-item="{ item }">
                <td></td>
                <td>
                    <p class="text-center" v-for="rs in item.subResult" :key="rs.stt + 'j'">{{ rs.stt }}</p>
                </td>
                <td>
                    <p v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'a'">{{ rs.className }}</p>
                </td>
                <td>
                    <p v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'b'">{{ rs.teacherName }}</p>
                </td>
                <td>
                    <p class="text-center" v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'c'">{{ rs.excellent + rs.good + rs.medium + rs.weak }}</p>
                </td>
                <td>
                    <p class="text-center" v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'd'">{{ rs.excellent }}</p>
                </td>
                <td>
                    <p class="text-center" v-for="rs in item.subResult" :key="rs.className+ rs.classCode + 'e'">{{ rs.good }}</p>
                </td>
                <td>
                    <p class="text-center" v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'f'">{{ rs.medium }}</p>
                </td>
                <td>
                    <p class="text-center" v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'g'">{{ rs.weak }}</p>
                </td>
                <td>
                    <p class="text-center" style="margin: 0px" v-for="rs in item.subResult" :key="rs.className + rs.classCode + 'h'" @click="showDetail(rs)">
                        <v-btn icon>
                            <v-icon>mdi-eye</v-icon>
                        </v-btn>
                    </p>
                </td>
            </template>

            <!-- config header -->
            <template v-slot:header.excellent="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.good="{ header }">
                <td rowspan="2" style="position: relative">
                    <tr style="position: absolute; left: -150%; top: -10%">
                        <th>
                            <p style="width: 300px">Số lượng học sinh đạt danh hiệu</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.medium="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.weak="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:footer.page-text="props">
                {{ props.pageStart }}-{{ props.pageStop }} của
                {{ props.itemsLength }} kết quả
            </template>
        </v-data-table>
    </v-card>
    <Loading ref="loading" />
    <ToastMessage class="bottom-toast-mess" ref="toastMessage" />
</div>
</template>

<script>
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage";

export default {
    name: "ReportTheResultsOfWholeSchoolCompetition",
    components: {
        ToastMessage,
        Loading,
    },
    data() {
        return {
            desserts: [],
            headers: [{
                    text: "STT",
                    value: "stt",
                    width: '55px',
                    align: 'center'
                },
                {
                    text: "Lớp",
                    value: "className"
                },
                {
                    text: "Giáo viên chủ nhiệm",
                    value: "teacherName"
                },
                {
                    text: "Tổng",
                    value: "total",
                    align: "center",
                    width: "60px"
                },
                {
                    text: "Giỏi",
                    value: "excellent",
                    align: "center",
                    width: "60px",
                },
                {
                    text: "Tiên tiến",
                    value: "good",
                    align: "center",
                    width: "82px"
                },
                {
                    text: "Trung bình",
                    value: "medium",
                    align: "center",
                    width: "95px",
                },
                {
                    text: "Yếu",
                    value: "weak",
                    align: "center",
                    width: "55px"
                },
                {
                    text: "Xem chi tiết",
                    value: "act",
                    align: "center",
                    width: "100px"
                },
            ],
            result: [],
            dataKhoi: [],
            choosedKhoi: null,
            dataKhoaBan: [],
            choosedKhoaBan: null,
            objectSearch: {},
            amountOfSemester: 0,
            dataLop: [],
            choosedLop: null,
            rules: [],
            choosedHocKy: null,
            expanded: [],
            prevRoute: null,
            watchYear: 1
        };
    },
    beforeRouteEnter(to, from, next) {
        next(vm => {
            vm.prevRoute = from.fullPath
        })
    },
    async mounted() {
        // if (this.prevRoute === '/') {
        //     this.watchYear = this.$watch('$store.state.year', async () => {
        //         console.log('watch tren')
        //         this.$refs['loading'].open()
        //         await this.getAllKhoi();
        //         await this.getAllKhoaBan();
        //         await this.getAmountOfSemester();
        //         await this.loadWhenChooseOption()
        //         await this.search()
        //         this.watchYear()
        //         this.watchYear = null
        //         this.$watch('choosedKhoi', async () => {
        //             this.$refs["loading"].open();
        //             await this.loadWhenChooseOption();
        //             this.$refs['loading'].close()
        //         })
        //         this.$watch('choosedKhoaBan', async () => {
        //             this.$refs["loading"].open();
        //             await this.loadWhenChooseOption();
        //             this.$refs['loading'].close()
        //         })
        //         this.$refs['loading'].close()
        //     })
        // } else {
            this.$refs['loading'].open()
            await this.getAllKhoi();
            await this.getAllKhoaBan();
            await this.getAmountOfSemester();
            await this.loadWhenChooseOption()
            await this.search()
            this.watchYear = null
            this.$watch('choosedKhoi', async () => {
                this.$refs["loading"].open();
                await this.loadWhenChooseOption();
                this.$refs['loading'].close()
            })
            this.$watch('choosedKhoaBan', async () => {
                this.$refs["loading"].open();
                await this.loadWhenChooseOption();
                this.$refs['loading'].close()
            })
            this.$refs['loading'].close()
        // }
    },
    methods: {
        getReportTheResultOfCompetition() {
            return AppService.getReportTheResultOfCompetition();
        },
        getAmountOfSemester() {
            return AppService.getAmountOfSemester(this.$store.getters["year"])
                .then((result) => {
                    this.amountOfSemester = result.data.semesterAmount;
                    this.chooseHocKy(0)
                })
                .catch(() => {});
        },
        getAllKhoi() {
            return AppService.getAllKhoi().then((result) => {
                this.dataKhoi = []
                this.dataKhoi.push({
                    id: null,
                    name: "-- Lựa chọn --",
                });
                this.dataKhoi.push(...result.data.data);
                this.choosedKhoi = this.dataKhoi[0].name;
                this.objectSearch.gradeLevel = this.dataKhoi[0].id;
            });
        },
        getAllKhoaBan() {
            return AppService.getAllDepartmentForClass().then((data) => {
                this.dataKhoaBan = []
                this.dataKhoaBan.push({
                    id: null,
                    name: "-- Lựa chọn --",
                });
                this.dataKhoaBan.push(...data.data.data);
                this.choosedKhoaBan = this.dataKhoaBan[0].name;
                this.objectSearch.deptId = this.dataKhoaBan[0].id;
            });

        },
        loadWhenChooseOption() {
            this.dataLop = []
            return AppService.getClassByKhoiAndKhoaBan(
                this.objectSearch.gradeLevel,
                this.objectSearch.deptId,
                this.$store.getters["year"]
            ).then((data) => {
                this.dataLop.push({
                    id: null,
                    name: '-- Lựa chọn --',
                    code: null
                })
                this.dataLop.push(...data.data.data.subjects);
                if (this.dataLop.length != 0) {
                    this.choosedLop = this.dataLop[0].name;
                    this.objectSearch.classCode = this.dataLop[0].code;
                } else {
                    this.choosedLop = null;
                    this.objectSearch.classCode = null;
                    this.$refs["form-search"].resetValidation();
                }
            })
        },
        chooseKhoaBan(item) {
            this.objectSearch.deptId = item.id;
            this.choosedKhoaBan = item.name;
        },
        chooseKhoi(item) {
            this.choosedKhoi = item.name;
            this.objectSearch.gradeLevel = item.id
        },
        chooseLop(item) {
            this.choosedLop = item.name;
            this.objectSearch.classCode = item.code;
        },
        chooseHocKy(item) {
            this.objectSearch.hocKy = item;
            switch (item) {
                case 0: {
                    this.choosedHocKy = "Cả năm";
                    break;
                }
                case 1: {
                    this.choosedHocKy = "Học kỳ I";
                    break;
                }
                case 2: {
                    this.choosedHocKy = "Học kỳ II";
                    break;
                }
                case 3: {
                    this.choosedHocKy = "Học kỳ III";
                    break;
                }
                case 4: {
                    this.choosedHocKy = "Học kỳ IV";
                    break;
                }
            }
        },
        formatKyHoc(item) {
            switch (item) {
                case 0: {
                    return "Cả năm";
                }
                case 1: {
                    return "Học kỳ I";
                }
                case 2: {
                    return "Học kỳ II";
                }
                case 3: {
                    return "Học kỳ III";
                }
                case 4: {
                    return "Học kỳ IV";
                }
            }
            return null;
        },
        search() {
            this.$refs["loading"].open();
            AppService.getReportTheResultOfCompetition({
                    year: this.$store.getters['year'],
                    deptId: this.objectSearch.deptId,
                    gradeLevel: this.objectSearch.gradeLevel,
                    classCode: this.objectSearch.classCode,
                    semester: this.objectSearch.hocKy
                })
                .then((res) => {
                    this.desserts = res.data.data
                    this.expanded = res.data.data
                })
                .catch(() => {
                    this.$refs['toastMessage'].open('Lỗi tìm kiếm', true)
                })
                .finally(() => {
                    this.$refs["loading"].close();
                })
        },
        showDetail(item) {
            this.$router.push({
                name: 'tung-lop',
                params: {
                    gradeLevel: item.gradeLevel,
                    classCode: item.classCode,
                    semester: item.semester,
                    deptId: item.deptId
                }
            })
        }
    },
    watch: {
        watchYear() {
            if (this.watchYear === null) {
                this.$watch('$store.state.year', async () => {
                    this.desserts = [];
                    this.$refs["loading"].open();
                    // await this.getAllKhoi();
                    let check = false
                    if (this.choosedKhoaBan == '-- Lựa chọn --') {
                        check = true
                    }
                    await this.getAllKhoaBan();
                    await this.getAmountOfSemester();
                    if (check) {
                        await this.loadWhenChooseOption()
                    }
                    await this.search()
                    this.$refs["loading"].close();
                })
            }
        }
    },
};
</script>

<style>
/* .customHeaderReportCompetition tbody tr:nth-child(even) {
  background: #eee;
} */
.customHeaderReportCompetition p {
    margin-top: 15px;
}

.customHeaderReportCompetition table th+th {
    border-left: 1px solid #dddddd;
}

.customHeaderReportCompetition table td+td {
    border-left: 1px solid #dddddd;
}

.customHeaderReportCompetition table th:nth-child(6):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
}

.customHeaderReportCompetition table th:nth-child(7):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
}

.customHeaderReportCompetition table th:nth-child(8):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
}

.customHeaderReportCompetition table th:nth-child(9):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
}

.customHeaderReportCompetition table th:nth-child(6) {
    position: relative;
}

.customHeaderReportCompetition table th:nth-child(7) {
    position: relative;
    border-left: none;
}

.customHeaderReportCompetition table th:nth-child(8) {
    position: relative;
    border-left: none;
}

.customHeaderReportCompetition table th:nth-child(9) {
    position: relative;
    border-left: none;
}

.customHeaderReportCompetition .v-data-table-header th td {
    text-align: center;
}
</style>
