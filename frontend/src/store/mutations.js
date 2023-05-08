export const mutations = {
    updateYear(state, payload) {
        state.year = payload
    },
    updateUser(state, payload) {
        state.user = payload
    },
    updateCurrentYear(state, payload){
        state.currentYear = payload
    },
    updateCurrentSemester(state, payload){
        state.currentSemester = payload
    }
}