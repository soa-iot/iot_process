layui.use(['tree','layer'], function() {
	var tree = layui.tree, layer = layui.layer, $ = layui.jquery;
	var layer = layui.layer;

	console.log($);
	$.ajax({
		url : '/iot_process/userOrganizationTree/userOrganizationTreeData',
		type : 'post',
		dataType : 'json',
		data : {},
		success : function(res) {
			
			
			if(res == 2){
				layer.msg('服务器处理异常，请联系管理员！！！');
				return;
			}
			
			console.log(res.data);
			var data = buildTree(res.data);
			
			// 加载人员组织树
			var org_tree = tree.render({
						elem : '#organization_tree',
						data : data,
						showCheckbox : true // 是否显示复选框
						,
						key : 'id' // 定义索引名称
						,
						checked : [1, 11, 12] // 选中节点(依赖于 showCheckbox 以及 key
						// 参数)。
						,
						spread : [1, 2, 4, 5, 11] // 展开节点(依赖于 key 参数)
						// ,accordion: true //是否开启手风琴模式
						,
						isJump : true // 是否允许点击节点时弹出新窗口跳转
						,
						click : function(obj) {
							layer.msg('状态：' + obj.state + '<br>节点数据：'
									+ JSON.stringify(obj.data)); // 获取当前选中的节点数据
						}
					});

			/*$('#btn').on('click', function() {
						console.log(org_tree.getChecked());

					});*/
		},
		error : function() {
			layer.msg('请求失败，请联系管理员！！！');

		}
	});

});