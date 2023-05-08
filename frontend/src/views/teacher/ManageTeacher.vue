<template>
<div>
    <v-card class="mb-3">
        <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
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
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Đơn vị *" readonly v-model="choosedDonVi" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataDonVi" :key="index" @click="chooseDonVi(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Khoa/ban *" readonly v-model="choosedKhoaBan">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataKhoaBan" :key="index" @click="chooseKhoaBan(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Chức vụ" readonly v-model="choosedChucVu" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataChucVu" :key="index" @click="chooseChucVu(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-text-field v-model="txtTeacher" label="Giáo viên" placeholder="Nhập thông tin giáo viên" :rules="ruleMaxLength250"></v-text-field>
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
    <v-card class="mb-3">
        <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
            <v-toolbar-title style="width: 100%; align-items: center" class="d-flex">
                <span>Danh sách cán bộ giáo viên</span>
                <v-spacer></v-spacer>
                <v-btn style="background-color:white;color:black;" v-if="canEdit" @click="$router.push({ name: 'them-moi-giao-vien'})">
                    <v-icon color="green">mdi-plus-outline</v-icon>
                    <v-spacer></v-spacer>
                    <span style="color:black;">Tạo mới</span>
                </v-btn>
            </v-toolbar-title>
        </v-toolbar>
        <v-data-table :header-props="{ sortIcon: null }" :custom-sort="sortByName" :headers="headers" :items="desserts" class="elevation-1" :items-per-page="10" :footer-props="{
          'items-per-page-text': 'Số dòng mỗi trang:',
        }">
            <template slot="no-data">
                Danh sách giáo viên rỗng
            </template>

            <template v-slot:item.sex="{ item }">
                {{ item.sex == 0 ? 'Nam' : 'Nữ' }}
            </template>

            <template v-slot:item.Authorities="{ item }">
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <span v-bind="attrs" v-on="on">{{
                formatTextTooltip(item.Authorities, 35)
              }}</span>
                    </template>
                    <span>{{ item.Authorities }}</span>
                </v-tooltip>
            </template>

            <template v-slot:item.action="{ item }">
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <router-link style="text-decoration: none;" :to="'/giao-vien/thong-tin/' + item.teacherId">
                            <v-icon v-bind="attrs" v-on="on" style="margin-right: 10px">
                                mdi-eye
                            </v-icon>
                        </router-link>
                    </template>
                    <span>Xem hồ sơ</span>
                </v-tooltip>

                <v-tooltip bottom v-if="canEdit">
                    <template v-slot:activator="{ on, attrs }">
                        <router-link style="text-decoration: none;" :to="'/giao-vien/cap-nhat/' + item.teacherId">
                            <v-icon v-bind="attrs" v-on="on">
                                mdi-pencil
                            </v-icon>
                        </router-link>
                    </template>
                    <span>Chỉnh sửa thông tin</span>
                </v-tooltip>
            </template>

            <template v-slot:footer.page-text="props">
                {{ props.pageStart }}-{{ props.pageStop }} của
                {{ props.itemsLength }} kết quả
            </template>
        </v-data-table>
    </v-card>
    <ToastMessage ref="toastMessage" />
    <Loading ref="Loading"></Loading>
</div>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";
import Loading from "@/components/Loading.vue";

export default {
    components: {
        ToastMessage,
        Loading
    },
    data() {
        return {
            txtTeacher: "",
            headers: [{
                    text: "STT",
                    value: "stt"
                },
                {
                    text: "Mã cán bộ",
                    value: "code"
                },
                {
                    text: "Họ và tên",
                    value: "teacherName"
                },
                {
                    text: "Giới tính",
                    value: "sex"
                },
                {
                    text: "Khoa/ban",
                    value: "deptName"
                },
                {
                    text: "Chức vụ",
                    value: "Authorities"
                },
                {
                    text: "Loại hợp đồng",
                    value: "contractType"
                },
                {
                    text: "Thao tác",
                    value: "action"
                },
            ],
            desserts: [],
            choosedDonVi: null,
            dataDonVi: [],
            choosedKhoaBan: null,
            dataKhoaBan: [],
            choosedChucVu: null,
            dataChucVu: [],
            objectSearch: {
                idDonVi: null,
                idKhoaBan: null,
                codeChucVu: null,
                txtTeacher: "",
            },
            rules: [
                (v) => !!v || 'Không được để trống'
            ],
            canEdit: true,
            ruleMaxLength250: [
                (v) => !v ? true : v.length <= 250 || 'Tối đa 250 ký tự'
            ]
        };
    },
    async mounted() {
        this.$refs['Loading'].open()
        await this.$store.dispatch('getCurrentUser')
            .then(async () => {
                let id = this.$store.getters['user'].id
                if (this.$store.getters['user'].roles.includes('ROLE_ADMIN') ||
                    this.$store.getters['user'].roles.includes('ROLE_HT') ||
                    this.$store.getters['user'].roles.includes('ROLE_HP')
                ) {
                    await this.getDropdownDonVi();
                } else if (this.$store.getters['user'].roles.includes('ROLE_TK')) {
                    this.dataDonVi.push({
                        id: -1,
                        name: 'Đơn vị đào tạo'
                    })
                    this.chooseDonVi(this.dataDonVi[0])
                    await this.getDropdownKhoaBanForTruongKhoa(id)
                }
                if (this.$store.getters['user'].roles.includes('ROLE_TK') ||
                    this.$store.getters['user'].roles.includes('ROLE_HP')) {
                    this.canEdit = false
                }
                await this.getAllChucVu();
            })
            .catch(() => {
                this.$refs['toastMessage'].open("Lỗi khi lấy thông tin của người dùng hiện tại", true)
            })
            .finally(() => {
                this.$nextTick(()=>{this.search()})
                this.$refs['Loading'].close()
            })

    },
    methods: {
        sortByName(items, index, isDescending) {
          items.sort((a, b) => {
            return a.teacherName.split(" ").slice(-1)[0].toLowerCase().localeCompare(b.teacherName.split(" ").slice(-1)[0].toLowerCase());
          });
          let countI = 0
          for(let ii of items) {
            countI += 1
            ii.stt = countI
          }
          return items;
        },
        chooseDonVi(item) {
            this.objectSearch.idDonVi = item.id;
            this.choosedDonVi = item.name;
        },
        chooseKhoaBan(item) {
            this.objectSearch.idKhoaBan = item.id;
            this.choosedKhoaBan = item.name;
        },
        chooseChucVu(item) {
            this.objectSearch.codeChucVu = item.code;
            this.choosedChucVu = item.name;
        },
        search() {
            if (this.$refs['form-search'].validate()) {
                this.$refs["Loading"].open();
                AppService.getInfoTeacherBySearch({
                        nameCode: this.txtTeacher.trim(),
                        authorityCode: this.objectSearch.codeChucVu,
                        deptId: this.objectSearch.idKhoaBan ? this.objectSearch.idKhoaBan : this.objectSearch.idDonVi
                    })
                    .then((res) => {
                        this.desserts = res.data.data.Teachers
                        let stt = 1
                        this.desserts.forEach(x => {
                            x.stt = stt++
                            x.contractType == 0 ? x.contractType = 'Hợp đồng' : x.contractType = 'Biên chế'

                        })
                    })
                    .catch(() => {
                        this.$refs['toastMessage'].open("Tìm kiếm xảy ra lỗi", true)
                    })
                this.$refs["Loading"].close();
            } else {
                this.desserts = []
                this.objectSearch = {}
            }
        },
        getAllChucVu() {
            return AppService.getAllChucVu().then((res) => {
                this.dataChucVu = res.data.data.authorities;
                this.dataChucVu.unshift({
                    code: "",
                    name: "Lựa chọn",
                });
                this.chooseChucVu(this.dataChucVu[0]);
            });
        },
        getDropdownDonVi() {
            return AppService.getDropdownDonVi().then((response) => {
                    if (response.data.data.length != 0) {
                        this.dataDonVi = response.data.data;
                        this.chooseDonVi(this.dataDonVi[0]);
                    }
                })
                .catch(() => {
                    this.$refs['toastMessage'].open('Lấy dữ liệu đơn vị thất bại', true)
                })
        },
        getDropdownKhoaBanForTruongKhoa(id) {
            return AppService.getDropdownKhoaBanForTruongKhoa(id)
                .then((res) => {
                    if (res.data.data.departments.length != 0) {
                        this.dataKhoaBan = res.data.data.departments
                        this.chooseKhoaBan(this.dataKhoaBan[0])
                    } else {
                        this.dataKhoaBan = []
                        this.objectSearch.idKhoaBan = -1
                        this.choosedKhoaBan = ''
                    }
                })
                .catch(() => {})
        },
        formatTextTooltip(item, max) {
            if (item.length >= max) {
                return item.substring(0, max) + "...";
            }
            return item;
        },
    },
    watch: {
        choosedDonVi() {
            if (this.objectSearch.idDonVi != -1) {
                AppService.getDropdownKhoaBanTeacher(this.objectSearch.idDonVi).then(
                    (res) => {
                        if (res.data.data.length != 0) {
                            this.dataKhoaBan = res.data.data;
                            this.chooseKhoaBan(this.dataKhoaBan[0]);
                        } else {
                            this.dataKhoaBan = []
                            this.choosedKhoaBan = ''
                            this.objectSearch.idKhoaBan = null
                        }
                    }
                )
            }
        },
    },
};
</script>
