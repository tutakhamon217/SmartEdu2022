import AppService from "@/services/app.service"

export const actions = {
    updateYear({commit}, payload) {
        commit('updateYear', payload)
    },
    updateUser({commit}, payload) {
        commit('updateUser', payload)
    },
    getCurrentUser({commit}) {
        return AppService.getCurrentUser()
        .then((res) => {
            let data = {}
            let roles = []
            res.data.roles.forEach(element => {
                roles.push(element.authority)
            });
            data.roles = roles
            data.id = res.data.id
            data.username = res.data.username
            data.email = res.data.email
            data.phone = res.data.phone
            data.fullName = res.data.fullName
            data.avatar = res.data.avatar
            commit('updateUser', data)
        })
        .catch(() => {
            localStorage.removeItem('school_token')
        })
    },
    updateCurrentYear({ commit }, payload){
        commit('updateCurrentYear', payload)
    },
    updateCurrentSemester( {commit}, payload){
        commit('updateCurrentSemester', payload)
    }
}