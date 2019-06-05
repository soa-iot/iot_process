layui.use(['form', 'jquery','layer'], function(){
  var form = layui.form
  ,	$ = layui.$
  ,layer = layui.layer;
	
	//点击下一步按钮操作
	form.on('submit(next_step)', function(data){
		  
		  console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
		  $.ajax({  
		    	url : "#",   ///iot_process/estimates/problemdescribe
		        type : "post",
		        data : {piid : "ADAA80DB601C4470BE8BB224705F5F9C",problemdescribe:$("#problem_describe").val()},
		        dataType : "json",  
		        success: function( json) {
		        	
		        	if (json.state==1) {
		        		alert(json.message);						
					}
		        }  
		   });		  
		  
		  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
   });
		  
	  
});