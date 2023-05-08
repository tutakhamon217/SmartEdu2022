<template>
<v-app>
    <div id="app">
        <v-app-bar v-if="!hiddenHeader" dark clipped-left app color="primary lighten-1">
            <v-icon large class="mr-4"> mdi-account-school </v-icon>
            <v-toolbar-title>SmartEdu</v-toolbar-title>
            <v-divider class="mx-4" inset vertical></v-divider>
            <v-app-bar-nav-icon @click="showSideBar = !showSideBar"></v-app-bar-nav-icon>
            <v-spacer></v-spacer>
            <v-toolbar-items class="hidden-sm-and-down">
                <v-divider inset vertical></v-divider>
                <v-menu transition="slide-y-transition" :close-on-content-click="false" bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn text v-bind="attrs" v-on="on">
                            <v-icon class="mx-4" color="white">mdi-account</v-icon>
                            <v-col style="font-family: sans-serif; font-size: 0.8rem">
                                <v-row>Xin chào,</v-row>
                                <v-row class="font-weight-bold mt-4">{{fullName}}</v-row>
                            </v-col>
                        </v-btn>
                    </template>
                    <v-card>
                        <v-list>
                            <v-list-item>
                                <v-list-item-avatar>
                                    <img :src="avatar" alt="Avatar">

                                </v-list-item-avatar>
                                <v-list-item-content>
                                    <v-list-item-title class="font-weight-bold">{{this.fullName}}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list>

                        <v-divider v-if="showSchoolInfor"></v-divider>
                        <v-list v-if="showSchoolInfor">
                            <v-list-item @click="$router.push({ name : 'thong-tin-truong-hoc'})">
                                <v-list-item-icon>
                                    <v-icon>mdi-school</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title>Thông tin trường học</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list>
                        <v-divider></v-divider>

                        <v-list>
                            <v-list-item @click="$router.push({name: 'thong-tin-ca-nhan-giao-vien'})" v-if="showInforTeacherWithRole">
                                <v-list-item-icon>
                                    <v-icon>mdi-account-details</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title>Thông tin cá nhân</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                            <v-list-item>
                                <v-list-item-icon>
                                    <v-icon>mdi-email</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title>{{ this.email }}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                            <v-list-item>
                                <v-list-item-icon>
                                    <v-icon>mdi-account</v-icon>
                                </v-list-item-icon>

                                <v-list-item-content>
                                    <v-list-item-title>{{ this.userName }}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                            <v-list-item>
                                <v-list-item-icon>
                                    <v-icon>mdi-phone</v-icon>
                                </v-list-item-icon>

                                <v-list-item-content>
                                    <v-list-item-title>{{ this.phone }}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list>
                        <v-divider></v-divider>
                        <v-list dense>
                            <v-list-item-group>
                                <v-list-item @click="changePassword()">
                                    <v-list-item-icon>
                                        <v-icon>mdi-key-chain-variant</v-icon>
                                    </v-list-item-icon>
                                    <v-list-item-content>
                                        <v-list-item-title>Đổi mật khẩu</v-list-item-title>
                                    </v-list-item-content>
                                </v-list-item>
                                <v-list-item @click="logout()">
                                    <v-list-item-icon>
                                        <v-icon>mdi-logout</v-icon>
                                    </v-list-item-icon>
                                    <v-list-item-content>
                                        <v-list-item-title>Đăng xuất</v-list-item-title>
                                    </v-list-item-content>
                                </v-list-item>
                            </v-list-item-group>
                        </v-list>

                    </v-card>
                </v-menu>
            </v-toolbar-items>
        </v-app-bar>

        <v-navigation-drawer v-if="!hiddenSideBar" app clipped light class="mr-4 ml-0" v-model="showSideBar" :mini-variant="!showSideBar" bottom>
            <v-list nav dense class="side_bar">
                <v-treeview :items="sideBarItems" activatable item-key="name" open-on-click expand-icon="" transition :active="currentActive" :open="currentOpen" color="primary" class="pf-0 ml-0">
                    <template class="ml-0 pl-0" v-slot:label="{ item }">
                        <v-list-item class="pl-0 ml-0" v-if="item.type === 'parent' && item.children.length > 0" link :key="item.path">
                            <v-list-item-icon class="pl-0 ml-0">
                                <v-icon color="primary lighten-1">{{ item.icon }}</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title class="font-weight-bold">
                                {{ item.title }}
                            </v-list-item-title>
                        </v-list-item>
                        <v-list-item class="ml-6" v-if="item.type === 'child'" link :key="item.path" @click="redirect(item.name)">
                            <v-list-item-title class="font-weight-bold">
                                {{ item.title }}
                            </v-list-item-title>
                        </v-list-item>
                        <v-list-item  class="pl-0 ml-0" v-if="item.type === 'parent' && item.children.length === 0" link :key="item.path" @click="redirect(item.name)">
                            <v-list-item-icon class="pl-0 ml-0">
                                <v-icon color="primary lighten-1">{{ item.icon }}</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title class="font-weight-bold">
                                {{ item.title }}
                            </v-list-item-title>
                        </v-list-item>
                        <v-divider v-if="item.type === 'parent'" light></v-divider>
                    </template>
                </v-treeview>
            </v-list>
        </v-navigation-drawer>
    </div>
    <v-main>
        <v-toolbar v-if="!hiddenHeader">
            <v-breadcrumbs :items="itemBreadCrumb">
                <template v-slot:divider>
                    <v-icon>mdi-chevron-right</v-icon>
                </template>
            </v-breadcrumbs>
            <v-spacer></v-spacer>
            <DropDownYear :hiddenDropDownYear="hiddenDropDownYear" :data="years" label="Năm học" />
        </v-toolbar>
        <v-container fluid style="height: 100%">
            <router-view fluid style="height: 100%"></router-view>
        </v-container>
    </v-main>
    <Loading ref="Loading"> </Loading>
    <toast-message ref="toastMessage" />
    <ChangePassword ref="ChangePassword"> </ChangePassword>
</v-app>
</template>

<script>
import DropDownYear from "@/components/DropDownYear";
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage.vue";
import ChangePassword from "@/views/Dialogs/ChangePassword.vue";
export default {
    name: "App",
    components: {
        DropDownYear,
        Loading,
        ToastMessage,
        ChangePassword
    },
    data() {
        return {
            showInforTeacherWithRole: false,
            showSchoolInfor: false,
            userName: "",
            fullName: "",
            phone: "",
            email: "",
            avatar: "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png",
            isLogined: true,
            showSideBar: true,
            currentOpen: ["nha-truong"],
            currentActive: [],
            years: [],
            itemBreadCrumb: [],
            hiddenSideBar: false,
            hiddenHeader: false,
            listHiddenSideBarAndHeader: ["login"],
            listHiddenDropDownYear: [
                "cau-hinh-nam-hoc",
                "mon-hoc-thuoc-truong",
                "cap-nhat-giao-vien",
                "thong-tin-giao-vien",
                "quan-ly-giao-vien",
                "them-moi-giao-vien",
                "cap-nhat-giao-vien",
                "diem-hoc-tap",
                "cau-hinh-truong-hoc",
                "diem-hoc-tap-parent",
                "ket-chuyen",
                "login",
                "thong-tin-truong-hoc",
                "thoi-khoa-bieu-lich-day",
                "cap-nhat-hoc-sinh",
                "thong-tin-ca-nhan-giao-vien"
            ],
            hiddenDropDownYear: true,
            allSideBarItems: [{
                    title: "Nhà trường",
                    path: "nha-truong",
                    icon: "mdi-town-hall",
                    type: "parent",
                    name: "nha-truong",
                    children: [{
                            title: "Cấu hình trường học",
                            path: "cau-hinh-truong-hoc",
                            type: "child",
                            name: "cau-hinh-truong-hoc",
                        },
                        {
                            title: "Cấu hình năm học",
                            path: "cau-hinh-nam-hoc",
                            type: "child",
                            name: "cau-hinh-nam-hoc",
                        },
                        {
                            title: "Môn học thuộc trường",
                            path: "/mon-hoc-thuoc-truong",
                            type: "child",
                            name: "mon-hoc-thuoc-truong",
                        },
                        {
                            title: "Quản lý lớp học",
                            path: "cau-hinh-lop-hoc",
                            type: "child",
                            name: "cau-hinh-lop-hoc",
                        },
                        {
                            title: "Khai báo môn học cho lớp",
                            path: "/mon-hoc-cho-lop",
                            type: "child",
                            name: "mon-hoc-cho-lop",
                        },
                        {
                            title: "Xếp thời khóa biểu",
                            path: "/xep-thoi-khoa-bieu",
                            type: "child",
                            name: "xep-thoi-khoa-bieu",
                        },
                    ],
                },
                {
                    title: "Cấu hình hệ thống",
                    path: "",
                    icon: "mdi-application-cog",
                    type: "parent",
                    name: "cau-hinh-he-thong",
                    children: [{
                        title: "Cấu hình bảng điểm",
                        path: "/bang-diem",
                        type: "child",
                        name: "bang-diem",
                    }, ],
                },
                {
                    title: "Giáo viên ",
                    path: "/giao-vien",
                    icon: "mdi-account-box-multiple",
                    type: "parent",
                    name: "giao-vien",
                    children: [{
                            title: "Quản lý giáo viên",
                            path: "/giao-vien/quan-ly",
                            type: "child",
                            name: "quan-ly-giao-vien",
                        },
                        {
                            title: "Phân công giảng dạy",
                            path: "/giao-vien/phan-cong-giang-day",
                            type: "child",
                            name: "phan-cong-giang-day",
                        },
                        {
                            title: "Thời khóa biểu lịch dạy",
                            path: "/giao-vien/thoi-khoa-bieu-lich-day",
                            type: "child",
                            name: "thoi-khoa-bieu-lich-day"
                        },
                    ],
                },
                {
                    title: "Học sinh ",
                    path: "/hoc-sinh",
                    icon: "mdi-account-settings",
                    type: "parent",
                    name: "hoc-sinh",
                    children: [{
                            title: "Quản lý học sinh",
                            path: "/quan-ly-hoc-sinh",
                            type: "child",
                            name: "quan-ly-hoc-sinh",
                        },
                        {
                            title: "Điểm danh chuyên cần",
                            path: "/diem-danh-chuyen-can",
                            type: "child",
                            name: "diem-danh-chuyen-can",
                        },
                        {
                            title: "Sổ điểm",
                            path: "/hoc-sinh/so-diem",
                            type: "child",
                            name: "so-diem",
                        },
                        {
                            title: "Đánh giá học lực",
                            path: "danh-gia-hoc-luc",
                            type: "child",
                            name: "danh-gia-hoc-luc"
                        },
                        {
                            title: "Đánh giá hạnh kiểm",
                            path: "/hoc-sinh/danh-gia-hanh-kiem",
                            type: "child",
                            name: "danh-gia-hanh-kiem",
                        },
                        {
                            title: "Kết chuyển học sinh",
                            path: "/hoc-sinh/ket-chuyen",
                            type: "child",
                            name: "ket-chuyen",
                        },
                    ],
                },
                {
                    title: "Báo cáo kết quả học tập",
                    path: "/bao-cao-ket-qua-hoc-tap",
                    icon: "mdi-file-document-edit-outline",
                    type: "parent",
                    name: "bao-cao-ket-qua-hoc-tap",
                    children: [{
                            title: "Toàn trường",
                            path: "/bao-cao-ket-qua-hoc-tap/toan-truong",
                            type: "child",
                            name: "toan-truong",
                        },
                        {
                            title: "Từng lớp",
                            path: "/bao-cao-ket-qua-hoc-tap/tung-lop",
                            type: "child",
                            name: "tung-lop",
                        },
                    ],
                },
                {
                    title: "Thông tin chung",
                    path: "phu-huynh-hoc-sinh",
                    icon: "mdi-account-box",
                    type: "parent",
                    name: "phu-huynh-hoc-sinh",
                    children: [{
                            title: "Hồ sơ",
                            path: "/phu-huynh-hoc-sinh/ho-so",
                            type: "child",
                            name: "ho-so-hoc-sinh-parent",
                        },
                        {
                            title: "Điểm học tập",
                            path: "/phu-huynh-hoc-sinh/diem-hoc-tap",
                            type: "child",
                            name: "diem-hoc-tap-parent",
                        },
                    ],
                },
                {
                    title: "Thời khóa biểu",
                    path: "thoi-khoa-bieu-hoc-sinh",
                    icon: "mdi-calendar-multiselect",
                    type: "parent",
                    name: "thoi-khoa-bieu-parent",
                    children: [

                    ],
                },
                {
                    title: "Điểm danh",
                    path: "diem-danh-hoc-sinh",
                    icon: "mdi-checkbox-marked-outline",
                    type: "parent",
                    name: "diem-danh-hoc-sinh",
                    children: [

                    ],
                },
            ],
            sideBarItems: [

            ]
        };
    },
    created() {},
    async mounted() {
        if (this.listHiddenSideBarAndHeader.includes(this.$route.name)) {
            this.hiddenSideBar = true;
            this.hiddenHeader = true;
        } else {
            this.hiddenSideBar = false;
            this.hiddenHeader = false;
        }
        this.currentOpen = [];
        if (this.$route.name != "login") {
            await this.$store.dispatch("getCurrentUser");
            this.userName = this.$store.getters["user"].username
            this.fullName = this.$store.getters["user"].fullName
            this.phone = this.$store.getters["user"].phone
            this.email = this.$store.getters["user"].email
            this.avatar = this.$store.getters["user"].avatar
            if (this.avatar === "" || this.avatar === null || this.avatar === undefined) {
                this.avatar = "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png"
            }
            this.sideBarItems = []
            let pageAllowed = []
            for(let role in this.$store.getters["user"].roles) {
              let tmpRole = this.$store.getters["user"].roles[role]
              pageAllowed = pageAllowed.concat(this.$store.getters["pageAllow"][tmpRole])
            }
            for(let parent of this.allSideBarItems) {
              let tempParent = parent
              if (tempParent.children.length === 0 && pageAllowed.includes(parent.name)) {
                    this.sideBarItems.push(tempParent)
              } else {
                tempParent.children = tempParent.children.filter((children)=>{return pageAllowed.includes(children.name)})
                if (tempParent.children.length > 0) {
                    this.sideBarItems.push(tempParent)
                }
              }
            }
            
            if (this.$store.getters["user"].roles.length == 1 && this.$store.getters["user"].roles[0] == "ROLE_USER") {
              //role user (phụ huynh và học sinh) 
              await this.getSchoolYearOfHistoryStudent()
            } else {
              await this.getAllYears().catch(()=>{this.$refs['toastMessage'].open("Không có năm học nào", true)});
            //   await this.getObjCurrentYear();
            }
        }
        // this.getItemBreadCrumb();
    },

    methods: {
        logout() {
            localStorage.removeItem("school_token")
            this.redirect("login")
        },
        redirect(name) {
            if(this.$route.name === name) {
                return
            }
            this.$router.push({
                name: name
            });
        },
        getAllYears() {
            return AppService.getAllYear().then((result) => {
                this.years = result.data.data;
            }).catch(()=>{});
        },
        getItemBreadCrumb() {
            return setTimeout(() => {
                this.itemBreadCrumb = []
                let links = this.$route.matched;
                let href = "";
                links.forEach((element, index) => {
                    href += element.path;
                    let obj = {};
                    obj.text = element.meta.text;
                    obj.href = href;
                    obj.disabled = index + 1 == links.length ? true : false;
                    this.itemBreadCrumb.push(obj);
                });
            }, 0);
        },
        getSchoolYearOfHistoryStudent() {
            return AppService.getSchoolYearOfHistoryStudent(this.$store.getters['user'].username)
                .then((result) => {
                    this.years = result.data.data;
                    // let currentYear = new Date().getFullYear();
                    // let schoolYear = currentYear + "-" + (currentYear + 1);
                    // let choosed = schoolYear;
                    // if (this.years.includes(schoolYear)) {
                    //     this.$store.dispatch("updateYear", choosed);
                    // } else {
                    //     this.$store.dispatch("updateYear", this.years[0]);
                    // }
                })
                .catch((res) => {
                    this.$refs['toastMessage'].open(res.message, true)
                })
        },
        getObjCurrentYear() {
            return AppService.getObjCurrentYear()
                .then((res) => {
                    if (res.data.status === 'OK') {
                        this.$store.dispatch("updateCurrentYear", res.data.data.years);
                        this.$store.dispatch("updateYear", res.data.data.years);
                        this.$store.dispatch("updateCurrentSemester", res.data.data.semester)
                    } else {
                        this.$store.dispatch("updateYear", this.years[0]);
                    }
                })
                .catch(() => {
                    this.$store.dispatch("updateYear", this.years[0]);
                })
        },
        changePassword() {
            this.$refs['ChangePassword'].open().then(() => {
                this.$refs['toastMessage'].open("Đổi mật khẩu thành công", false)
            })
        }
    },
    watch: {
        '$store.state.user'() {
            // $store.state.user !== null && 
            if(this.$store.state.user === null) {
                this.showSchoolInfor = false
                this.showInforTeacherWithRole = false
            }
            else {
                if(this.$store.state.user.roles.includes('ROLE_ADMIN')) {this.showSchoolInfor = true}
                else {this.showSchoolInfor = false}
                if(this.$store.state.user.roles.includes('ROLE_GVBM') || this.$store.state.user.roles.includes('ROLE_GVCN')){
                    this.showInforTeacherWithRole = true
                }else{
                    this.showInforTeacherWithRole = false
                }
            }
        },
        async "$route.name"() {
            this.currentOpen = []
            this.currentActive = [this.$route.name];
            for (let pa of this.sideBarItems) {
                let isOpen = false;
                if (pa.children === undefined) {
                    continue;
                }
                for (let chi of pa.children) {
                    if (chi.name === this.currentActive[0]) {
                        isOpen = true;
                        break;
                    }
                }
                if (isOpen) {
                    this.currentOpen.push(pa.name);
                }
            }
            if (this.listHiddenSideBarAndHeader.includes(this.$route.name)) {
                this.hiddenSideBar = true;
                this.hiddenHeader = true;
            } else {
                this.hiddenSideBar = false;
                this.hiddenHeader = false;
            }
            if (this.listHiddenDropDownYear.includes(this.$route.name)) {
                this.hiddenDropDownYear = true;
            } else {
                this.hiddenDropDownYear = false;
            }
            this.getItemBreadCrumb()
        },
    },
};
</script>

<style lang="scss">
#app {
    font-family: sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    // text-align: center;
}

#app .side_bar .v-treeview-node__root {
    flex-direction: row-reverse !important;
}

#app .side_bar .v-treeview-node__level {
    display: none !important;
}
</style>
