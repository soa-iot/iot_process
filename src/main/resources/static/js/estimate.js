/**
 * 全局变量——问题描述piid
 */
var piidp = "ADAA80DB601C4470BE8BB224705F5F9C";

 /**
  * 地址解析
  * @returns
  */
getUrlPara();
function getUrlPara(){
	var url = window.location.href;
	url = url.substring(url.indexOf("?")+1);
	url=url.split("&");
	var map = {};
	for (var i = 0; i < url.length; i++) {
		map[url[i].substring(0,url[i].indexOf("="))]=url[i].substring(url[i].indexOf("=")+1);
	}
//	//pid= url[1].substring(url[1].indexOf("=")+1);
//	//alert(pid);
//	console.log(map['name']);
	
}

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
        data : {piid : piidp},
        dataType : "json",  
        success: function( json) {
        	if (json.state == 0) {
				var problem = json.data;
        		$("#parea").val(problem.welName);
        		$("#major").val(problem.profession);
        		$("#rfid").val(problem.rfid);
        		$("#prob").val(problem.problemclass);
        		$("#applypeople").val(problem.applypeople);
        		$("#problemtype").val(problem.problemtype);
        		
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
    	url : "/iot_process/estimates/problemreportpho",  
        type : "get",
        data : {piid : "12323213123"},
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
		
		//数据初tree数据声明
		var tree_data=[];
		$.ajax({  
			url : "http://localhost:10238/iot_usermanager/user/rolename",  
			type : "get",
			data : {roleName:"超级管理员"},
			dataType : "json",  
			success: function( json) {
				if (json.state == 0) {
					var datapro = json.data;

					//数据初始化
					tree_data = buildTree(datapro);

					$.ajax({  
						url : "http://localhost:10238/iot_usermanager/role/rolid",  
						type : "get",
						data : {rolid:"A291958C761347159F28FBDF70ADC12D"},
						dataType : "json",  
						success: function( json) {
							if (json.state == 0) {
								var userRole = json.data;
								console.log(userRole);
								
								//tree
								layui.use('tree', function(){
									var tree = layui.tree
									,layer = layui.layer
									,data = [{label: userRole.name
										,id: 1
										,children: tree_data}];

									
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
														,area: ['300px','400px;']
														,id: 'work_plan'+type //防止重复弹出
														,key:'id'
														,content: $("#test1")
														,btn: ['确认',"取消"]
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
														//console.log(data);
														//开启复选框
														tree1 = tree.render({

															elem: '#test1'
																,data: data
																,showCheckbox: true
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


								});

							}

						}  
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
	/*$(".problem_describe").click(function(){
		//alert($(this).html());
		var problem_describe = $("#problem_describe").val();
		//alert(problem_describe =="");
		if (pd!=problem_describe && problem_describe!="") {
			$.ajax({  
		    	url : "/iot_process/estimates/problemdescribe",  
		        type : "post",
		        data : {piid : piidp,problemdescribe:$("#problem_describe").val()},
		        dataType : "json",  
		        success: function( json) {
		        	
		        	if (json.state==1) {
		        		alert(json.message);
						
					}
		        }  
		   });
		}else if (problem_describe == "") {
			alert("问题描述不能为空！！");
		}
	});*/
	
	//闭环处理
	$(".problem_describe").click(function(){
		if ($(this).html()=="回退" || $(this).html()=="闭环处理") {
			//验证处理说明不能为空
			if ($("#comment").val()=="") {
				alert("处理说明不能为空");
			}else{
				modifyEstimated();
			}
		}else{
			modifyEstimated();
		}
		
	});
	
	/**
	 * 非净化工段问题处理更新ajax
	 */
	function modifyEstimated() {
		
		//问题描述
		var problem_describe = $("#problem_describe").val();
		
		//问题处理信息
		var str = "problemdescribe="+problem_describe+"&piid="+piidp+"&"+$("#estimate").serialize();
		
		var period = $("#sdate").val();
		var date_amtch = period.match(/^(\d{4})(-)(\d{2})(-)(\d{2})$/);
		
		//日期格式处理
		if (period == "") {
			str = str.replace(/rectificationperiod=(\d+-\d+-\d+)/,period);
		}else{
			period = period.replace(/-/g, "/");
			str = str.replace(/rectificationperiod=(\d+-\d+-\d+)/,"rectificationperiod="+period);
		}
		
		if (($("#sele").val() =="指定日期" && period=="") || ($("#sele").val() =="指定日期" && date_amtch == null)) {
			alert("请正确输入指定日期！");
		}else if(problem_describe == ""){
			alert("问题描述不能为空！！");
		}else{
			$.ajax({  
		    	url : "/iot_process/estimates/modifyestimated",  
		        type : "post",
		        data : str,
		        dataType : "json",  
		        success: function( json) {
		        	
		        	if (json.state==0) {
		        		alert(json.message);
						
					}
		        }  
		   });
		}
		
	}
	