<template>
<div class="ClassInformationDialogCreate">
    <v-dialog v-model="show" persistent max-width="600px">
        <v-card>
            <v-toolbar color="primary" dark>
                <v-toolbar-title>Cập nhật thông tin lớp học</v-toolbar-title>
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
                                <v-text-field label="Mã lớp học (*)" required v-model="classCode" disabled :rules="[this.rule.required, this.rule.maLopHocPattern]"></v-text-field>
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
    async created() {
        await AppService.getAllGradeLevel().then((data) => {
            this.khoiList = []
            for (let gradeLevel of data.data.data) {
                this.khoiList.push({
                    id: gradeLevel.id,
                    name: gradeLevel.name
                })
            }
            if (this.khoiList.length > 0) {
                this.khoiId = this.khoiList[0].id
            } else {
                this.khoiId = ""
            }
        })
        await AppService.getAllDepartmentForClass().then((data) => {
            this.khoaList = []
            for (let khoa of data.data.data) {
                this.khoaList.push({
                    id: khoa.id,
                    name: khoa.name
                })
            }
            if (this.khoaList.length > 0) {
                this.khoaID = this.khoaList[0].id
            } else {
                this.khoaID = ""
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
                    const pattern = /^[a-zA-Z0-9]*$/
                    return pattern.test(value) || 'Mã lớp học không hợp lệ'
                },
                tenLopHoc: value => value.length <= 250 || 'Tên đơn vị không quá 250 ký tự',
            },
            isSubmitting: false,
        };
    },

    methods: {
        async open(classInformation) {
            this.valid = true
            this.classCode = ""
            this.className = ""
            this.khoiId = ""
            this.khoaID = ""
            this.monChuyenID = ""
            this.teacherID = ""
            this.teacherName = ""
            this.GVCNList = []
            this.monChuyenList = []
            this.khoiId = classInformation.khoiId,
                this.khoaID = classInformation.khoaID,
                await AppService.getMonChuyen(this.khoaID, this.khoiId).then((data) => {
                    this.monChuyenList = [];
                    for (let monChuyen of data.data.data) {
                        this.monChuyenList.push({
                            id: monChuyen.id,
                            name: monChuyen.name,
                        });
                    }
                    if (this.monChuyenList.length > 0) {
                        this.monChuyen = this.monChuyenList[0].id
                    } else {
                        this.monChuyen = ""
                    }
                });
            await AppService.getGiaoVienChuNhiem().then((data) => {
                this.GVCNList = []
                for (let gvcn of data.data.data.Teachers) {
                    this.GVCNList.push({
                        id: gvcn.id,
                        name: gvcn.full_name
                    })
                    if (this.GVCNList.length > 0) {
                        this.teacherID = this.GVCNList[0].id
                    } else {
                        this.teacherID = ""
                    }
                }
            })

            this.classCode = classInformation.classCode,
            this.className = classInformation.className,

            this.monChuyenID = classInformation.monChuyenID,
            this.teacherID = classInformation.teacherID

            this.show = true;
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
            if (this.isSubmitting) {
                return
            }
            this.valid = this.$refs.form.validate()
            if (this.valid === false) {
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

                let newClassInformation = {
                    years: this.$store.getters["year"],
                    classCode: result.classCode,
                    className: result.className,
                    khoiId: result.khoiId,
                    khoaID: result.khoaID,
                    monChuyenID: result.monChuyenID,
                    teacherID: result.teacherID,
                };
                this.isSubmitting = true
                AppService.updateClass(newClassInformation).then(
                    (res) => {
                        if (res.data.status === "OK") {
                            this.show = false
                            this.promise.resolve("Cập nhật lớp học thành công!");
                        } else {
                            this.$refs["ToastMessage"].open(res.data.message, true);
                        }
                        this.isSubmitting = false
                    }).catch(() => {
                    this.$refs["ToastMessage"].open("Cập nhật lớp học thất bại", true);
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
                if (this.monChuyenList.length > 0) {
                    this.monChuyen = this.monChuyenList[0].id
                } else {
                    this.monChuyen = ""
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
                if (this.monChuyenList.length > 0) {
                    this.monChuyen = this.monChuyenList[0].id
                } else {
                    this.monChuyen = ""
                }
            });
        },
    },
};
</script>

<style scoped>
</style>
