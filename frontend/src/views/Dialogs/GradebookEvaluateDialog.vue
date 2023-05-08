<template>
  <div>
    <v-dialog v-model="show" max-width="900">
      <v-card>
        <v-card-title
          style="background-color: rgb(26 118 207 / 1); color: white"
          class="justify-center py-2"
        >
          ĐÁNH GIÁ HỌC SINH
        </v-card-title>
        <v-card-actions>
          <v-row>
            <v-col md="2" class="d-flex align-center"
              ><p class="ma-0">Tên học sinh:</p></v-col
            >
            <v-col md="10">
              <!-- <v-autocomplete
                chips
                clearable
                deletable-chips
                multiple
                small-chips
                :items="listStudent"
                v-model="listStudentChoosed"
                item-value="code"
                item-text="nameCode"
              >
              </v-autocomplete> -->

            <v-menu offset-y :close-on-content-click="false">
              <template v-slot:activator="{ on, attrs }">
                <v-sheet
                  class="px-1"
                  style="border: 1px solid black;border-radius: 5px;height: 50px;"
                  v-bind="attrs"
                  v-on="on"
                >
                  <v-chip-group
                    multiple
                  >
                    <v-chip
                      v-for="tag in listStudentChoosedChip"
                      :key="tag.code"
                    >
                      {{ tag.nameCode }}
                    </v-chip>
                  </v-chip-group>
                </v-sheet>
              </template>
              <v-list>
                <v-list-item-group
                  v-model="listStudentChoosed"
                  multiple
                  style="max-height: 50vh !important; overflow-y: scroll;"
                >
                  <template v-for="(item) in listStudent">
                    <v-list-item
                      :key="item.code"
                      :value="item"
                      active-class="primary--text text--accent-4"
                    >
                      <template v-slot:default="{}">
                        <v-list-item-content>
                          <v-list-item-title v-text="item.nameCode"></v-list-item-title>
                        </v-list-item-content>

                        <v-list-item-action>
                          <v-btn v-if="item.isEvaluated" @click.stop="showEvaluate(item.evaluate, item.fullName, item.code)" icon color="primary">
                            <v-icon color="primary">mdi-eye</v-icon>
                          </v-btn>
                        </v-list-item-action>
                      </template>
                    </v-list-item>
                  </template>
                </v-list-item-group>
              </v-list>
            </v-menu>

            </v-col>
            <v-col md="2"><p class="ma-0">Đánh giá:</p></v-col>
            <v-col md="10">
              <v-textarea
                outlined
                name="input-7-4"
                v-model="evaluate"
                counter="500"
                clearable
                auto-grow
                :rules="ruleMaxLength"
              ></v-textarea>
            </v-col>
          </v-row>
        </v-card-actions>
        <v-card-actions class="justify-center pb-5">
          <v-btn
            class="mr-2"
            elevation="2"
            color="warning darken-1"
            text
            @click="cancel"
          >
            Hủy bỏ
          </v-btn>
          <v-btn
            class="ml-2"
            elevation="2"
            color="primary darken-1"
            text
            @click="confirm()"
          >
            Xác nhận
          </v-btn>
        </v-card-actions>
      </v-card>
      <ToastMessage class="bottom-toast-mess" ref="toastMessage" />
      <Loading ref="loading" />
      <v-dialog v-model="showEvaluateDialog" width="900px">
        <v-card>
          <v-card-title style="background-color: rgb(26 118 207 / 1); color: white"
            class="justify-center py-2"
          >
          <v-spacer></v-spacer>
          ĐÁNH GIÁ TRƯỚC ĐÓ
          <v-spacer></v-spacer>
          <v-btn color="white" @click="closeShowTextDialog" icon>
            <v-icon>mdi-close</v-icon>
          </v-btn>
          </v-card-title>
          <v-card-actions>
            <v-text-field class="mr-1" label="Tên học sinh" v-model="fullNameStudentChoosed" readonly></v-text-field>
            <v-text-field class="ml-1" label="Mã học sinh" v-model="studentCodeChoosed" readonly></v-text-field>
          </v-card-actions>
          <v-card-actions>
            <v-textarea label="Đánh giá" v-model="textEvaluateDialog" readonly outlined auto-grow name="input-7-4"></v-textarea>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-dialog>
  </div>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";
import Loading from "@/components/Loading.vue";

export default {
  name: "GradebookEvaluateDialog",
  components: {
    ToastMessage,
    Loading
  },
  data() {
    return {
      show: false,
      promise: null,
      listStudentChoosed: [],
      listStudent: [],
      evaluate: null,
      data: {},
      classCode: null,
      semester: null,
      subjectCode: null,
      listStudentChoosedChip: [],
      showEvaluateDialog: false,
      textEvaluateDialog: null,
      fullNameStudentChoosed: null,
      studentCodeChoosed: null,
      ruleMaxLength: [
        v => !v ? true : v.length <= 500 || 'Tối đa 500 ký tự' 
      ]
    };
  },
  methods: {
    open(data, classCode, semester, subjectCode) {
      this.semester = semester
      this.classCode = classCode
      this.subjectCode = subjectCode
      this.listStudent = []
      this.listStudentChoosed = []
      this.listStudentChoosedChip = []
      this.evaluate = ''
      data.forEach(element => {
        let obj = {}
        obj.isEvaluated = element.isEvaluated
        obj.evaluate = element.evaluate
        obj.fullName = element.fullName
        obj.nameCode = element.fullName + ' - ' + element.code
        obj.code = element.code
        this.listStudent.push(obj)
      });
      this.show = true;
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    async confirm() {
      if(this.listStudentChoosed.length > 0 && this.evaluate.trim().length > 0 && this.evaluate.trim().length <= 500){
        let codes = this.listStudentChoosed.map(x => x.code)
        await AppService.evaluateSubjectByTeacher({
          year: this.$store.state.year,
          classCode: this.classCode,
          semester: this.semester,
          listStudentCode: codes,
          evaluate: this.evaluate,
          subjectCode: this.subjectCode
        })
        .then((res) => {
          if(res.data.status === 'OK'){
            this.promise.resolve(res.data.message);
            this.cancel();
          }else{
            this.$refs['toastMessage'].open(res.data.message , true)
          }
        })
        .catch((res) => {
          this.$refs['toastMessage'].open(res.message , true)
        })
      }else{
        this.$refs['toastMessage'].open('Hãy điền đầy đủ thông tin theo quy định', true)
      }
    },
    cancel() {
      this.promise.reject();
      this.show = false;
    },
    showEvaluate(evaluate, fullName, studentCode){
      this.showEvaluateDialog = true
      this.textEvaluateDialog = evaluate
      this.fullNameStudentChoosed = fullName
      this.studentCodeChoosed = studentCode
    },
    closeShowTextDialog(){
      this.textEvaluateDialog = ''
      this.fullNameStudentChoosed = ''
      this.studentCodeChoosed = ''
      this.showEvaluateDialog = false
    }
  },
  watch:{
    listStudentChoosed(){
      this.listStudentChoosedChip = []
      if(this.listStudentChoosed.length > 3){
        for(let i = 0; i < 3; i++){
          this.listStudentChoosedChip.push(this.listStudentChoosed[i])
        }
          this.listStudentChoosedChip.push({
            nameCode: (this.listStudentChoosed.length - 3) + '+' ,
            code: -1
          })
      }else{
        for(let i = 0; i < this.listStudentChoosed.length; i++){
          this.listStudentChoosedChip.push(this.listStudentChoosed[i])
        }
      }
    }
  }
};
</script>

<style scoped>
</style>