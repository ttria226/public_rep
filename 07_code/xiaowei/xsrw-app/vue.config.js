module.exports = {
  devServer: {
    proxy: {
      '/prod-api': {
        target: 'http://192.168.0.153:8080/dev-api/',
        changeOrigin: true
      }
    }
  }
}