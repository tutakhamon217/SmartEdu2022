<template>
  <v-dialog v-model="show" width="1000" @click:outside="close">
        <v-card width="100%" class="pa-0 ma-0">
            <v-toolbar 
              flat 
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
              style="border-radius: 5px 5px 0px 0px"
            >
              <v-toolbar-title
                style="width: 100%;"
                class="d-flex"
              >
                <v-spacer></v-spacer>
                <span style="color: white">Đánh giá của giáo viên bộ môn</span>
                <v-spacer></v-spacer>
                <v-icon color="white" @click="close()">mdi-close</v-icon>
              </v-toolbar-title>
            </v-toolbar>
            <v-data-table
              :headers="header"
              :items="data"
              :items-per-page="5"
              disable-sort
              :loading="loading"
              :footer-props="{
                'items-per-page-text': 'Số dòng mỗi trang:',
              }"
            >
              <template slot="no-data">
                  Danh sách học sinh rỗng
              </template>
              <template v-slot:top>
                <v-toolbar flat>
                  <v-toolbar-title
                    style="width: 100%; align-items: center"
                    class="d-flex"
                  >
                    <span style="color: black;">{{
                      "Học sinh: " + nameStudent
                    }}</span>
                  </v-toolbar-title>
                </v-toolbar>
              </template>
              <template v-slot:item.evaluate="{ item }">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <span v-bind="attrs" v-on="on">{{
                      formatTextTooltip(item.evaluate, 50)
                    }}</span>
                  </template>
                  <span>{{ item.evaluate }}</span>
                </v-tooltip>
              </template>
              <template v-slot:footer.page-text="props">
                  {{ props.pageStart }}-{{ props.pageStop }} của {{ props.itemsLength }} kết quả
              </template>
            </v-data-table>
        </v-card>
    <ToastMessage ref="toastMessage" />
  </v-dialog>
</template>

<script>
import ToastMessage from "@/components/ToastMessage.vue";
import AppService from "@/services/app.service";

export default {
  name: "ConductStudentDialog",
  components: {
    ToastMessage,
  },
  data() {
    return {
      data: [],
      header: [
        { text: "STT", value: "stt" },
        { text: "Môn học", value: "name" },
        { text: "Học kỳ", value: "semester" },
        { text: "Giáo viên giảng dạy", value: "teacher_name" },
        { text: "Đánh giá", value: "evaluate" },
      ],
      show: false,
      loading: false,
      nameStudent: "",
    };
  },

  methods: {
    async open(student_code, hocKy, classCode) {
      this.data = [];
      this.loading = true;
      this.show = true;
      await AppService.getEvaluateOfTeacher({
        semester: hocKy,
        year: this.$store.getters["year"],
        studentCode: student_code,
        classCode: classCode,
      })
        .then((res) => {
          this.data = res.data.data;
          this.nameStudent = this.data[0].full_name;
          this.data.forEach((x, index) => {
            x.stt = index + 1;
            x.semester = this.formatKyHoc(x.semester);
            x.teacher_name = x.teacher_code + " - " + x.teacher_name;
          });
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
          this.$refs["toastMessage"].open(
            "Không có đánh giá nào của giáo viên",
            true
          );
        });
    },
    formatKyHoc(item) {
      switch (item.trim()) {
        case "0": {
          return "Cả năm";
        }
        case "1": {
          return "Học kỳ I";
        }
        case "2": {
          return "Học kỳ II";
        }
        case "3": {
          return "Học kỳ III";
        }
        case "4": {
          return "Học kỳ IV";
        }
      }
      return null;
    },
    close() {
      this.data = [];
      this.show = false;
      this.nameStudent = ""
    },
    formatTextTooltip(item, max) {
      if (item.length >= max) {
        return item.substring(0, max) + "...";
      }
      return item;
    },
  },
};
</script>