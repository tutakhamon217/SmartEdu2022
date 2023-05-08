<template>
<v-dialog v-model="show" max-width="900">
    <v-card>
        <v-card-title style="background-color: rgb(26 118 207 / 1); color: white" class="justify-center py-2">
            IMPORT FILE
        </v-card-title>
        <v-card-title class="justify-center" style="opacity: 0.6">
            Thực hiện import danh sách học sinh cho lớp
        </v-card-title>
        <v-card-title class="justify-center" style="opacity: 0.6">
            <span class="ml-4">File danh sách</span>
            <v-spacer></v-spacer>
            <span>Tải file mẫu</span>
            <v-btn text style="color: rgb(25 118 210)" @click="downloadFile"> [Bấm để tải]</v-btn>
        </v-card-title>
        <v-card-actions class="justify-center pb-5">
            <div class="mx-5" style="border: 1px solid black;border-style: dotted;width: 100%;">
                <div class="text-center my-3">
                    <v-btn color="primary" @click="chooseFile">
                        <v-icon>mdi-cloud-upload</v-icon>
                        Tải file
                    </v-btn>
                </div>
                <div v-if="fileExcel" class="text-center my-3">
                    <p>Tên file: {{ fileExcel.name }}</p>
                    <p>Dung lượng file: {{ (fileExcel.size / 1048576).toFixed(2) }} MB</p>
                </div>
                <div class="text-center my-3">
                    Yêu cầu định dạng file mẫu, có dung lượng nhỏ hơn 5 MB
                </div>

                <div v-if="importError" class="text-center my-3" style="color: red">
                    {{errorMessage}}
                </div>
                <div v-if="importError && errorList.length > 0" class="text-center my-3 justify-center d-flex flex-row-reverse" width="70%" style="color: red">
                    <v-spacer></v-spacer>
                    <v-virtual-scroll bench="10" :items="errorList" height="150" min-height="80" width="50%" item-height="50">
                        <template v-slot:default="{ item }">
                            <v-list-item :key="item.stt">
                                <v-list-item-action style="color: red">
                                    {{item.stt}}
                                </v-list-item-action>
                                <v-list-item-content>
                                    <v-list-item-title style="color: red">
                                        <v-tooltip bottom>
                                            <template v-slot:activator="{ on, attrs }">
                                                <span v-bind="attrs" v-on="on">{{formatTextTooltip(item.name, 50)}}</span>
                                            </template>
                                            <span>{{item.name}}</span>
                                        </v-tooltip>

                                    </v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                            <v-divider></v-divider>
                        </template>
                    </v-virtual-scroll>
                    <v-spacer></v-spacer>
                </div>
            </div>
            <v-form ref="form-import">
                <v-file-input v-show="false" ref="inputFile" v-model="fileExcel" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" :rules="rulesFile"></v-file-input>
            </v-form>
        </v-card-actions>
        <v-card-actions class="justify-center pb-5">
            <v-btn class="ml-2" elevation="2" color="warning darken-1" text @click="cancel">
                Hủy bỏ
            </v-btn>
            <v-btn :disabled="!fileExcel" class="mr-2" elevation="2" color="primary darken-1" text @click="confirm()" :loading="loadingImport">
                Lưu lại
            </v-btn>
        </v-card-actions>
    </v-card>
    <ToastMessage ref="toastMessage" />
</v-dialog>
</template>

<script>
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";
export default {
    data() {
        return {
            show: false,
            promise: null,
            fileExcel: null,
            loadingImport: false,
            importError: false,
            errorMessage: "",
            errorList: [],
            rulesFile: [
                value => !value || value.size <= 5242880 || 'Dung lượng file phải nhỏ hơn 5 MB',
            ]
        };
    },
    components: {
        ToastMessage
    },
    methods: {
        open() {
            this.fileExcel = null
            this.loadingImport = false
            this.importError = false
            this.errorMessage = ""
            this.errorList = []

            this.show = true;
            return new Promise((resolve, reject) => {
                this.promise = {
                    resolve,
                    reject,
                };
            });
        },
        confirm() {
            if (!this.$refs['form-import'].validate()) {
                this.$refs['toastMessage'].open('Dung lượng file đã quá 5MB', true)
                return
            }
            this.importError = false
            this.errorMessage = ""
            this.loadingImport = true
            AppService.importExcelStudent(this.fileExcel, this.$store.getters['year'])
                .then((res) => {
                    if (res.data.status === 'OK') {
                        this.show = false
                        this.resetData()
                        this.promise.resolve(res.data.message);
                    } else {
                        this.importError = true;
                        this.errorMessage = res.data.message
                        let stt = 0
                        if (res.data.data !== null && res.data.data !== undefined) {
                            this.errorList = []
                            for (let i of res.data.data) {
                                stt += 1
                                this.errorList.push({
                                    stt: stt,
                                    name: i
                                })
                            }
                        }

                        this.$refs['toastMessage'].open("Import không thành công", true)
                    }
                })
                .catch((res) => {
                    setTimeout(() => {
                        this.$refs['toastMessage'].open(res.message, true)
                    }, 1000)
                })
                .finally(() => {
                    this.loadingImport = false
                })
        },
        cancel() {
            this.resetData()
            this.promise.reject();
            this.show = false;
        },
        resetData() {
            this.fileExcel = null
        },
        chooseFile() {
            this.$refs["inputFile"].$refs["input"].click();
        },
        downloadFile() {
            AppService.downloadExcelImportStudent()
                .then((res) => {
                    var a = window.document.createElement('a');
                    a.href = window.URL.createObjectURL(new Blob([res.data], {
                        type: 'application/octet-stream'
                    }));
                    a.download = "DS_HocSinh.xlsx"; //fName was the file name portion of the key what was passed in as part of the key value within params. 

                    //  // Append anchor to body.
                    document.body.appendChild(a)
                    a.click();

                    //  // Remove anchor from body
                    document.body.removeChild(a)
                })
                .catch(() => {
                    this.$refs['toastMessage'].open("Tải file mẫu lỗi", true)
                })
        },
        formatTextTooltip(item, max) {
            if (item === null || item === undefined) {
                return ""
            }
            if (item.length >= max) {
                return item.substring(0, max) + "...";
            }
            return item;
        }

    },
};
</script>
