<template>
  <v-container class="SchoolInformation ma-0 pa-0" fluid height="100%">
    <v-row class="fill-height">
      <v-col cols="3">
        <v-card outlined height="100%">
          <v-toolbar
            dense
            dark
            class="font-weight-bold"
            color="primary lighten-1"
          >
            <v-toolbar-title class="text-center"
              >Tổ chức nhà trường</v-toolbar-title
            >
            <v-spacer></v-spacer>
          </v-toolbar>
          <v-treeview
            activatable
            hoverable
            transition
            color="primary"
            :items="structureTree"
            item-key="id"
          >
            <template class="ma-0 pa-0" slot="label" slot-scope="{ item }">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <h5 v-bind="attrs" v-on="on" style="cursor: pointer" @click="clickOnLeaf(item.code)">{{ formatTextTooltip(item.name, 50) }}</h5>
                  </template>
                  <span>{{ item.name }}</span>
                </v-tooltip>
            </template>
          </v-treeview>
          <v-spacer></v-spacer>
        </v-card>
      </v-col>
      <!-- <v-spacer></v-spacer> -->
      <v-col cols="9" class="mt-4">
        <v-row class="mb-3">
          <v-card width="99%">
            <v-toolbar
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
            >
              <v-toolbar-title
                >Thông tin tìm kiếm</v-toolbar-title
              >
              <v-spacer></v-spacer>
            </v-toolbar>
            <v-row class="mx-2 mt-1">
              <v-col>
                <v-text-field
                  v-model="maDonVi"
                  label="Mã đơn vị"
                ></v-text-field>
              </v-col>

              <v-col>
                <v-text-field
                  v-model="tenDonVi"
                  label="Tên đơn vị"
                ></v-text-field>
              </v-col>
            </v-row>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn @click="searchKhoaBan()" dark color="primary">
                <v-icon>mdi-magnify</v-icon>
                Tìm kiếm
              </v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-row>

        <v-row>
          <v-card width="99%" class="mt-3">
            <v-toolbar
              dense
              dark
              class="font-weight-bold"
              color="primary lighten-1"
            >
              <v-toolbar-title
                >Kết quả tìm kiếm</v-toolbar-title
              >
              <v-spacer></v-spacer>
              <v-btn @click="addNewDepartment()" color="white" class="float-right" dark>
                <v-icon color="green">mdi-plus-outline</v-icon>
                <p class="ma-0" style="color: black">Thêm mới</p>
              </v-btn>
            </v-toolbar>
            <v-data-table
              :headers="this.headersKhoaBan"
              :items="this.searchResult"
              :items-per-page="5"
              disable-sort
              :footer-props="{
                  'items-per-page-text': 'Số dòng mỗi trang:',
              }"
          >
          <template slot="no-data">
              Danh sách phòng ban rỗng
            </template>

              <template v-slot:item.actions="{ item }">
                  <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
                  <v-icon small class="mr-2" @click="deleteItem(item)"> mdi-delete </v-icon>
              </template>
              <template v-slot:footer.page-text="props">
                  {{ props.pageStart }}-{{ props.pageStop }} của {{ props.itemsLength }} kết quả
              </template>
          </v-data-table>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-row>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
    <SchoolDepartmentDialog
      ref="SchoolDepartmentDialog"
    ></SchoolDepartmentDialog>
        <SchoolUpdateDepartmentDialog
      ref="SchoolUpdateDepartmentDialog"
    ></SchoolUpdateDepartmentDialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <Loading ref="Loading"></Loading>
    <ConfirmDialog ref="confirmDialog" txtTitle="Xóa đơn vị" question="Bạn đã chắc chắn muốn xóa đơn vị này không?"/>
  </v-container>
</template>

<script>
import SchoolDepartmentDialog from "@/views/Dialogs/SchoolDepartmentDialog.vue";
import SchoolUpdateDepartmentDialog from "@/views/Dialogs/SchoolUpdateDepartmentDialog.vue";
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default {
  name: "SchoolInformation",
  components: { SchoolDepartmentDialog, ToastMessage, Loading, ConfirmDialog, SchoolUpdateDepartmentDialog },
  mounted() {
    this.$refs["Loading"].open();
    this.buildTreeGroup();
    this.searchKhoaBan()
  },
  data: () => ({
    isDelete: true,
    isEdit: true,
    maDonVi: "",
    tenDonVi: "",
    partentMap: new Map(),
    departmentMap: new Map(),
    structureTree: [],
    search: "",
    headersKhoaBan: [
      {
        text: "STT",
        align: "start",
        sortable: false,
        value: "stt",
      },
      { text: "Mã đơn vị", value: "code" },
      { text: "Tên đơn vị", value: "name" },
      { text: "Người phụ trách", value: "employeeName" },
      { text: "Chức vụ", value: "positionName" },
      { text: "Mô tả nhiệm vụ", value: "description" },
      { text: "Thao tác", value: "actions" },
    ],
    searchResult: [],
    roleMap: {
        0: "Hiệu Trưởng",
        1: "Trưởng phòng",
        2: "Trưởng Khoa/Ban",
        3: "Trưởng Tổ bộ môn",
        4: "Trưởng bộ môn",
    }
  }),
  computed: {},
  methods: {
    formatTextTooltip(item, max) {
      if (item === null || item === undefined) {
        return ""
      }
      if (item.length >= max) {
        return item.substring(0, max) + "...";
      }
      return item;
    },
    editItem(department) {
      this.$refs["SchoolUpdateDepartmentDialog"]
        .open(department, "Chỉnh sửa khoa ban")
        .then(() => {
          this.$refs["ToastMessage"].open("Cập nhật phòng ban thành công!", false);
          this.buildTreeGroup()
          this.searchKhoaBan()
        }, () => {
          this.$refs["ToastMessage"].open("Cập nhật phòng ban lỗi!", true);
        });
    },
    deleteItem(item) {
      this.$refs['confirmDialog'].open().then(()=>{
        this.$refs["Loading"].open();
        AppService.deleteDepartment(item.id).then(
                (res) => {
                  if(res.data.status === "OK") {
                    this.$refs["ToastMessage"].open("Xóa phòng ban thành công!", false);
                    this.buildTreeGroup()
                    this.searchKhoaBan()
                    this.$refs["Loading"].close();
                  } else {
                    this.$refs["ToastMessage"].open(res.data.message, true);
                    this.$refs["Loading"].close();
                  }
                }
              ).catch(()=>{
                this.$refs["Loading"].close();
              });
      }).catch(() => {})
    },
    buildTreeGroup() {
      AppService.getStructureOfGroup().then(
        (data) => {
          this.structureTree = this.buildTreeFromRaw(data.data.data);
          this.$refs["Loading"].close();
        },
        () => {
          this.$refs["ToastMessage"].open("Tải dữ liệu phòng ban lỗi!");
        }
      );
    },
    buildTreeGroupFromMap(root, partentMap, departmentMap) {
      let currentLeaf = [];
      if (partentMap[root] === undefined) {
        return [];
      }
      let childs = partentMap[root];
      if (childs.length === 0) {
        return [];
      }
      for (let child of childs) {
        currentLeaf.push({
          id: child,
          name: departmentMap[child].name,
          code: departmentMap[child].code,
          children: this.buildTreeGroupFromMap(child, partentMap, departmentMap),
        });
      }
      return currentLeaf;
    },
    buildTreeFromRaw (listData) {
      let partentMap = new Map();
      let departmentMap = new Map();
      let structureTree = [];


      for (let department of listData) {
        if (department.parent.id === null) {
          department.parent.id = 0
        }
        if (partentMap[department.parent.id] === undefined) {
          partentMap[department.parent.id] = [];
        }
        partentMap[department.parent.id].push(department.id);
        departmentMap[department.id] = department;
      }
      this.partentMap = partentMap
      this.departmentMap = departmentMap
      structureTree = this.buildTreeGroupFromMap(0, partentMap, departmentMap);
      return structureTree
    },
    searchKhoaBan() {
      let countStt = 0;
      this.$refs["Loading"].open();
      AppService.searchKhoaBan(this.maDonVi, this.tenDonVi)
        .then(
          (data) => {
            this.searchResult = [];
            for (let department of data.data.data) {
              countStt += 1;
              this.searchResult.push({
                stt: countStt,
                id: department.id,
                code: department.code,
                name: department.name,
                parentID: department.parent.id,
                employeeName: department.employee.name,
                employeeID: department.employee.id,
                position: department.position,
                positionName: this.roleMap[department.position],
                description: department.description,
                actions: "",
                type: department.type.code,
              });
            }
            if (this.searchResult.length === 0) {
              this.$refs["ToastMessage"].open("Danh sách phòng ban trống", true);
            }
          },
          () => {
            this.$refs["ToastMessage"].open("Danh sách phòng ban trống", true);
          }
        )
        .then(() => {
          this.$refs["Loading"].close();
        });
    },
    addNewDepartment() {
      this.$refs["SchoolDepartmentDialog"]
        .open(undefined, "Thêm mới khoa ban")
        .then(() => {
          this.buildTreeGroup()
          this.searchKhoaBan()
        }, ()=> {});
    },
    clickOnLeaf(code) {
      this.maDonVi = code
      this.searchKhoaBan()
    }
  },
};
</script>

<style scoped lang="scss">
</style>
