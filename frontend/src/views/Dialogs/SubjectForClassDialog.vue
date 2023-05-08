<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="400">
      <v-card>
      <v-card-title
        style="background-color: rgb(26 118 207 / 1); color: white"
        class="justify-center py-2"
      >
        {{ headerMessage }}
      </v-card-title>
        <v-card-text>
          {{ content }}
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="warning darken-1" text @click="cancel"> Hủy bỏ </v-btn>
          <v-btn color="primary darken-1" text @click="save" > Đồng ý </v-btn>
          
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  props: ["headerMessage", "content"],
  data() {
    return {
      dialog: false,
      promise: null,
    };
  },
  methods: {
    open() {
      this.dialog = true;
      return new Promise((resolve, reject) => {
        this.promise = {
          resolve,
          reject,
        };
      });
    },
    save() {
      this.promise.resolve();
      this.dialog = false;
    },
    cancel() {
      this.promise.reject();
      this.dialog = false;
    },
  },
};
</script>

