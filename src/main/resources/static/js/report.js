//加载layui内置模块
layui.use(['jquery','form','upload','layedit', 'laydate'], function(){
	var form = layui.form
	,layer = layui.layer
	,layedit = layui.layedit
	,laydate = layui.laydate
	,$ = layui.$ //重点处,使用jQuery
	,upload = layui.upload;
	
	//不安全行为下拉框事件监听
	$(".notsafe-select").click(function(){
	   var select = this.children[0];
	   if(select.disabled){
		 layer.msg("需要问题类别选择：不安全行为/状态");
	   }
	});
	
	//监听问题类别select选择
	  form.on('select(question-type)', function(data){
	    if(data.value == "不安全行为/状态"){
	    	$("#notsafe-select").removeAttr("disabled");
	    	$("#detail-select").removeAttr("disabled");
	    }else{
	    	$("#notsafe-select").attr("disabled","true");
	    	$("#detail-select").attr("disabled","true");
	    }
		//更新不安全select渲染
	    form.render('select','notsafe-select');
	  }); 
	
	//异步请求，从数据库中读取不安全数据列表
	  var unsafeList;
	  $.ajax({  
        type: 'get',  
        url: '/iot_process/report/unsafe/showlist', // ajax请求路径   
        success: function(data){ 
          if(data.state == 0){
          	unsafeList = data.data;
          	for( x in unsafeList){
          		var $option1 = $("<option></option>");
          		$option1.val(unsafeList[x].typesID);
          		$option1.text(unsafeList[x].typesName);
          		$("#notsafe-select").append($option1);            		
          	}
          	
          	var actionList = unsafeList[0].unsafeAction;
      		for( y in actionList){
      			var $option2 = $("<option></option>");
          		$option2.val(actionList[y].actionsID);
          		$option2.text(actionList[y].actionsName);
          		$("#detail-select").append($option2);
      		}
            //更新不安全select渲染
    	    form.render('select','notsafe-select');
          }
        },
    });
	  
	//监听不安全行为级联
	form.on('select(notsafe)', function(data){
	   var value = data.value;
	   for( x in unsafeList){
	      if(value == unsafeList[x].typesID){
	    	var actionList = unsafeList[x].unsafeAction;
	    	$("#detail-select").empty();  //清空子选项
            for( y in actionList){
            	var $option2 = $("<option></option>");
	            $option2.val(actionList[y].actionsID);
	            $option2.text(actionList[y].actionsName);
	            $("#detail-select").append($option2);
            }
	      }
	    }
	     //更新不安全select渲染
		 form.render('select','notsafe-select');
	});
})  
	