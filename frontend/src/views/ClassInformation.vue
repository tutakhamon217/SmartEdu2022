<template>
  <v-container class="ClassInformation ma-0 pa-0" fluid height="100%" >
    <v-row class="fill-height">
      <!-- <v-spacer></v-spacer> -->
      <v-col cols="12" class="ma-4">
        <v-row class="mb-3">
          <v-card width="97%">
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
                  :items="khoiList"
                  label="Khối"
                  v-model="maKhoi"
                  item-value="id"
                  item-text="name"
                ></v-select>
              </v-col>
              <v-col>
                <v-select
                  :items="khoaList"
                  label="Khoa/Ban"
                  v-model="maKhoa"
                  item-value="id"
                  item-text="name"
                ></v-select>
              </v-col>
            </v-row>
            <v-row class="mx-2 mt-0">
              <v-col>
                <v-text-field
                  v-model="classCodeSearch"
                  label="Mã lớp"
                ></v-text-field>
              </v-col>
              <v-col>
                <v-text-field
                  v-model="classNameSearch"
                  label="Tên lớp"
                ></v-text-field>
              </v-col>
            </v-row>
              <v-card-actions>
              <v-spacer></v-spacer>
                <v-btn @click="searchClass()" dark color="primary">
                  <v-icon>mdi-magnify</v-icon>
                  Tìm kiếm
                </v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-row>

        <v-row>
          <v-card class="mt-3" width="97%">
            <v-toolbar
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
            >
              <v-toolbar-title class="text-center"
                >Danh sách lớp học</v-toolbar-title
              >

            <v-spacer></v-spacer>
              <v-btn @click="addNewClass()" color="white" class="float-right" dark>
                <v-icon color="green">mdi-plus-outline</v-icon>
                <p class="ma-0" style="color: black">Tạo mới</p>
              </v-btn>
            </v-toolbar>

              <v-expand-transition>
              <v-data-table
                  v-show="searchResult.length > 0"
                  :headers="this.headersClass"
                  :items="this.searchResult"
                  :items-per-page="5"
                  disable-sort
                  :footer-props="{
                      'items-per-page-text': 'Số dòng mỗi trang:',
                  }"
              >
              <template slot="no-data">
                Danh sách lớp học rỗng
              </template>
              
              <template v-slot:item.className="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.className, 50)
                    }}</span>
                  </template>
                  <span>{{ item.className }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.teacherName="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.teacherName, 50)
                    }}</span>
                  </template>
                  <span>{{ item.teacherName }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.classCode="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.classCode, 50)
                    }}</span>
                  </template>
                  <span>{{ item.classCode }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.monChuyenName="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.monChuyenName, 50)
                    }}</span>
                  </template>
                  <span>{{ item.monChuyenName }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.khoaName="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.khoaName, 50)
                    }}</span>
                  </template>
                  <span>{{ item.khoaName }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.khoiName="{item}">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.khoiName, 50)
                    }}</span>
                  </template>
                  <span>{{ item.khoiName }}</span>
                </v-tooltip>
              </template>
              <template v-slot:item.actions="{ item }">
                  <v-icon small class="mr-2" @click="editClass(item)"> mdi-pencil </v-icon>
                  <v-icon small class="mr-2" @click="deleteClass(item)"> mdi-delete </v-icon>
              </template>
              <template v-slot:footer.page-text="props">
                  {{ props.pageStart }}-{{ props.pageStop }} của {{ props.itemsLength }} kết quả
              </template>
            </v-data-table>
            </v-expand-transition>
            <v-card-text v-show="searchResult.length === 0" style="display: flex">
                <v-spacer></v-spacer>
                    <div style="color: rgba(0, 0, 0, 0.6); font-weight: bold; font-size:17px"> Không tìm thấy danh sách lớp học</div>
                <v-spacer></v-spacer>
            </v-card-text>
            <v-card-actions>
            </v-card-actions>
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
    <ClassInformationDialogCreate ref="ClassInformationDialogCreate"></ClassInformationDialogCreate>
    <ClassInformationUpdate ref="ClassInformationUpdate"></ClassInformationUpdate>
    <ConfirmDialog ref="confirmDialog" txtTitle="Xóa lớp học" question="Bạn đã chắc chắn muốn xóa lớp học này không?"/>
  </v-container>
</template>

<script>

import SchoolDepartmentDialog from "@/views/Dialogs/SchoolDepartmentDialog.vue";
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";
import ClassInformationDialogCreate from "@/views/Dialogs/ClassInformationDialogCreate.vue"
import ClassInformationUpdate from "@/views/Dialogs/ClassInformationUpdate.vue"
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default { 
  name: "ClassInformation",
  components: { SchoolDepartmentDialog, ToastMessage, Loading, ClassInformationDialogCreate, ClassInformationUpdate, ConfirmDialog },
  created() {
  
  },
  data: () => ({
    isDelete: true,
    isEdit: true,
    maKhoi: "",
    maKhoa: "",
    classInformation: "",
    classCodeSearch: "",
    classNameSearch: "",
    headersClass: [
      {
        text: "STT",
        align: "start",
        sortable: false,
        value: "stt",
      },
      { text: "Mã lớp", value: "classCode" },
      { text: "Tên lớp", value: "className" },
      { text: "Khối", value: "khoiName" },
      { text: "Khoa Ban", value: "khoaName" },
      { text: "Môn chuyên", value: "monChuyenName" },
      { text: "Giáo viên chủ nhiệm", value: "teacherName" },
      { text: "Thao tác", value: "actions" },
    ],
    khoiList: [],
    khoaList: [],
    searchResult: [],
  }),
  computed: {},
  mounted() {
    let countReady = 0
    this.$refs["Loading"].open();
    AppService.getAllGradeLevel().then((data)=>{
      this.khoiList = []
      this.khoiList.push({
        id: "",
        name: "Tất cả",
      })
      for(let gradeLevel of data.data.data) {
        this.khoiList.push({
          id: gradeLevel.id,
          name:gradeLevel.name
        })
      }
      countReady += 1
      if (countReady == 2) {
        this.$refs["Loading"].close();
      }
    }).catch(()=>{})
    AppService.getAllDepartmentForClass().then((data)=>{
      this.khoaList = []
      this.khoaList.push({
        id: "",
        name: "Tất cả",
      })
      for(let khoa of data.data.data) {
        this.khoaList.push({
          id: khoa.id,
          name: khoa.code + " - " + khoa.name
        })
      }
      countReady +=1
      if (countReady == 2) {
        this.$refs["Loading"].close();
        
      }
    }).catch(()=>{})
    this.searchClass()
  },
  methods: {
    searchClass() {
      
      let years = this.$store.getters["year"]
      this.$refs["Loading"].open();
      this.searchResult = [];
      AppService.searchClass(years, this.classCodeSearch, this.classNameSearch, this.maKhoi, this.maKhoa).then((data) => {
            let countStt = 0;
            for (let classInformation of data.data.data.content) {
              countStt += 1
              this.searchResult.push({
                stt: countStt,
                id: classInformation.id,
                classCode: classInformation.code,
                className: classInformation.name,
                khoiId:  classInformation.gradeLevel.id,
                khoiName: classInformation.gradeLevel.name,
                khoaID: classInformation.department.id,
                khoaName: classInformation.department.name,
                monChuyenID: classInformation.specialize.id,
                monChuyenName: classInformation.specialize.name,
                teacherID: classInformation.teacher.id,
                teacherName: classInformation.teacher.name,
                actions: "",
              });
            }
            this.searchResult.sort(function(a, b) {
              return a.classCode.localeCompare(b.classCode)
            })
            countStt = 0
            for(let ii of this.searchResult) {
              countStt += 1
              ii.stt = countStt
            }
            if (this.searchResult.length === 0) {
              this.$refs["ToastMessage"].open("Danh sách lớp học rỗng", true);
            }
          },
          () => {
            this.$refs["ToastMessage"].open("Danh sách lớp học rỗng", true);
          }
        )
        .then(() => {
          this.$refs["Loading"].close();
        });
    },
    formatTextTooltip(item, max) {
      if (item === null || item === undefined) {
        return ""
      }
      if (item.length >= max) {
        return item.substring(0, max) + "...";
      }
      return item;
    },
    addNewClass() {
      this.$refs["ClassInformationDialogCreate"]
        .open()
        .then((message) => {
          this.$refs["ToastMessage"].open(message, false);
          this.searchClass()
        });
    },
    editClass (classInformation){
      this.$refs["ClassInformationUpdate"].open(classInformation).then((message) => {
          this.$refs["ToastMessage"].open(message, false);
          this.searchClass()
        });
    },
    deleteClass (classInformation){
      this.$refs['confirmDialog'].open().then(() => {
      AppService.deleteClass(classInformation.classCode).then((res)=>{
        if (res.data.status === "OK") {
          this.searchResult = this.searchResult.filter((resultElement)=> {
            return classInformation.id !== resultElement.id
          })
          this.$refs["ToastMessage"].open(res.data.message, false);
        } else {
          this.$refs["ToastMessage"].open(res.data.message, true);
        }
      },()=> {
        this.$refs["ToastMessage"].open("Xóa lớp học thất bại", true);
      }).catch(()=>{this.$refs["ToastMessage"].open("Xóa lớp học thất bại", true);})
      })
    }
  },
  watch: {
    khoaID(newparentDepartment) {
      AppService.getMonChuyen(newparentDepartment).then((data) => {
        this.monChuyenList = [];
        for (let monChuyen of data.data.data) {
          this.monChuyenList.push({
            id: monChuyen.id,
            name: monChuyen.name,
          });
        }
      });
    },
    '$store.state.year'(){
      this.searchClass()
    }
  },
};
</script>

<style scoped lang="scss">
</style>
