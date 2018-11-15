/**
 * 参数定义
 */
//项目ip
var ip = "localhost",
	//项目端口
	port = "8080",
	//域名
	ipPort = "http://" + ip + ":" + port ,
	//用户名
	currentUser = getCookie1( 'userName' ),
	//用户班组
	currentUserOrganization = getCookie1( 'userOrganization' ),
	//用户角色
	currentUserRole = getCookie1( 'userRole' )
	//流程节点信息
	processNodeInfo = {
		"问题上报" : "problemReport",
		"问题评估" : "problemEstimate",
		"净化分配" : "pureAssign",
		"维修分配" : "repairAssign"
	};