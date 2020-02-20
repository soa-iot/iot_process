var area = GetQueryString("area");
var piid = GetQueryString("piid");
var address = getUrlIp();

if (area == "净化工段" || area == "维修工段") {
	window.location.href = "http://"+getUrlIp()+"/iot_process/html/estimate-team-leader.html?piid="+piid+"&area="+area
}else{
	window.location.href = "http://"+address+"/iot_process/html/estimate.html?piid="+piid+"&area="+area;
}