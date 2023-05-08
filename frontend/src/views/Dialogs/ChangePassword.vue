<template>
  <div class="SchoolDepartmentDialog">
    <v-dialog v-model="show" persistent max-width="600px">
      <v-card>
        <v-toolbar color="primary" dark>
          <v-toolbar-title>Đổi mật khẩu</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="close()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-form
          ref="form"
          lazy-validation
        >
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="12" md="12">
                <v-text-field
                  label="Mật khẩu cũ"
                  required
                  v-model="oldPass"
                  :rules="[this.rule.required]"
                  :error-messages="errorMessage"
                  :type="show1 ? 'text' : 'password'"
                  :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="show1 = !show1"
                ></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" sm="12" md="12">
                <v-text-field
                  label="Mật khẩu mới"
                  v-model="newPass"
                  :rules="[this.rule.required, this.rule.passWord, this.rule.passWordLength, this.rule.passWordNoChange]"
                  :type="show2 ? 'text' : 'password'"
                  :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="show2 = !show2"
                ></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" sm="12" md="12">
                <v-text-field
                  label="Nhập lại mật khẩu mới"
                  v-model="newPassConfirm"
                  :rules="[this.rule.required, this.rule.passWord, this.rule.passWordConfirm]"
                  :type="show3 ? 'text' : 'password'"
                  :append-icon="show3 ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="show3 = !show3"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        </v-form>
        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn elevation="1" color="warning darken-1" text @click="close"> Hủy bỏ </v-btn>
          <v-btn elevation="1" color="primary darken-1" text @click="submit"> Lưu lại </v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <ToastMessage ref="ToastMessage"> </ToastMessage>
    <Loading ref="Loading"></Loading>
  </div>
</template>

<script>
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage.vue";


export default {
  name: "ChangePassword",
  components: {Loading, ToastMessage },
  data() {
    return {
      show: false,
      oldPass: "",
      newPass: "",
      newPassConfirm: "",
      userName: "",
      errorMessage: [],
      show1: false,
      show2: false,
      show3: false,
      rule: {
        required: value => !!value || 'Bắt buộc',
        passWord: value => {
          const pattern = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/
          return pattern.test(value) || 'Mật khẩu phải chứa cả ký tự và số'
        },
        passWordLength: value => {
          return value.length >= 6 || 'Mật khẩu phải chứa ít nhất 6 ký tự'
        },
        passWordConfirm:  value => {
          return value === this.newPass || 'Mật khẩu mới chưa khớp'
        },
        passWordNoChange:  value => {
          return value !== this.oldPass || 'Mật khẩu mới không được trùng với mật khẩu cũ'
        },
      },
    };
  },
  mounted () {

  },
  methods: {
    open(userName) {
      this.show = true
      
      this.errorMessage = []
      this.userName = this.$store.getters["user"].username
      this.oldPass = ""
      this.newPass = ""
      this.newPassConfirm = ""
      
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    close() {
      this.show = false;
      this.$refs.form.resetValidation()
      this.promise.reject();
    },
    submit() {
      if (!this.$refs.form.validate()) {
        return
      }

      this.$refs["Loading"].open();
      AppService.login({
        login: this.userName,
        password: this.oldPass,
      })
      .then((response) => {
          if (response.status === 200) {
              this.errorMessage = []
              AppService.changePassword({
                login: this.userName,
                password: this.newPass,
              })
              .then((response) => {
                  this.$refs["Loading"].close();
                  if (response.data.status === "BAD_REQUEST") {
                    this.$refs["ToastMessage"].open(response.data.message, true);
                  } else { 
                    this.promise.resolve();
                    this.close()
                  }
              })
              .catch(() => {
                this.$refs["ToastMessage"].open("Đổi mật khẩu thất bại", true);
                this.$refs["Loading"].close();
              })

          } else {
            this.errorMessage = "Mật khẩu không chính xác"
            this.$refs["Loading"].close();
          }
          
      }, ()=>{
        this.errorMessage = "Mật khẩu không chính xác"
        this.$refs["Loading"].close();
        }
      )
      .catch(() => {
        this.$refs["Loading"].close();
        this.errorMessage = "Mật khẩu không chính xác"
      })
    },
  },
  watch: {
    oldPass(newValue) {
      this.errorMessage = []
    }
  },
};
</script>

<style scoped>
</style>
