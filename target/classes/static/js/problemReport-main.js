	layui.use(['jquery','table','form','laydate','table'], function(){
			var layer = layui.layer,form = layui.form,
				$ = layui.$, //重点处,使用jQuery
				laydate = layui.laydate,table=layui.table,nowDate = new Date();
			console.log(layer);
//			function renderTime(param){
//				laydate.render({
//					elem: param,
//					type: 'date',
//					trigger: 'click' //采用click弹出
//				});
//			};
			var month = (nowDate.getMonth() + 1) > 9 ? nowDate.getMonth() : "0" + (nowDate.getMonth());
			var month1 = (nowDate.getMonth() + 1) > 9 ? nowDate.getMonth() + 1 : "0" + (nowDate.getMonth() + 1);
			var now = (nowDate.getDate()) > 9 ? nowDate.getDate() : "0" + (nowDate.getDate());
			var now1 = (nowDate.getDate()) > 9 ? nowDate.getDate() : "0" + (nowDate.getDate() );
			var ins1 = laydate.render({
		    	elem: '#startTime',
		    	type: 'date',
		    	value: "2019-01-01"
			})
			
			var ins2 = laydate.render({
		    	elem: '#endTime',
		    	type: 'date',
		    	value: nowDate.getFullYear() + "-" + month1+ "-" + now1
			})
//			renderTime('#startTime');
//			renderTime('#endTime');
			form.render(); 
			var Baroption = {
				color: ['#003366', '#006699', '#4cabce'],
				   grid:{
				    	left:38,
				    	right:-16
				    },			
				    tooltip: {
				        trigger: 'axis',
				        axisPointer: {
				            type: 'shadow'
				        }
				    },
				    legend: {
				        data: ['总量', '已整改', '未整改'],
//				        textStyle: {
//				            color: '2D547B'
//				        }
				    },
				    xAxis: {
				    	type : 'category',				    	
				        splitLine: {
				            show: false
				        },
				        splitArea:{
				        	interval:'2',
				        	show: false
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
				        name: '总量',
				        type: 'bar',
				        barWidth: 8,
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
				        name: '已整改',
				        type: 'bar',
				        barWidth: 8,
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
				      
				    }, {
				        name: '未整改',
				        type: 'bar',
				        barWidth: 8,
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
			var myChart1 = echarts.init($('.complate-show')[0]);
			//设置柱状图数据
			var xtotal;
			var xfinished;
			var xunfinished;
			function setBarData(n,i){
				if(n.PROBLEMSTATE=="FINISHED"){
					xfinished[i]=n.COUNT;
				}else{
					xunfinished[i]=n.COUNT
				};
				xtotal[i]+=n.COUNT;
			};
			$("#generateTestData").click(function(){
				var total=0, finished=0;
				$.getJSON('/iot_process/report/problemCount',{'beginTime':ins1.config.value,'endTime':ins2.config.value},function(aj){
					if(aj.state==0){
						//初始化基本数据
						tableData=[{
						    "jc-total":0
						    ,"jc-finished": 0
						    ,"jc-unfinished": 0
							,"wx-total":0
							,"wx-finished": 0
							,"wx-unfinished": 0
							,"hse-total":0
							,"hse-finished": 0
							,"hse-unfinished": 0
							,"sb-total":0
							,"sb-finished": 0
							,"sb-unfinished": 0
							,"zh-total":0
							,"zh-finished": 0
							,"zh-unfinished": 0
							,"cw-total":0
							,"cw-finished": 0
							,"cw-unfinished": 0
							,"sc-total":0
							,"sc-finished": 0
							,"sc-unfinished": 0
						  }];
						delete tableData[0].LAY_TABLE_INDEX;
						console.log(tableData);
						 xAxisdata=["净化工段","维修工段","HSE办","财务经营办","综合办","设备办","生产办"];
						 xtotal=[0,0,0,0,0,0,0];
						 xfinished=[0,0,0,0,0,0,0];
						 xunfinished=[0,0,0,0,0,0,0];
						 useData=aj.data;
						//根据获取的数据进行分析
						$.each(useData,function(i,n){
							total+=parseInt(n.COUNT);
							if(n.PROBLEMSTATE=='FINISHED'){
								finished+=parseInt(n.COUNT);
							};
						
							switch (n.AREA){
							case '净化工段':
								setBarData(n,0);
//								changeTableDate(n,'jc');
								break;
							case '维修工段':
								setBarData(n,1);
//								changeTableDate(n,'wx');
								break;
							case 'HSE办公室':
								setBarData(n,2);
//								changeTableDate(n,'hse');
								break;
							case '财务经营办公室':
								setBarData(n,2);
//								changeTableDate(n,'cw');
								break;
							case '设备办':
								setBarData(n,5);
//								changeTableDate(n,'sb');
								break;
							case '生产办':
								setBarData(n,6);
//								changeTableDate(n,'sc');
								break;
							case '综合办':
								setBarData(n,4);
//								changeTableDate(n,'zh');
								break;
							}
						});
						if(aj.data.length==0){
							total=1;
						}
						var value=(finished/total*100).toFixed(2);
						Baroption.xAxis.data=xAxisdata;
						Baroption.series[0].data=xtotal;
						Baroption.series[1].data=xfinished;
						Baroption.series[2].data=xunfinished;
						myChart1.setOption(Baroption);

					}
				})
			});
			
			$('#generateTestData').find('button').click();
		});		 