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
        <v-card-title class="pa-0 d-flex"
        style="width: 100%; align-items: center"
        >
          <span>Thông tin tìm kiếm</span>
          <v-spacer></v-spacer>
          <v-btn @click="createNew" style="background-color:white" v-if="$store.state.year == $store.state.currentYear">
            <v-icon color="green">mdi-plus-outline</v-icon>
            <v-spacer></v-spacer>
            <span style="color:black;">Tạo mới</span>
          </v-btn>
        </v-card-title>
      </v-toolbar>
      <v-card class="pa-6" elevation="0">
        <v-form ref="form-search">
          <v-row>
            <v-col sm="12" md="6">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Khối *"
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
            <v-col sm="12" md="6">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Kỳ học *"
                    readonly
                    v-model="nameKyHoc"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataKyHoc"
                    :key="index"
                    @click="chooseKyHoc(index + 1)"
                  >
                    <v-list-item-title>{{ formatKyHoc(index + 1) }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
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
    <v-card class="pa-4">
      <v-row>
        <v-col sm="12" md="6">
          <v-card>
            <v-toolbar
              flat
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
              style="border-radius: 5px 5px 0px 0px"
            >
              <v-card-title class="pa-0 d-flex"
              style="width: 100%; align-items: center"
              >
                <span>Môn học tính điểm</span>
              </v-card-title>
            </v-toolbar>
            <v-menu offset-y>
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-on="on"
                  v-bind="attrs"
                  width="100%"
                  append-icon="mdi-chevron-down"
                  label="Môn học"
                  readonly
                  v-model="choosedDDLTable_1"
                  :rules="rules"
                  class="mx-3"
                >
                </v-text-field>
              </template>
              <v-list style="max-height: 50vh; overflow-y: scroll">
                <v-list-item
                  v-for="(item, index) in dataDDLTable_1"
                  :key="index"
                  @click="chooseDDLTable_1(item)"
                >
                  <v-list-item-title>{{ item.name }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
            <v-data-table
              :headers="header_table_1"
              :items="desserts_table_1"
              :items-per-page="-1"
              disable-sort
              :hide-default-footer="true"
              class="mx-3"
            >
            <template slot="no-data">
              Danh sách môn học rỗng
            </template>
              <template v-slot:item.name="{ item }">
                {{ formatTextOverLength(item.name, 50) }}
              </template>
            </v-data-table>
            <v-btn class="ma-3"
              color="primary"
              v-if="dataDDLTable_1.length != 0 && ($store.state.year == $store.state.currentYear) && !isUpdatedScore_1 && canUpdateByYear"
              @click="editTable1"
            >
              Cập nhật
            </v-btn>
          </v-card>
        </v-col>
        <v-col sm="12" md="6">
          <v-card>
            <v-toolbar
              flat
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
              style="border-radius: 5px 5px 0px 0px"
            >
              <v-card-title class="pa-0 d-flex"
              style="width: 100%; align-items: center"
              >
                <span>Môn học xếp loại</span>
              </v-card-title>
            </v-toolbar>
            <v-menu offset-y>
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-on="on"
                  v-bind="attrs"
                  width="100%"
                  append-icon="mdi-chevron-down"
                  label="Môn học"
                  readonly
                  v-model="choosedDDLTable_2"
                  :rules="rules"
                  class="mx-3"
                >
                </v-text-field>
              </template>
              <v-list style="max-height: 50vh; overflow-y: scroll">
                <v-list-item
                  v-for="(item, index) in dataDDLTable_2"
                  :key="index"
                  @click="chooseDDLTable_2(item)"
                >
                  <v-list-item-title>{{ item.name }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
            <v-data-table
              :headers="header_table_2"
              :items="desserts_table_2"
              :items-per-page="-1"
              disable-sort
              :hide-default-footer="true"
              class="mx-3"
            >
            <template slot="no-data">
              Danh sách môn học rỗng
            </template>
              <template v-slot:item.type_choose="{ item }">
                <v-simple-checkbox
                  v-model="item.type_choose"
                  color="primary"
                  disabled
                ></v-simple-checkbox>
              </template>

              <template v-slot:item.selected_value="{ item }">
                {{ formatTextOverLength(item.selected_value, 50) }}
              </template>
            </v-data-table>
            <v-btn class="ma-3"
              color="primary"
              v-if="dataDDLTable_2.length != 0 && ($store.state.year == $store.state.currentYear) && !isUpdatedScore_2 && canUpdateByYear"
              @click="editTable2"
            >
              Cập nhật
            </v-btn>
          </v-card>
        </v-col>
      </v-row>
    </v-card>
    <ConfigScoreboardDialog
      ref="configScoreboardDialog"
      :dataKhoi="dataKhoi"
      :headerTable1="header_table_1"
      :headerTable2="header_table_2"
      :txtTitle="txtTitle"
      :dataKyHoc="dataKyHoc"
    />
    <ToastMessage ref="toastMessage" />
    <Loading ref="Loading"></Loading>
  </div>
</template>

<script>
import AppService from "@/services/app.service";
import ConfigScoreboardDialog from "@/views/Dialogs/ConfigScoreboardDialog";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";

export default {
  name: "ConfigScoreboard",
  components: {
    ConfigScoreboardDialog,
    ToastMessage,
    Loading,
  },
  data() {
    return {
      dataKhoi: [],
      choosedKhoi: null,
      rules: [],
      objectSearch: {},
      dataDDLTable_1: [],
      choosedDDLTable_1: null,
      dataDDLTable_2: [],
      choosedDDLTable_2: null,
      header_table_1: [
        { text: "STT", value: "stt", align: 'center' },
        { text: "Cột điểm", value: "name" },
        { text: "Hệ số", value: "coefficient" },
        { text: "Số lượng", value: "quantity" },
        { text: "Số lượng tối thiểu", value: "minimum_score" },
      ],
      header_table_2: [
        { text: "STT", value: "stt", align: 'center'},
        { text: "Cột xếp loại", value: "name" },
        { text: "Kiểu chọn", value: "type_choose" },
        { text: "Giá trị lựa chọn", value: "selected_value" },
      ],
      desserts_table_1: [],
      desserts_table_2: [],
      subjectCodeTable1: null,
      subjectCodeTable2: null,
      txtTitle: null,
      choosedGradeId: null,
      parent_code_1: null,
      parent_code_2: null,
      choosedKyHoc: null,
      dataKyHoc: null,
      isUpdatedScore_1: false,
      isUpdatedScore_2: false,
      canUpdateByYear: false,
      nameKyHoc: null,
      years: []
    };
  },
  async mounted() {
    this.$refs["Loading"].open();
     
    await Promise.all([
      this.getAllYears(),
      this.$store.dispatch("getCurrentUser"),
      this.getAllKhoi(),
      this.getAmountOfSemester(),
    ]);
    await this.getObjCurrentYear();
    await this.search()
    this.$refs["Loading"].close();
  },
  methods: {
    async search() {
      this.$refs["Loading"].open();
      await Promise.all([
        this.getSubjectByDeptAndSupType(
          this.objectSearch.grade_level,
          0,
          this.$store.getters["year"],
          this.choosedGradeId,
          this.choosedKyHoc
        ),
        this.getSubjectByDeptAndSupType(
          this.objectSearch.grade_level,
          1,
          this.$store.getters["year"],
          this.choosedGradeId,
          this.choosedKyHoc
        ),
      ]);
      if(this.$store.state.year < this.$store.state.currentYear){
        this.canUpdateByYear = false
      }else{
        this.canUpdateByYear = true
      }
      this.$refs["Loading"].close();
    },
    getAmountOfSemester() {
      return AppService.getAmountOfSemester(this.$store.getters["year"]).then(
        (result) => {
          this.dataKyHoc = result.data.semesterAmount;
          if (this.dataKyHoc != 0) {
            this.chooseKyHoc(1);
          }
        }
      );
    },
    chooseKhoi(item) {
      this.choosedGradeId = item.id;
      this.substringKhoi(item.name);
    },
    chooseDDLTable_1(item) {
      this.choosedDDLTable_1 = item.name;
      this.subjectCodeTable1 = item.code;
      this.parent_code_1 = item.cssCode;
      this.isUpdatedScore_1 = item.status == 1 ? true : false;
    },
    chooseDDLTable_2(item) {
      this.choosedDDLTable_2 = item.name;
      this.subjectCodeTable2 = item.code;
      this.parent_code_2 = item.cssCode;
      this.isUpdatedScore_2 = item.status == 1 ? true : false;
    },
    getAllKhoi() { 
      return AppService.getAllKhoi().then((result) => {
        this.dataKhoi = result.data.data;
        this.choosedGradeId = this.dataKhoi[0].id
        this.substringKhoi(this.dataKhoi[0].name);
      });
    },
    substringKhoi(name) {
      this.choosedKhoi = name;
      let index = this.choosedKhoi.indexOf(" ");
      let resultKhoi = this.choosedKhoi
        .substring(index, this.choosedKhoi.length)
        .trim();
      this.objectSearch.grade_level = resultKhoi;
    },
    createNew() {
      this.txtTitle = "TẠO MỚI CẤU HÌNH BẢNG ĐIỂM";
      this.$refs["configScoreboardDialog"]
        .open()
        .then(() => {
          this.$refs["toastMessage"].open("Cập nhật thành công", false);
        })
        .catch(() => {});
    },
    editTable1() {
      this.txtTitle = "CẬP NHẬT CẤU HÌNH BẢNG ĐIỂM MÔN TÍNH ĐIỂM";
      this.$refs["configScoreboardDialog"]
        .open(
          this.desserts_table_1,
          0,
          true,
          {
            name: this.choosedDDLTable_1,
            code: this.subjectCodeTable1,
            cssCode: this.parent_code_1,
          },
          { name: this.choosedKhoi, grade_id: this.choosedGradeId },
          this.choosedKyHoc
        )
        .then(() => {
          // this.$refs["toastMessage"].open("Cập nhật thành công", false);
          AppService.getConfigScoreSubject(this.parent_code_1).then(
            (result) => {
              this.desserts_table_1 = result.data.data.data;
              this.desserts_table_1.forEach((element, index) => {
                element.stt = index + 1;
              });
            }
          );
        })
        .catch(() => {});
    },
    editTable2() {
      this.txtTitle = "CẬP NHẬT CẤU HÌNH BẢNG ĐIỂM MÔN XẾP LOẠI";
      this.$refs["configScoreboardDialog"]
        .open(
          this.desserts_table_2,
          1,
          true,
          {
            name: this.choosedDDLTable_2,
            code: this.subjectCodeTable2,
            cssCode: this.parent_code_2,
          },
          { name: this.choosedKhoi, grade_id: this.choosedGradeId },
          this.choosedKyHoc
        )
        .then(() => {
          // this.$refs["toastMessage"].open("Cập nhật thành công", false);
          AppService.getConfigGradingDetails(this.parent_code_2).then(
            (result) => {
              this.desserts_table_2 = result.data.data.data;
              this.desserts_table_2.forEach((element, index) => {
                element.stt = index + 1;
                element.type_choose = element.type_choose == 1 ? true : false;
              });
            }
          );
        })
        .catch(() => {});
    },
    getSubjectByDeptAndSupType(grade_level, type, year, grade_id, semester) {
      return AppService.getSubjectByDeptAndSupType(
        grade_level,
        type,
        year,
        grade_id,
        semester
      )
        .then((result) => {
          if (type == 0) {
            this.dataDDLTable_1 = result.data.data.data;
            if (this.dataDDLTable_1.length == 0) {
              this.choosedDDLTable_1 = "";
              this.desserts_table_1 = [];
            } else {
              if (this.subjectCodeTable1 == this.dataDDLTable_1[0].code) {
                this.chooseDDLTable_1(this.dataDDLTable_1[0]);
                AppService.getConfigScoreSubject(
                  this.dataDDLTable_1[0].cssCode
                ).then((result) => {
                  this.desserts_table_1 = result.data.data.data;
                  this.desserts_table_1.forEach((element, index) => {
                    element.stt = index + 1;
                  });
                });
              } else {
                this.chooseDDLTable_1(this.dataDDLTable_1[0]);
              }
            }
          } else {
            this.dataDDLTable_2 = result.data.data.data;
            if (this.dataDDLTable_2.length == 0) {
              this.choosedDDLTable_2 = "";
              this.desserts_table_2 = [];
            } else {
              if (this.subjectCodeTable2 == this.dataDDLTable_2[0].code) {
                this.chooseDDLTable_2(this.dataDDLTable_2[0]);
                AppService.getConfigGradingDetails(
                  this.dataDDLTable_2[0].cssCode
                ).then((result) => {
                  this.desserts_table_2 = result.data.data.data;
                  this.desserts_table_2.forEach((element, index) => {
                    element.stt = index + 1;
                    element.type_choose =
                      element.type_choose == 1 ? true : false;
                  });
                });
              } else {
                this.chooseDDLTable_2(this.dataDDLTable_2[0]);
              }
            }
          }
        })
        .catch(() => {
          this.$refs["toastMessage"].open("Tải dữ liệu thất bại", true);
        });
    },
    formatTextOverLength(txt, length) {
      if (txt != null && txt.length >= length) {
        return txt.substring(0, 50) + "...";
      }
      return txt;
    },
    chooseKyHoc(item) {
      this.nameKyHoc = this.formatKyHoc(item)
      this.choosedKyHoc = item;
    },
    formatKyHoc(item) {
      switch (parseInt(item)) {
        case 1: {
          return "Học kỳ I";
        }
        case 2: {
          return "Học kỳ II";
        }
      }
      return null;
    },
    getAllYears() {
      return AppService.getAllYear().then((result) => {
        this.years = result.data.data;
      });
    },
    getObjCurrentYear(){
      return AppService.getObjCurrentYear()
      .then((res) => {
        if(res.data.status === 'OK'){
          this.$store.dispatch("updateCurrentYear", res.data.data.years);
          this.$store.dispatch("updateYear", res.data.data.years);
          this.$store.dispatch("updateCurrentSemester", res.data.data.semester)
        }else{
          this.$store.dispatch("updateYear", this.years[0]);
        }
      })
      .catch(() => {
        this.$store.dispatch("updateYear", this.years[0]);
      })
    }
  },
  watch: {
    subjectCodeTable1() {
      AppService.getConfigScoreSubject(this.parent_code_1).then((result) => {
        this.desserts_table_1 = result.data.data.data;
        this.desserts_table_1.forEach((element, index) => {
          element.stt = index + 1;
        });
      });
    },
    subjectCodeTable2() {
      AppService.getConfigGradingDetails(this.parent_code_2).then((result) => {
        this.desserts_table_2 = result.data.data.data;
        this.desserts_table_2.forEach((element, index) => {
          element.stt = index + 1;
          element.type_choose = element.type_choose == 1 ? true : false;
        });
      });
    },
    async "$store.state.year"() {
      this.$refs['Loading'].open()
      await this.getAmountOfSemester()
      this.dataDDLTable_1 = [];
      this.dataDDLTable_2 = [];
      this.choosedDDLTable_1 = null;
      this.choosedDDLTable_2 = null;
      this.desserts_table_1 = [];
      this.desserts_table_2 = [];
      await this.search()
      this.$refs['Loading'].close()
    }
  },
};
</script>