<template>
  <div class="SchoolUpdateDepartmentDialog">
    <v-dialog v-model="show" persistent max-width="600px" hide-overlay>
      <v-card>
        <v-toolbar color="primary" dark>
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
        <v-card-text>
          <v-container>
            <v-row>
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
                  :disabled="hardDisable"
                  item-text="name"
                >
                  <template slot="append-outer">
                    <v-btn v-if="!hardDisable" class="ml-0" icon @click="parentDepartment = 0">
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
                  :disabled="disableDepartmentType || hardDisable"
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
  name: "SchoolUpdateDepartmentDialog",
  components: {Loading, ToastMessage },
  data() {
    return {
      id: "",
      isCreate: false,
      valid: false,
      isEdit: false,
      promise: null,
        disableDepartmentType: false,
        disableDepartmentParent: false,
        hardDisable: false,
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
        this.$refs["Loading"].open();
        AppService.checkDisableUpdateParentAndTypeDepartment(department.id).then((res)=> {
            if (res.data.status === "OK" ) {
                this.hardDisable = false
            } else {
                this.hardDisable = true
            }
            this.$refs["Loading"].close();
        }).catch(()=> {
            this.$refs["Loading"].close();
        })
        this.title = title
        this.id = department.id
        this.isEdit = true
        this.departmentCode = department.code;
        this.departmentName = department.name;
        this.parentDepartment = null
        this.parentDepartment = department.parentID;
        this.departmentType = department.type;
        // if (this.departmentType !== "") {
        //   this.disableDepartmentType = true
        // }
        this.role = department.position;
        this.roleDescription = department.description
     
      this.show = true;
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
        id: this.id,
        code: this.departmentCode,
        name: this.departmentName,
        parentID: this.parentDepartment,
        departmentType: this.departmentType,
        employeeID: this.teacher,
        employeeName: employeeName,
        position: this.role,
        description: this.roleDescription,
      };
     

    let newDepartment = {
        id: this.id,
        code: result.code,
        name: result.name,
        parentID: result.parentID,
        employeeName: result.employeeName,
        employeeID: result.employeeID,
        position: result.position,
        positionName: this.roleMap[result.position],
        description: result.description,
        actions: "",
        type: result.departmentType,
        }
        // let department = {
        //   name: result.name,
        //   code: result.code,
        //   parentID: result.parentID,
        //   position: result.position,
        //   employeeID: result.employeeID,
        //   description: result.description,
        //   type: result.departmentType,
        // };
        AppService.updateDepartment(newDepartment).then(
        (res) => {
            if(res.data.status == 'OK'){
            this.show = false
            this.promise.resolve()
            } else {            
            this.$refs["ToastMessage"].open(
                res.data.message,
                true
            );
            }
        }
        ).catch(() => {
            this.show = false
            this.promis.reject()
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
          this.teacher = "";
          for (let teacher of data.data.data) {
            this.teacherList.push({
              id: teacher.id,
              name: teacher.name,
            });
          }
          if(this.teacherList.length > 0) {
            this.teacher = this.teacherList[0].id;
          }
        });
      }
    },
  },
};
</script>

<style scoped>
</style>
