<template>
  <v-form ref="formDetails">
    <v-dialog v-model="show" max-width="650px">
      <v-card>
        <v-toolbar
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1">
          <v-toolbar-title>{{ titleDialog }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="close()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card class="pa-6">
          <v-form ref="form">
            <v-row>
              <v-col cols="12" sm="12" md="6">
                <v-text-field
                  label="Mã môn học (*)"
                  v-model="editedItem.ma_mon_hoc"
                  :rules="ruleMaMonHoc"
                  :disabled="editedIndex != -1"
                >
                </v-text-field>
              </v-col>
              <v-col cols="12" sm="12" md="6">
                <v-text-field
                  label="Tên môn học (*)"
                  v-model="editedItem.ten_mon_hoc"
                  :rules="ruleTenMonHoc"
                >
                </v-text-field>
              </v-col>
              <v-col cols="12" sm="12" md="6">
                <v-menu offset-y>
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-on="on"
                      v-bind="attrs"
                      width="100%"
                      append-icon="mdi-chevron-down"
                      label="Lựa chọn khối (*)"
                      readonly
                      v-model="choosedKhoi"
                      :rules="rules"
                    >
                    </v-text-field>
                  </template>
                  <v-list v-if="(!editedItem.isConfigClass)" style="max-height: 50vh; overflow-y: scroll;">
                    <v-list-item
                      v-for="(item, index) in dataKhoi"
                      :key="index"
                      @click="chooseKhoi(item)"
                    >
                      <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-col>
              <v-col cols="12" sm="12" md="6">
                <v-menu offset-y>
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-on="on"
                      v-bind="attrs"
                      width="100%"
                      append-icon="mdi-chevron-down"
                      label="Lựa chọn khoa/ban (*)"
                      readonly
                      v-model="choosedKhoaBan"
                      :rules="rules"
                    >
                    </v-text-field>
                  </template>
                  <v-list style="max-height: 50vh; overflow-y: scroll;">
                    <v-list-item-group multiple v-model="listKhoaBanChoosed">
                      <v-list-item
                        v-for="(item, index) in dataKhoaBan"
                        :key="index"
                      >
                        <v-list-item-title>{{ item.name }}</v-list-item-title>
                      </v-list-item>
                    </v-list-item-group>
                  </v-list>
                </v-menu>
              </v-col>
              <v-col cols="12" sm="12" md="6">
                <v-menu offset-y>
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-on="on"
                      v-bind="attrs"
                      width="100%"
                      append-icon="mdi-chevron-down"
                      label="Lựa chọn loại môn (*)"
                      readonly
                      v-model="choosedLoaiMon"
                      :rules="rules"
                    >
                    </v-text-field>
                  </template>
                  <v-list style="max-height: 50vh; overflow-y: scroll;">
                    <v-list-item
                      v-for="(item, index) in dataLoaiMon"
                      :key="index"
                      @click="chooseLoaiMon(item)"
                    >
                      <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-col>
              <v-col cols="12" sm="12" md="6">
                <v-menu offset-y>
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-on="on"
                      v-bind="attrs"
                      width="100%"
                      append-icon="mdi-chevron-down"
                      label="Lựa chọn kiểu môn (*)"
                      readonly
                      v-model="choosedKieuMon"
                      :rules="rules"
                    >
                    </v-text-field>
                  </template>
                  <v-list v-if="!(editedItem.isConfigClass && editedItem.isConfigGrade)" style="max-height: 50vh; overflow-y: scroll;">
                    <v-list-item
                      v-for="(item, index) in dataKieuMon"
                      :key="index"
                      @click="chooseKieuMon(item)"
                    >
                      <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-col>
              <v-col cols="12" sm="12">
                <v-text-field label="Mô tả môn học" v-model="editedItem.mo_ta" :rules="ruleIfEnterMaxLength">
                </v-text-field>
              </v-col>
            </v-row>
            <v-row>
            <v-spacer></v-spacer>
              <v-col cols="3" sm="2">
                <v-btn elevation="1" color="warning darken-1" text @click="close"> Hủy bỏ </v-btn>
              </v-col>
              <v-col cols="3" sm="2">
                <v-btn elevation="1" color="primary darken-1" text @click="save"> Lưu lại </v-btn>
              </v-col>
            <v-spacer></v-spacer>
            </v-row>
          </v-form>
        </v-card>
      </v-card>
    </v-dialog>
  </v-form>
</template>

<script>
import AppService from "@/services/app.service";

export default {
  name: "SchoolSubjectDialog",
  props: ["desserts", "dataKhoi", "dataKhoaBan", "dataLoaiMon", "dataKieuMon"],
  data() {
    return {
      promise: null,
      show: false,
      editedItem: {
        ma_mon_hoc: "",
        khoi: "",
        loai_mon: "",
        ten_mon_hoc: "",
        khoa_ban: "",
        kieu_mon: "",
        mo_ta: ""
      },
      defaultItem: {
        ma_mon_hoc: "",
        khoi: "",
        loai_mon: "",
        ten_mon_hoc: "",
        khoa_ban: "",
        kieu_mon: "",
        mo_ta: "",
        isConfigGrade: 0,
        isConfigClass: 0
      },
      editedIndex: -1,
      ruleMaMonHoc: [
        (value) => !!value || "Không được để trống",
        (value) => value ? (!value.includes(' ')) || "Không được chứa khoảng trống" : true,
        (value) => value.length <= 50 || 'Không quá 50 ký tự',
        (value) => !this.existDau(value) || 'Không được chứa dấu'
    
      ],
      ruleTenMonHoc: [
        (value) => !!value || "Không được để trống",
        (value) => value.trim().length != 0 || 'Hãy nhập đầy đủ', 
        (value) => value.length <= 250 || 'Tối đa 250 ký tự'
      ],
      rules: [(value) => !!value || "Hãy chọn một lựa chọn"],
      choosedKhoaBan: null,
      choosedLoaiMon: null,
      choosedKieuMon: null,
      choosedKhoi: null,
      listKhoaBanChoosed: [],
      ruleIfEnterMaxLength:[
        (v) => !v ? true : v.length <= 500 || 'Tối đa 500 ký tự'
      ],
      listDau: ["á","à","ã","ạ","ắ","ặ","ẵ","ằ","â","ấ","ầ","ậ","ẫ","é","è","ẽ","ẹ","ê","ế","ề","ệ","ễ","í","ì","ĩ","ị","õ","ó","ò","ọ","ô","ố","ồ","ộ","ỗ","ơ","ớ","ờ","ỡ","ợ","ú","ù","ũ","ụ","ư","ứ","ừ","ự","ữ","đ","ý","ỳ","ỹ","ỵ"]
    };
  },
  computed: {
    titleDialog() {
      return this.editedIndex == -1 ? "Thêm mới môn học" : "Sửa môn học";
    },
  },
  methods: {
    open(item) {
      this.show = true;
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.oldListKhoaBanChoosed = []
      if (this.editedIndex < 0) {
        this.editedItem = {
            ma_mon_hoc: "",
            khoi: "",
            loai_mon: "",
            ten_mon_hoc: "",
            khoa_ban: "",
            kieu_mon: "",
            mo_ta: "",
            loai_mon_id: null,
            kieu_mon_id: null,
            khoa_ban_id: null,
            isConfigGrade: 0,
            isConfigClass: 0
        }
      }else{
        var indexLoaiMon = this.dataLoaiMon.findIndex(x => x.id == this.editedItem.loai_mon_id)
        this.choosedLoaiMon = this.dataLoaiMon[indexLoaiMon].name
        this.choosedKhoi = this.editedItem.khoi
        if(this.editedItem.kieu_mon_id != null){
          var indexKieuMon = this.dataKieuMon.findIndex(x => x.id == this.editedItem.kieu_mon_id)
          this.choosedKieuMon = this.dataKieuMon[indexKieuMon].name
        }
        // this.choosedKhoaBan = this.dataKhoaBan[this.dataKhoaBan.findIndex(x => x.name == this.editedItem.khoa_ban)].name
        let listSttKhoaBanChoosed = []
        item.khoa_ban_id.forEach((x) => {
          this.dataKhoaBan.forEach((y, index) => {
            if(x.dept_id == y.id){
              listSttKhoaBanChoosed.push(index)
            }
          })
        })
        this.listKhoaBanChoosed = listSttKhoaBanChoosed
        this.oldListKhoaBanChoosed = listSttKhoaBanChoosed
      }
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    close() {
      this.show = false;
      this.oldListKhoaBanChoosed = []
      this.$refs['form'].resetValidation();
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
      this.promise.reject();
    },
    save() {
      if (this.$refs['form'].validate()) {
        if (this.editedIndex > -1) {
          Object.assign(this.desserts[this.editedIndex], this.editedItem)
          AppService.updateSchoolSubject(this.editedItem)
          .then( (res) => {
            if(res.data.status === 'OK'){
              this.$emit('openToastMessage', {
                mess : 'Cập nhật thành công',
                status: false
              })
            }else{
              this.$emit('openToastMessage', {
                mess : res.data.message,
                status: true
              })
            }
          })
          .catch( () => {
            this.$emit('openToastMessage', {
              mess : 'Cập nhật thất bại',
              status: true
            })
          })
        } else {
          this.editedItem.stt = this.desserts.length + 1;
          this.promise.resolve(this.editedItem);
        }
        this.editedItem = this.defaultItem
        this.oldListKhoaBanChoosed = []
        this.show = false;
        this.close();
      }
    },
    choosedDropdown(event) {
      switch (event.type) {
        case "khoi": {
          this.editedItem.khoi = event.choosed
          break;
        }
        case "kieu-mon": {
          this.editedItem.kieu_mon = event.choosed
          break;
        }
        case "loai-mon": {
          this.editedItem.loai_mon = event.choosed
          break;
        }
      }
    },
    resetForm(){
        this.editedItem = {
            ma_mon_hoc: "",
            khoi: "",
            loai_mon: "",
            ten_mon_hoc: "",
            khoa_ban: "",
            kieu_mon: "",
            mo_ta: "",
            loai_mon_id: null,
            kieu_mon_id: null,
            khoa_ban_id: null
        }
    },
    chooseLoaiMon(item){
      this.editedItem.loai_mon = item.name
      this.editedItem.loai_mon_id = item.id
      this.choosedLoaiMon = item.name
    },
    chooseKieuMon(item){
      this.editedItem.kieu_mon = item.name
      this.editedItem.kieu_mon_id = item.id
      this.choosedKieuMon = item.name
    },
    chooseKhoi(item){
      this.editedItem.khoi = item.name
      this.editedItem.khoi_id = item.id
      this.choosedKhoi = item.name
    },
    existDau(value){
      for(var i=0; i < value.length; i++){
          for(var j=0; j < this.listDau.length; j++){
              if(value[i] === this.listDau[j]){
                  return true
              }
          }
      }
      return false
    }
  },
  watch:{
    show() {
      if (!this.show) {
        this.resetForm();
        this.choosedKhoaBan = null
        this.choosedLoaiMon = null
        this.choosedKieuMon = null
        this.oldListKhoaBanChoosed = []
        this.listKhoaBanChoosed = []
        this.$refs['form'].resetValidation()
        this.$refs['form'].reset()
      }
    },
    listKhoaBanChoosed(){
      this.editedItem.khoa_ban = ''
      this.editedItem.khoa_ban_id = []
      this.listKhoaBanChoosed.forEach(x => {
        this.editedItem.khoa_ban += (this.dataKhoaBan[x].name + ', ')
        this.editedItem.khoa_ban_id.push({
          dept_id: this.dataKhoaBan[x].id,
          id: null,
          name: this.dataKhoaBan[x].name,
          subject_id: this.editedItem.id
        })
      })
      this.editedItem.khoa_ban = this.editedItem.khoa_ban.substring(0, this.editedItem.khoa_ban.length - 2)
      this.choosedKhoaBan = this.editedItem.khoa_ban
    }
  }
};
</script>