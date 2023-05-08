<template>
  <v-card flat>
    <v-data-table
        :headers="headers"
        :items="desserts"
        :items-per-page="5"
        disable-sort
        :footer-props="{
            'items-per-page-text': 'Số dòng mỗi trang:',
        }"
    >
        <template v-slot:top>
            <v-toolbar flat color="primary">
                <v-toolbar-title style="width: 100%;align-items: center;" class="d-flex">
                    <v-icon color="white">mdi-arrow-up-drop-circle-outline</v-icon>
                    <span style="color: white">{{ txtTitle }}</span>
                    <v-spacer></v-spacer>
                    <slot></slot>
                </v-toolbar-title>
            </v-toolbar>
        </template>

        <template v-slot:item.actions="{ item }">
            <v-icon v-if="isEdit" small class="mr-2" @click="handleOption(item, 'edit')"> mdi-pencil </v-icon>
            <v-icon v-if="isDelete" small class="mr-2" @click="handleOption(item, 'delete')"> mdi-delete </v-icon>
        </template>
        <template v-slot:footer.page-text="props">
            {{ props.pageStart }}-{{ props.pageStop }} của {{ props.itemsLength }} kết quả
        </template>
    </v-data-table>
  </v-card>
</template>

<script>
export default {
  name: "DataTable",
  props: ['headers','desserts','txtTitle','isDelete','isEdit'],
  computed: {},
  mounted() {},
  methods: {
      handleOption(item, key){
          if(key === 'edit'){
            this.$emit('editItem', item)
          }else{
            this.$emit('deleteItem', item)
          }
      }
  },
}
</script>

<style scoped lang="scss">
</style>
