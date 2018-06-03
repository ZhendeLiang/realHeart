$(function(){
	// 全局，标识手机号状态
	var pstatus = false;

	// 全局，标识验证码状态
	var cstatus = false;

	// 检测手机号是否可注册
	function checkphone(phone){
	 	// 输入是否为空
		if (phone == '') {
			$('.tel-err').removeClass('hide').find("em").text('请输入手机');
 			$('.tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			pstatus = false;
			return false;
		}
		// 手机格式是否正确
		var param = /^1[345789]\d{9}$/;
		if (!param.test(phone)) {
			// globalTip({'msg':'手机号不合法，请重新输入','setTime':3});
			$('.tel-err').removeClass('hide').find("em").text('手机号不完整');
 			$('.tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			pstatus = false;
			return false;
		}
		// 检测手机号是否已经注册
		$.ajax({
            url: '/checkPhone',
            type: 'post',
            dataType: 'json',
            async: false,
            data: {phone:phone,type:"reg"},
            success:function(data){
                if (data.code == '1') {
                    // $('.tel-err').addClass('hide');
                    $('.tel-err').removeClass('hide').find("em").text('');
                    $('.tel-err').find("i").attr('class', 'icon-ok').css("color","#84d54b");
                    getcode();
                    pstatus = true;
                } else {
                	$('.send').off('click');
                    // $('.tel-err').removeClass('hide').text(data.msg);
                    $('.tel-err').removeClass('hide').find("em").text(data.msg);
 					$('.tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
					pstatus = false;
                }
            },
            error:function(){
            	pstatus = false;
            }
        });
        return pstatus;
	}

	// 检测是否输入验证码
	function checkcode(code) {
		if (code == '') {
			// $('.error').removeClass('hide').text('请输入验证码');
			$('.error').removeClass('hide').find("em").text('请输入验证码');
 			$('.error').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			return false;
		} else {
			$('.error').addClass('hide');
			return true;
		}
	}

	$('#passport').blur(function(event) {
		var password = $(this).val();
		checkpassport(password);
	});

	$('#passport2').blur(function(event) {
		var password2 = $(this).val();
		checkpassport2(password2);
	});

	// 检测密码是否合法
	function checkpassport(passport)
	{
		if (passport == '') {
			$('.pwd-err').removeClass('hide').find("em").text('请输入密码');
 			$('.pwd-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			return false;
		} else if(passport.length < 6 || passport.length > 20){
			$('.pwd-err').removeClass('hide').find("em").text('密码为6-20位字母或数字');
 			$('.pwd-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			return false;
		} else {
			$('.pwd-err').removeClass('hide').find("em").text('');
 			$('.pwd-err').find("i").attr('class', 'icon-ok').css("color","#84d54b");
			return true;
		}
	}

	// 检测密码是否一致
	function checkpassport2(passport2)
	{
		if (passport2 == '') {
			// $('.confirmpwd-err').removeClass('hide').text('请输入确认密码');
			$('.confirmpwd-err').removeClass('hide').find("em").text('请输入确认密码');
 			$('.confirmpwd-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			return false;
		} else if($('#passport').val() != $('#passport2').val()) {
			// $('.confirmpwd-err').removeClass('hide').text('两次密码输入不一致');
			$('.confirmpwd-err').removeClass('hide').find("em").text('两次密码不一致');
 			$('.confirmpwd-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			return false;
		} else {
			// $('.confirmpwd-err').addClass('hide');
			$('.confirmpwd-err').removeClass('hide').find("em").text('');
 			$('.confirmpwd-err').find("i").attr('class', 'icon-ok').css("color","#84d54b");
			return true;
		}
	}

	// 是否同意协议
	function checkagree(ag)
	{
		if (ag == '1') {
			// $('.pass-warn').addClass('hide');
			return true;
		} else {
			// $('.pass-warn').removeClass('hide').text('请阅读并同意用户协议');
			globalTip({'msg':'请阅读并同意用户协议!','setTime':3});
			return false;
		}
	}

	// 输入检测手机号
	$('#tel').blur(function(event) {
		checkphone($(this).val());
	});

	// 获取焦点事件
	$('#tel').focus(function(event) {
		$('.send').off('click');
	});

	// 验证码失去焦点检测验证码
	$('#veri-code').blur(function(){
		cpcode();
	});

	// 检测验证码是否正确
	function cpcode(){
		debugger;
		var phone = $.trim($('#tel').val());
		var code = $.trim($('#veri-code').val());
		if (checkcode(code)) {
			$.ajax({
		            url: '/checkPhoneVerifyCode',
		            type: 'post',
		            dataType: 'json',
		            async: false,
		            data: {phone:phone,veriCode:code,type:"reg"},
		            success:function(data){
		                if (data.code == '0') {
		                	cstatus = true;
		                	// $('.error').addClass('hide');
		                	$('.error').removeClass('hide').find("em").text('');
		                	$('.error').find("i").attr('class', 'icon-ok').css("color","#84d54b");
		                	$('#pwd').removeClass('hide');
		                	$('#confirmpwd').removeClass('hide');
		                } else {
		                	cstatus = false;
							// $('.error').removeClass('hide').text(data.msg);
							$('.error').removeClass('hide').find("em").text(data.msg);
		                	$('.error').find("i").attr('class', 'icon-warn').css("color","#d9585b");
							$('#pwd').addClass('hide');
		                	$('#confirmpwd').addClass('hide');
		                }
		            },
		            error:function(){
		            	cstatus = false;
		            	// $('.error').removeClass('hide').text(data.msg);
		            	$('.error').removeClass('hide').find("em").text(data.msg);
		                $('.error').find("i").attr('class', 'icon-warn').css("color","#d9585b");
						$('#pwd').addClass('hide');
		                $('#confirmpwd').addClass('hide');
		            }
		        });
		} else {
			cstatus = false;
			return false;
		}
		return cstatus;
	}

	// 注册按钮点击事件
	$('.lang-btn').on('click',function(){
		debugger;
		var phone = $.trim($('#tel').val());
		if (pstatus) {
			var code = $.trim($('#veri-code').val());
			if (cstatus) {
				var passport = $.trim($('#passport').val());
				var passport2 = $.trim($('#passport2').val());
				var ag = $("input[name='agree']").val();
				if (checkpassport(passport) && checkpassport2(passport2) && checkagree(ag)) {
					$.ajax({
			            url: '/registAction',
			            type: 'post',
			            dataType: 'json',
			            async: false,
			            data: {phone:phone,veriCode:code,passport:passport,passport2:passport2},
			            success:function(data){
			                if (data.code == '0') {
			                	// $('.tel-err').addClass('hide');
			                	// $('.pwd-err').addClass('hide');
			                	// $('.confirmpwd-err').addClass('hide');
			                	// $('.error').addClass('hide');
			                    globalTip({'msg':'恭喜您，注册成功!','setTime':3,'jump':true,'url':data.msg.url});
			                } else if(data.code == '1'){
								// $('.tel-err').removeClass('hide').text(data.msg);
								$('.tel-err').removeClass('hide').find("em").text(data.msg);
		                		$('.tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			                } else if(data.code == '2'){
			                	// $('.pwd-err').removeClass('hide').text(data.msg);
			                	$('.pwd-err').removeClass('hide').find("em").text(data.msg);
		                		$('.pwd-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			                } else if(data.code == '3'){
			                	// $('.confirmpwd-err').removeClass('hide').text(data.msg);
			                	$('.confirmpwd-err').removeClass('hide').find("em").text(data.msg);
		                		$('.confirmpwd-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			                } else if(data.code == '4'){
			                	// $('.error').removeClass('hide').text(data.msg);
			                	$('.error').removeClass('hide').find("em").text(data.msg);
		                		$('.error').find("i").attr('class', 'icon-warn').css("color","#d9585b");
			                }
			            },
			            error:function(){
			            	globalTip({'msg':'注册失败!','setTime':3});
			            }
			        });
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	});

	// 注册的回车事件
	$(window).keydown(function(event) {
    	if (event.keyCode == 13) {
    		$('.lang-btn').trigger('click');
    	}
    });

	// 发送验证码点击事件
	function getcode(){
		$(".form-data .send").on("click",function () {
			var phone = $('#tel').val();
			var note = $('#note').val();
			var veri = $('#veri').val();
			if (veri == '') {
				globalTip({'msg':'请输入图形验证码!','setTime':3});
				return false;
			}
			if (pstatus) {
				$.ajax({
		            url: '/getPhoneVerifyCode',
		            type: 'post',
		            dataType: 'json',
		            async: true,
		            data: {phone:phone,type:"regist",note:note,veri:veri},
		            success:function(data){
		            	debugger;
		                if (data.code == '0') {
		                    var oTime = $(".form-data .time"),
							oSend = $(".form-data .send"),
							num = parseInt(oTime.text()),
							oEm = $(".form-data .time em");
						    $('.send').hide();
						    oTime.removeClass("hide");
						    var timer = setInterval(function () {
						   	var num2 = num-=1;
					            oEm.text(num2);
					            if(num2==0){
					                clearInterval(timer);
					                oSend.text("重新发送验证码");
								    oSend.show();
					                oEm.text("120");
					                oTime.addClass("hide");
					            }
					        },1000);
		                } else if (data.code == '1'){
		                    globalTip({'msg':'验证码已发送!','setTime':3});
		                } else {
		                	globalTip({'msg':'图形验证码错误!','setTime':3});
		                	/*$('.code').find('img').attr('src','/verifyCode?'+Math.random());*/
		                	$('.code').find('img').attr('src','/verifyCode?type=regist&'+Math.random());
		                	return false;
		                }
		            },
		            error:function(){
		                globalTip({'msg':'验证码发送失败!','setTime':3});
		            }
		        });
			} else {
				return false;
			}
	    });
	}
	
/*	$('.code').find('img').attr('src','/verifyCode?'+Math.random()).click(function(event) {
	$(this).attr('src', '/verifyCode?'+Math.random());
});;*/
	$('.code').find('img').attr('src','/verifyCode?type=regist&'+Math.random()).click(function(event) {
    	$(this).attr('src', '/verifyCode?type=regist&'+Math.random());
    });;

});
