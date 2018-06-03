var $;
layui.config({
	base : "js/"
}).use(['form','layer','jquery'],function(){
	var form = layui.form(),
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage;
		$ = layui.jquery;

 	var addUserArray = [],addUser;
 	form.on("submit(addUser)",function(data){
 		//是否添加过信息
	 	if(window.sessionStorage.getItem("addUser")){
	 		addUserArray = JSON.parse(window.sessionStorage.getItem("addUser"));
	 	}
	 	
//	 	var userStatus,userGrade,userEndTime;
//	 	//会员等级
//	 	if(data.field.userGrade == '0'){
// 			userGrade = "注册会员";
// 		}else if(data.field.userGrade == '1'){
// 			userGrade = "中级会员";
// 		}else if(data.field.userGrade == '2'){
// 			userGrade = "高级会员";
// 		}else if(data.field.userGrade == '3'){
// 			userGrade = "超级会员";
// 		}
// 		//会员状态
// 		if(data.field.userStatus == '0'){
// 			userStatus = "正常使用";
// 		}else if(data.field.userStatus == '1'){
// 			userStatus = "限制用户";
// 		}
//
// 		addUser = '{"usersId":"'+ new Date().getTime() +'",';//id
// 		addUser += '"userName":"'+ $(".userName").val() +'",';  //登录名
// 		addUser += '"userEmail":"'+ $(".userEmail").val() +'",';	 //邮箱
// 		addUser += '"userSex":"'+ data.field.gender +'",'; //性别
// 		addUser += '"userStatus":"'+ userStatus +'",'; //会员等级
// 		addUser += '"userGrade":"'+ userGrade +'",'; //会员状态
// 		addUser += '"userEndTime":"'+ formatTime(new Date()) +'"}';  //登录时间
// 		console.log(addUser);
// 		addUserArray.unshift(JSON.parse(addUser));
// 		window.sessionStorage.setItem("addUser",JSON.stringify(addUserArray));
	 	
	 	var username = $("#username").val();
	 	var password = $("#password").val();
	 	var phoneNumber = $("#phoneNumber").val();
	 	var email = $("#email").val();
	 	var gender = data.field.gender;
	 	var viprand = data.field.viprank;
	 	var state = data.field.userStatus;
	 	var selfIntroduction = $("#selfIntroduction").val();
	 	
	 	console.log("username:"+username);
	 	console.log("password:"+password);
	 	console.log("phoneNumber:"+phoneNumber);
	 	console.log("email:"+email);
	 	console.log("gender:"+gender);
	 	console.log("viprand:"+viprand);
	 	console.log("state:"+state);
	 	console.log("selfIntroduction:"+selfIntroduction);
 		if(checkUsername(username) && checkPhone(phoneNumber) && checkEmail(email)){
 			//弹出loading
 			var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
	 		$.ajax({
				url: '/filter/json/user',
				type: 'post',
				dataType: 'json',
				async: false,
				data: {
					username : username,
					password : password,
					phoneNumber : phoneNumber,
					email : email,
					gender : data.field.gender,
					viprankName : data.field.viprank,
					state : data.field.userStatus,
					selfIntroduction : selfIntroduction
				},
				success:function(data){
					top.layer.close(index);
					top.layer.msg(data.msg);
		 			layer.closeAll("iframe");
			 		//刷新父页面
			 		parent.location.reload();
				},
				error:function(data){
					top.layer.close(index);
					top.layer.msg(data.msg);
		 			layer.closeAll("iframe");
			 		//刷新父页面
			 		parent.location.reload();
				}
			});
	 	}
	 	return false;
 	})
	
 	function checkUsername(username){
		var status = true;
		$.ajax({
            url: '/checkUsername',
            type: 'post',
            dataType: 'json',
            async: false,
            data: {username:username,type:"addUser"},
            success:function(data){
                if (data.code == '1') {
                	status = true;
                } else {
                	status = false;
                	alert(data.msg);
                }
            },
            error:function(){
            	status = false;
            	alert(data.msg);
            }
        });
		return status;
	}
 	
 	function checkPhone(phone){
		var status = true;
		$.ajax({
            url: '/checkPhone',
            type: 'post',
            dataType: 'json',
            async: false,
            data: {phone:phone,type:"addUser"},
            success:function(data){
                if (data.code == '1') {
                	status = true;
                } else {
                	status = false;
                	alert(data.msg);
                }
            },
            error:function(){
            	status = false;
            	alert(data.msg);
            }
        });
		return status;
	}
 	
 	function checkEmail(email){
		var status = true;
		$.ajax({
            url: '/checkEmail',
            type: 'post',
            dataType: 'json',
            async: false,
            data: {email:email,type:"addUser"},
            success:function(data){
                if (data.code == '1') {
                	status = true;
                } else {
                	status = false;
                	alert(data.msg);
                }
            },
            error:function(){
            	status = false;
            	alert(data.msg);
            }
        });
		return status;
	}
})

//格式化时间
function formatTime(_time){
    var year = _time.getFullYear();
    var month = _time.getMonth()+1<10 ? "0"+(_time.getMonth()+1) : _time.getMonth()+1;
    var day = _time.getDate()<10 ? "0"+_time.getDate() : _time.getDate();
    var hour = _time.getHours()<10 ? "0"+_time.getHours() : _time.getHours();
    var minute = _time.getMinutes()<10 ? "0"+_time.getMinutes() : _time.getMinutes();
    return year+"-"+month+"-"+day+" "+hour+":"+minute;
}
