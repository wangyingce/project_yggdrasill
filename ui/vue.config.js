//vue.config.js

const CopyWebpackPlugin = require('copy-webpack-plugin')

module.exports = {
  indexPath: 'engine.html',
  productionSourceMap: false,
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