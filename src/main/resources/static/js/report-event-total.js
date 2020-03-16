	layui.use(['jquery','table','form','laydate','table'], function(){
			var layer = layui.layer,form = layui.form,
				$ = layui.$, //重点处,使用jQuery
				laydate = layui.laydate,table=layui.table;
			
			laydate.render({
				  elem: '#startTime' //指定元素
				  ,max:0
				});
			
			laydate.render({
				  elem: '#endTime' //指定元素
				  ,max:0
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
				{field: 'depet', title: '部门', width:'15%'}
				,{field: 'ordinaryevent', title: '一般事件(件)', width:'18%'}
				,{field: 'accidentevent', title: '事故事件(件)', width:'18%'}
				,{field: 'risksevent', title: '隐患事件(件)', width:'18%'} 
				,{field: 'unsafebehavior', title: '不安全行为/状态(件)', width:'18%'}
				,{field: 'total', title: '合计(件)'}
				]]
			});
			
			var Baroption = {
					//color: ['#003366', '#006699', '#4cabce'],
					title: {
		                text: '事故事件情况统计'
		            },
				   grid:{
				    	top:60,
				    	right:50,
				    	left:50,
				    	bottom:30},
				    backgroundColor: '#F3F3F3',
				    tooltip: {
				        trigger: 'axis',
				        axisPointer: {
				            type: 'shadow'
				        }
				    },
				    legend: {
				        data: ['合计', '一般事件', '事故事件','隐患事件' ,  '不安全行为/状态'],
//				        textStyle: {
//				            color: '2D547B'
//				        }
				    },
				    xAxis: {
				      //  data: category,
				    	splitNumber:7,
				        axisLine: {
//				            lineStyle: {
//				                color: '#ccc'
//				            }
				        },
				        splitArea:{
				        	interval:'0'
				        }
				    },
				    yAxis: {
				        splitLine: {show: false},
				        axisLine: {
//				            lineStyle: {
//				                color: '#ccc'
//				            }
				        }
				    },
				    series: [  {
				        name: '合计',
				        type: 'bar',
				        barWidth: 10,
				        itemStyle: {
				            normal: {
				                barBorderRadius: 5,
				                color: new echarts.graphic.LinearGradient(
				                    0, 0, 0, 1,
				                    [
				                        {offset: 0, color: '#003366'},
				                        {offset: 1, color: '#003366'}
				                    ]
				                )
				            }
				        }
				       
				    }, {
				        name:  '一般事件',
				        type: 'bar',
				        barWidth: 10,
				        itemStyle: {
				            normal: {
				                barBorderRadius: 5,
				                color: new echarts.graphic.LinearGradient(
				                    0, 0, 0, 1,
				                    [
				                        {offset: 0, color: '#006699'},
				                        {offset: 1, color: '#006699'}
				                    ]
				                )
				            }
				        }
				      
				    },{
				        name:  '事故事件',
				        type: 'bar',
				        barWidth: 10,
				        itemStyle: {
				            normal: {
				                barBorderRadius: 5,
				                color: new echarts.graphic.LinearGradient(
				                    0, 0, 0, 1,
				                    [
				                        {offset: 0, color: '#5FB878'},
				                        {offset: 1, color: '#5FB878'}
				                    ]
				                )
				            }
				        }
				      
				    },{
				        name: '隐患事件' ,
				        type: 'bar',
				        barWidth: 10,
				        itemStyle: {
				            normal: {
				                barBorderRadius: 5,
				                color: new echarts.graphic.LinearGradient(
				                    0, 0, 0, 1,
				                    [
				                        {offset: 0, color: '#F7E005'},
				                        {offset: 1, color: '#F7E005'}
				                    ]
				                )
				            }
				        }
				      
				    },  {
				        name:   '不安全行为/状态',
				        type: 'bar',
				        barWidth: 10,
				        itemStyle: {
				            normal: {
				                barBorderRadius: 5,
				                color: new echarts.graphic.LinearGradient(
				                    0, 0, 0, 1,
				                    [
				                        {offset: 0, color: '#4cabce'},
				                        {offset: 1, color: '#4cabce'}
				                    ]
				                )
				            }
				        }
				       
				    }]
				};
			var myChart1 = echarts.init($('#linetu')[0]);
			var  total;     
			var  ordinaryevent;
			var  accidentevent;
			var  risksevent;
			var  unsafebehavior;
			function setBarData(n,i){
				
				total[i]=n.total;
				ordinaryevent[i]=n.ordinaryevent;
				accidentevent[i]=n.accidentevent;
				risksevent[i]=n.risksevent;
				unsafebehavior[i]=n.unsafebehavior;
			};
			function dataAnalysis(ldata){
				
				total = [0,0,0,0,0,0];     
				ordinaryevent = [0,0,0,0,0,0];  
				accidentevent = [0,0,0,0,0,0];
				risksevent = [0,0,0,0,0,0];
				unsafebehavior = [0,0,0,0,0,0];
				var xAxisdata = ["生产办公室","综合办","HSE办公室","设备办公室","财务办公室","厂领导","净化工段","维修工段"];
				
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
				Baroption.xAxis.data=xAxisdata;
				Baroption.series[0].data=total;
				Baroption.series[1].data=ordinaryevent;
				Baroption.series[2].data=accidentevent;
				Baroption.series[3].data=risksevent;
				Baroption.series[4].data=unsafebehavior;
				myChart1.setOption(Baroption);
			}
			dataAnalysis({});
			
			$("#generateTestData").click(function(){
				if ($("#endTime").val() != '' && $("#endTime").val()<$("#startTime").val()) {
					layer.msg("请选择正确的截至日期！",{time: 3000,icon:7,offset:"100px"});
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