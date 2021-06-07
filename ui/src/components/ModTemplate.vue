<template>
  <div class="content">
    <div class="tags">
      <div
        style="
          display: flex;
          align-items: center;
          justify-content: space-between;
          width: 100%;
        "
      >
        <div style="flex: 1">
          <el-button
            type="primary"
            class="button-new-tag"
            size="small"
            @click="open1()"
            >新建节点</el-button
          >
          <el-button
            type="primary"
            class="button-new-tag"
            size="small"
            style="margin-right: 20px"
            @click="open2()"
            >新建连线</el-button
          >
          <el-tag
            v-for="(it, i) in tags"
            :key="i"
            type="info"
            size="mini"
            effect="dark"
            style="margin-left: 10px; cursor: pointer"
            :color="it.style.fill"
            >{{ it.type }}</el-tag
          >
        </div>
        <div>
          <el-button
            type="success"
            class="button-new-tag"
            size="small"
            @click="open3()"
            >保存模板</el-button
          >
        </div>
      </div>
    </div>
    <div ref="svgDiv" id="svg" :class="svgFocus ? 'cfocus' : ''"></div>

    <el-dialog
      class="dialog-index"
      :visible="visible"
      title="节点信息"
      :close-on-click-modal="false"
      @close="close"
      destroy-on-close
      width="500px"
    >
      <el-form
        label-width="100px"
        ref="tagform"
        :model="tagform"
        :rules="tagRules"
        size="mini"
      >
        <el-form-item label="节点类型" prop="type">
          <el-input
            maxlength="50"
            v-model="tagform.type"
            placeholder="请输入节点类型"
            @blur="setColor"
          ></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="properties" prop="properties">
              <input-prop
                ref="inputProp"
                :showPropList="false"
                v-model="tagform.properties"
              />
              <!-- <el-select
                style="width: 100%"
                v-model="tagform.properties"
                multiple
                filterable
                allow-create
                default-first-option=""
                no-data-text="录入属性名，按回车键填充"
                placeholder="请录入节点属性"
              ></el-select> -->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="填充颜色" prop="fill">
              <el-color-picker
                :predefine="predefineColors"
                style="width: 100%"
                v-model="tagform.fill"
              ></el-color-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="描边颜色" prop="stroke">
              <el-color-picker
                style="width: 100%"
                v-model="tagform.stroke"
              ></el-color-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="close()">取 消</el-button>
        <el-button v-if="tagform.id" size="small" @click="delNode()"
          >删 除</el-button
        >
        <el-button size="small" type="primary" @click="ok()">保 存</el-button>
      </span>
    </el-dialog>
    <el-dialog
      class="dialog-index"
      :visible="visible2"
      title="连线信息"
      :close-on-click-modal="false"
      @close="close2"
      destroy-on-close
      width="500px"
    >
      <div>
        <el-row v-for="(v, i) in tempLinks" :key="i" :gutter="8">
          <el-col :span="7">
            <el-select v-model="v.source" size="mini" placeholder="source">
              <el-option
                v-for="(v, i) in nodes"
                :key="i"
                :label="v.name"
                :value="v.id"
              ></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-input
              maxlength="50"
              v-model="v.type"
              size="mini"
              placeholder="关系描述"
            ></el-input>
          </el-col>
          <el-col :span="7">
            <el-select v-model="v.target" size="mini" placeholder="target">
              <el-option
                v-for="(v, i) in nodes"
                :key="i"
                :label="v.name"
                :value="v.id"
              ></el-option>
            </el-select>
          </el-col>
          <el-col :span="2">
            <el-color-picker
              size="mini"
              style="width: 100%"
              v-model="v.color"
            ></el-color-picker>
          </el-col>
          <el-col :span="2">
            <el-button size="mini" type="text" @click="delLink(i)"
              >删除</el-button
            >
          </el-col>
        </el-row>
        <div>
          <el-button type="text" size="small" @click="addLink"
            >新增连线</el-button
          >
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="close2()">取 消</el-button>
        <el-button size="small" type="primary" @click="ok2()">保 存</el-button>
      </span>
    </el-dialog>
    <el-dialog
      class="dialog-index"
      :visible="visible3"
      title="保存模板"
      :close-on-click-modal="false"
      @close="close3"
      destroy-on-close
      width="400px"
    >
      <el-form
        label-width="70px"
        ref="modelform"
        :model="modelForm"
        :rules="modelRules"
        size="mini"
      >
        <el-form-item label="节点类型" prop="modelname">
          <el-input
            maxlength="50"
            v-model="modelForm.modelname"
            placeholder="请输入节点类型"
            @blur="setColor"
          ></el-input>
        </el-form-item>
        <el-form-item label="模板说明" prop="remark">
          <el-input
            type="textarea"
            maxlength="200"
            v-model="modelForm.remark"
            placeholder="请输入模板说明"
            @blur="setColor"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="close3()">取 消</el-button>
        <el-button size="small" type="primary" @click="ok3()">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import DrawForce from "@/plugins/drawForce";
import axios from "axios";
import InputProp from "./InputProp";

let timer;
let id = 1;
let colori = 0;
export default {
  name: "ModTemplate",
  inject: ["reload"],
  components: { InputProp },
  data() {
    return {
      visible: false,
      visible2: false,
      visible3: false,
      nodes: [],
      links: [],
      tempLinks: [],
      predefineColors: [
        "#ffcf3c",
        "#c990c0",
        "#f79767",
        "#57c7e3",
        "#f16667",
        "#d9c8ae",
        "#8dcc93",
        "#ecb5c9",
        "#4c8eda",
        "#ffc454",
        "#da7194",
        "#569480",
      ],
      tagform: {
        id: null,
        properties: {},
        size: "",
        fill: "",
        stroke: "",
        type: "",
      },
      tagRules: {
        type: [{ required: true, message: "请输入节点类型", trigger: "blur" }],
        fill: [
          { required: true, message: "请选择一个填充颜色", trigger: "change" },
        ],
        stroke: [
          { required: true, message: "请选择一个描边颜色", trigger: "change" },
        ],
      },
      modelForm: {
        id: '',
        modelname: '',
        remark: '',
      },
      modelRules: {
        modelname: [{ required: true, message: "请输入模板名", trigger: "blur" }],
        remark: [{ required: true, message: "请输入模板说明", trigger: "blur" }],
      },
      svgFocus: false,
      d: null,
    };
  },
  computed: {
    tags () {
      let arr = []
      let typeArr = []
      if (this.nodes) {
        for (let i = 0; i < this.nodes.length; i++) {
          const it = this.nodes[i];
          if (!typeArr.includes(it.type)) {
            arr.push(it)
            typeArr.push(it.type)
          }
        }
      }
      return arr
    }
  },
  methods: {
    getPredefineColors() {
      return this.predefineColors[colori++]
    },
    open1(row) {
      row = row || {};
      this.tagform = {
        id: row.id || null,
        name: row.name || "",
        properties: row.properties || { name: "" },
        size: row.size || "",
        fill: (row.style || {}).fill || this.getPredefineColors(),
        stroke: (row.style || {}).stroke || "#FFFFFF",
        type: row.type || "",
      };
      if (colori >= this.predefineColors.length) colori = 0;
      this.visible = true;
    },
    close() {
      this.visible = false;
    },
    delNode() {
      let d = this.tagform;
      this.$confirm("确定删除节点" + d.name + "和相关连线的吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.nodes = this.nodes.filter((v) => v.id != d.id);
          this.links = this.links.filter((v) => {
            return v.target != d.id && v.source != d.id;
          });
          this.close();
          this.updateD3();
        })
        .catch(() => {});
    },
    ok() {
      this.$refs.tagform.validate((valid) => {
        if (valid) {
          let d = this.tagform;
          if (/^\d+/.test(d.type)) {
            this.$message.warning("节点类型不能以数字为开头");
            return;
          }
          if (this.$refs.inputProp.emitInput()) {
            return;
          }
          this.$nextTick(() => {
            let objName = ""; // 圆圈上显示的内容
            let properties = d.properties || {};
            for (const key in properties) {
              if (
                Object.hasOwnProperty.call(properties, key) &&
                properties[key]
              ) {
                objName = properties[key];
              }
            }
            if (!objName) {
              this.$message.warning("properties至少需要录入一条内容");
              return;
            }
            let index;
            for (let i = 0; i < this.nodes.length; i++) {
              const it = this.nodes[i];
              // if (!d.id && this.nodes[i].type == d.type) {
              //   this.$message.warning('节点类型已存在')
              //   return
              // }
              if (it.id == d.id) {
                index = i;
              }
              if (it.type == this.tagform.type) {
                  it.style = { fill: d.fill, stroke: d.stroke }
              }
            }
            let nodes = {
              id: d.id || id++ + "",
              name: objName,
              style: { fill: d.fill, stroke: d.stroke },
              type: d.type,
              size: "20",
              properties: d.properties,
            };
            if (index != undefined) {
              this.nodes[index] = nodes;
            } else {
              this.nodes.push(nodes);
            }
            this.nodes.push()
            this.close();
            this.updateD3();
          });
        }
      });
    },
    setColor() {
      if(this.tagform.type) {
        for (let i = 0; i < this.nodes.length; i++) {
          const it = this.nodes[i];
          if(it.type == this.tagform.type) {
            this.tagform.fill = it.style.fill
            this.tagform.stroke = it.style.stroke
            break
          }
        }
      }
    },
    open2() {
      this.tempLinks = JSON.parse(JSON.stringify(this.links));
      this.visible2 = true;
    },
    close2() {
      this.visible2 = false;
    },
    ok2() {
      let links = [];
      for (let i = 0; i < this.tempLinks.length; i++) {
        const it = this.tempLinks[i];
        if (!it.target || !it.source || !it.type) {
          this.$message.warning("请补全信息！");
          return;
        }
        if (/^\d+/.test(it.type)) {
          this.$message.warning("关系描述不能以数字为开头");
          return true;
        }
        if (!it.color) {
          this.$message.warning("请选择颜色！");
          return;
        }
        if (it.source == it.target) {
          this.$message.warning("source和target不能为同一个节点！");
          return;
        }
        if (
          links.includes(it.source + it.target)
          // || links.includes(it.target + it.source)
        ) {
          this.$message.warning("有重复的连线！");
          return;
        }
        links.push(it.source + it.target);
        // links.push(it.target + it.source);
      }
      this.links = this.tempLinks;
      this.close2();
      this.updateD3();
    },
    updateD3() {
      this.d.update({
        nodes: JSON.parse(JSON.stringify(this.nodes)),
        links: JSON.parse(JSON.stringify(this.links)),
      });
    },
    addLink() {
      this.tempLinks.push({
        id: id++ + "",
        source: "",
        size: "1",
        color: "#545454",
        type: "",
        target: "",
      });
    },
    delLink(i) {
      this.tempLinks.splice(i, 1);
    },
    // 初始化D3
    startD3(nodes, links) {
      this.d = DrawForce.init("svg", {
        width: window.document.body.offsetWidth,
        height: window.document.body.offsetHeight,
        nodes,
        links,
        svgClick: this.svgClick,
        textClick: this.open2,
      });
    },
    reset() {
      this.$refs.svgDiv.innerHTML = "";
    },
    open3() {
      if (this.nodes && this.nodes.length > 1) {
        let ids = this.nodes.map((v) => v.id);

        for (let i = 0; i < this.links.length; i++) {
          const it = this.links[i];
          let idi1 = ids.indexOf(it.target);
          if (idi1 > -1) {
            ids.splice(idi1, 1);
          }
          let idi2 = ids.indexOf(it.source);
          if (idi2 > -1) {
            ids.splice(idi2, 1);
          }
        }
        if (ids.length > 0) {
          this.$message.warning("所有节点都需要添加连接，请检查！");
          return;
        }
        this.visible3 = true
      } else {
        this.$message.warning("至少需要两个节点！");
      }
    },
    close3() {
      this.visible3 = false;
    },
    ok3() {
      this.$refs.modelform.validate(v => {
        if (v) {
          let data = {
            userc: this.$cookies.get('ygg1412'),
            modelname: this.modelForm.modelname,
            remark: this.modelForm.remark,
            nodes: this.nodes,
            edges: this.links,
          };
          axios.get("/saveModel?modelString=" + encodeURIComponent(JSON.stringify(data)))
            .then(function (response) {
              if(response.status != 200 || response.data.error) {
                this.$message.error(response.data.error || response.statusText)
                return
              }
            })
            .catch(function (error) {
              console.log(error);
            });
        }
      }, () => {});
      this.$message.success("保存成功");
      this.close3();
      location.href = '/index.html#Query';
    },
    windowResize() {
      if (timer) clearTimeout(timer);
      timer = setTimeout(() => {
        this.reset();
        timer = undefined;
        this.startD3(this.nodes, this.links);
      }, 300);
    },
    // 点击svg时的处理
    svgClick(data) {
      if (data) {
        this.open1(data);
      }
      this.d.highlightObject();
      this.svgFocus = false;
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
    initData() {
      let vm = this
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
    },
  },
  mounted() {
    this.initData();
    window.addEventListener("resize", this.windowResize);
  },
  destroyed() {
    window.removeEventListener("resize", this.windowResize);
  },
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
#svg {
  height: 100%;
  width: 100%;
}
.tags {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding: 10px;
  min-height: 20px;
  border-bottom: 1px solid #555;
  background-color: #ffffff0f;
  display: flex;
  justify-content: space-between;
}

.button-new-tag {
  margin-left: 10px;
  height: 20px;
  padding: 0 5px;
  line-height: 19px;
}
</style>
