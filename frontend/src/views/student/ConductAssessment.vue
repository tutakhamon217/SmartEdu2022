<template>
  <div>
    <v-card v-show="isShow" class="mb-3">
      <v-toolbar 
        dense
        dark
        class="font-weight-bold"
        color="primary lighten-1"
      >
        <v-toolbar-title class="pa-0">
          <span>Thông tin tìm kiếm</span>
        </v-toolbar-title>
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
                    label="Học kỳ"
                    readonly
                    v-model="choosedHocKy"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list
                  style="max-height: 50vh; overflow-y: scroll"
                  v-if="amountOfSemester"
                >
                  <v-list-item @click="chooseHocKy(0)">
                    <v-list-item-title>{{ formatKyHoc(0) }}</v-list-item-title>
                  </v-list-item>
                  <v-list-item
                    v-for="(item, index) in amountOfSemester"
                    :key="index"
                    @click="chooseHocKy(item)"
                  >
                    <v-list-item-title>{{
                      formatKyHoc(item)
                    }}</v-list-item-title>
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
                    label="Lớp học"
                    readonly
                    v-model="choosedLop"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataLop"
                    :key="index"
                    @click="chooseLop(item)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
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
    <v-form v-show="isShow" ref="form-update">
      <v-card class="changeColorDisableInConduct">
          <v-toolbar 
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1"
          >
            <v-toolbar-title
              style="width: 100%; align-items: center"
              class="d-flex"
            >
              <span>Đánh giá hạnh kiểm {{ nameClassChoosedAfterSearch }}</span>
              <v-spacer></v-spacer>
              <v-btn style="background-color:white;color:black" v-if="desserts.length != 0 && canUpdate && !updating" @click="update" color="white"
                >Cập nhật</v-btn
              >
            </v-toolbar-title>
          </v-toolbar>

          <v-data-table
            :items="desserts"
            :headers="header"
            
            :header-props="{ sortIcon: null }"
            calculate-widths
            :items-per-page="10"
            no-data-text="Danh sách học sinh rỗng"
            :footer-props="{
              'items-per-page-text': 'Số dòng mỗi trang:',
            }"
            class="customHeader"
          >

            <!-- config header -->
            <template v-slot:header.excellent_conduct="{ header }">
              <td rowspan="2">
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.good_conduct="{ header }">
              <td rowspan="2" style="position: relative">
                <tr style="position: absolute; left: 90%; top: 0%">
                  <th><p style="width: 70px">Hạnh kiểm</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.medium_conduct="{ header }">
              <td rowspan="2">
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.weak_conduct="{ header }">
              <td rowspan="2">
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.excellent_competition="{ header }">
              <td rowspan="2">
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.good_competition="{ header }">
              <td rowspan="2" style="position: relative">
                <tr style="position: absolute; left: 0%; top: 0%">
                  <th><p style="width: 150px;position: relative;z-index: 1;">Danh hiệu thi đua</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.medium_competition="{ header }">
              <td rowspan="2">
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>

            <template v-slot:header.weak_competition="{ header }">
              <td rowspan="2">
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th><p class="mb-0">&nbsp;&nbsp;</p></th>
                </tr>
                <tr>
                  <th>{{ header.text }}</th>
                </tr>
              </td>
            </template>
            <!-- end config header -->

            <template v-slot:item.student_code="{ item }">
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <span v-bind="attrs" v-on="on">{{
                    formatTextTooltip(item.student_code, 16)
                  }}</span>
                </template>
                <span>{{ item.student_code }}</span>
              </v-tooltip>
            </template>

            <template v-slot:item.full_name="{ item }">
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <span v-bind="attrs" v-on="on">{{
                    formatTextTooltip(item.full_name, 22)
                  }}</span>
                </template>
                <span>{{ item.full_name }}</span>
              </v-tooltip>
            </template>

            <!-- hạnh kiểm -->
            <template v-slot:item.number_off="{ item }">
              <span>{{ item.number_off }}</span>
            </template>

            <template v-slot:item.number_off_allowed="{ item }">
              <span>{{ item.number_off_allowed }}</span>
            </template>

            <template v-slot:item.excellent_conduct="{ item }">
              <v-checkbox
                v-model="item.excellent_conduct"
                color="primary"
                :disabled="!updating || !item.academic_ability  || !item.enough" 
                @click="changeConduct('excellent', item, $event)"
              ></v-checkbox>
            </template>

            <template v-slot:item.good_conduct="{ item }">
              <v-checkbox
                v-model="item.good_conduct"
                color="primary"
                :disabled="!updating || !item.academic_ability  || !item.enough"
                @click="changeConduct('good', item, $event)"
              ></v-checkbox>
            </template>

            <template v-slot:item.medium_conduct="{ item }">
              <v-checkbox class="ml-4"
                v-model="item.medium_conduct"
                color="primary"
                :disabled="!updating || !item.academic_ability  || !item.enough" 
                @click="changeConduct('medium', item, $event)"
              ></v-checkbox>
            </template>

            <template v-slot:item.weak_conduct="{ item }">
              <v-checkbox
                v-model="item.weak_conduct"
                color="primary"
                :disabled="!updating || !item.academic_ability  || !item.enough" 
                @click="changeConduct('weak', item, $event)"
              ></v-checkbox>
            </template>
            <!--end hạnh kiểm -->

            <!-- danh hiệu thi đua -->
            <template v-slot:item.excellent_competition="{ item }">
              <v-checkbox
                v-model="item.excellent_competition"
                color="primary"
                :disabled="
                  !updating || !checkConduct(item) || !item.academic_ability  || !item.enough
                "
                @click="changeCompetition('excellent', item, $event)"
              ></v-checkbox>
            </template>

            <template v-slot:item.good_competition="{ item }">
              <v-checkbox class="ml-3"
                v-model="item.good_competition"
                color="primary"
                :disabled="
                  !updating || !checkConduct(item) || !item.academic_ability  || !item.enough
                "
                @click="changeCompetition('good', item, $event)"
              ></v-checkbox>
            </template>

            <template v-slot:item.medium_competition="{ item }">
              <v-checkbox class="ml-4"
                v-model="item.medium_competition"
                color="primary"
                :disabled="
                  !updating || !checkConduct(item) || !item.academic_ability  || !item.enough
                "
                @click="changeCompetition('medium', item, $event)"
              ></v-checkbox>
            </template>

            <template v-slot:item.weak_competition="{ item }">
              <v-checkbox
                v-model="item.weak_competition"
                color="primary"
                :disabled="
                  !updating || !checkConduct(item) || !item.academic_ability  || !item.enough
                "
                @click="changeCompetition('weak', item, $event)"
              ></v-checkbox>
            </template>
            <!--end danh hiệu thi đua -->

            <template v-slot:item.evaluate="{ item }">
              <v-icon
                v-if="item.evaluate != 0 && item.evaluate != null"
                @click="chooseEvaluate(item.student_code)"
                >mdi-eye</v-icon
              >
              <span v-else>Không có đánh giá</span>
            </template>

            <template v-slot:item.academic_ability="{ item }">
                <span v-if="item.academic_ability">{{
                  transferHocLuc(item.academic_ability)
                }}</span>
                <span v-else>-</span>
            </template>

            <template v-slot:footer.page-text="props">
              {{ props.pageStart }}-{{ props.pageStop }} của
              {{ props.itemsLength }} kết quả
            </template>
          </v-data-table>
      </v-card>
    </v-form>
    <v-row v-show="isShow" v-if="updating" align="center" justify="center" class="mt-3">
      <v-btn @click="cancel" class="mr-2" elevation="1" color="warning darken-1" text>Hủy bỏ</v-btn>
      <v-btn :disabled="sameOld" elevation="1" color="primary darken-1" text class="ml-2" @click="save">Lưu lại</v-btn>
    </v-row>
    <ToastMessage ref="toastMessage" />
    <Loading ref="loading"></Loading>
    <ConductStudentDialog ref="conductStudentDialog" />
    <ConfirmDialog
      ref="confirmDialog"
      txtTitle="Xác nhận cập nhật thông tin"
      question="Bạn có chắc chắn muốn lưu thông tin vừa cập nhật"
    />
  </div>
</template>

<script>
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";
import ConductStudentDialog from "@/views/Dialogs/ConductStudentDialog.vue";

export default {
  components: {
    ToastMessage,
    Loading,
    ConductStudentDialog,
    ConfirmDialog,
  },
  data() {
    return {
      isShow: true,
      objectSearch: {},
      amountOfSemester: 1,
      dataLop: [],
      choosedLop: null,
      choosedHocKy: null,
      rules: [(v) => !!v || "Không có lớp nào"],
      desserts: [],
      header: [
        { text: "STT", value: "stt", align: "center" },
        { text: "Mã học sinh", value: "student_code", align:'center' },
        { text: "Tên học sinh", value: "full_name", align: "center" },
        {
          text: "Số ngày nghỉ không phép",
          value: "number_off",
          align: "center",
          width: "80px",
        },
        {
          text: "Số ngày nghỉ có phép",
          value: "number_off_allowed",
          align: "center",
          width: "80px",
        },
        { text: "Học lực", value: "academic_ability", align: "center" },
        {
          text: "Đánh giá của giáo viên bộ môn",
          value: "evaluate",
          align: "center",
          width: "85px",
        },

        {
          text: "Tốt",
          value: "excellent_conduct",
          align: "center",
          width: "55px",
        },
        { text: "Khá", value: "good_conduct", align: "center", width: "55px" },
        {
          text: "Trung bình",
          value: "medium_conduct",
          align: "center",
          width: "95px",
        },
        { text: "Yếu", value: "weak_conduct", align: "center", width: "55px" },

        {
          text: "Giỏi",
          value: "excellent_competition",
          align: "center",
          width: "55px",
        },
        {
          text: "Tiên tiến",
          value: "good_competition",
          align: "center",
          width: "85px",
        },
        {
          text: "Trung bình",
          value: "medium_competition",
          align: "center",
          width: "95px",
        },
        {
          text: "Yếu",
          value: "weak_competition",
          align: "center",
          width: "55px",
        },
      ],
      updating: false,
      firstDesserts: [],
      rowChange: [],
      nameClassChoosedAfterSearch: "",
      dataHocLuc: [
        {
          name: "Giỏi",
          value: "excellent",
        },
        {
          name: "Khá",
          value: "good",
        },
        {
          name: "Trung bình",
          value: "average",
        },
        {
          name: "Yếu",
          value: "weak",
        },
      ],
      rulesNumber: [(v) => !isNaN(v) || "Yêu cầu", (v) => v >= 0 || ">= 0"],
      canUpdate: false,
      sameOld: true
    };
  },
  async mounted() {
    if (!this.$store.getters['user'].roles.includes('ROLE_GVCN')) {
        this.$refs["toastMessage"].open("Bạn không phải là giáo viên chủ nhiệm", true);
        this.isShow = false
        return
    }
    this.isShow = true
    this.$refs["loading"].open();
    // await this.$store.dispatch("getCurrentUser");
    await this.getAmountOfSemester();
    await this.getClassroomByTeacherIdAndYear();
    await this.search()
  },
  methods: {
  sortByName(items, index, isDescending) {
    items.sort((a, b) => {
        return a.full_name.split(" ").slice(-1)[0].toLowerCase().localeCompare(b.full_name.split(" ").slice(-1)[0].toLowerCase());
      });
      let countI = 0
      for(let ii of items) {
        countI += 1
        ii.stt = countI
      }
      return items;
    },
    chooseLop(item) {
      this.choosedLop = item.name;
      this.objectSearch.classCode = item.code;
    },
    chooseHocKy(item) {
      this.objectSearch.hocKy = item;
      switch (item) {
        case 0: {
          this.choosedHocKy = "Cả năm";
          break;
        }
        case 1: {
          this.choosedHocKy = "Học kỳ I";
          break;
        }
        case 2: {
          this.choosedHocKy = "Học kỳ II";
          break;
        }
        case 3: {
          this.choosedHocKy = "Học kỳ III";
          break;
        }
        case 4: {
          this.choosedHocKy = "Học kỳ IV";
          break;
        }
      }
    },
    formatKyHoc(item) {
      switch (item) {
        case 0: {
          return "Cả năm";
        }
        case 1: {
          return "Học kỳ I";
        }
        case 2: {
          return "Học kỳ II";
        }
        case 3: {
          return "Học kỳ III";
        }
        case 4: {
          return "Học kỳ IV";
        }
      }
      return null;
    },
    formatTextTooltip(item, max) {
      if (item.length > max) {
        return item.substring(0, max) + "...";
      }
      return item;
    },
    getAmountOfSemester() {
      return AppService.getAmountOfSemester(this.$store.getters["year"])
        .then((result) => {
          this.amountOfSemester = result.data.semesterAmount;
          if (this.amountOfSemester) {
            this.chooseHocKy(0);
          } else {
            this.choosedHocKy = null;
            this.amountOfSemester = null;
          }
        })
        .catch(() => {
          this.choosedHocKy = null;
          this.amountOfSemester = null;
          this.$refs["loading"].close();
        });
    },
    getClassroomByTeacherIdAndYear() {
      return AppService.getClassroomByTeacherIdAndYear(
        this.$store.getters["user"].id,
        this.$store.getters["year"]
      )
        .then((res) => {
          this.dataLop = res.data.data;
          this.chooseLop(this.dataLop[0]);
          this.$refs["loading"].close();
        })
        .catch(() => {
          this.choosedLop = null;
          this.dataLop = [];
          this.$refs["toastMessage"].open("Không có lớp nào", true);
          this.$refs["loading"].close();
        });
    },
    async search() {
      if (this.$refs["form-search"].validate()) {
        this.rowChange = []
        this.updating = false
        this.$refs["loading"].open();
        await this.checkUpdateConduct()
        await AppService.getInfoConductOfClass({
          semester: this.objectSearch.hocKy,
          year: this.$store.getters["year"],
          userId: this.$store.getters["user"].id,
          classCode: this.objectSearch.classCode,
        })
          .then((res) => {
            if(res.data.status === 'OK'){
              this.nameClassChoosedAfterSearch = " - " + this.choosedLop;
              let data = res.data.data;
              this.desserts = [];
              data.forEach((x, index) => {
                x.stt = index + 1;
                if(this.objectSearch.hocKy != 0){
                  x.enough = 1
                }
                //config conduct
                if (x.conduct == "excellent") {
                  x.excellent_conduct = true;
                  x.good_conduct = false;
                  x.medium_conduct = false;
                  x.weak_conduct = false;
                } else if (x.conduct == "good") {
                  x.excellent_conduct = false;
                  x.good_conduct = true;
                  x.medium_conduct = false;
                  x.weak_conduct = false;
                } else if (x.conduct == "medium") {
                  x.excellent_conduct = false;
                  x.good_conduct = false;
                  x.medium_conduct = true;
                  x.weak_conduct = false;
                } else if (x.conduct == "weak") {
                  x.excellent_conduct = false;
                  x.good_conduct = false;
                  x.medium_conduct = false;
                  x.weak_conduct = true;
                }

                //config competition
                if (x.competition_title == "excellent") {
                  x.excellent_competition = true;
                  x.good_competition = false;
                  x.medium_competition = false;
                  x.weak_competition = false;
                } else if (x.competition_title == "good") {
                  x.excellent_competition = false;
                  x.good_competition = true;
                  x.medium_competition = false;
                  x.weak_competition = false;
                } else if (x.competition_title == "medium") {
                  x.excellent_competition = false;
                  x.good_competition = false;
                  x.medium_competition = true;
                  x.weak_competition = false;
                } else if (x.competition_title == "weak") {
                  x.excellent_competition = false;
                  x.good_competition = false;
                  x.medium_competition = false;
                  x.weak_competition = true;
                } else if (x.competition_title == "excelent") {
                  x.competition_title = "excellent";
                  x.excellent_competition = true;
                  x.good_competition = false;
                  x.medium_competition = false;
                  x.weak_competition = false;
                }
                this.desserts.push(x);
              });
              this.firstDesserts = JSON.parse(JSON.stringify(this.desserts));
            }else{
              this.$refs["toastMessage"].open(res.data.message, true);
              this.canUpdate = false
              this.desserts = []
              this.rowChange = []
              this.firstDesserts = []
              this.updating = false
              this.nameClassChoosedAfterSearch = ''
            }
            this.$refs["loading"].close();
          })
          .catch((res) => {
            this.$refs["toastMessage"].open(res.message, true);
          })
          .finally(() => {
            this.$refs["loading"].close();
          })
      }else{
        this.canUpdate = false
        this.desserts = []
        this.rowChange = []
        this.firstDesserts = []
        this.updating = false
        this.nameClassChoosedAfterSearch = ''
      }
    },
    chooseEvaluate(item) {
      this.$refs["conductStudentDialog"].open(
        item,
        this.objectSearch.hocKy,
        this.objectSearch.classCode
      );
    },
    transferHocLuc(text) {
      switch (text) {
        case "excellent": {
          return "Giỏi";
        }
        case "good": {
          return "Khá";
        }
        case "average": {
          return "Trung bình";
        }
        case "weak": {
          return "Yếu";
        }
      }
      return null;
    },
    update() {
      this.updating = true;
    },
    changeCompetition(text, item) {
      let check = false;
      if (text == "weak" && item.weak_competition) {
        item.excellent_competition = false;
        item.good_competition = false;
        item.medium_competition = false;
        item.competition_title = "weak";
        check = true;
      }
      if (text == "medium" && item.medium_competition) {
        item.excellent_competition = false;
        item.good_competition = false;
        item.weak_competition = false;
        item.competition_title = "medium";
        check = true;
      }
      if (text == "good" && item.good_competition) {
        item.excellent_competition = false;
        item.medium_competition = false;
        item.weak_competition = false;
        item.competition_title = "good";
        check = true;
      }
      if (text == "excellent" && item.excellent_competition) {
        item.good_competition = false;
        item.medium_competition = false;
        item.weak_competition = false;
        item.competition_title = "excellent";
        check = true;
      }
      // this.desserts[item.stt - 1] = JSON.parse(JSON.stringify(item));
      this.rowChange.push(item.stt - 1);
      if (!check) {
        item.competition_title = null;
      }
      this.objectsEqual()
    },
    changeConduct(text, item) {
      const cache = item[text + '_conduct']
      item.excellent_conduct = false;
      item.good_conduct = false;
      item.medium_conduct = false;
      item.weak_conduct = false
      item[text + '_conduct'] = cache
      if(cache === true){
        item.conduct = text
      }else{
        item.conduct = null
        item.excellent_competition = false;
        item.good_competition = false;
        item.medium_competition = false;
        item.weak_competition = false;
        item.competition_title = null;
      }
      // this.desserts[item.stt - 1] = JSON.parse(JSON.stringify(item));
      this.rowChange.push(item.stt - 1);
      this.objectsEqual()
    },
    checkConduct(item) {
      if (
        item.conduct === null
      ) {
        return false;
      }
      return true;
    },
    save() {
      if (this.$refs["form-update"].validate()) {
        this.$refs["confirmDialog"]
          .open()
          .then(async () => {
            this.$refs["loading"].open();
            let dataSave = [];
            dataSave = this.desserts;
            await AppService.updateAssessStudentConductDetail({
              list: dataSave,
              classCode: this.objectSearch.classCode,
              semester: this.objectSearch.hocKy,
              schoolYear: this.$store.getters["year"],
            })
              .then((res) => {
                if(res.data.status === 'OK'){
                  this.$refs["toastMessage"].open(res.data.message, false);
                  this.rowChange = [];
                  this.firstDesserts = JSON.parse(JSON.stringify(this.desserts));
                  this.updating = false;
                  this.search();
                }else{
                  this.$refs["toastMessage"].open(res.data.message, true);
                }
              })
              .catch((res) => {
                this.$refs["toastMessage"].open(res.message, true);
              })
            this.$refs["loading"].close();
          })
          .catch(() => {});
      }
    },
    cancel() {
      this.desserts = JSON.parse(JSON.stringify(this.firstDesserts));
      this.updating = false;
    },
    checkUpdateConduct(){
      return AppService.checkUpdateConduct(this.objectSearch.hocKy)
      .then((res) => {
        this.canUpdate = res.data.data
      })
      .catch((res) => {
        this.$refs['toastMessage'].open(res.message, true)
        this.canUpdate = false
      })
    },
    objectsEqual () {
      this.sameOld = true
      for(let i = 0; i < this.desserts.length; i++){
          var vmm = this
          Object.keys(this.desserts[i]).forEach(function(p) {
            if(vmm.desserts[i][p] != vmm.firstDesserts[i][p]){
              vmm.sameOld = false
              return
            }
          })
      }
    }
  },
  watch: {
    async "$store.state.year"() {
      this.$refs["loading"].open();
      await this.getAmountOfSemester();
      await this.getClassroomByTeacherIdAndYear();
    }
  },
};
</script>

<style>
.customHeader tbody tr:nth-child(even) {
  background: #eee;
}
.customHeader table th + th {
  border-left: 1px solid #dddddd;
}

.customHeader .v-input--selection-controls__input {
  margin: 0px !important;
}

.customHeader table th:nth-child(8):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
}

.customHeader table th:nth-child(9):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
  border-left: 1px solid #dddddd;
}

.customHeader table th:nth-child(10):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
  border-left: 1px solid #dddddd;
}

.customHeader table th:nth-child(11):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
  border-left: 1px solid #dddddd;
}

.customHeader table th:nth-child(13):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
  border-left: 1px solid #dddddd;
}

.customHeader table th:nth-child(14):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
  border-left: 1px solid #dddddd;
}

.customHeader table th:nth-child(15):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
  border-left: 1px solid #dddddd;
}


.customHeader table td:nth-child(12),
.customHeader table td:nth-child(13),
.customHeader table td:nth-child(14),
.customHeader table td:nth-child(15)
{
  background-color: #bdd4e9;
}

.customHeader table td:nth-child(12){
  border-left: 2px solid red;
}

.customHeader table th:nth-child(12):after {
  content: "";
  position: absolute;
  height: 50%;
  width: 100%;
  left: 0;
  bottom: 0;
  border-top: 1px solid #dddddd;
}

.customHeader table th:nth-child(8) {
  position: relative;
}
.customHeader table th:nth-child(9) {
  position: relative;
  border-left: none;
}
.customHeader table th:nth-child(10) {
  position: relative;
  border-left: none;
}
.customHeader table th:nth-child(11) {
  position: relative;
  border-left: none;
}

.customHeader table th:nth-child(12) {
  position: relative;
  background-color: #bdd4e9;
  border-left: 2px solid red;
}
.customHeader table th:nth-child(13) {
  position: relative;
  border-left: none;
  background-color: #bdd4e9;
}
.customHeader table th:nth-child(14) {
  position: relative;
  background-color: #bdd4e9;
  border-left: none;
}
.customHeader table th:nth-child(15) {
  position: relative;
  border-left: none;
  background-color: #bdd4e9;
}
.customHeader table td + td {
  border-left: 1px solid #dddddd;
}
.changeColorDisableInConduct .v-input--is-label-active .v-input--selection-controls__input{
  color: rgb(72 143 239);
}
</style>