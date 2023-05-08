<template>
  <v-form ref="form">
    <v-dialog v-model="show" max-width="900px">
      <v-card>
        <v-card-title
          class="justify-center"
          style="background-color: rgb(26 118 207 / 1); color: white"
        >
          {{ txtTitle }}
        </v-card-title>
        <v-card class="pa-6">
          <v-row>
            <v-col sm="12" md="6">
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    v-on="on"
                    v-bind="attrs"
                    width="100%"
                    append-icon="mdi-chevron-down"
                    label="Kiểu môn muốn cấu hình"
                    readonly
                    v-model="choosedTypeSubject"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list v-show="!updating" style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataTypeSubject"
                    :key="index"
                    @click="chooseTypeSubject(item)"
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
                    label="Học kỳ *"
                    readonly
                    v-model="nameKyHoc"
                    :rules="rules"
                  >
                  </v-text-field>
                </template>
                <v-list v-show="!updating" style="max-height: 50vh; overflow-y: scroll">
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
                <v-list v-show="!updating" style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in updating ? dataKhoi : dataFullKhoi"
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
                    label="Môn học *"
                    readonly
                    v-model="choosedSubject"
                    :rules="rulesSubject"
                  >
                  </v-text-field>
                </template>
                <v-list v-show="!updating" style="max-height: 50vh; overflow-y: scroll">
                  <v-list-item
                    v-for="(item, index) in dataSubject"
                    :key="index"
                    @click="chooseSubject(item)"
                  >
                    <v-list-item-title>{{
                      formatTextOverLength(item.name, 50)
                    }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
          </v-row>
          <v-row>
            <v-col sm="12">
              <v-data-table
                :headers="headerTable"
                :items="dessertTable"
                :items-per-page="-1"
                disable-sort
                :hide-default-footer="true"
              >
              <template slot="no-data">
                  Chưa có cột điểm
              </template>
                <template v-slot:item.name="{ item }">
                  <v-text-field
                    v-model="item.name"
                    :disabled="item.name.trim() == 'Điểm trung bình'"
                    :rules="[
                      (v) => v.trim() != '' || 'Không được để trống',
                      (v) => v.length <= 250 || 'Tối đa 250 ký tự',
                      (v) =>
                        !dessertTable.some((x) =>
                          x == item ? false : x.name == v
                        ) || 'Đã tồn tại',
                    ]"
                  >
                  </v-text-field>
                </template>

                <template v-slot:item.selected_value="{ item }">
                  <v-text-field
                    v-model="item.selected_value"
                    :disabled="!item.type_choose"
                    :rules="item.type_choose ? rulesSelectedValue : []"
                  >
                  </v-text-field>
                </template>

                <template v-slot:item.type_choose="{ item }">
                  <v-simple-checkbox
                    v-model="item.type_choose"
                    color="primary"
                    :disabled="item.name.trim() == 'Điểm trung bình'"
                    :ripple="false"
                    @input="onChangeCheckBox(item)"
                  ></v-simple-checkbox>
                </template>

                <template v-slot:item.coefficient="{ item }">
                  <v-menu offset-y>
                    <template v-slot:activator="{ on, attrs }">
                      <v-text-field
                        v-on="on"
                        v-bind="attrs"
                        width="100%"
                        append-icon="mdi-chevron-down"
                        readonly
                        :value="item.coefficient"
                        :rules="rules"
                      >
                      </v-text-field>
                    </template>
                    <v-list style="max-height: 50vh; overflow-y: scroll">
                      <v-list-item
                        v-for="(it, index) in listCoefficient"
                        :key="index"
                        @click="chooseCoefficient(item, it)"
                      >
                        <v-list-item-title>{{ it }}</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </template>

                <template v-slot:item.quantity="{ item }">
                  <v-menu offset-y>
                    <template v-slot:activator="{ on, attrs }">
                      <v-text-field
                        v-on="on"
                        v-bind="attrs"
                        width="100%"
                        append-icon="mdi-chevron-down"
                        readonly
                        :value="item.quantity"
                        :rules="rules"
                      >
                      </v-text-field>
                    </template>
                    <v-list style="max-height: 50vh; overflow-y: scroll">
                      <v-list-item
                        v-for="(it, index) in listQuantity"
                        :key="index"
                        @click="chooseQuantity(item, it)"
                      >
                        <v-list-item-title>{{ it }}</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </template>

                <template v-slot:item.minimum_score="{ item }">
                  <v-menu offset-y>
                    <template v-slot:activator="{ on, attrs }">
                      <v-text-field
                        v-on="on"
                        v-bind="attrs"
                        width="100%"
                        append-icon="mdi-chevron-down"
                        readonly
                        :value="item.minimum_score"
                        :rules="[
                          (v) =>
                            v <= item.quantity || 'Nhỏ hơn hoặc bằng số lượng',
                        ]"
                      >
                      </v-text-field>
                    </template>
                    <v-list style="max-height: 50vh; overflow-y: scroll">
                      <v-list-item
                        v-for="(it, index) in listMinimumScore"
                        :key="index"
                        @click="chooseMinimumScore(item, it)"
                      >
                        <v-list-item-title>{{ it }}</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </template>

                <template v-slot:item.action="{ item }">
                  <v-icon v-if="item.name != 'Điểm trung bình'" @click="remove(item)">mdi-delete</v-icon>
                </template>

              </v-data-table>
            </v-col>
            <v-row align="center" justify="center" v-if="emptyTable">
              <h3 style="color: red;">Không có thông tin nào để cập nhật</h3>
            </v-row>
          </v-row>
          <v-row>
            <v-col v-if="choosedSubject">
              <v-btn @click="addRow()" color="primary" outlined>
                {{ txtBtn }}
              </v-btn>
            </v-col>
          </v-row>
          <v-row align="center" justify="center">
            <v-btn
              class="mr-2"
              elevation="1"
              color="warning darken-1"
              text
              @click="cancel"
            >
              Hủy bỏ
            </v-btn>
            <v-btn
              class="ml-2"
              elevation="1"
              color="primary darken-1"
              text
              @click="save"
              :loading="loading"
            >
              Lưu lại
            </v-btn>
          </v-row>
        </v-card>
      </v-card>
    </v-dialog>
    <ToastMessage ref="toastMessage" />
  </v-form>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";

export default {
  name: "ConfigScoreboardDialog",
  props: ["dataKhoi", "headerTable1", "headerTable2", "txtTitle", "dataKyHoc"],
  components: {
    ToastMessage
  },
  data() {
    return {
      show: false,
      promise: null,
      showTinhDiemTable: true,
      showXepLoaiTable: false,
      dataTypeSubject: [
        {
          id: 0,
          name: "Tính điểm",
        },
        {
          id: 1,
          name: "Xếp loại",
        },
      ],
      rules: [
        (v) => !!v || "Không được để trống"
      ],
      rulesSubject: [
        (v) => !!v || "Không có môn học nào"
      ],
      rulesSelectedValue: [
        (v) => !!v || "Không được trống",
        (v) => !v ? true : v.length < 500 || 'Tối đa 500 ký tự'
      ],
      choosedTypeSubject: null,
      choosedTypeSubjectId: null,
      choosedKhoi: null,
      choosedSubject: null,
      dataSubject: [],
      choosedCodeSubject: null,
      item: {},
      headerTable: [],
      dessertTable: [],
      txtBtn: "",
      choosedCoefficient: null,
      listCoefficient: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      listQuantity: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      listMinimumScore: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      objNewType1: {
        stt: null,
        name: "",
        coefficient: 1,
        quantity: 1,
        minimum_score: 1,
      },
      objNewType2: {
        stt: null,
        name: "",
        type_choose: false,
        selected_value: "",
      },
      dataTransfer: null,
      saveIndexChanged: [],
      dataRaw: null,
      arrIdSet: null,
      arrIdDeleted: [],
      countNew: 0,
      arrNewCreate: [],
      parent_code: null,
      arrRowChange: [],
      updating: false,
      choosedGradeId: null,
      dataFullKhoi: [],
      choosedKyHoc: null,
      emptyTable: false,
      loading: false,
      nameKyHoc: null
    };
  },
  methods: {
    async open(desserts, type, updating, subject, khoi, semester) {
      this.updating = updating ? true : false
      this.show = true;
      this.arrIdSet = new Set();
      if (desserts != null) {
        this.dataTransfer = desserts.map((x) => JSON.parse(JSON.stringify(x)));
        this.dataRaw = desserts.map((x) => JSON.parse(JSON.stringify(x)));
        this.chooseSubject(subject)
        this.chooseKhoi(khoi)
        this.choosedKyHoc = semester
      } else {
        this.getData(2)
        this.choosedKyHoc = 1
      }
      if (type == 0 || type == null) {
        this.choosedTypeSubject = this.dataTypeSubject[0].name;
        this.choosedTypeSubjectId = this.dataTypeSubject[0].id;
      }
      if (type == 1) {
        this.choosedTypeSubject = this.dataTypeSubject[1].name;
        this.choosedTypeSubjectId = this.dataTypeSubject[1].id;
      }
      if(updating == null){
        //get all khối
        await AppService.getAllKhoi()
        .then( (result) => {
          this.dataFullKhoi = result.data.data
          this.chooseKhoi(this.dataFullKhoi[0])
        })

        //get tất cả môn học chưa cấu hình bảng điểm
        await this.getSubjectsNotYetConfigScoreboard(this.item.grade_level, this.choosedTypeSubjectId, this.$store.getters['currentYear'], this.choosedGradeId, this.choosedKyHoc)

      }
      this.nameKyHoc = this.formatKyHoc(this.choosedKyHoc)
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    async save() {
      if (this.$refs["form"].validate()) {
        //nếu như không có dòng dữ liệu nào
        if(this.dessertTable.length != 0){
          this.loading = true
          if(this.updating == true){
            if(this.choosedTypeSubjectId == 0){
              //call api delete here
              await AppService.deleteConfigScoreDetail(
                this.arrIdDeleted.filter((x) => x >= 0)
              )
                .then((res) => {
                  if(res.data.status !== 'OK'){
                    this.$refs['toastMessage'].open(res.data.message, true)
                  }else{
                    if(res.data.message != 'empty'){
                      this.$refs["toastMessage"].open("Cập nhật thành công", false);
                    }
                  }
                })
                .catch(() => {
                });

              //call api add new here
              await AppService.addConfigScoreDetail(this.arrNewCreate)
                .then((result) => {
                  if(result.data.status !== 'OK'){
                    this.$refs['toastMessage'].open(result.data.message, true)
                  }else{
                    if(result.data.message != 'empty'){
                      this.$refs["toastMessage"].open("Cập nhật thành công", false);
                    }
                    ///filter elements added just now
                    this.arrNewCreate = result.data.data;
                    this.dessertTable = this.dessertTable.filter((x) => x.id >= 0);

                    var vm = this;

                    this.dataRaw.forEach(element => {
                      let index = this.dessertTable.findIndex(x => x.id == element.id)
                      if(index >= 0){
                        Object.keys(element).some(function(prop){
                          if(prop != 'stt'){
                            if(element[prop] !== vm.dessertTable[index][prop]){
                              vm.arrIdSet.add(element.id);
                            }
                          }
                        })
                      }
                    });
                    let arrDeleted = new Set(this.arrIdDeleted);
                    for (let elem of arrDeleted) {
                      this.arrIdSet.delete(elem);
                    }
                    // this.arrIdSet = this.arrIdSet.filter((x) => x >= 0);
                  }
                })
                .catch(() => {});

              //call api change here
              await AppService.updateChangeConfigScoreDetail(
                this.dessertTable.filter((x) => this.arrIdSet.has(x.id))
              )
                .then((res) => {
                  if(res.data.status !== 'OK'){
                    this.$refs['toastMessage'].open(res.data.message, true)
                    this.resetTable();
                    this.choosedTypeSubjectId = -1;
                    this.loading = false
                    this.promise.resolve();
                    this.show = false;
                  }else{
                    if(res.data.message != 'empty'){
                      this.$refs["toastMessage"].open("Cập nhật thành công", false);
                    }
                    this.resetTable();
                    this.choosedTypeSubjectId = -1;
                    this.loading = false
                    this.promise.resolve();
                    this.show = false;
                  }
                })
                .catch(() => {});
            }
            if(this.choosedTypeSubjectId == 1){
              //call api delete here
              await AppService.deleteConfigGradeDetail(
                this.arrIdDeleted.filter((x) => x >= 0)
              )
                .then((res) => {
                  if(res.data.status !== 'OK'){
                    this.$refs['toastMessage'].open(res.data.message, true)
                  }else{
                    if(res.data.message != 'empty'){
                      this.$refs["toastMessage"].open("Cập nhật thành công", false);
                    }
                  }
                })
                .catch(() => {});

              //call api add new here
              await AppService.addConfigGradeDetail(this.arrNewCreate)
                .then((result) => {
                  if(result.data.status !== 'OK'){
                    this.$refs['toastMessage'].open(result.data.message, true)
                  }else{
                    if(result.data.message != 'empty'){
                      this.$refs["toastMessage"].open("Cập nhật thành công", false);
                    }
                    ///filter elements added just now
                    this.arrNewCreate = result.data.data;
                    this.dessertTable = this.dessertTable.filter((x) => x.id >= 0);

                    var vm = this;

                    this.dataRaw.forEach(element => {
                      let index = this.dessertTable.findIndex(x => x.id == element.id)
                      if(index >= 0){
                        Object.keys(element).some(function(prop){
                          if(prop != 'stt'){
                            if(element[prop] !== vm.dessertTable[index][prop]){
                              vm.arrIdSet.add(element.id);
                            }
                          }
                        })
                      }
                    });
                    let arrDeleted = new Set(this.arrIdDeleted);
                    for (let elem of arrDeleted) {
                      this.arrIdSet.delete(elem);
                    }
                    // this.arrIdSet = this.arrIdSet.filter((x) => x >= 0);
                  }
                })
                .catch(() => {});

              //call api change here
              await AppService.updateChangeConfigGradeDetail(
                this.dessertTable.filter((x) => this.arrIdSet.has(x.id))
              )
                .then((res) => {
                  if(res.data.status !== 'OK'){
                    this.$refs['toastMessage'].open(res.data.message, true)
                    this.resetTable();
                    this.choosedTypeSubjectId = -1;
                    this.loading = false
                    this.promise.resolve();
                    this.show = false;
                  }else{
                    if(res.data.message != 'empty'){
                      this.$refs["toastMessage"].open("Cập nhật thành công", false);
                    }
                    this.resetTable();
                    this.choosedTypeSubjectId = -1;
                    this.loading = false
                    this.promise.resolve();
                    this.show = false;
                  }

                })
                .catch(() => {});
            }
          }else{
            if(this.choosedTypeSubjectId == 0){
              AppService.configFromBeginnigScoreboardScore({
                configScoreDetails : this.dessertTable,
                grade_id: this.choosedGradeId,
                year: this.$store.getters['currentYear'],
                subjectCode: this.choosedCodeSubject,
                semester: this.choosedKyHoc
              })
              .then( () => {
                this.resetTable();
                this.choosedTypeSubjectId = -1;
                this.loading = false
                this.promise.resolve();
                this.show = false;
              })
              .catch( () => {
              })
            }else{
              AppService.configFromBeginnigScoreboardGrading({
                configGradeDetailForms : this.dessertTable,
                grade_id: this.choosedGradeId,
                year: this.$store.getters['currentYear'],
                subjectCode: this.choosedCodeSubject,
                semester: this.choosedKyHoc
              })
              .then( () => {
                this.resetTable();
                this.choosedTypeSubjectId = -1;
                this.loading = false
                this.promise.resolve();
                this.show = false;
              })
              .catch( () => {
              })
            }
          }
        }else{
          this.emptyTable = true
          setTimeout(() => {
            this.emptyTable = false
          }, 3000)
        }
      }
    },
    cancel() {
      this.dessertTable = []
      this.promise.reject();
      this.show = false;
      this.resetTable();
      this.choosedTypeSubjectId = -1;
    },
    chooseKyHoc(item){
      this.choosedKyHoc = item
      this.nameKyHoc = this.formatKyHoc(item)
    },
    chooseTypeSubject(item) {
      this.choosedTypeSubject = item.name;
      this.choosedTypeSubjectId = item.id;
    },
    chooseKhoi(item) {
      this.choosedGradeId = item.grade_id != undefined ? item.grade_id : item.id 
      this.substringKhoi(item.name);
    },
    chooseSubject(item) {
      if(this.updating == true){
        this.parent_code = item.cssCode
      }
      if(item != null){
        this.choosedSubject = item.name;
        this.choosedCodeSubject = item.code;
      }
    },
    substringKhoi(name) {
      this.choosedKhoi = name;
      let index = this.choosedKhoi.indexOf(" ");
      let resultKhoi = this.choosedKhoi
        .substring(index, this.choosedKhoi.length)
        .trim();
      this.item.grade_level = resultKhoi;
    },
    getSubjectsNotYetConfigScoreboard(grade_level, type,year, grade_id, semester) {
        return AppService.getSubjectsNotYetConfigScoreboard(grade_level, type, year, grade_id, semester)
          .then((result) => {
            this.dataSubject = result.data.data.data;
            if (this.dataSubject.length != 0) {
              this.chooseSubject(this.dataSubject[0]);
            }else{
              this.choosedSubject = ''
              this.dessertTable = []
              this.arrNewCreate = []
            }
          })
          .catch(() => {
          });
    },
    formatTextOverLength(txt, length) {
      if (txt != null && txt.length >= length) {
        return txt.substring(0, 50) + "...";
      }
      return txt;
    },
    remove(item) {
      this.arrIdDeleted.push(item.id);
      this.dessertTable.splice(item.stt - 1, 1);
      this.dessertTable.forEach((element, index) => {
        element.stt = index + 1;
      });
    },
    addRow(addXepLoai) {
      if(addXepLoai == true && this.choosedTypeSubjectId == 1){
        let clone = JSON.parse(JSON.stringify(this.objNewType2));
        clone.stt = 1;
        clone.name = "Điểm trung bình"
        clone.id = -1 + this.countNew;
        clone.parent_code = this.parent_code;
        clone.type_choose = true
        clone.selected_value = ""
        this.countNew--;
        this.dessertTable.forEach(element => {
          element.stt++
        });
        this.dessertTable.unshift(clone);
        this.arrNewCreate.push(clone);
      }else{
        let clone = null;
        if (this.choosedTypeSubjectId == 0) {
          clone = JSON.parse(JSON.stringify(this.objNewType1));
        } else {
          clone = JSON.parse(JSON.stringify(this.objNewType2));
        }
        clone.stt = this.dessertTable.length + 1;
        clone.id = -1 + this.countNew;
        clone.parent_code = this.parent_code;
        this.countNew--;
        this.dessertTable.push(clone);
        this.arrNewCreate.push(clone);
      }
    },
    chooseCoefficient(item, coeff) {
      item.coefficient = coeff;
    },
    chooseQuantity(item, quantity) {
      item.quantity = quantity;
    },
    chooseMinimumScore(item, minimum_score) {
      item.minimum_score = minimum_score;
    },
    resetTable() {
      this.dessertTable = [];
      this.arrNewCreate = [];
      this.arrIdDeleted = [];
      this.arrRowChange = [];
      this.arrIdSet = null;
      this.saveIndexChanged = [];
    },
    async getData(type){
        if(this.choosedTypeSubjectId == 0){
          if(this.updating == true){
            await AppService.getConfigScoreSubject(this.parent_code)
            .then((result) => {
              let arrTemp = result.data.data.data
              arrTemp.forEach((element, index) => {
                element.stt = index + 1
              });
              this.dataTransfer = arrTemp.map((x) => JSON.parse(JSON.stringify(x)));
              this.dataRaw = arrTemp.map((x) => JSON.parse(JSON.stringify(x)));
              this.dessertTable = this.dataTransfer
            })
          }else{
            this.dessertTable = []
            this.arrNewCreate = []
          }
        }
        if(this.choosedTypeSubjectId == 1){
          if(this.updating == true){
            await AppService.getConfigGradingDetails(this.parent_code)
            .then((result) => {
              let arrTemp = result.data.data.data
              let flag = false
              arrTemp.forEach((element, index) => {
                element.stt = index + 1
                element.type_choose = element.type_choose == 1 ? true : false
                if(element.name.trim() == 'Điểm trung bình'){
                  flag = true
                }
              });
              this.dataTransfer = arrTemp.map((x) => JSON.parse(JSON.stringify(x)));
              this.dataRaw = arrTemp.map((x) => JSON.parse(JSON.stringify(x)));
              this.dessertTable = this.dataTransfer
              if(flag == false){
                this.addRow(true)
              }
            })
          }else{
            this.dessertTable = []
            this.arrNewCreate = []
            this.addRow(true)
          }
        }
        if(this.updating == false && this.item.grade_level != undefined && this.choosedGradeId != null){
          await AppService.getSubjectsNotYetConfigScoreboard(this.item.grade_level, this.choosedTypeSubjectId, this.$store.getters['currentYear'], this.choosedGradeId, this.choosedKyHoc)
            .then((result) => {
              this.dataSubject = result.data.data.data
              if(type == 0 && this.choosedTypeSubjectId != -1){
                if(this.dataSubject.length == 0){
                  this.choosedSubject = '';
                  this.choosedCodeSubject = '';
                }else{
                  this.chooseSubject(this.dataSubject[0])
                }
              }
          })
        }
    },
    onChangeCheckBox(item){
      if(item.type_choose == false){
        item.selected_value = ''
      }
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
  },
  watch: {
    choosedTypeSubjectId() {
        this.arrNewCreate = []
        if (this.dataTransfer != null) {
          this.dessertTable = this.dataTransfer;
        } else {
          this.dessertTable = [];
        }
        if(this.updating == false){
          this.getData(0)
        }
        if (this.choosedTypeSubjectId == 0) {
          this.headerTable = this.headerTable1.map((x) => x);
          this.txtBtn = "+ Thêm cột điểm";
        } else {
          this.headerTable = this.headerTable2.map((x) => x);
          this.txtBtn = "+ Thêm cột xếp loại";
        }
        this.headerTable.push({ text: "", value: "action" });
    },
    choosedKhoi() {
      if(this.updating == false && this.item.grade_level != undefined && this.choosedGradeId != null){
        this.getSubjectsNotYetConfigScoreboard(
          this.item.grade_level,
          this.choosedTypeSubjectId,
          this.$store.getters['currentYear'],
          this.choosedGradeId,
          this.choosedKyHoc
        );
      }
    },
    choosedCodeSubject(){
      this.getData(1)
    },
    choosedKyHoc(){
      if(this.updating == false && this.item.grade_level != undefined && this.choosedGradeId != null){
        this.getSubjectsNotYetConfigScoreboard(
            this.item.grade_level,
            this.choosedTypeSubjectId,
            this.$store.getters['currentYear'],
            this.choosedGradeId,
            this.choosedKyHoc
        )
      }
      this.dataDDLTable_1 = [];
      this.dataDDLTable_2 = [];
      this.choosedDDLTable_1 = null;
      this.choosedDDLTable_2 = null;
      this.desserts_table_1 = [];
      this.desserts_table_2 = [];
    }
  },
};
</script>