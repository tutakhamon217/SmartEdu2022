<template>
  <v-container class="StudentLearningRate ma-0 pa-0" fluid height="100%">
    <v-row v-show="isShow" class="fill-height">
      <!-- <v-spacer></v-spacer> -->
      <v-col cols="12" class="ma-4">
        <v-row class="mb-3">
          <v-card width="98%">
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
            <v-row class="mx-2">
              <v-col>
                <v-select
                  :items="hocKyList"
                  label="Học kỳ"
                  v-model="hocKy"
                  item-value="id"
                  item-text="name"
                ></v-select>
              </v-col>
              <v-col>
                <v-select
                  :items="lopHocList"
                  label="Lớp học"
                  v-model="lopHoc"
                  item-value="code"
                  item-text="name"
                ></v-select>
              </v-col>
            </v-row>
              <v-card-actions>
              <v-spacer></v-spacer>
                <v-btn @click="search()" dark color="primary">
                  <v-icon>mdi-magnify</v-icon>
                  Tìm kiếm
                </v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-row>
        <v-row>
          <v-card width="98%" class="mt-3">
          <v-toolbar dense
            dark
            class="font-weight-bold"
            color="primary lighten-1">
                <v-toolbar-title style="width: 100%;align-items: center;" class="d-flex">
                    <span style="color: white">Đánh giá học lực</span>
                    <v-spacer></v-spacer>
                    <v-btn v-if="!isUpdating && hasData" @click="update" color="white" class="float-right" dark>
                      <p class="ma-0" style="color: black">Cập nhật</p>
                    </v-btn>
                    <v-btn v-if="isUpdating && hasData" @click="isUpdating=false" color="white" class="float-right mr-3" dark>
                      <p class="ma-0" style="color: black">Hủy</p>
                    </v-btn>
                    <v-btn v-if="isUpdating && hasData" @click="save()" color="white" class="float-right" dark>
                      <p class="ma-0" style="color: black">Lưu</p>
                    </v-btn>
                </v-toolbar-title>
            </v-toolbar>
          <v-expand-transition>
          
          <div v-show="hasData">
             <v-data-table
                hide-default-header
                :headers="headers"
                :items="tableData"
                :items-per-page="10"
                :custom-sort="sortByName"
                :header-props="{ sortIcon: null }"
                :footer-props="{
                    'items-per-page-text': 'Số dòng mỗi trang:'
                }"
                class="overflow-hidden"
            >

              <template slot="header" :headers="headers">
              <thead class="v-data-table-header">
                    <tr>
                        <th v-for="header in headers" role="columnheader" scope="col" class="text-start"
                            :key="header.value">
                            <p>{{header.text}}</p>
                        </th>
                    </tr>
              </thead>
              <thead class="v-data-table-header">
                <tr>
                    <th v-for="header in headers" role="columnheader" scope="col" class="text-start"
                        :key="header.value">
                        <p v-if="header.type===1">HS{{header.coefficient}}</p>
                        <p v-if="header.type===2">Nhận xét</p>
                        <p v-if="header.value === 'dtb' || header.value ==='hocLuc'">
                        <span v-if="hocKy === 0">Cả năm</span>
                        <span v-if="hocKy === 1">Học kỳ 1</span>
                        <span v-if="hocKy === 2">Học kỳ 2</span>
                        </p>
                    </th>
                </tr>
              </thead>
              </template>
              <template v-slot:item.maHocSinh="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.maHocSinh, 6)
                    }}</span>
                  </template>
                  <span>{{ item.maHocSinh }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.tenHocSinh="{item}">
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <span v-bind="attrs" v-on="on">{{
                    formatTextTooltip(item.tenHocSinh, 30)
                  }}</span>
                </template>
                <span>{{ item.tenHocSinh }}</span>
              </v-tooltip>
              </template>

              <template v-slot:item.hocLuc="{item}">
                <v-select
                  :disabled="!isUpdating || isNaN(item.dtb)" 
                  :items="hocLucList"
                  v-model="item.hocLuc"
                  item-value="code"
                  item-text="name"
                ></v-select>
              </template>
            </v-data-table>
          </div>
          </v-expand-transition>
          <v-expand-transition>
            <v-card-text v-show="!hasData" style="display: flex">
                <v-spacer></v-spacer>
                    <div style="color: rgba(0, 0, 0, 0.6);"> Danh sách học sinh rỗng</div>
                <v-spacer></v-spacer>
            </v-card-text>
          </v-expand-transition>
          </v-card>
        </v-row>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
    <SchoolDepartmentDialog
      ref="SchoolDepartmentDialog"
    ></SchoolDepartmentDialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <Loading ref="Loading"></Loading>
    <ConfirmDialog ref="confirmDialog" txtTitle="Cập nhật thông tin học lưc" question="Bạn đã chắc chắn muốn cập nhật thông tin đánh giá học lực này không?"/>
  </v-container>
</template>

<script>
// import DataTable from "@/components/DataTable.vue";
import SchoolDepartmentDialog from "@/views/Dialogs/SchoolDepartmentDialog.vue";
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default {
  name: "StudentLearningRate",
  components: { SchoolDepartmentDialog, ToastMessage, Loading, ConfirmDialog },
  mounted() {
    if (!this.$store.getters['user'].roles.includes('ROLE_GVCN')) {
      this.$refs["ToastMessage"].open("Bạn không phải là giáo viên chủ nhiệm", true);
      this.isShow = false
      return
    }
    this.isShow = true
    this.loadData()
  },
  data: () => ({
    isShow: true,
    isInSemesterTime: false,
    isUpdating: false,
    hasData: false,
    isDelete: false,
    isEdit: true,
    headers: [
    ],
    hocKy: "",
    hocKyList: [{
      id: 0,
      name: "Cả năm"
    }],
    lopHoc: "",
    lopHocList: [],
    tableData: [],
    subjectClassList: [],
    scoreMap: new Map(),
    hocSinhList: [],
    hocLucList: [],
    studentRatedMap: new Map(),
    allNotFull: false
  }),
  computed: {
  },
  methods: {
    sortByName(items, index, isDescending) {
      items.sort((a, b) => {
        return a.tenHocSinh.split(" ").slice(-1)[0].toLowerCase().localeCompare(b.tenHocSinh.split(" ").slice(-1)[0].toLowerCase());
      });
      let countI = 0
      for(let ii of items) {
        countI += 1
        ii.stt = countI
      }
      return items;
    },
    loadData () {
      this.lopHocList = []
      this.hocKyList = [{
        id: 0,
        name: "Cả năm"
      }]
      
      this.$refs["Loading"].open();
      let pro1 = AppService.getAmountOfSemester(this.$store.getters["year"])
      pro1.then((data)=>{
        this.hocKyList = [{
                            id: 0,
                            name: "Cả năm"
                          }]
          for( let i of [...Array(data.data.semesterAmount).keys()]) {
              this.hocKyList.push({
                name: "Học kỳ " + (i+1),
                id: i+1
            })
          }
          if (this.hocKyList.length > 0) {
            this.hocKy = this.hocKyList[0].id
          }
      })
      let pro2 = AppService.getClassroomByCurrentTeacherAndYear(this.$store.getters["year"])
      pro2.then((data)=>{
        if (data.data.data.length === 0 ) {
          this.$refs["ToastMessage"].open(`Danh sách lớp phụ trách trong năm ${this.$store.state.year} rỗng`, true);
        } else {
          for(let classRoom of data.data.data) {
              this.lopHocList.push({
                name: classRoom.name,
                code: classRoom.code,
                id: classRoom.id
            })
          }
          if (this.lopHocList.length > 0) {
            this.lopHoc = this.lopHocList[0].code
          } else {
            this.lopHoc = ""
            this.$refs["ToastMessage"].open("Không có lớp học trong thời gian này", true);
          }
        }
      }, () => {
        this.$refs["ToastMessage"].open("Lấy dữ liệu lớp học lỗi", true);
      })

      let pro3 = AppService.getAllHocLuc()
      pro3.then((data)=>{
        this.hocLucList = []
        for(let i of data.data.data) {
          this.hocLucList.push({
            name: i.name,
            code: i.code
          })
        }
      })
      Promise.all([pro1, pro2, pro3]).then(()=>{
        this.$refs["Loading"].close();
      })
    },

    search () {
      this.isUpdating = false
      this.scoreMap = new Map()
      this.hasData = false
      this.$refs["Loading"].open();
      this.tableData = []

      let pro1 = AppService.getAllStudentOfClassDanhGiaHocLuc({year: this.$store.getters["year"], class_code: this.lopHoc })
      this.tableData = []
      let count  = 0
      pro1.then((data)=> {
        this.hocSinhList = []
        for(let student of data.data.data.data) {
          count += 1
          this.hocSinhList.push({
            stt: count,
            maHocSinh: student.code,
            tenHocSinh: student.fullName,
          })
        }
      })

      let class_id_search = ""
      for(let i of this.lopHocList) {
        if (i.code === this.lopHoc) {
          class_id_search = i.id
          break
        }
      }

      let pro2 = AppService.getSubjectClassByClassIdAndClassCodeSemester({classId: class_id_search, semester: this.hocKy })
      pro2.then((data)=>{
        this.subjectClassList = []
        for(let i of data.data.data.subjectClass) {
          this.subjectClassList.push({
            name: i.name,
            code: i.code,
            id: i.subject_id,
            coefficient: i.coefficient,
            sub_type: i.sub_type
          })
        }
      })


      let pro3 = AppService.getAvgScore({
        classCode: this.lopHoc,
        year: this.$store.getters["year"],
        semester: this.hocKy
      })
      pro3.then((data)=>{
        
        for (let i of data.data.data.scoreDetail) {
          if (i.avg_score === "" || i.avg_score === null || i.avg_score === undefined) {
            this.scoreMap.set(i.student_code+"@@"+i.subject_code, "_")
          } else {
            this.scoreMap.set(i.student_code+"@@"+i.subject_code, i.avg_score)
          }
          
        }
      })
      let tempHocKy = this.hocKy
      if (tempHocKy === 0) {
        tempHocKy = 2
      }
      let pro4 = AppService.getXepLoai({
        classCode: this.lopHoc,
        year: this.$store.getters["year"],
        semester: tempHocKy
      })
      pro4.then((data)=>{
        for (let i of data.data.data.scoreDetail) {
          if (i.value === "" || i.value === null || i.value === undefined) {
            this.scoreMap.set(i.student_code+"@@"+i.subject_code, "_")
          } else {
            this.scoreMap.set(i.student_code+"@@"+i.subject_code, i.value)
          }
          
        }
      })
      
      let pro5 = AppService.getAllStudentRated({
        classCode: this.lopHoc,
        year: this.$store.getters["year"],
        semester: this.hocKy
      })

      pro5.then((data)=>{
        this.studentRatedMap = new Map()
        for(let i of data.data.data.studentRate) {
          this.studentRatedMap.set(i.student_code, i.academic_ability)
        }
      })

      
      Promise.all([pro1, pro2, pro3, pro4, pro5]).then(()=>{
        this.$refs["Loading"].close();
        this.realoadHeader()
        this.reloadScore()
        if (this.tableData.length > 0 ){
          this.hasData = true
        } else {
          this.hasData = false
        }
      })
    },
    realoadHeader () {
      this.headers = [{
        text: "STT",
        align: "start",
        sortable: false,
        value: "stt",
        type: 0
        },
        { text: "Mã học sinh", value: "maHocSinh" , width: '5%', type: 0 },
        { text: "Tên học sinh", value: "tenHocSinh", width: '10%', type: 0 },
      ]
      for (let i of this.subjectClassList) {
        if (i.sub_type === 0) {
          this.headers.push({
            text: i.name,
            value: i.code,
            coefficient: i.coefficient,
            id: i.id,
            type: 1,

          })
        } else {
          this.headers.push({
            text: i.name,
            value: i.code,
            coefficient: i.coefficient,
            id: i.id,
            type: 2,
          })
        }

      }
      this.headers.push({ text: "Điểm trung bình", value: "dtb", type: 0})
      this.headers.push({ text: "Học lực", value: "hocLuc", type: 0})
    },
    reloadScore () {
      this.allNotFull = false
      this.tableData = []
      for(let i of this.hocSinhList) {
        let sumScore = 0
        let countScore = 0
        let hocSinhScoreInfor = {}
        hocSinhScoreInfor.stt = i.stt,
        hocSinhScoreInfor.maHocSinh = i.maHocSinh,
        hocSinhScoreInfor.tenHocSinh = i.tenHocSinh
        let notFull = false
        for(let j of this.subjectClassList) {
          // tinh diem
          if (j.sub_type === 0 ) {
            if(this.scoreMap.has(i.maHocSinh+"@@"+j.code)) {
              let avgScore = this.scoreMap.get(i.maHocSinh+"@@"+j.code)
              if (avgScore === "_") {
                notFull = true
                hocSinhScoreInfor[j.code] = "*"
              } else {
                hocSinhScoreInfor[j.code] = avgScore
              }
              if (!isNaN(avgScore)) {
                sumScore += avgScore*j.coefficient
                countScore += j.coefficient
              }
            } else {
              hocSinhScoreInfor[j.code] = "_"
              notFull = true
            }
          } else {
            if(this.scoreMap.has(i.maHocSinh+"@@"+j.code)) {
              let rateScore = this.scoreMap.get(i.maHocSinh+"@@"+j.code)
              hocSinhScoreInfor[j.code] = rateScore
            } else {
              hocSinhScoreInfor[j.code] = "_"
            }
          }
        }
        if (countScore === 0 || notFull) {
          hocSinhScoreInfor["dtb"] = "_"
          this.allNotFull = true
        } else {
          hocSinhScoreInfor["dtb"] = Math.round(sumScore/countScore * 10) / 10
        }
        if(this.studentRatedMap.has(i.maHocSinh)) {
          hocSinhScoreInfor["hocLuc"] = this.studentRatedMap.get(i.maHocSinh)
        } else {
          hocSinhScoreInfor["hocLuc"] = ""
        }
        this.tableData.push(hocSinhScoreInfor)
      }
    },
    formatTextTooltip(item, max) {
      if (item.length >= max) {
        return item.substring(0, max) + "...";
      }
      return item;
    },
    update() {
      this.$refs["Loading"].open()
      AppService.checkSemesterStartEnd(this.$store.getters["year"], this.hocKy).then((res)=>{
        if (res.data.status === "OK") {
          this.isUpdating = true
        } else {
          this.$refs["ToastMessage"].open(res.data.message, true);
        }
        this.$refs["Loading"].close()
      }).catch((res)=>{this.$refs["ToastMessage"].open(res.data.message, true);this.$refs["Loading"].close()})
    },
    save (){
      this.$refs['confirmDialog'].open().then(()=> {
        let updateData = []
        for(let i of this.tableData) {
          updateData.push({
            studentCode: i.maHocSinh,
            academicAbility: i.hocLuc
          })
        }
        let datatmp = {
          classCode: this.lopHoc,
          year: this.$store.getters["year"],
          semester: this.hocKy,
          rateList: updateData
        }
        this.$refs["Loading"].open()
        AppService.updateStudentRate(datatmp).then((res)=>{
          this.$refs["Loading"].close()
          if(res.data.status === "OK") {
            this.$refs["ToastMessage"].open(`Cập nhật thành công`, false);
            this.isUpdating = false
            this.$refs["Loading"].close()
          } else {
            this.$refs["ToastMessage"].open(res.data.message, true);
          }
        }).catch((res)=>{
          this.$refs["ToastMessage"].open(res.data.message, true);
          this.$refs["Loading"].close()
        })
      }, ()=>{})
    }
  },
  watch: {
    '$store.state.year'(){
      this.$refs["Loading"].open()
      this.lopHocList = []
      let pro1 = AppService.getClassroomByCurrentTeacherAndYear(this.$store.getters["year"])
      pro1.then((data)=>{
          if (data.data.data.length === 0 ) {
            this.$refs["ToastMessage"].open(`Danh sách lớp phụ trách trong năm ${this.$store.state.year} rỗng`, true);
          } else {
            for(let classRoom of data.data.data) {
                this.lopHocList.push({
                  name: classRoom.name,
                  id: classRoom.id,
                  code: classRoom.code,
              })
            }
            if (this.lopHocList.length > 0) {
              this.lopHoc = this.lopHocList[0].code
            } else {
              this.lopHoc = ""
              this.$refs["ToastMessage"].open("Không có lớp học trong thời gian này", true);
            }
          }
        }, () => {
          this.$refs["ToastMessage"].open("Lấy dữ liệu lớp học lỗi", true);
        })
        this.hocKyList = []
      let pro2 = AppService.getAmountOfSemester(this.$store.getters["year"])
      pro2.then((data)=>{
          this.hocKyList = [{
              id: 0,
              name: "Cả năm"
            }]
          for( let i of [...Array(data.data.semesterAmount).keys()]) {
              this.hocKyList.push({
                name: "Học kỳ " + (i+1),
                id: i+1
            })
          }
      })
      Promise.all([pro1, pro2]).then(()=>{this.search();this.$refs["Loading"].close()})
      
    },
    hocKy () {
      this.search()
    },
    lopHoc () {
      this.search()
    }
  }
};
</script>

<style scoped lang="scss">
</style>
