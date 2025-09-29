import Vue from 'vue'
import App from './App'
import store from './store' // store
import plugins from './plugins' // plugins
import http from '@/api/manage'
import './permission' // permission
import uView from "uview-ui";
Vue.use(uView);
Vue.use(plugins)

Vue.config.productionTip = false
Vue.prototype.$store = store
Vue.prototype.$http = http
import headtitle from '@/components/headTitle.vue'
Vue.component('headTitle', headtitle)
import scanMaterialsNoRFID from '@/components/scanMaterialsNoRFID.vue'
Vue.component('scanMaterialsNoRFID', scanMaterialsNoRFID)
import diduijianhuo from '@/components/diduijianhuo.vue'
Vue.component('diduijianhuo', diduijianhuo)
import scanMaterials from '@/components/scanMaterials.vue'
Vue.component('scanMaterials', scanMaterials)
import VehicleList from '@/components/vehicleScanning.vue'
Vue.component('VehicleList', VehicleList)
import LocationScanning from '@/components/locationScanning.vue'
Vue.component('LocationScanning', LocationScanning)
import GroupMaterials from '@/components/groupMaterials.vue'
Vue.component('GroupMaterials', GroupMaterials)
import groupMaterialsZP from '@/components/groupMaterialsZP.vue'
Vue.component('groupMaterialsZP', groupMaterialsZP)
import OnlineVehicle from '@/components/onlineVehicle.vue'
Vue.component('OnlineVehicle', OnlineVehicle)
import RelocationMaterials from '@/components/relocationMaterials.vue'
Vue.component('RelocationMaterials', RelocationMaterials)

App.mpType = 'app'

const app = new Vue({
	...App
})

app.$mount()