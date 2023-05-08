<template>
<v-container class="StudentInformation ma-0 pa-0" fluid height="100%">
    <v-row class="fill-height">
        <!-- <v-spacer></v-spacer> -->
        <v-col cols="12" class="ma-1">
            <v-row class="mb-5 mt-0 mr-1">
                <v-btn-toggle v-model="toggle_exclusive" mandatory color="primary">
                    <v-btn @click="$router.push({name: 'ho-so-hoc-sinh-parent' })">Chi tiết hồ sơ</v-btn>
                    <v-btn @click="$router.push({name: 'diem-hoc-tap-parent' })">Điểm học tập</v-btn>
                </v-btn-toggle>
            </v-row>
            <v-expand-transition>
            <v-row v-show="hasData" class="mb-3 mr-1">
                <v-card width="100%">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin lý lịch cá nhân</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="pa-4">
                        <v-col cols="4">
                            <v-list nav dense>
                                <v-list-item class="mr-4">
                                    <v-spacer></v-spacer>
                                    <v-img height=120 max-height="295" max-width="250" contain :src="imageBase64"></v-img>
                                    <v-spacer></v-spacer>
                                </v-list-item>
                            </v-list>

                            <v-divider></v-divider>

                            <v-list nav dense>
                                <v-list-item>
                                    <v-row justify="center" align="center">
                                        <v-col>Mã học sinh (*):</v-col>
                                        <v-col>
                                            {{ maHocSinh }}
                                        </v-col>
                                    </v-row>
                                </v-list-item>
                                <v-list-item>
                                    <v-row justify="center" align="center">
                                        <v-col>Họ và tên (*):</v-col>
                                        <v-col>
                                            {{ tenHocSinh }}
                                        </v-col>
                                    </v-row>
                                </v-list-item>
                                <v-list-item>
                                    <v-row justify="center" align="center">
                                        <v-col>Trạng thái (*):</v-col>
                                        <v-col>
                                            {{ trangThai }}
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
                                                    {{khoi}}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Tên lớp:</v-col>
                                                <v-col>
                                                    {{ lop }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Số điện thoại:</v-col>
                                                <v-col>
                                                    {{ soDienThoai }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Ngày sinh:</v-col>
                                                <v-col>
                                                    {{ ngaySinhDisplay }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Dân tộc:</v-col>
                                                <v-col>
                                                    {{ danToc }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Địa chỉ thường trú:</v-col>
                                                <v-col>
                                                    {{ diaChiThuongTru }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Số sổ BHXH:</v-col>
                                                <v-col>
                                                    {{ soSoBHXH }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Ngày cấp CMND/TCC:</v-col>
                                                <v-col>
                                                    {{ ngayCapDisplay }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Giới tính:</v-col>
                                                <v-col>
                                                    {{ gioiTinh }}
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
                                                    {{ khoa }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Hệ đào tạo (*):</v-col>
                                                <v-col>
                                                    {{ heDaoTao }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Email:</v-col>
                                                <v-col>
                                                    {{ email }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Tôn giáo:</v-col>
                                                <v-col>
                                                    {{ tonGiao }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Quê quán:</v-col>
                                                <v-col>
                                                    {{ queQuan }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Địa chỉ tạm trú:</v-col>
                                                <v-col>
                                                    {{ diaChiTamTru }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Số CMND/TCC:</v-col>
                                                <v-col>
                                                    {{ soCMND }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                        <v-list-item>
                                            <v-row justify="center" align="center">
                                                <v-col>Nơi cấp CMND/TCC:</v-col>
                                                <v-col>
                                                    {{ noiCap }}
                                                </v-col>
                                            </v-row>
                                        </v-list-item>
                                    </v-list>
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                </v-card>
                <v-card width="100%" class="mt-5">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin vào trường</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="mx-2 py-3">
                        <v-col>
                            <v-row>
                                <v-col>Ngày vào trường:</v-col>
                                <v-col>
                                    {{ ngayVaoTruongDisplay }}
                                </v-col>
                            </v-row>
                        </v-col>
                        <v-col>
                            <v-row>
                                <v-col>Hình thức trúng tuyển:</v-col>
                                <v-col>
                                    {{ hinhThucTrungTuyen }}
                                </v-col>
                            </v-row>
                        </v-col>
                        <v-col>
                            <v-row>
                                <v-col>Loại tốt nghiệp cấp dưới:</v-col>
                                <v-col>
                                    {{ loaiTotNghiepCapDuoi }}
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                </v-card>
                <v-card width="100%" class="mt-5">
                    <v-toolbar dense dark class="font-weight-bold" color="primary lighten-1">
                        <v-toolbar-title class="text-center">Thông tin liên lạc phụ huynh học sinh</v-toolbar-title>

                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-row class="mx-2 mb-2">
                        <v-col class="mt-2">
                            <v-row>
                                <v-col>Quan hệ:</v-col>
                                <v-col>
                                    {{ quanHe }}
                                </v-col>
                            </v-row>
                        </v-col>
                        <v-col class="mt-2">
                            <v-row>
                                <v-col>Họ tên phụ huynh:</v-col>
                                <v-col>
                                    {{ hoTenPhuHuynh }}
                                </v-col>
                            </v-row>
                        </v-col>
                        <v-col class="mt-2">
                            <v-row>
                                <v-col>Số điện thoại:</v-col>
                                <v-col>
                                    {{ soDienThoaiPhuHuynh }}
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                    <v-row class="mx-2 mb-2">
                        <v-col class="mt-2">
                            <v-row>
                                <v-col>Quan hệ:</v-col>
                                <v-col>
                                    {{ quanHeSecond }}
                                </v-col>
                            </v-row>
                        </v-col>
                        <v-col class="mt-2">
                            <v-row>
                                <v-col>Họ tên phụ huynh:</v-col>
                                <v-col>
                                    {{ hoTenPhuHuynhSecond }}
                                </v-col>
                            </v-row>
                        </v-col>
                        <v-col class="mt-2">
                            <v-row>
                                <v-col>Số điện thoại:</v-col>
                                <v-col>
                                    {{ soDienThoaiPhuHuynhSecond }}
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                </v-card>
            </v-row>
            </v-expand-transition>
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
    name: "StudentInformation",
    components: {
        ToastMessage,
        Loading
    },
    created() {},
    data: () => ({
        hasData: false,
        studentCodeSearch: "",
        valid: true,
        image: "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png",
        imageBase64: "1111",
        url: "",
        maHocSinh: "",
        tenHocSinh: "",
        khoi: "",
        khoa: "",
        trangThai: 0,
        lop: "",
        heDaoTao: 1,
        gioiTinh: 0,
        noiCap: "",
        ngaySinhDisplay: "",
        ngayCap: "",
        ngayCapDisplay: "",
        ngayVaoTruongDisplay: "",
        email: "",
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
        trangThaiMap: {
            0: "Đang học",
            1: "Bảo lưu",
            2: "Đã thôi học",
            3: "Đã chuyển trường",
        },
        heDaoTaoMap: {
            1: "Chính quy",
            2: "Tại Chức",
            3: "Vừa học vừa làm",
            4: "Liên thông",
            5: "Đào tạo nghề",
            6: "Hợp tác đào tạo",
        },
        gioiTinhMap: {
            0: "Nam",
            1: "Nữ",
        },
        hinhThucTrungTuyenMap: {
            1: "Thi tuyển",
            2: "Xét tuyển"
        },
        loaiTotNghiepCapDuoiMap: {
            1: "Giỏi",
            2: "Khá",
            3: "Trung bình",
        },
        quanHeMap: {
            0: "Bố",
            1: "Mẹ",
            2: "Ông Bà",
            3: "Anh Chị",
            4: "Cô chú",
            5: "Người giám hộ",
        },
        toggle_exclusive: 0
    }),
    computed: {},
    async mounted() {
        this.hasData = false
        this.$refs["Loading"].open()
        if (this.$store.getters["user"].username === undefined) {
          await this.$store.dispatch("getCurrentUser");
        }
        this.studentCodeSearch = this.$store.getters["user"].username
        this.loadData()
        this.$refs["Loading"].close()
    },
    methods: {
        loadData() {
            this.$refs["Loading"].open()
            if(this.$store.getters["year"] === null)  return
            AppService.getStudentDetailInformationByYear(this.studentCodeSearch, this.$store.getters["year"]).then((data) => {
                if (data.data.data.length == 0) {
                    this.$refs["ToastMessage"].open("Lỗi khi lấy thông tin học sinh !", true);
                    this.$refs["Loading"].close()
                    this.hasData = false
                    return
                }
                let parent_primary = data.data.data[0];
                // let parent_second = null
                // if (data.data.data.length == 2) {
                //   parent_second = data.data.data[1]
                // }
                this.imageBase64 = parent_primary.avatar
                if (this.imageBase64 === "") {
                    this.imageBase64 = this.image
                }
                this.maHocSinh = parent_primary.code
                this.tenHocSinh = parent_primary.full_name
                this.khoi = parent_primary.grade_level
                this.khoa = parent_primary.department_name
                this.trangThai = this.trangThaiMap[parent_primary.status]
                this.lop = parent_primary.class_name
                this.heDaoTao = this.heDaoTaoMap[parent_primary.training_system]
                this.gioiTinh = this.gioiTinhMap[parent_primary.sex]
                this.noiCap = parent_primary.issued_address
                this.ngaySinhDisplay = this.formatDate(parent_primary.birth_day)
                this.ngayCapDisplay = this.formatDate(parent_primary.issued_date)
                this.ngayVaoTruongDisplay = this.formatDate(parent_primary.start_date)
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
                this.quanHe = this.quanHeMap[parent_primary.relationship]
                this.hoTenPhuHuynhSecond = parent_primary.parent_name_second
                this.soDienThoaiPhuHuynhSecond = parent_primary.parent_phone_second
                this.quanHeSecond = this.quanHeMap[parent_primary.relationship_second]
                this.hinhThucTrungTuyen = this.hinhThucTrungTuyenMap[parent_primary.elect_format]
                this.loaiTotNghiepCapDuoi = this.loaiTotNghiepCapDuoiMap[parent_primary.graduation_type]
                this.hasData = true
                this.$refs["Loading"].close()
            }).catch(() => {
                this.hasData = false
                this.$refs["Loading"].close()
            })
        },
        formatDate(date) {
            if (!date) return null
            date = date.slice(0, date.indexOf(" "))

            const [year, month, day] = date.split('-')
            return `${day}-${month}-${year}`
        }

    },
    watch: {
        '$store.state.year'() {
            this.loadData()
        }
    }
};
</script>

<style scoped lang="scss">
</style>
