<template>
  <v-menu offset-y>
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-on="on"
        v-bind="attrs"
        width="100%"
        append-icon="mdi-chevron-down"
        :label="label"
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
</template>
<script>

export default {
    name: 'DropDown',
    props: ['label', 'data', 'type', 'prevDataUpdate'],
    data() {
        return{
            choosed : null,
            rules: [
              v => !!v || 'Hãy chọn một lựa chọn',
            ],
        }
    },
    mounted(){
      if(this.prevDataUpdate){
        this.choosed = this.prevDataUpdate
      }
    },
    watch: {
        choosed(){
            this.$emit('choosedDropdown', {
                type : this.type,
                choosed : this.choosed
            })
        },
        prevDataUpdate(){
          this.choosed = this.prevDataUpdate
        }
    }
}
</script>

<style scoped>

</style>