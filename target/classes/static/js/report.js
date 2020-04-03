//加载layui内置模块
layui.use(['jquery','form','upload','layer','layedit'], function(){
	var form = layui.form
	,layer = layui.layer
	,layedit = layui.layedit
	,$ = layui.$ //重点处,使用jQuery
	,upload = layui.upload;
	

	//从cookie中获取当前登录用户
	var resavepeople = getCookie1("name").replace(/"/g,'');
	//上报部门
	var depet = getCookie1("organ").replace(/"/g,'');
	if(depet == 'HSE办'){
		depet = 'HSE办公室';
	}
	var piid = GetQueryString("piid");
	//暂存的问题报告id和上报问题报告id和
	var tProblemRepId = null, tempRepId = null;
	//0-表示暂存，1-表示上报
	var type = 0,problemclasses = ["分厂级检查","工段级检查","不安全行为/状态","其他"];

	//验证表单是否为空
	function isempty(){
		console.log(123);
		if($("#problemdescribe").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("问题描述不能为空", {icon: 7, offset: ['150px', '330px']});
			  return true;
		}
		if($("#problemtype").val() == ''){
			  layer.msg("属地单位不能为空", {icon: 7, offset: ['150px', '330px']});
			  return true;
		}
		if($("#profession").val() == ''){
			  layer.msg("所属专业不能为空", {icon: 7, offset: ['150px', '330px']});
			  return true;
		}
		if($("#problemclass").val() == ''){
			  layer.msg("问题类别不能为空", {icon: 7, offset: ['150px', '330px']});
			  return true;
		}
		if($("#rfid").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("设备位号不能为空", {icon: 7, offset: ['150px', '330px']});
			  return true;
		}
		if($("#welName").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("问题区域不能为空", {icon: 7, offset: ['150px', '330px']});
			  return true;
		}
		 if($.inArray($("#problemclass").val(), problemclasses) != -1){
			 
			 console.log($("#problemclass").val())
			 if($('#imgZmList').children().length == 0){
					layer.msg("现场施工图必须上传", {icon: 7, offset: '100px'});
					return true;
				}
		 }
			
		
		return false;
	}
	
	//根据piid判断是否是回退到问题上报节点
	if(piid != null && piid != ""){
		$("#problem_back").css({"display":"block"});
	}else{
		$("#problem_report").css({"display":"block"});
	}
	
	  //隐藏字段初始赋值
	  form.val('report-form', {
	    "depet": depet,
	    "applypeople": resavepeople
	  })
	  
	  //上报人输入框校验
	  var ch,msg=null;
	  var userVerify = form.verify({
		  "applypeople": function(value, item){ 
			  if(!(value.indexOf(resavepeople) >= 0)){
				  return "上报人必须包含当前登录人";
			  }
			  
			  ch = value.split("，");
			  console.log(ch);
			  for(var i=0;i<ch.length;i++){

				  if(ch[i].length == 0 || new RegExp("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s]+").test(ch[i])){

					  return '上报人不能有特殊字符，用户名之间请以中文逗号(，)隔开';
				  }
			  }
			  
			  if(msg != null){
				  return msg;
			  }
		  }
	  })
	  
	  $("#applypeople").blur(function(){
		  ch = $(this).val().split("，");
		  //后台校验每个用户名是否合法
			  $.ajax({
				 async: false,
				 type: "POST",
				 url: "/iot_process/report/verifyuser",
				 data: {"userList":ch},
				 dataType: "json",
				 success: function(data){
					 if(data.state == 0){
						msg = null; 
					 }else{
						 msg = data.message;
					 }
				 }
			  })  
	  })
	
	  //监听问题类别select选择
	  form.on('select(question-type)', function(data){
	    if(data.value == "不安全行为/状态"){
	    	$("#div-notsafe").css({"display":"block"});
	    }else{
	    	$("#div-notsafe").css({"display":"none"});
	    }
		//更新不安全select渲染
	    form.render('select','notsafe-select');
	  }); 
	  
	//异步请求，从数据库中读取问题属地对应区域
	  var problemTypeList;
	  $.ajax({  
        type: 'get',  
        async: false,
        url: '/iot_process/report/problemtype/area', // ajax请求路径   
        success: function(data){ 
          if(data.state == 0 && data.data != null){
        	problemTypeList = data.data;
          	for( x in problemTypeList){
          		var $option1 = $("<option></option>");
          		$option1.val(problemTypeList[x].problemName);
          		$option1.text(problemTypeList[x].problemName);
          		$("#problemtype").append($option1);  
          	}
      		form.render();
          }
        }
    });
	
	  //同步请求，从数据库中读取不安全数据列表
	  var unsafeList;
	  $.ajax({  
        type: 'get',  
        async: false,
        url: '/iot_process/report/unsafe/showlist', // ajax请求路径   
        success: function(data){ 
          if(data.state == 0 && data.data != null){
          	unsafeList = data.data;
          	for( x in unsafeList){
          		var $option1 = $("<option></option>");
          		$option1.val(unsafeList[x].typesName);
          		$option1.text(unsafeList[x].typesName);
          		$("#notsafe-select").append($option1);            		
          	}
          	
          	var actionList = unsafeList[0].unsafeAction;
      		for( y in actionList){
      			var $option2 = $("<option></option>");
          		$option2.val(actionList[y].actionsName);
          		$option2.text(actionList[y].actionsName);
          		$("#detail-select").append($option2);
      		}
      		//$("#detail-select").append("<option value='其他'>其他</option>");
            //更新不安全select渲染
    	    form.render('select','notsafe-select');
          }
        }
    });
	  
	  
	  
	//异步请求，从数据库中读取当前登录用户的暂存问题报告数据
	 var imgList = new Array(); 
	 $.ajax({  
        type: 'get',  
        url: ((piid != null && piid != "")?('/iot_process/report/reload'):('/iot_process/report/show')), // ajax请求路径   
        data: ((piid != null && piid != "")?{"piid":piid}:{"resavepeople":resavepeople}), 
        success: function(data){ 
          if(data.state == 0 && data.data != null && data.data != ""){
        	  tempRepId = data.data.tproblemRepId;
        	  console.log("tempRepId="+tempRepId);
        	  
        	  //根据属地单位判断问题区域
      		  selectWelName(data.data.problemtype);
        	  
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
      			$("#div-notsafe").css({"display":"block"});
       	    	
       	    	for( x in unsafeList){
       		      if(data.data.remarkfive == unsafeList[x].typesName){
       		    	var actionList = unsafeList[x].unsafeAction;
       		    	$("#detail-select").empty();  //清空子选项
       	            for( y in actionList){
       	            	var $option2 = $("<option></option>");
       		            $option2.val(actionList[y].actionsName);
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
	
    //监听问题区域级联
	form.on('select(problem-area)', function(data){
	   var value = data.value;
	   console.log(value);
	   selectWelName(value);
	});
	
	function selectWelName(value){
		$("#welName").empty();  //清空子选项
		$("#welName").append("<option value=''>请选择</option>");
		for( x in problemTypeList){
		    if(value == problemTypeList[x].problemName){
		    var areaList = problemTypeList[x].problemAreas;
	        for( y in areaList){
	            var $option2 = $("<option></option>");
		        $option2.val(areaList[y].problemName);
		        $option2.text(areaList[y].problemName);
		        $("#welName").append($option2);
	         }
	          $("#welName").append("<option value='其他'>其他</option>");
		    }
		}
		//更新select渲染
		form.render();
	}
    
	//监听不安全行为级联
	form.on('select(notsafe)', function(data){
	   var value = data.value;
	   for( x in unsafeList){
	      if(value == unsafeList[x].typesName){
	    	var actionList = unsafeList[x].unsafeAction;
	    	$("#detail-select").empty();  //清空子选项
            for( y in actionList){
            	var $option2 = $("<option></option>");
	            $option2.val(actionList[y].actionsName);
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
		  
		  if(isempty()){
			 return false;
		  }
		  //var jsonData = JSON.stringify(data.field);
		  //保存主键
		  if(tempRepId != undefined || tempRepId != null){
			  data.field.tProblemRepId = tempRepId;
		  }
		  //保存当前登录人
		  data.field.resavepeople = resavepeople;
		  //设置上报时间
		  data.field.applydate = new Date();
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
			    async:false,
		   		type: "POST",
		   		url: "/iot_process/report/",
		   		data: data.field,
		   		dataType: "json",
		   		success: function(data){
		   			if(data.data != null && data.data != ""){
		   				tempRepId = tProblemRepId = data.data;
		   				type = 0;
			   			//上传问题图片
				   		uploadList.upload();
				   		layer.msg("问题暂存成功", {icon: 1, offset: ['150px', '330px']});
		   			}else{
		   				layer.msg("问题暂存失败", {icon: 2, offset: ['150px', '330px']});
		   			}
		   			
		   		},
		   		error: function(){
			   		layer.msg("问题暂存失败", {icon: 2, offset: ['150px', '330px']});
			   	}
		  })
	    return false;
	  });
	
	function imgCount(){
		 var len = $('#imgZmList').children().length;
     	 if(len <= 0){
     		 $("#problem-img").css("display", "none");
     	 }
	}  
	    
	//多图片上传功能
     var uploadList = upload.render({
          elem: '#addProblemImg'
          , url: '/iot_process/report/upload'
          , data: {		resavepeople: function(){ return resavepeople}, 
        	  			username: function(){ return toChar(resavepeople);},
        	  			piid: function(){console.log("piid: "+piid); return piid;},
        	  	   		tProblemRepId: function(){ console.log("tProblemRepId: "+tProblemRepId); return tProblemRepId;},
        	  			remark: "0"
          		 }
          , accept: 'images'
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
               $('#imgZmList').append('<li style="position:relative" id="'+ index +'"><img name="imgZmList"  src="' + result + '"width="180" height="150"><div class="img_close" onclick="deleteElement(this)">X</div></li>');
               //删除列表中对应的文件
               $(".img_close").click(function(){
              	 delete files[index]; //删除对应的文件
              	 imgCount();
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
        		 if(type == 1){
        		  delete this.files[index];
        		 }     		
        	  }
          }
           //上传失败时，回调函数
          ,error: function(index, upload){
        	  //layer.msg("图片上传失败");
          }
      });
     
 	//监听流程上报提交事件
	  form.on('submit(problem_report)', function(data){
		 if(isempty()){
			return false;
		 }
		 console.log("问题上报开始...");
		  //保存当前登录人
		  data.field.resavepeople = resavepeople;
		  //设置上报时间
		  data.field.applydate = new Date();
		  console.log(data.field);
		 //流程上报：
		 //dfid为流程定义id（暂时就是dfid="processPure2:4:47506"）
		 $.ajax({
			  async:false
		     ,type: "POST"
		     ,url: '/iot_process/process'    //dfid为流程定义id（暂时就是dfid="processPure2:4:47506"）
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
		    		
		    		//上报成功，删除暂存数据
		    		$.ajax({
		    			type: "POST",
		    			url: "/iot_process/report/deletereport",
		    			data: {"repid":tempRepId},
		    			dataType: "json",
		    			success: function(json){
		    				console.log(json.message);
		    			}
		    		});
		    		
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
			    	type = 1;
		    		//上传问题图片
			    	uploadList.upload();
			    	layer.msg("问题上报成功",{icon: 1, offset: ['150px', '330px']});
			    	$("#problemdescribe").val("");
			    	$('#imgZmList').empty();
			    	imgCount();
		    	}else{
		    		layer.msg("问题上报失败",{icon: 2, offset: ['150px', '330px']});
		    	}
		     }
		     ,error:function(){}		       
		});
		 	 
	    return false;
	  });
	  
	  
	 //监听回退后问题上报提交事件
	  form.on('submit(problem_report_again)', function(data){
		 if(isempty()){
			 return false;
		 }
		 console.log("回退_问题上报开始...");
		 //保存主键
		  data.field.tProblemRepId = tempRepId;
		  //保存当前登录人
		  data.field.resavepeople = resavepeople;
		  //保存piid
		  data.field.piid = piid;
		  //设置上报时间
		  data.field.applydate = new Date();
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
			    async:false,
		   		type: "POST",
		   		url: "/iot_process/report/",
		   		data: data.field,
		   		dataType: "json",
		   		success: function(data){
		   			if(data.data != null && data.data != ""){
		   				tempRepId = tProblemRepId = data.data;
		   				var result = false;  //上报返回响应结果
		   				//回退后，问题上报
		   				$.ajax({
		   					 async: false
			   			     ,type: "PUT"
			   			     ,url: '/iot_process/process/nodes/next/piid/'+piid    //piid为流程实例id
			   			     ,data: {
			   			     	"comment": $("#problemdescribe").val()     //节点的处理信息     	
			   			     	,"area": $("#problemtype").val()
			   			     	,"operateName": "上报"
			   			     }  
			   			     ,contentType: "application/x-www-form-urlencoded"
			   			     ,dataType: "json"
			   			     ,success: function(jsonData){
			   			     	//后端返回值： ResultJson<Boolean>
			   			    	 result = jsonData.data;
			   			     }
			   			     ,error:function(){
			   			     }		       
		   			    });
		   				
				   		if(result){
				   			type = 1;
				    		//上传问题图片
					    	uploadList.upload();
					    	layer.msg("问题上报成功",{icon: 1, offset: ['150px', '330px']});
					    	$("#problemdescribe").val("");
					    	$('#imgZmList').empty();
					    	imgCount();
				   		}else{
				   			layer.msg("问题上报失败", {icon: 2,offset: ['150px', '330px']});
				   		}
		   			}else{
		   				layer.msg("问题上报失败", {icon: 2, offset: ['150px', '330px']});
		   			}
		   			
		   	  },
		   	  error: function(){
		   		layer.msg("问题上报失败", {icon: 2, offset: ['150px', '330px']});
		   	  }
		  })
	    return false;
		
	  });
	  
	  /**
	   * 问题批量上报
	   */
	  $("#saveMassBtn").click(function(){
		  console.log("-----问题批量上报开始--------");
		  layer.open({
		        type: 1
		        ,title: '问题批量上报'
		        ,id: 'problemmass'
		        ,size: '51200'
		        ,btn: ['上&nbsp;&nbsp;报','关&nbsp;&nbsp;闭']
		  		,offset: ['50px','100px']
		  		,area: ['650px','335px']
		        ,content:$("#problemMass")
		        ,yes: function(index, layero){
		        	if($("#problemmass").find("span.layui-upload-choose").length == 1){
						uploadTepmlate.upload();
					}
		        }
		        ,btn2: function(index, layero){
		        	 $("#response-div").css({"display": "none"});
		        }
		        ,success: function(){
		        	 console.log("success");
		        	 $(".layui-layer .layui-layer-btn0").prepend("<i class='layui-icon'>&#x1005;</i>&nbsp;&nbsp;");
		    	     $(".layui-layer .layui-layer-btn1").prepend("<i class='layui-icon'>&#x1006;</i>&nbsp;&nbsp;");
		    	     $(".layui-layer .layui-layer-btn0").addClass("primary-btn");
		    	     $(".layui-layer .layui-layer-btn1").addClass("primary-btn");
		    	     $(".layui-layer .layui-layer-btn0,.layui-layer .layui-layer-btn1").mouseover(function(){
		    	        $(this).find("i").css({"color":"white"});
		    	     })
		    	        				  		   
		    	     $(".layui-layer .layui-layer-btn1,.layui-layer .layui-layer-btn0").mouseout(function(){
		    	        $(this).find("i").css({"color":"green"});
		    	     })
		        }
		  });
	  })
	  
     var uploadTepmlate = upload.render({
	        			   elem: '#upload-excel-btn'
	        			   ,url: '/iot_process/report/upload/template'
	        			   ,auto: false
	        			   ,data: {
	        			       depet: function(){ return depet;},
	        			       resavepeople: function(){ return resavepeople; }
	        			   }
	        			   ,accept:'file'
	        			   ,bindAction: '#'
	        			   ,done: function(res, index){
	        			     //请求完成后回调
	        			     $("#response-div").css({"display": "block"});
	        			     layer.closeAll('loading'); 
	        			     if(res.state == 0 && res.message != null){
	        				     if(res.message.match("上报失败") || res.message.match("成功")){
	        				        $("#reponse-text").text(res.message);
	        				     }else{
	        				    	 $("#reponse-text").text("数据验证错误 - "+res.message);
	        				     }
	        				 }else if(res.state == 1){
	        				      $("#reponse-text").text(res.message);
	        				   }else{
	        				      layer.msg("连接服务器失败，请检查网络是否正常", {icon: 2, offset: ['180px', '300px']});
	        				   }

	        		       }
	        			   ,before: function(res){
	        			      //文件提交上传前的回调
	        			      layer.load(1,{offset: ['190px', '400px']}); 
	        			   }
	        			   ,error: function(){
	        			      //请求异常回调
	        			       $(".layui-layer-dialog .layui-layer-padding:contains('请求上传接口出现异常')").css({"display":"none"});
	        			        	layer.msg("连接服务器失败，请检查网络是否正常", {icon: 2, offset: ['180px', '300px']});
	        			        	layer.closeAll('loading');
	        			    }
	     });
	    
	  
	  
     
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
	  
	  /**
	   * 定位设备弹窗
	   */
	  $("#rfid-btn").click(function(){
		  console.log("rfid");
		  //获取问题区域选择的值
		 var welName =  $.trim($("#welName").val());
		 console.log("welName: "+ welName);
		  $.cookie('welName', welName, { path: '/' }); 
		  
		  layer.open({
		    	title: '设备定位',
		    	type: 2,
		    	id: 'equipmentInfo',
		    	btn: ['确&nbsp;&nbsp;认','取&nbsp;&nbsp;消','其它设备'],
		    	offset: ['45px','50px'],
		    	area: ['90%','82%'],
		        content: './equipment-location.html',
		        yes: function(index, layero){
		        	//获取iframe窗口的body对象
		        	var body = layer.getChildFrame('body', index);
		        	//找到body对象下被选中的设备位号值
		        	var value = body.find(".layui-table-click td[data-field='equPositionNum']").find("div").text();
		        	$("#rfid").val(value);
		        	layer.close(index); //如果设定了yes回调，需进行手工关闭
		        },
		  		btn3: function(index, layero){
		  			$("#rfid").val("其它设备");
		  			layer.close(index);
		  		},
		  		success: function(){
		  		  /**
		  		   * 按钮css样式
		  		   */
		  		  $(".layui-layer-btn0").css({
		  			  "background-color":"#f1f5f7",
		  			  "color":"black",
		  			  "border":"1px solid rgb(230,230,230)"
		  		  });
		  		  $(".layui-layer-btn0").prepend("<i class='layui-icon'>&#x1005;</i>&nbsp;");
		  		  $(".layui-layer-btn1").prepend("<i class='layui-icon'>&#x1007;</i>&nbsp;");
		  		 
		  		 $(".layui-layer-btn0").mouseover(function(){
		  			$(".layui-layer-btn0").css({
		  				"color":"white",
		  				"background-color":"#3a95ca"
		  			})
		  		 })
		  		 $(".layui-layer-btn0").mouseout(function(){
		  			$(".layui-layer-btn0").css({
		  				"background-color":"#f1f5f7",
			  			"color":"black",
		  			})
		  		 })
		  		 $(".layui-layer-btn0,.layui-layer-btn1,.layui-layer-btn2").mouseover(function(){
		  			  $(this).find("i").css({"color":"white"});
		  		  })
		  		   
		  		  $(".layui-layer-btn0,.layui-layer-btn1,.layui-layer-btn2").mouseout(function(){
		  			  $(this).find("i").css({"color":"green"});
		  		  })
		  		}
		  });
	  });
  	
})  

window.onload = function(){
	 /**
	 * 窗口调整大小
	 */
	function windowResize(){
		if(window.innerWidth < 990){
	  		$("#problemdescribe").css({"width": "86%"});
	  	}else if(window.innerWidth < 1200){
	  		$("#problemdescribe").css({"width": "84%"});
	  	}else{
	  		$("#problemdescribe").css({"width": "77%"});
	  	}
	}
	windowResize();
	window.onresize = function(){
		windowResize();
	}
}
	