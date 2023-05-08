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
            <v-col sm="12" md="6">
              <v-text-field
                v-model="ten_mon_hoc"
                label="Môn học"
                placeholder="Nhập thông tin môn học"
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
    <v-form ref="form">
      <v-card class="mb-3" elevation="1">
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
              <span>Danh sách môn học: {{ nameClassChoosedAfterSearch }}</span>
            </v-toolbar-title>
          </v-toolbar>
        <v-data-table
          :headers="headers"
          :items="desserts"
          :items-per-page="5"
          :footer-props="{
            'items-per-page-text': 'Số dòng mỗi trang:',
          }"
        >
              <template slot="no-data">
                Danh sách môn học rỗng
              </template>
          <template v-slot:item.he_so="{ item }">
            <v-menu offset-y>
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-on="on"
                  v-bind="attrs"
                  width="100%"
                  append-icon="mdi-chevron-down"
                  readonly
                  style="width: 110px"
                  class="input-sfc"
                  :value="item.he_so == 0 ? listHeSo[0] : item.he_so"
                  :rules="item.hoc ? rulesHeSo : []"
                >
                </v-text-field>
              </template>
              <v-list v-if="disableDDL(item)">
                <v-list-item
                  v-for="(heSo, index) in listHeSo"
                  :key="index"
                  @click="chooseHeSo(item, heSo)"
                >
                  <v-list-item-title>{{ heSo }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </template>
          <template v-slot:item.hoc="{ item }">
            <v-simple-checkbox
              v-model="item.hoc"
              color="primary"
              :ripple="false"
              @input="changedHoc(item)"
              :disabled="disableHoc(item)"
            ></v-simple-checkbox>
          </template>
          <template v-slot:item.ca_nam="{ item }">
            <v-simple-checkbox
              v-model="item.ca_nam"
              color="primary"
              :ripple="false"
              @input="changedCaNam(item)"
              :disabled="disableCheckbox(item)"
            ></v-simple-checkbox>
          </template>
          <template v-slot:item.hoc_ky_1="{ item }">
            <v-simple-checkbox
              v-model="item.hoc_ky_1"
              color="primary"
              :ripple="false"
              @input="changed(item)"
              :disabled="disableCheckbox(item)"
            ></v-simple-checkbox>
          </template>
          <template v-slot:item.hoc_ky_2="{ item }">
            <v-simple-checkbox
              v-model="item.hoc_ky_2"
              color="primary"
              :ripple="false"
              @input="changed(item)"
              :disabled="disableCheckbox(item)"
            ></v-simple-checkbox>
          </template>
          <template v-slot:footer.page-text="props">
            {{ props.pageStart }}-{{ props.pageStop }} của
            {{ props.itemsLength }} kết quả
          </template>
        </v-data-table>
      </v-card>
    </v-form>
    <v-row
      v-if="updating == false"
      justify="center"
      align="center"
      class="mt-4"
    >
      <v-btn class="float-right" v-if="desserts.length > 0" @click="updating = true" color="primary">
        Cập nhật
      </v-btn>
    </v-row>
    <v-row class="py-4" v-else>
      <v-col sm="6">
        <v-btn class="float-right" elevation="1" color="warning darken-1" text @click="cancel" :disabled="isDisableCancel">
          Hủy bỏ
        </v-btn>
      </v-col>
      <v-col sm="6">
        <v-btn
          elevation="1" color="primary darken-1" text
          @click="save"
          :disabled="indexOfRowsChanged.length == 0"
          :loading="loadingSave"
        >
          Lưu lại
        </v-btn>
      </v-col>
    </v-row>
    <ToastMessage class="bottom-toast-mess" ref="toastMessage" />
    <SubjectForClassDialog
      headerMessage="Xác nhận thông tin cập nhật"
      content="Bạn có chắc chắn muốn lưu thông tin vừa cập nhật ?"
      ref="subjectForClassDialog"
    />
    <Loading ref="loading" />
  </div>
</template>

<script>
import ToastMessage from "@/components/ToastMessage";
import SubjectForClassDialog from "@/views/Dialogs/SubjectForClassDialog";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";

export default {
  components: {
    ToastMessage,
    SubjectForClassDialog,
    Loading
  },
  data() {
    return {
      headers: [
        { text: "STT", value: "stt", sortable: false, align: 'center' },
        { text: "Tên môn học", value: "ten_mon_hoc", sortable: false },
        { text: "Học", value: "hoc", sortable: false },
        { text: "Hệ số", value: "he_so", sortable: false },
        { text: "Cả năm", value: "ca_nam", sortable: false, align: 'center' },
        { text: "Học kỳ I", value: "hoc_ky_1", sortable: false, align: 'center' },
      ],
      desserts: [],
      dessertsFirst: [],
      listHeSo: ["Lựa chọn", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      rules: [(v) => !!v || "Hãy chọn một lựa chọn"],
      choosedKhoi: null,
      choosedKhoaBan: null,
      choosedLop: null,
      dataKhoi: [],
      dataKhoaBan: [],
      dataLop: [],
      indexOfRowsChanged: [],
      arrIncomingUpdate: [],
      objectSearch: {},
      ten_mon_hoc: null,
      updating: false,
      loadingSave: false,
      isDisableCancel: false,
      rulesHeSo: [(v) => v != "Lựa chọn" || "Hãy chọn hệ số"],
      firstLoadDone: false,
      amountOfSemester: null,
      nameClassChoosedAfterSearch: '',
      prevRoute: null,
      watchYear: 1,
      ruleMaxLength: [
        v => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự' 
      ]
    };
  },
  async mounted() {
    
    // await this.$store.dispatch("getCurrentUser");
    // if(this.prevRoute === '/'){
    //    this.watchYear = this.$watch('$store.state.year', async () => {
    //     this.$refs['loading'].open()
    //     await Promise.all([this.getAllKhoi(), this.getAllKhoaBan()])
    //     await this.getClassFirstLoad();
    //     await this.getAmountOfSemester();
    //     await this.initLoad();
    //     this.search()
    //     this.watchYear()
    //     this.watchYear = null
    //     this.$refs['loading'].close()
        
    //   })
      
    // }else{
        this.$refs['loading'].open()
        await Promise.all([this.getAllKhoi(), this.getAllKhoaBan(), this.getAmountOfSemester()])
        await this.getClassFirstLoad();
        await this.initLoad();
        await this.search()
        this.watchYear = null
        this.$refs['loading'].close()
    // }
    
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.prevRoute = from.fullPath
    })
  },
  methods: {
    initLoad() {
      this.desserts = [
        // {
        // stt: 1,
        // id: 1,
        // ten_mon_hoc: "Toán",
        // hoc: true,
        // he_so: 2,
        // ca_nam: true,
        // hoc_ky_1: true,
        // hoc_ky_2: true,
        // nhap_diem: true,
        // giang_day: true,
        // thoi_khoa_bieu: true
        // },
      ];
      if(this.objectSearch.class_id != null){
        this.desserts = [];
        return AppService.getInfoSubjectBySearch(this.objectSearch.class_id, "", this.objectSearch.grade_level, this.objectSearch.dept_id)
          .then((data) => {
            this.desserts = [];
            let dataClass = data.data.data.subjectClass;
            for (let i = 0; i < dataClass.length; i++) {
              let obj = {};
              obj.stt = i + 1;
              obj.id = dataClass[i].subjectId
              obj.ten_mon_hoc = dataClass[i].subjectName;
              obj.hoc = dataClass[i].study == 0 || dataClass[i].study == null ? false : true;
              obj.he_so =
                dataClass[i].study == 0 || dataClass[i].study == null ? 0 : dataClass[i].coefficient;

              obj.hoc_ky_1 = dataClass[i].flg_semester1 == 0 || dataClass[i].flg_semester1 == null ? false : true;
              obj.hoc_ky_2 = dataClass[i].flg_semester2 == 0 || dataClass[i].flg_semester2 == null ? false : true;
              obj.ca_nam = false
              if(this.amountOfSemester == 1){
                obj.ca_nam = obj.hoc_ky_1 ? true : false
              }else if(this.amountOfSemester == 2){
                if(obj.hoc_ky_1 && obj.hoc_ky_2){
                  obj.ca_nam = true
                }
              }

              obj.nhap_diem = dataClass[i].marked == 0 || dataClass[i].marked == null ? false : true;
              obj.giang_day = dataClass[i].teachingAssignment == 0 || dataClass[i].teachingAssignment == null ? false : true;
              obj.thoi_khoa_bieu = dataClass[i].scheduled == 0 || dataClass[i].scheduled == null ? false : true;
              this.desserts.push(obj);
            }
            this.dessertsFirst = JSON.parse(JSON.stringify(this.desserts));
          })
          .catch(() => {
            this.$refs["toastMessage"].open("Tìm kiếm thất bại", true);
          });
      }
    },
    getAmountOfSemester(){
      if(this.objectSearch.class_id != null){
        return AppService.getAmountOfSemester(this.$store.state.year)
        .then((result) => {
          this.amountOfSemester = result.data.semesterAmount
          if(result.data.semesterAmount == 2){
            this.headers.push(
              { text: "Học kỳ II", value: "hoc_ky_2", sortable: false, align: 'center' },
            )
          }
        })
        .catch( () => {})
      }
    },
    getAllKhoi() { 
      return AppService.getAllKhoi().then((result) => {
        this.dataKhoi = result.data.data;
        this.choosedKhoi = this.dataKhoi[0].name;
        this.objectSearch.grade_level = this.dataKhoi[0].id;
        this.firstLoadDone = true;
      });
    },
    getAllKhoaBan() {
      return AppService.getAllDepartmentForClass().then((data) => {
        this.dataKhoaBan = data.data.data;
        this.choosedKhoaBan = this.dataKhoaBan[0].name;
        this.objectSearch.dept_id = this.dataKhoaBan[0].id;
      });
    },
    chooseHeSo(item, he_so) {
      if (item.nhap_diem) {
        this.$refs["toastMessage"].open(
          "Môn học đã được nhập điểm, không cho phép thay đổi hệ số",
          true
        );
      } else {
        if (he_so == "Lựa chọn") {
          item.he_so = 0;
        } else {
          item.he_so = he_so;
        }
        this.indexOfRowsChanged.push(item.stt);
      }
    },
    async search() {
      if (this.$refs["form-search"].validate()) {
        this.desserts = [];
        this.$refs['loading'].open()
        this.nameClassChoosedAfterSearch = this.choosedLop
        this.indexOfRowsChanged = []
        this.headers = [
          { text: "STT", value: "stt", sortable: false, align: 'center' },
          { text: "Tên môn học", value: "ten_mon_hoc", sortable: false },
          { text: "Học", value: "hoc", sortable: false, align: 'center' },
          { text: "Hệ số", value: "he_so", sortable: false },
          { text: "Cả năm", value: "ca_nam", sortable: false, align: 'center' },
          { text: "Học kỳ I", value: "hoc_ky_1", sortable: false, align: 'center' }
        ]
        await this.getAmountOfSemester()
        await AppService.getInfoSubjectBySearch(
          this.objectSearch.class_id,
          this.ten_mon_hoc == null ? "" : this.ten_mon_hoc,
          this.objectSearch.grade_level,
          this.objectSearch.dept_id
        )
          .then((data) => {
            this.updating = false;
            let dataClass = data.data.data.subjectClass;
            for (let i = 0; i < dataClass.length; i++) {
              let obj = {};
              obj.stt = i + 1;
              obj.id = dataClass[i].subjectClassId
              obj.ten_mon_hoc = dataClass[i].subjectName;
              obj.hoc = dataClass[i].study == 0 || dataClass[i].study == null ? false : true;
              obj.he_so =
                dataClass[i].study == 0 || dataClass[i].study == null ? 0 : dataClass[i].coefficient;

              obj.hoc_ky_1 = dataClass[i].flg_semester1 == 0 || dataClass[i].flg_semester1 == null ? false : true;
              obj.hoc_ky_2 = dataClass[i].flg_semester2 == 0 || dataClass[i].flg_semester2 == null ? false : true;
              obj.ca_nam = false
              if(this.amountOfSemester == 1){
                obj.ca_nam = obj.hoc_ky_1 ? true : false
              }else if(this.amountOfSemester == 2){
                if(obj.hoc_ky_1 && obj.hoc_ky_2){
                  obj.ca_nam = true
                }
              }

              obj.nhap_diem = dataClass[i].marked == 0 || dataClass[i].marked == null ? false : true;
              obj.giang_day = dataClass[i].teachingAssignment == 0 || dataClass[i].teachingAssignment == null ? false : true;
              obj.thoi_khoa_bieu = dataClass[i].scheduled == 0 || dataClass[i].scheduled == null ? false : true;
              obj.subjectId = dataClass[i].subjectId
              this.desserts.push(obj);
            }
            this.dessertsFirst = JSON.parse(JSON.stringify(this.desserts));
          })
          .catch(() => {
            this.$refs["toastMessage"].open("Tìm kiếm thất bại", true);
          });
        this.$refs['loading'].close()
      }else{
        this.desserts = []
        this.dessertsFirst = []
        this.updating = false
        this.nameClassChoosedAfterSearch = null
      }
    },
    changed(item) {
      if(this.amountOfSemester == 1){
        if (item.hoc_ky_1) {
          item.ca_nam = true;
        } else {
          item.ca_nam = false;
          item.hoc = false;
          item.he_so = 0;
        }
      }else if(this.amountOfSemester == 2){
        if (item.hoc_ky_1 && item.hoc_ky_2) {
          item.ca_nam = true;
        } else if (item.hoc_ky_1 || item.hoc_ky_2) {
          item.ca_nam = false;
          item.hoc = true;
        } else {
          item.ca_nam = false;
          item.hoc = false;
          item.he_so = 0;
        }
      }
      this.indexOfRowsChanged.push(item.stt);
    },
    changedCaNam(item) {
      if (item.ca_nam) {
        if(this.amountOfSemester == 1){
          item.hoc_ky_1 = true;
        }else if(this.amountOfSemester == 2){
          item.hoc_ky_1 = true;
          item.hoc_ky_2 = true;
        }
      } else {
        if(this.amountOfSemester == 1){
          item.hoc_ky_1 = false;
        }else if(this.amountOfSemester == 2){
          item.hoc_ky_2 = false;
          item.hoc_ky_1 = false;
        }
      }
      this.indexOfRowsChanged.push(item.stt);
    },
    save() {
      if (this.$refs["form"].validate()) {
        this.$refs["subjectForClassDialog"]
          .open()
          .then(() => {
            this.isDisableCancel = true;
            this.arrIncomingUpdate = [];
            this.loadingSave = true;
            let set = new Set();
            for (let elm of this.indexOfRowsChanged) {
              set.add(elm);
            }
            for (let elements of set) {
              if (this.desserts[elements - 1].hoc) {
                if (this.desserts[elements - 1].he_so != 0) {
                  if (
                    this.desserts[elements - 1].hoc_ky_1 ||
                    this.desserts[elements - 1].hoc_ky_2
                  ) {
                    this.arrIncomingUpdate.push(this.desserts[elements - 1]);
                  } else {
                    this.$refs["toastMessage"].open(
                      "Cần chọn học kỳ cho môn: " +
                        this.desserts[elements - 1].ten_mon_hoc,
                      true
                    );
                    this.loadingSave = false;
                    return;
                  }
                }
              }else{
                for(let k = 0; k < this.dessertsFirst.length; k++){
                  if(this.dessertsFirst[k].id !== null && this.desserts[elements - 1].subjectId == this.dessertsFirst[k].subjectId ){
                    this.arrIncomingUpdate.push(this.desserts[elements - 1]);
                    break;
                  }
                }
              }
            }
            //call api to save
            let arrTemp = []
            this.arrIncomingUpdate.forEach(element => {
              let tempObj = {}
              tempObj.subjectId = element.subjectId
              tempObj.coefficient = element.hoc ? element.he_so : 1
              tempObj.study = element.hoc ? 1 : 0
              tempObj.flgSemester1 = element.hoc_ky_1 ? 1 : 0
              tempObj.flgSemester2 = element.hoc_ky_2 ? 1 : 0
              tempObj.subjectClassId = element.id
              tempObj.classId = this.objectSearch.class_id
              arrTemp.push(tempObj)
            });

            AppService.saveSubjectForClass(arrTemp)
            .then( (response) => {
            
              if(response.data.message == 'success'){
                this.dessertsFirst = JSON.parse(JSON.stringify(this.desserts));
                this.indexOfRowsChanged = [];
                this.arrIncomingUpdate = [];
                this.$refs["toastMessage"].open("Cập nhật thành công", false);
                this.loadingSave = false;
                this.updating = false;
                this.isDisableCancel = false;
              }else{
                this.$refs["toastMessage"].open("Cập nhật thất bại", true);
                this.loadingSave = false;
                this.updating = false;
                this.isDisableCancel = false;
              }
            } )
            .catch(() => {
              this.$refs["toastMessage"].open("Cập nhật thất bại", true);
              this.loadingSave = false;
              this.updating = false;
              this.isDisableCancel = false;
            })
          })
          .catch(() => {});
      }
    },
    cancel() {
      this.desserts = JSON.parse(JSON.stringify(this.dessertsFirst));
      this.indexOfRowsChanged = [];
      this.arrIncomingUpdate = [];
      this.updating = false;
    },
    disableCheckbox(item) {
      if (item.nhap_diem || item.thoi_khoa_bieu || item.giang_day) {
        return true;
      }
      if (this.updating) {
        if (item.hoc) {
          return false;
        } else {
          return true;
        }
      }
      return true;
    },
    disableHoc(item) {
      if (item.nhap_diem || item.thoi_khoa_bieu || item.giang_day) {
        return true;
      } else {
        return !this.updating;
      }
    },
    changedHoc(item) {
      if (item.hoc) {
        item.ca_nam = true;
        if(this.amountOfSemester == 2){
          item.hoc_ky_1 = true;
          item.hoc_ky_2 = true;
        }else if(this.amountOfSemester == 1){
          item.hoc_ky_1 = true;
        }
        item.he_so = 1;
      } else {
        item.ca_nam = false;

        if(this.amountOfSemester == 2){
          item.hoc_ky_1 = false;
          item.hoc_ky_2 = false;
        }else if(this.amountOfSemester == 1){
          item.hoc_ky_1 = false;
        }
        item.he_so = 0;
      }
      this.indexOfRowsChanged.push(item.stt);
    },
    disableDDL(item) {
      if (this.updating) {
        return item.hoc;
      } else {
        return false;
      }
    },
    chooseKhoaBan(item) {
      this.objectSearch.dept_id = item.id;
      this.choosedKhoaBan = item.name;
    },
    chooseKhoi(item) {
      this.choosedKhoi = item.name;
      this.objectSearch.grade_level = item.id;
    },
    getClassFirstLoad() {
      return AppService.getClassByKhoiAndKhoaBan(
        this.objectSearch.grade_level,
        this.objectSearch.dept_id,
        this.$store.state.year
      ).then((data) => {
        this.dataLop = data.data.data.subjects;
        if (this.dataLop.length != 0) {
          this.choosedLop = this.dataLop[0].name;
          this.objectSearch.class_id = this.dataLop[0].id;
        } else {
          this.choosedLop = null;
          this.$refs["form-search"].resetValidation();
        }
      });
    },
    chooseLop(item) {
      this.choosedLop = item.name;
      this.objectSearch.class_id = item.id;
    },
    loadWhenChooseOption() {
      if (this.firstLoadDone) {
        if (this.choosedKhoi != null && this.choosedKhoaBan != null) {
          AppService.getClassByKhoiAndKhoaBan(
            this.objectSearch.grade_level,
            this.objectSearch.dept_id,
            this.$store.getters['year']
          ).then((data) => {
            this.dataLop = data.data.data.subjects;
            if (this.dataLop.length != 0) {
              this.choosedLop = this.dataLop[0].name;
              this.objectSearch.class_id = this.dataLop[0].id;
            } else {
              this.choosedLop = null;
              this.objectSearch.class_id = null;
              this.$refs["form-search"].resetValidation();
            }
          });
        }
      }
    },
  },
  watch: {
    choosedKhoi() {
      this.loadWhenChooseOption();
    },
    choosedKhoaBan() {
      this.loadWhenChooseOption();
    },
    watchYear(){
      if(this.watchYear === null){
        this.$watch('$store.state.year',async () => {
          this.desserts = []
          this.indexOfRowsChanged = []
          this.$refs['loading'].open()
          await this.getClassFirstLoad();
          await this.search()
          this.$refs['loading'].close()
        })
      }
    }
  },
};
</script>

<style>
.input-sfc .v-input__slot {
  margin: 0px;
  border: 2px solid gray;
  border-radius: 5px;
  padding-left: 5px;
}
.bottom-toast-mess {
  position: fixed;
  right: 1%;
  top: 1%;
  z-index: 10;
}
</style>