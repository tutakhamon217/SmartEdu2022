<template>
  <div class="TransferStudentUpdateDialog">
    <v-dialog v-model="show" persistent max-width="600px">
      <v-card>
        <v-card-title class="text-h6 text-center" style="background-color: #0275d8;color:white ;"
          ><p class="ma-0 text-center" style=" width: 100%;">KẾT CHUYỂN HỌC SINH</p></v-card-title
        >
        <v-form ref="form">
            <v-col class="pa-8">
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0">
                        <p class="pa-0 text-h6">Năm học: </p>
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <p class="text-h6">{{ year }}</p>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0"><p>Học sinh</p></v-col>
                    <v-col cols="9" class="pa-0">
                        <p>{{dataStudent.details.studentCode}} {{" - "}} {{dataStudent.details.studentName}}</p>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0"><p>Lớp</p></v-col>
                    <v-col cols="9" class="pa-0">
                        <p>{{dataStudent.classCode}} {{" - "}} {{dataStudent.className}}</p>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 pt-2">Kết chuyển</v-col>
                    <v-col cols="9" class="pa-0">
                        <v-select :rules="[this.rule.ketChuyen]" class="pa-0" style="margin-top: 22px;" v-model="dataStudent.details.status" item-text="name" item-value="id" :items="dataKetChuyen"></v-select>
                    </v-col>
                </v-row>
                <v-row class="ma-0">
                    <v-col cols="12" class="pa-0"><p class="text-h6" style="color:#488fef">Thông tin kết chuyển</p></v-col>
                </v-row>

                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 text-h6" > <p>Năm học mới:</p></v-col>
                    <v-col cols="9" class="pa-0 text-h6">
                        <p>{{dataStudent.details.newSchoolYear}}</p>
                    </v-col>
                </v-row>

                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 pt-2">
                        Khối
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <v-select :rules="[this.rule.required]" class="pa-0" style="margin-top: 22px;" v-model="dataStudent.details.newGradeLevel" item-text="name" item-value="id" :items="dataKhoi"></v-select>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 pt-2">
                        Lớp
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <v-select :rules="[this.rule.required]" class="pa-0" style="margin-top: 22px;" v-model="dataStudent.details.newClassCode" item-text="name" item-value="code" :items="dataLop">
                          <template #no-data>
                              <p class="ma-0">Chưa khai báo lớp học cho năm học {{nextYear}} trong khối đã chọn</p>
                          </template>
                        </v-select>
                    </v-col>
                </v-row>
            </v-col>
        </v-form>
        <v-card-actions class="pb-5">
          <v-spacer></v-spacer>
          <v-btn @click="cancel()">Hủy bỏ</v-btn>
          <v-btn color="primary" :loading="isSend" @click="accept()"
            >Xác nhận</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    <ToastMessage ref="toastMessage" />
    </v-dialog>

  </div>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";

export default {
  components: {
    ToastMessage,
  },
  props: ['dataKhoi', 'year', 'nextYear'],
  data() {
    return {
      show: false,
      dataKetChuyen: [
        { id: 1, name: 'Được lên lớp' },
        { id: 0, name: 'Lưu ban' }
      ],
      dataStudent: "",
      rule: {
        required: value => !!value || 'Bắt buộc',
        ketChuyen: value => (value === 0 || value === 1) || 'Không được để trống' 
      },
      dataLop: [],
      isSend: false
    };
  },
  methods: {
    open(dataStudent) {
        this.show = true;
        this.dataStudent = dataStudent
        this.getClassByGradeLevelAndYear()
        return new Promise((resolve, reject) => {
            this.promise = {
            resolve,
            reject,
            };
        });
        },
    cancel() {
        this.show = false
    },
    failUpdate() {
      this.resetData()
      this.show = false;
      this.promise.reject();
    },
    accept() {
        if(this.$refs['form'].validate()){
            this.isSend = true
            AppService.updateListTransferStudent([this.dataStudent])
            .then((response) => {
                if(response.data.status === 'OK'){
                    this.show = false
                    this.promise.resolve(response.data.message)
                }else{
                this.$refs['toastMessage'].open(response.data.message, true)
                }
                this.isSend = false
            }).catch(() => {
                this.isSend = false
                this.failUpdate()
            })
        }
    },
    resetData(){
      this.$refs['form'].reset()
    },
    getClassByGradeLevelAndYear(){
      return AppService.getClassByGradeAndYearsAllDept(this.dataStudent.details.newGradeLevel,this.nextYear)
      .then((res) => {
        if(res.data.data.length !== 0){
          this.dataLop = []
          for(let i of res.data.data) {
            this.dataLop.push({
              id: i.id,
              name: i.code + " - " + i.name,
              code: i.code
            })
          }
          this.dataStudent.details.newClassCode = this.dataLop[0].code
        }else{
          this.dataLop = []
          this.dataStudent.details.newClassCode = ""
        }
      })
      .catch(() => {this.$refs['loading'].close()})
    },
  },
  watch: {
    'dataStudent.details.newGradeLevel'(){
      this.getClassByGradeLevelAndYear()
    }
  }
};
</script>

<style>
.transferStudentDialog .v-text-field{
    padding-top: 0px !important;
}

.transferStudentDialog .col{
    padding: 0px !important;
}
</style>