<template>
  <div class="indiScore">
    <v-container class="StudentInformation ma-0 pa-0" fluid height="100%">
      <v-row class="fill-height">
        <v-col cols="12" class="ma-1">
          <v-row class="mb-5 mt-0 mr-1">
            <v-btn-toggle v-model="toggle_top" mandatory color="primary">
              <v-btn @click="$router.push('/hoc-sinh/ho-so-hoc-sinh?student_code=' + $route.query.student_code)">Chi tiết hồ sơ</v-btn>
              <v-btn @click="$router.push('/hoc-sinh/diem-hoc-tap?student_code=' + $route.query.student_code)">Điểm học tập</v-btn>
            </v-btn-toggle>
          </v-row>
          <v-row class="school-year-indi">
            <v-col md="2" class="pa-0 pr-4">
              <v-card color="rgb(72 143 239)" class="mb-2">
                <v-card-title
                  ><p
                    class="ma-0"
                    style="color: white; text-align: center; width: 100%"
                  >
                    Năm học
                  </p></v-card-title
                >
              </v-card>
              <v-btn-toggle class="v-btn-toggle-indi" v-model="toggle_year" mandatory color="primary" style="width: 100%;">
                <v-btn v-for="year in years" @click="getTableScoreForIndi(null, year)" style="width: 100%;"
                :key="year.year">
                <div>
                  <p class="ma-0">{{ year.year }}</p>
                  <p class="ma-0">{{ year.className }}</p>
                </div>

                </v-btn>
              </v-btn-toggle>
              <!-- <v-card
                v-for="year in years"
                :key="year.year"
                color="#488fef"
                class="mb-2"
                @click="getTableScoreForIndi(null, year)"
              >
                <v-card-title style="">
                  <p
                    class="ma-0"
                    style="color: white; text-align: center; width: 100%"
                  >
                    {{ year.year }}
                  </p>
                </v-card-title>
                <v-card-title>
                  <p
                    class="ma-0"
                    style="color: white; text-align: center; width: 100%"
                  >
                    {{ year.className }}
                  </p>
                </v-card-title>
              </v-card> -->
            </v-col>
            <v-col md="10" class="pa-0">
              <v-row style="width: 100%">
                <table style="width: 100%" class="mt-3 ml-3">
                  <tr>
                    <th
                      style="background-color: rgb(72 143 239); color: white;border-radius: 6px 0px 0px 0px"
                      colspan="4"
                    >
                      HỌC KỲ I
                    </th>
                    <th
                      style="background-color: rgb(72 143 239); color: white"
                      colspan="4"
                    >
                    HỌC KỲ II
                    </th>
                    <th
                      style="background-color: rgb(72 143 239); color: white;border-radius: 0px 6px 0px 0px"
                      colspan="4"
                    >
                      CẢ NĂM
                    </th>
                  </tr>
                  <tr>
                    <th>Điểm TB</th>
                    <th>Học lực</th>
                    <th>Hạnh kiểm</th>
                    <th>Danh hiệu</th>
                    <th>Điểm TB</th>
                    <th>Học lực</th>
                    <th>Hạnh kiểm</th>
                    <th>Danh hiệu</th>
                    <th>Điểm TB</th>
                    <th>Học lực</th>
                    <th>Hạnh kiểm</th>
                    <th>Danh hiệu</th>
                  </tr>
                  <tr v-if="tableTop.length != 0">
                    <td class="text-center">{{ transferScore(tableTop[0].avgScore) }}</td>
                    <td class="text-center">{{ transferHocLuc(tableTop[0].academic_ability) }}</td>
                    <td class="text-center">{{ transferHanhKiem(tableTop[0].conduct) }}</td>
                    <td class="text-center">{{ transferDanhHieu(tableTop[0].competition_title) }}</td>
                    <td class="text-center">{{ transferScore(tableTop[1].avgScore) }}</td>
                    <td class="text-center">{{ transferHocLuc(tableTop[1].academic_ability) }}</td>
                    <td class="text-center">{{ transferHanhKiem(tableTop[1].conduct) }}</td>
                    <td class="text-center">{{ transferDanhHieu(tableTop[1].competition_title) }}</td>
                    <td class="text-center">{{ transferScore(tableTop[2].avgScore) }}</td>
                    <td class="text-center">{{ transferHocLuc(tableTop[2].academic_ability) }}</td>
                    <td class="text-center">{{ transferHanhKiem(tableTop[2].conduct) }}</td>
                    <td class="text-center">{{ transferDanhHieu(tableTop[2].competition_title) }}</td>
                  </tr>
                </table>
              </v-row>
              <v-row align="center" justify="center" class="my-10">
                <v-btn-toggle
                  v-model="toggle_exclusive"
                  mandatory
                  color="primary"
                  rounded
                >
                  <v-btn @click="getTableScoreForIndi(1)">Học kỳ I</v-btn>
                  <v-btn @click="getTableScoreForIndi(2)">Học kỳ II</v-btn>
                </v-btn-toggle>
              </v-row>
              <v-row class="mb-8 mx-0" style="width: 100%" align="center" justify="center">
                <v-card width="100%" class="mr-3">
                    <v-toolbar 
                      flat 
                      dense
                      dark
                      class="font-weight-bold"
                      color="primary lighten-1"
                      style="border-radius: 5px 5px 0px 0px"
                    >
                      <v-toolbar-title
                        style="width: 100%; align-items: center"
                        class="d-flex"
                      >
                        <span>Kết quả các môn học</span>
                      </v-toolbar-title>
                    </v-toolbar>
                    <v-data-table
                      style="width: 100%;"
                      class="mr-4"
                      :headers="headers"
                      :items="desserts"
                      :items-per-page="-1"
                      disable-sort
                      hide-default-footer
                      :footer-props="{
                        'items-per-page-text': 'Số dòng mỗi trang:',
                      }"
                    >

                    <template slot="no-data">
                      Danh sách điểm rỗng
                    </template>

                      <template v-slot:item.act="{ item }">
                          <v-icon @click="showDetailIndi(item)"> mdi-eye </v-icon>
                      </template>

                      <template v-slot:footer.page-text="props">
                        {{ props.pageStart }}-{{ props.pageStop }} của
                        {{ props.itemsLength }} kết quả
                      </template>

                    </v-data-table>
                </v-card>
              </v-row>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <ToastMessage ref="toastMessage" style="display: fixed; bottom: 0">
      </ToastMessage>
      <Loading ref="loading"></Loading>
      <IndividualScoreHistoryDialog ref="individualScoreHistoryDialog"/>
    </v-container>
  </div>
</template>



<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";
import IndividualScoreHistoryDialog from "@/views/Dialogs/IndividualScoreHistoryDialog.vue"

export default {
  components: { ToastMessage, Loading, IndividualScoreHistoryDialog},
  data() {
    return {
      years: [],
      choosedYear: null,
      toggle_exclusive: 0,
      toggle_top: 1,
      classCode: null,
      headers: [
        { text: 'STT', value: 'stt', align: 'center'},
        { text: 'Môn học', value: 'subjectName'},
        { text: 'Điểm trung bình', value: 'avgScore', align: "center"},
        { text: 'Đánh giá của giáo viên bộ môn', value: 'evaluate'},
        { text: 'Xem chi tiết', value: 'act', align: 'center'}
      ],
      desserts: [],
      semester: 1,
      subjectCode: null,
      classId: null,
      tableTop: [],
      toggle_year: 0,
    };
  },
  async mounted(){
    await this.getHistoryYear()
    await this.getResultStudyEver()
    await this.getTableScoreForIndi(1, null)
  },
  methods: {
    getHistoryYear(){
        return AppService.getAllYearHistoryOfStudent(this.$route.query.student_code)
        .then((res) => {
            this.years = res.data.data
            if(this.years.length != 0){
                this.classCode = this.years[0].classCode
                this.choosedYear = this.years[0].year
                this.classId = this.years[0].classId
            }
        })
        .catch(() => {
            this.$refs['toastMessage'].open('Không tồn tại học sinh này', true)
        })
    },
    getTableScoreForIndi(semester, year){
        if(semester){
            this.semester = semester
        }
        if(year){
          this.choosedYear = year.year
          this.classCode = year.classCode
          this.classId = year.classId
          this.getResultStudyEver()
        }
        this.$refs['loading'].open()
        return AppService.getTableScoreForIndi({
            year: this.choosedYear,
            classCode: this.classCode,
            studentCode: this.$route.query.student_code,
            semester: this.semester
        })
        .then((res) => {
            if(res.data.status === 'OK'){
                this.desserts = res.data.data
                this.desserts.forEach((x, index) => {
                    x.stt = index + 1
                })
            }else{
                this.desserts = []
                this.$refs['toastMessage'].open('Không có dữ liệu về học sinh này', true)
            }
        })
        .catch(() => {
            this.desserts = []
            this.$refs['toastMessage'].open('Không có dữ liệu về học sinh này', true)
        })
        .finally(() => {
          this.$refs['loading'].close()
        })
    },
    showDetailIndi(item){
      this.$refs['individualScoreHistoryDialog'].open(item, item.subType === 0 ? 'score':'grade')
      .then(() => {

      })
      .catch(() => {})
    },
    getResultStudyEver(){
      return AppService.getResultStudyEver({
        classCode: this.classCode,
        year: this.choosedYear,
        studentCode: this.$route.query.student_code,
        classId: this.classId
      })
      .then((res) => {
        this.tableTop = res.data.data
      })
      .catch(() => {
        this.$refs['toastMessage'].open('Không tồn tại học sinh này', true)
      })
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
    transferHanhKiem(text){
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
      return null;
    },
    transferDanhHieu(text){
      switch (text) {
        case "excellent": {
          return "Học sinh giỏi";
        }
        case "excelent": {
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
      return null;
    },
    transferScore(score){
      if(isNaN(score)){
        return null
      }else{
        return score.toFixed(2)
      }
    }
  },
};
</script>

<style>
.indiScore table{
  border-collapse: collapse;
}

.indiScore th,
.indiScore td {
    border: 1px solid #eeeeee;
}
.active-btn-indi {
  color: white;
  background-color: #488fef;
}
.indiScore .school-year-indi .v-card__title {
  padding: 0px !important;
}
.v-btn-toggle-indi {
  display: block !important;
}
</style>