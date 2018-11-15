$(function(){
	/*
	 * 参数定义
	 */
	//历史节点查询url
	var historyUrl = ipPort + "/pureAssign/processNode/id/name",
	//历史节点json
		historyNode = {},
	//历史节点json对应html-标签名
		historyNodeTag = {
			"问题上报" : "bodyPR",
			"问题评估" : "bodyPE",
			"净化分配" : "bodyPA",
			"维修分配" : "bodyRA"	
		},
	//获取当前处理piid
		piid = currentDealPiid;
		
	/*
	 * 事件绑定
	 */
	
	
	/*
	 * 页面初始化 - 动态生成页面
	 */		
	//查询历史节点-更新参数
	getAjax( historyUrl + "/" + piid, {}, getHisNodeFunc );
	//动态生成历史节点
	
	//初始化节点数据
	
	
	
	/*
	 * 查询历史节点-更新参数-成功回调函数
	 */
	function getHisNodeFunc( data ){
		console.log( '查询历史节点-更新参数-成功回调函数……' );
		historyNode = data;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * ajax-get访问模板
	 */
	function getAjax( url, data, ){
		console.log( 'ajax-get访问模板……' );
		$.ajax({
		     type : "GET",
		     url : ,
		     data : data,
		     async : false, 
		     cache : true,
		     contentType : "application/x-www-form-urlencoded"
		     dataType : "json",
		     success : function( jsonData ){
		    	console.log( '查询历史节点-成功回调函数……' );
		 		if( jsonData ){
		 			var data = jsonData.data;
		 			if( jsonData.state == 0 && data ){
		 				eval( getHisNodeFunc + "(data)" );
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