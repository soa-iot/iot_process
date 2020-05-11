layui.use(['form','tree','jquery','upload','layer','table'], function(){
  var form = layui.form
  ,	$ = layui.$
  ,layer = layui.layer
  ,upload = layui.upload
  ,tree = layui.tree
  ,table = layui.table;

  	//从cookie中获取当前登录用户
	var resavepeople = getCookie1("name").replace(/"/g,'');
	console.log("当前登录人为:"+resavepeople);
	
	//点击完成按钮操作
	$("#finish_task").click(function(){
		//验证表单是否为空
		  if($("#comment_finish").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("处理说明不能为空", {icon: 7, offset: '100px'});
			  return;
		  }

		  if($('#imgZmList').children().length == 0){
			  layer.msg("现场施工图必须上传", {icon: 7, offset: '100px'});
			  return;
		  }
		 $("#finish_task").attr({"disabled":"disabled"});
		  
		  //上传问题图片
	   	  uploadList.upload();
	})
	
	/**
	 * 完成作业下一步
	 */
	function finishTask(){
	     //图片上传成功后，清空图片列表
		 $('#imgZmList').empty();
		 $("#finish-phos").css({"display":"none"});
		 
		 processNode();	
	}
	
	/**
	 * 完成作业节点
	 */
	function processNode(){
		$.ajax({  
		    async : false,
	    	url : "/iot_process/process/nodes/next/group/piid/"+piid,   ///iot_process/estimates/problemdescribe
	        type : "put",
	        data : {
        		"comment": $("#comment_finish").val(),
        		"complementor": resavepeople,
        		"userName": resavepeople,
        		"operateName": "完成作业"
            },
            contentType: "application/x-www-form-urlencoded",
	        dataType : "json",  
	        success: function(jsonData) {
	        	if(jsonData.data == true){
			   		$("#comment_finish").val("");
			   		layer.msg("完成作业提交成功",{icon: 1, time:2000, offset: '100px'}, function(){
			   			top.location.href = "http://10.89.90.118:10239/CZ_PIOTMS/index.action";
			   		});
	        	}else{
	        		layer.msg("完成作业提交失败",{icon: 2, time:2000, offset: '100px'});
	        	}
	        	$("#finish_task").removeAttr("disabled");
	        } 
	        ,error:function(){
	        	layer.msg("完成作业提交失败， 请检查网络是否正常",{icon: 2, time:2000, offset: '100px'});
	        	$("#finish_task").removeAttr("disabled");
	        }	
	   });
	}
	
	//多图片上传功能
	var imgCount = 0;
    var uploadList = upload.render({
         elem: '#addFinishImg'
         , url: '/iot_process/report/upload'
         , data: {		resavepeople: function(){ return resavepeople;}, 
        	 			username: function(){ return toChar(resavepeople);},
			  			piid: function(){console.log("piid: "+piid); return piid;},
			  	   		tProblemRepId: function(){ console.log("tProblemRepId: "+tProblemRepId); return tProblemRepId;},
			  			remark: "1"
         			}
         , accept: 'images'
         , multiple: true
         , auto:false
         , bindAction: '#'
         , choose: function (obj) {
       	  
       	  //将每次选择的文件追加到文件队列
         	var files = this.files = obj.pushFile();
         	$("#finish-phos").css({"display":"block"});
         	
          //预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
          obj.preview(function (index, file, result) {
              console.log(index);
              imgCount++;
              $('#imgZmList').append('<li style="position:relative"><img style="margin:0px; height:150px; width:180px;" name="imgZmList" src="' + result + '"><div class="img_close" onclick="deleteElement(this)">X</div></li>');
              //删除列表中对应的文件
              $(".img_close").click(function(){
             	 delete files[index]; //删除对应的文件
             	 imgCount--;
             	 var len = $('#imgZmList').children().length;
             	 if(len <= 0){
             		 $("#finish-phos").css("display", "none");
             	 }
             	 uploadList.config.elem.next()[0].value = ''; //清空 input file值，以免删除后出现同名文件不可选
              })
              
              form.render();
           });
         }
     	   //上传前执行的函数
         ,before: function(obj){
        	console.log("------------------------------------");
 			console.log("imgCount="+imgCount);
 			if(imgCount > 0){
 				layer.load(1,{offset: '100px'}); 
 			}
   	    }
          //上传完毕后的，回调函数
         , done: function (res, index, upload) {
        	 if(res.state == 0){
        		 if(imgCount != 0){
        			 imgCount--; 
        		 }
        		 console.log("删除上传的图片");
				 delete this.files[index];  //删除上传成功的文件
			  }else{
				layer.closeAll('loading');
			  }
			  console.log("imgCount1="+imgCount);
			  if(imgCount == 0 ){
				 //所有图片上传完毕后，开始执行完成作业流程
				 layer.closeAll('loading');
				 console.log("图片上传成功，开始流程。。。。。。")
				 finishTask();
			  }
			  $("#finish_task").removeAttr("disabled");
         }
          //上传失败时，回调函数
         ,error: function(index, upload){
        	layer.closeAll('loading');
        	layer.msg("图片上传失败, 请检查网络是否正常",{icon: 2, offset:'100px'});
        	$("#finish_task").removeAttr("disabled");
         }
     });
    
    //验证是否上传现场施工图
   /* form.verify({
    	imgs: function(value, item){  //value：表单的值、item：表单的DOM对象
    		if($('#imgZmList').children("li").length < 1){
    			layer.msg("必须上传现场施工图",{icon: 2, offset:'100px'});
    			return "1";
    		}
    	}
    });*/

      /**
	   * 汉字转成拼音的功能
	   */
	  function toChar(str){
		 return pinyin.getFullChars(str);
	  }
    
	  /**
	   * 按钮鼠标移入/移除事件
	   */
	  $(".primary-btn").mouseover(function(){
		  $(this).find("i").css({"color":"white"});
	  })
	   
	  $(".primary-btn").mouseout(function(){
		  $(this).find("i").css({"color":"green"});
	  })
	  
});
