layui.use(['tree', 'layer'], function() {
	var tree = layui.tree, layer = layui.layer, $ = layui.jquery;
	var layer = layui.layer;

	console.log($);
	$.ajax({
		url : '/iot_process/userOrganizationTree/userOrganizationTreeData',
		type : 'post',
		dataType : 'json',
		data : {},
		success : function(res) {

			if (res == 2) {
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
				key : 'id',// 定义索引名称
				accordion : true // 是否开启手风琴模式
				,
				isJump : true
					// 是否允许点击节点时弹出新窗口跳转
				});
			// 修改样式
			$('.layui-icon-file').addClass("layui-icon-username");
			$('.layui-icon-file').removeClass("layui-icon-file");

			console.log($('.layui-tree-icon'));

			$('.layui-tree-icon')
					.after('<i class="my-icon layui-icon layui-icon-user" style = "font-size: 18px;margin-right:8px;color:#c0c4cc;;height: 100%;vertical-align: inherit;"></i>');

			/*
			 * $('#btn').on('click', function() {
			 * console.log(org_tree.getChecked());
			 * 
			 * });
			 */
		},
		error : function() {
			layer.msg('请求失败，请联系管理员！！！');
		}
	});

});