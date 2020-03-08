//全局配置
layui.config({
	base: '../jsPackage/js/',
}).extend({
    excel: 'excel',
});
//从cookie中获取当前登录用户
var resavepeople = getCookie1("name").replace(/"/g,'');
console.log("resavepeople="+resavepeople);

/**
 * 日期插件
 */
layui.use(['jquery','form','layer','table','excel','laydate'], function(){
	var laydate = layui.laydate;
	
	//常规用法
	laydate.render({
		elem: '#startdate'
		,format: 'yyyy-MM-dd'
	});

	//常规用法
	laydate.render({
		elem: '#enddate'
		,format: 'yyyy-MM-dd'
	});
})


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
layui.use(['jquery','form','layer','table','excel','laydate'], function(){
	var form = layui.form
	,layer = layui.layer
	,table = layui.table
	,laydate = layui.laydate
	,$ = layui.$ //使用jQuery
	var excel = layui.excel;
	
	/**
	 * 初始化日期
	 */
	var date = new Date();
	date.setMonth(date.getMonth()-1);
	if(isNaN(date)){
		date = new Date();
		date.setMonth(date.getMonth()-1, 30);
		if(isNaN(date)){
			date = new Date();
			date.setMonth(date.getMonth()-1, 29);
			if(isNaN(date)){
				date = new Date();
				date.setMonth(date.getMonth()-1, 28);
			}
		}
	}
	$("#startdate").val(formatDate(date));
	$("#enddate").val(formatDate(new Date()));
	

	/**
	 * 问题上报信息展示表
	 */
	var reportTable = table.render({
		elem: '#reportFinish',
		method: 'post',
		url: '/iot_process/report/finish/record',
		//toolbar: "#toolBtn",
		autoSort: false,  //禁用前端自动排序
		defaultToolbar: [''],
		cellMinWidth:70,
		page: false,   //开启分页
		where: {
			'startDate': $.trim($("#startdate").val()),
			'endDate': $.trim($("#enddate").val())
	    },
		parseData: function(res){ //res 即为原始返回的数据
			var data = res.data;
			var departName = ['维修工段','净化工段','生产办公室','HSE办公室','设备办公室','财务办公室','综合办'];
			if(data != null || data.length < 7){
				for(var i=0;i<departName.length;i++){
					for(var j=0;j<data.length;j++){
						if(departName[i] == data[j].DEPARTNAME){
							departName.splice(i,1,null);
							break;
						}
					}
				}
				
				if(data.length == 0) data = [];
				
				for(var i=0;i<departName.length;i++){
					if(departName[i] != null){
						var sample = {
								"DEPARTNAME": departName[i],
								"UNFINISHED": 0,
								"FINISHED": 0,
								"TOTAL": 0
						}
						data.push(sample);
					}
				}
			}
			initEchart(data);
		    return {
		      "code": res.state, //解析接口状态
		      "msg": res.message, //解析提示文本
		      //"count": res.count, //解析数据长度
		      "data": data      //解析数据列表
		    };
		},
		cols: [[{field:'id', title:'编号', width:'8%', sort:false, type:'numbers', fixed:'left', align:'center'},
			{field:'DEPARTNAME', title:'部门名称', width:'23%', sort:false, align:'center'},
			{field:'UNFINISHED', title:'整改中', width:'23%', sort:false, align:'center'}, 
			{field:'FINISHED', title:'已完成', width:'23%', sort:false, align:'center'}, 
			{field:'TOTAL', title:'总负责整改数', sort:false, align:'center'}]]
			//{fixed:'right',  title:'处理过程', minWidth:105, width:'15%', align:'center', toolbar:'#barBtn'} ]]  
	});
	
	/**
	 * 重新加载表
	 */
	function reloadTable(){
		reportTable.reload({
    		url: '/iot_process/report/finish/record'
    	   ,where: {
    			'startDate': $.trim($("#startdate").val()),
    			'endDate': $.trim($("#enddate").val())
    	   }
    	})
	}
	
	/**
	 * 监听工具行事件
	 */
	table.on('toolbar(reportFinish)', function(obj){
		console.log(obj);
	    if(obj.event === 'query'){
	    	console.log("----点击条件查询按钮----");
	    	var startDate = $.trim($("#startdate").val());
	    	var endDate = $.trim($("#startdate").val());
	    	
			reloadTable(null, null);
	    }
	});
	
	/**
	 * 查询按钮点击事件
	 */
	$("#queryBtn").click(function(){
		console.log("----点击条件查询按钮----");
		reloadTable(null, null);
	})
	
	/**
	 *  echart柱状图
	 */
	 // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('finishedEchart'));
    function initEchart(data){
    	console.log("-----initEchart-----");
    	var yName = [], finished = [], unfinished = [];
    	for(var i=0;i<data.length;i++){
    		yName[i] = data[i].DEPARTNAME;
    		finished[i] = data[i].FINISHED;
    		unfinished[i] = data[i].UNFINISHED;
    	}
    	console.log(yName);
    	// 指定图表的配置项和数据
        var option = {
        		tooltip: {
        	        trigger: 'axis',
        	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
        	            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        	        }
        	    },
        	    legend: {
        	        data: ['已完成', '整改中']
        	    },
        	    grid: {
        	        left: '0.2%',
        	        right: '2%',
        	        bottom: '3%',
        	        containLabel: true
        	    },
        	    xAxis: {
        	        type: 'value'
        	    },
        	    yAxis: {
        	        type: 'category',
        	        data: yName
        	    },
        	    series: [
        	        {
        	            name: '已完成',
        	            type: 'bar',
        	            stack: '总量',
        	            label: {
        	                show: true,
        	                position: 'inside'
        	            },
        	            data: finished
        	        },
        	        {
        	            name: '整改中',
        	            type: 'bar',
        	            stack: '总量',
        	            label: {
        	                show: true,
        	                position: 'inside'
        	            },
        	            data: unfinished
        	        }
        	    ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    
    
    /**
     * 日期格式化
     */
    function formatDate(date){
    	var year = date.getFullYear();
    	var month = date.getMonth()+1;
    	var day = date.getDate();
    	if(month < 10){
    		month = '0' + month;
    	}
    	if(day < 10){
    		day = '0' + day;
    	}
    	return year+'-'+month+'-'+day;
    }
})