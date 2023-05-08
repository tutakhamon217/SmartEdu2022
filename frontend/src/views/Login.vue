<template>
<v-row style="height: 100%">
    <v-col md="6" v-if="!hiddenLeft" style="background-image: url('sideImage.svg');  background-size: cover; background-position: center center;">
    </v-col>
    <v-col md="6" class="mt-4 text-center pa-3" style="position: relative">
        <div style="max-width: 720px; margin: 0px auto" class="text-center">
            <div style="margin-bottom: 40px">
                <v-img src="../../public/logoNew.svg" height="300px" width="300px" style="margin: 0px auto"></v-img>
            </div>
            <div class="px-8 mb-0 pb-0">
                <v-scroll-x-reverse-transition>
                    <div v-show="isLogin" class="mt-10 mb-12">
                        <div style="font-size: 19px">
                            <h1>QUẢN LÝ TRƯỜNG HỌC</h1>
                        </div>
                        <br />
                        <div>
                            <p style="margin-top: 16px">
                                Chào mừng trở lại! Hãy đăng nhập tài khoản của bạn
                            </p>
                        </div>
                        <div>
                            <v-form ref="form-login">
                                <v-row>
                                    <v-text-field label="Tài khoản" :rules="rules" hide-details="auto" placeholder="Nhập tài khoản" v-model="username"></v-text-field>
                                </v-row>
                                <v-row>
                                    <v-text-field :rules="rules" label="Mật khẩu" append-icon="mdi-eye" @click:append="showPassword" placeholder="Nhập mật khẩu" :type="typePassword" v-model="password" @keyup.enter="login">
                                    </v-text-field>
                                </v-row>
                            </v-form>
                            <v-row align="center" justify="center">
                                <v-btn class="mr-4 my-8" color="primary" @click="login()">
                                    <v-icon color="white">mdi-chevron-right</v-icon>
                                    <span>Đăng nhập</span>
                                </v-btn>
                            </v-row>
                            <v-row class="text-left option-account">
                                <v-col md="3">
                                    <p>Quên mật khẩu?</p>
                                </v-col>
                                <v-col md="9">
                                    <p @click="toResetPass()" style="cursor: pointer; color: #1976d2">Khôi phục mật khẩu</p>
                                </v-col>
                            </v-row>
                        </div>
                    </div>
                </v-scroll-x-reverse-transition>
                <v-scroll-x-reverse-transition>
                    <div v-show="isResetPass" class="mt-10 mb-12">
                        <div style="font-size: 19px">
                            <h1>QUẢN LÝ TRƯỜNG HỌC</h1>
                        </div>
                        <br />
                        <div>
                            <p style="margin-top: 16px">
                                Khôi phục mật khẩu
                            </p>
                        </div>
                        <v-slide-x-reverse-transition>
                            <div v-show="isSendOTP">
                                <v-form ref="form-send-otp">
                                    <v-row>
                                        <v-text-field label="Tài khoản" :rules="rules" hide-details="auto" placeholder="Nhập tài khoản" v-model="usernameOTP"></v-text-field>
                                    </v-row>
                                </v-form>
                                <v-row align="center" justify="center">
                                    <v-btn class="mr-4 my-8" color="primary" @click="sendOTP()">
                                        <v-icon color="white">mdi-chevron-right</v-icon>
                                        <span>Nhận OTP</span>
                                    </v-btn>
                                </v-row>
                                <v-row class="text-left option-account">
                                    <v-col md="3">
                                        <p>Đăng nhập lại</p>
                                    </v-col>
                                    <v-col md="9">
                                        <p @click="toLogin()" style="cursor: pointer; color: #1976d2">Đăng nhập</p>
                                    </v-col>
                                </v-row>
                            </div>
                        </v-slide-x-reverse-transition>
                        <v-slide-x-reverse-transition>
                            <div v-show="isCheckOTP">
                                <v-form ref="form-check-otp">
                                    <v-row>
                                        <v-text-field label="Mã OTP" :rules="rulesOTP" hide-details="auto" placeholder="Mã OTP được gửi qua SMS" v-model="otp"></v-text-field>
                                    </v-row>
                                </v-form>
                                <v-row align="center" justify="center">
                                    <v-btn class="mr-4 my-8" color="primary" @click="toSendOTP()">
                                        <v-icon color="white">mdi-chevron-left</v-icon>
                                        <span>Trở lại</span>
                                    </v-btn>
                                    <v-btn class="mr-4 my-8" color="primary" @click="checkOTP()">
                                        <v-icon color="white">mdi-chevron-right</v-icon>
                                        <span>Gửi</span>
                                    </v-btn>
                                </v-row>
                                <v-row class="text-left option-account">
                                    <v-col md="3">
                                        <p>Đăng nhập lại</p>
                                    </v-col>
                                    <v-col md="9">
                                        <p @click="toLogin()" style="cursor: pointer; color: #1976d2">Đăng nhập</p>
                                    </v-col>
                                </v-row>
                            </div>
                        </v-slide-x-reverse-transition>
                    </div>
                </v-scroll-x-reverse-transition>
                <div class="login-footer mb-0 pb-0">
                    <p style="opacity: 0.65" class="mb-0">Smart Edu - Capstone Project</p>
                </div>
            </div>
        </div>
    </v-col>
    <ToastMessage ref="toastMessage" />
    <Loading ref="Loading"></Loading>
</v-row>
</template>

<script>
import AppService from "@/services/app.service";
import ToastMessage from "@/components/ToastMessage";
import Loading from "@/components/Loading.vue";

export default {
    name: "LoginView",
    components: {
        ToastMessage,
        Loading
    },
    data() {
        return {
            isLogin: true,
            isSendOTP: true,
            isResetPass: false,
            isCheckOTP: false,
            rules: [
                (value) => !!value || "Không được để trống",
                (value) => (value && value.length >= 3) || "Ít nhất 3 ký tự",
            ],
            rulesOTP: [
                (value) => !!value || "Không được để trống",
                (value) => (value && value.length >= 6) || "Ít nhất 6 ký tự",
            ],
            username: "",
            password: "",
            hiddenLeft: false,
            typePassword: "password",

            usernameOTP: "",
            otp: "",
            timer: "",
            countWrong: 3,
        };
    },
    methods: {
        login() {
            if (this.$refs['form-login'].validate()) {
                this.$refs["Loading"].open();
                AppService.login({
                        login: this.username,
                        password: this.password,
                    })
                    .then((response) => {
                        if (response.data.status == "BAD_REQUEST") {
                            this.$refs["toastMessage"].open(response.data.message, true);
                        } else {
                            localStorage.setItem("school_token", response.data.accessToken);
                            this.$store.dispatch("updateUser", response.data);
                            window.location.href = '/cau-hinh-truong-hoc'
                        }
                        this.$refs["Loading"].close();
                    })
                    .catch(() => {
                        this.$refs["Loading"].close();
                        this.$refs["toastMessage"].open("Sai mật khẩu", true);
                    })
            }
        },
        showPassword() {
            if (this.typePassword === "password") {
                this.typePassword = "text";
            } else {
                this.typePassword = "password";
            }
        },
        sendOTP() {
            if (!this.$refs['form-send-otp'].validate()) return
            this.$refs["Loading"].open();
            AppService.sendOTP(this.usernameOTP).then((res) => {
                if (res.data.status === "OK") {
                    this.countWrong = 3
                    this.$refs["toastMessage"].open(res.data.message, false);
                    this.toCheckOTP()
                } else {
                    this.$refs["toastMessage"].open(res.data.message, true);
                }
                this.$refs["Loading"].close();
            }).catch(() => {
                this.$refs["Loading"].close();
                this.$refs["toastMessage"].open("Lỗi trong quá trình gửi OTP", true);
            })
        },
        toLogin() {
            this.username = ""
            this.password = ""
            this.isResetPass = false
            this.timer = setTimeout(() => {
                this.isLogin = true;
            }, 400);
            this.$refs['form-login'].resetValidation()
        },
        toResetPass() {
            this.usernameOTP = ""
            this.otp = ""
            this.isLogin = false
            this.isSendOTP = true;
            this.isCheckOTP = false;
            this.$refs['form-send-otp'].resetValidation()
            this.timer = setTimeout(() => {
                this.isResetPass = true;
            }, 400);
        },
        toCheckOTP() {
            this.isSendOTP = false
            this.otp = ""
            this.timer = setTimeout(() => {
                this.isCheckOTP = true;
            }, 400);
            this.$refs['form-check-otp'].resetValidation()
        },
        toSendOTP() {
            this.isCheckOTP = false
            this.otp = ""
            this.timer = setTimeout(() => {
                this.isSendOTP = true;
            }, 400);
        },
        checkOTP() {
            this.$refs["Loading"].open();
            AppService.checkOTP(this.usernameOTP, this.otp).then((res) => {
                if (res.data.status === "OK") {
                    this.$refs["toastMessage"].open(res.data.message, false);
                    this.toLogin()
                } else {
                    this.countWrong -= 1
                    if(this.countWrong <= 0) {
                        this.countWrong = 3
                        this.$refs["toastMessage"].open("Nhập sai OTP quá 3 lần", true);
                        this.toSendOTP()
                    } else {
                        this.$refs["toastMessage"].open(res.data.message, true);
                    }
                }
                this.$refs["Loading"].close();
            }).catch(() => {
                this.$refs["Loading"].close();
                this.$refs["toastMessage"].open("Lỗi trong quá xác nhận OTP", true);
            })
        }
    },
    watch: {
        "$vuetify.breakpoint.width"() {
            if (this.$vuetify.breakpoint.width < 960) {
                this.hiddenLeft = true
            } else {
                this.hiddenLeft = false
            }
        },
    },
};
</script>

<style>
.option-account p {
    padding: 0px;
    margin: 0px;
}

.option-account div {
    padding: 0px !important;
}

.option-account a {
    text-decoration: none;
}

.login-footer {
    padding-top: 3vh;
    position: absolute;
    bottom: 0;
    text-align: center;
    max-width: 720px;
    width: 100%;
}

.zoomOut {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
}

.zoomOut .v-image {
    max-width: 720px;
    max-height: 720px;
}
</style>
