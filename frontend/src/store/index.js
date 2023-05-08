import Vue from "vue";
import Vuex from 'vuex'

import {actions} from './actions'
import {mutations} from './mutations'
import {getters} from './getters'

Vue.use(Vuex)

export const store = new Vuex.Store({
    state: {
        year: null,
        user: {},
        currentYear: null,
        currentSemester: null,

        pageAllow: {
            "ROLE_ADMIN": [
                "cau-hinh-truong-hoc",
                "cau-hinh-nam-hoc",
                "mon-hoc-thuoc-truong",
                "cau-hinh-lop-hoc",
                "mon-hoc-cho-lop",
                "xep-thoi-khoa-bieu",

                "bang-diem",

                "quan-ly-giao-vien",
                "phan-cong-giang-day",
                "thoi-khoa-bieu-lich-day",

                "quan-ly-hoc-sinh",
                "diem-danh-chuyen-can",
                "so-diem",
                "danh-gia-hoc-luc",
                "danh-gia-hanh-kiem",
                "ket-chuyen",

                "toan-truong",
                "tung-lop",
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
                "phan-cong-giang-day",
                "thoi-khoa-bieu-lich-day",

                "quan-ly-hoc-sinh",
                "diem-danh-chuyen-can",
                "so-diem",
                "danh-gia-hoc-luc",
                "danh-gia-hanh-kiem",
                "ket-chuyen",

                "toan-truong",
                "tung-lop",
            ],

            "ROLE_HP": [
                "quan-ly-giao-vien",
                "phan-cong-giang-day",
                "thoi-khoa-bieu-lich-day",
                "quan-ly-hoc-sinh",
                "diem-danh-chuyen-can",
                "so-diem",
                "danh-gia-hoc-luc",
                "danh-gia-hanh-kiem",
                "ket-chuyen",
                "toan-truong",
                "tung-lop",
            ],
            "ROLE_TK": [
                "quan-ly-giao-vien",
                "phan-cong-giang-day",
                "thoi-khoa-bieu-lich-day",
                "quan-ly-hoc-sinh",
                "diem-danh-chuyen-can",
                "so-diem",
                "danh-gia-hoc-luc",
                "danh-gia-hanh-kiem",
                "ket-chuyen",
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
                "thong-tin-ca-nhan-giao-vien"
            ],

            "ROLE_GVBM": [
                "thoi-khoa-bieu-lich-day",
                "quan-ly-hoc-sinh",
                "so-diem",

                "toan-truong",
                "tung-lop",
                "thong-tin-ca-nhan-giao-vien"
            ],

            "ROLE_USER": [
                "ho-so-hoc-sinh-parent",
                // "diem-hoc-tap-parent",
                "thoi-khoa-bieu-parent",
                "diem-danh-hoc-sinh"
            ]
        },
        allMenu:{
            menuNhaTruong: [
                "cau-hinh-truong-hoc",
                "cau-hinh-nam-hoc",
                "mon-hoc-thuoc-truong",
                "cau-hinh-lop-hoc",
                "mon-hoc-cho-lop",
                "xep-thoi-khoa-bieu",
            ],
            menuCauHinhHeThong: [
                "bang-diem"
            ],
            menuGiaoVien: [
                "quan-ly-giao-vien",
                "phan-cong-giang-day",
                "thoi-khoa-bieu-lich-day"
            ],
            menuHocSinh: [
                "quan-ly-hoc-sinh",
                "diem-danh-chuyen-can",
                "so-diem",
                "danh-gia-hoc-luc",
                "danh-gia-hanh-kiem",
                "ket-chuyen",
            ],
            menuBaoCao: [
                "toan-truong",
                "tung-lop",
            ],
            menuThongTinChung: [
                "ho-so-hoc-sinh-parent",
                "diem-hoc-tap-parent"
            ],
            menuThoiKhoaBieu: [
                "thoi-khoa-bieu-parent"
            ],
            menuDiemDanh: [
                "diem-danh-hoc-sinh"
            ]
        }
    },
    getters,
    mutations,
    actions,
    modules: {}
})