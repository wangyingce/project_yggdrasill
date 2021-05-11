<template>
  <div id="app">
    <template v-if="isShow && isLogin">
      <AddTemplate v-if="hash == '#/template'" />
      <IndexPage v-else />
    </template>
  </div>
</template>

<script>
import IndexPage from './components/IndexPage.vue'
import AddTemplate from './components/AddTemplate.vue'
import axios from "axios";

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
    },
    goIndex() {
      if(location.pathname != '/static/index.html') {
        location.href = '/static/index.html'
      }
    }
  },
  data() {
    return {
      isLogin: false,
      isShow: true,
      hash: '',
      search: '',
    }
  },
  created() {
    let _this = this
    var ygg1412 = this.$cookies.get('ygg1412')
    if(!ygg1412) {
      _this.goIndex()
      _this.$message.error('未登录')
    } else {
      var formData = new FormData()
      formData.append('property', ygg1412)
      axios.post('/initUserInfo', formData)
      .then(function (response) {
        let data = response.data || {}
        if(response.status != 200) {
          if(data.sus != null) {
            this.isLogin = true
						console.log(data)
					} else {
            _this.$cookies.remove('ygg1412')
						_this.goIndex()
						_this.$message.error(data.error || '请求失败')
					}
        } else {
          _this.$cookies.remove('ygg1412')
          _this.goIndex()
          _this.$message.error(data.error || '请求失败')
        }
      })
      .catch(function () {
        _this.$cookies.remove('ygg1412')
        _this.goIndex()
        _this.$message.error('请求失败')
      });
    }
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
