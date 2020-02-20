
/**作业验收*/

/**
 * 全局变量——piid
 */
var piidp = GetQueryString('piid');

/**
 * 全局变量——属地单位
 */
var area = GetQueryString('area');

/**
 *作业完成图片
 */
$.ajax({  
	url : "/iot_process/estimates/problemreportpho",  
	type : "get",
	data : {piid : piidp,remark:1},
	dataType : "json",  
	success: function( json) {
		//console.log(json.state);
		if (json.state == 0) {
			var imgs = json.data;
			if (imgs.length==0) {
				//$("#doimg-div").hide();
				$("#doimg-div").empty();
				$("#doimg-div").append("<p style='background-color:#FAFDFD; padding-top:9px;'>没有上传图片</p>")
			}else{
				$("#doimg-div").show();
				var mode = imgs.length%3;
				var img_id = 0;
	
	
				for (var j = 0; j < Math.ceil(imgs.length/3); j++) {
					var img_div='<div  class="img_p">';
					if (mode != 0 && j == (Math.ceil(imgs.length/3) - 1) ) {
						//img_div = '';
						for (var i = 0; i < mode; i++) {
							img_div = img_div+'<img class="big-img-new"  data-method="offset" alt="图片无法显示" src="'+imgs[img_id].phoAddress+'">';
							img_id++;
						}
	
					}else{
	
						for (var i = 0; i < 3; i++) {
							img_div = img_div+'<img class="big-img-new"  data-method="offset" alt="图片无法显示" src="'+imgs[img_id].phoAddress+'">';
							img_id++;
						}
	
					}
					img_div = img_div+'</div>'
					$("#doimg").append(img_div);
				}
				$("#doimg-div").show();
				
				//轮播图
				layui.use(['carousel', 'form'], function(){
					var carousel = layui.carousel
					,form = layui.form;

					//常规轮播
					carousel.render({
						elem: '#doimg-div'
						//,arrow: 'always'
							//,width: '778px'
						,height: '150px'
						,interval: 5000
					});
				});
				
				/**
				 * 图片点击放大
				 * @returns
				 */
				//弹出层
				layui.use('layer', function(){ //独立版的layer无需执行这一句
					var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
					
					
					//触发事件
					var active = {
							offset: function(othis){
								
							var imgHtml= "<img alt='图片无法显示' src='"+$(this).attr("src")+"'width='600px'  height='500px'/>";
								//var type = othis.data('type')
								layer.open({
								type: 1
								//,offset: type 
								,area: ['600px','500px']
								,content: imgHtml
								,title:false
								//,shadeClose:true
								//,cancel:false
								,offset:'50px'
								
								});
							}
					};

					$('.big-img-new').on('click', function(){
						var othis = $(this), method = othis.data('method');
						active[method] ? active[method].call(this, othis) : '';
					});

				});
			}
		}

	}  
});

/**
 * 作业验收——闭环处理
 * @returns
 */

$("#complete1").click(function(){
	if ($("#comment").val()=="") {
		layer.msg("处理说明不能为空",{icon:7,offset:"100px"});
	}else{
		
		var rollback = layer.msg("是否确认闭环？",{
			btn:["确认","取消"]
			,time:20000
			,offset:"100px"
			,btnAlign:"c"
			,area:"300px"
			,icon:3
			,yes:function(){
				$(".layui-layer-btn0").off('click');
				$.ajax({
					 async:false
					,type: "PUT"
					,url: '/iot_process/process/nodes/next/group/piid/'+piidp    //piid为流程实例id
					,data: {
		
						"comment": $("#comment").val()     //节点的处理信息
						,"operateName":"闭环处理"
						,"userName":$.cookie("name").replace(/"/g,"")
					}   //问题上报表单的内容
					,contentType: "application/x-www-form-urlencoded"
					,dataType: "json"
					,success: function(jsonData){
						//后端返回值： ResultJson<Boolean>
						
						if (jsonData.data) {
							layer.msg("闭环处理成功！",{time: 3000,icon:1,offset:"100px"},function() {
								top.location.href = "http://10.89.90.118:10239/CZ_PIOTMS/index.action";
							});
						}else{
							layer.msg("闭环处理失败！！",{icon:2,offset:"100px"});
						}
					},
						//,error:function(){}		       
				});
		
			}})
	}

})

/**
 * 作业验收——回退
 * @returns
 */
$("#rollback-accept").click(function(){
	
	if ($("#comment").val()=="") {
		layer.msg("处理说明不能为空",{icon:7,offset:"100px"});
	}else{
		
		var rollback = layer.msg("是否确认回退？",{
			btn:["确认","取消"]
			,time:20000
			,offset:"100px"
			,btnAlign:"c"
			,area:"300px"
			,icon:3
			,yes:function(){
				$(".layui-layer-btn0").off('click');
				$.ajax({
					 async:false
					,type: "PUT"
					,url: '/iot_process/process/nodes/before/group/piid/'+piidp    //piid为流程实例id
					,data: {
						"comment": $("#comment").val()  //处理信息
						,"operateName":"回退"
				     	,"userName":$.cookie("name").replace(/"/g,"")
					}   //问题上报表单的内容
					,contentType: "application/x-www-form-urlencoded"
					,dataType: "json"
					,success: function(jsonData){
						//后端返回值： ResultJson<Boolean>
						if (jsonData.data) {
							 $("#comment").val("");
							layer.msg("回退成功！",{time: 3000,icon:1,offset:"100px"},function() {
								top.location.href = "http://10.89.90.118:10239/CZ_PIOTMS/index.action";
							});
						}else{
							layer.msg("回退成功失败！！",{icon:2,offset:"100px"});
						}
					},
						//,error:function(){}		       
				});
		
			}})
	}

})