/**
 * 问题评估（班组长评估）
 */

/**
 * 属地单位
 */
var area = GetQueryString("area");

$("#coordinate_tree").hide();

$("#repair_cadre_tree").hide();
$("#estimate_cadre_tree").hide();
console.log("--------"+$.cookie("organ"));
/**
 * 作业安排
 */
//数据初tree数据声明
$.ajax({   
	url : "/iot_process/userOrganizationTree/userOrganizationArea",  
	type : "get",
	data : {area:$.cookie("organ").replace(/"/g,""),username:$.cookie("name").replace(/"/g,"")},
	dataType : "json",  
	success: function( json) {
		console.log(json);
		var coordinate_tree_data=[];
		var coordinate_tree ;
		var usernames = "";
		if (json.code == 0) {
			var datapro = json.data;
			//数据初始化
			coordinate_tree_data = buildTree(datapro);
			
			//tree
			layui.use('tree', function(){
				var tree = layui.tree
				,layer = layui.layer
				,data = coordinate_tree_data;


				//弹出层
				layui.use('layer', function(){ //独立版的layer无需执行这一句
					var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
					//触发事件
					var active = {
							offset: function(othis){
								var type = othis.data('type')
								var ope = layer.open({
								type: 1
								,offset: type 
								,area: ['300px','400px;']
								,id: 'work_plan'+type //防止重复弹出
								,key:'id'
								,content: $("#coordinate_tree")
								,btn: ['确认',"取消"]
								,btnAlign: 'c' //按钮居中
								,yes: function(){

									var check = coordinate_tree.getChecked(); //获得被勾选的节点
									console.log(check);

									for (var i = 0; i < check.length; i++) {
										usernames += userOrDept(check[i][1]);
										if (usernames != "" && i != check.length - 1) {
											usernames +=",";
										}
										}
									console.log("选中的人："+usernames);

										if (usernames=="") {
											layer.msg('至少选定一人！！！',{icon:7,offset:"100px"});
										}else {
											workPlan(this,usernames);
											layer.close(ope);
										}
										usernames="";

								}
								,success:function(){
									//console.log(data);
									//开启复选框
									coordinate_tree = tree.render({

										elem: '#coordinate_tree'
											,data: data
											,showCheckbox: true
									})
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

			});

		}

	}  
});


$('#estimate_next').on('click', function(){
	estimate_next();

});

/**
 * 作业安排确认提交
 * 
 * @param obj 当前对象
 * @param usernames 人名用“，”隔开
 * @returns
 */
function workPlan(obj,usernames){
	$.ajax({
		async:false,
		type: "PUT"
		,url: '/iot_process/process/nodes/next/group/piid/'+piidp    //piid为流程实例id
		,data: {
			"isIngroup": 2,    /*流程变量名称,流程变量值(属地单位为非维修非净化+前端选择"作业安排"时，值为1；
		     								   属地单位为非维修非净化+前端选择"外部协调"时，值为2；
		     								   属地单位为维修或净化+前端选择"作业安排"时，值为1；
		     								    属地单位为维修或净化+前端选择"下一步"时，值为3 )*/
			"comment": $("#comment").val()     //节点的处理信息
			,"receivor":usernames
			,"operateName":"作业安排"
			,"userName":$.cookie("name").replace(/"/g,"")

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

/**
 * 生成办处理流程
 * @param obj
 * @param usernames
 * @returns
 */
function estimate_next(){
	
	var estimate_next_data = {
			"isIngroup": 1,    /*流程变量名称,流程变量值(属地单位为非维修非净化+前端选择"作业安排"时，值为1；
								   属地单位为非维修非净化+前端选择"外部协调"时，值为2；
								   属地单位为维修或净化+前端选择"作业安排"时，值为1；*/
			"comment": $("#comment").val()     //节点的处理信息
			,"operateName":"生产办评估"
			,"userName":$.cookie("name").replace(/"/g,"")
	}
	
	$.ajax({
		async:false,
		type: "PUT"
		,url: '/iot_process/process/nodes/next/group/piid/'+piidp    //piid为流程实例id
		,data: estimate_next_data
		,contentType: "application/x-www-form-urlencoded"
		,dataType: "json"
		,success: function(jsonData){
			//后端返回值： ResultJson<Boolean>
			console.log("人员提交："+jsonData.data);
			if (jsonData.data) {
				modifyEstimated("问题流转到生产办");
			}else{
				layer.msg('安排人员发送失败！！！',{icon:7,offset:"100px"});
			}
		},
		//,error:function(){}		       
	});
}
