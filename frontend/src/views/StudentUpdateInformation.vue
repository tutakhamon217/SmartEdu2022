<template>
<v-container class="StudentUpdateInformation ma-0 pa-0" fluid height="100%">
    <v-row class="fill-height" width="90%">
        <!-- <v-spacer></v-spacer> -->
        <v-col cols="12" class="ma-4">
            <v-form class="mb-7" width="100%" ref="form" v-model="valid" lazy-validation>
                <v-card width="98%">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin lý lịch cá nhân</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="pa-4">
                        <v-col cols="4">
                            <v-list nav dense>
                                <v-list-item class="mr-4">
                                    <v-spacer></v-spacer>
                                    <v-img height=120 max-height="295" max-width="250" contain lazy-src="https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png" :src="imageBase64"></v-img>
                                    <v-spacer></v-spacer>
                                </v-list-item>
                                <v-list-item>
                                    <v-spacer></v-spacer>
                                    <v-btn @click="hanndleUpload" depressed color="primary">Chọn ảnh
                                        <input accept="image/*" v-show="false" ref="upload" type="file" @change="Preview_image" />
                                    </v-btn>
                                    <v-spacer></v-spacer>
                                </v-list-item>

                            </v-list>

                            <v-divider></v-divider>

                            <v-list nav dense>
                                <v-list-item>
                                    <v-row justify="center" align="center">
                                        <v-col>Mã học sinh (*):</v-col>
                                        <v-col>
                                            <v-text-field disabled :rules="[this.rule.maHocSinh, this.rule.required]"  v-model="maHocSinh">
                                            </v-text-field>
                                        </v-col>
                                    </v-row>
                                </v-list-item>
                                <v-list-item>
                                    <v-row justify="center" align="center">
                                        <v-col>Họ và tên (*):</v-col>
                                        <v-col>
                                            <v-text-field :rules="[this.rule.tenHocSinh, this.rule.required]" v-model="tenHocSinh">
                                            </v-text-field>
                                        </v-col>
                                    </v-row>
                                </v-list-item>
                                <v-list-item>
                                    <v-row justify="center" align="center">
                                        <v-col>Trạng thái (*):</v-col>
                                        <v-col>
                                            <v-select :items="trangThaiList" v-model="trangThai" item-value="id" item-text="name"></v-select>
                                        </v-col>
                                    </v-row>
                                </v-list-item>
                            </v-list>
                        </v-col>
                        <v-divider class="mt-2" vertical></v-divider>
                        <v-col cols="8">
                            <v-row>
                                <v-col>
                                    <v-list nav dense>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Khối (*):</v-col>
                                                <v-col>
                                                    <v-select :items="khoiList" v-model="khoi" item-value="id" item-text="name"></v-select>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Tên lớp (*):</v-col>
                                                <v-col>
                                                    <v-select :disabled="!enableChangeClass" :items="lopList" :rules="[this.rule.required]" v-model="lop" item-value="code" item-text="name"></v-select>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Số điện thoại:</v-col>
                                                <v-col>
                                                    <v-text-field :rules="[this.rule.required, this.rule.soDienThoai]" v-model="soDienThoai"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Ngày sinh:</v-col>
                                                <v-col>
                                                    <div class="mr-4">
                                                        <v-menu ref="ngaySinhPicker" v-model="menuNgaySinh" :close-on-content-click="false" transition="scale-transition" offset-y min-width="auto">
                                                            <template v-slot:activator="{ on, attrs }">
                                                                <v-text-field v-model="ngaySinhDisplay" append-icon="mdi-calendar" readonly v-bind="attrs" v-on="on"></v-text-field>
                                                            </template>
                                                            <v-date-picker locale="vi-VN" v-model="ngaySinh" :active-picker.sync="activePickerNgaySinh" @change="ngaySinhSave"></v-date-picker>
                                                        </v-menu>
                                                    </div>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Dân tộc:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="danToc" :rules="[this.rule.danToc]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Địa chỉ thường trú:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="diaChiThuongTru" :rules="[this.rule.diaChiThuongTru]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Số sổ BHXH:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="soSoBHXH" :rules="[this.rule.soSoBHXH]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Ngày cấp CMND/TCC:</v-col>
                                                <v-col>
                                                    <div class="mr-4">
                                                        <v-menu ref="ngayCapPicker" v-model="menuNgayCap" :close-on-content-click="false" transition="scale-transition" offset-y min-width="auto">
                                                            <template v-slot:activator="{ on, attrs }">
                                                                <v-text-field v-model="ngayCapDisplay" append-icon="mdi-calendar" readonly v-bind="attrs" v-on="on"></v-text-field>
                                                            </template>
                                                            <v-date-picker locale="vi-VN" v-model="ngayCap" :active-picker.sync="activePickerNgayCap" @change="ngayCapSave"></v-date-picker>
                                                        </v-menu>
                                                    </div>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Giới tính:</v-col>
                                                <v-col>
                                                    <v-select :items="gioiTinhList" v-model="gioiTinh" item-value="id" item-text="name"></v-select>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                    </v-list>
                                </v-col>
                                <v-col>
                                    <v-list nav dense>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Khoa ban:</v-col>
                                                <v-col>
                                                    <v-select :disabled="!enableChangeClass" :items="khoaList" v-model="khoa" item-value="id" item-text="name"></v-select>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Hệ đào tạo (*):</v-col>
                                                <v-col>
                                                    <v-select :items="heDaoTaoList" v-model="heDaoTao" item-value="id" item-text="name"></v-select>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Email:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="email" :rules="[this.rule.email, this.rule.emailPattern]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Tôn giáo:</v-col>
                                                <v-col>
                                                    <v-text-field :rules="[this.rule.tonGiao]" v-model="tonGiao"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Quê quán:</v-col>
                                                <v-col>
                                                    <v-text-field :rules="[this.rule.queQuan]" v-model="queQuan"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Địa chỉ tạm trú:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="diaChiTamTru" :rules="[this.rule.diaChiTamTru]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Số CMND/TCC:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="soCMND" :rules="[this.rule.soCMND]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Nơi cấp CMND/TCC:</v-col>
                                                <v-col>
                                                    <v-text-field v-model="noiCap" :rule="[this.rule.noiCap]"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                    </v-list>
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                </v-card>
                <v-card width="98%" class="mt-5">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin vào trường</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="mx-2">
                        <v-col class="mt-2">
                            <div>
                                <v-menu ref="ngayVaoTruongPicker" v-model="menuNgayVaoTruong" :close-on-content-click="false" transition="scale-transition" offset-y min-width="auto">
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-text-field v-model="ngayVaoTruongDisplay" append-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" label="Ngày vào trường"></v-text-field>
                                    </template>
                                    <v-date-picker locale="vi-VN" v-model="ngayVaoTruong" :active-picker.sync="activePickerNgayVaoTruong" @change="ngayVaoTruongSave"></v-date-picker>
                                </v-menu>
                            </div>
                        </v-col>
                        <v-col class="mt-2">
                            <v-select :items="hinhThucTrungTuyenList" label="Hình thức trúng tuyển" v-model="hinhThucTrungTuyen" item-value="id" item-text="name"></v-select>
                        </v-col>
                        <v-col class="mt-2">
                            <v-select :items="loaiTotNghiepCapDuoiList" label="Loại tốt nghiệp cấp dưới " v-model="loaiTotNghiepCapDuoi" item-value="id" item-text="name"></v-select>
                        </v-col>
                    </v-row>
                </v-card>
                <v-card width="98%" class="mt-5">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin liên lạc phụ huynh học sinh</v-toolbar-title>

                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="mx-2 mb-2 mt-4">
                        <v-col class="mt-2">
                            <v-select :items="quanHeList" label="Quan hệ (*)" v-model="quanHe" item-value="id" item-text="name"></v-select>
                        </v-col>
                        <v-col class="mt-2">
                            <v-text-field v-model="hoTenPhuHuynh" :rules="[this.rule.hoTenPhuHuynh, this.rule.required]" label="Họ tên phụ huynh (*)"></v-text-field>
                        </v-col>
                        <v-col class="mt-2">
                            <v-text-field :rules="[ this.rule.required, this.rule.soDienThoai]" v-model="soDienThoaiPhuHuynh" label="Số điện thoại (*)"></v-text-field>
                        </v-col>
                    </v-row>

                    <v-row class="mx-2 mb-2">
                        <v-col>
                            <v-select :items="quanHeList" label="Quan hệ" v-model="quanHeSecond" item-value="id" item-text="name"></v-select>
                        </v-col>
                        <v-col>
                            <v-text-field v-model="hoTenPhuHuynhSecond" :rules="[this.rule.hoTenPhuHuynh]" label="Họ tên phụ huynh"></v-text-field>
                        </v-col>
                        <v-col>
                            <v-text-field :rules="[this.rule.soDienThoai]" v-model="soDienThoaiPhuHuynhSecond" label="Số điện thoại"></v-text-field>
                        </v-col>
                    </v-row>
                </v-card>
            </v-form>
            <v-row>
                <v-spacer></v-spacer>
                <v-btn class="mr-2" @click="discard()" elevation="1" color="warning darken-1" text>
                    Hủy bỏ
                </v-btn>
                <v-btn class="ml-2" @click="updateStudent()" elevation="1" color="primary darken-1" text>
                    Lưu lại
                </v-btn>
                <v-spacer></v-spacer>
            </v-row>
        </v-col>
        <v-spacer></v-spacer>
    </v-row>

    <ToastMessage ref="ToastMessage" style="display: fixed;
    bottom: 0;"> </ToastMessage>
    <Loading ref="Loading"></Loading>
</v-container>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage.vue";
import Loading from "@/components/Loading.vue";

export default {
    name: "StudentUpdateInformation",
    components: {
        ToastMessage,
        Loading
    },
    created() {},
    data () {
        return {
        enableChangeClass: false,
        valid: true,
        image: "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png",
        imageBase64: "1111",
        url: "",
        isDelete: false,
        isEdit: true,
        activePickerNgaySinh: null,
        activePickerNgayCap: null,
        activePickerNgayVaoTruong: null,
        maHocSinh: "",
        tenHocSinh: "",
        khoi: "",
        khoa: "",
        trangThai: 0,
        lop: "",
        heDaoTao: 1,
        gioiTinh: 0,
        noiCap: "",
        ngaySinh: null,
        ngaySinhDisplay: "",
        ngayCap: "",
        ngayCapDisplay: "",
        ngayVaoTruongDisplay: "",
        email: "",
        menuNgaySinh: false,
        menuNgayCap: false,
        menuNgayVaoTruong: false,
        ngayVaoTruong: "",
        soDienThoai: "",
        danToc: "",
        diaChiThuongTru: "",
        soSoBHXH: "",
        tonGiao: "",
        queQuan: "",
        diaChiTamTru: "",
        soCMND: "",
        hoTenPhuHuynh: "",
        soDienThoaiPhuHuynh: "",
        quanHe: 0,
        hoTenPhuHuynhSecond: "",
        soDienThoaiPhuHuynhSecond: "",
        quanHeSecond: 0,
        hinhThucTrungTuyen: 1,
        loaiTotNghiepCapDuoi: 1,

        phuHuynhPrimaryId: "",
        phuHuynhSecondId: "",
        trangThaiList: [{
                id: 0,
                name: "Đang học"
            },
            {
                id: 1,
                name: "Bảo lưu"
            },
            {
                id: 2,
                name: "Đã thôi học"
            },
            {
                id: 3,
                name: "Đã chuyển trường"
            },
        ],
        khoiList: [],
        khoaList: [],
        lopList: [],
        heDaoTaoList: [{
                id: 1,
                name: "Chính quy"
            },
            {
                id: 2,
                name: "Tại Chức"
            },
            {
                id: 3,
                name: "Vừa học vừa làm"
            },
            {
                id: 4,
                name: "Liên thông"
            },
            {
                id: 5,
                name: "Đào tạo nghề"
            },
            {
                id: 6,
                name: "Hợp tác đào tạo"
            },
        ],
        gioiTinhList: [{
                id: 0,
                name: "Nam"
            },
            {
                id: 1,
                name: "Nữ"
            },
        ],
        hinhThucTrungTuyenList: [{
                id: 1,
                name: "Thi tuyển"
            },
            {
                id: 2,
                name: "Xét tuyển"
            }
        ],
        loaiTotNghiepCapDuoiList: [{
                id: 1,
                name: "Giỏi"
            },
            {
                id: 2,
                name: "Khá"
            },
            {
                id: 3,
                name: "Trung bình"
            },
        ],
        quanHeList: [{
                id: 0,
                name: "Bố"
            },
            {
                id: 1,
                name: "Mẹ"
            },
            {
                id: 2,
                name: "Ông Bà"
            },
            {
                id: 3,
                name: "Anh Chị"
            },
            {
                id: 4,
                name: "Cô chú"
            },
            {
                id: 5,
                name: "Người giám hộ"
            },
        ],
        headerOfPhoneNumber: [
            "032",
            "033",
            "034",
            "035",
            "036",
            "037",
            "038",
            "039",
            "096",
            "097",
            "098",
            "086",
            "070",
            "077",
            "079",
            "076",
            "078",
            "081",
            "082",
            "083",
            "084",
            "085",
            "056",
            "058",
            "059",
            "094",
            "091",
        ],
        ruleSoDienThoai:  [(v) => !!v || "Không được để trống",
                (v) =>!v ?true :v.length >= 3 ?this.headerOfPhoneNumber.some((x) => x == v.substring(0, 3)) ||"Sai định dạng số điện thoại" :false || "Chứa 10 ký tự số",
                (v) =>!v ? true : v.length == 10 ? true : false || "Chứa 10 ký tự số"
        ],
        rule: {
            required: value => !!value || 'Bắt buộc',
            maHocSinh: value => value.length <= 50 || 'Mã học sinh không quá 50 ký tự',
            tenHocSinh: value => value.length <= 250 || 'Tên  học sinh không quá 250 ký tự',
            so: value => {
                const pattern = /^\d+$/
                return pattern.test(value) || value.length === 0 || 'Số điện thoại không hợp lệ'
            },
            email: value => value.length <= 250 || 'Email không quá 250 ký tự',
            emailPattern: value => {
                const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                return pattern.test(value) || value.length === 0 || 'Email không hợp lệ'
            },
            tonGiao: value => value.length <= 250 || 'Tôn giáo không quá 250 ký tự',
            danToc: value => value.length <= 250 || 'Dân tộc không quá 250 ký tự',
            diaChiThuongTru: value => value.length <= 250 || 'Địa chỉ thường trú không quá 250 ký tự',
            soSoBHXH: value => value.length <= 250 || 'Số sổ BHXH không quá 250 ký tự',
            noiCap: value => value.length <= 250 || 'Nơi cấp không quá 250 ký tự',
            queQuan: value => value.length <= 250 || 'Quê quán không quá 250 ký tự',
            diaChiTamTru: value => value.length <= 250 || 'Địa chỉ tạm trú không quá 250 ký tự',
            soCMND: value => value.length <= 250 || 'Số CMND/TCC không quá 250 ký tự',
            hoTenPhuHuynh: value => value.length <= 250 || 'Họ tên phụ huynh không quá 250 ký tự',
            soDienThoai: (value)=>{
                if(value===null || value.length === 0) return true
                const pattern = /^\d+$/
                if(!pattern.test(value)) return 'Số điện thoại không hợp lệ'
                if (value.length!==10) return "Độ dài số điện thoại phải chứa 10 ký tự số"
                if(!this.headerOfPhoneNumber.some((x) => x == value.substring(0, 3))) return "Số điện thoại không hợp lệ"
                return true
            }
        },
    }},
    computed: {},
    async mounted() {
        this.$refs["Loading"].open()
        this.khoiList = []
        await AppService.getAllGradeLevel().then((data) => {
            for (let grade of data.data.data) {
                this.khoiList.push({
                    id: grade.id,
                    name: grade.name
                })
            }
            if (this.khoiList.length > 0) {
                this.khoi = this.khoiList[0].id
            }
        })
        this.khoaList = []
        await AppService.getAllDepartments().then((data) => {
            for (let department of data.data.data) {
                this.khoaList.push({
                    id: department.id,
                    name: department.name
                })
            }
            if (this.khoaList.length > 0) {
                this.khoa = this.khoaList[0].id
            }
            
        })

        
        await AppService.getClassByGradeAndYears(this.khoi, this.$store.getters["year"], this.khoa).then((data) => {
            this.lopList = []
            this.lop = ""
            for (let lop of data.data.data) {
                this.lopList.push({
                    id: lop.id,
                    name: lop.code + " - " + lop.name,
                    code: lop.code
                })
            }
        }, () => {}).catch(() => {})

        await AppService.getStudentDetailInformationByYear(this.$route.query.student_code, this.$store.getters["year"]).then((data) => {
            if (data.data.data.length == 0) {
                this.$refs["ToastMessage"].open("Lỗi khi lấy thông tin học sinh !", true);
                setTimeout(() => {
                    this.$router.push({
                        name: "quan-ly-hoc-sinh"
                    });
                }, 500)
                return
            }
            let parent_primary = data.data.data[0];
            // let parent_second = null
            // if (data.data.data.length == 2) {
            //   parent_second = data.data.data[1]
            // }
            this.imageBase64 = parent_primary.avatar
            this.maHocSinh = parent_primary.code
            this.tenHocSinh = parent_primary.full_name
            this.khoi = parent_primary.grade_level
            this.khoa = parent_primary.department_id
            this.trangThai = parent_primary.status
            this.lop = parent_primary.class_code
            this.heDaoTao = parent_primary.training_system
            this.gioiTinh = parent_primary.sex
            this.noiCap = parent_primary.issued_address
            this.ngaySinh = parent_primary.birth_day.slice(0, parent_primary.birth_day.indexOf(" "))
            this.ngayCap = parent_primary.issued_date.slice(0, parent_primary.issued_date.indexOf(" "))
            this.ngayVaoTruong = parent_primary.start_date.slice(0, parent_primary.start_date.indexOf(" "))
            this.email = parent_primary.email
            this.soDienThoai = parent_primary.phone
            this.danToc = parent_primary.nation
            this.diaChiThuongTru = parent_primary.permanent_address
            this.soSoBHXH = parent_primary.social_insurance_number
            this.tonGiao = parent_primary.religion
            this.queQuan = parent_primary.home_town
            this.diaChiTamTru = parent_primary.temporary_address
            this.soCMND = parent_primary.identity_card
            this.hoTenPhuHuynh = parent_primary.parent_name
            this.soDienThoaiPhuHuynh = parent_primary.parent_phone
            this.quanHe = parent_primary.relationship
            this.hoTenPhuHuynhSecond = parent_primary.parent_name_second === null?"":parent_primary.parent_name_second
            this.soDienThoaiPhuHuynhSecond = parent_primary.parent_phone_second === null?"":parent_primary.parent_phone_second
            this.quanHeSecond = parent_primary.relationship_second === null?"":parent_primary.relationship_second
            this.hinhThucTrungTuyen = parent_primary.elect_format
            this.loaiTotNghiepCapDuoi = parent_primary.graduation_type
            this.phuHuynhPrimaryId = parent_primary.parent_id
            
        })

        await AppService.checkCanChangeClass(this.$store.getters["year"], this.$route.query.student_code).then((res)=>{
            if (res.data.status === "OK") {
                this.enableChangeClass = true
            } else {
                this.enableChangeClass = false
            }
        }).catch(()=>{this.enableChangeClass = false})
        this.$refs["Loading"].close()
    },
    methods: {
        hanndleUpload() {
            this.$refs.upload.click()
        },
        updateStudent() {
            let okClassCode = false
            for (let i of this.lopList) {
                if (i.code === this.lop) {
                    okClassCode = true
                }
            }
            if (!okClassCode) {
                this.lop = ""
                return
            }
            if (!this.$refs.form.validate()) {
                return
            }

            let studentDetail = {
                avatar: this.imageBase64,
                code: this.maHocSinh,
                fullName: this.tenHocSinh,
                status: this.trangThai,
                phone: this.soDienThoai,
                birthDay: this.ngaySinhDisplay,
                nation: this.danToc,
                permanentAddress: this.diaChiThuongTru,
                socialInsuranceNumber: this.soSoBHXH,
                issuedDate: this.ngayCapDisplay,
                sex: this.gioiTinh,
                deptId: this.khoa,
                trainingSystem: this.heDaoTao,
                email: this.email,
                religion: this.tonGiao,
                homeTown: this.queQuan,
                temporaryAddress: this.diaChiTamTru,
                identityCard: this.soCMND,
                issuedAddress: this.noiCap,
                startDate: this.ngayVaoTruongDisplay,
                electFormat: this.hinhThucTrungTuyen,
                graduationType: this.loaiTotNghiepCapDuoi,
                quanHe: this.quanHe,
                hoTenPhuHuynh: this.hoTenPhuHuynh,
                soDienThoaiPhuHuynh: this.soDienThoaiPhuHuynh,
                quanHeSecond: this.quanHeSecond,
                hoTenPhuHuynhSecond: this.hoTenPhuHuynhSecond,
                soDienThoaiPhuHuynhSecond: this.soDienThoaiPhuHuynhSecond,
                classCode: this.lop,
                year: this.$store.getters["year"]
            }
            AppService.updateStudent(studentDetail ).then(
                (res) => {
                    if (res.data.status === "OK") {
                        this.$refs["ToastMessage"].open(res.data.message, false);
                        this.$router.push({
                            name: "quan-ly-hoc-sinh"
                        });
                    } else {
                        this.$refs["ToastMessage"].open(res.data.message, true);
                    }

                },
            ).catch(() => {
                this.$refs["ToastMessage"].open("Cập nhật thông tin học sinh thất bại", true);
            });
        },
        discard() {
            this.$router.push({
                name: "quan-ly-hoc-sinh"
            });
        },
        deleteClass(classInformation) {
            AppService.deleteClass(classInformation.id).then(() => {
                this.searchResult = this.searchResult.filter((resultElement) => {
                    return classInformation.id !== resultElement.id
                })
                this.$refs["ToastMessage"].open("Xóa học sinh thành công", false);
            }, () => {
                this.$refs["ToastMessage"].open("Xóa học sinh thất bại", false);
            })
        },
        formatDate(date) {
            if (!date) return null

            const [year, month, day] = date.split('-')
            return `${day}-${month}-${year}`
        },
        ngaySinhSave(date) {
            this.$refs.ngaySinhPicker.save(date)
        },
        ngayCapSave(date) {
            this.$refs.ngayCapPicker.save(date)
        },
        ngayVaoTruongSave(date) {
            this.$refs.ngayVaoTruongPicker.save(date)
        },
        Preview_image(e) {
            if(e.target.files[0].size > 5242880) {
                this.$refs["ToastMessage"].open("File ảnh không được quá 5MB", true)
                return
            }
            if(e.target.files[0]['type'].split('/')[0] !== 'image') {
                this.$refs["ToastMessage"].open("File ảnh không đúng định dạng", true)
                return    
            }
            this.image = e.target.files[0]
            const getBase64 = (file) => new Promise(function (resolve, reject) {
                let reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result)
                reader.onerror = (error) => reject('Error: ', error);
            })

            getBase64(this.image).then((data) => {
                this.imageBase64 = data
                this.url = URL.createObjectURL(this.image)
            }, ()=>{this.$refs["ToastMessage"].open("File ảnh lỗi", true);}).catch(()=>{()=>{this.$refs["ToastMessage"].open("File ảnh lỗi", true);}})
            
        },
        async loadLop() {
            if (this.khoi === "" || this.khoa === "") return
            await AppService.getClassByGradeAndYears(this.khoi, this.$store.getters["year"], this.khoa).then((data) => {
                this.lopList = []
                for (let lop of data.data.data) {
                    this.lopList.push({
                        id: lop.id,
                        name: lop.code + " - " + lop.name,
                        code: lop.code
                    })
                }
            }, () => {}).catch(() => {})
        }
    },
    watch: {
        async khoi(newKhoi) {
            if (this.$store.getters["year"] === null) {
                return
            }
            this.loadLop()
        },
        async khoa(newKhoa) {
            if (this.$store.getters["year"] === null) {
                return
            }
            this.loadLop()
        },
        '$store.state.year'() {
            if (this.$store.getters["year"] === null) {
                return
            }
            this.loadLop()
        },
        ngaySinh(val) {
            this.ngaySinhDisplay = this.formatDate(val)
        },
        ngayCap(val) {
            this.ngayCapDisplay = this.formatDate(val)
        },
        ngayVaoTruong(val) {
            this.ngayVaoTruongDisplay = this.formatDate(val)
        },
        menuNgaySinh(val) {
            val && setTimeout(() => (this.activePickerNgaySinh = 'YEAR'))
        },
        menuNgayCap(val) {
            val && setTimeout(() => (this.activePickerNgayCap = 'YEAR'))
        },
        menuNgayVaoTruong(val) {
            val && setTimeout(() => (this.activePickerNgayVaoTruong = 'YEAR'))
        },
    }
};
</script>

<style scoped lang="scss">
</style>
