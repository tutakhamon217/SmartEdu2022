<template>
<div class="ClassInformationDialogCreate">
    <v-dialog v-model="show" persistent max-width="600px">
        <v-card>
            <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                <v-toolbar-title>Tạo mới lớp học</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon @click="close()">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
            <v-card-text>
                <v-container>
                    <v-form ref="form" v-model="valid" lazy-validation>
                        <v-row>
                            <v-col cols="12" sm="6" md="6">
                                <v-text-field label="Mã lớp học (*)" required v-model="classCode" :rules="[this.rule.required, this.rule.maLopHocPattern]"></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-text-field label="Tên lớp học (*)" v-model="className" :rules="[this.rule.tenLopHoc, this.rule.required]"></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-select :items="khoiList" :rules="[this.rule.required]" label="Khối (*)" v-model="khoiId" item-value="id" item-text="name"></v-select>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-select :items="khoaList" :rules="[this.rule.required]" label="Khoa ban (*)" v-model="khoaID" item-value="id" item-text="name"></v-select>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-select :items="monChuyenList" label="Môn chuyên" v-model="monChuyenID" item-value="id" item-text="name">
                                    <template slot="no-data">
                                        <div>Danh sách môn chuyên rỗng</div>
                                    </template>
                                    <template slot="append-outer">
                                        <v-btn class="ml-0" icon @click="monChuyenID = ''">
                                            <v-icon>mdi-close-circle</v-icon>
                                        </v-btn>
                                    </template>
                                </v-select>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-select :items="GVCNList" :rules="[this.rule.required]" label="GVCN (*)" v-model="teacherID" item-value="id" item-text="name">
                                    <template slot="no-data">
                                        <div>Danh sách GVCN rỗng</div>
                                    </template>
                                </v-select>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn elevation="1" color="warning darken-1" text @click="close"> Hủy bỏ </v-btn>
                <v-btn elevation="1" color="primary darken-1" text :loading="isSubmitting" @click="submit"> Lưu lại </v-btn>
                <v-spacer></v-spacer>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
</div>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";

export default {
    name: "ClassInformationDialogCreate",
    components: {
        ToastMessage
    },
    created() {
        AppService.getAllGradeLevel().then((data) => {
            this.khoiList = []
            for (let gradeLevel of data.data.data) {
                this.khoiList.push({
                    id: gradeLevel.id,
                    name: gradeLevel.name
                })
            }
        })
        AppService.getAllDepartmentForClass().then((data) => {
            this.khoaList = []
            for (let khoa of data.data.data) {
                this.khoaList.push({
                    id: khoa.id,
                    name: khoa.name
                })
            }
        })
    },
    data() {
        return {
            valid: false,
            title: "",
            classCode: "",
            className: "",
            khoiId: "",
            khoaID: "",
            monChuyenID: "",
            teacherID: "",
            teacherName: "",
            show: false,
            khoiList: [],
            khoaList: [],
            GVCNList: [],
            monChuyenList: [],
            rule: {
                required: value => !!value || 'Bắt buộc',
                maLopHocPattern: value => {
                    if(value.includes(" ")) return "Mã lớp học không được chứa khoảng trắng"
                    if(value.length > 50) return 'Mã lớp học không quá 50 ký tự'
                    const pattern = /^[a-zA-Z0-9]*$/
                    return pattern.test(value) || 'Mã lớp học không hợp lệ'
                },
                tenLopHoc: value => value.length <= 250 || 'Tên lớp học không quá 250 ký tự',
            },
            isSubmitting: false
        };
    },

    methods: {
        open() {
            this.classCode = ""
            this.className = ""
            this.khoiId = ""
            this.khoaID = ""
            this.monChuyenID = ""
            this.teacherID = ""
            this.teacherName = ""
            this.GVCNList = []
            this.monChuyenList = []

            this.show = true;

            AppService.getMonChuyen(this.khoaID, this.khoiId).then((data) => {
                this.monChuyenList = [];
                for (let monChuyen of data.data.data) {
                    this.monChuyenList.push({
                        id: monChuyen.id,
                        name: monChuyen.name,
                    });
                }
            });
            AppService.getGiaoVienChuNhiem().then((data) => {
                this.GVCNList = []
                for (let gvcn of data.data.data.Teachers) {
                    this.GVCNList.push({
                        id: gvcn.id,
                        name: gvcn.full_name
                    })
                }
            })

            this.valid = true
            //   this.initData();
            return new Promise((resolve, reject) => {
                this.promise = {
                    resolve,
                    reject,
                };
            });

        },
        close() {
            this.show = false;
        },
        submit() {
            this.valid = this.$refs.form.validate()

            if (this.valid === false || this.isSubmitting) {
                return
            } else {
                let result = {
                    classCode: this.classCode,
                    className: this.className,
                    khoiId: this.khoiId,
                    khoaID: this.khoaID,
                    monChuyenID: this.monChuyenID,
                    teacherID: this.teacherID,
                };

                let classInformation = {
                    years: this.$store.getters["year"],
                    classCode: result.classCode,
                    className: result.className,
                    khoiId: result.khoiId,
                    khoaID: result.khoaID,
                    monChuyenID: result.monChuyenID,
                    teacherID: result.teacherID,
                };
                this.isSubmitting = true
                AppService.addClass(classInformation).then(
                    (res) => {
                        if (res.data.status === "OK") {
                            this.show = false
                            this.promise.resolve("Tạo lớp học thành công!");
                            this.$refs.form.resetValidation()
                        } else {
                            this.$refs["ToastMessage"].open(res.data.message, true);
                        }
                        this.isSubmitting = false
                    }).catch((res) => {
                    this.$refs["ToastMessage"].open(res.data.message, true);
                    this.isSubmitting = false
                });
            }
        },
    },
    watch: {
        khoaID(newparentDepartment) {
            AppService.getMonChuyen(newparentDepartment, this.khoiId).then((data) => {
                this.monChuyenList = [];
                for (let monChuyen of data.data.data) {
                    this.monChuyenList.push({
                        id: monChuyen.id,
                        name: monChuyen.name,
                    });
                }
            });
        },
        khoiId(newKhoiId) {
            AppService.getMonChuyen(this.khoaID, newKhoiId).then((data) => {
                this.monChuyenList = [];
                for (let monChuyen of data.data.data) {
                    this.monChuyenList.push({
                        id: monChuyen.id,
                        name: monChuyen.name,
                    });
                }
            });
        },
    },
};
</script>

<style scoped>
</style>
