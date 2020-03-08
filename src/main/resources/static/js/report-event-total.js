	layui.use(['jquery','table','form','laydate','table'], function(){
			var layer = layui.layer,form = layui.form,
				$ = layui.$, //重点处,使用jQuery
				laydate = layui.laydate,table=layui.table,
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
					,url: '/iot_process/estimates/eventtotal' //数据接口
					// ,page: true //开启分页
					,toolbar: true
					,where: { 
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
			,cols: [[//一级表头
					{field: 'depet',title:'部门',rowspan:2,align:'center'}
			      ,{ title:'一般事件',colspan:2,align:'center'}
			      ,{ title:'事故事件',colspan:2,align:'center'}
			      ,{ title:'隐患事件',colspan:2,align:'center'}
			      ,{ field: 'total',title:'合计',rowspan:2,align:'center'}
			    ],[ //二级表头
			    {field: 'ordinaryeventUnsafebehavior', title: '不安全行为/状态(件)',align:'center'}
				,{field: 'ordinaryevent', title: '其他',align:'center'}
				,{field: 'accidenteventUnsafebehavior', title: '不安全行为/状态(件)',align:'center'}
				,{field: 'accidentevent', title: '其他',align:'center'}
				,{field: 'riskseventUnsafebehavior', title: '不安全行为/状态(件)',align:'center'}
				,{field: 'risksevent', title: '其他',align:'center'} 
				
				]]
			});
			
			var Baroption = {
				   grid:{
				    	top:60,
				    	right:20,
				    	left:80,
				    	bottom:30,
				    	containLabel: true},
				   
				    legend: {
				        data: [ '一般事件(不安全)','一般事件(其他)', '事故事件(不安全)', '事故事件(其他)','隐患事件(不安全)','隐患事件(其他)' ],
				    },
				    xAxis: {
				    },
				    yAxis: {
				    },
				    series: [  {
				        name: '一般事件(不安全)',
				        type: 'bar',
				        stack: '合计',
				        label: {
        	                show: true,
        	                position: 'inside'
        	            },
				        itemStyle: {
				            normal: {
				                color:  '#2F4554'
				            }
				        }
				    }, {
				        name:  '一般事件(其他)',
				        type: 'bar',
				        stack: '合计',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        itemStyle: {
				            normal: {
				                color: 'green'
				            }
				        }
				      
				    },{
				        name:   '事故事件(不安全)', 
				        type: 'bar',
				        stack: '合计',
				        label: {
        	                show: true,
        	                position: 'inside'
        	            },
				        itemStyle: {
				            normal: {
				                color:'#FD0F0F'
				            }
				        }
				      
				    },{
				        name:'事故事件(其他)',
				        type: 'bar',
				        stack: '合计',
				        label: {
        	                show: true,
        	                position: 'inside'
        	            },
				        itemStyle: {
				            normal: {
				                color: '#8B0015'
				            }
				        }
				      
				    },  {
				        name:  '隐患事件(不安全)',
				        type: 'bar',
				        stack: '合计',
				        label: {
        	                show: true,
        	                position: 'inside'
        	            },
				        itemStyle: {
				            normal: {
				                color: 'yellow'
				            }
				        }
				       
				    },  {
				        name:  '隐患事件(其他)',
				        stack: '合计',
				        type: 'bar',
				        label: {
        	                show: true,
        	                position: 'inside'
        	                	
        	            },
				        itemStyle: {
				            normal: {
				                color: '#FAF06E'
				            }
				        }
				       
				    }]
				};
			var myChart1 = echarts.init($('#linetu')[0]);
			var  ordinaryeventUnsafebehavior;     
			var  ordinaryevent;
			var  accidenteventUnsafebehavior;
			var  accidentevent;
			var  riskseventUnsafebehavior;
			var  risksevent;
			function setBarData(n,i){
				
				ordinaryeventUnsafebehavior[i]=n.ordinaryeventUnsafebehavior;
				ordinaryevent[i]=n.ordinaryevent;
				riskseventUnsafebehavior[i]=n.riskseventUnsafebehavior;
				accidentevent[i]=n.accidentevent;
				riskseventUnsafebehavior[i]=n.riskseventUnsafebehavior;
				risksevent[i]=n.risksevent;
			};
			function dataAnalysis(ldata){
				
				ordinaryeventUnsafebehavior = [0,0,0,0,0,0,0,0];     
				ordinaryevent = [0,0,0,0,0,0,0,0];  
				accidenteventUnsafebehavior = [0,0,0,0,0,0,0,0];
				accidentevent = [0,0,0,0,0,0,0,0];
				riskseventUnsafebehavior = [0,0,0,0,0,0,0,0];
				risksevent = [0,0,0,0,0,0,0,0];
				var yAxisdata = ["生产办公室","综合办","HSE办公室","设备办公室","财务办公室","厂领导","净化工段","维修工段"];
				
				var data = {};
				$.ajax({  
					async:false,
					url : "/iot_process/estimates/eventtotal",  
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
				console.log("++++++++++++++++++++++++++++")
				Baroption.yAxis.data=yAxisdata;
				Baroption.series[0].data=ordinaryeventUnsafebehavior;
				Baroption.series[1].data=ordinaryevent;              
				Baroption.series[2].data=accidenteventUnsafebehavior;
				Baroption.series[3].data=accidentevent;              
				Baroption.series[4].data=riskseventUnsafebehavior;   
				Baroption.series[5].data=risksevent;                 
				myChart1.setOption(Baroption);
			}
			dataAnalysis({ 
				  "startTime" : year + "-" + month + "-" + now,
					"endTime" : nowDate.getFullYear() + "-" + month1+ "-" + now1,
			  });
			
			$("#generateTestData").click(function(){
				if ($("#endTime").val() != '' && $("#endTime").val()<$("#startTime").val()) {
					layer.msg("请选择正确的截止日期！",{time: 3000,icon:7,offset:"100px"});
					return;
				}

				dataAnalysis({"startTime" : $("#startTime").val(),
					"endTime" : $("#endTime").val(),
					"date" : $("#monthTime").val()});
				
				tab.reload({
					  where: { 
						  "startTime" : $("#startTime").val(),
							"endTime" : $("#endTime").val(),
							"date" : $("#monthTime").val()
					  }
					 
					});
			})
		});		 