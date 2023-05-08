<template>
  <v-dialog v-model="show" max-width="650px">
    <v-card>
        <v-toolbar
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1">
          <v-toolbar-title>{{ formTitle }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="close()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>

      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12" md="6">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="editedIndex === -1 ? on: null"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Năm học (*)"
                    readonly
                    v-model="editedItem.nam_hoc"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll;">
                  <v-list-item
                    v-for="(item, index) in schoolYears"
                    :key="index"
                    @click="editedItem.nam_hoc = item"
                  >
                    <v-list-item-title>{{ item }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-col cols="12" md="6">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Số học kỳ (*)"
                    v-model="editedItem.so_hoc_ky"
                    readonly
                  >
                  </v-text-field>
                </template>
                <v-list>
                  <v-list-item
                    v-for="(n, index) in 2"
                    :key="index"
                    @click="editedItem.so_hoc_ky = n"
                  >
                    <v-list-item-title>{{ n }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-card
              width="100%"
              elevation="1"
              style="position: relative; border: 1px solid grey"
            >
              <v-row style="position: absolute; top: -1px; left: 40px">
                <v-card-text
                  style="color: black; background-color: white; padding: 0px"
                  >Thông tin chi tiết</v-card-text
                >
              </v-row>
              <v-form ref="formDetails">
                <v-row v-if="showDatePickerLines.ky1" class="ma-4">
                  <v-row style="width: 100%">
                    <v-col cols="12" md="6">
                      <v-menu offset-y
                      min-width="auto"
                      transition="scale-transition"
                      ref="ngayBatDau1"
                      v-model="picker11"
                      :close-on-content-click="false">
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="editedItem.bat_dau_hoc_ky_1"
                            label="Bắt đầu học kỳ I"
                            readonly
                            v-on="on"
                            :rules="validateBatDau1"
                            v-bind="attrs"
                            append-icon="mdi-calendar"
                          >
                          </v-text-field>
                        </template>
                        <v-date-picker
                          locale="vi-VN"
                          style="width: 100%"
                          v-model="picker.picker11"
                          :active-picker.sync="activeBatDau1"
                          @change="ngayBatDau1Save"
                        ></v-date-picker>
                      </v-menu>
                    </v-col>
                    <v-col cols="12" md="6">
                      <v-menu offset-y
                      min-width="auto"
                      transition="scale-transition"
                      ref="ngayKetThuc1"
                      v-model="picker12"
                      :close-on-content-click="false">
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="editedItem.ket_thuc_hoc_ky_1"
                            label="Kết thúc học kỳ I"
                            readonly
                            v-on="on"
                            :rules="validateKetThuc1"
                            v-bind="attrs"
                            append-icon="mdi-calendar"
                          >
                          </v-text-field>
                        </template>
                        <v-date-picker
                          locale="vi-VN"
                          style="width: 100%"
                          v-model="picker.picker12"
                          :active-picker.sync="activeKetThuc1"
                          @change="ngayKetThuc1Save"
                        ></v-date-picker>
                      </v-menu>
                    </v-col>
                  </v-row>
                </v-row>
                <v-row v-if="showDatePickerLines.ky2" class="ma-4">
                  <v-row style="width: 100%">
                    <v-col cols="12" md="6">
                      <v-menu offset-y
                      min-width="auto"
                      transition="scale-transition"
                      ref="ngayBatDau2"
                      v-model="picker21"
                      :close-on-content-click="false">
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="editedItem.bat_dau_hoc_ky_2"
                            label="Bắt đầu học kỳ II"
                            readonly
                            v-on="on"
                            :rules="validateBatDau2"
                            v-bind="attrs"
                            append-icon="mdi-calendar"
                          >
                          </v-text-field>
                        </template>
                        <v-date-picker
                          locale="vi-VN"
                          style="width: 100%"
                          v-model="picker.picker21"
                          :active-picker.sync="activeBatDau2"
                          @change="ngayBatDau2Save"
                        ></v-date-picker>
                      </v-menu>
                    </v-col>
                    <v-col cols="12" md="6">
                      <v-menu offset-y
                      transition="scale-transition"
                      ref="ngayKetThuc2"
                      v-model="picker22"
                      min-width="auto"
                      :close-on-content-click="false">
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="editedItem.ket_thuc_hoc_ky_2"
                            label="Kết thúc học kỳ II"
                            readonly
                            v-on="on"
                            :rules="validateKetThuc2"
                            v-bind="attrs"
                            append-icon="mdi-calendar"
                          >
                          </v-text-field>
                        </template>
                        <v-date-picker
                          locale="vi-VN"
                          style="width: 100%"
                          v-model="picker.picker22"
                          :active-picker.sync="activeKetThuc2"
                          @change="ngayKetThuc2Save"
                        ></v-date-picker>
                      </v-menu>
                    </v-col>
                  </v-row>
                </v-row>
              </v-form>
            </v-card>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn elevation="1" color="warning darken-1" text @click="close"> Hủy bỏ </v-btn>
        <v-btn elevation="1" color="primary darken-1" text @click="save"> Lưu </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { schoolYearMixin } from "@/mixin/SchoolYearMixin";
import AppService from "@/services/app.service";

export default {
  name: "SchoolYearDialog",
  mixins: [schoolYearMixin],
  props: ["schoolYears", "desserts", "headers", "gradeLevels"],
  data() {
    return {
      promise: null,
      show: false,
      showDatePickerLines: {
        ky1: true,
        ky2: false,
      },
      picker: {
        picker11: new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
          .toISOString()
          .substr(0, 10),
        picker12: new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
          .toISOString()
          .substr(0, 10),
        picker21: new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
          .toISOString()
          .substr(0, 10),
        picker22: new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
          .toISOString()
          .substr(0, 10),
      },
      defaultItem: {
        nam_hoc: this.schoolYears[0],
        so_hoc_ky: "1",
        bat_dau_hoc_ky_1: "",
        ket_thuc_hoc_ky_1: "",
        bat_dau_hoc_ky_2: "",
        ket_thuc_hoc_ky_2: "",
      },
      validateBatDau1: [
        (value) => !!value || "Không được để trống",
        (value) => ( (new Date(this.formatDateRevert(value)).getFullYear() >= this.editedItem.nam_hoc.substring(0,4)) && (new Date(this.formatDateRevert(value)).getFullYear() <= this.editedItem.nam_hoc.substring(5,9)) ) || "Ngày này phải thuộc trong khoảng năm học đã chọn",
      ],
      validateKetThuc1: [
        (value) => !!value || "Không được để trống",
        (value) => ( (new Date(this.formatDateRevert(value)).getFullYear() >= this.editedItem.nam_hoc.substring(0,4)) && (new Date(this.formatDateRevert(value)).getFullYear() <= this.editedItem.nam_hoc.substring(5,9)) ) || "Ngày này phải thuộc trong khoảng năm học đã chọn",
        (value) => (new Date(this.formatDateRevert(value)) > new Date(this.formatDate(this.editedItem.bat_dau_hoc_ky_1))) || "Ngày kết thúc kỳ 1 phải lớn hơn ngày bắt đầu kỳ 1",
      ],
      validateBatDau2: [
        (value) => !!value || "Không được để trống",
        (value) => ( (new Date(this.formatDateRevert(value)).getFullYear() >= this.editedItem.nam_hoc.substring(0,4)) && (new Date(this.formatDateRevert(value)).getFullYear() <= this.editedItem.nam_hoc.substring(5,9)) ) || "Ngày này phải thuộc trong khoảng năm học đã chọn",
        (value) => (new Date(this.formatDateRevert(value)) > new Date(this.formatDate(this.editedItem.ket_thuc_hoc_ky_1))) || "Ngày bắt đầu kỳ 2 phải lớn hơn ngày kết thúc kỳ 1"
      ],
      validateKetThuc2: [
        (value) => !!value || "Không được để trống",
        (value) => ( (new Date(this.formatDateRevert(value)).getFullYear() >= this.editedItem.nam_hoc.substring(0,4)) && (new Date(this.formatDateRevert(value)).getFullYear() <= this.editedItem.nam_hoc.substring(5,9)) ) || "Ngày này phải thuộc trong khoảng năm học đã chọn",
        (value) => (new Date(this.formatDateRevert(value)) > new Date(this.formatDate(this.editedItem.bat_dau_hoc_ky_2))) || "Ngày kết thúc kỳ 2 phải lớn hơn ngày bắt đầu kỳ 2"
      ],
      editedIndex: -1,
      editedItem: {
        nam_hoc: this.schoolYears[0],
        so_hoc_ky: "1",
        bat_dau_hoc_ky_1: "",
        ket_thuc_hoc_ky_1: "",
        bat_dau_hoc_ky_2: "",
        ket_thuc_hoc_ky_2: ""
      },
      activeKetThuc2: null,
      activeKetThuc1: null,
      activeBatDau2: null,
      activeBatDau1: null,
      picker11: false,
      picker12: false,
      picker21: false,
      picker22: false,
      oldObject: {}
    };
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "Thêm mới" : "Chỉnh sửa";
    }
  },
  methods: {
    open(item) {
      this.show = true;
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.oldObject = Object.assign({}, item);
      if (this.editedIndex < 0) {
        this.editedItem = this.defaultItem;
      }else{
        this.editedItem.bat_dau_hoc_ky_1 = this.formatDate(this.editedItem.bat_dau_hoc_ky_1) 
        this.editedItem.ket_thuc_hoc_ky_1 = this.formatDate(this.editedItem.ket_thuc_hoc_ky_1) 
        this.editedItem.bat_dau_hoc_ky_2 = this.editedItem.bat_dau_hoc_ky_2 ? (this.formatDate(this.editedItem.bat_dau_hoc_ky_2)) : null
        this.editedItem.ket_thuc_hoc_ky_2 = this.editedItem.ket_thuc_hoc_ky_2 ? (this.formatDate(this.editedItem.ket_thuc_hoc_ky_2)) : null
      }
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    close(data) {
      this.show = false;
      this.$refs.formDetails.resetValidation();
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
      this.promise.reject(data);
    },
    async save() {
      if (this.$refs.formDetails.validate()) {
        if (this.editedIndex > -1) {
          var temp = this.editedItem
          var objTemp = {}
          objTemp.years = temp.nam_hoc
          objTemp.semesterAmount = temp.so_hoc_ky
          objTemp.fromDate1 = this.formatDateRevert(temp.bat_dau_hoc_ky_1) + ' 00:00:00'
          objTemp.toDate1 = this.formatDateRevert(temp.ket_thuc_hoc_ky_1) + ' 00:00:00'
          objTemp.fromDate2 = temp.bat_dau_hoc_ky_2 ? (this.formatDateRevert(temp.bat_dau_hoc_ky_2) + ' 00:00:00') : null
          objTemp.toDate2 = temp.ket_thuc_hoc_ky_2 ? (this.formatDateRevert(temp.ket_thuc_hoc_ky_2) + ' 00:00:00') : null
          AppService.saveSchoolYear(objTemp)
          .then( (res) => {
            if(res.data.status === 'BAD_REQUEST'){
              this.close('error');
              this.$emit('openToastMessage', {
                mess: res.data.message,
                status: true
              })
            }else{
              this.$emit('openToastMessage', {
                mess: 'Cập nhật thành công',
                status: false
              })
              this.editedItem.bat_dau_hoc_ky_1 = this.formatDateRevert(temp.bat_dau_hoc_ky_1) 
              this.editedItem.ket_thuc_hoc_ky_1 = this.formatDateRevert(temp.ket_thuc_hoc_ky_1)
              this.editedItem.bat_dau_hoc_ky_2 = temp.bat_dau_hoc_ky_2 ? (this.formatDateRevert(temp.bat_dau_hoc_ky_2)) : null
              this.editedItem.ket_thuc_hoc_ky_2 = temp.ket_thuc_hoc_ky_2 ? (this.formatDateRevert(temp.ket_thuc_hoc_ky_2)) : null
              Object.assign(this.desserts[this.editedIndex], this.editedItem);
              this.checkExistKy12();
              if (this.existKy2) {
                this.$emit("changeHeader", this.dataKy2);
                // this.promise.reject()
                this.close();
                return;
              }
              if (this.existKy1) {
                this.$emit("changeHeader", this.dataKy1);
                this.promise.reject()
              }
            }
          })
          .catch( () => {
            this.$emit('openToastMessage', {
              mess: 'Cập nhật thất bại',
              status: true
            })
            this.close('error')
          })
        } else {
          this.editedItem.stt = this.desserts.length + 1
          this.promise.resolve(this.editedItem)

          this.show = false;
          this.$refs.formDetails.resetValidation();
          this.$nextTick(() => {
            this.editedItem = Object.assign({}, this.defaultItem);
            this.editedIndex = -1;
          });
        }
      }
    },
    resetForm() {
      this.showDatePickerLines.ky1 = true;
      this.showDatePickerLines.ky2 = false;
      this.defaultItem = {
        nam_hoc: this.schoolYears[0],
        so_hoc_ky: "1",
        bat_dau_hoc_ky_1: "",
        ket_thuc_hoc_ky_1: "",
        bat_dau_hoc_ky_2: "",
        ket_thuc_hoc_ky_2: "",
      };
    },
    ngayBatDau1Save(date){
      this.$refs.ngayBatDau1.save(date)
    },
    ngayBatDau2Save(date){
      this.$refs.ngayBatDau2.save(date)
    },
    ngayKetThuc1Save(date){
      this.$refs.ngayKetThuc1.save(date)
    },
    ngayKetThuc2Save(date){
      this.$refs.ngayKetThuc2.save(date)
    }
  },
  watch: {
    "editedItem.so_hoc_ky"() {
      switch (this.editedItem.so_hoc_ky) {
        case 1: {
          this.showDatePickerLines.ky1 = true;
          this.showDatePickerLines.ky2 = false;
          this.editedItem.ket_thuc_hoc_ky_2 = "";
          this.editedItem.bat_dau_hoc_ky_2 = "";
          break;
        }
        case 2: {
          this.showDatePickerLines.ky1 = true;
          this.showDatePickerLines.ky2 = true;
          break;
        }
      }
    },
    "picker.picker11"() {
      this.editedItem.bat_dau_hoc_ky_1 = this.formatDate(this.picker.picker11);
    },
    "picker.picker12"() {
      this.editedItem.ket_thuc_hoc_ky_1 = this.formatDate(this.picker.picker12);
    },
    "picker.picker21"() {
      this.editedItem.bat_dau_hoc_ky_2 = this.formatDate(this.picker.picker21);
    },
    "picker.picker22"() {
      this.editedItem.ket_thuc_hoc_ky_2 = this.formatDate(this.picker.picker22);
    },
    show() {
      if (!this.show) {
        this.resetForm();
      }
    },
    picker11(val){
      val && setTimeout(() => (this.activeBatDau1 = 'YEAR'))
    },
    picker12(val){
      val && setTimeout(() => (this.activeKetThuc1 = 'YEAR'))
    },
    picker21(val){
      val && setTimeout(() => (this.activeBatDau2 = 'YEAR'))
    },
    picker22(val){
      val && setTimeout(() => (this.activeKetThuc2 = 'YEAR'))
    },
  },
};
</script>