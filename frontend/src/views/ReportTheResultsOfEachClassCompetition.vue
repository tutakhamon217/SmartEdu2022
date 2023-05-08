<template>
  <div>
    <v-card class="mb-3">
      <v-toolbar
        dense
        dark
        class="font-weight-bold"
        color="primary lighten-1"
      >
        <v-toolbar-title class="pa-0">
          Thông tin tìm kiếm
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
                    label="Lựa chọn khối (*)"
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
                    label="Lựa chọn khoa/ban (*)"
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
                    :label="
                      dataLop.length == 0
                        ? 'Không có lớp nào'
                        : 'Lựa chọn lớp (*)'
                    "
                    :disabled="dataLop.length == 0"
                    readonly
                    v-model="choosedLop"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list
                  style="max-height: 50vh; overflow-y: scroll"
                  v-if="dataLop.length != 0"
                >
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
    <v-card class="mb-3 mt-3">
      <v-toolbar 
        flat 
        dense
        dark
        class="font-weight-bold"
        color="primary lighten-1"
        >
          <v-toolbar-title
              style="width: 100%; align-items: center"
              class="d-flex"
          >
            <span>Kết quả thi đua</span>
            <v-spacer></v-spacer>
          </v-toolbar-title>
      </v-toolbar>
      <v-data-table
        :custom-sort="sortByName"
        :items="desserts"
        :headers="headers"
        :items-per-page="10"
        :footer-props="{
          'items-per-page-text': 'Số dòng mỗi trang:',
        }"
        :header-props="{ sortIcon: null }"
        class="customHeaderEachClass"
      >
            <template slot="no-data">
              Danh sách kết quả thi đua rỗng
            </template>

          <template v-slot:header.stt="{ header }">
            <p class="ma-0" style="height: 150px;display:flex;align-items:center;">{{ header.text }}</p>
          </template>

        <template v-slot:item.number="{ item }">
          <v-row>
            <v-col class="text-center" style="background-color: yellow" sm="6">{{
              item.number_off_allowed
            }}</v-col>
            <v-col class="text-center" style="background-color: red" sm="6">{{
              item.number_off
            }}</v-col>
          </v-row>
        </template>

        <template v-slot:item.studentCode="{ item }">
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <span v-bind="attrs" v-on="on">{{
                formatTextTooltip(item.studentCode, 16)
              }}</span>
            </template>
            <span>{{ item.studentCode }}</span>
          </v-tooltip>
        </template>

        <template v-slot:item.studentName="{ item }">
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <span v-bind="attrs" v-on="on">{{
                formatTextTooltip(item.studentName, 30)
              }}</span>
            </template>
            <span>{{ item.studentName }}</span>
          </v-tooltip>
        </template>

        <template v-slot:item.conduct="{ item }">
          <span>{{ formatConduct(item.conduct) }}</span>
        </template>
        <template v-slot:item.competition_title="{ item }">
          <span>{{ formatCompetitionTitle(item.competition_title) }}</span>
        </template>
        <template v-slot:item.academic_ability="{ item }">
          <span>{{ formatAcademicAbility(item.academic_ability) }}</span>
        </template>

        <template v-slot:footer.page-text="props">
          {{ props.pageStart }}-{{ props.pageStop }} của
          {{ props.itemsLength }} kết quả
        </template>

        <template v-if="ratioScroll !== 0" v-slot:body.append>
          <tr>
            <td :colspan="3"></td>
            <td :colspan="5" class="pa-0">
              <div @scroll="onScroll" class="scroll-bar-table-report">
                _________________________________________________________________________________________
              </div>
            </td>
            <td :colspan="6"></td>
          </tr>
        </template>
      </v-data-table>
    </v-card>
    <v-row>
      <v-col>
        <h3 style="color:red">Chú thích:</h3>
        <p class="ma-0">- Cột số ngày nghỉ màu vàng là <span style="color: #ffc107;font-weight: bold">số ngày nghỉ có phép</span></p>
        <p>- Cột số ngày nghỉ màu đỏ là <span style="color: red;font-weight: bold">số ngày nghỉ không phép</span></p>
      </v-col>
    </v-row>
    <Loading ref="loading" />
    <ToastMessage class="bottom-toast-mess" ref="toastMessage" />
  </div>
</template>

<script>
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage";

export default {
  name: "ReportTheResultsOfEachClassCompetition",
  components: {
    ToastMessage,
    Loading,
  },
  data() {
    return {
      desserts: [],
      headers: [
        { text: "STT", value: "stt", align: "center", width: "50px" },
        { text: "Mã học sinh", value: "studentCode", align: "center", width: "75px" },
        { text: "Họ và tên", value: "studentName" },
        { text: "Điểm TB", value: "avg", width: "55px", align: "center" },
        {
          text: "Học lực",
          value: "academic_ability",
          width: "55px",
          align: "center",
        },
        { text: "Hạnh kiểm", value: "conduct", width: "55px", align: "center" },
        {
          text: "Số ngày nghỉ",
          value: "number",
          width: "80px",
          align: "center",
        },
        {
          text: "Danh hiệu",
          value: "competition_title",
          width: "100px",
          align: "center",
        },
        { text: "Xếp hạng", value: "rank", width: "55px", align: "center" },
      ],
      defaultHeader: [
        { text: "STT", value: "stt", align: "center", width: "50px" },
        { text: "Mã học sinh", value: "studentCode", align: "center", width: "75px" },
        { text: "Họ và tên", value: "studentName" },
        { text: "Điểm TB", value: "avg", width: "55px", align: "center" },
        {
          text: "Học lực",
          value: "academic_ability",
          width: "55px",
          align: "center",
        },
        { text: "Hạnh kiểm", value: "conduct", width: "55px", align: "center" },
        {
          text: "Số ngày nghỉ",
          value: "number",
          width: "80px",
          align: "center",
        },
        {
          text: "Danh hiệu",
          value: "competition_title",
          width: "100px",
          align: "center",
        },
        { text: "Xếp hạng", value: "rank", width: "55px", align: "center" },
      ],
      dataKhoi: [],
      choosedKhoi: null,
      dataKhoaBan: [],
      choosedKhoaBan: null,
      amountOfSemester: 0,
      dataLop: [],
      choosedLop: null,
      rules: [(v) => !!v || "Không có lớp nào để tìm kiếm"],
      choosedHocKy: null,
      objectSearch: {},
      subjects: [],
      ratioScroll: 0,
      changeStatus: false,
      firstLoadDone: false,
      prevRoute: null,
      watchYear: 1
    };
  },
  async mounted() {
    // if(this.prevRoute === '/'){
    //   this.watchYear = this.$watch('$store.state.year', async () => {
    //     this.desserts = [];
    //     this.$refs["loading"].open();
    //     await this.getAllKhoi();
    //     await this.getAllKhoaBan();
    //     await this.getAmountOfSemester();
    //     await this.loadWhenChooseOption()
    //     this.watchYear()
    //     this.watchYear = null
    //     this.$watch('choosedKhoi', async () => {
    //       this.$refs["loading"].open();
    //       await this.loadWhenChooseOption();
    //       this.$refs["loading"].close();
    //     })
    //     this.$watch('choosedKhoaBan', async() => {
    //         this.$refs["loading"].open();
    //         await this.loadWhenChooseOption();
    //         this.$refs["loading"].close();
    //     })
    //     this.$refs["loading"].close();
    //   })
    // }else{
      this.$refs["loading"].open();
      await this.getAllKhoi();
      await this.getAllKhoaBan();
      await this.getAmountOfSemester()
      await this.loadWhenChooseOption()
      if(this.$route.params.classCode){
        this.chooseLop(this.dataLop.find(x => x.code == this.$route.params.classCode))
      }
      await this.search()
      this.watchYear = null
      this.$watch('choosedKhoi', async () => {
        this.$refs["loading"].open();
        await this.loadWhenChooseOption();
        this.$refs["loading"].close();
      })
      this.$watch('choosedKhoaBan', async() => {
          this.$refs["loading"].open();
          await this.loadWhenChooseOption();
          this.$refs["loading"].close();
      })
      this.$refs["loading"].close();
    // }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.prevRoute = from.fullPath
    })
  },
  methods: {
    sortByName(items, index, isDescending) {
      items.sort((a, b) => {
        return a.studentName.split(" ").slice(-1)[0].toLowerCase().localeCompare(b.studentName.split(" ").slice(-1)[0].toLowerCase());
      });
      let countI = 0
      for(let ii of items) {
        countI += 1
        ii.stt = countI
      }
      return items;
    },
    chooseKhoaBan(item) {
      this.objectSearch.deptId = item.id;
      this.choosedKhoaBan = item.name;
    },
    chooseKhoi(item) {
      this.objectSearch.gradeLevel = item.id;
      this.choosedKhoi = item.name;
    },
    chooseLop(item) {
      this.objectSearch.classCode = item.code;
      this.choosedLop = item.name;
    },
    chooseHocKy(item) {
      this.objectSearch.hocKy = item;
      switch (parseInt(item)) {
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
    loadWhenChooseOption() {
      return AppService.getClassByKhoiAndKhoaBan(
        this.objectSearch.gradeLevel,
        this.objectSearch.deptId,
        this.$store.getters["year"]
      )
        .then((data) => {
          this.dataLop = []
          this.dataLop = data.data.data.subjects;
          if (this.dataLop.length != 0) {
            this.choosedLop = this.dataLop[0].name;
            this.objectSearch.classCode = this.dataLop[0].code;
          } else {
            this.choosedLop = null;
            this.objectSearch.classCode = null;
            this.$refs["form-search"].resetValidation();
          }
        })
    },
    getAmountOfSemester() {
      return AppService.getAmountOfSemester(this.$store.getters["year"])
        .then((result) => {
          this.amountOfSemester = result.data.semesterAmount;
          if(this.$route.params.classCode && !this.firstLoadDone){
            this.chooseHocKy(this.$route.params.semester)
          }else{
            this.chooseHocKy(0);
          }
        })
        .catch(() => {});
    },
    getAllKhoi() {
      return AppService.getAllKhoi().then((result) => {
        this.dataKhoi = result.data.data;
        if(!this.$route.params.classCode || this.firstLoadDone){
          this.choosedKhoi = this.dataKhoi[0].name;
          this.objectSearch.gradeLevel = this.dataKhoi[0].id;
        }else{
          this.chooseKhoi(this.dataKhoi.find(x => x.id == this.$route.params.gradeLevel))
        }
      });
    },
    getAllKhoaBan() {
      return AppService.getAllDepartmentForClass().then((data) => {
        this.dataKhoaBan = data.data.data;
        if(!this.$route.params.classCode || this.firstLoadDone){
          this.choosedKhoaBan = this.dataKhoaBan[0].name;
          this.objectSearch.deptId = this.dataKhoaBan[0].id;
        }else{
          this.chooseKhoaBan(this.dataKhoaBan.find(x => x.id == this.$route.params.deptId))
        }
      });
    },
    search() {
      if (this.$refs["form-search"].validate()) {
        this.$refs["loading"].open();
        AppService.getReportTheResultOfEachClass({
          year: this.$store.getters["year"],
          classCode: this.objectSearch.classCode,
          semester: this.objectSearch.hocKy,
        })
          .then((res) => {
            if(res.data.status !== 'OK'){
              this.$refs['toastMessage'].open(res.data.message, true)
              this.desserts = []
              this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
              this.subjects = []
            }else{
              this.desserts = res.data.data[0].desserts;
              this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
              this.subjects = res.data.data[1].subjects;
              if (this.subjects.length <= 5) {
                this.ratioScroll = 0;
              } else if (this.subjects.length <= 10) {
                this.changeStatus = 0.5;
                this.ratioScroll = 0.5;
              } else {
                this.ratioScroll = 0.33;
                this.changeStatus = 0.33;
              }
              if (this.subjects.length <= 5) {
                res.data.data[1].subjects.forEach((x, index) => {
                  this.headers.splice(3 + index, 0, {
                    text: x,
                    value: x,
                    align: "center",
                    width: "55px",
                  });
                });
              } else {
                for (let i = 0; i < 5; i++) {
                  this.headers.splice(3 + i, 0, {
                    text: this.subjects[i],
                    value: this.subjects[i],
                    align: "center",
                    width: "55px",
                  });
                }
              }

              this.desserts.forEach((x, index) => {
                x.stt = index + 1;
                Object.keys(x).forEach((prop) => {
                  if (x[prop] === null || x[prop] === "") {
                    x[prop] = "-";
                  }
                });
              });
            }
          })
          .catch(() => {
            this.desserts = []
            this.$refs["toastMessage"].open("Không có dữ liệu", true);
          })
          .finally(() => {
            this.$refs["loading"].close();
          });
      }
    },
    formatTextTooltip(item, max) {
      if (item.length > max) {
        return item.substring(0, max) + "...";
      }
      return item;
    },
    formatConduct(text) {
      switch (text) {
        case "excellent": {
          return "Tốt";
        }
        case "good": {
          return "Khá";
        }
        case "medium": {
          return "Trung bình";
        }
        case "weak": {
          return "Yếu";
        }
      }
      return "-";
    },
    formatCompetitionTitle(text) {
      switch (text) {
        case "excelent": {
          return "Học sinh giỏi";
        }
        case "excellent": {
          return "Học sinh giỏi";
        }
        case "good": {
          return "Học sinh tiên tiến";
        }
        case "medium": {
          return "Học sinh trung bình";
        }
        case "weak": {
          return "Học sinh yếu";
        }
      }
      return "-";
    },
    formatAcademicAbility(text) {
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
      return "-";
    },
    onScroll(event) {
      if (this.ratioScroll == 0.5) {
        if(event.target.scrollLeft <= (0.5 * event.target.offsetWidth)){
          if (this.changeStatus == 1) {
            this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
            for (let i = 0; i < 5; i++) {
              this.headers.splice(3 + i, 0, {
                text: this.subjects[i],
                value: this.subjects[i],
                align: "center",
                width: "55px",
              });
            }
            this.changeStatus = 0.5;
          }
        }else{
          if (this.changeStatus == 0.5) {
            this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
            let temp = 0;
            for (let i = this.subjects.length - 5; i < this.subjects.length; i++) {
              this.headers.splice(3 + temp++, 0, {
                text: this.subjects[i],
                value: this.subjects[i],
                align: "center",
                width: "55px",
              });
            }
            this.changeStatus = 1;
          }
        }
      } else {
        if(event.target.scrollLeft <= (0.33 * event.target.offsetWidth)){
          if (this.changeStatus == 0.66) {
            this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
            for (let i = 0; i < 5; i++) {
              this.headers.splice(3 + i, 0, {
                text: this.subjects[i],
                value: this.subjects[i],
                align: "center",
                width: "55px",
              });
            }
            this.changeStatus = 0.33;
          }
        }
        if(event.target.scrollLeft > (0.33 * event.target.offsetWidth) && event.target.scrollLeft <= (0.66 * event.target.offsetWidth)){
          if (this.changeStatus == 0.33 || this.changeStatus == 1) {
            this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
            let temp = 0;
            for (let i = 5; i < 10; i++) {
              this.headers.splice(3 + temp++, 0, {
                text: this.subjects[i],
                value: this.subjects[i],
                align: "center",
                width: "55px",
              });
            }
            this.changeStatus = 0.66;
          }
        }

        if(event.target.scrollLeft > (0.66 * event.target.offsetWidth)){
          if (this.changeStatus == 0.66) {
            this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
            let temp = 0;
            for (let i = this.subjects.length - 5; i < this.subjects.length; i++) {
              this.headers.splice(3 + temp++, 0, {
                text: this.subjects[i],
                value: this.subjects[i],
                align: "center",
                width: "55px",
              });
            }
            this.changeStatus = 1;
          }
        }
      }
    }
  },
  watch: {
    watchYear(){
      if(this.watchYear === null){
        this.$watch('$store.state.year',async () => {
          this.desserts = [];
          this.$refs["loading"].open();
          // await this.getAllKhoi();
          await this.getAllKhoaBan();
          await this.getAmountOfSemester();
          await this.loadWhenChooseOption();
          await this.search()
          this.$refs["loading"].close();
        })
      }
    }
  }
};
</script>

<style>
.customHeaderEachClass table th + th {
  border-left: 1px solid #dddddd;
}
.customHeaderEachClass table td + td {
  border-left: 1px solid #dddddd;
}
.customHeaderEachClass .row {
  margin: -16px !important;
}
.scroll-bar-table-report {
  overflow-x: auto;
  max-width: 310px;
  color:white;
  margin-top: -10px;
  white-space: nowrap;
}
.scroll-bar-table-report:hover{
  background-color: #eee;
  color: #eee;
}
</style>