<template>
<div :style="{visibility: hiddenDropDownYear ? 'hidden' : 'visible'}">
  <v-menu offset-y>
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-on="on"
        v-bind="attrs"
        style="max-width: 200px"
        append-icon="mdi-chevron-down"
        readonly
        v-model="choosed"
        :rules="rules"
      >
      </v-text-field>
    </template>
    <v-list style="max-height: 50vh; overflow-y: scroll;">
      <v-list-item
        v-for="(item, index) in data"
        :key="index"
        @click="choosed = item"
      >
        <v-list-item-title>{{ item }}</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</div>
</template>
<script>

export default {
    name: 'DropDownYear',
    props: ['label', 'data', 'hiddenDropDownYear'],
    data() {
        return{
            choosed : null,
            rules: [
              v => !!v || 'Hãy chọn một lựa chọn',
            ],
        }
    },
    mounted(){
      this.choosed = this.$store.getters["year"]
    },
    methods: {

    },
    watch: {
        choosed(){
          this.$store.dispatch('updateYear', this.choosed)
        },
        "$store.state.year"(){
          this.choosed = this.$store.getters["year"]
        }
    }
}
</script>

<style scoped>

</style>