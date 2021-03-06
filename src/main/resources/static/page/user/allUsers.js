layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage'],function(){
	var form = layui.form(),
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;

	//加载页面数据
	var usersData = '';
//	$.get("../../json/usersList.json", function(data){
	$.get("/filter/json/usersList", function(data){
		usersData = data;
		if(window.sessionStorage.getItem("addUser")){
			var addUsers = window.sessionStorage.getItem("addUser");
			usersData = JSON.parse(addUsers).concat(usersData);
		}
		//执行加载数据的方法
		usersList();
	})

	//查询
	$(".search_btn").click(function(){
		var username = $("#username").val();
	 	var phoneNumber = $("#phoneNumber").val();
	 	var email = $("#email").val();
	 	var gender = $("input[name='gender']:checked").val();
	 	var viprank = $("#viprank").val();
	 	var state = $("#state").val();
	 	
		var userArray = [];
//		if($(".search_input").val() != ''){
		var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
		console.log("username"+username);
		console.log("phoneNumber"+phoneNumber);
		console.log("email"+email);
		console.log("gender"+gender);
		console.log("viprank"+viprank);
		console.log("state"+state);
		$.ajax({
			url: '/filter/json/usersListWithSearch',
			type: 'get',
			dataType: 'json',
			async: false,
			data: {
				username : username,
				phoneNumber : phoneNumber,
				email : email,
				gender : gender,
				viprankName : viprank,
				state : state
			},
			success:function(data){
				usersData = data;
            	usersList(usersData);
			},
			error:function(data){
			}
		});
        layer.close(index);
//            setTimeout(function(){
//            	$.ajax({
//					url : "../../json/usersList.json",
//					type : "get",
//					dataType : "json",
//					success : function(data){
//						if(window.sessionStorage.getItem("addUsers")){
//							var addUsers = window.sessionStorage.getItem("addUsers");
//							usersData = JSON.parse(addUsers).concat(data);
//						}else{
//							usersData = data;
//						}
//						for(var i=0;i<usersData.length;i++){
//							var usersStr = usersData[i];
//							var selectStr = $(".search_input").val();
//		            		function changeStr(data){
//		            			var dataStr = '';
//		            			var showNum = data.split(eval("/"+selectStr+"/ig")).length - 1;
//		            			if(showNum > 1){
//									for (var j=0;j<showNum;j++) {
//		            					dataStr += data.split(eval("/"+selectStr+"/ig"))[j] + "<i style='color:#03c339;font-weight:bold;'>" + selectStr + "</i>";
//		            				}
//		            				dataStr += data.split(eval("/"+selectStr+"/ig"))[showNum];
//		            				return dataStr;
//		            			}else{
//		            				dataStr = data.split(eval("/"+selectStr+"/ig"))[0] + "<i style='color:#03c339;font-weight:bold;'>" + selectStr + "</i>" + data.split(eval("/"+selectStr+"/ig"))[1];
//		            				return dataStr;
//		            			}
//		            		}
//		            		//用户名
//		            		if(usersStr.userName.indexOf(selectStr) > -1){
//			            		usersStr["userName"] = changeStr(usersStr.userName);
//		            		}
//		            		//用户邮箱
//		            		if(usersStr.userEmail.indexOf(selectStr) > -1){
//			            		usersStr["userEmail"] = changeStr(usersStr.userEmail);
//		            		}
//		            		//性别
//		            		if(usersStr.userSex.indexOf(selectStr) > -1){
//			            		usersStr["userSex"] = changeStr(usersStr.userSex);
//		            		}
//		            		//会员等级
//		            		if(usersStr.userGrade.indexOf(selectStr) > -1){
//			            		usersStr["userGrade"] = changeStr(usersStr.userGrade);
//		            		}
//		            		if(usersStr.userName.indexOf(selectStr)>-1 || usersStr.userEmail.indexOf(selectStr)>-1 || usersStr.userSex.indexOf(selectStr)>-1 || usersStr.userGrade.indexOf(selectStr)>-1){
//		            			userArray.push(usersStr);
//		            		}
//		            	}
//		            	usersData = userArray;
//		            	usersList(usersData);
//					}
//				})
//            	
//                layer.close(index);
//            },2000);
//		}else{
//			layer.msg("请输入需要查询的内容");
//		}
	})

	//添加会员
	$(".usersAdd_btn").click(function(){
		var index = layui.layer.open({
			title : "新增用户",
			type : 2,
			content : "addUser.html",
			success : function(layero, index){
				layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
					tips: 3
				});
			}
		})
		//改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
		$(window).resize(function(){
			layui.layer.full(index);
		})
		layui.layer.full(index);
	})

    //全选
	form.on('checkbox(allChoose)', function(data){
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
		child.each(function(index, item){
			item.checked = data.elem.checked;
		});
		form.render('checkbox');
	});

	//通过判断文章是否全部选中来确定全选按钮是否选中
	form.on("checkbox(choose)",function(data){
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
		var childChecked = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"]):checked')
		if(childChecked.length == child.length){
			$(data.elem).parents('table').find('thead input#allChoose').get(0).checked = true;
		}else{
			$(data.elem).parents('table').find('thead input#allChoose').get(0).checked = false;
		}
		form.render('checkbox');
	})

	//操作
	$("body").on("click",".users_edit",function(){
		var uid = $(this).attr("data-id");
		//		layer.alert('您点击了会员编辑按钮，由于是纯静态页面，所以暂时不存在编辑内容，后期会添加，敬请谅解。。。',{icon:6, title:'文章编辑'});
		var index = layui.layer.open({
			title : "修改用户",
			type : 2,
			content : "editUser.html?uid"+uid,
			success : function(layero, index){
				layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
					tips: 3
				});
				// 获取子页面的iframe  
                var iframe = window['layui-layer-iframe' + index];  
                // 向子页面的全局函数child传参  
                iframe.child(uid);
                
			}
		})
		//改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
		$(window).resize(function(){
			layui.layer.full(index);
		})
		layui.layer.full(index);
	})

	$("body").on("click",".users_del",function(){  //删除
		var _this = $(this);
		var uid = _this.attr("data-id");
		console.log(uid);
		layer.confirm('确定删除此用户？',{icon:3, title:'提示信息'},function(index){
				var alertMsg = layer.msg('正在删除',{icon: 16,time:false,shade:0.8});
				$.ajax({
					url: '/filter/json/user',
					type: 'POST',    
					dataType: 'json',
					async: false,
					data: {
						uid : uid,
						_method: 'DELETE'
					},
					success:function(data){
						if(data.code == 0){
							_this.parents("tr").remove();
							layer.close(index);
							layer.close(alertMsg);
//							layer.msg(data.msg,{icon: 16,time:500,shade:0.8});
						}
						layer.msg(data.msg);
					},
					error:function(data){
					}
				});
		});
	})

	//批量删除
	$(".batchDel").click(function(){
		var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
		var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
		if($checkbox.is(":checked")){
			layer.confirm('确定删除选中的信息？',{icon:3, title:'提示信息'},function(index){
				var alertMsg = layer.msg('删除中，请稍候',{icon: 16,time:false,shade:0.8});
				var uids='';
				//删除数据
				for(var j=0;j<$checked.length;j++){
					uids += $checked.eq(j).parents("tr").find(".users_del").attr("data-id")+',';
				}
				$.ajax({
					url: '/filter/json/users',
					type: 'POST',    
					dataType: 'json',
					async: false,
					data: {
						uids : uids,
						_method: 'DELETE'
					},
					success:function(data){
						if(data.code == 0){
							for(var j=0;j<$checked.length;j++){
			            		for(var i=0;i<usersData.length;i++){
									if(usersData[i].uid == $checked.eq(j).parents("tr").find(".users_del").attr("data-id")){
										usersData.splice(i,1);
									}
								}
			            	}
							usersList(usersData);
							form.render();
							layer.close(index);
							layer.close(alertMsg);
							$('.news_list thead input[type="checkbox"]').prop("checked",false);
						}
						layer.msg(data.msg);
					},
					error:function(data){
					}
				});
	        })
		}else{
			layer.msg("请选择需要删除的文章");
		}
	})
	
	function usersList(){
		//渲染数据
		function renderDate(data,curr){
			var dataHtml = '';
			currData = usersData.concat().splice(curr*nums-nums, nums);
			if(currData.length != 0){
				for(var i=0;i<currData.length;i++){
					dataHtml += '<tr>'
				    	+'<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose" value="'+currData[i].uid+'"></td>'
				    	+'<td align="left">'+currData[i].username+'</td>';
						dataHtml += '<td>';
						if(currData[i].gender == "0"){
				    		dataHtml += '女';
				    	}else{
				    		dataHtml += '男';
				    	}
				    	dataHtml += '</td>';
				    	dataHtml += '<td>'+currData[i].email+'</td>'
				    	+'<td>'+currData[i].phoneNumber+'</td>'
				    	+'<td>'+currData[i].viprankName+'</td>';
				    	dataHtml += '<td><input type="checkbox" name="show" lay-skin="switch" lay-text="是|否" lay-filter="isShow"';
				    	if(currData[i].state == "0"){
				    		dataHtml += 'checked';
				    	}
				    	dataHtml += '></td>'
				    	+'<td>'
						+  '<a class="layui-btn layui-btn-mini users_edit" data-id="'+currData[i].uid+'"><i class="iconfont icon-edit"></i> 编辑</a>'
						+  '<a class="layui-btn layui-btn-normal layui-btn-mini users_collect" data-id="'+currData[i].uid+'"><i class="layui-icon">&#xe600;</i> 收藏</a>'
						+  '<a class="layui-btn layui-btn-danger layui-btn-mini users_del" data-id="'+currData[i].uid+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
				        +'</td>'
				    	+'</tr>';
				}
			}else{
				dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
			}
		    return dataHtml;
		}

		//分页
		var nums = 13; //每页出现的数据量
		laypage({
			cont : "page",
			pages : Math.ceil(usersData.length/nums),
			jump : function(obj){
				$(".users_content").html(renderDate(usersData,obj.curr));
				$('.users_list thead input[type="checkbox"]').prop("checked",false);
		    	form.render();
			}
		})
	}
        
})