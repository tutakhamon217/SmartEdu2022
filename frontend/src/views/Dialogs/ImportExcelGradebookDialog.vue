<template>
  <v-dialog v-model="show" max-width="900">
    <v-form ref="checkChooseHeadOfScore">
    <v-card>
      <v-card-title
        style="background-color: rgb(26 118 207 / 1); color: white"
        class="justify-center py-2"
      >
        IMPORT FILE ĐIỂM
      </v-card-title>
      <v-card-title class="justify-center">
        Thực hiện import file điểm cho các cột điểm sau:
      </v-card-title>

      <v-card-title class="mx-3">
            <v-select
            :items="items"
            v-model="choosedItems"
            multiple
            dense
            outlined
            item-text="name"
            item-value="stt"
            no-data-text="Không có dữ liệu"
            class="selectColumnGradebook"
            :rules="rules"
            label="Tên cột điểm"
            ></v-select>
      </v-card-title>

      <v-card-title class="justify-center">
        <span class="ml-4">File danh sách</span>
        <v-spacer></v-spacer>
        <span>Tải file mẫu</span>
        <v-btn text style="color: rgb(25 118 210)" :loading="loading" @click="downloadFile"> [Bấm để tải]</v-btn>
      </v-card-title>
      <v-card-actions class="justify-center pb-5" >
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
            <div v-if="messageError" class="text-center my-3">
              <p style="color: red">{{ messageError }}</p>
            </div>
        </div>
        <v-form ref="form">
          <v-file-input
            v-show="false"
            ref="inputFile"
            v-model="fileExcel"
            accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
            :rules="rulesFile"
          ></v-file-input>
        </v-form>
      </v-card-actions>
      <v-card-actions class="justify-center pb-5">
        <v-btn
          class="mr-2"
          elevation="2"
          color="warning darken-1"
          text
          @click="cancel"
        >
          Hủy bỏ
        </v-btn>
        <v-btn
          class="ml-2"
          elevation="2"
          color="primary darken-1"
          text
          @click="confirm()"
          :loading="loadingImport"
          :disabled="!fileExcel"
        >
          Lưu lại
        </v-btn>
      </v-card-actions>
    </v-card>
    </v-form>
    <ToastMessage ref="toastMessage" />
  </v-dialog>
</template>

<script>
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";
export default {
  props: ['items'],
  data() {
    return {
      show: false,
      promise: null,
      fileExcel: null,
      loadingImport: false,
      choosedItems: [],
      subjectCode: null,
      classCode: null,
      semester: null,
      rules: [
        (v) => v.length !== 0 || 'Chưa chọn đầu điểm nào'
      ],
      loading: false,
      rulesFile: [
        value => !value || value.size <= 5242880 || 'Dung lượng file phải nhỏ hơn 5 MB',
      ],
      messageError: null
    };
  },
  components:{
    ToastMessage
  },
  methods: {
    open(subjectCode, classCode, semester, type) {
      this.show = true;
      this.subjectCode = subjectCode
      this.classCode = classCode
      this.semester = semester
      this.type = type
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    confirm() {
      if(this.$refs['form'].validate()){
            this.loadingImport = true
            let choosed = this.choosedItems.map(x => this.items[x].id)
            AppService.importExcelGradeBook({
                file: this.fileExcel,
                subjectCode: this.subjectCode,
                classCode: this.classCode,
                schoolYear: this.$store.getters['year'],
                semester: this.semester,
                login: this.$store.getters['user'].username,
                confScoreDetailsIdList: choosed
            }, this.type)
            .then((res) => {
                if(res.data.status === 'OK'){
                    this.show = false
                    this.resetData()
                    this.promise.resolve(res.data.message);
                }else{
                    this.fileExcel = null
                    this.messageError = res.data.message
                }
            })
            .catch(() => {
              this.fileExcel = null
              this.messageError = "Import file thất bại"
            })
            .finally(() => {
                this.loadingImport = false
            })
      }else{
        this.$refs['toastMessage'].open('Dung lượng file đã quá 5MB', true)
      }
    },
    cancel() {
      this.resetData()
      this.promise.reject();
      this.show = false;
    },
    resetData(){
      this.choosedItems = []
      this.fileExcel = null
      this.messageError = null
      this.$refs['checkChooseHeadOfScore'].reset()
    },
    chooseFile(){
      this.messageError = null
      this.$refs["inputFile"].$refs["input"].click();
    },
    downloadFile(){
      if(this.$refs['checkChooseHeadOfScore'].validate()){
        this.loading = true
        let choosed = this.choosedItems.map(x => this.items[x].id)
        if(this.type == 'score'){
            AppService.exportExcelGradeBookScore({
                year: this.$store.getters['year'],
                subjectCode: this.subjectCode,
                classCode: this.classCode,
                semester: this.semester,
                confScoreDetailsIdList: choosed
            })
            .then((res) => {
            var a = window.document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob([res.data], { type: 'application/octet-stream' }));
            a.download = "sodiem.xlsx"; //fName was the file name portion of the key what was passed in as part of the key value within params. 

            //  // Append anchor to body.
            document.body.appendChild(a)
            a.click();

            //  // Remove anchor from body
            document.body.removeChild(a)
            })
            .catch((res) => {
                this.$refs['toastMessage'].open(res.message, true)
            })
            .finally(() => {
              this.loading = false
            })
        }else{
            AppService.exportExcelGradeBookGrading({
                year: this.$store.getters['year'],
                subjectCode: this.subjectCode,
                classCode: this.classCode,
                semester: this.semester,
                confGradeDetailsIdList: choosed
            })
            .then((res) => {
            var a = window.document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob([res.data], { type: 'application/octet-stream' }));
            a.download = "GradeBookGrading.xlsx"; //fName was the file name portion of the key what was passed in as part of the key value within params. 

            //  // Append anchor to body.
            document.body.appendChild(a)
            a.click();

            //  // Remove anchor from body
            document.body.removeChild(a)
            })
            .catch((res) => {
                this.$refs['toastMessage'].open(res.message, true)
            })
            .finally(() => {
              this.loading = false
            })
        }
      }
    },
    isIncreasing(xs) {
        for (var i = 0; i < xs.length - 1; i++) {
            if (xs[i] > xs[i + 1]) {
                return false;
            }
        }
        return true;
    }
  },
  watch: {
    choosedItems(){
      if(!this.isIncreasing(this.choosedItems)){
        let temp = JSON.parse(JSON.stringify(this.choosedItems))
        temp.sort()
        this.choosedItems = temp
      }
    }
  }
};
</script>

