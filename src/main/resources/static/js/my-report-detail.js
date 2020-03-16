//全局配置
layui.config({
	base: '../jsPackage/js/',
}).extend({
    excel: 'excel',
});
//从cookie中获取当前登录用户
var resavepeople = getCookie1("name").replace(/"/g,'');
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
	
	//问题未整改数量和已整改数量
	var uncount = 0, count = 0;
	var piid, isreload = true;
	var piids = null;
	
	/**
	 * 问题上报信息展示表
	 */
	var problemTable = table.render({
		elem: '#myReportDetail',
		method: 'post',
		url: '/iot_process/report/showproblembycondition',
		toolbar: '#toolbarBtn',
		autoSort: false,  //禁用前端自动排序
		defaultToolbar: [''],
		cellMinWidth:70,
		page: true,   //开启分页
		request: {
		    pageName: 'page' //页码的参数名称，默认：page
		    ,limitName: 'limit' //每页数据量的参数名，默认：limit
		},
		where: {
			'applypeople': resavepeople
		},
		parseData: function(res){ //res 即为原始返回的数据
			var data = res.data     
	    	if(data != null && data != '' && res.count != 0){
	    		 for(var i=0;i<data.length;i++){
	    			 data[i].applydate = (data[i].applydate == '' || data[i].applydate == null)?data[i].applydate : data[i].applydate.replace(/T/, ' ').replace(/\..*/, '');
	    			 data[i].problemstate = (data[i].problemstate == 'FINISHED')?'已整改':'未整改';
	    			 if(isreload){
	    				count = res.msg;
		    		    uncount = res.count - count; 
	    			 }
	    		 }
	    		$("#count").text(count);
	 			$("#uncount").text(uncount);
	    	}else{
	    		$("#count").text(0);
				count = 0;
	 			$("#uncount").text(0);
				uncount = 0;
	    	}
			
			isreload = false;
		    return {
		      "code": res.code, //解析接口状态
		      "msg": res.msg, //解析提示文本
		      "count": res.count, //解析数据长度
		      "data": data      //解析数据列表
		    };
		},
		cols: [[{field:'id', title:'编号', width:'5%', sort:false, type:'numbers', fixed:'left', align:'center'},
			{field:'applydate', title:'上报日期', width:'10%', sort:true, align:'center'},    //, templet:"<div>{{layui.util.toDateString(d.applydate,'yyyy-MM-dd HH:mm:ss')}}</div>"
			{field:'applypeople', title:'上报人', width:'8%', sort:true, align:'center'},
			{field:'welName', title:'装置单元', width:'9%', sort:true, align:'center'},
			{field:'problemclass', title:'问题类别', width:'9%', sort:true, align:'center'},
			{field:'profession', title:'专业', width:'7%', sort:true, align:'center'},
			{field:'problemstate', title:'问题状态', width:'7%', sort:true, align:'center'},
			{field:'rectificationperiod', title:'整改日期', width:'8%', sort:true, align:'center'},
			{field:'remarkthree', title:'是否超期', width:'7%', sort:true, align:'center'},
			{field:'problemdescribe', title:'问题描述', width:'16%', sort:true, align:'center'},
			{field:'piid', title:'流程ID',sort:false, hide:true},
			{fixed:'right',  title:'处理过程', minWidth:105, width:'15.7%', align:'center', toolbar:'#barBtn'} ]]  
	});

	
	/**
	 * 监听每一行工具事件
	 */
	table.on('tool(myReportDetail)', function(obj){
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
	    	offset: '100px',
	    	area: ['60%','60%'],
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
	
	/**
	 * 导出表事件
	 */
	function exportExcel(){
		//从后台取出表数据
		$.ajax({
			async: false,
			type: "POST",
			url: "/iot_process/report/showproblembycondition",
			data: {
    			'applypeople': resavepeople
			},
			dataType: "json",
			success: function(json){
				if(json.code == 0){
					var data = json.data     
			    	if(data != null || data != ''){
			    		 for(var i=0;i<data.length;i++){
			    			 data[i].applydate = data[i].applydate.replace(/T/, ' ').replace(/\..*/, '');
			    			 data[i].problemstate = (data[i].problemstate == 'FINISHED')?'已整改':'未整改';
			    			 data[i].rectificationperiod = 
			    				 (data[i].rectificationperiod != null && data[i].rectificationperiod != '')?data[i].rectificationperiod.substring(0,10):'';
			    			 if(data[i].problemclass != '不安全行为/状态'){
			    				 data[i].remarkfive = '';
			    				 data[i].remarksix = '';
			    			 }
			    		 }
			    	}
					// 1. 数组头部新增表头
					data.unshift({
						applydate: '上报日期',
						applypeople: '上报人', 
						depet: '部门',
						maintenanceman: '问题当前责任人',
						remarktwo: '当前进度',
						rectificationperiod: '计划完成时间',
						remarkthree: '是否超期',
						problemtype: '属地单位',
						welName: '问题区域',
						profession: '专业',
						rfid: '设备位号',
						problemclass: '问题类别',
						remarkfive: '不安全行为',
						remarksix: '具体行为',
						problemdescribe: '问题描述',
						problemstate: '问题状态',
						
					});
					//2. 过滤多余属性
					var exportData = excel.filterExportData(data, ['applydate', 'applypeople', 'depet', 'maintenanceman', 'remarktwo', 
						'rectificationperiod', 'remarkthree', 'problemtype', 'welName', 'profession', 'rfid', 'problemclass', 'remarkfive', 'remarksix', 'problemdescribe', 'problemstate']);
					console.log(123);
					//2.1 设置列宽,N列为180， 其他列默认为100
					var colConf = excel.makeColConfig({
						'A': 120,
					    'O': 200,
					    'P': 100
					}, 100);
					
					// 2.2 调用设置样式的函数，传入设置的范围，支持回调
					excel.setExportCellStyle(exportData, 'A1:P1', {
					    s: {
					        alignment: {
					            horizontal: 'center',
					            vertical: 'center'
					        },
					        font: {bold: true }
					    }
					});
					
					var date= new Date();
					date = '问题上报跟踪数据'+date.toLocaleDateString().replace(/\//g, '-')+'.xlsx';
					
					//3. 执行导出函数，系统会弹出弹框
					excel.exportExcel({
			            sheet1: exportData
			        }, date, 'xlsx',{
			            extend: {
			                '!cols': colConf
			            }
			        });

					//table.exportFile(problemTable.config.id,exportData, 'xls');
				}
			},
			error: function(){
				layer.msg("获取数据失败,请检查网路情况 !", {icon:2})
			}
		});	
	}
	
	/**
	 * 监听头工具栏事件 
	 */ 
	  table.on('toolbar(myReportDetail)', function(obj){
	     //var checkStatus = table.checkStatus(obj.config.id);
		 console.log(obj);
		 switch(obj.event){
	      case 'export':
	    	  console.log('export');
	    	  exportExcel();
	    	  break;
	      case 'finish':
	    	  console.log('finish');
			   console.log("count="+count+" , uncount="+uncount);
	    	  if(count != 0){
		    	  problemTable.reload({
		      		url: '/iot_process/report/showproblembycondition'
		      	   ,page: {
		      		   curr: 1 //重新从第 1 页开始
		      	   }
		      	   ,where: {
		      			'problemstate': 'FINISHED',
		      	   }
		      	})
	    	  }
	        break;
	      case 'unfinish':
	    	  console.log('unfinish');
			  console.log("count="+count+" , uncount="+uncount);
	    	  if(uncount != 0){
	    		  problemTable.reload({
			      		url: '/iot_process/report/showproblembycondition'
			      	   ,page: {
			      		   curr: 1 //重新从第 1 页开始
			      	   }
			      	   ,where: {
			      			'problemstate': 'UNFINISHED',
			      	   }
			      })  
	    	  }
		      break;
	    };
	  });
	
	 /**
	  * 监听排序事件
	  */
	 table.on('sort(myReportDetail)', function(obj){
		 console.log(obj.field); //当前排序的字段名
		 console.log(obj.type); //当前排序类型：desc（降序）、asc（升序）、null（空对象，默认排序）
		 reloadTable(obj.field, obj.type, null);
	 });
	 
})