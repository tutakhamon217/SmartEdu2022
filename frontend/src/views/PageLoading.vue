<template>
    <div class="PageLoading" v-if="count > 0">
        <!-- <Loading1></Loading1> -->
    </div>
</template>

<script>
// import Loading1 from "@/components/Loading1";

export default {
    name: "PageLoading",
    // components: { Loading1 },
    data () {
        return {
            count: 0
        }
    },
    methods: {
        open () {
            this.count++
        },
        close () {
            this.count--
            if (this.count < 0) {
                this.count = 0
            }
        }
    }
}
</script>

<style scoped>
    .PageLoading {
        width: 100vw;
        height: 100vh;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
        position: fixed;
    }
</style>
