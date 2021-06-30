<template>
  <div id="app">
    <Mod v-if="isShow && isLogin" />
  </div>
</template>

<script>
import Mod from '../../components/Mod.vue'
import comjs from '../../common'

export default {
  name: 'app',
  components: {
    Mod
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
    },
    goIndex() {
      if(location.pathname != '/login.html') {
        location.href = '/login.html'
      }
    }
  },
  data() {
    return {
      isLogin: false,
      isShow: true,
      search: '',
    }
  },
  created() {
    let _this = this
    var ygg1412 = this.$cookies.get('ygg1412')
    comjs.isLogin(ygg1412, (v, msg) => {
      if (v === false) {
        _this.goIndex()
        _this.$message.error(msg)
      } else {
        this.isLogin = true
        console.log(v)
      }
    })
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
