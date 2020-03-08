	layui.use(['jquery','table','form','laydate','table'], function(){
			var layer = layui.layer,form = layui.form,
				$ = layui.$, //重点处,使用jQuery
				laydate = layui.laydate,table=layui.table
nowDate = new Date();
			
			var year = nowDate.getFullYear();
			var month = (nowDate.getMonth() + 1) > 9 ? nowDate.getMonth() : "0" + (nowDate.getMonth());
			var month1 = (nowDate.getMonth() + 1) > 9 ? nowDate.getMonth() + 1 : "0" + (nowDate.getMonth() + 1);
			var now = (nowDate.getDate()) > 9 ? nowDate.getDate() : "0" + (nowDate.getDate());
			var now1 = (nowDate.getDate()) > 9 ? nowDate.getDate() : "0" + (nowDate.getDate() );
			
			if(month == 0){
				year -= 1;
				month = 12;
			}
			
			laydate.render({
				  elem: '#startTime' //指定元素
				  ,max:0
				  ,value: year + "-" + month + "-" + now
				});
			
			laydate.render({
				  elem: '#endTime' //指定元素
				  ,max:0
				  ,value: nowDate.getFullYear() + "-" + month1+ "-" + now1
				});
			
			laydate.render({
				  elem: '#monthTime' //指定元素
				  ,type:"month"
				  ,max:0
				});
			
			form.on('select(quecla)', function(data){
				  if (data.value == 1) {
					$("#monthTimediv").show();
					$("#startTimediv").hide();
					$("#endTimediv").hide();
					$("#startTime").val("");
					$("#endTime").val("");
				}else if (data.value == 2) {
					$("#monthTimediv").hide();
					$("#startTimediv").show();
					$("#endTimediv").show();
					$("#monthTime").val("");
				}
				});
			
			var tab = table.render({
				elem: '#event'
					,url: '/iot_process/estimates/finished' //数据接口
					// ,page: true //开启分页
					,toolbar: true
					,where:{
						  "startTime" : year + "-" + month + "-" + now,
							"endTime" : nowDate.getFullYear() + "-" + month1+ "-" + now1,
					  }
					,parseData: function(res) { //res 即为原始返回的数据
						console.log(res.data);
						return {
							"code": res.state, //解析接口状态
							"msg": res.message, //解析提示文本
							"count": res.length, //解析数据长度
							"data": res.data //解析数据列表
						}
					}
			//,format:'yyyy-MM-dd'
			,cols: [[ //表头
				{field: 'depet', title: '部门',rowspan:2,align:'center'}
				,{field: 'depets', title: '总录入数(件)',rowspan:2,align:'center'}
				 ,{ title:'完成数量',colspan:2,align:'center'}
				,{field: 'unfinished', title: '整改中(件)',rowspan:2,align:'center'}
				],[
				//{field: 'finished', title: '完成数量(件)', width:'18%'}
				{field: 'normalfinished', title: '正常闭环(件)',align:'center'} 
				,{field: 'directfinished', title: '直接闭环(件)',align:'center'}]]
			});
			
			var Baroption = {
					//color: ['#003366', '#006699', '#4cabce'],
				   grid:{
				    	top:60,
				    	right:50,
				    	left:50,
				    	bottom:30},
				   // backgroundColor: '#F3F3F3',
				    tooltip: {
				        trigger: 'axis',
				        axisPointer: {
				            type: 'shadow'
				        }
				    },
				    legend: {
				        data: ['总录入数', '完成数量', '正常闭环','直接闭环' ,  '整改中'],
				    },
				    xAxis: {
				    	type: 'category',
				    	splitNumber:7,
				        splitArea:{
				        	interval:'0'
				        }
				    },
				    yAxis: {
				        splitLine: {show: false},
				        axisLine: {
				        }
				    },
				    series: [  {
				        name: '总录入数',
				        type: 'bar',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        itemStyle: {
				            normal: {
				                color: '#003366'
				            }
				        }
				       
				    }, {
				        name: '完成数量',
				        type: 'bar',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        stack: '总录入数',
				        itemStyle: {
				            normal: {
				                color: '#5FB878'
				            }
				        }
				      
				    },  {
				        name: '整改中',
				        type: 'bar',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        stack: '总录入数',
				        itemStyle: {
				            normal: {
				                color: 'red'
				            }
				        }
				       
				    },{
				        name: '正常闭环',
				        type: 'bar',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        stack: '完成数量',
				        itemStyle: {
				            normal: {
				                color: 'green'
				            }
				        }
				      
				    },{
				        name: '直接闭环',
				        type: 'bar',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        stack: '完成数量',
				        itemStyle: {
				            normal: {
				                color: 'yellow'
				            }
				        }
				      
				    }]
				};
			var myChart1 = echarts.init($('#linetu')[0]);
			var  total;     
			var  finished;
			var  normalfinished;
			var  directfinished;
			var  unfinished;
			function setBarData(n,i){
				
				total[i]=n.depets;
				finished[i]=n.finished;
				normalfinished[i]=n.normalfinished;
				directfinished[i]=n.directfinished;
				unfinished[i]=n.unfinished;
			};
			function dataAnalysis(ldata){
				
				total = [0,0,0,0,0,0];     
				finished = [0,0,0,0,0,0];  
				unfinished = [0,0,0,0,0,0];
				normalfinished = [0,0,0,0,0,0];
				directfinished = [0,0,0,0,0,0];
				var xAxisdata = ["生产办公室","综合办","HSE办公室","设备办公室","财务办公室","厂领导","净化工段","维修工段"];
				
				var data = {};
				$.ajax({  
					async:false,
					url : "/iot_process/estimates/finished",  
					type : "get",
					data : ldata,
					dataType : "json",  
					success: function( json) {
						if (json.state == 0) {
							data = json.data;
							
						}
						
					}  
				});
				
				for (var i = 0; i < data.length; i++) {
					switch (data[i].depet){
					case "生产办公室":
						setBarData(data[i],0);
						break;
					case "综合办":
						setBarData(data[i],1);
						break;
					case "HSE办公室":
						setBarData(data[i],2);
						break;
					case "设备办公室":
						setBarData(data[i],3);
						break;
					case "财务办公室":
						setBarData(data[i],4);
						break;
					case "厂领导":
						setBarData(data[i],5);
						break;
					case "净化工段":
						setBarData(data[i],6);
						break;
					case "维修工段":
						setBarData(data[i],7);
						break;
					}
				}
				Baroption.xAxis.data=xAxisdata;
				Baroption.series[0].data=total;
				Baroption.series[1].data=finished;
				Baroption.series[2].data=unfinished;
				Baroption.series[3].data=directfinished;
				Baroption.series[4].data=normalfinished;
				myChart1.setOption(Baroption);
			}
			dataAnalysis({"startTime" : year + "-" + month + "-" + now,
				"endTime" : nowDate.getFullYear() + "-" + month1+ "-" + now1,
			  });
			
			$("#generateTestData").click(function(){
				if ($("#endTime").val() != '' && $("#endTime").val()<$("#startTime").val()) {
					layer.msg("请选择正确的截止日期！",{time: 3000,icon:7,offset:"100px"});
					return;
				}
				
				tab.reload({
					  where: { 
						  "startTime" : $("#startTime").val(),
							"endTime" : $("#endTime").val(),
							"date" : $("#monthTime").val()
					  }
					 
					});
				dataAnalysis({"startTime" : $("#startTime").val(),
					"endTime" : $("#endTime").val(),
					"date" : $("#monthTime").val()});
				
			})
		});		 