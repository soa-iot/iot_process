//全局配置
layui.config({
	base: '../jsPackage/js/',
}).extend({
    excel: 'excel',
});


//加载layui内置模块
layui.use(['jquery','form','layer','table','excel'], function(){
	var form = layui.form
	,layer = layui.layer
	,table = layui.table
	,$ = layui.$ //使用jQuery
	var excel = layui.excel;
	welunit();
	secondclassequipment();
	tequmemoone();
	
	//读取问题区域选择的值
	var welName = $.cookie('welName');
	console.log("-------"+welName);
	if(welName != null || welName != ''){
		switch(welName){
		case '主体装置一列':
			$("#welName").val('主体装置Ⅰ列');
			break;
		case '主体装置二列':
			$("#welName").val('主体装置Ⅱ列');
			break;
		case '主体装置三列':
			$("#welName").val('主体装置Ⅲ列');
			break;
		case '主体装置四列':
			$("#welName").val('主体装置Ⅳ列');
			break;
		case '主体装置五列':
			$("#welName").val('主体装置Ⅴ列');
			break;
		case '主体装置六列':
			$("#welName").val('主体装置Ⅵ列');
			break;
		case '主体装置七列':
			$("#welName").val('主体装置Ⅶ列');
		default:
			break;
		}
	}
	//删除cookie
	$.cookie('welName', null, { path: '/' });
	
	/**
	 * 设备信息展示表
	 */
	var equipmentTable = table.render({
		elem: '#equipmentInfo',
		method: 'post',
		url: '/iot_process/equipment/show',
		/*toolbar: '#toolbarBtn',
		defaultToolbar: [''],*/
		totalRow: true,
		page: true,   //开启分页
		cellMinWidth: 130,
		where: {
			'welName': $('#welName').val()
		},
		request: {
		    pageName: 'page' //页码的参数名称，默认：page
		    ,limitName: 'limit' //每页数据量的参数名，默认：limit
		},
		parseData: function(res){ //res 即为原始返回的数据
		    return {
		      "code": res.code, //解析接口状态
		      "msg": res.msg, //解析提示文本
		      "count": res.count, //解析数据长度
		      "data": res.data      //解析数据列表
		    };
		},
		cols: [[
			{type:'radio'},
			{field:'welName', title:'装置列名', align:'center'},    //, templet:"<div>{{layui.util.toDateString(d.applydate,'yyyy-MM-dd HH:mm:ss')}}</div>"
			{field:'welUnit', title:'装置单元', align:'center'},
			{field:'equMemoOne', title:'设备类别', align:'center'},
			{field:'equPositionNum', title:'设备位号', align:'center'},
			{field:'equName', title:'设备名称', align:'center'}]]
	});
	
	/**
	 * onblur失去焦点事件
	 */
	form.on('select(welName)', function(data){
		console.log("welName");
		$('#welUnit').val("")
		$('#secondclassEquipment').val("")
		welunit();
		tequmemoone();
		secondclassequipment();
		tableReload();
	});
	form.on('select(welUnit)', function(data){
		console.log("welUnit");
		$('#secondclassEquipment').val("")
		tequmemoone();
		secondclassequipment();
		tableReload();
	});
	form.on('select(secondclassEquipment)', function(data){
		console.log("secondclassEquipment");
		tequmemoone();
		tableReload();
	});
	form.on('select(equMemoOne)', function(data){
		console.log("equMemoOne");
		tableReload();
	});
	/*$("#equMemoOne").blur(function(){
		tableReload();
	})*/;
	$("#equPositionNum").blur(function(){
		tableReload();
	});
	$("#equName").blur(function(){
		tableReload();
	});
	
	//下拉选函数
	function welunit(){
		$.ajax({  
			url : "/iot_process/equipment/showselectwelunit",  
			type : "get",
			data : {
				'welName' : $('#welName').val()},
				dataType : "json",  
				success: function( json) {
					$("#welUnit").empty();
					var data = json.data;
					var select = '<option value="">请选择</option>'
					if(data.length>0){
						for (var i = 0; i < data.length; i++) {
							select += '<option value="'+data[i]+'">'+data[i]+'</option>'
						}
					}
					$("#welUnit").append(select);
					form.render('select');
				}
			})
	}
	
	function secondclassequipment(){
		$.ajax({  
			url : "/iot_process/equipment/showselectsecondclassequipment",  
			type : "get",
			data : {
				'welName' : $('#welName').val(),
				'welUnit' : $('#welUnit').val()
				},
				dataType : "json",  
				success: function( json) {
					$("#secondclassEquipment").empty();
					var data = json.data;
					var select = '<option value="">请选择</option>'
					if(data.length>0){
						for (var i = 0; i < data.length; i++) {
							select += '<option value="'+data[i]+'">'+data[i]+'</option>'
						}
					}
					$("#secondclassEquipment").append(select);
					form.render('select');
				}
			})
	}
	
	function tequmemoone(){
		$.ajax({  
			url : "/iot_process/equipment/showselectequmemoone",  
			type : "get",
			data : {
				'welName' : $('#welName').val(),
				'welUnit' : $('#welUnit').val(),
				'secondclassEquipment':$('#secondclassEquipment').val()
				},
				dataType : "json",  
				success: function( json) {
					$("#equMemoOne").empty();
					var data = json.data;
					var select = '<option value="">请选择</option>'
					if(data.length>0){
						for (var i = 0; i < data.length; i++) {
							select += '<option value="'+data[i]+'">'+data[i]+'</option>'
						}
					}
					$("#equMemoOne").append(select);
					form.render('select');
				}
			})
	}
	
	/**
	 * 表重新加载
	 */
	function tableReload(){
		equipmentTable.reload({
    		url: '/iot_process/equipment/show'
    	   ,page: {
    		   curr: 1 //重新从第 1 页开始
    	   }
    	   ,where: {
    			'welName': $("#welName").val(),
    			'equMemoOne': $("#equMemoOne").val(),
    			'equPositionNum': $("#equPositionNum").val(),
    			'equName': $("#equName").val(),
    			'welUnit':$("#welUnit").val(),
    	   }
    	})
	}
	
	
	/**
	 * 监听头工具栏事件 
	 */ 
	/*
	  table.on('toolbar(equipmentInfo)', function(obj){
	     //var checkStatus = table.checkStatus(obj.config.id);
		 console.log(obj);
		 switch(obj.event){
	      case 'querydata':
	    	console.log('querydata');
	    	equipmentTable.reload({
	    		url: '/iot_process/equipment/show'
	    	   ,page: {
	    		   curr: 1 //重新从第 1 页开始
	    	   }
	    	   ,where: {
	    			'welName': $("#welName").val(),
	    			'equMemoOne': $("#equMemoOne").val(),
	    			'equPositionNum': $("#equPositionNum").val(),
	    			'equName': $("#equName").val(),
	    	   }
	    	})
	        break;
	      case 'delete':
	    	  console.log('delete');
	    	  layer.msg("功能正在完善中...",{icon: 5})
	    };
	  });*/
})