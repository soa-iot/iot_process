//轮播图
/*layui.use(['carousel', 'form'], function(){
	var carousel = layui.carousel
	,form = layui.form;

	//常规轮播
	carousel.render({
		elem: '#imag'
		,width: '700px'
		,arrow: 'always'
		,interval: 5000
		,anim: 'default'
		,height: '150px'
	});

}); */ 

//折叠
layui.use([ 'element', 'layer' ], function() {
	var element = layui.element;
	var layer = layui.layer;

});


/**
 * 处理过程表格
 */
//从cookie中获得piid
var piid = GetQueryString("piid");

layui.use('table', function(){
	  var table = layui.table;
	  
	  //第一个实例
	  table.render({
	    elem: '#process-table'
	    ,url: '/iot_process/process/nodes/historyTask/piid/'+piid //数据接口
	   // ,page: true //开启分页
	    ,parseData: function(res) { //res 即为原始返回的数据
	    	//后端返回值： ResultJson<List<Map<String,Object>>>
            return {
                "code": res.state, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.length, //解析数据长度
                "data": res.data //解析数据列表
            }
        }
	    ,cols: [[ //表头
	      {field: 'nodeExecutor', title: '处理人', width:'25%'}
	      ,{field: 'nodeName', title: '处理节点', width:'25%'}
	      ,{field: 'nodeComment', title: '处理说明', width:'25%'}
	      ,{field: 'nodeEndTime', title: '时间', width:'25%',templet:"<div>{{layui.util.toDateString(d.nodeEndTime,'yyyy-MM-dd HH:mm:ss')}}</div>"} 
	    ]]
	  });

});

var tProblemRepId = null;

layui.use(['form', 'jquery','layer'], function(){
  var form = layui.form
  ,	$ = layui.$
  ,layer = layui.layer;
	
  //异步加载初始化页面数据
  $.ajax({
	  type: "GET",
	  url: "/iot_process/estimates/estim",
	  data:{"piid": piid},
	  dataType: "json",
	  success: function(json){
		  if(json.state == 0){
			  tProblemRepId = json.data.tproblemRepId;
			//表单初始赋值
			form.val('receive-task', {
			  "incident_sign": ((json.data.ticketNo == 1)?"事故事件":((json.data.ticketNo == 2)?"隐患":"一般问题"))    //事件/隐患标记
			  ,"remark": json.data.remark            //整改日期
			  ,"sdate": function(){
				  if(json.data.rectificationperiod != null){
					 return json.data.rectificationperiod.match(/\d+-\d+-\d+/)
				  }
			  }
			  ,"applypeople": json.data.applypeople
			  ,"problemtype": json.data.problemtype
			  ,"welName":  json.data.welName
			  ,"profession": json.data.profession
			  ,"rfid": json.data.rfid
			  ,"problemclass": json.data.problemclass
			  ,"problem_describe": json.data.problemdescribe
			})
			
			 //判断问题类别是否是 "不安全行为/状态"
     		 if(json.data.problemclass == "不安全行为/状态"){
     			$("#div-notsafe").css({"display":"block"});
	       		form.val('receive-task', {
	       			"remarkfive":json.data.remarkfive
	       			,"remarksix":json.data.remarksix
	       		})
        	 }
			
			//判断整改日期
			if(json.data.remark == "指定日期"){
				$("#sdate-div").css({"display":"inline-block"})
			}
			
		  }
	  }
  });
  
  /**
   *问题图片
   */
 $.ajax({  
 	url : "/iot_process/estimates/problemreportpho",  
 	async: false,
 	type : "get",
 	data : {"piid":piid,"remark":0},
 	dataType : "json",  
 	success: function( json) {
 		if (json.state == 0) {
 			var imgs = json.data;
 			if (imgs==null || imgs.length==0) {
				//$("#div-img").hide();
				$("#div-img").empty();
				$("#div-img").append("<p style='background-color:#FAFDFD; padding-top:9px;'>没有上传图片</p>")
			}else{
			$("#div-img").show();
 			var mode = imgs.length%3;
 			var img_id = 0;
 			for (var j = 0; j < Math.ceil(imgs.length/3); j++) {
 				var $img_div=$('<div></div>');
 				if (mode != 0 && j == (Math.ceil(imgs.length/3) - 1) ) {
 					for (var i = 0; i < mode; i++) {
 						$img_div.append('<img data-method="offset" class="big-img" alt="'+imgs[img_id].phoDispiayName+'" src="'+imgs[img_id].phoAddress+'" >');
 						img_id++;
 					}

 				}else{
 					for (var i = 0; i < 3; i++) {
 						$img_div.append('<img data-method="offset" class="big-img" alt="'+imgs[img_id].phoDispiayName+'" src="'+imgs[img_id].phoAddress+'" >');
 						img_id++;
 					}

 				}
 				$("#imag").append($img_div);
 			}
 			//轮播图
			layui.use(['carousel', 'form'], function(){
				var carousel = layui.carousel
				,form = layui.form;

				//常规轮播
				carousel.render({
					elem: '#div-img'
					,arrow: 'always'
					,height: '150px'
					,width: '556px'
					,interval: 5000
				});
			}); 
		  }
 		}

 	}  
 });
  	
 	//从cookie中获取处理人
    var userName = getCookie1("name").replace(/"/g,'');
	//点击下一步按钮操作
	form.on('submit(next_step)', function(data){
		//验证表单是否为空
		 if($("#comment_receive").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("处理说明不能为空", {icon: 7, offset: '100px'});
			  return;
		  }	
		  console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
		  $("#next_step").attr({"disabled":"disabled"});
		  $.ajax({  
			    async : false,
		    	url : "/iot_process/process/nodes/next/group/piid/"+piid,   ///iot_process/estimates/problemdescribe
		        type : "PUT",
		        data : {
		        		"comment": data.field.comment,
		        		"complementor":userName,
		        		"userName": userName,
		        		"operateName": "下一步"
		        },
		        contentType: "application/x-www-form-urlencoded",
		        dataType : "json",  
		        success: function(jsonData) {

		        	if(jsonData.data == true){
		        		$("#comment_receive").val("");
		        		layer.msg("接收作业成功", {icon: 1, time: 2000, offset: '100px'}, function(){
		        			top.location.href = "http://10.89.90.118:10239/CZ_PIOTMS/index.action";
		        		});
		        	}else{
		        		layer.msg("接收作业失败",{icon: 2, offset: '100px'});
		        	}
		        	$("#next_step").removeAttr("disabled");
		        } 
		        ,error:function(){
		        	layer.msg("接收作业失败,请检查网络是否正常",{icon: 2, offset: '100px'});
		        	$("#next_step").removeAttr("disabled");
		        }	
		   });		  
		  
		  //return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
   });
    
    /**
	 * 回退到上一个节点
	 */
    form.on('submit(back_previous)', function(data){
		if(isempty()){
			return false;
		}
		console.log(data.field)
		$("#back_previous").attr({"disabled":"disabled"});
		$.ajax({
			 async:false
		     ,type: "PUT"
		     ,url: '/iot_process/process/nodes/before/group/piid/'+piid   //piid为流程实例id
		     ,data: {
		     	"comment": data.field.comment  //处理信息
		     	,"userName": userName
		     	,"operateName": "回退"
		     }  
		     ,contentType: "application/x-www-form-urlencoded"
		     ,dataType: "json"
		     ,success: function(jsonData){
		     	//后端返回值： ResultJson<String>
		    	 if(jsonData){
		    		 layer.msg("回退成功",{icon:1, time: 2000, offset: '100px'}, function(){
		    			 top.location.href = "http://10.89.90.118:10239/CZ_PIOTMS/index.action";
		    		 })
		    	 }else{
		    		 layer.msg("回退失败",{icon:2, offset: '100px'});
		    	 }
		    	 $("#back_previous").removeAttr("disabled");
		     },
		     error:function(){
		    	 layer.msg("回退失败,请检查网络是否正常",{icon:2, offset: '100px'});
		    	 $("#back_previous").removeAttr("disabled");
		     }		       
	  });
    })
   
    //验证表单是否为空
	function isempty(){
		if($("#comment_receive").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("处理说明不能为空", {icon: 7, offset: '100px'});
			  return true;
		}
		return false;
	}
    
   //触发事件
	var active = {
			offset: function(othis){
				
			var imgHtml= "<img src='"+$(this).attr("src")+"'width='600px'  height='500px'/>";
				//var type = othis.data('type')
				layer.open({
				type: 1
				//,offset: type 
				,area: ['600px','500px']
				,content: imgHtml
				,title:false
				//,shadeClose:true
				//,cancel:false
				,offset:'auto'
				});
			}
	};

	$('.big-img').click(function(){
		console.log(123);
		var othis = $(this), method = othis.data('method');
		active[method] ? active[method].call(this, othis) : '';
	});
		    
});
