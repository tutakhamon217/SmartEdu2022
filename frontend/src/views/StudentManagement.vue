<template>
<v-container class="StudentManagement ma-0 pa-0" fluid height="100%">
    <v-row class="fill-height" width="90%">
        <!-- <v-spacer></v-spacer> -->
        <v-col cols="12" class="ma-4">
            <v-row class="mb-3">
                <v-card width="98%">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin tìm kiếm</v-toolbar-title>

                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="mx-2">
                        <v-col>
                            <v-select :items="khoiList" label="Khối" v-model="maKhoi" item-value="id" item-text="name"></v-select>
                        </v-col>
                        <v-col>
                            <v-select :items="trangThaiList" label="Trạng thái" v-model="trangThai" item-value="id" item-text="name"></v-select>
                        </v-col>
                    </v-row>
                    <v-row class="mx-2 mt-0">
                        <v-col>
                            <v-select :items="lopHocList" label="Lớp học" v-model="lopHoc" item-value="code" item-text="name"></v-select>
                        </v-col>
                        <v-col>
                            <v-text-field v-model="thongTinHocSinhSearch" label="Học sinh"></v-text-field>
                        </v-col>
                    </v-row>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn @click="searchStudent(false)" dark color="primary">
                            <v-icon>mdi-magnify</v-icon>
                            Tìm kiếm
                        </v-btn>
                        <v-spacer></v-spacer>
                    </v-card-actions>
                </v-card>
            </v-row>

            <v-row>
                <v-card width="98%" class="mt-3">
                    <v-data-table 
                    :page.sync="pageCurrent"
                    :header-props="{ sortIcon: null }" 
                    :headers="this.headersClass" 
                    :items="this.searchResult" 
                    :items-per-page="10"
                    hide-default-footer
                    >
                        <template slot="no-data">
                            Danh sách học sinh rỗng
                        </template>
                    
                        <template v-slot:top>
                            <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                                <v-toolbar-title class="text-center">Kết quả tìm kiếm: {{totalItems}} học sinh</v-toolbar-title>

                                <v-spacer></v-spacer>
                                <div v-if="canAddUpdate">
                                    <v-btn @click="addNewStudent()" color="white" class="float-right ml-2" dark>
                                        <v-icon color="green">mdi-plus-outline</v-icon>
                                        <p class="ma-0" style="color: black">Thêm mới</p>
                                    </v-btn>
                                    <v-btn @click="importExcelFile()" color="white" class="float-right" dark>
                                        <v-icon color="primary" >mdi-book-plus</v-icon>
                                        <p class="ma-0" style="color: black">Import File</p>
                                    </v-btn>
                                </div>
                            </v-toolbar>
                        </template>

                        <template v-slot:item.trangThai="{ item }">
                        <div style="color:red !important;" v-if="item.trangThaiId !==0">{{item.trangThai}}</div>
                        <div style="color:green !important;" v-if="item.trangThaiId ===0">{{item.trangThai}}</div>
                        </template>

                        <template v-slot:item.hoSo="{ item }">
                            <v-icon small class="mr-2" @click="viewDetail(item)"> mdi-eye </v-icon>
                        </template>
                        <template v-slot:item.actions="{ item }" v-if="canAddUpdate">
                            <v-icon small class="mr-2" @click="editStudent(item)"> mdi-pencil </v-icon>
                        </template>
                        <template v-slot:footer.page-text="props">
                            {{ props.pageStart }}-{{ props.pageStop }} của {{ props.itemsLength }} kết quả
                        </template>
                    </v-data-table>
                    <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-pagination
                        v-model="pageCurrent"
                        :length="pageCount"
                        :total-visible="8"
                        ></v-pagination>
                    </v-card-actions>
                </v-card>
            </v-row>
        </v-col>
        <v-spacer></v-spacer>
    </v-row>
    <SchoolDepartmentDialog ref="SchoolDepartmentDialog"></SchoolDepartmentDialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <ImportStudentDialog ref="ImportStudentDialog"> </ImportStudentDialog>
    <Loading ref="Loading"></Loading>
</v-container>
</template>

<script>
// import DataTable from "@/components/DataTable.vue";
import SchoolDepartmentDialog from "@/views/Dialogs/SchoolDepartmentDialog.vue";
import ImportStudentDialog from "@/views/Dialogs/ImportStudentDialog.vue"
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";

export default {
    name: "StudentManagement",
    components: {
        SchoolDepartmentDialog,
        ToastMessage,
        Loading,
        ImportStudentDialog
    },
    async mounted() {
        this.$refs["Loading"].open();
        await this.$store.dispatch('getCurrentUser')
            .then(async () => {
                let id = this.$store.getters['user'].id
                if (this.$store.getters['user'].roles.includes('ROLE_ADMIN') || this.$store.getters['user'].roles.includes('ROLE_HT') ||
                    this.$store.getters['user'].roles.includes('ROLE_GVCN')
                ) {
                    this.canAddUpdate = true
                } else {
                    this.canAddUpdate = false
                }
            })
        await AppService.getAllGradeLevel().then((data) => {
            this.khoiList = [{
                id: "",
                name: "Tất cả"
            }]
            for (let gradeLevel of data.data.data) {
                this.khoiList.push({
                    id: gradeLevel.id,
                    name: gradeLevel.name
                })

            }
            this.maKhoi = this.khoiList[0].id
            this.$refs["Loading"].close()
            this.searchStudent(false)
        })

        await AppService.getClassByGradeAndYearsAllDept(0, this.$store.getters["year"]).then((data) => {
                this.lopHocList = []
                this.lopHocList.push({
                    id: "",
                    name: "Tất cả",
                    code: ""
                })
                for (let lop of data.data.data) {
                    this.lopHocList.push({
                        id: lop.id,
                        name: lop.name,
                        code: lop.code
                    })
                }
                this.lopHocList.sort(function(a, b) {
                    if(a.id === "") {return -1 }
                    if(b.id === "") {return 1 }
                    else {return a.name.localeCompare(b.name)}
                })
            })

    },
    data: () => ({
        pageCurrent: 1,
        pageCount: 1,
        totalItems: 10,
        isDelete: false,
        isEdit: true,
        maKhoi: "",
        trangThai: "",
        lopHoc: "",
        thongTinHocSinhSearch: "",
        headersClass: [{
                text: "STT",
                align: "start",
                sortable: false,
                value: "stt",
            },
            {
                text: "Mã học sinh",
                value: "maHocSinh",
                sortable: true,
            },
            {
                text: "Tên học sinh",
                value: "tenHocSinh",
                sortable: true,
            },
            {
                text: "Giới tính",
                value: "gioiTinh"
            },
            {
                text: "Ngày Sinh",
                value: "ngaySinh"
            },
            {
                text: "Tên lớp",
                value: "tenLop"
            },
            {
                text: "Khoa/Ban",
                value: "khoaBan"
            },
            {
                text: "Trạng thái",
                value: "trangThai"
            },
            {
                text: "Hồ sơ",
                value: "hoSo"
            },
            {
                text: "Thao tác",
                value: "actions"
            },
        ],
        khoiList: [{
            id: "",
            name: "Tất cả"
        }],
        trangThaiList: [{
                id: "",
                name: "Tất cả"
            },
            {
                id: 0,
                name: "Đang học"
            },
            {
                id: 1,
                name: "Bảo lưu"
            },
            {
                id: 2,
                name: "Đã thôi học"
            },
            {
                id: 3,
                name: "Đã chuyển trường"
            },
        ],
        lopHocList: [{
            id: "",
            name: "Tất cả",
            code: ""
        }],
        searchResult: [],
        trangThaiMap: {
            0: "Đang học",
            1: "Bảo lưu",
            2: "Đã thôi học",
            3: "Đã chuyển trường"
        },
        gioTinhMap: {
            0: "Nam",
            1: "Nữ"
        },
        canAddUpdate: true
    }),
    computed: {},
    methods: {
        searchStudent(noToInitPage) {
            if(!noToInitPage) {
                this.pageCurrent = 1
            }
            let countStt = (this.pageCurrent-1)*10;
            // this.pageCurrent = 1
            this.$refs["Loading"].open();
            AppService.searchStudentInClassWithNameOrStudentCode(this.maKhoi, this.trangThai, this.lopHoc, this.thongTinHocSinhSearch, this.$store.getters["year"], this.pageCurrent-1).then((data) => {
                        this.pageCount = data.data.data.totalPages
                        this.totalItems =  data.data.data.totalElements

                        this.searchResult = []
                        for (let studentInformation of data.data.data.content) {
                            countStt += 1
                            let dateTemp = studentInformation.birth_day
                            if (dateTemp != null) {
                                dateTemp = dateTemp.slice(0, dateTemp.indexOf("T"))
                                const [year, month, day] = dateTemp.split('-')
                                dateTemp = `${day}-${month}-${year}`
                            }
                            this.searchResult.push({
                                stt: countStt,
                                maHocSinh: studentInformation.code,
                                tenHocSinh: studentInformation.full_name,
                                gioiTinh: this.gioTinhMap[studentInformation.sex],
                                ngaySinh: dateTemp,
                                tenLop: studentInformation.class_name,
                                khoaBan: studentInformation.department_name,
                                trangThai: this.trangThaiMap[studentInformation.status],
                                trangThaiId: studentInformation.status,
                            })
                        }
                        if (this.searchResult.length === 0) {
                            this.$refs["ToastMessage"].open("Danh sách học sinh rỗng", true);
                        }
                    },
                    () => {

                    }
                )
                .then(() => {
                    this.$refs["Loading"].close();
                }).catch(()=>{this.$refs["Loading"].close();});
        },
        addNewStudent() {
            this.$router.push({
                name: "tao-hoc-sinh"
            })
        },
        editStudent(item) {
            this.$router.push({
                name: 'cap-nhat-hoc-sinh',
                query: {
                    student_code: item.maHocSinh
                }
            })
        },
        viewDetail(item) {
            this.$router.push({
                name: 'ho-so-hoc-sinh',
                query: {
                    student_code: item.maHocSinh
                }
            })
        },
        importExcelFile() {
            this.$refs["ImportStudentDialog"].open().then((message) => {
                this.$refs["ToastMessage"].open(message, false);
                this.searchStudent(false)
            }).catch(() => {})
        }
    },
    watch: {
        pageCurrent() {
            this.searchStudent(true)
        },
        maKhoi(newMaKhoi) {
            if (this.$store.getters["year"] === null) {
                return
            }
            let tmpMaKhoi = newMaKhoi
            if(newMaKhoi === "") {
                tmpMaKhoi = 0
            }
            AppService.getClassByGradeAndYearsAllDept(tmpMaKhoi, this.$store.getters["year"]).then((data) => {
                this.lopHocList = []
                this.lopHocList.push({
                    id: "",
                    name: "Tất cả",
                    code: ""
                })
                for (let lop of data.data.data) {
                    this.lopHocList.push({
                        id: lop.id,
                        name: lop.name,
                        code: lop.code
                    })
                }
                this.lopHocList.sort(function(a, b) {
                    if(a.id === "") {return -1 }
                    if(b.id === "") {return 1 }
                    else {return a.name.localeCompare(b.name)}
                })
            })
        },
        '$store.state.year'() {
            if (this.$store.getters["year"] === null) {
                return
            }
            let tmpMaKhoi = this.maKhoi
            if(this.maKhoi === "") {
                tmpMaKhoi = 0
            }
            AppService.getClassByGradeAndYearsAllDept(tmpMaKhoi, this.$store.getters["year"]).then((data) => {
                this.lopHocList = []
                this.lopHocList.push({
                    id: "",
                    name: "Tất cả",
                    code: ""
                })
                for (let lop of data.data.data) {
                    this.lopHocList.push({
                        id: lop.id,
                        name: lop.name,
                        code: lop.code
                    })
                }
                this.lopHocList.sort(function(a, b) {
                    if(a.id === "") {return -1 }
                    if(b.id === "") {return 1 }
                    else {return a.name.localeCompare(b.name)}
                })
            })
        }
    }
};
</script>

<style scoped lang="scss">
</style>
