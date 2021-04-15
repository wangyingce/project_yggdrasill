import Vue from 'vue'
import {
  Row,
  Col,
  Button,
  Input,
  Tag,
  Select,
  Option,
  Dialog,
  Form,
  FormItem,
  Scrollbar,
  ColorPicker,
  Message,
  MessageBox
} from 'element-ui'

Vue.use(Row)
Vue.use(Col)
Vue.use(Button)
Vue.use(Input)
Vue.use(Select)
Vue.use(Option)
Vue.use(Tag)
Vue.use(Dialog)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Scrollbar)
Vue.use(ColorPicker)

Vue.prototype.$message = Message;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;