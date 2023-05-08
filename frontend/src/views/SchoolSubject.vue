<template>
  <div>
    <v-card class="mb-3">
      <v-toolbar 
                flat
                dense
                dark
                class="font-weight-bold"
                color="primary lighten-1"
                style="border-radius: 5px 5px 0px 0px"
      >
        <v-card-title class="pa-0">
          <span>Thông tin tìm kiếm</span>
        </v-card-title>
      </v-toolbar>
      <v-card class="pa-6" elevation="0">
        <v-form ref="form">
          <v-row>
            <v-col cols="12" sm="12" md="6">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Khối"
                    readonly
                    v-model="choosedKhoi"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
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
                    label="Khoa/ban"
                    readonly
                    v-model="choosedKhoaBan"
                    :rules="rules"
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
            <v-col cols="12" sm="12" md="6">
              <v-text-field
                v-model="ma_mon_hoc"
                label="Mã môn học"
                :rules="ruleSpace"
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="12" md="6">
              <v-text-field
                v-model="ten_mon_hoc"
                label="Tên môn học"
                :rules="ruleMaxLength"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row align="center" justify="center">
            <v-btn color="primary" @click="search">
              <v-icon>mdi-magnify</v-icon>
              Tìm kiếm
            </v-btn>
          </v-row>
        </v-form>
      </v-card>
    </v-card>
    <!-- <data-table
      :headers="headers"
      :desserts="desserts"
      :isDelete="true"
      :isEdit="true"
      @editItem="editItem"
      @deleteItem="deleteItem"
      :txtTitle="'Kết quả tìm kiếm : ' + this.desserts.length"
    >
      <v-btn @click="editItem" color="white" class="float-right" dark>
        <v-icon color="white" class="mr-1" style="background-color: green"
          >mdi-plus</v-icon
        >
        <p class="ma-0" style="color: black">Tạo mới</p>
      </v-btn>
    </data-table> -->
    <v-card class="mb-3 mt-5">
        <v-toolbar
                flat
                dense
                dark
                color="primary lighten-1"
                style="border-radius: 5px 5px 0px 0px"
        >
          <v-toolbar-title
            style="width: 100%; align-items: center"
            class="d-flex"
          >
            <span>{{
              "Kết quả tìm kiếm : " + desserts.length
            }}</span>
            <v-spacer></v-spacer>
            <v-btn @click="editItem" color="white" class="float-right" dark>
              <v-icon color="green">mdi-plus-outline</v-icon>
              <p class="ma-0" style="color: black">Tạo mới</p>
            </v-btn>
          </v-toolbar-title>
        </v-toolbar>
        <v-data-table
          :items="desserts"
          :headers="headers"
          :items-per-page="10"
          :footer-props="{
            'items-per-page-text': 'Số dòng mỗi trang:',
          }"
        >
            <template slot="no-data">
              Danh sách môn học rỗng
            </template>
          <template v-slot:item.khoa_ban="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-bind="attrs" v-on="on">{{
                  formatTextTooltip(item.khoa_ban, 30)
                }}</span>
              </template>
              <span>{{ item.khoa_ban }}</span>
            </v-tooltip>
          </template>

          <template v-slot:item.ten_mon_hoc="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-bind="attrs" v-on="on">{{
                  formatTextTooltip(item.ten_mon_hoc, 30)
                }}</span>
              </template>
              <span>{{ item.ten_mon_hoc }}</span>
            </v-tooltip>
          </template>

          <template v-slot:item.actions="{ item }" >
            <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
            <v-icon small class="mr-2" v-if="item.isConfigGrade === 0 && item.isConfigClass === 0" @click="deleteItem(item)"> mdi-delete </v-icon>
          </template>

          <template v-slot:footer.page-text="props">
            {{ props.pageStart }}-{{ props.pageStop }} của
            {{ props.itemsLength }} kết quả
          </template>
        </v-data-table>

    </v-card>
    <SchoolSubjectDialog
      ref="schoolSubjectDialog"
      :desserts="desserts"
      :dataKhoaBan="dataKhoaBanForDialog"
      :dataKhoi="dataKhoiForDialog"
      :dataLoaiMon="dataLoaiMon"
      :dataKieuMon="dataKieuMon"
      @openToastMessage="openToastMessage"
    />
    <ToastMessage ref="toastMessage" />
    <Loading ref="loading" />
    <ConfirmDialog
      ref="confirmDialog"
      txtTitle="Xóa môn học"
      question="Bạn đã chắc chắn muốn xóa môn học này không?"
    />
  </div>
</template>

<script>
import SchoolSubjectDialog from "@/views/Dialogs/SchoolSubjectDialog";
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";
import Loading from "@/components/Loading.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default {
  name: "SchoolSubject",
  components: {
    SchoolSubjectDialog,
    ToastMessage,
    Loading,
    ConfirmDialog,
  },
  data() {
    return {
      dataKhoi: [],
      dataKhoaBan: [],
      dataMonHoc: [],
      dataLoaiMon: [],
      dataKieuMon: [],
      headers: [
        {
          text: "STT",
          align: "center",
          sortable: false,
          value: "stt",
        },
        { text: "Mã môn học", sortable: false, value: "ma_mon_hoc" },
        { text: "Tên môn học", sortable: false, value: "ten_mon_hoc" },
        { text: "Khối", sortable: false, value: "khoi" },
        { text: "Khoa/ Ban", sortable: false, value: "khoa_ban" },
        { text: "Loại môn", sortable: false, value: "loai_mon" },
        { text: "Kiểu môn", sortable: false, value: "kieu_mon" },
        { text: "Mô tả", sortable: false, value: "mo_ta" },
        { text: "Thao tác",sortable: false, value: "actions" },
      ],
      desserts: [],
      dialogDelete: false,
      editedItem: {
        ma_mon_hoc: "",
        khoi: "",
        loai_mon: "",
        ten_mon_hoc: "",
        khoa_ban: "",
        kieu_mon: "",
        mo_ta: "",
      },
      defaultItem: {
        ma_mon_hoc: "",
        khoi: "",
        loai_mon: "",
        ten_mon_hoc: "",
        khoa_ban: "",
        kieu_mon: "",
        mo_ta: "",
      },
      editedIndex: -1,
      ma_mon_hoc: null,
      rules: [(v) => !!v || "Hãy chọn một lựa chọn"],
      objectSearch: {
        choosedKhoaBan: null,
        choosedKhoi: null,
        choosedLoaiMon: null,
      },
      choosedKhoaBan: null,
      choosedLoaiMon: null,
      choosedKhoi: null,
      ten_mon_hoc: null,
      dataKhoiForDialog: [],
      dataKhoaBanForDialog: [],
      ruleMaxLength: [
        v => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự' 
      ],
      listDau: ["á","à","ã","ạ","ắ","ặ","ẵ","ằ","â","ấ","ầ","ậ","ẫ","é","è","ẽ","ẹ","ê","ế","ề","ệ","ễ","í","ì","ĩ","ị","õ","ó","ò","ọ","ô","ố","ồ","ộ","ỗ","ơ","ớ","ờ","ỡ","ợ","ú","ù","ũ","ụ","ư","ứ","ừ","ự","ữ","đ","ý","ỳ","ỹ","ỵ"],
      ruleSpace: [
        v => !v ? true : v.length <= 50 || 'Tối đa 50 ký tự',
        v => !v ? true : !v.includes(' ') || 'Không được chứa khoảng trống',
        (value) => !value ? true : !this.existDau(value) || 'Không được chứa dấu'
      ]
    };
  },
  async created() {
    this.dataMonHoc = [];
    this.dataLoaiMon = [
      {
        id: 0,
        name: "Tự chọn",
      },
      {
        id: 1,
        name: "Bắt buộc",
      },
    ];
    this.dataKieuMon = [
      {
        id: 0,
        name: "Tính điểm",
      },
      {
        id: 1,
        name: "Xếp loại",
      },
    ];
  },
  async mounted() {
    this.$refs["loading"].open();
    // await this.$store.dispatch("getCurrentUser");
    await Promise.all([
      AppService.getAllKhoi().then(({ data }) => {
        this.dataKhoiForDialog = data.data.map((x) => x);
        this.dataKhoi = data.data;
        this.dataKhoi.unshift({
          id: null,
          name: "Tất cả",
        });
        this.chooseKhoi(this.dataKhoi[0]);
      }),
      AppService.getAllDepartmentForClass().then(({ data }) => {
        this.dataKhoaBanForDialog = data.data.map((x) => x);
        this.dataKhoaBan = data.data;
        this.dataKhoaBan.unshift({
          id: null,
          name: "Tất cả",
        });
        this.chooseKhoaBan(this.dataKhoaBan[0]);
      })
    ])

    await AppService.getSchoolSubjectPaging({
      pageSize: 0,
      pageIndex: 0,
      deptId: null,
      code: "",
      gradeLevel: null,
      name: "",
    })
      .then((data) => {
        this.desserts = [];
        data.data.data.subjects.forEach((x, index) => {
          let obj = {};
          obj.id = x.id;
          obj.stt = index + 1;
          obj.ma_mon_hoc = x.code;
          obj.ten_mon_hoc = x.name;
          obj.khoi = x.gradeName;
          obj.mo_ta = x.description;
          obj.khoa_ban = "";
          x.Departments.forEach((ele) => {
            obj.khoa_ban += ele.name + ", ";
          });
          obj.khoa_ban_id = x.Departments;
          obj.khoa_ban = obj.khoa_ban.substring(0, obj.khoa_ban.length - 2);

          obj.loai_mon_id = x.type;
          obj.kieu_mon_id = x.sub_type;
          obj.loai_mon =
            this.dataLoaiMon[
              this.dataLoaiMon.findIndex((rs) => rs.id == obj.loai_mon_id)
            ].name;
          if (x.sub_type != null) {
            obj.kieu_mon =
              this.dataKieuMon[
                this.dataKieuMon.findIndex((rs) => rs.id == obj.kieu_mon_id)
              ].name;
          } else {
            obj.kieu_mon = null;
          }
          obj.isConfigGrade = x.isConfigGrade
          obj.isConfigClass = x.isConfigClass
          this.desserts.push(obj);
        });
      })
      .catch(() => {
        setTimeout(() => {
          this.$refs["toastMessage"].open("Tìm kiếm thất bại", true);
        }, 0);
      })
      .finally(() => {
        this.$refs["loading"].close();
      });
    this.$refs["loading"].close();
  },
  methods: {
    choosedDropdown(event) {
      switch (event.type) {
        case "khoi": {
          this.objectSearch.choosedKhoi = event.choosed;
          break;
        }
        case "khoa/ban": {
          this.objectSearch.choosedKhoaBan = event.choosed;
          break;
        }
        case "loai-mon": {
          this.objectSearch.choosedLoaiMon = event.choosed;
          break;
        }
      }
    },
    editItem(item) {
      this.$refs["schoolSubjectDialog"].open(item).then((result) => {
        let index = result.khoi.indexOf(" ");
        let resultKhoi = result.khoi.substring(index, result.khoi.length);
        let deptIdNew = []
        result.khoa_ban_id.forEach(x => {
            deptIdNew.push(x.dept_id)
        })
        AppService.saveSchoolSubject({
          code: result.ma_mon_hoc,
          name: result.ten_mon_hoc,
          gradeLevel: resultKhoi,
          deptId: deptIdNew,
          type: result.loai_mon_id,
          subType: result.kieu_mon_id,
          description: result.mo_ta,
        })
          .then((response) => {
            if (response.data.status === "OK") {
              result.id = response.data.data.addSubject.id;
              this.desserts.push(result);
              console.log(this.desserts)
              setTimeout(() => {
                this.$refs["toastMessage"].open("Thêm thành công", false);
              }, 0);
            } else {
              setTimeout(() => {
                this.$refs["toastMessage"].open(response.data.message, true);
              }, 0);
            }
          })
          .catch(() => {
            setTimeout(() => {
              this.$refs["toastMessage"].open("Thêm thất bại", true);
            }, 0);
          });
      })
      .catch(() => {})
    },
    deleteItem(item) {
      this.$refs["confirmDialog"]
        .open()
        .then(() => {
          this.editedIndex = this.desserts.indexOf(item);
          this.editedItem = Object.assign({}, item);
          AppService.deleteSchoolSubject(this.editedItem.id)
            .then(() => {
              setTimeout(() => {
                this.desserts.splice(this.editedIndex, 1);
                this.$refs["toastMessage"].open("Xoá thành công", false);
              }, 0);
            })
            .catch(() => {
              setTimeout(() => {
                this.$refs["toastMessage"].open("Xoá thất bại", true);
              }, 0);
            });
        })
        .catch(() => {});
    },
    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
    chooseKhoaBan(item) {
      this.choosedKhoaBan = item.name;
      this.objectSearch.choosedKhoaBan = item.id;
    },
    chooseLoaiMon(item) {
      this.choosedLoaiMon = item.name;
      this.objectSearch.choosedLoaiMon = item.id;
    },
    async search() {
      if (this.$refs["form"].validate()) {
        this.$refs["loading"].open();
        this.objectSearch.ma_mon_hoc = this.ma_mon_hoc;
        this.objectSearch.ten_mon_hoc = this.ten_mon_hoc;
        await AppService.getSchoolSubjectPaging({
          pageSize: 0,
          pageIndex: 0,
          deptId: this.objectSearch.choosedKhoaBan,
          code:
            this.objectSearch.ma_mon_hoc == null
              ? ""
              : this.objectSearch.ma_mon_hoc.trim(),
          gradeLevel: this.objectSearch.choosedKhoi,
          name:
            this.objectSearch.ten_mon_hoc == null
              ? ""
              : this.objectSearch.ten_mon_hoc.trim(),
        })
          .then((data) => {
            this.desserts = [];
            data.data.data.subjects.forEach((x, index) => {
              let obj = {};
              obj.id = x.id;
              obj.stt = index + 1;
              obj.ma_mon_hoc = x.code;
              obj.ten_mon_hoc = x.name;
              obj.khoi = x.gradeName;
              obj.mo_ta = x.description;
              obj.khoa_ban = "";
              x.Departments.forEach((ele) => {
                obj.khoa_ban += ele.name + ", ";
              });
              obj.khoa_ban = obj.khoa_ban.substring(0, obj.khoa_ban.length - 2);
              obj.khoa_ban_id = x.Departments;
              obj.loai_mon_id = x.type;
              obj.kieu_mon_id = x.sub_type;
              obj.loai_mon =
                this.dataLoaiMon[
                  this.dataLoaiMon.findIndex((rs) => rs.id == obj.loai_mon_id)
                ].name;
              if (x.sub_type != null) {
                obj.kieu_mon =
                  this.dataKieuMon[
                    this.dataKieuMon.findIndex((rs) => rs.id == obj.kieu_mon_id)
                  ].name;
              } else {
                obj.kieu_mon = null;
              }
              obj.isConfigGrade = x.isConfigGrade
              obj.isConfigClass = x.isConfigClass
              this.desserts.push(obj);
            });
          })
          .catch(() => {
            setTimeout(() => {
              this.$refs["toastMessage"].open("Tìm kiếm thất bại", true);
            }, 0);
          })
          .finally(() => {
            this.$refs["loading"].close();
          });
      }
    },
    openToastMessage(data) {
      this.$refs["toastMessage"].open(data.mess, data.status);
    },
    chooseKhoi(item) {
      if (item.id == null) {
        this.objectSearch.choosedKhoi = null;
        this.choosedKhoi = item.name;
      } else {
        let index = item.name.indexOf(" ");
        let resultKhoi = item.name.substring(index, item.name.length).trim();
        this.objectSearch.choosedKhoi = resultKhoi;
        this.choosedKhoi = item.name;
      }
    },
    formatTextTooltip(item, max) {
      if (item.length >= max) {
        return item.substring(0, max) + "...";
      }
      return item;
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
};
</script>