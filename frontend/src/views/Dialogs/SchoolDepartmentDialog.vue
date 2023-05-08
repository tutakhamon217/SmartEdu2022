<template>
  <div class="SchoolDepartmentDialog">
    <v-dialog v-model="show" persistent max-width="600px">
      <v-card>
        <v-toolbar
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1">
          <v-toolbar-title>{{title}}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="close()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-form
          ref="form"
          v-model="valid"
          lazy-validation
        >
        <v-card-text class="mt-0 pt-0">
          <v-container class="mt-0 pt-0">
            <v-row class="mt-0 pt-0">
              <v-col cols="12" sm="6" md="6">
                <v-text-field
                  label="Mã đơn vị (*)"
                  required
                  :disabled="isEdit"
                  v-model="departmentCode"
                  :rules="[this.rule.maDonVi, this.rule.maDonViPattern, this.rule.required]"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-text-field
                  label="Tên đơn vị (*)"
                  v-model="departmentName"
                  :rules="[this.rule.tenDonVi, this.rule.required]"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-select
                  :items="departmentList"
                  label="Đơn vị cha"
                  v-model="parentDepartment"
                  item-value="id"
                  item-text="name"
                >
                <template slot="append-outer">
                  <v-btn class="ml-0" icon @click="parentDepartment = 0">
                    <v-icon>mdi-close-circle</v-icon>
                  </v-btn>
                </template>
                </v-select>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-select
                  :items="departmentTypeList"
                  label="Loại đơn vị (*)"
                  v-model="departmentType"
                  :disabled="disableDepartmentType"
                  :rules="[this.rule.required]"
                  item-value="id"
                  item-text="name"
                ></v-select>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-autocomplete
                  :items="teacherList"
                  label="Người phụ trách"
                  v-model="teacher"
                  item-value="id"
                  item-text="name"
                >
                  <template slot="no-data">
                    <div>Không có giáo viên trong đơn vị</div>
                  </template>
                </v-autocomplete>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-select
                  :items="roleList"
                  label="Chức vụ"
                  v-model="role"
                  item-value="id"
                  item-text="name"
                ></v-select>
              </v-col>
              <v-col cols="12" sm="12" md="12">
                <v-text-field
                  label="Mô tả nhiệm vụ"
                  v-model="roleDescription"
                  :rules="[this.rule.moTaNhiemVu]"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        </v-form>
        <v-card-actions>

          <v-spacer></v-spacer>
          <v-btn elevation="1" color="warning darken-1" text @click="close"> Hủy bỏ </v-btn>
          <v-btn elevation="1" color="primary darken-1" text @click="submit"> Lưu lại </v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <Loading ref="Loading"></Loading>
  </div>
</template>

<script>
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage.vue";


export default {
  name: "SchoolDepartmentDialog",
  components: {Loading, ToastMessage },
  data() {
    return {
      id: "",
      isCreate: false,
      valid: false,
      isEdit: false,
      promise: null,
      disableDepartmentType: false,
      count: 0,
      show: false,
      title: "",
      departmentCode: "",
      departmentName: "",
      parentDepartment: 0,
      departmentType: "",
      teacher: "",
      role: "",
      roleDescription: "",
      departmentList: [],
      roleList: [
        {id: 0, name:"Hiệu Trưởng"},
        {id: 1, name:"Trưởng phòng"},
        {id: 2, name:"Trưởng Khoa/Ban"},
        {id: 3, name:"Trưởng Tổ bộ môn"},
        {id: 4, name:"Trưởng bộ môn"},
      ],
      roleMap: {
        0: "Hiệu Trưởng",
        1: "Trưởng phòng",
        2: "Trưởng Khoa/Ban",
        3: "Trưởng Tổ bộ môn",
        4: "Trưởng bộ môn",
      },
      departmentTypeList: [],
      teacherList: [],
      rule: {
        required: value => !!value || 'Bắt buộc',
        maDonVi:  value => value.length <= 50 || 'Mã đơn vị không quá 50 ký tự',
        maDonViPattern: value => {
          if(value.includes(" ")) return "Mã đơn vị không được chứa khoảng trắng"
          const pattern = /^[a-zA-Z0-9]*$/
          return pattern.test(value) || 'Mã đơn vị không hợp lệ'
        },
        tenDonVi:  value => value.length <= 250 || 'Tên đơn vị không quá 500 ký tự',
        moTa: value => value.length <= 500 || 'Mô tả không quá 500 ký tự',
        moTaNhiemVu:  value => value.length <= 500 || 'Mô tả không quá 500 ký tự',
    },
    };
  },
  mounted () {
    this.initData()
  },
  methods: {
    initData() {
      this.$refs["Loading"].open();
      this.departmentList = [];
      this.departmentTypeList = [];
      let pro1 = AppService.getAllDepartmentWithType()
      let pro2 = AppService.getAllDepartmentType()
      
      pro1.then(
        (data) => {
          for (let department of data.data.data) {
            this.departmentList.push({
              id: department.id,
              name: department.name,
              type: department.type
            });
          }
        },
        () => {}
      );

      pro2.then(
        (data) => {
          for (let departmentType of data.data.data) {
            this.departmentTypeList.push({
              id: departmentType.code,
              name: departmentType.name,
            });
          }
        },
        () => {}
      );

      Promise.all([pro1, pro2]).then(()=>{
        this.$refs["Loading"].close();
      })
    },
    open(department, title) {
      this.valid = true
      this.title = title
      this.disableDepartmentType = false

      this.isCreate = true
      this.isEdit = false
      this.departmentCode = ""
      this.departmentName = ""
      this.parentDepartment = 0
      this.departmentType = ""
      this.teacher = ""
      this.role = ""
      this.roleDescription = ""
    
      this.show = true;
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    close() {
      this.$refs['form'].resetValidation()
      this.show = false;
      this.promise.reject();
    },
    submit() {
      this.valid = this.$refs.form.validate()
      if (this.valid === false) {
        return
      }
      let employeeName = ""
      for(let teacherTemp of this.teacherList) {
        if(teacherTemp.id === this.teacher) {
          employeeName = teacherTemp.name
        }
      }
      let result = {
        code: this.departmentCode,
        name: this.departmentName,
        parentID: this.parentDepartment,
        departmentType: this.departmentType,
        employeeID: this.teacher,
        employeeName: employeeName,
        position: this.role,
        description: this.roleDescription,
      };
        // this.$refs["Loading"].open();
        let department = {
            name: result.name,
            code: result.code,
            parentID: result.parentID,
            position: result.position,
            employeeID: result.employeeID,
            description: result.description,
            type: result.departmentType,
          };

          AppService.addNewDepartment(department).then(
            (res) => {
              if(res.data.status == 'OK'){
                this.$refs["ToastMessage"].open(
                  "Tạo khoa ban mới thành công!",
                  false
                );
                this.$refs["Loading"].close();
                this.show = false
                this.promise.resolve();
              } else {
                this.$refs["ToastMessage"].open(
                  res.data.message,
                  true
                );
                // this.$refs["Loading"].close();
              }
              // this.$refs["Loading"].close();
            }
          ).catch((res) => {
              this.$refs["ToastMessage"].open(res.data.message, true);
              // this.$refs["Loading"].close();
              // this.$refs["Loading"].close();
              this.show = false;
          });
      // this.promise.resolve(result);
      // this.show = false;
    },
  },
  watch: {
    parentDepartment(newparentDepartment) {
      let currentType = ""
      for(let i of this.departmentList) {
        if (i.id === newparentDepartment) {
          currentType = i.type
          break
        }
      }
      if (currentType === "") {
        this.departmentType = ""
        this.disableDepartmentType = false
      } else {
        this.departmentType = currentType
        this.disableDepartmentType = true
        AppService.getTeachersOfRootParent(newparentDepartment).then((data) => {
          this.teacherList = [];
          for (let teacher of data.data.data) {
            this.teacherList.push({
              id: teacher.id,
              name: teacher.name,
            });
          }
        });
      }
    },
  },
};
</script>

<style scoped>
</style>
