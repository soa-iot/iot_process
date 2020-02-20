/**
 * 技术干部评估（净化）
 */

var area = GetQueryString("area");
var piid = GetQueryString("piid");

/**
 * 作业安排
 * @param obj
 * @returns
 */
/*var url = area == "维修工段"?"../html/organization_tree_repair.html":"../html/organization_tree.html";
console.log(area == "维修工段");*/
//弹出层
layui.use('layer', function(){ //独立版的layer无需执行这一句
	var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
	//触发事件
	var active = {
			offset: function(othis){
				var usernames="";
				var type = othis.data('type')
				var ope = layer.open({
				type: 1
				,offset: type 
				,area: ['300px','400px;']
				,id: 'work_plan'+type //防止重复弹出
				,key:'id'
					//../html/organization_tree.html
				,content: '<iframe frameborder="no" id="addFrame" src="../html/organization_tree.html" style="width:98%;height:98%;"></iframe>'
				,btn: ['确认',"取消"]
				,btnAlign: 'c' //按钮居中
					,yes: function(){

						var childWindow = $("#addFrame")[0].contentWindow;
						var checkData = childWindow.getCheckedData();
						console.log(checkData);
						for (var i = 0; i < checkData.length; i++) {
							
							//console.log(checkData[i][1]);
							var user=userOrDept(checkData[i][1]);
							if (user!="") {
								//对比是否为同一部门
								if (i<checkData.length-2) {
									if (!compareTodept(checkData[i][1],checkData[i+1][1])) {
										layer.msg('请选择同一部门的人！！！',{icon:7,offset:"100px"});
										usernames = "";
										return;
									}
									
								}
								usernames +=user;
								if (usernames != "" && i != checkData.length - 1) {
									usernames += ",";
								}
							}
						}
						console.log("选中的人："+usernames);
	
						if (usernames=="") {
							layer.msg('至少选定一人！！！',{icon:7,offset:"100px"});
						}else{
							$(".layui-layer-btn0").off('click');
							layer.close(ope);
							workPlan(this,usernames);
						}
						usernames="";
					}
				});
			}
	};

	$('#work_plan').on('click', function(){
		if (yesCompare()) {
			var othis = $(this), method = othis.data('method');
			active[method] ? active[method].call(this, othis) : '';
		}
	});

});

/**
 * 外部协调
 */
$("#coordinate_tree").hide();
var coordinate_tree ;

/**
 * 作业安排确认提交
 * 
 * @param obj 当前对象
 * @param usernames 人名用“，”隔开
 * @returns
 */
function workPlan(obj,usernames){
	var isIngroup = 0;
	
	console.log("id:"+$(obj).attr("id"));
	console.log("id判断:"+$(obj).attr("id")=="work_plant");
	
	if ($(obj).attr("id")=="work_plant") {
		isIngroup = 1;
	}
	
	if ($(obj).attr("id")=="coordinatet") {
		isIngroup = 2;
	}
	console.log("isIngroupg:"+isIngroup);
	$.ajax({
		async:false,
		type: "PUT"
		,url: '/iot_process/process/nodes/jump/group/piid/'+piidp    //piid为流程实例id
		,data: {
			
			//"area": ""  //属地单位
			"actId": "receive"  //跳转节点id
			,"receivor": usernames  //下一步流程变量
			,"userName": $.cookie("name").replace(/"/g,"")  //当前节点任务执行人
			,"comment": $("#comment").val()   //备注信息\
			,"operateName":"作业安排"
					
			/*"isIngroup": isIngroup,   
			"comment": $("#comment").val(),     //节点的处理信息
			"receivor":usernames,
			"userName":$.cookie("name").replace(/"/g,"")*/
		}   //问题上报表单的内容
		,contentType: "application/x-www-form-urlencoded"
		,dataType: "json"
		,success: function(jsonData){
			//后端返回值： ResultJson<Boolean>
			console.log("人员提交："+jsonData.data);
			if (jsonData.data) {
				modifyEstimated("作业安排成功，问题流转到："+usernames);
			}else{
				layer.msg('安排人员发送失败！！！',{icon:7,offset:"100px"});
			}
		},
		//,error:function(){}		       
	});
}
