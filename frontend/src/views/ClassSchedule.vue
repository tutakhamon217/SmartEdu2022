<template>
  <v-container class="ClassInformation ma-0 pa-0" fluid height="100%">
    <v-row class="fill-height">
      <!-- <v-spacer></v-spacer> -->
      <v-col cols="11" class="ma-4">
        <v-row class="mb-3">
          <v-card width="100%">
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
            <v-row class="ma-2 mt-1">
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
            <v-row class="mx-2 mt-1">
              <v-col>
                <v-text-field
                  v-model="classInformation"
                  label="Thông tin lớp học"
                ></v-text-field>
              </v-col>
              <v-col> </v-col>
            </v-row>
            <v-row class="mb-3 mt-0">
              <v-col>
                <v-btn @click="searchClass()" dark color="primary">
                  <v-icon>mdi-magnify</v-icon>
                  Tìm kiếm
                </v-btn>
              </v-col>
            </v-row>
          </v-card>
        </v-row>

        <v-row>
          <v-card width="100%">
            <v-toolbar
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
            >
              <v-toolbar-title class="text-center"
                >Kết quả tìm kiếm</v-toolbar-title
              >
              <v-spacer></v-spacer>
              <v-btn
                class="mr-4"
                depressed
                color="primary"
                @click="exportClass()"
              >
                Xuất excel
              </v-btn>
            </v-toolbar>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                class="mr-4"
                depressed
                color="primary"
                @click="addNewClass()"
              >
                Thêm mới
              </v-btn>
              
              <v-spacer></v-spacer>
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
    <ClassInformationDialog ref="ClassInformationDialog"></ClassInformationDialog>
  </v-container>
</template>

<script>
import DataTable from "@/components/DataTable.vue";
import SchoolDepartmentDialog from "@/views/Dialogs/SchoolDepartmentDialog.vue";
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";
import ClassInformationDialog from "@/views/Dialogs/ClassInformationDialog.vue"

export default {
  name: "ClassInformation",
  components: { DataTable, SchoolDepartmentDialog, ToastMessage, Loading, ClassInformationDialog },
  created() {},
  data: () => ({
    maKhoi: "",
    maKhoa: "",
    classInformation: "",
    search: "",
    headersClass: [
      {
        text: "STT",
        align: "start",
        sortable: false,
        value: "stt",
      },
      { text: "Mã lớp", value: "classCode" },
      { text: "Tên lớp", value: "className" },
      { text: "Khối", value: "tenKhoi" },
      { text: "Mã lớp", value: "classCode" },
      { text: "Môn chuyên", value: "monChuyen" },
      { text: "Giáo viên chủ nhiệm", value: "giaoVienChuNhiem" },
      { text: "Thao tác", value: "actions" },
    ],
     khoiList: [
      {
        name: "Khối 10",
        id: "10",
      },
      {
        name: "Khối 11",
        id: "11",
      },
      {
        name: "Khối 12",
        id: "12",
      },
    ],
    khoaList: [
      {
        name: "Khoa 1",
        id: "1",
      },
      {
        name: "Khoa 2",
        id: "2",
      },
      {
        name: "Khối 3",
        id: "3",
      },
    ],
    searchResult: [],
  }),
  computed: {},
  mounted() {},
  methods: {
    searchClass() {
      let countStt = 0;
      // this.$refs["Loading"].open();
      AppService.searchClass(this.maKhoi, this.maKhoa, this.classInformation)
        .then(
          (data) => {
            this.searchResult = [];
            for (let classInformation of data.data) {
              countStt += 1;
              this.searchResult.push({
                stt: countStt,
                classCode: classInformation.classCode,
                className: classInformation.className,
                tenKhoi:  classInformation.tenKhoi,
                khoaBan: classInformation.khoaBan,
                monChuyen: classInformation.monChuyen,
                giaoVienChuNhiem: classInformation.giaoVienChuNhiem,
                actions: "",
              });
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
    addNewClass() {
      this.$refs["ClassInformationDialog"]
        .open(undefined, "Thêm mới lớp")
        .then((result) => {
          let classInformation = {
            classCode: result.classCode,
            className: result.className,
            tenKhoi: result.tenKhoi,
            khoaBan: result.khoaBan,
            monChuyen: result.monChuyen,
            giaoVienChuNhiem : result.giaoVienChuNhiem,
          };
          AppService.addClass(classInformation).then(
            () => {
              this.$refs["ToastMessage"].open("Tạo lớp học thành công!", false);
              this.searchResult.push({
                stt: this.searchResult.length + 1,
                classCode: classInformation.classCode,
                className: classInformation.className,
                tenKhoi:  classInformation.tenKhoi,
                khoaBan: classInformation.khoaBan,
                monChuyen: classInformation.monChuyen,
                giaoVienChuNhiem: classInformation.giaoVienChuNhiem,
                actions: "",
              });
            },
            () => {
              this.$refs["ToastMessage"].open(
                "Create new class fail!",
                true
              );
            }
          );
        });
    },
    editClass (classInformation){
      this.$refs["ClassInformationDialog"].open(classInformation, "Thêm mới lớp")
    },
    deleteClass (classInformation){
      let newsearchResult = []
      for (let classElement of this.searchResult) {
        if (classElement.classCode !== classInformation.classCode) {
          newsearchResult.push(classElement)
        }
      }
      this.searchResult = newsearchResult
    }
  },
};
</script>

<style scoped lang="scss">
</style>
