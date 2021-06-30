<template>
  <div>
    <el-row v-for="(v ,i) in values" :key="i">
      <el-col :span="6">
        <el-select v-if="showPropList" v-model="v.key" placeholder="key" filterable>
          <el-option v-for="(v, i) in propList" :key="i" :label="v.label[0]" :value="v.label[0]"></el-option>
        </el-Select>
        <el-input v-else v-model="v.key" placeholder="key"></el-input>
      </el-col>
      <el-col :span="16">
        <el-input v-model="v.value" placeholder="请输入value"></el-input>
      </el-col>
      <el-col :span="2" style="text-align:right;">
        <el-button :disabled="values.length < 2" type='text' style="margin-left:5px;" @click="del(v.key)">删除</el-button>
      </el-col>
    </el-row>
    
    <el-row :style="{paddingTop: values.length > 0 ? '8px' : '0'}">
      <el-col :span="20" >
        <el-button type='text' style="margin-left:5px;" @click="add">增加</el-button>
      </el-col>
    </el-row>
    <div style="padding:8px">
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  export default {
  name: "input-prop",
  props: {
    value: Object,
    showPropList: {
      type: Boolean,
      default: true
    },
    propList: {
      type: Array,
      default: ()=>{
        return []
      }
    },
    nid:String
  },
  data() {
    return {
      values:[]
    }
  },
  watch:{
    value(nv) {
      if (nv) {
        this.setValues(nv)
      }
    }
  },
  mounted(){
    this.setValues(this.value)
  },
  methods: {
    setValues(nv) {
      let values = []
      for (const key in nv) {
        if (Object.hasOwnProperty.call(nv, key)) {
          const it = nv[key];
          values.push({
            key: key,
            value: it
          })
        }
      }
      this.values = values
    },
    emitInput() {
      let value = {}
      for (let i = 0; i < this.values.length; i++) {
        const v = this.values[i];
        if (!v.key) {
          this.$message.error("key不能为空")
          return true
        }
        if (/^\d+/.test(v.key)) {
          this.$message.warning('key不能以数字为开头')
          return true
        }
        if (!v.value) {
          this.$message.error("value不能为空")
          return true
        }
        if (value[v.key] != undefined) {
          this.$message.error("key不能重复")
          return true
        }
        value[v.key] = v.value
      }
      this.$emit('input', value)
    },
    add() {
      this.values.push({key:'',value: ''})
    },
    del(i) {
      let vm = this;
      this.$message.error(this.nid)
      axios.get("/delNodeProperty", {
                params: {
                  id: vm.nid,
                  property: i
                },
              }).then(function (response) {
                if(response.status != 200 || response.data.error) {
                  vm.$message.error(response.data.error || response.statusText)
                  return
                }
                vm.$message.success("删除成功");
              }).catch(function (error) {
                console.log(error)
              });
      this.values.splice(i, 1)
    }
  }
}
</script>

<style>

</style>