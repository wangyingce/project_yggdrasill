//vue.config.js

const CopyWebpackPlugin = require('copy-webpack-plugin')

module.exports = {
  productionSourceMap: false,
  devServer: {
    // proxy: 'http://10.100.8.250:8080'
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