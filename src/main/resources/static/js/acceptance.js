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
		elem: '#test1'
			,arrow: 'always'
	});
});  
//轮播图
layui.use(['carousel', 'form'], function(){
	var carousel = layui.carousel
	,form = layui.form;

	//常规轮播
	carousel.render({
		elem: '#test11'
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
	
	//指定日期禁用
	$("#sele").change(function(){
		if ($("#sele").val()=="overhaul") {
			$("#sdate").val("");
			$("#sdate").attr("disabled","disabled");
		}else{
			$("#sdate").removeAttr("disabled");
		}
	});
	
	 