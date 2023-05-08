<template>
        <v-form>
            <v-dialog v-model="show" max-width="900px">
                <v-card>
                    <v-card-title 
                    class="justify-center" 
                    style="background-color: rgb(26 118 207 / 1); color: white">
                        CẬP NHẬT PHÂN CÔNG GIẢNG DẠY
                    </v-card-title>
                    <v-card class="pa-6">
                        <v-row>
                            <v-col sm="12" md="6">
                                <v-text-field
                                v-model="item.year"
                                label="Năm học: *"
                                append-icon="mdi-chevron-down"
                                readonly>
                                </v-text-field>
                            </v-col>
                            <v-col sm="12" md="6">
                                <v-text-field
                                v-model="item.name"
                                append-icon="mdi-chevron-down"
                                label="Lớp: *"
                                readonly>
                                </v-text-field>
                            </v-col>
                            <v-col sm="12" md="6">
                                <v-text-field
                                v-model="item.dept"
                                append-icon="mdi-chevron-down"
                                label="Khối: *"
                                readonly>
                                </v-text-field>
                            </v-col>
                            <v-col sm="12" md="6">
                                <v-text-field
                                v-model="item.subject"
                                append-icon="mdi-chevron-down"
                                label="Môn học: *"
                                readonly>
                                </v-text-field>
                            </v-col>
                            <v-col sm="12" md="6">
                                <v-menu offset-y>
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-text-field
                                            v-on="on"
                                            v-bind="attrs"
                                            width="100%"
                                            append-icon="mdi-chevron-down"
                                            label="Giáo viên phân công: *"
                                            readonly
                                            v-model="infoTeacherChoosed"
                                            :rules="rules"
                                        >
                                        </v-text-field>
                                    </template>
                                    <v-list style="max-height: 50vh; overflow-y: scroll;">
                                        <v-list-item
                                            v-for="(it, index) in listTeacher"
                                            :key="index"
                                            @click="chooseTeacher(it)"
                                        >
                                            <v-list-item-title>{{ it.code }} - {{ it.full_name }}</v-list-item-title>
                                        </v-list-item>
                                    </v-list>
                                </v-menu>
                            </v-col>
                            <v-col sm="12" md="6">
                                <v-menu offset-y>
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-text-field
                                            v-on="on"
                                            v-bind="attrs"
                                            width="100%"
                                            append-icon="mdi-chevron-down"
                                            label="Kỳ dạy: *"
                                            readonly
                                            v-model="infoSemesterChoosed"
                                            :rules="rules"
                                        >
                                        </v-text-field>
                                    </template>
                                    <v-list style="max-height: 50vh; overflow-y: scroll;">
                                        <v-list-item-group
                                        multiple
                                        mandatory
                                        v-model="semesterChoosed">
                                            <v-list-item  
                                            key=0
                                            >
                                                <v-list-item-title>Cả năm</v-list-item-title>
                                            </v-list-item>
                                            <v-list-item
                                                v-for="(it, index) in amountOfSemester"
                                                :key="index + 1"
                                            >
                                                <v-list-item-title>Kỳ {{ it }}</v-list-item-title>
                                            </v-list-item>
                                        </v-list-item-group>
                                    </v-list>
                                </v-menu>
                            </v-col>
                        </v-row>
                        <v-row align="center" justify="center">
                            <v-btn class="mr-2" elevation="1" color="warning darken-1" text @click="cancel"> Hủy bỏ </v-btn>
                            <v-btn class="ml-2" elevation="1" color="primary darken-1" text @click="save" > Lưu lại </v-btn>
                        </v-row>
                    </v-card>
                </v-card>
            </v-dialog>
        </v-form>
</template>

<script>
import AppService from "@/services/app.service";
export default {
    props: ['amountOfSemester'],
    data() {
       return{
           show: false,
           item: {
               year: null,
               name: null,
               dept: null,
               subject: null
           },
           promise: null,
           infoTeacherChoosed: null,
           infoSemesterChoosed: null,
           rules: [(v) => !!v || "Hãy chọn một lựa chọn"],
           semesterChoosed: [],
           listTeacher: [],
           subjectId: null
       } 
    },
    methods: {
        open(item){
            this.show = true
            this.item.year = this.$store.getters['year']
            this.item.name = item.name
            this.item.dept = item.grade
            this.item.subject = item.subjectName
            this.item.teachingAssignmentId = item.id
            this.item.subjectId = item.subjectId
            this.subjectId = item.subjectId
            this.item.deptId = item.deptId

            this.item.teacherId = item.teacherId
            this.item.teacherName = item.teacherName
            this.item.code = item.code
            this.item.subjectCode = item.subjectCode
            this.item.classCode = item.classCode
            this.infoTeacherChoosed = item.code + ' - ' + item.teacherName

            let semester = []
            for(let i = 1; i <= this.amountOfSemester; i++){
                if(item['semester' + i]){
                    semester.push(i)
                }
            }
            this.semesterChoosed = semester
            return new Promise((resolve, reject) => {
                this.promise = {
                    resolve,
                    reject
                }
            })
        },
        save(){
            this.promise.resolve(this.item)
            this.cancel()
        },
        cancel(){
            this.show = false
        },
        chooseTeacher(item){
            this.item.teacherId = item.id 
            this.item.teacherName = item.full_name
            this.item.code = item.code
            this.infoTeacherChoosed = item.code + ' - ' + item.full_name
        }
    },
    watch: {
        semesterChoosed(){
            if(this.semesterChoosed.some(x => x == 0)){
                this.semesterChoosed = []
                for(let i = 1; i <= this.amountOfSemester; i++){
                    this.semesterChoosed.push(i)
                }
            }

            //KhÔng sort trực tiếp semesterChoosed được, vì sẽ bị loop vô hạn 
            let arrTemp = this.semesterChoosed.map(x => x)
            arrTemp.sort()

            for(let i = 1; i <= 2; i++){
                this.item['semester' + i] = false
            }
            for(let i = 0; i < this.semesterChoosed.length; i++){
                this.item['semester' + this.semesterChoosed[i]] = true
            }
            if(this.semesterChoosed.length == this.amountOfSemester){
                this.item.applyAllSemester = 1
            }else{
                this.item.applyAllSemester = 0
            }
            this.infoSemesterChoosed = arrTemp.reduce(( previousValue, currentValue ) => 
                previousValue + ', '+ currentValue , ''
            )
            this.infoSemesterChoosed = this.infoSemesterChoosed.substring(2, this.infoSemesterChoosed.length)
        },
        subjectId(){
            AppService.getTeacherBySubjectId(this.item.subjectId)
            .then((res) => {
                this.listTeacher = res.data.data.Teachers
            })
        }
    }
}
</script>

<style scoped>
</style>