<template>
  <div id="app">
    <template v-if="isShow">
      <AddTemplate v-if="hash == '#/template'" />
      <IndexPage v-else />
    </template>
  </div>
</template>

<script>
import IndexPage from './components/IndexPage.vue'
import AddTemplate from './components/AddTemplate.vue'

export default {
  name: 'app',
  components: {
    IndexPage,
    AddTemplate
  },
  provide(){
    return {
      reload: this.reload
    }
  },
  methods: {
    reload () {
      this.isShow = false
      this.$nextTick(()=>{
        this.isShow = true
      })
    }
  },
  data() {
    return {
      isShow: true,
      hash: '',
      search: '',
    }
  },
  created() {
    this.hash = location.hash
    this.search = location.search
  }
}
</script>

<style>
html,
body, #app {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  overflow-y: hidden;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.cfocus svg > g > :not(.tagHighlight) {
  opacity: 0.3 !important;
}
.cfocus svg > g > g:not(.tagHighlight) {
  opacity: 0.1 !important;
}
.dialog-index .el-dialog__body{
  padding-bottom: 0;
}
</style>
