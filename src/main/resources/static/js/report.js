//加载layui内置模块
layui.use(['jquery','form','upload','layedit'], function(){
	var form = layui.form
	,layer = layui.layer
	,layedit = layui.layedit
	,$ = layui.$ //重点处,使用jQuery
	,upload = layui.upload;
	
	//从cookie中获取当前登录用户
	//var resavepeople = getCookie1("currentName");
	var resavepeople = "test4";
	console.log("currentLoginUser:"+resavepeople);
	//上报问题报告id和piid
	var tProblemRepId = null, piid = null;
	//暂存的问题报告id
	var tempRepId = null;
	
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
        async: false,
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
        }
    });
	  
	//异步请求，从数据库中读取当前登录用户的暂存问题报告数据
	 var imgList = new Array(); 
	 $.ajax({  
        type: 'get',  
        url: '/iot_process/report/show', // ajax请求路径   
        data: {resavepeople:resavepeople}, 
        success: function(data){ 
          if(data.state == 0 && data.data != null && data.data != undefined){
        	  tempRepId = data.data.tproblemRepId;
        	  console.log("tempRepId="+tempRepId);
        	  
              //表单初始赋值
      		  form.val('report-form', {
      		    "applypeople": data.data.applypeople 
      		    ,"problemtype": data.data.problemtype
      		    ,"welName": data.data.welName
      		    ,"profession": data.data.profession
      		    ,"rfid": data.data.rfid 
      		  // ,"remarkfive": data.data.remarkfive
      		  // ,"remarksix": data.data.remarksix
      		    ,"problemdescribe": data.data.problemdescribe 
      		    ,"problemclass": data.data.problemclass 
      		  })
      		  
      		  //判断问题类别是否是 "不安全行为/状态"
      		 if(data.data.problemclass == "不安全行为/状态"){
         		$("#notsafe-select").removeAttr("disabled");
       	    	$("#detail-select").removeAttr("disabled");
       	    	
       	    	for( x in unsafeList){
       		      if(data.data.remarkfive == unsafeList[x].typesID){
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
       	    	
        		form.val('report-form', {
        			"remarkfive":data.data.remarkfive
        			,"remarksix":data.data.remarksix
        		})
         	  }
      		  
      		  //加载显示暂存的图片
      		  if(data.data.reportPhos.length > 0){
      			for(var i=0; i<data.data.reportPhos.length; i++){
      				var tProblemPhoId = data.data.reportPhos[i].tproblemPhoId;
      				var phoAddress = data.data.reportPhos[i].phoAddress;
      				imgList.push(tProblemPhoId);
      				$('#imgZmList').append('<li style="position:relative"><img class="imgList" id="'+tProblemPhoId+'" src="' + phoAddress + '" width="180" height="150"><div class="img_close" onclick="deleteElement(this)">X</div></li>');
      				$(".img_close").click(function(){
      	              	 var len = $('#imgZmList').children().length;
      	             	 if(len <= 0){
      	             		 $("#problem-img").css("display", "none");
      	             	 }
      				})
      			}
      			$("#problem-img").css({"display":"block"});
      		  }
      		console.log("加载的图片列表："+imgList);
          }
        }
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
	
	  //监听暂存提交事件
	  form.on('submit(saveBtn)', function(data){
		  //var jsonData = JSON.stringify(data.field);
		  //保存主键
		  if(tempRepId != undefined || tempRepId != null){
			  data.field.tProblemRepId = tempRepId;
		  }
		  //保存当前登录人
		  data.field.resavepeople = resavepeople;
		  console.log(data.field);
		  
		  $(".imgList").each(function(index){
			  imgList.splice(index,1,"");
		  })	  
		  console.log(imgList);
		  if(imgList.length > 0){
			  data.field.imgList = imgList;
		  }
		  
		  //判断问题类别是否选择的是不安全行为/状态
		  if(data.field.problemclass != "不安全行为/状态"){
			  data.field.remarkfive = null;
			  data.field.remarksix = null;
		  }
		  //异步请求后端保存数据
		   $.ajax({
		   		type: "POST",
		   		url: "/iot_process/report/",
		   		data: data.field,
		   		dataType: "json",
		   		success: function(data){
		   			if(data.data != null){
		   				tempRepId = tProblemRepId = data.data;
			   			//上传问题图片
				   		uploadList.upload();
		   			}
		   			
		   		}
		  })
	    return false;
	  });
	  
	//多图片上传功能
     var uploadList = upload.render({
          elem: '#addProblemImg'
          , url: '/iot_process/report/upload'
          , data: {		resavepeople: function(){ return resavepeople;}, 
        	  			piid: function(){console.log("piid: "+piid); return piid;},
        	  	   		tProblemRepId: function(){ console.log("tProblemRepId=="+tProblemRepId); return tProblemRepId;}
          			}
          , accept: 'images'
          , number: 3
          , multiple: true
          , auto:false
          , bindAction: '#'
          , choose: function (obj) {
        	  
        	//将每次选择的文件追加到文件队列
          	var files = this.files = obj.pushFile();
          	$("#problem-img").css({"display":"block"});
          	
          //预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
           obj.preview(function (index, file, result) {
               console.log(index);
               // $('#imgZmList').append('<li style="position:relative"><img name="imgZmList" src="' + result + '"width="150" height="113"><div class="title_cover" name="imgZmName" onclick="divClick(this)"></div><div class="img_edit layui-icon" onclick="croppers_pic(this)">&#xe642;</div><div class="img_close" onclick="deleteElement(this)">X</div></li>');
               $('#imgZmList').append('<li style="position:relative"><img name="imgZmList" src="' + result + '"width="180" height="150"><div class="img_close" onclick="deleteElement(this)">X</div></li>');
               //删除列表中对应的文件
               $(".img_close").click(function(){
              	 delete files[index]; //删除对应的文件
              	 var len = $('#imgZmList').children().length;
             	 if(len <= 0){
             		 $("#problem-img").css("display", "none");
             	 }
              	 console.log(index);
              	 uploadList.config.elem.next()[0].value = ''; //清空 input file值，以免删除后出现同名文件不可选
               })
               
               form.render();
            });
          }
      	   //上传前执行的函数
          ,before: function(obj){
    	     
    	  }
           //上传完毕后的，回调函数
          , done: function (res, index, upload) {
        	 // layer.closeAll('loading'); //关闭loading
        	  console.log(res);
        	  if(res.state == 0){
        		  delete this.files[index];  //删除上传成功的文件
        	  }else{
        		 // this.error(index, upload);
        	  }
          }
           //上传失败时，回调函数
          ,error: function(index, upload){
        	 // layer.closeAll('loading'); //关闭loading
        	 // upload();   //重新上传
        	  console.log("error");
          }
      });
     
 	//监听流程上报提交事件
	  form.on('submit(problem_report)', function(data){
		 console.log("问题上报开始...");
		 console.log(data.field);
		 //流程上报：
		 //dfid为流程定义id（暂时就是dfid="processPure2:1:37516"）
		 $.ajax({
		     type: "POST"
		     ,url: '/iot_process/process/processPure2:1:37516'    //dfid为流程定义id（暂时就是dfid="processPure2:1:37516"）
		     ,data: data.field  //问题上报表单的内容
		     ,contentType: "application/x-www-form-urlencoded"
		     ,dataType: "json"
		     ,success: function(jsonData){
		     	//后端返回值： data="piid,bsid"(流程实例piid,业务数据bsid)
		    	 var data = jsonData.data;
		    	if(data != undefined || data != null){
		    		var arr = data.split(",");
		    		console.log("arr = " + arr);
		    		piid = arr[0];
		    		tProblemRepId = arr[1];
		    		
		    		//获取暂存图片列表
			    	  $(".imgList").each(function(index){
			  			  imgList.splice(index,1,"");
			  		  })	  
		    		
		    		//更新暂存的图片piid和问题上报主键id
		    		$.ajax({
		    			type: "POST",
		    			url: "/iot_process/report/updatepho",
		    			data: {"imgList":imgList, "tProblemRepId":tProblemRepId, "tempRepId":tempRepId, "piid":piid},
		    			dataType: "json",
		    			success: function(json){
		    				console.log("暂存图片流程实例piid,业务数据bsid更新完毕");
		    			}
		    		});
		    		
		    		//上传问题图片
			    	uploadList.upload();
		    	}
		     }
		     ,error:function(){}		       
		});
		 	 
	    return false;
	  });
	  
     
})  
	