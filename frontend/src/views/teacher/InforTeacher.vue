<template>
    <div>
        <v-row>
            <v-col sm="12" md="3">
                <v-img :src="employee.avatar ? employee.avatar : defaultImg"></v-img>
            </v-col>
            <v-col sm="12" md="9">
                <v-row>
                    <v-col sm="12">
                        <v-text-field 
                        readonly 
                        style="font-weight: bolder; font-size: 30px;" 
                        v-model="employee.full_name">
                        </v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col sm="12" md="4">
                        <v-text-field readonly label="Mã cán bộ" v-model="employee.code"></v-text-field>
                    </v-col>
                    <v-col sm="12" md="4">
                        <v-text-field readonly label="Ngày vào trường" v-model="employee.start_date"></v-text-field>
                    </v-col>
                    <v-col sm="12" md="4">
                        <v-text-field readonly label="Số điện thoại" v-model="employee.phone"></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col sm="12" md="4">
                        <v-text-field readonly label="Trạng thái" v-model="employee.status"></v-text-field>
                    </v-col>
                    <v-col sm="12" md="4">
                        <v-text-field readonly label="Loại hợp đồng" v-model="employee.contract_type"></v-text-field>
                    </v-col>
                    <v-col sm="12" md="4">
                        <v-text-field readonly label="Email" v-model="employee.email"></v-text-field>
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
        <v-row style="background-color: #488fef;color: white;">
            <v-col>
                THÔNG TIN LÝ LỊCH CÁ NHÂN
            </v-col>
        </v-row>
        <v-row>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Đơn vị" v-model="employee.nameDept3"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Khoa ban" v-model="employee.nameDept2"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Tổ bộ môn" v-model="employee.nameDept1"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Ngày sinh" v-model="employee.birth_day"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Tôn giáo" v-model="employee.religion"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Dân tộc" v-model="employee.nation"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Quê quán" v-model="employee.home_town"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Địa chỉ thường trú" v-model="employee.permanent_address"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Địa chỉ tạm trú" v-model="employee.temporary_address"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Số sổ BHXH" v-model="employee.social_insurance_number"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Số CMND/TCC" v-model="employee.identity_card"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Nơi cấp CMND/TCC" v-model="employee.issued_address"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Ngày cấp CMND/TCC" v-model="employee.issued_date"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Tình trạng hôn nhân" v-model="employee.marriage_status"></v-text-field>
            </v-col>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Giới tính" v-model="employee.sex"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col sm="12" md="4">
                <v-text-field readonly label="Chức vụ" v-model="employee.name_authorities"></v-text-field>
            </v-col>
        </v-row>
        <Loading ref="loading" />
    </div>
</template>


<script>
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";

export default {
    name: 'InforTeacher',
    components:{
        Loading
    },
    data() {
        return{
            employee: {},
            defaultImg: 'https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png',
        }
    },
    async mounted(){
        this.$refs['loading'].open()
        await this.$store.dispatch("getCurrentUser");
        await AppService.getInforTeacher(this.$route.params.id)
        .then((res) => {
            if(res.data.status === 'OK'){
                this.employee = res.data.data
                this.employee.contract_type == 0 ? this.employee.contract_type = 'Hợp đồng' : this.employee.contract_type = 'Biên chế'
                this.employee.sex == 0 ? this.employee.sex = 'Nam' : this.employee.sex = 'Nữ'
                this.employee.marriage_status == 0 ? this.employee.marriage_status = 'Độc thân' : this.employee.marriage_status = 'Đã kết hôn'
                this.employee.issued_date = new Date(this.employee.issued_date).toLocaleString().substring(10)
                this.employee.start_date = new Date(this.employee.start_date).toLocaleString().substring(10)
                this.employee.birth_day = new Date(this.employee.birth_day).toLocaleString().substring(10)
                if(this.employee.status == 0){
                    this.employee.status = 'Đang làm việc'
                }else if(this.employee.status == 1){
                    this.employee.status = 'Đã nghỉ việc'
                }else if(this.employee.status == 2){
                    this.employee.status = 'Đã nghỉ hưu'
                }else{
                    this.employee.status = 'Tạm nghỉ'
                }
            }else{
                this.$refs['toastMessage'].open(res.data.message, true)
            }
        })
        .catch(() => {
            this.$refs['toastMessage'].open('Lấy thông tin giáo viên thất bại', true)
        })
        this.$refs['loading'].close()
    }
}
</script>