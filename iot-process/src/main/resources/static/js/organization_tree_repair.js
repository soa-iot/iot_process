

var area = GetQueryString('area');

console.log(area);
var tree1 ;
	//console.log($.cookie("name"));
	//数据初tree数据声明
	var tree_data=[{label:"仪表班长",id:"仪表班长"},{label:"电气班长",id:"电气班长"},{label:"机械班长",id:"机械班长"}];
	//$.ajax({  
//		url : "/iot_process/userOrganizationTree/userOrganizationArea",  
//		type : "get",
//		data : {area:era,username:"li"},//$.cookice("name")
//		dataType : "json",  
		//success: function( json) {
			//if (json.code == 0) {
				//var datapro = json.data;
				//数据初始化
				//tree_data = buildTree(datapro);
				console.log(tree_data);

				//tree
				layui.use('tree', function(){
					var tree = layui.tree
					,layer = layui.layer
					,data = tree_data;

					tree1 = tree.render({

						elem: '#work_plan_tree'
							,data: data
							,showCheckbox: true
							
					})

				});

			//}

		//}  
	//});
	
	/**
	 * 获取选中的数据
	 * @return {}
	 */
	function getCheckedData(){
		var checkData = tree1.getChecked('id');
		return checkData;
	}
	
	/*var check = tree1.getChecked(); //获得被勾选的节点

	for (var i = 0; i < check.length; i++) {
		usernames += check[i][0].innerText;
		if (i!=check.length-1) {
			usernames +=",";
		}
	}

	workPlan(obj,usernames);

	console.log(usernames);

	layer.closeAll();*/
	
	