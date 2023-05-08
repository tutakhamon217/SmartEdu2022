<template>
  <div class="transferStudentDialog">
    <v-dialog v-model="show" persistent max-width="600px">
      <v-card>
        <v-card-title class="text-h6 text-center" style="background-color: #0275d8;color:white ;"
          ><p class="ma-0 text-center" style=" width: 100%;">KẾT CHUYỂN HỌC SINH</p></v-card-title
        >
        <v-form ref="form">
            <v-col class="pa-8">
                <v-row class="ma-0">
                    <v-col cols="3" class="pa-0">
                        <p class="text-h6">Năm học: </p>
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <p class="text-h6">{{ year }}</p>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 pt-4">Kết chuyển</v-col>
                    <v-col cols="9" class="pa-0">
                        <v-select :rules="[this.rule.ketChuyen]" class="pa-0" style="margin-top: 22px;" v-model="ketChuyen" item-text="name" item-value="id" :items="dataKetChuyen"></v-select>
                    </v-col>
                </v-row>
                <v-row class="ma-0 mt-5" align="center">
                    <v-col cols="12" class="pa-0"><p class="text-h6" style="color: #488fef">Thông tin kết chuyển</p></v-col>
                </v-row>
                <v-row class="ma-0">
                    <v-col cols="3" class="pa-0">
                        <p class="text-h6">Năm học mới:</p>
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <p class="text-h6">{{nextYear}}</p>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 pt-4">
                        Khối
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <v-select :rules="[this.rule.required]" class="pa-0" style="margin-top: 22px;" v-model="khoi" item-text="name" item-value="id" :items="dataKhoi"></v-select>
                    </v-col>
                </v-row>
                <v-row class="ma-0" align="center">
                    <v-col cols="3" class="pa-0 pt-4">
                        Lớp
                    </v-col>
                    <v-col cols="9" class="pa-0">
                        <v-select :rules="[this.rule.required]" class="pa-0" style="margin-top: 22px;" v-model="lop" item-text="name" item-value="code" :items="dataLop">
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
          <v-btn color="primary" @click="accept()"
            >Xác nhận</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import AppService from "@/services/app.service";
export default {
  props: ['dataKhoi', 'year', 'nextYear'],
  data() {
    return {
      show: false,
      dataKetChuyen: [
        { id: 1, name: 'Được lên lớp' },
        { id: 0, name: 'Lưu ban' }
      ],
      ketChuyen: null,
      khoi: null,
      lop: null,
      rule: {
        required: value => !!value || 'Bắt buộc',
        ketChuyen: value => (value === 0 || value === 1) || 'Không được để trống' 
      },
      dataLop: []
    };
  },
  methods: {
    open() {
      this.show = true;
      this.ketChuyen = this.dataKetChuyen[0].id
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    cancel() {
      this.resetData()
      this.show = false;
      this.promise.reject();
    },
    accept() {
        if(this.$refs['form'].validate()){
          this.promise.resolve({
            status: this.ketChuyen,
            newClassCode: this.lop,
            newGradeLevel: this.khoi,
            newSchoolYear: this.nextYear
          })
          this.show = false;
          this.resetData()
        }
    },
    resetData(){
      this.ketChuyen = null
      this.khoi = null
      this.lop = null
      this.$refs['form'].reset()
    },
    getClassByGradeLevelAndYear(){
      return AppService.getClassByGradeAndYearsAllDept(this.khoi,this.nextYear)
      .then((res) => {
        if(res.data.data.length != 0){
          this.dataLop = []
          for(let i of res.data.data) {
            this.dataLop.push({
              id: i.id,
              name: i.code + " - " + i.name,
              code: i.code
            })
          }
          this.lop = this.dataLop[0].code
        }else{
          this.dataLop = []
          this.lop = null
        }
      })
      .catch(() => {})
    }
  },
  watch: {
    khoi(){
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