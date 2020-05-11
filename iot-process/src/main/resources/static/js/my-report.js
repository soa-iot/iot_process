//全局配置
layui.config({
	base: '../jsPackage/js/',
}).extend({
    excel: 'excel',
});
//从cookie中获取当前登录用户
//var resavepeople = getCookie1("name").replace(/"/g,'');
var resavepeople = GetQueryString("username");
console.log("resavepeople="+resavepeople);
if(resavepeople == null || resavepeople == ''){
	resavepeople = '当前登录人为空';
}
/**
 * 日期插件
 */
layui.use('laydate', function(){
	var laydate = layui.laydate;
	//常规用法
	laydate.render({
		elem: '#startdate'
		,format: 'yyyy-MM-dd'
	});
});

/**
 * 获取url地址的参数
 */
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null) return $.trim(decodeURI(r[2])); return null;
}

//加载layui内置模块
layui.use(['jquery','form','layer','table','excel'], function(){
	var form = layui.form
	,layer = layui.layer
	,table = layui.table
	,$ = layui.$ //使用jQuery
	var excel = layui.excel;
	
	
	/**
	 * 问题上报信息展示表
	 */
	var problemTable = table.render({
		elem: '#myReport',
		method: 'post',
		url: '/iot_process/report/showproblembycondition',
		autoSort: false,  //禁用前端自动排序
		skin: 'nob',
		even: true,
		page: true,   //开启分页
		request: {
		    pageName: 'page' //页码的参数名称，默认：page
		    ,limitName: 'limit' //每页数据量的参数名，默认：limit
		},
		where: {
			'applypeople': resavepeople,
			'limit': 5
		},
		parseData: function(res){ //res 即为原始返回的数据
			var data = res.data     
	    	if(data != null && data != '' && res.count != 0){
	    		 for(var i=0;i<data.length;i++){
	    			 data[i].applydate = (data[i].applydate == '' || data[i].applydate == null)?data[i].applydate : data[i].applydate.replace(/T/, ' ').replace(/\..*/, '');
	    			 data[i].problemstate = (data[i].problemstate == 'FINISHED')?'已整改':'未整改';
	    		 }
	    	}

		    return {
		      "code": res.code, //解析接口状态
		      "msg": res.msg, //解析提示文本
		      "count": res.count, //解析数据长度
		      "data": data      //解析数据列表
		    };
		},
		cols: [[{field:'id', title:'序号', width:'8%', sort:false, type:'numbers', align:'center'},
			{field:'applydate', title:'上报日期', width:'17%', sort:false, align:'center'},    //, templet:"<div>{{layui.util.toDateString(d.applydate,'yyyy-MM-dd HH:mm:ss')}}</div>"
			{field:'remarktwo', title:'当前节点', width:'15%', sort:false, align:'center'},
			{field:'remarkthree', title:'是否超期', width:'15%', sort:false, align:'center'},
			{field:'problemdescribe', title:'问题描述', width:'29%', sort:false, align:'center'},
			{field:'piid', title:'流程ID', sort:false, hide:true},
			{fixed:'right',  title:'处理详情', minWidth:105, width:'15.9%', align:'center', toolbar:'#barBtn'} ]]  
	});
	
	/**
	 * 监听每一行工具事件
	 */
	var piid;
	table.on('tool(myReport)', function(obj){
		console.log(obj);
	    var data = obj.data;
	    if(obj.event === 'process'){
	      piid = data.piid;
	      loadTable();
	      layer.open({
	    	title: '处理过程信息',
	    	type: 1,
	    	id: obj.event+1,
	    	btn: ['确认'],
	    	offset: '3px',
	    	area: ['99%','99%'],
	        content: $('#div-process'),
	        yes: function(index, layero){
	            layer.close(index); //如果设定了yes回调，需进行手工关闭
	          }
	      });
	    }
	    if(obj.event === 'detail'){
		   piid = data.piid;
		   $(".problem-detail").attr({"href":"/iot_process/html/problem-detail.html?piid="+piid});
		}
	});
	
	/**
	 * 加载处理过程展示表
	 */
	function loadTable(){
		table.render({
			elem: '#processStep'
			,url: '/iot_process/process/nodes/all/piid/'+piid //数据接口
			,parseData: function(res) { //res 即为原始返回的数据
				var data = res.data; 
				
				if(data != null && data != '' && data.length != 0){
					for(var i=0; i<data.length;i++){
						data[i].duration_ = (data[i].duration_ <= 172800000)?'未超期':'超期';
						data[i].start_TIME_ = data[i].start_TIME_.replace(/T/, ' ').substring(0, 19);
						if(data[i].end_TIME_ == null){
							var startTime = new Date(Date.parse(data[i].start_TIME_.replace(/-/g, "/"))).getTime();
							var currentTime = new Date().getTime();
							if((currentTime - startTime) > 172800000){
								data[i].duration_ = '超期';
							}
						}
					}
				}
				
				return {
					"code": res.state, //解析接口状态
					"msg": res.message, //解析提示文本
					"count": data.length, //解析数据长度
					"data": data //解析数据列表
				}
			}
			,cols: [[ //表头
				{field: 'assignee_', title: '处理人', width:'23%',fixed: 'left'}
				,{field: 'act_NAME_', title: '处理节点', width:'20%'}
				,{field: 'duration_', title: '是否超期', width:'15%'}
				,{field: 'tenant_ID_', title: '操作名称', width:'17%'}
				,{field: 'start_TIME_', title: '时间', width:'24.9%',fixed: 'right'} 
				]]
		});
	}
})