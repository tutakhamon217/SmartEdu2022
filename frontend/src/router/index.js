import Vue from 'vue'
import VueRouter from 'vue-router'
import axios from "axios";
import $http from "@/plugins/axios";
Vue.use(VueRouter)

export const routes = [
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/nha-truong',
        name: 'nha-truong',
        meta: {
            text: 'Nhà trường',
        },
        component: () => import('@/views/School.vue'),
        children: [
            {
                path: 'cau-hinh-nam-hoc',
                name: 'cau-hinh-nam-hoc',
                meta: {
                    text: 'Cấu hình năm học',
                },
                component: () => import('@/views/SchoolYearInformation.vue')
            },
            {
                path: 'cau-hinh-truong-hoc',
                name: 'cau-hinh-truong-hoc',
                meta: {
                    text: 'Cấu hình trường học',
                },
                component: () => import('@/views/SchoolInformation.vue')
            },
            {
                path: 'mon-hoc-thuoc-truong',
                name: 'mon-hoc-thuoc-truong',
                meta: {
                    text: 'Môn học thuộc trường',
                },
                component: () => import('@/views/SchoolSubject.vue')
            },
            {
                path: 'mon-hoc-cho-lop',
                name: 'mon-hoc-cho-lop',
                meta: {
                    text: 'Môn học cho lớp'
                },
                component: () => import('@/views/SubjectForClass.vue')
            },
            {
                path: 'cau-hinh-lop-hoc',
                name: 'cau-hinh-lop-hoc',
                meta: {
                    text: 'Quản lý lớp học'
                },
                component: () => import('@/views/ClassInformation.vue')
            },
            {
                path: "xep-thoi-khoa-bieu",
                name: "xep-thoi-khoa-bieu",
                meta: {
                    text: "Xếp thời khóa biểu"
                },
                component: () => import('@/views/Timetable.vue')
            }
        ]
    },
    {
        path: '/cau-hinh-he-thong',
        name: 'cau-hinh-he-thong',
        meta: {
            text: 'Cấu hình hệ thống',
        },
        component: () => import('@/views/SystemConfiguration.vue'),
        children: [
            {
                path: 'bang-diem',
                name: 'bang-diem',
                meta: {
                    text: 'Cấu hình bảng điểm',
                },
                component: () => import('@/views/ConfigScoreboard.vue')
            }
        ]
    },
    {
        path: '/giao-vien',
        name: 'giao-vien',
        meta: {
            text: 'Giáo viên',
        },
        component: () => import('@/views/teacher/Teacher.vue'),
        children: [
            {
                path: 'quan-ly',
                name: 'quan-ly-giao-vien',
                meta: {
                    text: 'Quản lý cán bộ giáo viên',
                },
                component: () => import('@/views/teacher/ManageTeacher.vue'),
            },
            {
                path: 'them-moi',
                name: 'them-moi-giao-vien',
                meta: {
                    text: 'Thêm mới cán bộ giáo viên',
                },
                component: () => import('@/views/teacher/UpdateTeacher.vue')
            },
            {
                path: 'cap-nhat/:id',
                name: 'cap-nhat-giao-vien',
                meta: {
                    text: 'Cập nhật cán bộ giáo viên',
                },
                component: () => import('@/views/teacher/UpdateTeacher.vue')
            },
            {
                path: 'thong-tin/:id',
                name: 'thong-tin-giao-vien',
                meta: {
                    text: 'Hồ sơ cán bộ giáo viên',
                },
                component: () => import('@/views/teacher/InforTeacher.vue')
            },
            {
                path: 'phan-cong-giang-day',
                name: 'phan-cong-giang-day',
                meta: {
                    text: 'Phân công giảng dạy',
                },
                component: () => import('@/views/TeachingAssignment.vue')
            },
            {
                path: 'thoi-khoa-bieu-lich-day',
                name: 'thoi-khoa-bieu-lich-day',
                meta: {
                    text: 'Thời khóa biểu lịch dạy',
                },
                component: () => import('@/views/TeachingTimetable.vue')
            },
        ]
    },
    {
        path: '/hoc-sinh',
        name: 'hoc-sinh',
        meta: {
            text: 'Học sinh'
        },
        component: () => import('@/views/student/Student.vue'),
        children: [
            {
                path: '/danh-gia-hoc-luc',
                name: 'danh-gia-hoc-luc',
                meta: {
                    text: 'Đánh giá học lực ',
                },
                component: () => import('@/views/StudentLearningRate.vue'),
            },
            {
                path: 'danh-gia-hanh-kiem',
                name: 'danh-gia-hanh-kiem',
                meta: {
                    text: 'Đánh giá hạnh kiểm',
                },
                component: () => import('@/views/student/ConductAssessment.vue'),
            },
            {
                path: 'diem-danh-chuyen-can',
                name: 'diem-danh-chuyen-can',
                meta: {
                    text: 'Điểm danh chuyên cần',
                },
                component: () => import('@/views/student/Attendance.vue'),
            },        
            {
                path: 'so-diem',
                name: 'so-diem',
                meta: {
                    text: 'Sổ điểm',
                },
                component: () => import('@/views/GradeBook.vue'),
            },
            {
                path: 'ho-so-hoc-sinh',
                name: 'ho-so-hoc-sinh',
                meta: {
                    text: 'Hồ sơ học sinh',
                },
                component: () => import('@/views/StudentInformation.vue'),
            },
            {
                path: 'quan-ly-hoc-sinh',
                name: 'quan-ly-hoc-sinh',
                meta: {
                    text: 'Quản lý học sinh',
                },
                component: () => import('@/views/StudentManagement.vue'),
            },
            {
                path: 'tao-hoc-sinh',
                name: 'tao-hoc-sinh',
                meta: {
                    text: 'Tạo mới học sinh',
                },
                component: () => import('@/views/StudentCreate.vue'),
            },
            {
                path: 'diem-hoc-tap',
                name: 'diem-hoc-tap',
                meta: {
                    text: 'Điểm học tập',
                },
                component: () => import('@/views/student/IndividualScore.vue'),
            },
            {
                path: 'ket-chuyen',
                name: 'ket-chuyen',
                meta: {
                    text: 'Kết chuyển',
                },
                component: () => import('@/views/student/TransferStudent.vue'),
            },
        ]
    },
    {
        path: '/thong-tin-ca-nhan-giao-vien',
        name: 'thong-tin-ca-nhan-giao-vien',
        meta: {
            text: 'Thông tin cá nhân giáo viên'
        },
        component: () => import('@/views/teacher/InforTeacherWithRole.vue'),
    },
    // {
    //     path: '',
    //     name: '',
    //     meta: {
    //         text: '',
    //     },
    //     component: () => import('@/views/StudentManagement.vue'),
    //     children: [
    //         {
    //             path: 'tao-hoc-sinh',
    //             name: 'tao-hoc-sinh',
    //             meta: {
    //                 text: 'Tạo mới học sinh',
    //             },
    //             component: () => import('@/views/StudentCreate.vue')
    //         }
    //     ]
    // },
    // {
    //     path: '/tao-hoc-sinh',
    //     name: 'tao-hoc-sinh',
    //     meta: {
    //         text: 'Tạo mới học sinh',
    //     },
    //     component: () => import('@/views/StudentCreate.vue'),
    // },
    {
        path: '/cap-nhat-hoc-sinh',
        name: 'cap-nhat-hoc-sinh',
        meta: {
            text: 'Cập nhật học sinh',
        },
        component: () => import('@/views/StudentUpdateInformation.vue'),
    },
    {
        path: '/bao-cao-ket-qua-hoc-tap',
        name: 'bao-cao-ket-qua-hoc-tap',
        meta: {
            text: 'Báo cáo kết quả học tập',
        },
        component: () => import('@/views/ReportCompetition.vue'),
        children: [
            {
                path: 'toan-truong',
                name: 'toan-truong',
                meta: {
                    text: 'Toàn trường',
                },
                component: () => import('@/views/ReportTheResultsOfWholeSchoolCompetition.vue'),
            },
            {
                path: 'tung-lop',
                name: 'tung-lop',
                meta: {
                    text: 'Từng lớp',
                },
                component: () => import('@/views/ReportTheResultsOfEachClassCompetition.vue'),
            },
        ]
    },
    {
        path: '/phu-huynh-hoc-sinh',
        name: 'phu-huynh-hoc-sinh',
        meta: {
            text: 'Thông tin chung'
        },
        component: () => import('@/views/ParentSide.vue'),
        children: [
            {
                path: 'ho-so',
                name: 'ho-so-hoc-sinh-parent',
                meta: {
                    text: 'Hồ sơ'
                },
                component: () => import('@/views/StudentInformationParentSide.vue')
            },
            {
                path: 'diem-hoc-tap',
                name: 'diem-hoc-tap-parent',
                meta: {
                    text: 'Điểm học tập',
                },
                component: () => import('@/views/IndividualScoreSideParent.vue'),
            }
        ]
    },
    {
        path: 'thoi-khoa-bieu',
        name: 'thoi-khoa-bieu-parent',
        meta: {
            text: "Thời khóa biểu"
        },
        component: () => import('@/views/student/TimeTableSiteParent.vue')
    },
    // {
    //     path: '/danh-gia-hoc-luc',
    //     name: 'danh-gia-hoc-luc',
    //     meta: {
    //         text: 'Đánh giá học lực ',
    //     },
    //     component: () => import('@/views/StudentLearningRate.vue'),
    // }
    // {
    //     path: '/danh-gia-hoc-luc',
    //     name: 'danh-gia-hoc-luc',
    //     meta: {
    //         text: 'Đánh giá học lực ',
    //     },
    //     component: () => import('@/views/StudentLearningRate.vue'),
    // },
    {
        path: '/thong-tin-truong-hoc',
        name: 'thong-tin-truong-hoc',
        meta: {
            text: 'Thông tin trường học',
        },
        component: () => import('@/views/SchoolInformationUpdate.vue'),
    },
    {
        path: '/lich-su-diem-danh',
        name: 'diem-danh-hoc-sinh',
        meta: {
            text: 'Điểm danh',
        },
        component: () => import('@/views/student/AttendanceSiteParent.vue'),
    },
]
// StudentUpdateInformation
const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

const pageAllow = {
    "ROLE_ADMIN": [
        "cau-hinh-truong-hoc",
        "cau-hinh-nam-hoc",
        "mon-hoc-thuoc-truong",
        "cau-hinh-lop-hoc",
        "mon-hoc-cho-lop",
        "xep-thoi-khoa-bieu",

        "bang-diem",

        "quan-ly-giao-vien",
        "them-moi-giao-vien",
        "cap-nhat-giao-vien",
        "thong-tin-giao-vien",
        "phan-cong-giang-day",
        "thoi-khoa-bieu-lich-day",

        "quan-ly-hoc-sinh",
        "diem-danh-chuyen-can",
        "so-diem",
        "danh-gia-hoc-luc",
        "danh-gia-hanh-kiem",
        "cap-nhat-hoc-sinh",
        "tao-hoc-sinh",
        "diem-hoc-tap",
        "ho-so-hoc-sinh",
        "ket-chuyen",

        "toan-truong",
        "tung-lop",

        "thong-tin-truong-hoc",
    ],
    "ROLE_HT": [
         "cau-hinh-truong-hoc",
        "cau-hinh-nam-hoc",
        "mon-hoc-thuoc-truong",
        "cau-hinh-lop-hoc",
        "mon-hoc-cho-lop",
        "xep-thoi-khoa-bieu",

        "bang-diem",

        "quan-ly-giao-vien",
        "them-moi-giao-vien",
        "cap-nhat-giao-vien",
        "thong-tin-giao-vien",
        "phan-cong-giang-day",
        "thoi-khoa-bieu-lich-day",

        "quan-ly-hoc-sinh",
        "diem-danh-chuyen-can",
        "so-diem",
        "danh-gia-hoc-luc",
        "danh-gia-hanh-kiem",
        "cap-nhat-hoc-sinh",
        "tao-hoc-sinh",
        "diem-hoc-tap",
        "ho-so-hoc-sinh",
        "ket-chuyen",

        "toan-truong",
        "tung-lop",
        
        "thong-tin-truong-hoc",
    ],

    "ROLE_HP": [
        "quan-ly-giao-vien",
        "thoi-khoa-bieu-lich-day",


        "quan-ly-hoc-sinh",

        "toan-truong",
        "tung-lop",
    ],
    "ROLE_TK": [
        "quan-ly-giao-vien",
        "thoi-khoa-bieu-lich-day",
        "thong-tin-giao-vien",


        "quan-ly-hoc-sinh",

        "toan-truong",
        "tung-lop",
    ],

    
    "ROLE_GVCN": [
        "thoi-khoa-bieu-lich-day",

        "quan-ly-hoc-sinh",
        "diem-danh-chuyen-can",
        "so-diem",
        "danh-gia-hoc-luc",
        "danh-gia-hanh-kiem",
        "cap-nhat-hoc-sinh",
        "tao-hoc-sinh",
        "diem-hoc-tap",
        "ho-so-hoc-sinh",

        "toan-truong",
        "tung-lop",
        "thong-tin-ca-nhan-giao-vien"
    ],

    "ROLE_GVBM": [
        "thoi-khoa-bieu-lich-day",
        "quan-ly-hoc-sinh",
        "so-diem",
        "ho-so-hoc-sinh",
        "diem-hoc-tap",

        "toan-truong",
        "tung-lop",
        "thong-tin-ca-nhan-giao-vien"
    ],

    "ROLE_USER": [
        "ho-so-hoc-sinh-parent",
        "diem-hoc-tap-parent",
        "thoi-khoa-bieu-parent",
        "diem-danh-hoc-sinh",
    ]
}

async function initDataUser() {
    await router.app.$store.dispatch("getCurrentUser");
    let years
    await $http.get('/school_year/history', {
        params: {
            studentCode: router.app.$store.getters['user'].username
        }
    }).then((result) => {
        years = result.data.data;
        let currentYear = new Date().getFullYear();
        let schoolYear = currentYear + "-" + (currentYear + 1);
        let choosed = schoolYear;
        if (years.includes(schoolYear)) {
            router.app.$store.dispatch("updateYear", choosed);
        } else {
            router.app.$store.dispatch("updateYear", years[0]);
        }
    }).catch(() => {})
    

}


async function initDataNotUser() {
    await router.app.$store.dispatch("getCurrentUser");
    let years
    if(router.app.$store.getters["year"] !== null) {
        return
    }
    await $http.get("/school_years/all").then((result) => {years = result.data.data;});
    await $http.get('/school_year/obj_current_year')
    .then((res) => {
        if (res.data.status === 'OK') {
            router.app.$store.dispatch("updateCurrentYear", res.data.data.years);
            router.app.$store.dispatch("updateYear", res.data.data.years);
            router.app.$store.dispatch("updateCurrentSemester", res.data.data.semester)
        } else {
            router.app.$store.dispatch("updateYear", years[0]);
        }
    })
    .catch(() => {
        router.app.$store.dispatch("updateYear", years[0]);
    })

}

router.beforeResolve( async (to, from, next) => {
    axios.defaults.headers.common["Authorization"] = "Bearer " + localStorage.getItem('school_token')
    axios({
        method: 'get',
        baseURL: `${process.env.VUE_APP_BASE_API_URL}`,
        url :'/api/auth/checkAuthToken'
    })
    .then(async (res) => {
        let tempRoles = []
        for(let i of res.data.authorities) {
            if (i.authority !== undefined && i.authority !==null) {
                tempRoles.push(i.authority)
            }
        }

        if(res.data.credentials === null){

            if (tempRoles.includes("ROLE_USER") && tempRoles.length === 1) {
                await initDataUser()
            } else {
                await initDataNotUser()
            }
            if(to.name == 'login' || to.name == undefined) {
                if (tempRoles.includes("ROLE_ADMIN")) {
                    next({name : 'cau-hinh-truong-hoc'})
                } else if (tempRoles.includes("ROLE_HT")){
                    next({name : 'cau-hinh-truong-hoc'})
                } else if (tempRoles.includes("ROLE_HP")){
                    next({name : 'quan-ly-giao-vien'})
                } else if (tempRoles.includes("ROLE_TK")){
                    next({name : 'quan-ly-giao-vien'})
                } else if (tempRoles.includes("ROLE_GVCN")){
                    next({name : 'thoi-khoa-bieu-lich-day'})
                } else if (tempRoles.includes("ROLE_GVBM")){
                    next({name : 'thoi-khoa-bieu-lich-day'})
                } else {
                    next({name : 'ho-so-hoc-sinh-parent'})
                } 
            }
            else {
                let pageAllowed = []
                for(let role of tempRoles) {
                    pageAllowed = pageAllowed.concat(pageAllow[role])
                }
                if (pageAllowed.includes(to.name)) {
                    next()
                } else {
                    if (tempRoles.includes("ROLE_ADMIN")) {
                        next({name : 'cau-hinh-truong-hoc'})
                    } else if (tempRoles.includes("ROLE_HT")){
                        next({name : 'cau-hinh-truong-hoc'})
                    } else if (tempRoles.includes("ROLE_HP")){
                        next({name : 'quan-ly-giao-vien'})
                    } else if (tempRoles.includes("ROLE_TK")){
                        next({name : 'quan-ly-giao-vien'})
                    } else if (tempRoles.includes("ROLE_GVCN")){
                        next({name : 'thoi-khoa-bieu-lich-day'})
                    } else if (tempRoles.includes("ROLE_GVBM")){
                        next({name : 'thoi-khoa-bieu-lich-day'})
                    } else {
                        next({name : 'ho-so-hoc-sinh-parent'})
                    } 
                }
            } 
        }
        else{
            if(to.name != 'login'){
                next({ name: 'login' })
            }else{
                next()
            }
        }
    })
    .catch(() => {})
})



export default router