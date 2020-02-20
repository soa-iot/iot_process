
$(".show_div").hide();

/**
 * 动态请求
 */
$("#problemdescribe").autocomplete({  
	source: function( request, response ) { 
		getAutoFill()
		console.log($("#problemdescribe").val());
	}

});

/*鼠标点击其他地方，补全的div消失  */
$(document).mouseup(function(){
	$(".show_div").hide();
});

$("#problemdescribe").click(function(){
	getAutoFill();
});

function getAutoFill() {
	if($("#problemdescribe").val()!=""){
		$.ajax({  
			url : "/iot_process/estimates/autoFill",  
			type : "get",
			data : {problemdescribe : $("#problemdescribe").val().trim()},
			dataType : "json",  
			success: function( json) {
				if (json.state == 0) {
					var list = json.data;
					var context = "";
					for (var i = 0; i < list.length; i++) {
						var problemdescribe = list[i].problemdescribe+"";
						context = context + "<span class='append_span' onclick=select(this)>"+problemdescribe+"</span>";
					}
				}

				if(context != ""){
					$(".show_div").html("");
					$(".show_div").append(context);
					$(".show_div").show();
				}else{
					$(".show_div").hide();
				}

			}  
		});
	}else{
		$(".show_div").hide();
	} 
}

/*该方法是再拼接div内容时加上的，即鼠标选中的时候，input框输入选中值  */
function select(obj){
	$("#problemdescribe").val($(obj).html());
	$(".show_div").hide();
}
