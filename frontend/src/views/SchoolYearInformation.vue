<template>
  <div>
    <v-card>
          <v-toolbar
                flat
                dense
                dark
                class="font-weight-bold"
                color="primary lighten-1"
                style="border-radius: 5px 5px 0px 0px"
          >
            <v-toolbar-title
              style="width: 100%; align-items: center"
              class="d-flex"
            >
              <span>Danh sách năm học</span>
              <v-spacer></v-spacer>
              <v-btn @click="editItem" color="white" class="float-right" dark>
                <v-icon color="green">mdi-plus-outline</v-icon>
                <p class="ma-0" style="color: black">Khai báo năm học</p>
              </v-btn>
            </v-toolbar-title>
          </v-toolbar>
    </v-card>
    <v-data-table
    :items="desserts"
    :headers="headers"
    :items-per-page="10"
    :footer-props="{
      'items-per-page-text': 'Số dòng mỗi trang:',
    }">
        <template v-slot:item.bat_dau_hoc_ky_1="{ item }">
          <span>{{ formatDate(item.bat_dau_hoc_ky_1) }}</span>
        </template>

        <template v-slot:item.ket_thuc_hoc_ky_1="{ item }">
          <span>{{ formatDate(item.ket_thuc_hoc_ky_1) }}</span>
        </template>

        <template v-slot:item.bat_dau_hoc_ky_2="{ item }">
          <span>{{ formatDate(item.bat_dau_hoc_ky_2) }}</span>
        </template>

        <template v-slot:item.ket_thuc_hoc_ky_2="{ item }">
          <span>{{ formatDate(item.ket_thuc_hoc_ky_2) }}</span>
        </template>

        <template slot="no-data">
          Danh sách năm học rỗng
        </template>
        <template v-slot:item.actions="{ item }" >
            <v-icon v-if="item.canUpdate" small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        </template>

        <template v-slot:footer.page-text="props">
          {{ props.pageStart }}-{{ props.pageStop }} của
          {{ props.itemsLength }} kết quả
        </template>

    </v-data-table>
    <SchoolYearDialog
      ref="schoolYearDialog"
      :desserts="desserts"
      :schoolYears="schoolYears"
      :headers="headers"
      @changeHeader="changeHeader"
      @openToastMessage="openToastMessage"
    />
    <ToastMessage ref="toastMessage"/>
    <Loading ref="Loading"></Loading>
  </div>
</template>

<script>
import ToastMessage from "@/components/ToastMessage";
import SchoolYearDialog from "@/views/Dialogs/SchoolYearDialog";
import { schoolYearMixin } from "@/mixin/SchoolYearMixin";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue"

export default {
  mixins: [schoolYearMixin],
  name: "SchoolYearInformation",
  components: {
    SchoolYearDialog,
    ToastMessage,
    Loading
  },
  data: () => ({
    isDelete: false,
    isEdit: true,
    headers: [],
    desserts: [],
    schoolYears: [],
    gradeLevels: [],
    promise: null,
    oldDesserts: []
  }),
  created() {
    this.generateSchoolYears()
  },
  async mounted() {
    this.$refs["Loading"].open()
    await this.$store.dispatch("getCurrentUser");
    await this.initLoad()
    this.$refs["Loading"].close()
    // this.removeCol();
  },
  methods: {
    editItem(item) {
      this.$refs.schoolYearDialog
        .open(item)
        .then(async (result) => {
          let indexYear = this.desserts.findIndex(x => x.nam_hoc == result.nam_hoc)
          let temp = result
          let objTemp = {}
          objTemp.years = temp.nam_hoc
          objTemp.semesterAmount = temp.so_hoc_ky
          objTemp.fromDate1 = this.formatDateRevert(temp.bat_dau_hoc_ky_1) + ' 00:00:00'
          objTemp.toDate1 = this.formatDateRevert(temp.ket_thuc_hoc_ky_1) + ' 00:00:00'
          // objTemp.fromDate2 = temp.bat_dau_hoc_ky_2 + ' 00:00:00'
          // objTemp.toDate2 = temp.ket_thuc_hoc_ky_2 + ' 00:00:00'
          objTemp.fromDate2 = temp.bat_dau_hoc_ky_2 ? (this.formatDateRevert(temp.bat_dau_hoc_ky_2) + ' 00:00:00') : null
          objTemp.toDate2 = temp.ket_thuc_hoc_ky_2 ? (this.formatDateRevert(temp.ket_thuc_hoc_ky_2) + ' 00:00:00') : null
          // objTemp.fromDate3 = temp.bat_dau_hoc_ky_3 + ' 00:00:00'
          // objTemp.toDate3 = temp.ket_thuc_hoc_ky_3 + ' 00:00:00'
          // objTemp.fromDate4 = temp.bat_dau_hoc_ky_4 + ' 00:00:00'
          // objTemp.toDate4 = temp.ket_thuc_hoc_ky_4 + ' 00:00:00'
          if(indexYear < 0){
            await AppService.saveSchoolYear(objTemp)
            .then((result) => {
              if(result.data.status === 'BAD_REQUEST'){
                setTimeout(() => {
                  this.$refs['toastMessage'].open(result.data.message, true)
                }, 0)
              }else{
                temp.bat_dau_hoc_ky_1 = this.formatDateRevert(temp.bat_dau_hoc_ky_1) 
                temp.ket_thuc_hoc_ky_1 = this.formatDateRevert(temp.ket_thuc_hoc_ky_1)
                temp.bat_dau_hoc_ky_2 = temp.bat_dau_hoc_ky_2 ? (this.formatDateRevert(temp.bat_dau_hoc_ky_2)) : null
                temp.ket_thuc_hoc_ky_2 = temp.ket_thuc_hoc_ky_2 ? (this.formatDateRevert(temp.ket_thuc_hoc_ky_2)) : null
                temp.canUpdate = 1
                this.desserts.push(temp);
                this.oldDesserts = JSON.parse(JSON.stringify(this.desserts));
                setTimeout(() => {
                  this.$refs['toastMessage'].open(result.data.message, false)
                }, 0)
                this.checkExistKy12();
                if (this.existKy2) {
                  this.changeHeader(this.dataKy2)
                  return;
                }
                if (this.existKy1) {
                  this.changeHeader(this.dataKy1)
                }
              }
              this.initLoad()
            })
            .catch(() => {
              this.$refs['toastMessage'].open('Thêm thất bại', true)
            })
          }else{
            await AppService.saveSchoolYear(objTemp)
            .then( (res) => {
              if(res.data.status === 'BAD_REQUEST'){
                this.$refs['toastMessage'].open(res.data.message, true)
              }else{
                result.stt = indexYear + 1
                result.bat_dau_hoc_ky_1 = this.formatDateRevert(result.bat_dau_hoc_ky_1) 
                result.ket_thuc_hoc_ky_1 = this.formatDateRevert(result.ket_thuc_hoc_ky_1)
                result.bat_dau_hoc_ky_2 = result.bat_dau_hoc_ky_2 ? (this.formatDateRevert(result.bat_dau_hoc_ky_2)) : null
                result.ket_thuc_hoc_ky_2 = result.ket_thuc_hoc_ky_2 ? (this.formatDateRevert(result.ket_thuc_hoc_ky_2)) : null
                Object.assign(this.desserts[indexYear], result)
                this.oldDesserts = JSON.parse(JSON.stringify(this.desserts));
                // this.$emit('openToastMessage', {
                //     mess: 'Cập nhật thành công',
                //     status: false
                // })
                //truong hop da ton tai nam hoc roi nhung van tao vao nam do
                this.$refs['toastMessage'].open("Cập nhật thành công", false)
                this.checkExistKy12();
                if (this.existKy2) {
                  this.changeHeader(this.dataKy2)
                  return;
                }
                if (this.existKy1) {
                  this.changeHeader(this.dataKy1)
                }
              }
            })
            .catch( () => {
              this.$emit('openToastMessage', {
                mess: 'Cập nhật thất bại',
                status: true
              })
            })
          }
        })
        .catch( (res) => {
          if(res){
            this.desserts = JSON.parse(JSON.stringify(this.oldDesserts));
          }else{
            this.oldDesserts = JSON.parse(JSON.stringify(this.desserts));
          }
        })
    },
    removeCol() {
      if (this.existKy2) {
        this.headers = this.dataKy2;
        return;
      }
      if (this.existKy1) {
        this.headers = this.dataKy1;
      }
    },
    changeHeader(data) {
      this.headers = data;
    },
    initLoad(){
     return AppService.getAllSchoolYearInformation()
      .then( data => {
        this.desserts = []
        var schoolYears = data.data.data.schoolYears
        for(let i = 0; i < schoolYears.length; i++){
          let year = {}
          year.stt = i + 1
          year.nam_hoc = schoolYears[i].year
          year.so_hoc_ky = schoolYears[i].semesterAmount
          year.canUpdate = schoolYears[i].canUpdate
          for(let j = 0; j < schoolYears[i].semesterAmount; j++){
            switch(j){
              case 0: {
                year.bat_dau_hoc_ky_1 = schoolYears[i].semesters[j].from_date
                year.ket_thuc_hoc_ky_1 = schoolYears[i].semesters[j].to_date
                break;
              }
              case 1: {
                year.bat_dau_hoc_ky_2 = schoolYears[i].semesters[j].from_date
                year.ket_thuc_hoc_ky_2 = schoolYears[i].semesters[j].to_date
                break;
              }
            }
          }
          this.desserts.push(year)
        }
        this.oldDesserts = JSON.parse(JSON.stringify(this.desserts));
      })
      .then( () => {
        this.checkExistKy12()
        if(this.existKy2){
          this.changeHeader(this.dataKy2)
          return
        }
        if(this.existKy1){
          this.changeHeader(this.dataKy1)
        }
      })
      .catch( () => {
          setTimeout(() => {
              this.$refs['toastMessage'].open('Lỗi khi lấy dữ liệu năm học', true)
          }, 0)
      })
    },
    openToastMessage(data){
      this.$refs['toastMessage'].open(data.mess, data.status)
    },
    generateSchoolYears(){
      var crYear = new Date().getFullYear()
      for(let i = 0; i < 10; i++){
        let str = crYear + '-' + (crYear + 1)
        crYear++
        this.schoolYears.push(str)
      }
    }
  },
};
</script>

<style scoped>

</style>