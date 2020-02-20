
var dfid ="",
	/**
	 * 外部协调接口属性
	 */

	outhelper = {
		"HSE办公室": "HSE办主任"
		,"生产办": "生产办主任"
		,"设备办": "设备办主任"
		,"财务经营办公室": "财务经营办主任"
		,"净化工段": "净化技术干部"
		,"维修工段": "维修技术干部"
		,"综合办": "综合办主任"
},
/**
 * 跳转节点id
 */
	actualId = {
		"净化工段": "pure"
		,"维修工段": "repair"
		,"其他": "estimate"
},
/**
 * 流程变量id
 */
	actualVar = {
		"净化工段": "puror"
		,"维修工段": "repairor"
		,"其他": "estimators"
},
url = "http://"+getUrlIp()+"/iot_usermanager/html/userCenter/test.html";