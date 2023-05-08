<template>
  <div>
    <v-form ref="form">
      <v-row>
        <v-col>
          <v-card width="100%" color="primary">
            <v-card-title style="color: white"
              >THÔNG TIN TRƯỜNG HỌC</v-card-title
            >
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col md="3">
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
                @click="updating ? changeImage() : null"
                :src="rawImg ? rawImg : defaultImg"
              ></v-img>
            </v-col>
          </v-row>
          <v-row align="center" justify="center">
            <v-btn @click="changeImage()" v-if="updating" color="primary">Chọn ảnh</v-btn>
          </v-row>
        </v-col>
        <v-col md="3">
          <v-row>
            <v-col sm="12">
              <v-text-field
                label="Mã trường *"
                v-model="item.code"
                :rules="rules"
                disabled
              >
              </v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-text-field
                label="Tên trường *"
                v-model="item.name"
                :rules="rules.concat(ruleMaxLength250)"
                :readonly="!updating"
              >
              </v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-text-field
                v-model="item.abbreviation_name"
                label="Tên viết tắt *"
                :readonly="!updating"
                :rules="rules.concat(ruleMaxLength250)"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-select
                v-model="item.level_school"
                label="Cấp học"
                :items="itemCaphoc"
                item-text="name"
                item-value="code"
                disabled
              >
              </v-select>
            </v-col>
            <v-col sm="12">
              <v-menu
                offset-y
                min-width="auto"
                ref="foundedDateRef"
                transition="scale-transition"
                v-model="foundedDateRef"
                :close-on-content-click="false"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-model="foundedDateFormatted"
                    label="Ngày thành lập trường"
                    readonly
                    v-on="on"
                    v-bind="attrs"
                    append-icon="mdi-calendar"
                    placeholder="dd/mm/yyyy"
                  >
                  </v-text-field>
                </template>
                <v-date-picker
                  style="width: 100%"
                  locale="vi-VN"
                  v-model="foundedDate"
                  :active-picker.sync="activeFoundedDate"
                  @change="foundedDateSave"
                  v-if="updating"
                  :max="today"
                ></v-date-picker>
              </v-menu>
            </v-col>
          </v-row>
        </v-col>
        <v-col md="3">
          <v-row>
            <v-col sm="12">
              <v-text-field
                label="Số điện thoại"
                v-model="item.hot_line"
                :rules="rulesSoDienThoai"
                :readonly="!updating"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field
                label="Email *"
                v-model="item.email"
                :rules="rulesEmail"
                :readonly="!updating"
              >
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
                    label="Tỉnh/Thành phố *"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="provinceName"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list v-if="updating" style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataProvince"
                    :key="index"
                    @click="chooseProvince(item)"
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
                    label="Quận huyện"
                    readonly
                    placeholder="Lựa chọn"
                    v-model="districtName"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list v-if="updating" style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataDistrict"
                    :key="index"
                    @click="chooseDistrict(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-col sm="12">
              <v-text-field v-model="item.address" label="Địa chỉ" :readonly="!updating" :rules="ruleIfEnterMaxLength">
              </v-text-field>
            </v-col>
          </v-row>
        </v-col>
        <v-col md="3">
          <v-row>
            <v-col sm="12">
              <v-select
                v-model="item.type_education"
                :items="typeEdu"
                item-text="name"
                item-value="code"
                :readonly="!updating"
              >
              </v-select>
            </v-col>
            <v-col sm="12">
              <v-text-field
                v-model="item.principal"
                :rules="rules.concat(ruleMaxLength250)"
                label="Hiệu trưởng *"
                :readonly="!updating"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field
                v-model="item.phone_principal"
                label="SĐT hiệu trưởng"
                :rules="rulesSoDienThoai"
                :readonly="!updating"
              >
              </v-text-field>
            </v-col>
            <v-col sm="12">
              <v-text-field label="Website" v-model="item.website" :readonly="!updating" :rules="ruleIfEnterMaxLength">
              </v-text-field>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <v-row align="center" justify="center">
        <v-btn color="primary" v-if="!updating" @click="updating = true">Cập nhật</v-btn>
        <v-btn class="mr-2" v-if="updating" @click="cancel">Hủy bỏ</v-btn>
        <v-btn color="primary" class="ml-2" v-if="updating" :loading="loading" @click="save">Lưu lại</v-btn>
      </v-row>
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
      rawImg: null,
      defaultImg:
        "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png",
      item: {},
      itemCaphoc: [
        {
          name: "Mẫu giáo",
          code: "KID",
        },
        {
          name: "Tiểu học",
          code: "PRIMARY",
        },
        {
          name: "Trung học cơ cở",
          code: "JUNIOR",
        },
        {
          name: "Trung học phổ thông",
          code: "HIGH",
        },
        {
          name: "Đại học",
          code: "UNI",
        },
      ],
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
      rules: [
        (v) => !!v || "Không được để trống"
      ],
      rulesEmail: [
        (v) => !!v || "Không được để trống",
        (v) => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự',
        (v) =>
          !v ||
          /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
          "E-mail không hợp lệ",
      ],
      typeEdu: [
        {
          name: "Dân lập",
          code: "PRIVATE",
        },
        {
          name: "Công lập",
          code: "PUBLIC",
        },
      ],
      dataDistrict: [],
      dataProvince: [],
      loading: false,
      provinceName: null,
      districtName: null,
      foundedDateRef: false,
      activeFoundedDate: null,
      foundedDateFormatted: null,
      foundedDate: null,
      firstLoad: true,
      updating: false,
      ruleFile: [
        // value => !value || value.size <= 5242880 || 'Dung lượng file phải nhỏ hơn 5 MB',
      ],
      firstItem: null,
      firstRawImg: null,
      rawProvinceName: null,
      rawDistrictName: null,
      rawFounded_date: null,
      today: new Date().getFullYear() + '-' + (new Date().getMonth() + 1 < 10 ? '0' + (new Date().getMonth() + 1) : new Date().getMonth() + 1) + '-' + (new Date().getDate() < 10 ? '0' + new Date().getDate() : new Date().getDate()),
      cancelState: false,
      ruleIfEnterMaxLength:[
        (v) => !v ? true : v.length <= 500 || 'Tối đa 500 ký tự'
      ],
      ruleMaxLength250: [
        (v) => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự'
      ]
    };
  },
  async mounted() {
    this.$refs["loading"].open();
    await this.getSchoolInfo();
    await this.getAllProvince();
    this.$refs["loading"].close();
  },
  methods: {
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
    chooseDistrict(item) {
      this.item.district_id = item.id;
      this.districtName = item.name;
    },
    chooseProvince(item) {
      this.item.province_id = item.id;
      this.provinceName = item.name;
    },
    changeImage() {
      this.$refs["file-input"].$refs["input"].click();
    },
    getAllDistrict() {
      return AppService.getAllDistrict(this.item.province_id)
        .then((res) => {
          if (res.data.status === "OK") {
            this.dataDistrict = res.data.data;
            if(!this.firstLoad && !this.cancelState){
                this.districtName = res.data.data[0].name
                this.item.district_id = res.data.data[0].id
            }else{
                this.firstLoad = false
                if(this.cancelState){
                  this.cancelState = false
                }
            }
          } else {
            this.$refs["toastMessage"].open(res.data.message, true);
          }
        })
        .catch(() => {
          this.$refs["toastMessage"].open(
            "Lỗi khi lấy thông tin quận huyện",
            true
          );
        });
    },
    getAllProvince() {
      return AppService.getAllProvince()
        .then((res) => {
          if (res.data.status === "OK") {
            this.dataProvince = res.data.data;
          } else {
            this.$refs["toastMessage"].open(res.data.message, true);
          }
        })
        .catch(() => {
          this.$refs["toastMessage"].open(
            "Lỗi khi lấy thông tin tỉnh/thành phố",
            true
          );
        });
    },
    getSchoolInfo() {
      return AppService.getSchoolInfo()
        .then((res) => {
          if (res.data.status === "OK") {
            this.item = res.data.data[0];
            this.provinceName = this.item.provinceName;
            this.districtName = this.item.districtName;
            this.foundedDate = this.item.founded_date;
            this.rawImg = this.item.logo

            this.firstItem = JSON.parse(JSON.stringify(res.data.data[0]));
            this.rawProvinceName = JSON.parse(JSON.stringify(this.item.provinceName));
            this.rawDistrictName = JSON.parse(JSON.stringify(this.item.districtName));
            this.rawFounded_date = JSON.parse(JSON.stringify(this.item.founded_date));
            this.firstRawImg = JSON.parse(JSON.stringify(this.item.logo));
          } else {
            this.$refs["toastMessage"].open(res.data.message, true);
          }
        })
        .catch(() => {
          this.$refs["toastMessage"].open(
            "Lỗi khi lấy thông tin trường học",
            true
          );
        });
    },
    foundedDateSave(date) {
      this.$refs.foundedDateRef.save(date);
    },
    async save() {
      if(this.$refs['form'].validate()){
        this.loading = true
        this.item.founded_date = this.foundedDate ? this.foundedDate : null
        this.item.logo = this.rawImg
          await AppService.saveSchool(this.item)
          .then((res) => {
            if(res.data.status === 'OK'){
              this.$refs['toastMessage'].open(res.data.message, false)
              this.firstItem = JSON.parse(JSON.stringify(this.item));
              this.rawProvinceName = JSON.parse(JSON.stringify(this.provinceName));
              this.rawDistrictName = JSON.parse(JSON.stringify(this.districtName));
              this.rawFounded_date = JSON.parse(JSON.stringify(this.foundedDate));
              this.firstRawImg = JSON.parse(JSON.stringify(this.rawImg));
            }else{
              this.$refs['toastMessage'].open(res.data.message, true)
            }
          })
          .catch(() => {
            this.$refs['toastMessage'].open('Cập nhật thất bại', true)
          })
          .finally(() => {
            this.loading = false
            this.updating = false
          })
      }
    },
    formatDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split("-");
      return `${day}/${month}/${year}`;
    },
    cancel(){
      this.updating = false
      this.cancelState = true
      this.item = JSON.parse(JSON.stringify(this.firstItem));
      this.provinceName = JSON.parse(JSON.stringify(this.rawProvinceName));
      this.districtName = JSON.parse(JSON.stringify(this.rawDistrictName));
      this.foundedDate = JSON.parse(JSON.stringify(this.rawFounded_date));
      this.rawImg = JSON.parse(JSON.stringify(this.firstRawImg));
    }
  },
  watch: {
    async provinceName() {
      this.$refs["loading"].open();
      await this.getAllDistrict();
      this.$refs["loading"].close();
    },
    foundedDateRef(val) {
      val && setTimeout(() => (this.activeFoundedDate = "YEAR"));
    },
    foundedDate() {
      this.foundedDateFormatted = this.formatDate(this.foundedDate);
      this.item.founded_date = this.foundedDate;
    },
  },
};
</script>