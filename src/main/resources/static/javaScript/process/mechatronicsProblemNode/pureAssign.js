$(function(){	
	/*
	 * 临时参数定义
	 */
	var layer = layui.layer;
	
	/*
	 * 参数定义
	 */
	//获取当前处理piid
	var piid = currentDealPiid,
	//下一步\特送\回退\闭环按钮url
		nextNodePAUrl = ipPort + "",
		sendToPAUrl = ipPort + "",
		backToPAUrl = ipPort + "",
		endProcPAUrl = ipPort + "",
	//当前窗口的index
		currWindIndex = "";
		
	/*
	 * 事件绑定
	 */
	//下一步按钮 - 单击事件
	$( '#submitPA' ).on( 'click', nextNodeFunc );
	//特送按钮 - 单击事件
	
	//回退按钮 - 单击事件
	
	//闭环按钮 - 单击事件
	
	/*
	 * 页面初始化 - 动态生成页面
	 */		
	
	
	/*
	 * 下一步按钮 - 单击事件回调函数
	 */
	function nextNodeFunc(){
		console.log( '下一步按钮 - 单击事件回调函数……' );
		//选择下一步执行人
		layer.open({
			type: 2,
			title: '选择下一步(维修分配)执行人',
			btn: ['确定', '取消'],
			area: ['400px','200px'],
			content: '/html/process/organTree/nextExecutorPA.html',
			//此处要求返回的格式
			success: function(){
				
			},
			yes: function( index, sonDom ){
				currWindIndex = index;
				var sonWindow = window[sonDom.find('iframe')[0]['name']],
					nextNodeExecuRA = sonWindow.getCurrTreeNodeName();
				console.log( '下一步按钮-单击事件回调函数……nextNodeExecuRA：' + nextNodeExecuRA );
				//赋值
				if( !nextNodeExecuRA && nextNodeExecuRA == "维修技干" ){
					nextNodeExecuRA = "";
					layer.msg( '获取下一步节点处理人失败', {icon : 5});
				}
				
				//执行节点任务-保存处理信息
				
			},
			cancel: function( index, layero ){
				layer.close( index );
			},
		});

		
		//请求
	}
	
	
	
	
	
	
	
	/*
	 * ajax静态代理proxy
	 */
	function ajax( t, u, d, f, a, ){
		console.log( 'ajax代理proxy……' );
		switch( arguments.length ){
			case 4:
				switch( t.toUpperCase() ){
					case 'GET':
						gAjax( u, d, f );
						break;
					case 'POST':
						break;
					case 'PUT':
						break;
					case 'DELETE':
						break;
				}
				break;
			case 5:
				switch( t.toUpperCase() ){
				case 'GET':
					gAjax1( u, d, f, a,);
					break;
				case 'POST':
					break;
				case 'PUT':
					break;
				case 'DELETE':
					break;
				}
				break;
		}
	}
	
	
	/*
	 * ajax-get同步访问模板
	 */
	function gAjax( url, data, succFuncName ){
		console.log( 'ajax-get同步访问模板……' );
		$.ajax({
		     type : "GET",
		     url : url,
		     data : data,
		     async : false, 
		     cache : true,
		     contentType : "application/x-www-form-urlencoded",
		     dataType : "json",
		     success : function( jsonData ){
		 		if( jsonData ){
		 			var data = jsonData.data;
		 			if( jsonData.state == 0 && data ){
		 				eval( succFuncName + "(data)" );
		 			}else{
		 				layer.msg( jsonData.message, {icon:2} );
		 			}		
		 		}else{
		 			layer.msg( '请求失败', {icon:2} );
		 		}		
		     },
		     error:function(){
		    	 layer.msg('请求失败：');
		     }		       
		});
	}
	
	/*
	 * ajax-get异步访问模板
	 */
	function gAjax1( url, data, succFuncName, requestType){
		console.log( 'ajax-get异步访问模板……' );
		$.ajax({
		     type : "GET",
		     url : url,
		     data : data,
		     async : requestType, 
		     cache : true,
		     contentType : "application/x-www-form-urlencoded",
		     dataType : "json",
		     success : function( jsonData ){
		 		if( jsonData ){
		 			var data = jsonData.data;
		 			if( jsonData.state == 0 && data ){
		 				eval( succFuncName + "(data)" );
		 			}else{
		 				layer.msg( jsonData.message, {icon:2} );
		 			}		
		 		}else{
		 			layer.msg( '请求失败', {icon:2} );
		 		}		
		     },
		     error:function(){
		    	 layer.msg('请求失败：');
		     }		       
		});
	}
	
	/*
	 * ajax-post访问模板
	 */
	function pAjax( url, data, succFuncName ){
		console.log( 'ajax-get访问模板……' );
		$.ajax({
		     type : "POST",
		     url : url,
		     data : data,
		     async : true, 
		     cache : true,
		     contentType : "application/x-www-form-urlencoded",
		     dataType : "json",
		     success : function( jsonData ){
		 		if( jsonData ){
		 			var data = jsonData.data;
		 			if( jsonData.state == 0 && data ){
		 				eval( succFuncName + "(data)" );
		 			}else{
		 				layer.msg( jsonData.message, {icon:2} );
		 			}		
		 		}else{
		 			layer.msg( '请求失败', {icon:2} );
		 		}		
		     },
		     error:function(){
		    	 layer.msg('请求失败：');
		     }		       
		});
	}
})