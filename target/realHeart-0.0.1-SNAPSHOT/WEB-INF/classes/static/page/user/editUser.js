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

	 	var uid = $("#uid").val();
	 	var username = $("#username").val();
	 	var password = $("#password").val();
	 	var phoneNumber = $("#phoneNumber").val();
	 	var email = $("#email").val();
	 	var gender = data.field.gender;
	 	var viprankName = data.field.viprank;
	 	var state = data.field.userStatus;
	 	var selfIntroduction = $("#selfIntroduction").val();

	 	console.log("uid:"+uid);
	 	console.log("username:"+username);
	 	console.log("password:"+password);
	 	console.log("phoneNumber:"+phoneNumber);
	 	console.log("email:"+email);
	 	console.log("gender:"+gender);
	 	console.log("viprankName:"+viprankName);
	 	console.log("state:"+state);
	 	console.log("selfIntroduction:"+selfIntroduction);
 		//弹出loading
 		var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
	 	$.ajax({
			url: '/filter/json/user',
			type: 'put',
			dataType: 'json',
			async: false,
			data: {
				uid : uid,
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
 		return false;
 	})
	
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
