//日期插件
	layui.use('laydate', function(){
		  var laydate = layui.laydate;
		  
		  //常规用法
		  laydate.render({
		    elem: '#sdate'
		  });
		  
	});
	
//轮播图
layui.use(['carousel', 'form'], function(){
	var carousel = layui.carousel
	,form = layui.form;

	//常规轮播
	carousel.render({
		elem: '#test2'
			,arrow: 'always'
	});
});  

//折叠
layui.use([ 'element', 'layer' ], function() {
	var element = layui.element;
	var layer = layui.layer;

});

//信息问题请求
	var pd="";
	$.ajax({  
    	url : "/iot_process/estimates/estim",  
        type : "get",
        data : {piid : "ADAA80DB601C4470BE8BB224705F5F9C"},
        dataType : "json",  
        success: function( json) {
        	if (json.state == 0) {
				var problem = json.data;
        		$("#parea").val(problem.welName);
        		$("#major").val(problem.profession);
        		$("#rfid").val(problem.rfid);
        		$("#prob").val(problem.problemclass);
        		if (problem.problemclass=="不安全行为/状态") {
					$("#remark1").val(problem.remarkfive);
					$("#remark2").val(problem.remarksix);
					$("#remark").show();
				}else {
					$("#remark").hide();
				}
        		$("#problem_describe").val(problem.problemdescribe);
        		pd=problem.problemdescribe;
			}
        	 
        }  
   });
	
	//图片
	$.ajax({  
    	url : "/iot_process/estimates/problemreportpho/",  
        type : "get",
        data : {piid : "13"},
        dataType : "json",  
        success: function( json) {
        	if (json.state == 0) {
				var imgs = json.data;
				var mode = imgs.length%3;
				var img_id = 0;
				//alert(img[i]);
				
				for (var j = 0; j < Math.ceil(imgs.length/3); j++) {
					var img_div='<div>';
					if (mode != 0 && j == (Math.ceil(imgs.length/3) - 1) ) {
						//img_div = '';
							
						for (var i = 0; i < mode; i++) {
							img_div = img_div+'<img alt="图片1" src="'+imgs[img_id].phoAddress+'">';
							img_id++;
						}
						
					}else{
						
						for (var i = 0; i < 3; i++) {
							img_div = img_div+'<img alt="图片1" src="'+imgs[img_id].phoAddress+'">';
							img_id++;
						}
						
					}
					img_div = img_div+'</div>'
					$("#imag").append(img_div);
				}
			}
        	 
        }  
   });
	
	
	//作业安排
	$("#test1").hide();
	$("#work_plan").click(function(){

		var tree1 ;

		$.ajax({  
			url : "http://localhost:10238/iot_usermanager/user/role_name/",  
			type : "get",
			data : {roleName:"3591A0F744F49EFBABA6959917139CE"},
			dataType : "json",  
			success: function( json) {
				if (json.state == 0) {
					var datapro = json.data;

					//数据初始化
					var tree_data = buildTree(datapro);

					//tree
					layui.use('tree', function(){
						var tree = layui.tree
						,layer = layui.layer
						,data = tree_data;

						//弹出层
						layui.use('layer', function(){ //独立版的layer无需执行这一句
							var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
							//触发事件
							var active = {
								offset: function(othis){
									var type = othis.data('type')
									layer.open({
										type: 1
										,offset: type 
										,area: '300px;'
										,id: 'work_plan'+type //防止重复弹出
										,key:'id'
										,content: $("#test1")
										,btn: ['提交',"取消"]
										,btnAlign: 'c' //按钮居中
										,yes: function(){

											var check = tree1.getChecked(); //获得被勾选的节点
											for (var i = 0; i < check.length; i++) {
												
											}
												console.log(check);
												console.log(check[0][0]);
												layer.closeAll();
											}
										,success:function(){
											console.log(data);
											tree1 = tree.render({

												elem: '#test1'
													,data: data
													,showCheckbox: true
													,oncheck: function(obj){
														console.log(obj.data); //得到当前点击的节点数据
														console.log(obj.checked); //得到当前节点的展开状态：open、close、normal
														console.log(obj.elem); //得到当前节点元素
													}
											});
										}
										});
									}
							};

							$('#work_plan').on('click', function(){
								var othis = $(this), method = othis.data('method');
								active[method] ? active[method].call(this, othis) : '';
							});

						});

						//开启复选框

					});

				}

			}  
		});






	});
	
	//指定日期禁用
	$("#sele").change(function(){
		if ($("#sele").val()=="大修时整改") {
			$("#sdate").val("");
			$("#sdate").attr("disabled","disabled");
		}else{
			$("#sdate").removeAttr("disabled");
		}
	});
	
	//更新问题描述
	$(".problem_describe").click(function(){
		var problem_describe = $("#problem_describe").val();
		//alert(problem_describe =="");
		if (pd!=problem_describe && problem_describe!="") {
			$.ajax({  
		    	url : "/iot_process/estimates/problemdescribe",  
		        type : "post",
		        data : {piid : "ADAA80DB601C4470BE8BB224705F5F9C",problemdescribe:$("#problem_describe").val()},
		        dataType : "json",  
		        success: function( json) {
		        	
		        	if (json.state==1) {
		        		alert(json.message);
						
					}
		        }  
		   });
		}
	});
	
	
	 