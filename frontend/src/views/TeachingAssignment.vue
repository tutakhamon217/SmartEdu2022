<template>
<div>
    <v-card class="mb-3">
        <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
            <v-card-title class="pa-0">
                <span>Thông tin tìm kiếm</span>
            </v-card-title>
        </v-toolbar>
        <v-card class="pa-6" elevation="0">
            <v-form ref="form-search">
                <v-row>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Khối *" readonly v-model="choosedKhoi" :rules="rules">
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
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Lớp *" readonly v-model="choosedLop" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataLop" :key="index" @click="chooseLop(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-text-field v-model="ten_mon_hoc" label="Môn học" placeholder="Nhập thông tin môn học" :rules="ruleMaxLength250"></v-text-field>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-text-field v-model="ten_giao_vien" label="Giáo viên" placeholder="Nhập thông tin giáo viên" :rules="ruleMaxLength250"></v-text-field>
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
    <v-card class="mb-3">
        <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
            <v-toolbar-title style="width: 100%; align-items: center" class="d-flex">
                <span>Danh sách phân công giảng dạy</span>
                <v-spacer></v-spacer>
                <v-btn style="background-color:white;color:black" @click="chooseFile">
                    <v-icon color="primary">mdi-book-plus</v-icon>
                    <p class="ma-0" style="color: black">Import File</p>
                </v-btn>
            </v-toolbar-title>
        </v-toolbar>

        <v-data-table :headers="headers" :items="desserts" :items-per-page="10" disable-sort no-data-text="Danh sách phân công rỗng" :footer-props="{
              'items-per-page-text': 'Số dòng mỗi trang:',
            }">

            <template v-slot:item.assignmentTeacher="{ item }">
                {{ item.code }} - {{ item.teacherName }}
            </template>
            <template v-slot:item.allYear="{ item }">
                <v-simple-checkbox v-model="item.allYear" color="primary" :ripple="false" disabled></v-simple-checkbox>
            </template>
            <template v-slot:item.semester1="{ item }">
                <v-simple-checkbox v-model="item.semester1" color="primary" :ripple="false" disabled></v-simple-checkbox>
            </template>
            <template v-slot:item.semester2="{ item }">
                <v-simple-checkbox v-model="item.semester2" color="primary" :ripple="false" disabled></v-simple-checkbox>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon small class="mr-2" @click="handleOption(item, 'edit')">
                    mdi-pencil
                </v-icon>
                <v-icon small v-if="item.canDelete" class="mr-2" @click="handleOption(item, 'delete')">
                    mdi-delete
                </v-icon>
            </template>
            <template v-slot:footer.page-text="props">
                {{ props.pageStart }}-{{ props.pageStop }} của
                {{ props.itemsLength }} kết quả
            </template>
        </v-data-table>

    </v-card>

    <TeachingAssignmentDialog ref="teachingAssignmentDialog" :amountOfSemester="amountOfSemester" />
    <ToastMessage ref="toastMessage" />
    <DeleteDialog ref="deleteDialog" />
    <Loading ref="loading" />
    <import-excel-teaching-assignment-dialog ref="importExcelTeachingAssignmentDialog" />
</div>
</template>

<script>
import TeachingAssignmentDialog from "@/views/Dialogs/TeachingAssignmentDialog.vue";
import ToastMessage from "@/components/ToastMessage.vue";
import DeleteDialog from "@/components/DeleteDialog.vue";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ImportExcelTeachingAssignmentDialog from '@/views/Dialogs/ImportExcelTeachingAssignmentDialog.vue';

export default {
    components: {
        TeachingAssignmentDialog,
        ToastMessage,
        DeleteDialog,
        Loading,
        ImportExcelTeachingAssignmentDialog
    },
    data() {
        return {
            choosedKhoi: null,
            choosedLop: null,
            ten_giao_vien: "",
            ten_mon_hoc: "",
            dataKhoi: [],
            dataLop: [],
            headers: [{
                    text: "STT",
                    value: "stt"
                },
                {
                    text: "Khối",
                    value: "grade"
                },
                {
                    text: "Lớp",
                    value: "name"
                },
                {
                    text: "Môn học",
                    value: "subjectName"
                },
                {
                    text: "Giáo viên phân công",
                    value: "assignmentTeacher"
                },
                {
                    text: "Cả năm",
                    value: "allYear"
                },
                {
                    text: "Học kỳ I",
                    value: "semester1"
                },
            ],
            desserts: [],
            rules: [],
            amountOfSemester: null,
            objectSearch: {},
            years: [],
            ruleMaxLength250: [
                v => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự'
            ],
        };
    },
    async mounted() {
        this.$refs['loading'].open()
        await this.getAllYears();
        await this.getObjCurrentYear()
        await this.getAllKhoi();
        await this.getClassByGradeLevel();
        await this.search()
        this.$refs['loading'].close()
    },
    methods: {
        getAllKhoi() {
            return AppService.getAllKhoi().then((result) => {
                this.dataKhoi = result.data.data;
                this.choosedKhoi = this.dataKhoi[0].name;
                let index = this.choosedKhoi.indexOf(" ");
                let resultKhoi = this.choosedKhoi
                    .substring(index, this.choosedKhoi.length)
                    .trim();
                this.objectSearch.grade_level = resultKhoi;
            });
        },
        getClassByGradeLevel() {
            return AppService.getClassByGradeLevel(
                this.objectSearch.grade_level,
                this.$store.getters["year"]
            ).then((result) => {
                this.dataLop = result.data.data.classrooms;
                this.choosedLop = this.dataLop[0].name;
                this.objectSearch.classId = this.dataLop[0].id;
            });
        },
        chooseKhoi(item) {
            this.choosedKhoi = item.name;
            let index = this.choosedKhoi.indexOf(" ");
            let resultKhoi = this.choosedKhoi
                .substring(index, this.choosedKhoi.length)
                .trim();
            this.objectSearch.grade_level = resultKhoi;
            this.getClassByGradeLevel();
        },
        chooseLop(item) {
            this.choosedLop = item.name;
            this.objectSearch.classId = item.id;
        },
        async search() {
            this.$refs['loading'].open()
            this.headers = [{
                        text: "STT",
                        value: "stt"
                    },
                    {
                        text: "Khối",
                        value: "grade"
                    },
                    {
                        text: "Lớp",
                        value: "name"
                    },
                    {
                        text: "Môn học",
                        value: "subjectName"
                    },
                    {
                        text: "Giáo viên phân công",
                        value: "assignmentTeacher"
                    },
                    {
                        text: "Cả năm",
                        value: "allYear"
                    },
                    {
                        text: "Học kỳ I",
                        value: "semester1"
                    },
                ],
                await this.getAmountOfSemester();
            await AppService.getTeachingAssignment(
                    this.ten_giao_vien,
                    this.ten_mon_hoc,
                    this.objectSearch.classId
                )
                .then((result) => {
                    this.desserts = [];
                    let data = result.data.data.teachingAssignment;
                    data.forEach((element, index) => {
                        let obj = {};
                        obj.stt = index + 1;
                        obj.id = element.id;
                        obj.grade = element.grade;
                        obj.teacherName = element.teacherName;
                        obj.subjectName = element.subjectName;
                        obj.code = element.code;
                        obj.name = element.name;
                        obj.teacherId = element.teacherId;
                        obj.subjectId = element.subjectId
                        obj.deptId = element.deptId

                        obj.semester1 = element.semester_1 == 0 ? false : true;
                        obj.semester2 = element.semester_2 == 0 ? false : true;
                        obj.allYear = false;
                        if (this.amountOfSemester == 1) {
                            obj.allYear = obj.semester1 ? true : false;
                        } else if (this.amountOfSemester == 2) {
                            if (obj.semester1 && obj.semester2) {
                                obj.allYear = true;
                            }
                        }
                        obj.canDelete = element.canDelete
                        obj.subjectCode = element.subjectCode
                        obj.classCode = element.classCode
                        this.desserts.push(obj);
                    });
                })
                .catch(() => {
                    this.$refs["toastMessage"].open('Lỗi khi tìm kiếm', true);
                });
            this.$refs['loading'].close()
        },
        handleOption(item, option) {
            if (option == "edit") {
                this.edit(item);
            } else {
                this.delete(item);
            }
        },
        edit(item) {
            this.$refs["teachingAssignmentDialog"]
                .open(item)
                .then((data) => {
                    AppService.updateTeachingAssignment(data)
                        .then((res) => {
                            if (res.data.status === 'OK') {
                                this.$refs["toastMessage"].open(res.data.message, false);
                                let indexUpdated = this.desserts.findIndex(
                                    (x) => x.id == data.teachingAssignmentId
                                );
                                let rowUpdated = this.desserts[indexUpdated];
                                rowUpdated.teacherName = data.teacherName;
                                rowUpdated.code = data.code;
                                rowUpdated.teacherId = data.teacherId;

                                rowUpdated.allYear = data.applyAllSemester == 1 ? true : false;
                                rowUpdated.semester1 = data.semester1;
                                rowUpdated.semester2 = data.semester2;
                            } else {
                                this.$refs["toastMessage"].open(res.data.message, false);
                            }
                        })
                        .catch(() => {
                            this.$refs["toastMessage"].open("Cập nhật thất bại", true);
                        });
                })
                .catch(() => {
                    this.$refs["toastMessage"].open("Cập nhật thất bại", true);
                });
        },
        delete(item) {
            this.$refs["deleteDialog"]
                .open(item)
                .then((item) => {
                    AppService.deleteTeachingAssignment(item.id)
                        .then(() => {
                            let indexDelete = this.desserts.findIndex((x) => x.id == item.id);
                            this.desserts.splice(indexDelete, 1);
                            this.$refs["toastMessage"].open("Xóa thành công", false);
                        })
                        .catch(() => {
                            this.$refs["toastMessage"].open("Xóa thất bại", true);
                        });
                })
                .catch(() => {});
        },
        getAmountOfSemester() {
            return AppService.getAmountOfSemester(this.$store.getters["year"]).then(
                (result) => {
                    this.amountOfSemester = result.data.semesterAmount;
                    if (this.amountOfSemester == 2) {
                        this.headers.push({
                            text: "Học kỳ II",
                            value: "semester2"
                        });
                    }
                    this.headers.push({
                        text: "Thao tác",
                        value: "actions"
                    });
                }
            );
        },
        chooseFile() {
            this.$refs['importExcelTeachingAssignmentDialog'].open()
                .then((mess) => {
                    this.$refs['toastMessage'].open(mess, false)
                    this.search()
                })
                .catch(() => {})
        },
        getAllYears() {
            return AppService.getAllYear().then((result) => {
                this.years = result.data.data;
            });
        },
        getObjCurrentYear() {
            return AppService.getObjCurrentYear()
                .then((res) => {
                    if (res.data.status === 'OK') {
                        this.$store.dispatch("updateCurrentYear", res.data.data.years);
                        this.$store.dispatch("updateYear", res.data.data.years);
                        this.$store.dispatch("updateCurrentSemester", res.data.data.semester)
                    } else {
                        this.$store.dispatch("updateYear", this.years[0]);
                    }
                })
                .catch(() => {
                    this.$store.dispatch("updateYear", this.years[0]);
                })
        }
    },
    watch: {
        async '$store.state.year'() {
            this.$refs['loading'].open()
            try {
                await this.getAllKhoi();
                await this.getClassByGradeLevel();
            } catch (ex) {
                this.$refs["toastMessage"].open(`Năm học ${this.$store.state.year} không có dữ liệu`, true);
                this.desserts = []
                this.headers = []
                this.dataKhoi = []
                this.dataLop = []
                this.ten_giao_vien = ''
                this.ten_mon_hoc = ''
                this.choosedKhoi = null
                this.choosedLop = null
            }
            this.$refs['loading'].close()
        }
    }
};
</script>
