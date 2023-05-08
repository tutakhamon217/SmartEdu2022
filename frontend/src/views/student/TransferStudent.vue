<template>
<div v-if="allow">
    <v-card class="mb-3">
                  <v-toolbar
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1"
          >
            <v-toolbar-title class="text-center"
              >Thông tin tìm kiếm</v-toolbar-title
            >
            <v-spacer></v-spacer>
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
                        <v-autocomplete auto-select-first clearable :items="dataLop" :label="
                dataLop.length == 0
                  ? 'Không có lớp nào'
                  : 'Lựa chọn lớp'
              " item-value="code" item-text="name" :disabled="dataLop.length == 0" v-model="choosedLop" :rules="rules"></v-autocomplete>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-text-field v-model="student" label="Học sinh" placeholder="Nhập thông tin học sinh"></v-text-field>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-select :items="dataTrangThai" label="Trạng thái" v-model="choosedTrangThai" item-value="id" item-text="name"></v-select>
                    </v-col>
                </v-row>
                <v-row align="center" justify="center">
                    <v-btn color="primary" @click="searchTransferStudent">
                        <v-icon>mdi-magnify</v-icon>
                        Tìm kiếm
                    </v-btn>
                </v-row>
            </v-form>
        </v-card>
    </v-card>
    <v-card width="99%" class="mt-6">
        <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
            <v-toolbar-title>Danh sách học sinh</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn @click="exportFile()" color="white" class="float-right" dark>
                <v-icon color="primary" class="mr-1" >mdi-book-arrow-down</v-icon>
                <p class="ma-0" style="color: black">Export</p>
            </v-btn>
        </v-toolbar>
        <v-data-table :items-per-page="-1" v-model="selected" :headers="headers" :items="desserts" disable-sort item-key="studentCode" show-select :footer-props="{
      'items-per-page-text': 'Số dòng mỗi trang:',
    }" class="customHeaderTransfer">
            <template slot="no-data">
                Danh sách học sinh rỗng
            </template>
            <template v-slot:header.stt="{ header }">
                <td rowspan="2" style="width: 100%">
                    <tr>
                        <th></th>
                    </tr>
                    <tr style="width: 100%;">
                        <th>
                            <p class="mb-0 text-center" style="width: 100%;">{{ header.text }}</p>
                        </th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.studentCode="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
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

            <template v-slot:header.studentName="{ header }">
                <td rowspan="2" style="position: relative;">
                    <tr style="position: absolute; left: 100%; top: 10%;">
                        <th>
                            <p style="width: 150px">Năm học ({{ currentYear }})</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th class="text-center" style="width: 180px">{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.className="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th class="text-center" style="width: 100px">{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.competition_title="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th class="text-center" style="width: 170px">{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.status="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th class="text-center" style="width: 170px">{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.gradeUp="{ header }">
                <td rowspan="2">

                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th class="text-center" style="width: 170px">{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.gradeStay="{ header }">
                <td rowspan="2" style="position: relative">
                    <tr style="position: absolute; left: -72%; top: 10%">
                        <th>
                            <p style="width: 150px">Năm học ({{ nextYear }})</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th class="text-center" style="width: 170px">{{ header.text }}</th>
                    </tr>
                </td>
            </template>

            <template v-slot:header.act="{ header }">
                <td rowspan="2">
                    <tr>
                        <th>
                            <p class="mb-0">&nbsp;&nbsp;</p>
                        </th>
                    </tr>
                    <tr>
                        <th style="width: 170px">
                            <p>{{ header.text }}</p>
                        </th>
                    </tr>
                </td>
            </template>

            <template v-slot:item.act="{ item }">
                <td rowspan="1">
                    <tr>
                        <th style="width: 170px">
                            <p>
                                <v-icon v-if="item.status_id === 1 || item.status_id === 0" small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
                            </p>
                        </th>
                    </tr>
                </td>
            </template>

            <template v-slot:footer.page-text="props">
                {{ props.pageStart }}-{{ props.pageStop }} của
                {{ props.itemsLength }} kết quả
            </template>

        </v-data-table>
        <v-card-actions>
            <v-spacer></v-spacer>
              <v-btn :disabled="selected.length === 0" color="primary" @click="transfer">Kết chuyển</v-btn>
            <v-spacer></v-spacer>
        </v-card-actions>
    </v-card>

    <ToastMessage ref="toastMessage" />
    <Loading ref="loading" />
    <TransferStudentDialog ref="transferStudentDialog" :dataKhoi="dataKhoi" :year='currentYear' :nextYear='nextYear' />
    <TransferStudentUpdateDialog ref="TransferStudentUpdateDialog" :dataKhoi="dataKhoi" :year='currentYear' :nextYear='nextYear' />
</div>
</template>

<script>
import ToastMessage from "@/components/ToastMessage";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import TransferStudentDialog from '@/views/Dialogs/TransferStudentDialog.vue'
import TransferStudentUpdateDialog from '@/views/Dialogs/TransferStudentUpdateDialog.vue'
export default {
    components: {
        ToastMessage,
        Loading,
        TransferStudentDialog,
        TransferStudentUpdateDialog
    },
    data() {
        return {
            dataTrangThai: [{
                    id: 3,
                    name: 'Tất cả'
                },
                {
                    id: 1,
                    name: 'Được lên lớp'
                },
                {
                    id: 0,
                    name: 'Lưu ban'
                },
                {
                    id: 2,
                    name: 'Chưa kết chuyển'
                }
            ],
            choosedTrangThai: null,
            dataKhoi: [],
            choosedKhoi: null,
            student: null,
            dataLop: [],
            choosedLop: null,
            objectSearch: {},
            headers: [{
                    text: 'STT',
                    value: 'stt',
                    align: 'center',
                    width: '57px'
                },
                {
                    text: 'Mã học sinh',
                    value: 'studentCode',
                    align: 'center',
                    width: '105px'
                },
                {
                    text: 'Tên học sinh',
                    value: 'studentName',
                    align: 'center',
                    width: '180px'
                },
                {
                    text: 'Lớp',
                    value: 'className',
                    align: 'center',
                    width: '100px'
                },
                {
                    text: 'Danh hiệu thi đua',
                    value: 'competition_title',
                    align: 'center',
                    width: '170px'
                },
                {
                    text: 'Trạng thái',
                    value: 'status',
                    align: 'center',
                    width: '160px'
                },
                {
                    text: 'Được lên lớp',
                    value: 'gradeUp',
                    align: 'center',
                    width: '170px'
                },
                {
                    text: 'Lớp lưu ban',
                    value: 'gradeStay',
                    align: 'center',
                    width: '170px'
                },
                {
                    text: 'Thao tác',
                    value: 'act',
                    align: 'center'
                }
            ],
            desserts: [],
            rules: [],
            selected: [],
            nextYear: null,
            currentYear: null,
            allow: false,
            objOldSearch: {},
            rawData: []
        };
    },
    async mounted() {
        await this.getCurrentYearAndNextYear()
        this.$refs['loading'].open()
        await this.getAllKhoi()
        this.choosedTrangThai = 3
        this.$refs['loading'].close()
    },
    methods: {
        chooseKhoi(item) {
            this.choosedKhoi = item.name;
            this.objectSearch.gradeLevel = item.id;
        },
        getAllKhoi() {
            return AppService.getAllKhoi()
                .then((result) => {
                    this.dataKhoi = result.data.data;
                    this.choosedKhoi = this.dataKhoi[0].name;
                    this.objectSearch.gradeLevel = this.dataKhoi[0].id;
                })
                .catch(() => {
                    this.$refs['toastMessage'].open("Lấy dữ liệu khối thất bại", true)
                })
        },
        getClassByGradeLevel() {
            return AppService.getClassByGradeAndYearsAllDept(this.objectSearch.gradeLevel, this.currentYear)
                .then((res) => {
                    this.dataLop = []
                    for (let i of res.data.data) {
                        this.dataLop.push({
                            id: i.id,
                            name: i.code + " - " + i.name,
                            code: i.code,
                        })
                    }
                    if (this.dataLop.length == 0) {
                        this.choosedLop = ''
                    } else {
                        this.choosedLop = this.dataLop[0].code
                    }
                })
                .catch(res => {
                    this.$refs['toastMessage'].open(res.message, true)
                })
        },
        searchTransferStudent() {
            this.objOldSearch = this.objectSearch
            this.$refs['loading'].open()
            AppService.searchTransferStudent({
                    schoolYear: this.currentYear,
                    gradeLevel: this.objectSearch.gradeLevel,
                    classCode: this.objectSearch.classCode,
                    transferStatus: this.objectSearch.status,
                    studentSearch: this.student
                })
                .then((res) => {
                    this.desserts = []
                    this.rawData = res.data.content
                    this.rawData.forEach((x, index) => {
                        x.stt = index + 1
                        let obj = {}
                        obj.stt = index + 1
                        obj.studentCode = x.details.studentCode
                        obj.studentName = x.details.studentName
                        obj.className = x.className

                        obj.competition_title = x.competitionTitle
                        obj.conduct = x.conduct
                        obj.status_id = x.details.status
                        if (x.details.status === null) {
                            obj.status = 'Chưa kết chuyển'
                            obj.gradeUp = null
                            obj.gradeStay = null
                        } else if (x.details.status === 0) {
                            obj.status = 'Lưu ban'
                            obj.gradeStay = x.details.newClassName
                            obj.gradeUp = null
                        } else {
                            obj.status = 'Được lên lớp'
                            obj.gradeUp = x.details.newClassName
                            obj.gradeStay = null
                        }
                        this.desserts.push(obj)
                    })
                })
                .catch((res) => {
                    this.$refs['toastMessage'].open(res.message, true)
                })
                .finally(() => {
                    this.$refs['loading'].close()
                })
        },
        editItem(item) {
            if (this.nextYear === null || this.nextYear === undefined) {
                this.$refs['toastMessage'].open("Năm học kế tiếp chưa được cấu hình", true)
                return
            }
            let data = {}
            for (let j = 0; j < this.rawData.length; j++) {
                if (item.stt === this.rawData[j].stt) {
                    data = this.rawData[j]
                    break;
                }
            }
            this.$refs['TransferStudentUpdateDialog'].open(data)
                .then((message) => {
                    this.$refs['toastMessage'].open(message, false)
                    this.searchTransferStudent()
                })
                .catch(() => {
                    this.$refs['toastMessage'].open("Cập nhật thất bại", true)
                })
        },
        getCurrentYearAndNextYear() {
            return AppService.getCurrentYearAndNextYear()
                .then((res) => {
                    if (res.data.status === 'OK') {
                        this.currentYear = res.data.data[0]
                        this.nextYear = res.data.data[1]
                        this.allow = true
                    } else {
                        this.$refs['toastMessage'].open('Năm học tiếp theo chưa được cấu hình, vui lòng cấu hình năm học liền kề tới, trước khi thực hiện chức năng "Kết chuyển học sinh"', true)
                        this.allow = false
                    }

                })
        },
        transfer() {
            if (this.nextYear === null || this.nextYear === undefined) {
                this.$refs['toastMessage'].open("Năm học kế tiếp chưa được cấu hình", true)
                return
            }
            let isAccepted = true
            for (let i of this.selected) {
                if (i.competition_title === "" || i.competition_title === null || i.competition_title === undefined) {
                    isAccepted = false
                }
                if (i.status_id === 1 || i.competition_title === 0) {
                    isAccepted = false
                }
            }
            if (!isAccepted) {
                this.$refs['toastMessage'].open("Không cho phép kết chuyển do có học sinh không hợp lệ (chưa được đánh giá xếp loại thi đua hoặc đã được kết chuyển)", true)
                return
            }
            this.$refs['transferStudentDialog'].open()
                .then((res) => {
                    let data = []
                    for (let i = 0; i < this.selected.length; i++) {
                        for (let j = 0; j < this.rawData.length; j++) {
                            if (this.selected[i].stt === this.rawData[j].stt) {
                                this.rawData[j].details.currentClassCode = this.rawData[j].classCode
                                this.rawData[j].details.curentSchoolYear = this.currentYear
                                this.rawData[j].details.status = res.status
                                this.rawData[j].details.newClassCode = res.newClassCode
                                this.rawData[j].details.newGradeLevel = res.newGradeLevel
                                this.rawData[j].details.newSchoolYear = res.newSchoolYear
                                data.push(this.rawData[j])
                                break;
                            }
                        }
                    }
                    AppService.updateListTransferStudent(data)
                        .then((response) => {
                            if (response.data.status === 'OK') {
                                this.searchTransferStudent()
                                this.selected = []
                                this.$refs['toastMessage'].open(response.data.message, false)
                            } else {
                                this.$refs['toastMessage'].open(response.data.message, true)
                            }
                        })
                        .catch((response) => {
                            this.$refs['toastMessage'].open(response.message, true)
                        })
                })
                .catch(() => {})
        },
        formatTextTooltip(item, max) {
            if (item === null || item === undefined) {
                return ""
            }
            if (item.length >= max) {
                return item.substring(0, max) + "...";
            }
            return item;
        },
        exportFile() {
            let classNameSearched = ""
            let classNameFile = ""
            let gradeNameFile = ""

            if (this.rawData === null || this.rawData.length === 0) {
              this.$refs['toastMessage'].open("Không có dữ liệu", true)
              return
            }
            for (let i of this.dataLop) {
                if (i.code === this.objOldSearch.classCode) {
                    classNameSearched = i.name
                    classNameFile = i.name
                }
            }
            if (classNameSearched === "") {
                let khoiNameSearched = ""
                for (let i of this.dataKhoi) {
                    if (i.id === this.objOldSearch.gradeLevel) {
                        khoiNameSearched = i.name
                        gradeNameFile = i.name
                    }
                }
                classNameSearched = khoiNameSearched
            }
            AppService.exportKetChuyenFile(this.rawData, classNameSearched.toUpperCase(), this.currentYear, this.nextYear)
                .then((res) => {
                    var a = window.document.createElement('a');
                    a.href = window.URL.createObjectURL(new Blob([res.data], {
                        type: 'application/octet-stream'
                    }));
                    if (classNameFile === "") {
                        a.download = "DS_KetChuyenHocSinh_khoi_" + gradeNameFile + "_namhoc_" + this.currentYear + ".xlsx";
                    } else {
                        a.download = "DS_KetChuyenHocSinh_lop_" + this.objOldSearch.classCode + "_namhoc_" + this.currentYear + ".xlsx";
                    }
                    //  // Append anchor to body.
                    document.body.appendChild(a)
                    a.click();

                    //  // Remove anchor from body
                    document.body.removeChild(a)
                })
                .catch(() => {
                    this.$refs['toastMessage'].open("Tải file mẫu lỗi", true)
                })
        }
    },
    watch: {
        choosedKhoi() {
            this.getClassByGradeLevel()
        },
        choosedLop() {
            this.objectSearch.classCode = this.choosedLop
        },
        choosedTrangThai(item) {
            this.objectSearch.status = item
        },
    }
};
</script>

<style>
.customHeaderTransfer table th+th:nth-child(2),
.customHeaderTransfer table th+th:nth-child(3),
.customHeaderTransfer table th+th:nth-child(8) {
    border-left: 1px solid #dddddd;
}

.customHeaderTransfer table th:nth-child(3):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
}

.customHeaderTransfer table th:nth-child(4):after,
.customHeaderTransfer table th:nth-child(5):after,
.customHeaderTransfer table th:nth-child(6):after,
.customHeaderTransfer table th:nth-child(7):after,
.customHeaderTransfer table th:nth-child(9):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
}

.customHeaderTransfer .v-input--selection-controls__input {
    margin: 0px !important;
}

.customHeaderTransfer table th:nth-child(4):after,
.customHeaderTransfer table th:nth-child(5):after,
.customHeaderTransfer table th:nth-child(6):after,
.customHeaderTransfer table th:nth-child(7):after,
.customHeaderTransfer table th:nth-child(8):after,
.customHeaderTransfer table th:nth-child(9):after {
    content: "";
    position: absolute;
    height: 50%;
    width: 100%;
    left: 0;
    bottom: 0;
    border-top: 1px solid #dddddd;
}

.customHeaderTransfer table {
    border: 1px solid #dddddd;
}

.customHeaderTransfer table th:nth-child(3),
.customHeaderTransfer table th:nth-child(4),
.customHeaderTransfer table th:nth-child(5),
.customHeaderTransfer table th:nth-child(6),
.customHeaderTransfer table th:nth-child(7),
.customHeaderTransfer table th:nth-child(8),
.customHeaderTransfer table th:nth-child(9) {
    position: relative;
    border-top: 1px solid #dddddd;
}

.customHeaderTransfer table th:nth-child(10) {
    position: relative;
    border-top: 1px solid #dddddd;
    border-right: 1px solid #dddddd;
    border-left: 1px solid #dddddd;
}

.customHeaderTransfer table tbody td {
    border-left: 1px solid #dddddd;
}
</style>
