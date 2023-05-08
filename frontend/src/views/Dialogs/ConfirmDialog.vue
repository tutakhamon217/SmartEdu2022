<template>
  <div class="ConfirmDialog">
    <v-dialog v-model="show" max-width="290" >
      <v-card>
        <v-card-title class="text-h6"
          >{{title}}</v-card-title
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="cancel()">Hủy bỏ</v-btn>
          <v-btn color="blue darken-1" text @click="accept()"
            >Xác nhận</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

export default {
  name: "ConfirmDialog",
  created () {
  },
  data() {
    return {
        show: false,
        title: ""
    };
  },

  methods: {
    open(title) {
        this.show = true
        this.title = title
        return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    cancel() {
        this.show = false;
        this.promise.reject();
    },
    accept() {
        this.show = false;
        this.promise.resolve()
    }
  },

};
</script>

<style scoped>
</style>
