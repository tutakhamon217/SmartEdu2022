export const schoolYearMixin = {
    name: "SchoolYearMixin",
    data() {
        return {
            existKy1: false,
            existKy2: false,
            dataKy2: [
                {
                    text: "STT",
                    align: "start",
                    sortable: false,
                    value: "stt",
                },
                {text: "Năm học", value: "nam_hoc"},
                {text: "Số học kỳ", value: "so_hoc_ky"},
                {text: "Bắt đầu học kỳ I", value: "bat_dau_hoc_ky_1"},
                {text: "Kết thúc học kỳ I", value: "ket_thuc_hoc_ky_1"},
                {text: "Bắt đầu học kỳ II", value: "bat_dau_hoc_ky_2"},
                {text: "Kết thúc học kỳ II", value: "ket_thuc_hoc_ky_2"},
                {text: "Thao tác", value: "actions"},
            ],
            dataKy1: [
                {
                    text: "STT",
                    align: "start",
                    sortable: false,
                    value: "stt",
                },
                {text: "Năm học", value: "nam_hoc"},
                {text: "Số học kỳ", value: "so_hoc_ky"},
                {text: "Bắt đầu học kỳ I", value: "bat_dau_hoc_ky_1"},
                {text: "Kết thúc học kỳ I", value: "ket_thuc_hoc_ky_1"},
                {text: "Thao tác", value: "actions"},
            ],
        }
    },
    methods: {
        checkExistKy12() {
            this.existKy1 = false;
            this.existKy2 = false;
            this.desserts.forEach((element) => {
                if (element.ket_thuc_hoc_ky_1) {
                    this.existKy1 = true;
                    if (element.ket_thuc_hoc_ky_2) {
                        this.existKy2 = true;
                        return;
                    }
                }
            });
            if (!this.existKy1 && !this.existKy2) {
                this.$emit('changeHeader', [])
            }
        },
        dateRange(startDate, endDate) {
            //ref: https://stackoverflow.com/questions/30464628/javascript-get-all-months-between-two-dates
            var start = startDate.split('-');
            var end = endDate.split('-');
            var startYear = parseInt(start[0]);
            var endYear = parseInt(end[0]);
            var dates = [];

            for (var i = startYear; i <= endYear; i++) {
                var endMonth = i != endYear ? 11 : parseInt(end[1]) - 1;
                var startMon = i === startYear ? parseInt(start[1]) - 1 : 0;
                for (var j = startMon; j <= endMonth; j = j > 12 ? j % 12 || 11 : j + 1) {
                    var month = j + 1;
                    var displayMonth = month < 10 ? '0' + month : month;
                    var startDay = 1
                    if(i == startYear && j == startMon)
                    {
                        startDay = parseInt(start[2])
                    }
                    //var endDate = (i == endYear && j == endMonth) ? parseInt(end[2]) : new Date(i, parseInt(displayMonth), 0).getDate()
                    // if(i == endYear && j == endMonth)
                    // {
                    //     endDate = parseInt(end[2])
                    // }
                    dates.push({"year": i, "month":parseInt(displayMonth), "totalDate": new Date(i, parseInt(displayMonth), 0).getDate(), startDate: startDay, endDate: (i == endYear && j == endMonth) ? parseInt(end[2]) : new Date(i, parseInt(displayMonth), 0).getDate()});
                }
            }
            return dates;
        },
        formatDate(date) {
            if (!date) return null;
            const [year, month, day] = date.split("-");
            return `${day}-${month}-${year}`;
        },
        formatDateRevert(date) {
            if (!date) return null;
            const [day, month, year] = date.split("-");
            return `${year}-${month}-${day}`;
        }
    }
}