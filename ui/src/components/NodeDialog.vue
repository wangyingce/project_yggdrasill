<template>
  <div>
    <el-dialog
      class="dialog-index"
      visible
      :title="title"
      :close-on-click-modal="false"
      @close="close"
      destroy-on-close
      width="500px"
    >
      <el-form
        label-position="top"
        label-width="100px"
        ref="form"
        :model="form"
        :rules="rules"
        size="mini"
      >
        <!-- <el-form-item label="name" prop="name">
          <el-input v-model="form.name" placeholder="请输入name"></el-input>
        </el-form-item> -->
        <!-- <el-row> -->
        <!-- <el-col :span="24">
            <el-form-item label="type" prop="type">
              <el-input v-model="form.type" placeholder="请输入type"></el-input>
            </el-form-item>
          </el-col> -->
        <!-- <el-col :span="4">
            <el-form-item prop="color"  label-width="10px">
              <el-color-picker style="width:100%" v-model="form.color"></el-color-picker>
            </el-form-item>
          </el-col> -->
        <!-- </el-row> -->
        <el-form-item label="properties" prop="properties">
          <input-prop
            ref="inputProp"
            v-model="form.properties"
            :propList="propertiesList"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="close()">取 消</el-button>
        <el-button size="small" type="primary" @click="save()">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import InputProp from "./InputProp";

export default {
  components: { InputProp },
  props: {
    title: String,
    formData: Object,
    propertiesList: Array,
    disabled: Boolean,
  },
  inject: ["reload"],
  data() {
    return {
      form: {
        id: "",
        name: "",
        type: "",
        color: "",
        properties: {},
      },
      rules: {
        name: [{ required: true, message: "请输入name", trigger: "blur" }],
        type: [{ required: true, message: "请输入type", trigger: "blur" }],
        color: [{ required: true, message: "请输入color", trigger: "change" }],
        properties: [
          // { required: true, message: '请输入properties', trigger: 'change' },
        ],
      },
    };
  },
  methods: {
    getKey(label) {
      for (let i = 0; i < this.propertiesList.length; i++) {
        const it = this.propertiesList[i];
        if (it.label.includes(label)) {
          return it.key;
        }
      }
      return label;
    },
    close() {
      this.$emit("close");
    },
    // 是否可以保存数据的状态 修改properties时不可关闭
    save() {
      let vm = this;
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.$refs.inputProp.emitInput()) {
            return;
          }
          this.$nextTick(() => {
            vm.dialogVisible = false;
            // updateNode?id=1273196
            let p = {};
            for (const key in vm.form.properties) {
              if (Object.hasOwnProperty.call(vm.form.properties, key)) {
                p[vm.getKey(key)] = vm.form.properties[key];
              }
            }
            axios
              .get("/updateNode", {
                params: {
                  id: vm.form.id,
                  properties: JSON.stringify(p),
                },
              })
              .then(function () {
                vm.reload();
              })
              .catch(function () {
                vm.reload();
              });
          });
        }
      });
    },
  },
  mounted() {
    let data = this.formData || {};
    this.form = {
      ...data,
      properties: { ...(data.properties || {}) },
    };
  },
};
</script>

<style>
</style>