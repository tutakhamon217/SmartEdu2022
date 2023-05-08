<template>
<div>
    <v-card v-show="isShow" class="mb-3">
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
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Học kỳ" readonly v-model="choosedHocKy" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll" v-if="amountOfSemester">
                                <v-list-item @click="chooseHocKy(0)">
                                    <v-list-item-title>{{ formatKyHoc(0) }}</v-list-item-title>
                                </v-list-item>
                                <v-list-item v-for="(item, index) in amountOfSemester" :key="index" @click="chooseHocKy(item)">
                                    <v-list-item-title>{{
                      formatKyHoc(item)
                    }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Môn học (*)" readonly v-model="choosedMon" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataMon" :key="index" @click="chooseMon(item)">
                                    {{ item.name }}
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col sm="12" md="6">
                        <v-menu offset-y>
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field v-on="on" v-bind="attrs" width="100%" append-icon="mdi-chevron-down" label="Lớp học (*)" v-model="choosedLop" :rules="rules">
                                </v-text-field>
                            </template>
                            <v-list style="max-height: 50vh; overflow-y: scroll">
                                <v-list-item v-for="(item, index) in dataLop" :key="index" @click="chooseLop(item)">
                                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
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
    <v-form v-show="isShow" ref="form">
        <div v-if="oldSearch.hocKy == 0">
            <v-card>
                <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
                    <v-toolbar-title style="width: 100%; align-items: center" class="d-flex">
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on, attrs }">
                                <span v-bind="attrs" v-on="on" style="color:white">{{
                      formatTextTooltip('Sổ điểm môn ' + subjectNameSearched + ' - ' + classNameSearched , 60)
                    }}</span>
                            </template>
                            <span v-if="subjectNameSearched" style="color: white">Sổ điểm môn {{ subjectNameSearched }} -
                                {{ classNameSearched }}</span>
                        </v-tooltip>
                        <v-spacer></v-spacer>
                    </v-toolbar-title>
                </v-toolbar>
                <v-data-table :headers="headers" :items="desserts" :items-per-page="10" disable-sort :hide-default-header="true" :footer-props="{
                'items-per-page-text': 'Số dòng mỗi trang:',
              }" class="customTableSemester0">
            
                    <template slot="no-data">
                    Danh sách sổ điểm rỗng
                    </template>
                    <template v-slot:header>
                        <tr>
                            <th v-for="(head) in headers" :key="head.value + 'a'">
                                {{ head.text }}
                            </th>
                        </tr>
                    </template>

                    <template #item="{ item }">
                        <tr>
                            <td>
                                <p class="text-center ma-0">{{ item.stt }}</p>
                            </td>
                            <td>
                                <span>{{ item.studentCode }}</span>
                            </td>
                            <td>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on, attrs }">
                                        <span v-bind="attrs" v-on="on">{{
                          formatTextTooltip(item.studentName, 25)
                        }}</span>
                                    </template>
                                    <span>{{ item.studentName }}</span>
                                </v-tooltip>
                            </td>
                            <td>
                                <p class="text-center ma-0">{{ item.avgScore }}</p>
                            </td>
                        </tr>
                    </template>

                    <template v-slot:footer.page-text="props">
                        {{ props.pageStart }}-{{ props.pageStop }} của
                        {{ props.itemsLength }} kết quả
                    </template>
                </v-data-table>
            </v-card>
        </div>
        <div v-else>
            <v-card v-if="typeHeader == 'score'">
                <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
                    <v-toolbar-title style="width: 100%; align-items: center" class="d-flex">
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on, attrs }">
                                <span v-bind="attrs" v-on="on" style="color:white">{{
                      formatTextTooltip('Sổ điểm môn ' + subjectNameSearched + ' - ' + classNameSearched , 38)
                    }}</span>
                            </template>
                            <span v-if="subjectNameSearched" style="color: white">Sổ điểm môn {{ subjectNameSearched }} -
                                {{ classNameSearched }}</span>
                        </v-tooltip>
                        <v-spacer></v-spacer>

                        <v-btn style="background-color:white;color:black" v-if="subjectNameSearched && canUpdate" @click="openEvaluate" class="mr-3">Đánh giá hạnh kiểm</v-btn>

                        <v-btn style="background-color:white;color:black" @click="importFile" v-if="subjectNameSearched && canUpdate">
                            <v-icon color="primary">mdi-book-plus</v-icon>
                            <p class="ma-0" style="color: black">Import File</p>
                        </v-btn>
                        <v-btn style="background-color:white;color:black" @click="update" class="ml-3" v-if="subjectNameSearched && canUpdate && !updating">Cập nhật</v-btn>
                    </v-toolbar-title>
                </v-toolbar>
                <v-data-table :headers="headers" :items="desserts" :items-per-page="10" disable-sort :hide-default-header="true" :footer-props="{
                'items-per-page-text': 'Số dòng mỗi trang:',
              }" class="customHeaderSoDiem">
                    <template slot="no-data">
                    Danh sách sổ điểm rỗng
                    </template>
                    <template v-slot:header>
                        <tr style="height: 27px">
                            <th style="border-left: 1px solid #dddddd"></th>
                            <th></th>
                            <th></th>
                            <th v-for="(cus, index) in customHeader" :key="index" style="position: relative" :style="{
                      borderLeft:
                        cus.d > 1 && cus.d <= cus.quantity
                          ? 'none'
                          : '1px solid #dddddd',
                    }">
                                <span v-if="cus.d == 1 && cus.quantity > 1" :style="{ width: 75 * cus.quantity + 'px' }" style="
                        position: absolute;
                        left: 0;
                        right: 0;
                        margin-left: auto;
                        margin-right: auto;
                      ">{{ cus.text }}</span>
                                <span v-else-if="cus.d == 1 && cus.quantity == 1">{{
                      cus.text
                    }}</span>
                                <span v-else></span>
                            </th>
                            <th style="
                      border-left: 1px solid #dddddd;
                      border-right: 1px solid #dddddd;
                    "></th>
                        </tr>

                        <tr>
                            <th v-for="(head, index) in headers" :key="head.value + 'a'" :style="{ width: index >= 3 ? '75px' : '' }">
                                {{ head.text }}
                            </th>
                        </tr>
                    </template>

                    <template #item="{ item }">
                        <tr>
                            <td>
                                <p class="text-center ma-0">{{ item.stt }}</p>
                            </td>
                            <td>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on, attrs }">
                                        <span v-bind="attrs" v-on="on">{{
                          formatTextTooltip(item.studentCode, 13)
                        }}</span>
                                    </template>
                                    <span>{{ item.studentCode }}</span>
                                </v-tooltip>
                            </td>
                            <td>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on, attrs }">
                                        <span v-bind="attrs" v-on="on">{{
                          formatTextTooltip(item.studentName, 25)
                        }}</span>
                                    </template>
                                    <span>{{ item.studentName }}</span>
                                </v-tooltip>
                            </td>
                            <td v-for="(cus, index) in customHeader" :key="index">
                                <v-text-field v-if="updating" v-model="item[cus.value]" :rules="rulesNumber" min="0" max="10" type="number" @change="catchChange(item.stt, cus.code, cus.time, $event)"></v-text-field>
                                <p class="text-center ma-0" v-else>
                                    {{ !item[cus.value] ? "-" : item[cus.value] }}
                                </p>
                            </td>
                            <td>
                                <p class="text-center ma-0">{{ item.average }}</p>
                            </td>
                        </tr>
                    </template>

                    <template v-slot:footer.page-text="props">
                        {{ props.pageStart }}-{{ props.pageStop }} của
                        {{ props.itemsLength }} kết quả
                    </template>
                </v-data-table>
            </v-card>
            <v-card v-else>
                <v-toolbar flat dense dark class="font-weight-bold" color="primary lighten-1" style="border-radius: 5px 5px 0px 0px">
                    <v-toolbar-title style="width: 100%; align-items: center" class="d-flex">
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on, attrs }">
                                <span v-bind="attrs" v-on="on" style="color:white">{{
                      formatTextTooltip('Sổ điểm môn ' + subjectNameSearched + ' - ' + classNameSearched , 38)
                    }}</span>
                            </template>
                            <span v-if="subjectNameSearched" style="color: white">Sổ điểm môn {{ subjectNameSearched }} -
                                {{ classNameSearched }}</span>
                        </v-tooltip>
                        <v-spacer></v-spacer>

                        <v-btn style="background-color:white;color:black" v-if="subjectNameSearched && canUpdate" @click="openEvaluate" class="mr-3">Đánh giá hạnh kiểm</v-btn>

                        <v-btn style="background-color:white;color:black" @click="importFile" v-if="subjectNameSearched && canUpdate">
                            <v-icon color="primary">mdi-book-plus</v-icon>
                            <p class="ma-0" style="color: black">Import File</p>
                        </v-btn>
                        <v-btn style="background-color:white;color:black" @click="update" class="ml-3" v-if="subjectNameSearched && canUpdate && !updating">Cập nhật</v-btn>
                    </v-toolbar-title>
                </v-toolbar>
                <v-data-table :headers="headers" :items="desserts" :items-per-page="10" disable-sort :hide-default-header="true" :footer-props="{
                'items-per-page-text': 'Số dòng mỗi trang:',
              }" class="customHeaderSoDiemXepLoai">
                    <template slot="no-data">
                    Danh sách sổ điểm rỗng
                    </template>
                    <template v-slot:header>
                        <th v-for="head in defaultHeader" :key="head.value + 'a'">
                            {{ head.text }}
                        </th>
                        <th v-for="(head, index) in customHeader" :key="index + 'b'">
                            {{ head.text }}
                        </th>
                        <!-- <th>Nhận xét</th> -->
                    </template>

                    <template #item="{ item }">
                        <tr>
                            <td>
                                <p class="text-center ma-0">{{ item.stt }}</p>
                            </td>
                            <td>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on, attrs }">
                                        <span v-bind="attrs" v-on="on">{{
                          formatTextTooltip(item.studentCode, 13)
                        }}</span>
                                    </template>
                                    <span>{{ item.studentCode }}</span>
                                </v-tooltip>
                            </td>
                            <td>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on, attrs }">
                                        <span v-bind="attrs" v-on="on">{{
                          formatTextTooltip(item.studentName, 25)
                        }}</span>
                                    </template>
                                    <span>{{ item.studentName }}</span>
                                </v-tooltip>
                            </td>
                            <td v-for="(cus, index) in customHeader" :key="index" style="width: 130px">
                                <v-select :items="item[cus.text + 'selectedValue']" v-model="item[cus.text]" dense outlined class="selectGradingGradeBook" v-if="
                        updating && item[cus.text + 'selectedValue'].length != 0
                      " @change="catchChangeGrade(item.stt, cus.text, $event)"></v-select>
                                <v-text-field v-else-if="
                        updating && item[cus.text + 'selectedValue'].length == 0
                      " v-model="item[cus.text]" @change="catchChangeGrade(item.stt, cus.text,$event)"></v-text-field>
                                <span v-else>{{ item[cus.text] }}</span>
                            </td>
                            <!-- <td>
                    <v-text-field
                      v-if="updating"
                      v-model="item.evaluate"
                    ></v-text-field>
                    <span v-else>{{ item.evaluate }}</span>
                  </td> -->
                        </tr>
                    </template>

                    <template v-slot:footer.page-text="props">
                        {{ props.pageStart }}-{{ props.pageStop }} của
                        {{ props.itemsLength }} kết quả
                    </template>
                </v-data-table>
            </v-card>
        </div>
    </v-form>
    <v-row v-show="isShow" v-if="updating" class="mt-5" align="center" justify="center">
        <v-btn @click="cancel" class="mr-2" elevation="1" color="warning darken-1" text>Hủy bỏ</v-btn>
        <v-btn @click="save" class="ml-2" elevation="1" color="primary darken-1" text>Xác nhận</v-btn>
    </v-row>

    <Loading ref="loading" />
    <ToastMessage class="bottom-toast-mess" ref="toastMessage" />
    <ImportExcelGradebookDialog ref="importExcelGradebookDialog" :items="dataConfigScoreForDialog" />
    <GradebookEvaluateDialog ref="gradebookEvaluateDialog" />
</div>
</template>

<script>
import AppService from "@/services/app.service";
import Loading from "@/components/Loading.vue";
import ToastMessage from "@/components/ToastMessage";
import ImportExcelGradebookDialog from "@/views/Dialogs/ImportExcelGradebookDialog";
import GradebookEvaluateDialog from "@/views/Dialogs/GradebookEvaluateDialog";

export default {
    name: "GradeBook",
    components: {
        ToastMessage,
        Loading,
        ImportExcelGradebookDialog,
        GradebookEvaluateDialog
    },
    data() {
        return {
            isShow: true,
            amountOfSemester: 0,
            objectSearch: {},
            choosedMon: "",
            choosedHocKy: "",
            choosedLop: "",
            dataLop: [],
            dataMon: [],
            rules: [(v) => !!v || "Không có lớp nào"],
            desserts: [
                // {
                //   stt: 1,
                //   studentCode : "abc",
                //   "studentName": "Tran Duc Thanh",
                //   "d1conf_score_details202199_017137_06a1a6cd-80ed-474c-8d1c-d5755c6d44e1": 8,
                //   "d1conf_score_details202199_017137_381be8b5-480d-4afa-b368-05271b77e96c": 10,
                //   "d2conf_score_details202199_017137_381be8b5-480d-4afa-b368-05271b77e96c": 8.5,
                //   "d1conf_score_details202199_017137_f7a98837-7a18-4eb5-b068-2cdd78c843a7": 9.6,
                //   "d2conf_score_details202199_017137_f7a98837-7a18-4eb5-b068-2cdd78c843a7": 10,
                //   "d1conf_score_details202199_017137_3331ce07-a5dd-4e04-98af-706230d9b4c5": 8.3,
                //   "average": 9.3
                // }
            ],
            defaultHeader: [{
                    text: "STT",
                    value: "stt",
                    align: "center",
                    width: "55px"
                },
                {
                    text: "Mã học sinh",
                    value: "studentCode",
                    align: "center",
                    width: "60px",
                },
                {
                    text: "Tên học sinh",
                    value: "studentName"
                },
            ],
            headers: [{
                    text: "STT",
                    value: "stt",
                    align: "center"
                },
                {
                    text: "Mã học sinh",
                    value: "studentCode",
                    align: "center"
                },
                {
                    text: "Tên học sinh",
                    value: "studentName"
                }
            ],
            classNameSearched: "",
            subjectNameSearched: "",
            customHeader: [],
            typeHeader: "score",
            selectValue: [],
            updating: false,
            rulesNumber: [
                (v) =>
                !v ?
                true :
                !isNaN(v) ?
                v >= 0 && v <= 10 ?
                true :
                false || "[0, 10]" :
                false || "Sai định dạng",
            ],
            rowChange: [],
            dessertsFirst: [],
            dataConfigScoreForDialog: [],
            oldSearch: {},
            configScore: null,
            canUpdate: false,
        };
    },
    async mounted() {
        if (!this.$store.getters['user'].roles.includes('ROLE_GVBM')) {
            this.$refs["toastMessage"].open("Bạn không phải là giáo viên bộ môn", true);
            this.isShow = false
            return
        }
        this.isShow = true
        this.$refs['loading'].open()
        await this.getAmountOfSemester();
        await this.getListClassRoomByLoginUser();
        await this.getListSubjectByLoginUser();
        await this.search();
        this.$refs['loading'].close()
    },
    methods: {
        getAmountOfSemester() {
            return AppService.getAmountOfSemester(this.$store.getters["year"])
                .then((result) => {
                    this.amountOfSemester = result.data.semesterAmount;
                    if (this.$route.params.classCode && !this.firstLoadDone) {
                        this.chooseHocKy(this.$route.params.semester);
                    } else {
                        this.chooseHocKy(0);
                    }
                })
                .catch(() => {});
        },
        formatKyHoc(item) {
            switch (item) {
                case 0: {
                    return "Cả năm";
                }
                case 1: {
                    return "Học kỳ I";
                }
                case 2: {
                    return "Học kỳ II";
                }
                case 3: {
                    return "Học kỳ III";
                }
                case 4: {
                    return "Học kỳ IV";
                }
            }
            return null;
        },
        chooseHocKy(item) {
            this.objectSearch.hocKy = item;
            switch (parseInt(item)) {
                case 0: {
                    this.choosedHocKy = "Cả năm";
                    break;
                }
                case 1: {
                    this.choosedHocKy = "Học kỳ I";
                    break;
                }
                case 2: {
                    this.choosedHocKy = "Học kỳ II";
                    break;
                }
                case 3: {
                    this.choosedHocKy = "Học kỳ III";
                    break;
                }
                case 4: {
                    this.choosedHocKy = "Học kỳ IV";
                    break;
                }
            }
            this.firstLoadDone = true;
        },
        chooseLop(item) {
            this.objectSearch.classCode = item.class_code;
            this.choosedLop = item.name;
        },
        chooseMon(item) {
            this.objectSearch.subjectCode = item.subject_code;
            this.choosedMon = item.name;
        },
        async search() {
            if (this.$refs["form-search"].validate()) {
                this.$refs["loading"].open();
                return AppService.searchSoDiem({
                        subjectCode: this.objectSearch.subjectCode,
                        classCode: this.objectSearch.classCode,
                        schoolYear: this.$store.getters["year"],
                        semester: this.objectSearch.hocKy,
                    })
                    .then((res) => {
                        if (res.data.status === 'OK') {
                            this.canUpdate = res.data.data.canUpdate
                            this.oldSearch = JSON.parse(JSON.stringify(this.objectSearch));
                            this.updating = false;
                            this.classNameSearched = this.choosedLop;
                            this.subjectNameSearched = this.choosedMon;
                            this.desserts = [];
                            this.customHeader = [];
                            this.rowChange = []
                            if (this.objectSearch.hocKy === 0) {
                                this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
                                this.headers.push({
                                    text: 'Điểm trung bình',
                                    value: 'avgScore'
                                })
                                this.desserts = res.data.data.AvgScore
                                this.desserts.forEach((x, index) => {
                                    x.stt = index + 1
                                })
                            } else {
                                this.headers = JSON.parse(JSON.stringify(this.defaultHeader));
                                this.configScore = res.data.data.ScoreConfig;
                                let scoreConfig = res.data.data.ScoreConfig;
                                this.dataConfigScoreForDialog = scoreConfig.map((x) => x);
                                if (this.dataConfigScoreForDialog.length >= 2) {
                                    this.dataConfigScoreForDialog.push(this.dataConfigScoreForDialog.splice(this.dataConfigScoreForDialog.findIndex(x => x.name == 'Điểm trung bình'), 1)[0]);
                                }
                                this.dataConfigScoreForDialog.forEach((x, index) => x.stt = index)
                                let config = {};
                                if (!scoreConfig[0].code.includes("grading")) {
                                    this.typeHeader = "score";
                                    for (let i = 0; i < scoreConfig.length; i++) {
                                        // totalCoff += (scoreConfig[i].quantity * scoreConfig[i].coefficient)
                                        config[scoreConfig[i].code] = scoreConfig[i].coefficient;
                                        for (let j = 1; j <= scoreConfig[i].quantity; j++) {
                                            this.customHeader.push({
                                                text: scoreConfig[i].name,
                                                value: "d" + j + scoreConfig[i].code,
                                                code: scoreConfig[i].code,
                                                time: j,
                                                align: "center",
                                                d: j,
                                                quantity: scoreConfig[i].quantity,
                                                width: "60px",
                                            });
                                            this.headers.push({
                                                text: "Đ" + j,
                                                value: "d" + j + scoreConfig[i].code,
                                                align: "center",
                                                width: "60px",
                                            });
                                        }
                                    }
                                    this.headers.push({
                                        text: "Điểm trung bình",
                                        value: "average",
                                        align: "center",
                                    });
                                    let scoreList = res.data.data.ScoreList;
                                    for (let i = 0; i < scoreList.length; i++) {
                                        // let avgScore = 0
                                        let obj = {};
                                        obj.stt = i + 1;
                                        obj.studentCode = scoreList[i].studentCode;
                                        obj.studentName = scoreList[i].studentName;
                                        obj.id = scoreList[i].id;
                                        obj.average = scoreList[i].avgScore;
                                        obj.code = scoreList[i].code;
                                        obj.parentCode = scoreList[i].parentCode;
                                        obj.subjectCode = scoreList[i].subjectCode;
                                        obj.evaluate = scoreList[i].evaluate;
                                        obj.studentId = scoreList[i].studentId;
                                        obj.classId = scoreList[i].classId;
                                        obj.classCode = scoreList[i].classCode;
                                        obj.updater = scoreList[i].updater;
                                        obj.status = scoreList[i].status;
                                        obj.listScore = [];
                                        let listScore = scoreList[i].listScore;
                                        for (let j = 0; j < listScore.length; j++) {
                                            obj["d" + listScore[j].times + listScore[j].scoreCode] =
                                                listScore[j].value === null ? null : listScore[j].value;
                                            obj.listScore.push({
                                                id: listScore[j].id,
                                                code: listScore[j].code,
                                                parentCode: listScore[j].parentCode,
                                                scoreId: listScore[j].scoreId,
                                                scoreCode: listScore[j].scoreCode,
                                                scoreName: listScore[j].scoreName,
                                                times: listScore[j].times,
                                                value: listScore[j].value,
                                                type: listScore[j].type,
                                                coeficient: listScore[j].coeficient,
                                                quantity: listScore[j].quantity,
                                                idStudent: listScore[j].idStudent,
                                                subjectCode: listScore[j].subjectCode,
                                                years: listScore[j].years,
                                                semester: listScore[j].semester,
                                                minimumScore: listScore[j].minimumScore,
                                                typeChoose: listScore[j].typeChoose,
                                                selectedValue: listScore[j].selectedValue,
                                            });
                                            // avgScore += ( config[listScore[j].scoreCode] * listScore[j].value )
                                        }
                                        this.desserts.push(obj);
                                    }
                                } else {
                                    this.typeHeader = "grade";
                                    let scoreList = res.data.data.ScoreList;
                                    scoreConfig.forEach((x) => {
                                        this.customHeader.push({
                                            text: x.name,
                                            value: x.name,
                                        });
                                        this.headers.push({
                                            text: x.name,
                                            value: x.name,
                                        });
                                    });
                                    if (this.customHeader.length > 1) {
                                        this.customHeader.push(this.customHeader.shift())
                                    }
                                    for (let i = 0; i < scoreList.length; i++) {
                                        let obj = {};
                                        obj.stt = i + 1;
                                        obj.studentCode = scoreList[i].studentCode;
                                        obj.studentName = scoreList[i].studentName;
                                        obj.id = scoreList[i].id;
                                        obj.average = scoreList[i].avgScore;
                                        obj.code = scoreList[i].code;
                                        obj.parentCode = scoreList[i].parentCode;
                                        obj.subjectCode = scoreList[i].subjectCode;
                                        obj.evaluate = scoreList[i].evaluate;
                                        obj.studentId = scoreList[i].studentId;
                                        obj.classId = scoreList[i].classId;
                                        obj.classCode = scoreList[i].classCode;
                                        obj.updater = scoreList[i].updater;
                                        obj.status = scoreList[i].status;
                                        let listScore = scoreList[i].listScore;
                                        for (let j = 0; j < listScore.length; j++) {
                                            obj[listScore[j].scoreName] = listScore[j].value;
                                            if (listScore[j].typeChoose == 1) {
                                                obj[listScore[j].scoreName + "selectedValue"] =
                                                    listScore[j].selectedValue;
                                                // obj[listScore[j].scoreName + "selectedValue"].unshift(
                                                //   "--- Lựa chọn ---"
                                                // );
                                            } else {
                                                obj[listScore[j].scoreName + "selectedValue"] = [];
                                            }
                                        }
                                        obj.listScore = scoreList[i].listScore
                                        this.desserts.push(obj);
                                    }
                                }
                                this.dessertsFirst = JSON.parse(JSON.stringify(this.desserts));
                            }
                        } else {
                            this.dataConfigScoreForDialog = [];
                            this.subjectNameSearched = "";
                            this.classNameSearched = ""
                            this.desserts = [];
                            this.customHeader = [];
                            this.selectValue = [];
                            this.canUpdate = false
                            this.oldSearch = {}
                            this.$refs['toastMessage'].open(res.data.message, true)
                        }
                    })
                    .catch(() => {
                        this.dataConfigScoreForDialog = [];
                        this.subjectNameSearched = "";
                        this.classNameSearched = ""
                        this.desserts = [];
                        this.customHeader = [];
                        this.selectValue = [];
                        this.canUpdate = false
                        this.oldSearch = {}
                        this.$refs["toastMessage"].open("Tìm kiếm thất bại", true);
                    })
                    .finally(() => {
                        this.rowChange = [];
                        this.$refs["loading"].close();
                    });
            } else {
                this.headers = [{
                        text: "STT",
                        value: "stt",
                        align: "center"
                    },
                    {
                        text: "Mã học sinh",
                        value: "studentCode",
                        align: "center"
                    },
                    {
                        text: "Tên học sinh",
                        value: "studentName"
                    }
                ]
                this.desserts = []
                this.dessertsFirst = []
                this.rowChange = []
                this.customHeader = []
                this.classNameSearched = null
                this.subjectNameSearched = null
                this.oldSearch = {}
                this.canUpdate = false
            }
        },
        getListClassRoomByLoginUser() {
            return AppService.getListClassRoomByLoginUser({
                    teacherCode: this.$store.getters["user"].username,
                    schoolYear: this.$store.getters["year"],
                    semester: this.objectSearch.hocKy,
                })
                .then((res) => {
                    this.dataLop = res.data.data.ClassRoom;
                    if (this.dataLop.length != 0) {
                        this.chooseLop(this.dataLop[0]);
                    } else {
                        this.chooseLop({
                            class_code: null,
                            name: null,
                        });
                    }
                })
                .catch(() => {
                    this.$refs["toastMessage"].open("Không có lớp nào", true);
                });
        },
        getListSubjectByLoginUser() {
            return AppService.getListSubjectByLoginUser({
                    teacherCode: this.$store.getters["user"].username,
                    classCode: this.objectSearch.classCode,
                    schoolYear: this.$store.getters["year"],
                    semester: this.objectSearch.hocKy,
                })
                .then((res) => {
                    this.dataMon = res.data.data.Subjects;
                    if (this.dataMon.length != 0) {
                        this.chooseMon(this.dataMon[0]);
                    } else {
                        this.chooseMon({
                            subject_code: null,
                            name: null,
                        });
                    }
                })
                .catch(() => {
                    this.$refs["toastMessage"].open("Không có môn nào", true);
                });
        },
        formatTextTooltip(item, max) {
            if (item.length > max) {
                return item.substring(0, max) + "...";
            }
            if (item.includes('null')) {
                return ''
            }
            return item;
        },
        importFile() {
            this.$refs["importExcelGradebookDialog"]
                .open(
                    this.objectSearch.subjectCode,
                    this.objectSearch.classCode,
                    this.objectSearch.hocKy,
                    this.typeHeader
                )
                .then((mess) => {
                    this.$refs["toastMessage"].open(mess, false);
                    this.search()
                })
                .catch(() => {});
        },
        update() {
            this.updating = true;
        },
        save() {
            if (this.$refs["form"].validate()) {
                this.$refs["loading"].open();
                let set = new Set(this.rowChange);
                let arrSet = [...set];
                if (this.typeHeader == "score") {
                    //Tính điểm trung bình trước khi save
                    for (let i = 0; i < this.desserts.length; i++) {
                        let obj = this.desserts[i];
                        if (obj.average) {
                            continue
                        }
                        let isEnough = false
                        let totalScore = 0
                        let totalCoff = 0
                        //check xem đủ đầu điểm chưa để tính điểm tb
                        for (let i = 0; i < this.configScore.length; i++) {
                            let quantity = this.configScore[i].minimumScore
                            let count = 0
                            for (let j = 0; j < obj.listScore.length; j++) {
                                if (obj.listScore[j].scoreCode == this.configScore[i].code && obj.listScore[j].value !== null && obj.listScore[j].value !== '') {
                                    count++;
                                    totalScore += (this.configScore[i].coefficient * obj.listScore[j].value)
                                }
                            }
                            if (count >= quantity) {
                                totalCoff += (this.configScore[i].coefficient * count)
                                isEnough = true
                            } else {
                                isEnough = false
                                break;
                            }
                        }
                        if (isEnough) {
                            this.rowChange.push(i)
                            obj.average = (totalScore / totalCoff).toFixed(2)
                        } else {
                            obj.average = null
                        }
                    }
                    set = new Set(this.rowChange);
                    arrSet = [...set]
                }
                if (this.typeHeader == "score") {
                    let subjectsDetails = [];
                    arrSet.forEach((x) => {
                        let listScoreChange = [];
                        this.desserts[x].listScore.forEach((ele) => {
                            listScoreChange.push({
                                id: ele.id,
                                code: ele.code,
                                parentCode: ele.parentCode,
                                scoreId: ele.scoreId,
                                scoreCode: ele.scoreCode,
                                scoreName: ele.scoreName,
                                times: ele.times,
                                value: ele.value,
                                type: ele.type,
                                coeficient: ele.coeficient,
                                quantity: ele.quantity,
                                idStudent: ele.idStudent,
                                subjectCode: ele.subjectCode,
                                years: ele.years,
                                semester: ele.semester,
                                minimumScore: ele.minimumScore,
                                typeChoose: ele.typeChoose,
                                selectedValue: ele.selectedValue,
                            });
                        });
                        subjectsDetails.push({
                            id: this.desserts[x].id,
                            code: this.desserts[x].code,
                            studentCode: this.desserts[x].studentCode,
                            studentName: this.desserts[x].studentName,
                            parentCode: this.desserts[x].parentCode,
                            avgScore: this.desserts[x].average,
                            subjectCode: this.desserts[x].subjectCode,
                            evaluate: this.desserts[x].evaluate,
                            studentId: this.desserts[x].studentId,
                            classId: this.desserts[x].classId,
                            classCode: this.desserts[x].classCode,
                            updater: this.desserts[x].updater,
                            status: this.desserts[x].status,
                            listScore: listScoreChange,
                        });
                    });
                    AppService.saveSoDiem({
                                schoolYear: this.$store.getters["year"],
                                semester: this.oldSearch.hocKy,
                                classCode: this.oldSearch.classCode,
                                subjectsDetails: subjectsDetails,
                                subjectCode: this.oldSearch.subjectCode
                            },
                            this.$store.getters["user"].username
                        )
                        .then((res) => {
                            this.$refs["toastMessage"].open(res.data.message, false);
                            this.dessertsFirst = JSON.parse(JSON.stringify(this.desserts));
                            this.updating = false
                            this.rowChange = []
                        })
                        .catch((res) => {
                            this.$refs["toastMessage"].open(res.message, true);
                        })
                        .finally(() => {
                            this.$refs["loading"].close();
                        });
                } else {
                    let subjectsDetails = [];
                    arrSet.forEach((x) => {
                        let listScoreChange = [];
                        this.desserts[x].listScore.forEach((ele) => {
                            listScoreChange.push({
                                id: ele.id,
                                code: ele.code,
                                parentCode: ele.parentCode,
                                scoreId: ele.scoreId,
                                scoreCode: ele.scoreCode,
                                scoreName: ele.scoreName,
                                times: 1,
                                value: ele.value,
                                type: ele.type,
                                coeficient: ele.coefficient,
                                quantity: ele.quantity,
                                idStudent: ele.idStudent,
                                subjectCode: ele.subjectCode,
                                years: ele.years,
                                semester: ele.semester,
                                typeChoose: ele.typeChoose,
                                selectedValue: ele.selectedValue
                            });
                        });
                        subjectsDetails.push({
                            id: this.desserts[x].id,
                            code: this.desserts[x].code,
                            studentCode: this.desserts[x].studentCode,
                            studentName: this.desserts[x].studentName,
                            parentCode: this.desserts[x].parentCode,
                            avgScore: this.desserts[x].average,
                            subjectCode: this.desserts[x].subjectCode,
                            evaluate: this.desserts[x].evaluate,
                            studentId: this.desserts[x].studentId,
                            classId: this.desserts[x].classId,
                            classCode: this.desserts[x].classCode,
                            updater: this.desserts[x].updater,
                            status: this.desserts[x].status,
                            listScore: listScoreChange,
                        })
                    })

                    AppService.saveSoDiem({
                                schoolYear: this.$store.getters["year"],
                                semester: this.oldSearch.hocKy,
                                classCode: this.oldSearch.classCode,
                                subjectsDetails: subjectsDetails,
                                subjectCode: this.oldSearch.subjectCode
                            },
                            this.$store.getters["user"].username
                        )
                        .then((res) => {
                            this.$refs["toastMessage"].open(res.data.message, false);
                            this.dessertsFirst = JSON.parse(JSON.stringify(this.desserts));
                            this.updating = false
                            this.rowChange = []
                        })
                        .catch((res) => {
                            this.$refs["toastMessage"].open(res.message, true);
                        })
                        .finally(() => {
                            this.$refs["loading"].close();
                        });

                }
            }
        },
        cancel() {
            this.updating = false;
            this.desserts = JSON.parse(JSON.stringify(this.dessertsFirst));
        },
        catchChange(index, code, time, score) {
            //gán giá trị mới
            for (let i = 0; i < this.desserts[index - 1].listScore.length; i++) {
                if (
                    this.desserts[index - 1].listScore[i].scoreCode == code &&
                    this.desserts[index - 1].listScore[i].times == time
                ) {
                    this.desserts[index - 1].listScore[i].value = score;
                    break;
                }
            }

            this.rowChange.push(index - 1);
            let obj = this.desserts[index - 1];
            let isEnough = false
            let totalScore = 0
            let totalCoff = 0
            //check xem đủ đầu điểm chưa để tính điểm tb
            for (let i = 0; i < this.configScore.length; i++) {
                let quantity = this.configScore[i].minimumScore
                let count = 0
                for (let j = 0; j < obj.listScore.length; j++) {
                    if (obj.listScore[j].scoreCode == this.configScore[i].code && obj.listScore[j].value !== null && obj.listScore[j].value !== '') {
                        count++;
                        totalScore += (this.configScore[i].coefficient * obj.listScore[j].value)
                    }
                }
                if (count >= quantity) {
                    totalCoff += (this.configScore[i].coefficient * count)
                    isEnough = true
                } else {
                    isEnough = false
                    break;
                }
            }
            if (isEnough) {
                obj.average = (totalScore / totalCoff).toFixed(2)
            } else {
                obj.average = null
            }
        },
        catchChangeGrade(stt, nameCol, value) {
            //type == true là lấy từ dropdownlist else là từ text field
            this.rowChange.push(stt - 1)
            for (let i = 0; i < this.desserts[stt - 1].listScore.length; i++) {
                if (this.desserts[stt - 1].listScore[i].scoreName == nameCol) {
                    this.desserts[stt - 1].listScore[i].value = value;
                    break;
                }
            }
        },
        openEvaluate() {
            AppService.getAllStudentOfClass({
                    year: this.$store.getters['year'],
                    class_code: this.oldSearch.classCode,
                    semester: this.oldSearch.hocKy,
                    subjectCode: this.oldSearch.subjectCode
                })
                .then((res) => {
                    if (res.data.status !== 'OK') {
                        this.$refs['toastMessage'].open(res.data.message, true)
                    } else {
                        this.$refs['gradebookEvaluateDialog'].open(res.data.data, this.oldSearch.classCode, this.oldSearch.hocKy, this.oldSearch.subjectCode)
                            .then((res) => {
                                this.$refs['toastMessage'].open(res, false)
                            })
                            .catch(() => {})
                    }
                })
                .catch(() => {
                    this.$refs['toastMessage'].open('Lấy thông tin học sinh của lớp thất bại', true)
                })
        }
    },
    watch: {
        async "$store.state.year"() {
            this.$refs["loading"].open();
            this.headers = [{
                    text: "STT",
                    value: "stt",
                    align: "center"
                },
                {
                    text: "Mã học sinh",
                    value: "studentCode",
                    align: "center"
                },
                {
                    text: "Tên học sinh",
                    value: "studentName"
                }
            ]
            this.desserts = []
            this.dessertsFirst = []
            this.rowChange = []
            this.customHeader = []
            this.classNameSearched = null
            this.subjectNameSearched = null
            await this.getAmountOfSemester();
            await this.getListClassRoomByLoginUser();
            this.$refs["loading"].close();
        },
        async choosedLop() {
            this.$refs["loading"].open();
            await this.getListSubjectByLoginUser();
            this.$refs["loading"].close();
        },
        async choosedHocKy() {
            this.$refs["loading"].open();
            await this.getListClassRoomByLoginUser();
            this.$refs["loading"].close();
        },
    },
};
</script>

<style>
.customHeaderSoDiem table tr:nth-child(3) th {
    border: 1px solid #dddddd;
}

.customHeaderSoDiem table td {
    border: 1px solid #dddddd;
}

.customHeaderSoDiem tbody tr:nth-child(even) {
    background: #eee;
}

.customHeaderSoDiemXepLoai table th {
    border: 1px solid #dddddd;
}

.customHeaderSoDiemXepLoai table td {
    border: 1px solid #dddddd;
}

.customHeaderSoDiemXepLoai tbody tr:nth-child(even) {
    background: #eee;
}

.selectGradingGradeBook .v-text-field__details {
    display: none;
}

.selectGradingGradeBook .v-input__slot {
    margin: 0px !important;
}

.customTableSemester0 table td,
.customTableSemester0 table th {
    border: 1px solid #dddddd;
}

.customTableSemester0 tbody tr:nth-child(even) {
    background: #eee;
}
</style>
