layui.use(['tree', 'layer', 'form'], function() {
	var tree = layui.tree, layer = layui.layer, $ = layui.$;
	var layer = layui.layer, form = layui.form;
	
	var treeResult, assignUsers = new Array();
	var ticketNo = $("#ticketNo").val();
	$("#ticketNo").val((ticketNo == 1)?"事故事件":((ticketNo == 2)?"隐患":"一般问题"));
	
	//从cookie中获取当前登录用户
	var resavepeople = getCookie1("name").replace(/"/g,'');
	//从cookie中获取所在组
	var dept = getCookie1("organ").replace(/"/g,'');
	
	//验证表单是否为空
	function isempty(){
		if($("#comment_assign").val().replace(/^\s+/, '').replace(/\s+$/, '') == ''){
			  layer.msg("处理说明不能为空", {icon: 7, offset: '100px'});
			  return true;
		}
		return false;
	}

	/**
	 * 作业指派异步请求
	 */
	function workAssignment(comment, receivor, username){
		$.ajax({
			 async: false
		     ,type: "PUT"
		     ,url: '/iot_process/process/nodes/next/group/piid/'+piidp    //piid为流程实例id
		     ,data: {
		     	"comment": comment     //通用 -- 节点的处理信息
		     	,"receivor": receivor     //通用 -- 下一个节点问题处理人
		     	,"userName": username    //当前任务的完成人
		     	,"operateName": "作业安排"
		     }   //问题上报表单的内容
		     ,contentType: "application/x-www-form-urlencoded"
		     ,dataType: "json"
		     ,success: function(jsonData){
		     	//后端返回值： ResultJson<Boolean>
		    	 if(jsonData.data){
		    		 $("#comment_assign").val("");
		    		 layer.msg("作业安排成功,问题流转到:"+receivor,{icon:1, time: 2000, offset: '100px'}, function(){
		    			 top.location.href = jumpHtml;
		    		 })
		    	 }else{
		    		 layer.msg("作业安排失败",{icon:2, time: 2000, offset: '100px'});
		    	 }
		     }
		     ,error:function(){
		    	 layer.msg("作业安排失败，请检查网络是否正常",{icon:2, time: 2000, offset: '100px'});
		     }		       
		});
		
	    return false;
	}
	
	/**
	 * 回退到上一个节点
	 */
	form.on('submit(back_previous)', function(data){
		if(isempty()){
			return false;
		}
		console.log(data.field);
		layer.confirm("是否确定回退？",{offset: '100px'}, function(){
			$(".layui-layer-btn0").off('click');
			$.ajax({
				 async:false
			     ,type: "PUT"
			     ,url: '/iot_process/process/nodes/before/group/piid/'+piidp   //piid为流程实例id
			     ,data: {
			     	"comment": data.field.comment  //处理信息
			     	,"userName": resavepeople
			     	,"operateName": "回退"
			     }  
			     ,contentType: "application/x-www-form-urlencoded"
			     ,dataType: "json"
			     ,success: function(jsonData){
			     	//后端返回值： ResultJson<String>
			    	 if(jsonData){
			    		 layer.msg("回退成功",{icon:1, time: 2000, offset: '100px'}, function(){
			    			 top.location.href = jumpHtml;
			    		 })
			    	 }else{
			    		 layer.msg("回退失败",{icon:2, offset: '100px'});
			    	 }
			     },
			     error:function(){
			    	 layer.msg("回退失败，请检查网络是否正常",{icon:2, offset: '100px'});
			     }		       
			});
		})
		
		return false;
	});
	
	/**
	 * 闭环流程
	 */
	form.on('submit(complete)', function(data){
		if(isempty()){
			return false;
		}
		layer.confirm("是否确定闭环？",{offset: '100px'}, function(){
			$(".layui-layer-btn0").off('click');
			$.ajax({
				 async: false
			     ,type: "PUT"
			     ,url: '/iot_process/process/nodes/end/group/piid/'+piidp   //piid为流程实例id
			     ,data: {
			     	"comment": data.field.comment  //处理信息
			     	,"userName": resavepeople
			     	,"operateName": "闭环处理"
			     }  
			     ,contentType: "application/x-www-form-urlencoded"
			     ,dataType: "json"
			     ,success: function(jsonData){
			     	//后端返回值： ResultJson<String>
			    	 if(jsonData.state==0){
			    		 //updateEstimated(data);
			    		 layer.msg("闭环处理成功",{icon:1, time: 2000, offset: '100px'}, function(){
			    			 top.location.href = jumpHtml;
			    		 })
			    	 }else{
			    		 layer.msg("闭环处理失败",{icon:2, offset: '100px'});
			    	 }
			     },
			     error:function(){
			    	 layer.msg("闭环处理失败，请检查网络是否正常",{icon:2, offset: '100px'});
			     }		       
			});
		})
		
		return false;
	});
	
	/**
	 * 校验表单是否为空, 为空则不弹出层
	 */
	form.on('submit(arrange)', function(data){
		if(isempty()){
			return false;
		}
		//弹出层
		layer.open({
			type: 1
			,offset: 't'
			,area: ['300px','400px;']
			,id: 'work_arrange'+1 //防止重复弹出
			,content: $("#task_tree")
			,btn: ['确认',"取消"]
			,btnAlign: 'c' //按钮居中
			,yes: function(index, layero){
				//确认按钮的回调函数
				var comment = $("#comment_assign").val();
				var receivor = assignUsers.join(",");
				console.log(receivor);
				$(".layui-layer-btn0").off('click');
				workAssignment(comment, receivor, resavepeople);
				layer.close(index);
		    }
		,success:function(){
			assignUsers = [];
			//单选框
			tree.render({
				elem: '#task_tree'
				,data: treeResult
				,showCheckbox: true
				,oncheck:function(obj){
					parseTree(obj);
					console.log(assignUsers);
				}
			})
			
			//人名前面 显示人形图标
			$("i.layui-icon-file").addClass("layui-icon-user");
		 }
		});
	    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	    
	});
	
	/**
	 * 异步请求人员角色树
	 */
	$.ajax({
		url : '/iot_process/userOrganizationTree/users',
		type : 'POST',
		dataType : 'json',
		data : {"dept":dept},
		success : function(json) {
			treeResult = json.data;
		},
		error : function() {
		}
	});
	
	
	/**
	 * 解析树型结构,获取选中人员信息
	 */
	function parseTree(obj){
		 //console.log(obj.data); //得到当前点击的节点数据
		 // console.log(obj.checked); //得到当前节点的展开状态：open、close、normal
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
		if(data.children == null || data.children == undefined){
			assignUsers.push(data.label);
		 }else{
			for(var i=0;i<data.children.length;i++){
				getUser(data.children[i]);
			}
		 }
	}
	
	function removeUser(data){
		if(data.children == null || data.children == undefined){
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
});