/**
 * 地址解析
 * @returns
 */

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  $.trim(decodeURI(r[2])); return null;
}

/**
 * 获取地址和端口
 * @returns ip：端口号
 */
function getUrlIp(){
	
	return $.trim(window.location.href.split("/")[2]);
}


/**
  * 脚手架和防腐保温作业清单url
  */
function getUrl(){
	var piid = GetQueryString("piid");
	$(".staging-requisition").attr({"href": "./staging-work-requisition.html?piid="+piid});
	$(".anticor-requisition").attr({"href": "./anticorrosion-work-requisition.html?piid="+piid});
}
getUrl();