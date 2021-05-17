<template>
  <div class="content">
    <div class="tags">
      <div style="display: flex;align-items: center;">
        <el-tag v-for="(it, i) in tags" :key="i"
          type="info" size="mini" effect="dark" style="margin-left:10px;cursor:pointer"
          @click="tagClick(it)"
          :color="it[0].style.fill" >{{i}}({{it.length}})</el-tag>
      </div>
    </div>
    <div class="left-content">
      <div style="height: 100%;">
        <el-scrollbar style="height:100%" wrap-style="overflow-x: hidden;" view-style="overflow-x: hidden;">
          <div class="header">知识图谱</div>
          <div class="pdtop15">
            <el-input size="mini" placeholder="搜索" clearable v-model="input1" @keyup.native.enter="search">
              <el-button slot="append" type="primary" size="mini" @click="search">搜索</el-button>
            </el-input>
          </div>
          <div class="pdtop15" v-show="dataList && dataList.length > 0">
            <div class="title1">查询结果：</div>
            <div style="height: 180px;">
                <el-scrollbar style="height:100%" wrap-style="overflow-x: hidden;" view-style="overflow-x: hidden;">
                  <div v-for="(it, i) in dataList" :key="i"
                    :class="'data-list-row' + (activeI == i ? ' active' : '')"
                    @click="selectRow(i)"
                    >name: {{it.name}}</div>
                </el-scrollbar>
            </div>
          </div>
          <div class="pdtop15" v-show="tooltip">
            <div class="title1">properties：</div>
            <div v-for="(it, i) in (tooltip || {})" :key="i" style="padding-left: 8px;">{{i}}：{{it}}</div>
            <div class="pdtop15">
              <el-button size="mini" @click="modify">修改节点</el-button>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <div ref="svgDiv" id="svg" :class="(svgFocus ? 'cfocus' : '')"></div>
    
    <NodeDialog
      v-if="dialogVisible"
      :title="title"
      :formData="formData"
      :propertiesList="propertiesList"
      :disabled="false"
      @close="nodeDialogClose" @ok="nodeDialogOk" />
  </div>
</template>

<script>
import DrawForce from "@/plugins/drawForce"
import axios from 'axios'
import NodeDialog from './NodeDialog'

// 获取query并去重
function getQuery(u) {
  var a = (u || '').split('?')
  if (a.length < 2) {
    return {}
  }
  var aa = a[1]
  var bb = aa.split('&')
  var cc = {}
  for (var b of bb) {
    var c = b.split('=')
    cc[c[0]] = c[1]
  }
  return cc
}

let timer
export default {
  name: "IndexPage",
  components: {NodeDialog},
  inject: ['reload'],
  data() {
    return {
      nodes: [],
      links: [],
      tags: {},
      propertiesList: [
        {key: 'name', label:['name']},
        {key: 'identNum', label:['身份证']},
        {key: 'policyNum', label:['保单', '保单号']},
        {key: 'stard', label:['起期', '保险起期']},
        {key: 'endd', label:['止期', '保险止期']},
        {key: 'accd', label:['出险', '出险时间']},
        {key: 'payNum', label:['银行卡']},
        {key: 'reportD', label:['报案', '报案时间']},
        {key: 'reportNum', label:['报案电话']}
      ],
      input1: '',
      dataList: [],
      nodeData: null,
      tooltip: null,
      activeI: '',
      svgFocus: false,
      dialogVisible: false,
      title: '',
      d: null
    }
  },
  methods: {
    nodeDialogClose() {
      this.dialogVisible = false
    },
    nodeDialogOk() {
      this.nodeDialogClose()
    },
    modify() {
      this.formData = this.nodeData || {}
      this.title = "修改节点"
      this.dialogVisible = true
    },
    // 点击标签时的处理
    tagClick(it) {
      this.svgFocus = true
      this.selectRow('null')
      this.removeTagHighlight()
      let arr2 = document.getElementsByClassName("i"+it[0].id)
      for (let i = arr2.length - 1; i > -1; i--) {
        arr2[i].classList.add('tagHighlight')
      }
    },
    // 清空高亮的块
    removeTagHighlight() {
      let arr = document.getElementsByClassName('tagHighlight')
      for (let i = arr.length - 1; i > -1; i--) {
        arr[i].classList.remove('tagHighlight')
      }
    },
    // 按name搜索
    search() {
      if(!this.d)return
      if(!this.input1) {
        this.dataList = []
        this.selectRow('null')
        return
      }
      this.dataList = this.d.search(this.input1, ['name']) // 查询name相同的数据
      this.selectRow(this.dataList.length ? 0 : 'null')
    },
    // 选中一行
    selectRow(i) {
      let d = this.d
      let dataList = this.dataList
      this.activeI = null
      d.highlightObject()
      if (dataList && dataList[i]) {
        d.highlightObject(dataList[i])
        d.nodeData = dataList[i]
        this.drawTooltip(d.nodeData, 'selectRow')
        this.activeI = i
      }
    },
    // 更新Tooltip信息
    drawTooltip(data) {
      this.nodeData = data
      if (data && data.properties && Object.keys(data.properties).length > 0) {
        this.tooltip = data.properties
      } else {
        this.tooltip = null
      }
    },
    // 点击svg时的处理
    svgClick(data) {
      this.d.highlightObject()
      this.drawTooltip(data)
      this.svgFocus = false
      this.selectRow("null")
    },
    // 初始化D3
    startD3(nodes, links ) { // tags
      this.d = DrawForce.init('svg', {
        width: window.document.body.offsetWidth,
        height: window.document.body.offsetHeight,
        nodes, links,
        svgClick: this.svgClick
      })
    },
    reset() {
      this.dataList = []
      this.tooltip = null
      this.$refs.svgDiv.innerHTML = ''
    },
    windowResize() {
      if(timer)clearTimeout(timer);
      timer = setTimeout(() => {
        this.reset()
        timer=undefined
        this.startD3(this.nodes, this.links, this.tags)
      }, 300)
    },
    initData() {
      let vm = this
      let query = getQuery(location.href)
      if (query.model) {
        axios.get('/showUserModelKg' + location.search)
          .then(function (response) {
            if(response.status != 200 || response.data.error) {
              vm.$message.error(response.data.error || response.statusText)
              return
            }
            let json = response.data
            vm.setData(json)
          })
          .catch(function (error) {
            console.log(error)
          });
        return
      }
      axios.get('/showUserKg' + location.search)
      .then(function (response) {
        if(response.status != 200 || response.data.error) {
          vm.$message.error(response.data.error || response.statusText)
          return
        }
        let json = response.data
        vm.setData(json)
      })
      .catch(function (error) {
        console.log(error)
      });
    },
    setData(data){
      let vm = this
      let nodes = data.nodes || []
      let links = data.edges || []
      let tags = {}
      nodes.forEach((element) => {
        if (element.type) {
          tags[element.type] = tags[element.type] || []
          tags[element.type].push(element)
          element.typeKey = tags[element.type][0].id // 取第一个id当相同的key
        }
      });
      vm.nodes = nodes
      vm.links = links
      vm.tags = tags
      vm.startD3(nodes, links, tags)
    },
  },
  mounted() {
    this.initData()
    window.addEventListener('resize', this.windowResize)
  },
  destroyed() {
    window.removeEventListener('resize', this.windowResize)
  }
};
</script>

<style scoped>
.content {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  overflow-y: hidden;
  background-color: #2e2d2d;
  color: #fff;
}
.pdtop15 {
  padding-top: 15px;
}
.title1 {
  font-size: 18px;
  color: #b7b3b3;
  border-top: 1px solid #777;
  padding-bottom: 8px;
}
#svg {
  height: 100%;
  width: 100%;
}
.left-content {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  width: 300px;
  padding: 10px;
  border-right: 1px solid #555;
  background-color: #ffffff0f;
}
.header {
  font-size: 30px;
  font-weight: bolder;
}
.data-list-row {
  padding: 4px 8px;
  cursor: pointer;
  word-break: break-all;
}
.data-list-row.active {
  background-color: #5d5d5d;
  border-radius: 4px;
}
.tags {
  position: fixed;
  top: 0;
  left: 320px;
  right: 0;
  padding: 10px;
  min-height: 20px;
  border-bottom: 1px solid #555;
  background-color: #ffffff0f;
  display: flex;
  justify-content: space-between;
}

</style>
