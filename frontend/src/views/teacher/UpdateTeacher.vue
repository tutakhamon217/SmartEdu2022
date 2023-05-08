<template>
  <div>
    <v-form ref="form">
            <v-card outlined height="100%">
          <v-toolbar
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1"
          >
            <v-toolbar-title class="text-center"
              >Thông tin lý lịch cá nhân</v-toolbar-title
            >
            <v-spacer></v-spacer>
          </v-toolbar>
      <v-row class="pa-4">
        <v-col md="4">
          <v-row>
            <v-col sm="12" v-show="false">
              <v-file-input
                ref="file-input"
                @change="uploadImage()"
                :rules="ruleFile"
                accept="image/png, image/jpeg"
              ></v-file-input>
            </v-col>
            <v-col offset="2" sm="8">
              <v-img
                style="cursor: pointer"
                @click="changeImage"
                :src="rawImg ? rawImg : defaultImg"
              ></v-img>
            </v-col>
          </v-row>
          <v-row align="center" justify="center">
            <v-btn @click="changeImage()" color="primary">Chọn ảnh</v-btn>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-text-field
                label="Mã cán bộ *"
                placeholder="Nhập mã CBGV"
                :rules="rules.concat(ruleMaxLength50).concat(ruleIncludeSpace)"
                v-model="maNhanVien"
                :disabled="$route.params.id"
              >
              </v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-text-field
                label="Tên cán bộ *"
                :rules="rules.concat(ruleMaxLength250).concat(ruleTrim)"
                placeholder="Nhập tên CBGV"
                v-model="fullName"
              >
              </v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Trạng thái"
                    readonly
                    placeholder="Chọn trạng thái"
                    v-model="choosedTrangThai"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list
                  v-if="$route.params.id"
                  style="max-height: 50vh; overflow-y: scroll"
                >
                  <v-list-item
                    v-for="(item, index) in dataTrangThai"
                    :key="index"
                    @click="chooseTrangThai(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
          </v-row>
        </v-col>
        <v-col md="4">
          <v-row>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Đơn vị *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedDonVi"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataDonVi"
                    :key="index"
                    @click="chooseDonVi(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Tổ bộ môn *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedToBoMon"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataToBoMon"
                    :key="index"
                    @click="chooseToBoMon(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-menu offset-y
              min-width="auto"
              transition="scale-transition"
                ref="ngayVaoTruongRef"
                v-model="ngayVaoTruongRef"
                :close-on-content-click="false">
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-model="ngayVaoTruongFormatted"
                    label="Ngày vào trường *"
                    readonly
                    v-on="on"
                    v-bind="attrs"
                    :rules="rules"
                    append-icon="mdi-calendar"
                    placeholder="dd/mm/yyyy"
                  >
                  </v-text-field>
                </template>
                <v-date-picker
                  style="width: 100%"
                  locale="vi-VN"
                  v-model="ngayVaoTruong"
                  :active-picker.sync="activeNgayVaoTruong"
                  @change="ngayVaoTruongSave"
                ></v-date-picker>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-text-field
                v-model="soDienThoai"
                label="Số điện thoại *"
                :rules="rulesSoDienThoai"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-menu offset-y
              min-width="auto"
              transition="scale-transition"
                ref="ngaySinhRef"
                v-model="ngaySinhRef"
                :close-on-content-click="false">
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-model="ngaySinhFormatted"
                    label="Ngày sinh *"
                    readonly
                    v-on="on"
                    :rules="rules"
                    v-bind="attrs"
                    append-icon="mdi-calendar"
                    placeholder="dd/mm/yyyy"
                  >
                  </v-text-field>
                </template>
                <v-date-picker
                  style="width: 100%"
                  locale="vi-VN"
                  v-model="ngaySinh"
                  :active-picker.sync="activeNgaySinh"
                  @change="ngaySinhSave"
                ></v-date-picker>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="danToc" label="Dân tộc" :rules="ruleMaxLength250"> </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field
                v-model="diaChiThuongTru"
                label="Địa chỉ thường trú"
                :rules="ruleMaxLength250"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="soSoBHXH" label="Số sổ BHXH" :rules="ruleMaxLength250">
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field :disabled="!inputedCMND" v-model="noiCap" :rules="ruleMaxLength250" label="Nơi cấp CMND/TCC">
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Tình trạng hôn nhân *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedTinhTrangHonNhan"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataTinhTrangHonNhan"
                    :key="index"
                    @click="chooseTinhTrangHonNhan(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
          </v-row>
        </v-col>
        <v-col md="4">
          <v-row>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Khoa/Ban *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedKhoaBan"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataKhoaBan"
                    :key="index"
                    @click="chooseKhoaBan(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Chức vụ *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedChucVu"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item-group
                    v-model="multipleChucVu"
                    multiple
                    color="gray"
                  >
                    <v-list-item v-for="item in dataChucVu" :key="item.code">
                      <v-list-item-title>{{ item.name }}</v-list-item-title>
                    </v-list-item>
                  </v-list-item-group>
                </v-list>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Loại hợp đồng *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedLoaiHopDong"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataLoaiHopDong"
                    :key="index"
                    @click="chooseLoaiHopDong(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="email" :rules="rulesEmail" label="Email *">
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="tonGiao" :rules="ruleMaxLength250" label="Tôn giáo"> </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="queQuan" :rules="ruleMaxLength250" label="Quê quán"> </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="diaChiTamTru" :rules="ruleMaxLength250" label="Địa chỉ tạm trú">
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field
                v-model="soCMND"
                label="Số CMND/TCC (9 hoặc 12 số) *"
                :rules="ruleCMND"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-menu offset-y
                min-width="auto"
                transition="scale-transition"
                ref="ngayCapRef"
                v-model="ngayCapRef"
                :close-on-content-click="false">
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    :rules="rules"
                    width="100%"
                    append-icon="mdi-calendar"
                    label="Ngày cấp CMND/TCC *"
                    readonly
                    placeholder="dd/mm/yyyy"
                    v-model="ngayCapCMNDFormatted"
                    :disabled="!inputedCMND"
                  >
                  </v-text-field>
                </template>
                <v-date-picker
                  style="width: 100%"
                  v-model="ngayCapCMND"
                  locale="vi-VN"
                  :active-picker.sync="activeNgayCap"
                  @change="ngayCapSave"
                  :disabled="!inputedCMND"
                ></v-date-picker>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Giới tính *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="choosedGioiTinh"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataGioiTinh"
                    :key="index"
                    @click="chooseGioiTinh(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
                  <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="primary" :loading="loading" @click="save">Lưu lại</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
      </v-card>
    </v-form>
    <ToastMessage ref="toastMessage" />
    <Loading ref="loading" />
  </div>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";
import Loading from "@/components/Loading.vue";

export default {
  name: "UpdateTeacher",
  components: {
    ToastMessage,
    Loading,
  },
  data() {
    return {
      choosedTrangThai: "",
      dataTrangThai: [
        {
          id: 0,
          name: "Đang làm việc",
        },
        {
          id: 1,
          name: "Đã nghỉ việc",
        },
        {
          id: 2,
          name: "Đã nghỉ hưu",
        },
        {
          id: 3,
          name: "Tạm nghỉ",
        },
      ],
      choosedDonVi: "",
      dataDonVi: [],
      choosedToBoMon: "",
      dataToBoMon: [],
      ngayVaoTruong: null,
      ngayVaoTruongFormatted: null,
      rules: [(v) => !!v || "Không được để trống"],
      rulesEmail: [
        (v) => !!v || "Không được để trống",
        (v) => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự',
        (v) =>
          !v ||
          /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
          "E-mail không hợp lệ"
      ],
      maNhanVien: null,
      fullName: null,
      soDienThoai: null,
      ngaySinh: null,
      ngaySinhFormatted: null,
      danToc: null,
      diaChiThuongTru: null,
      soSoBHXH: null,
      noiCap: null,
      dataTinhTrangHonNhan: [
        {
          id: 0,
          name: "Độc thân",
        },
        {
          id: 1,
          name: "Đã kết hôn",
        },
      ],
      choosedTinhTrangHonNhan: null,
      dataKhoaBan: [],
      choosedKhoaBan: null,
      dataChucVu: [],
      choosedChucVu: null,
      dataLoaiHopDong: [
        {
          id: 0,
          name: "Hợp đồng",
        },
        {
          id: 1,
          name: "Biên chế",
        },
      ],
      choosedLoaiHopDong: null,
      email: null,
      tonGiao: null,
      queQuan: null,
      diaChiTamTru: null,
      soCMND: null,
      ngayCapCMND: null,
      ngayCapCMNDFormatted: null,
      dataGioiTinh: [
        {
          id: 0,
          name: "Nam",
        },
        {
          id: 1,
          name: "Nữ",
        },
      ],
      choosedGioiTinh: null,
      multipleChucVu: [],
      rawImg: null,
      defaultImg:
        "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png",
      employee: {},
      updating: false,
      firstLoadUpdating: false,
      loading: false,
      headerOfPhoneNumber: [
        "032",
        "033",
        "034",
        "035",
        "036",
        "037",
        "038",
        "039",
        "096",
        "097",
        "098",
        "086",
        "070",
        "077",
        "079",
        "076",
        "078",
        "081",
        "082",
        "083",
        "084",
        "085",
        "056",
        "058",
        "059",
        "094",
        "091",
      ],
      rulesSoDienThoai: [
        (v) => !!v || "Không được để trống",
        (v) =>
          !v
            ? true
            : v.length >= 3
            ? this.headerOfPhoneNumber.some((x) => x == v.substring(0, 3)) ||
              "Sai định dạng số điện thoại"
            : false || "Chứa 10 ký tự số",
        (v) =>
          !v ? true : v.length == 10 ? true : false || "Chứa 10 ký tự số",
      ],
      ruleCMND: [
        (v) =>
          v
            ? v.length == 9 || v.length == 12
              ? true
              : false || "Dài 9 hoặc 12 kí tự số"
            : false || "Không được để trống",
      ],
      ngayVaoTruongRef: false,
      activeNgayVaoTruong: null,
      ngaySinhRef: false,
      activeNgaySinh: null,
      ngayCapRef: false,
      activeNgayCap: null,
      inputedCMND: false,
      ruleMaxLength50: [
        v => !v ? true : v.length <= 50 || 'Tối đa 50 ký tự'
      ],
      ruleMaxLength250: [
        v => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự'
      ],
      ruleFile: [
        // value => !value || value.size <= 5242880 || 'Dung lượng file phải nhỏ hơn 5 MB',
      ],
      ruleIncludeSpace: [
        v => !v ? true : !v.includes(' ') || 'Không được chứa khoảng trống',
        (value) => !this.existDau(value) || 'Không được chứa dấu'
      ],
      listDau: ["á","à","ã","ạ","ắ","ặ","ẵ","ằ","â","ấ","ầ","ậ","ẫ","é","è","ẽ","ẹ","ê","ế","ề","ệ","ễ","í","ì","ĩ","ị","õ","ó","ò","ọ","ô","ố","ồ","ộ","ỗ","ơ","ớ","ờ","ỡ","ợ","ú","ù","ũ","ụ","ư","ứ","ừ","ự","ữ","đ","ý","ỳ","ỹ","ỵ"],
      ruleTrim: [
        (value) => value.trim().length != 0 || 'Hãy nhập đầy đủ'
      ]
    };
  },
  async mounted() {
    // await this.$store.dispatch("getCurrentUser");
    if (this.$route.params.id) {
      this.$refs["loading"].open();
      this.updating = true;
      this.firstLoadUpdating = true;
      await AppService.getDropdownDonVi().then((res) => {
        this.dataDonVi = res.data.data;
      });
      await this.getAllChucVu();
      await this.getCurrentTeacher();
      this.$refs["loading"].close();
    } else {
      await this.getDropdownDonVi();
      await this.getAllChucVu();
      this.chooseTrangThai(this.dataTrangThai[0]);
    }
  },
  methods: {
    changeImage() {
      this.$refs["file-input"].$refs["input"].click();
    },
    chooseTrangThai(item) {
      this.choosedTrangThai = item.name;
      this.employee.idTrangThai = item.id;
    },
    chooseDonVi(item) {
      this.employee.idDonVi = item.id;
      this.choosedDonVi = item.name;
    },
    chooseToBoMon(item) {
      this.employee.idToBoMon = item.id;
      this.choosedToBoMon = item.name;
    },
    chooseTinhTrangHonNhan(item) {
      this.choosedTinhTrangHonNhan = item.name;
      this.employee.idTinhTrangHonNhan = item.id;
    },
    chooseKhoaBan(item) {
      this.employee.idKhoaBan = item.id;
      this.choosedKhoaBan = item.name;
    },
    chooseLoaiHopDong(item) {
      this.choosedLoaiHopDong = item.name;
      this.employee.idLoaiHopDong = item.id;
    },
    chooseGioiTinh(item) {
      this.choosedGioiTinh = item.name;
      this.employee.idGioiTinh = item.id;
    },
    formatDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split("-");
      return `${day}/${month}/${year}`;
    },
    async save() {
      if (this.$refs["form"].validate()) {
        this.loading = true;
        let chucVu = [];
        this.multipleChucVu.forEach((x) =>
          chucVu.push(this.dataChucVu[x].code)
        );
        if (this.$route.params.id) {
          let idDept = null
          if(this.employee.idToBoMon === null && this.employee.idKhoaBan === null){
            idDept = this.employee.idDonVi
          }else if(this.employee.idToBoMon === null && this.employee.idKhoaBan !== null){
            idDept = this.employee.idKhoaBan
          }else{
            idDept = this.employee.idToBoMon
          }
          AppService.updateTeacher(this.$route.params.id, {
            image: this.rawImg,
            code: this.maNhanVien,
            fullName: this.fullName,
            deptId: idDept,
            authorities: chucVu,
            startDate: this.ngayVaoTruong
              ? this.ngayVaoTruong + " 00:00:00"
              : null,
            contractType: this.employee.idLoaiHopDong,
            phone: this.soDienThoai,
            email: this.email,
            birthDay: this.ngaySinh,
            religion: this.tonGiao,
            nation: this.danToc,
            homeTown: this.queQuan,
            permanentAddress: this.diaChiThuongTru,
            temporaryAddress: this.diaChiTamTru,
            socialInsuranceNumber: this.soSoBHXH,
            identityCard: this.soCMND,
            issuedAddress: this.noiCap,
            issuedDate: this.ngayCapCMND
              ? this.ngayCapCMND + " 00:00:00"
              : null,
            marriageStatus:
              this.employee.idTinhTrangHonNhan == undefined
                ? null
                : this.employee.idTinhTrangHonNhan,
            sex:
              this.employee.idGioiTinh == undefined
                ? null
                : this.employee.idGioiTinh,
            status: this.employee.idTrangThai
          })
            .then((res) => {
              if(res.data.message == 'Cập nhật thông tin thành công'){
                this.$refs["toastMessage"].open(res.data.message, false);
                this.loading = false;
                setTimeout(() => {
                  this.$router.push({name: 'quan-ly-giao-vien'})
                }, 500)
              }else{
                this.$refs["toastMessage"].open(res.data.message, true);
                this.loading = false;
              }
            })
            .catch(() => {
              this.$refs["toastMessage"].open("Cập nhật không thành công", true);
              this.loading = false;
            });
        } else {
          let idDept = null
          if(this.employee.idToBoMon === null && this.employee.idKhoaBan === null){
            idDept = this.employee.idDonVi
          }else if(this.employee.idToBoMon === null && this.employee.idKhoaBan !== null){
            idDept = this.employee.idKhoaBan
          }else{
            idDept = this.employee.idToBoMon
          }
          AppService.addTeacher({
            image: this.rawImg,
            code: this.maNhanVien,
            fullName: this.fullName,
            deptId: idDept,
            authorities: chucVu,
            startDate: this.ngayVaoTruong
              ? this.ngayVaoTruong + " 00:00:00"
              : null,
            contractType: this.employee.idLoaiHopDong,
            phone: this.soDienThoai,
            email: this.email,
            birthDay: this.ngaySinh,
            religion: this.tonGiao,
            nation: this.danToc,
            homeTown: this.queQuan,
            permanentAddress: this.diaChiThuongTru,
            temporaryAddress: this.diaChiTamTru,
            socialInsuranceNumber: this.soSoBHXH,
            identityCard: this.soCMND,
            issuedAddress: this.noiCap,
            issuedDate: this.ngayCapCMND
              ? this.ngayCapCMND + " 00:00:00"
              : null,
            marriageStatus:
              this.employee.idTinhTrangHonNhan == undefined
                ? null
                : this.employee.idTinhTrangHonNhan,
            sex:
              this.employee.idGioiTinh == undefined
                ? null
                : this.employee.idGioiTinh,
          })
            .then((res) => {

              if(res.data.status === "OK"){
                this.$refs["toastMessage"].open(res.data.message, false);
                this.loading = false;
                setTimeout(() => {
                  this.$router.push({name: 'quan-ly-giao-vien'})
                }, 1000)
              }else{
                this.$refs["toastMessage"].open(res.data.message, true);
                this.loading = false;
              }
            })
            .catch(() => {
              this.$refs["toastMessage"].open("Thêm mới không thành công", true);
              this.loading = false;
            });
        }
      }
    },
    uploadImage() {
      const file = document.querySelector("input[type=file]").files[0];
      if(file.size > 5242880){
        this.rawImg = null
        this.$refs['toastMessage'].open("Dung lượng file ảnh phải nhỏ hơn 5 MB", true)
        return
      }else{
        if(!file.type.includes('image/')){
          this.rawImg = null
          this.$refs['toastMessage'].open("Sai định dạng ảnh", true)
          return
        }
      }

      const reader = new FileReader();

      reader.onloadend = () => {
        this.rawImg = reader.result;
      };
      reader.readAsDataURL(file);
    },
    getDropdownDonVi() {
      return AppService.getDropdownDonVi()
        .then((res) => {
          this.dataDonVi = res.data.data;
          if (this.dataDonVi.length != 0) {
            this.chooseDonVi(this.dataDonVi[0]);
          } else {
            this.dataKhoaBan = [];
            this.choosedKhoaBan = "";
            this.dataToBoMon = [];
            this.choosedToBoMon = "";
            this.employee.idToBoMon = null;
            this.employee.idKhoaBan = null;
            this.employee.idDonVi = null;
          }
        })
        .catch(() => {
          this.dataKhoaBan = [];
          this.choosedKhoaBan = "";
          this.dataToBoMon = [];
          this.choosedToBoMon = "";
          this.choosedDonVi = ""
          this.dataDonVi = []
          this.employee.idToBoMon = null;
          this.employee.idKhoaBan = null;
          this.employee.idDonVi = null;
        });
    },
    getDropdownKhoaBanTeacher() {
      return AppService.getDropdownKhoaBanTeacher(this.employee.idDonVi)
        .then((res) => {
          this.dataKhoaBan = res.data.data;
          if (this.dataKhoaBan.length != 0) {
            if (this.updating && this.firstLoadUpdating) {
              this.chooseKhoaBan(
                this.dataKhoaBan.find((x) => x.id == this.employee.deptId2)
              );
            }else{
              this.chooseKhoaBan(this.dataKhoaBan[0]);
            }
          } else {
            this.dataKhoaBan = [];
            this.choosedKhoaBan = "";
            this.dataToBoMon = [];
            this.choosedToBoMon = "";
            this.employee.idToBoMon = null;
            this.employee.idKhoaBan = null;
          }
        })
        .catch(() => {
          this.dataKhoaBan = [];
          this.choosedKhoaBan = "";
          this.dataToBoMon = [];
          this.choosedToBoMon = "";
          this.employee.idKhoaBan = null;
          this.employee.idToBoMon = null;
        });
    },
    getAllChucVu() {
      return AppService.getAllChucVu().then((res) => {
        this.dataChucVu = res.data.data.authorities;
      });
    },
    getDropdownToBoMon() {
      return AppService.getDropdownKhoaBanTeacher(this.employee.idKhoaBan).then(
        (res) => {
          if (res.data.data.length != 0) {
            this.dataToBoMon = res.data.data;
            if (this.updating && this.firstLoadUpdating) {
              this.chooseToBoMon(
                this.dataToBoMon[
                  this.dataToBoMon.findIndex(
                    (x) => x.id == this.employee.deptId1
                  )
                ]
              );
              this.firstLoadUpdating = false;
            }else{
              this.chooseToBoMon(this.dataToBoMon[0]);
            }
          } else {
            this.dataToBoMon = [];
            this.choosedToBoMon = "";
            this.employee.idToBoMon = null;
          }
        }
      )
      .catch(() => {
        this.dataToBoMon = [];
        this.choosedToBoMon = "";
        this.employee.idToBoMon = null;
        this.firstLoadUpdating = false;
      })
    },
    getCurrentTeacher() {
      return AppService.getInforTeacher(this.$route.params.id)
        .then((res) => {
          this.employee = res.data.data;
          this.rawImg = this.employee.avatar;
          this.maNhanVien = this.employee.code;
          this.fullName = this.employee.full_name;
          this.deptId = this.employee.deptId1;
          let arrChucVu = this.employee.Authorities.split(",");
          arrChucVu.forEach((x) =>
            this.dataChucVu.forEach((cv, index) =>
              cv.code == x ? this.multipleChucVu.push(index) : null
            )
          );
          this.chooseDonVi(
              this.dataDonVi.find((x) => x.id == this.employee.deptId3)
          );
          this.chooseLoaiHopDong(
            this.dataLoaiHopDong[
              this.dataLoaiHopDong.findIndex(
                (x) => x.id == this.employee.contract_type
              )
            ]
          );
          this.chooseTrangThai(
            this.dataTrangThai[
              this.dataTrangThai.findIndex((x) => x.id == this.employee.status)
            ]
          );
          this.soDienThoai = this.employee.phone;
          this.email = this.employee.email;
          this.ngayVaoTruong = this.employee.start_date;
          this.ngaySinh = this.employee.birth_day;
          this.tonGiao = this.employee.religion;
          this.danToc = this.employee.nation;
          this.queQuan = this.employee.home_town;
          this.diaChiThuongTru = this.employee.permanent_address;
          this.diaChiTamTru = this.employee.temporary_address;
          this.soSoBHXH = this.employee.social_insurance_number;
          this.soCMND = this.employee.identity_card;
          this.noiCap = this.employee.issued_address;
          this.ngayCapCMND = this.employee.issued_date;
          this.chooseTinhTrangHonNhan(
            this.dataTinhTrangHonNhan[
              this.dataTinhTrangHonNhan.findIndex(
                (x) => x.id == this.employee.marriage_status
              )
            ]
          );
          this.chooseGioiTinh(
            this.dataGioiTinh[
              this.dataGioiTinh.findIndex((x) => x.id == this.employee.sex)
            ]
          );
        })
        .catch(() => {
          this.$refs["toastMessage"].open(
            "Lỗi khi lấy thông tin giáo viên",
            true
          );
        });
    },
    ngayVaoTruongSave(date){
      this.$refs.ngayVaoTruongRef.save(date)
    },
    ngaySinhSave(date){
      this.$refs.ngaySinhRef.save(date)
    },
    ngayCapSave(date){
      this.$refs.ngayCapRef.save(date)
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
  watch: {
    ngayVaoTruong() {
      this.ngayVaoTruongFormatted = this.formatDate(this.ngayVaoTruong);
    },
    ngayCapCMND() {
      this.ngayCapCMNDFormatted = this.formatDate(this.ngayCapCMND);
    },
    ngaySinh() {
      this.ngaySinhFormatted = this.formatDate(this.ngaySinh);
    },
    choosedDonVi() {
      this.getDropdownKhoaBanTeacher();
    },
    choosedKhoaBan() {
      this.getDropdownToBoMon();
    },
    multipleChucVu() {
      this.choosedChucVu = "";
      this.multipleChucVu.forEach(
        (x) => (this.choosedChucVu += this.dataChucVu[x].name + ", ")
      );
      this.choosedChucVu = this.choosedChucVu.slice(0, -2);
    },
    ngayVaoTruongRef(val){
      val && setTimeout(() => (this.activeNgayVaoTruong = 'YEAR'))
    },
    ngaySinhRef(val){
      val && setTimeout(() => (this.activeNgaySinh = 'YEAR'))
    },
    ngayCapRef(val){
      val && setTimeout(() => (this.activeNgayCap = 'YEAR'))
    },
    soCMND(){
      if(this.soCMND.trim() != ''){
        this.inputedCMND = true
      }else{
        this.noiCap = null
        this.ngayCapCMND = null
        this.inputedCMND = false
      }
    }
  },
};
</script>