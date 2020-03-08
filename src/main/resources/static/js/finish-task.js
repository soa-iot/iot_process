layui.use(['form','tree','jquery','upload','layer','table'], function(){
  var form = layui.form
  ,	$ = layui.$
  ,layer = layui.layer
  ,upload = layui.upload
  ,tree = layui.tree
  ,table = layui.table;
  	
  	//动设备设备类别
    var equipMove = ['K类设备', 'P类设备'];
    //是否是动设备
    var isMove = false;
  	//从cookie中获取当前登录用户
	var resavepeople = getCookie1("name").replace(/"/g,'');
	console.log("当前登录人为:"+resavepeople);
	/**
	 * 页面加载时，初始化检维修弹窗信息
	 */
	function initPage(){
		var positionNum = $.trim($("#rfid").val());
		if(positionNum != null && positionNum != '' && positionNum != '其它设备' && positionNum != '其他设备'){
			$("#positionNum").val(positionNum);
			$("#repairPerson").val(resavepeople);
			//异步获取设备名称和设备型号
			$.ajax({
				type: "POST",
				url: "/iot_process/equipment/show",
				data: {
					'equPositionNum': positionNum
				},
				dataType: "json",
				success: function(json){
					if(json.code == 0){
						var data = json.data;
						if(data != null && data.length != 0){
							if(equipMove.toString().indexOf(data[0].equMemoOne) >= 0){
								$("#repair-div").css({"display": "block"});
								isMove = true;
								$("#equipName").val(data[0].equName);
								$("#serialNum").val(data[0].equModel);
							}
						}
					}
				},
				error: function(){
				}
			});
		}
	}
	initPage();
	
	//点击完成按钮操作
	form.on('submit(finish_task)', function(data){
		  //验证表单是否为空
		  if($("#comment_finish").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("处理说明不能为空", {icon: 7, offset: '100px'});
			  return;
		  }
		  if(isMove && ($.trim($("#repairReason").val()) == '' || $.trim($("#repairContent").val()) == '')){
			  layer.msg("请填写检维修信息", {icon: 7, offset: '100px'});
			  return; 
		  }
		  //验证检维修信息
		  
		  $("#finish_task").attr({"disabled":"disabled"});
		  if($('#imgZmList').children().length == 0){
			  layer.msg("现场施工图必须上传", {icon: 7, offset: '100px'});
			  return;
		  }
		  console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
		  //上传问题图片
	   	  uploadList.upload();
		  
		  //return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
   });
	
	/**
	 * 完成作业下一步
	 */
	function finishTask(){
	     //图片上传成功后，清空图片列表
		 $('#imgZmList').empty();
		 $("#finish-phos").css({"display":"none"});
		 
		 if(isMove){
			 //保存检维修信息
			 var replacement = '';
			 $("td[data-field='content']").each(function(index,element){
				 if($.trim($(this).text()) != ''){
					 replacement += $.trim($(this).text()) + ':' + $.trim($("td[data-field='number']")[index].innerText) + ";";
				 }
			 })
			 
			 $.ajax({
				async: false,
				type: 'POST',
				url: '/iot_process/report/save/repairinfo',
				data: {
					'piid': piid,     //流程id
					'supervisoryperson': $.trim($("#repairPerson").val()).replace(/，/g, ','),  //检修人员
					'impacts': $.trim($("#repairReason").val()),            //检修原因
					'action': $.trim($("#repairContent").val()),             //检修内容
					'processDesc': replacement,        //更换备品备件规格和数量
					'others': $.trim($("#repairComment").val()),              //备注
					'equipName': $.trim($("#equipName").val()),
					'serialNum': $.trim($("#serialNum").val()),
					'positionNum': $.trim($("#positionNum").val())
				},
				dataType: 'json',
				success: function(json){
					if(json != null && json.state == 0){
						processNode();
					}else{
						layer.msg("保存检维修信息失败",{icon: 2, offset:'100px'});
					}
					$("#finish_task").removeAttr("disabled");
				},
				error: function(){
					layer.msg("连接服务器失败",{icon: 2, offset:'100px'});
					$("#finish_task").removeAttr("disabled");
				}
			 });
		 }else{
			 processNode();
		 }	
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
         }
          //上传失败时，回调函数
         ,error: function(index, upload){
        	layer.closeAll('loading');
        	layer.msg("图片上传失败, 请检查网络是否正常",{icon: 2, offset:'100px'});
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
     * 动设备检维修信息录入弹窗
     */
    $("#repair-info-btn").click(function(){
    	console.log("动设备检维修信息录入弹窗...");
    	 layer.open({
		    	title: '动设备检维修信息录入',
		    	type: 1,
		    	id: 'repairInfo',
		    	btn: ['确&nbsp;&nbsp;认','取&nbsp;&nbsp;消'],
		    	btnAlign: 'c',
		    	offset: '45px',
		    	area: ['800px','85%'],
		        content: $("#repair-info-window"),
		        yes: function(index, layero){
		        	
		        	//检修原因和检修内容非空判断
		        	if($.trim($("#repairReason").val()) == '' || $.trim($("#repairReason").val()) == null){
		        		layer.msg("检修原因不能为空",{icon: 7, time:2000, offset: '150px'});
		        		return;
		        	}
		        	
		        	if($.trim($("#repairContent").val()) == '' || $.trim($("#repairContent").val()) == null){
		        		layer.msg("检修内容不能为空",{icon: 7, time:2000, offset: '150px'});
		        		return;
		        	}
		        	
		        	layer.close(index); //如果设定了yes回调，需进行手工关闭
		        },
		  		success: function(){
		  		
		  		}
		  });
    })
    
    /**
     * 人员组织树弹窗
     */
    $("#chose-user-btn").click(function(){
    	console.log("人员组织树弹窗...");
    	//弹出层
		layer.open({
			type: 1
			,offset: '45px'
			,area: ['300px','400px;']
			,id: 'user_organization' //防止重复弹出
			,content: $("#task_tree")
			,btn: ['确认',"取消"]
			,btnAlign: 'c' //按钮居中
			,yes: function(index, layero){
				//确认按钮的回调函数
				var currentPerson = $("#repairPerson").val();
				if(assignUsers != null && assignUsers.length != 0){
					if(currentPerson.match(/[,，]$/) == null){
						currentPerson += ",";
					}
					$("#repairPerson").val(currentPerson+assignUsers.toString());
				}
				
				layer.close(index);
			}
			,success:function(){
				assignUsers.length = 0;
				//同步请求人员组织树数据
				$.ajax({
					url : '/iot_process/userOrganizationTree/userOrganizationTreeData',
					type : 'post',
					dataType : 'json',
					success : function(res) {
						if(res != null && res.code == 0){
							var data = buildTree(res.data);
							//单选框
				  			tree.render({
				  				elem: '#task_tree'
				  				,data: data
				  				,showCheckbox: true
				  				,oncheck:function(obj){
				  					parseTree(obj);
				  				}
				  			})
						}
					},
					error : function() {
						layer.msg('请求失败，请联系管理员！！！');
					}
				});	
		    	
				// 人名前面 显示人形图标
				$("i.layui-icon-file").addClass("layui-icon-user");
			}
		});

    })
    
    /**
	 * 解析树型结构,获取选中人员信息
	 */
    var assignUsers = [];
	function parseTree(obj){
		 console.log(obj.data); //得到当前点击的节点数据
		 var data = obj.data;
		 //选中就添加人员
		 if(obj.checked){
			 getUser(data);
		 }else{
			 //去掉选中就删除人员
			 removeUser(data);
		 }
	}
	
	function getUser(data){
		if(data.children == null || data.children == undefined || data.children.length == 0){
			assignUsers.push(data.label);
		 }else{
			for(var i=0;i<data.children.length;i++){
				getUser(data.children[i]);
			}
		 }
	}
	
	function removeUser(data){
		if(data.children == null || data.children == undefined || data.children.length == 0){
			for(var i=0;i<assignUsers.length;i++){
				if(assignUsers[i] == data.label){
					assignUsers.splice(i,1);
				}
			}
	    }else{
	    	for(var i=0;i<data.children.length;i++){
	    		removeUser(data.children[i]);
			}
	    }
	}
    
	/**
	 * 更换备品备件表格
	 */
	table.render({
	    elem: '#replacement'
	    ,cols: [[ //标题栏
	      {field:'content', title:'备件型号规格', width:300, edit: 'text',align:'center'}
	      ,{field:'number', title:'备件数量', width:200, edit: 'text', align:'center'}
	    ]]
	    ,data: [{"content": "","number": ""},
	    		{"content": "","number": ""},
	    		{"content": "","number": ""},
	    		{"content": "","number": ""},
	    		{"content": "","number": ""}]
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
	  
});
