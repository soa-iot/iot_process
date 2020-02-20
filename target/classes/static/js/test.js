$.ajax({
		async:false,
		type: "PUT"
		,url: '/iot_process/process/nodes/jump/group/piid/125142'    //piid为流程实例id
		,data:{
			
			"area": "维修工段" //属地单位
			,"actId": "repair"  //跳转节点id
			,"operateName":'外部协调'								 
			,"comment": "电器专业桥架，请电器专业处理"    //节点的处理信息
			,"userName":"王琪"
			,"repairor":"曾钊伟"
		}    //问题上报表单的内容
			
		,contentType: "application/x-www-form-urlencoded"
		,dataType: "json"
		,success: function(jsonData){
			//后端返回值： ResultJson<Boolean>
			if (jsonData.data) {

				console.log(jsonData);
				//modifyEstimated("外部协调成功，问题流转到："+usernames);
			}else{
				layer.msg('安排人员发送失败！！！',{icon:7,offset:"100px"});
			}
		}
		//,error:function(){}		       
	});

<!-- 新增巡检点实例
Integer insertTaskInspectionPoint(TaskInspectionPoint taskInspectionPoint), -->
<insert id="insertTaskInspectionContent"
	parameterType="cn.zg.entity.taskInspection.TaskInspectionContent">
	
	insert all 
	
	<foreach collection="taskInspectionContent" index="index" separator=" " item="item">
		into CZ_TASK_INSPECTION_POINT
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<if test="item.taskPointId != null and item.taskPointId != ''">
				TASK_POINT_ID,
			</if>
			<if test="item.taskContentId != null and item.taskContentId != ''">
				TASK_CONTENT_ID,
			</if>
			<if test="item.taskContentName != null and item.taskContentName != ''">
				TASK_CONTENT_NAME,
			</if>
			<if test="item.taskPointName != null and item.taskPointName != ''">
				TASK_POINT_NAME,
			</if>
			<if test="item.taskContentStarttime != null and item.taskContentStarttime != ''">
				TASK_CONTENT_STARTTIME,
			</if>
			<if test="item.taskContentEndtime != null and item.taskContentEndtime != ''">
			TASK_CONTENT_ENDTIME,
			</if>
			<if test="item.taskContentState != null and item.taskContentState != '' ">
				TASK_CONTENT_STATE,
			</if>
			<if test="item.taskContentOrder != null">
				TASK_CONTENT_ORDER,
			</if>
			<if test="item.taskContentDesc != null and item.taskContentDesc != ''">
				TASK_CONTENT_DESC,
			</if>
			<if test="item.planStartTime != null and item.planStartTime != '' ">
				PLAN_START_TIME,
			</if>
			<if test="item.planIntervalTime != null">
				PLAN_INTERVAL_TIME,
			</if>
			<if test="item.equPositionNum != null and item.equPositionNum != ''">
				EQU_POSITION_NUM,
			</if>
			<if test="item.remarkone != null and item.remarkone != ''">
				REMARKONE,
			</if>
			<if test="item.remarktwo != null and item.remarktwo != ''">
				REMARKTWO,
			</if>
			<if test="item.remarkthree!= null and item.remarkthree!= ''">
				REMARKTHREE,
			</if>
			<if test="item.remarkfour!= null and item.remarkfour!= ''">
				REMARKFOUR,
			</if>
		</trim>
	
		<trim prefix="values (" suffix=")" suffixOverrides=",">
			<if test="item.taskPointId != null and item.taskPointId != ''">
				#{item.taskPointId},
			</if>
			<if test="item.taskContentId != null and item.taskContentId != ''">
				#{item.taskContentId},
			</if>
			<if test="item.taskContentName != null and item.taskContentName != ''">
				#{item.taskContentName},
			</if>
			<if test="item.taskPointName != null and item.taskPointName != ''">
				#{item.taskPointName},
			</if>
			<if test="item.taskContentStarttime != null  and item.taskContentStarttime != ''">
				#{item.taskContentStarttime},
			</if>
			<if test="item.taskContentEndtime != null and item.taskContentEndtime != ''">
				#{item.taskContentEndtime},
			</if>
			<if test="item.taskContentState != null">
				#{item.taskContentState},
			</if>
			<if test="item.taskContentOrder != null">
				#{item.taskContentOrder},
			</if>
			<if test="item.taskContentDesc != null and item.taskContentDesc != ''">
				#{item.taskContentDesc},
			</if>
			<if test="item.planStartTime != null and item.planStartTime != '' ">
				#{item.planStartTime},
			</if>
			<if test="item.planIntervalTime != null">
				#{item.planIntervalTime},
			</if>
			<if test="item.equPositionNum != null and item.equPositionNum != ''">
				#{item.equPositionNum},
			</if>
			<if test="item.remarkone != null and item.remarkone != ''">
				#{item.remarkone},
			</if>
			<if test="item.remarktwo != null and item.remarktwo != ''">
				#{item.remarktwo},
			</if>
			<if test="item.remarkthree!= null and item.remarkthree!= ''">
				#{item.remarkthree},
			</if>
			<if test="item.remarkfour!= null and item.remarkfour!= ''">
				#{item.remarkfour},
			</if>
		</trim>
	</foreach>
	select 1 from dual
</insert>