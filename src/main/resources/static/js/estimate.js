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

//折叠
layui.use([ 'element', 'layer' ], function() {
	var element = layui.element;
	var layer = layui.layer;

});

//信息问题请求
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
			}
        	 
        }  
   });

	//判断‘不安全行为’是否隐藏
	/*if($("#prob").val() == "不安全行为/状态"){
		$("#remark").show();
	}else{
		
	}*/
	
	/*var a=10;
	for (var i = 0; i < Math.ceil(a/3); i++) {
		alert(Math.ceil(a/3));
	}*/
	 