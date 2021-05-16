//vue.config.js

const CopyWebpackPlugin = require('copy-webpack-plugin')

module.exports = {
  indexPath: 'engine.html',
  productionSourceMap: false,
  pages: {
    index: {
      // page 的入口
      entry: 'src/pages/engine/index.js',
      // 模板来源
      template: 'public/index.html',
      // 在 dist/index.html 的输出
      filename: 'engine.html',
      // 当使用 title 选项时，
      // template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
      title: 'yggdrasill_engine',
    },
    template: {
      entry: 'src/pages/template/index.js',
      template: 'public/index.html',
      filename: 'model.html',
      title: 'yggdrasill_model',
    },
  },
  devServer: {
    proxy: 'http://10.6.6.118:8080'
  },
  configureWebpack : {
    plugins: [
      new CopyWebpackPlugin({
        patterns: [{
            from: './static',
            to: 'static',
            globOptions: {
                ignore: [
                    '**/.*'
                ]
            }
        }]
      })
    ]
  }
}