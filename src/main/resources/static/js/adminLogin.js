$(function(){
	$("#adminSubmit").click(function(){
		debugger;
		var adminUsername =$("#adminUsername").val();
		var adminPassword =$("#adminPassword").val();
		if (adminUsername == null || adminUsername == undefined || adminUsername == '' || adminUsername == '用户名') { 
			alert("用户名空");
			return false;
		}else{
			if(adminPassword == null || adminPassword == undefined || adminPassword == '' || adminPassword == '密码'){
				alert("密码空");
				return false;
			}else{
				$.ajax({
					url: '/verifyLogin',
					type: 'post',
					dataType: 'json',
					async: true,
					data: {adminUsername:adminUsername,adminPassword:adminPassword},
					success:function(data){
						debugger;
						if (data.code == '0') {
							window.location.href = data.msg.URL;
						} else if(data.code == '1') {
							alert('用户名不正确');
							return false;
						} else if(data.code == '2') {
							alert('密码不正确');
							return false;
						}
					},
					error:function(){
						alert('系统错误');
					}
				});
			}
		}
	});
});