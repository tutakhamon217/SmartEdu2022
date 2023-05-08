<template>
  <v-dialog v-model="show" max-width="900" @click:outside="cancel">
    <v-data-table
      v-if="typeHeader == 'score'"
      :headers="headers"
      :items="desserts"
      :items-per-page="-1"
      disable-sort
      :hide-default-header="true"
      :hide-default-footer="true"
      :loading="loading"
      loading-text="Đang tải dữ liệu điểm số"
      :footer-props="{
        'items-per-page-text': 'Số dòng mỗi trang:',
      }"
      class="customHeaderSoDiem"
    >
      <template v-slot:top>
        <v-toolbar flat color="primary">
          <v-toolbar-title
            style="width: 100%; align-items: center"
            class="d-flex"
          >
            <p style="color: white; width: 100%;" class="text-center ma-0">THÔNG TIN CHI TIẾT</p>
            <v-btn @click="cancel" icon color="white"><v-icon>mdi-close</v-icon></v-btn>
          </v-toolbar-title>
        </v-toolbar>
      </template>

      <template v-slot:header>
        <tr style="height: 27px">
          <th
            v-for="(cus, index) in customHeader"
            :key="index"
            style="position: relative"
            :style="{
              borderLeft:
                cus.d > 1 && cus.d <= cus.quantity
                  ? 'none'
                  : '1px solid #dddddd',
            }"
          >
            <span
              v-if="cus.d == 1 && cus.quantity > 1"
              :style="{ width: 75 * cus.quantity + 'px' }"
              style="
                position: absolute;
                left: 40%;
                right: 0;
                top: 10%;
                margin-left: auto;
                margin-right: auto;
              "
              >{{ cus.text }}</span
            >
            <span v-else-if="cus.d == 1 && cus.quantity == 1">{{cus.text}}</span>
            <span v-else></span>
          </th>
          <th
            style="
              border-left: 1px solid #dddddd;
              border-right: 1px solid #dddddd;
            "
          ></th>
        </tr>

        <tr>
          <th
            v-for="(head) in headers"
            :key="head.value + 'a'"
            :style="{ width: '75px' }"
          >
            {{ head.text }}
          </th>
        </tr>
      </template>

      <template #item="{ item }">
        <tr>
          <td v-for="(cus, index) in customHeader" :key="index">
            <p class="text-center ma-0" >
              {{ !item[cus.value] ? "-" : item[cus.value] }}
            </p>
          </td>
          <td>
            <p class="text-center ma-0">{{ item.average }}</p>
          </td>
        </tr>
      </template>

      <template v-slot:footer.page-text="props">
        {{ props.pageStart }}-{{ props.pageStop }} của
        {{ props.itemsLength }} kết quả
      </template>
    </v-data-table>
    <v-data-table
      v-else
      :headers="headers"
      :items="desserts"
      :items-per-page="-1"
      :hide-default-footer="true"
      disable-sort
      :loading="loading"
      :hide-default-header="true"
      :footer-props="{
        'items-per-page-text': 'Số dòng mỗi trang:',
      }"
      class="customHeaderSoDiemXepLoai"
    >
      <template v-slot:top>
        <v-toolbar flat color="primary">
          <v-toolbar-title
            style="width: 100%; align-items: center"
            class="d-flex"
          >
            <p style="color: white; width: 100%;" class="text-center ma-0">THÔNG TIN CHI TIẾT</p>
            <v-btn @click="cancel" icon color="white"><v-icon>mdi-close</v-icon></v-btn>
          </v-toolbar-title>
        </v-toolbar>
      </template>

      <template v-slot:header>
        <th v-for="head in defaultHeader" :key="head.value + 'a'"  style="border:1px solid grey;">
          {{ head.text }}
        </th>
        <th v-for="(head, index) in customHeader" :key="index + 'b'" style="border:1px solid grey;">
          {{ head.text }}
        </th>
        <!-- <th>Nhận xét</th> -->
      </template>

      <template #item="{ item }">
        <tr>
          <td
            v-for="(cus, index) in customHeader"
            :key="index"
            style="width: 130px; border:1px solid grey;"
          >
            <p style="width: 100%" class="text-center ma-0">{{ item[cus.text] }}</p>
          </td>
        </tr>
      </template>

      <template v-slot:footer.page-text="props">
        {{ props.pageStart }}-{{ props.pageStop }} của
        {{ props.itemsLength }} kết quả
      </template>
    </v-data-table>
    <ToastMessage ref="toastMessage" style="display: fixed; bottom: 0">
    </ToastMessage>
  </v-dialog>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";

export default {
  components: {
    ToastMessage,
  },
  data() {
    return {
      show: false,
      promise: null,
      typeHeader: null,
      desserts: [],
      defaultHeader: [
      ],
      headers: [
      ],
      customHeader: [],
      dataConfigScoreForDialog: [],
      loading: false,
    };
  },

  methods: {
    async open(item, typeHeader) {
      this.show = true;
      this.loading = true;
      this.typeHeader = typeHeader;
      await AppService.searchSoDiem({
        subjectCode: item.subjectCode,
        classCode: item.classCode,
        schoolYear: item.year,
        semester: item.semester,
      })
        .then((res) => {
          this.desserts = [];
          this.customHeader = [];
          // this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
          this.headers = []
          let scoreConfig = res.data.data.ScoreConfig;
          this.dataConfigScoreForDialog = scoreConfig.map((x) => x);
          // let totalCoff = 0
          let config = {};
          if (!scoreConfig[0].code.includes("grading")) {
            this.typeHeader = "score";
            for (let i = 0; i < scoreConfig.length; i++) {
              // totalCoff += (scoreConfig[i].quantity * scoreConfig[i].coefficient)
              config[scoreConfig[i].code] = scoreConfig[i].coefficient;
              for (let j = 1; j <= scoreConfig[i].quantity; j++) {
                this.customHeader.push({
                  text: scoreConfig[i].name,
                  value: "d" + j + scoreConfig[i].code,
                  code: scoreConfig[i].code,
                  time: j,
                  align: "center",
                  d: j,
                  quantity: scoreConfig[i].quantity,
                  width: "60px",
                });
                this.headers.push({
                  text: "Đ" + j,
                  value: "d" + j + scoreConfig[i].code,
                  align: "center",
                  width: "60px",
                });
              }
            }
            this.headers.push({
              text: "Điểm trung bình",
              value: "average",
              align: "center",
            });
            let scoreList = res.data.data.ScoreList;
            for (let i = 0; i < scoreList.length; i++) {
              if(scoreList[i].studentCode == item.studentCode){
                let obj = {};
                obj.studentCode = scoreList[i].studentCode;
                obj.studentName = scoreList[i].studentName;
                obj.id = scoreList[i].id;
                obj.average = scoreList[i].avgScore;
                obj.code = scoreList[i].code;
                obj.parentCode = scoreList[i].parentCode;
                obj.subjectCode = scoreList[i].subjectCode;
                obj.evaluate = scoreList[i].evaluate;
                obj.studentId = scoreList[i].studentId;
                obj.classId = scoreList[i].classId;
                obj.classCode = scoreList[i].classCode;
                obj.updater = scoreList[i].updater;
                obj.status = scoreList[i].status;
                obj.listScore = [];
                let listScore = scoreList[i].listScore;
                for (let j = 0; j < listScore.length; j++) {
                  obj["d" + listScore[j].times + listScore[j].scoreCode] =
                    listScore[j].value === null ? null : listScore[j].value;
                  obj.listScore.push({
                    id: listScore[j].id,
                    code: listScore[j].code,
                    parentCode: listScore[j].parentCode,
                    scoreId: listScore[j].scoreId,
                    scoreCode: listScore[j].scoreCode,
                    scoreName: listScore[j].scoreName,
                    times: listScore[j].times,
                    value: listScore[j].value,
                    type: listScore[j].type,
                    coeficient: listScore[j].coeficient,
                    quantity: listScore[j].quantity,
                    idStudent: listScore[j].idStudent,
                    subjectCode: listScore[j].subjectCode,
                    years: listScore[j].years,
                    semester: listScore[j].semester,
                    minimumScore: listScore[j].minimumScore,
                    typeChoose: listScore[j].typeChoose,
                    selectedValue: listScore[j].selectedValue,
                  });
                  // avgScore += ( config[listScore[j].scoreCode] * listScore[j].value )
                }
                this.desserts.push(obj);
                break;
              }
            }
          } else {
            this.typeHeader = "grade";
            let scoreList = res.data.data.ScoreList;
            scoreConfig.forEach((x) => {
              this.customHeader.push({
                text: x.name,
                value: x.name,
              });
              this.headers.push({
                text: x.name,
                value: x.name,
              });
            });
            for (let i = 0; i < scoreList.length; i++) {
              if(scoreList[i].studentCode == item.studentCode){
                let obj = {};
                obj.studentCode = scoreList[i].studentCode;
                obj.studentName = scoreList[i].studentName;
                obj.evaluate = scoreList[i].evaluate;
                let listScore = scoreList[i].listScore;
                for (let j = 0; j < listScore.length; j++) {
                  obj[listScore[j].scoreName] = listScore[j].value;
                }
                this.desserts.push(obj);
                break;
              }
            }
          }
        })
        .catch((res) => {
          this.$refs["toastMessage"].open(res.message, true);
        })
        .finally(() => {
          this.loading = false;
        });
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    cancel() {
      this.show = false;
      this.desserts = []
      this.headers = []
      this.customHeader = []
      this.promise.reject();
    },
    accept() {
      this.show = false;
      this.desserts = []
      this.headers = []
      this.customHeader = []
      this.promise.resolve();
    },
  },
};
</script>

<style>
.customHeaderSoDiem table tr:nth-child(3) th {
  border: 1px solid #dddddd;
}
.customHeaderSoDiem table td {
  border: 1px solid #dddddd;
}
</style>