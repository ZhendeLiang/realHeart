$(function(){
	// 全局，用户输入账户类型
	var dtype = 'phone';
	// 全局用户输入状态
	var status = false;
	// 用户输入合法且为邮箱时，显示图片验证码
	$("#pc_tel").blur(function () {
        var pc_tel_con = $(this).val();
        if(checkInput(pc_tel_con)){
        	// if (dtype == 'email') {
        		$(".pc-code").find('img').attr('src','/verifyCode?type=reset&'+Math.random()).click(function(event) {
	            	$(this).attr('src','/verifyCode?type=reset&'+Math.random());
	            });
	            // $('.pc-code').find('a').click(function(event) {
	            // 	$('.pc-code').find('img').attr('src','/verifyCode?type=reset');
	            // });
        	// }
        }
    });

	$("#pc_tel").focus(function(event) {
		$(".pc-very").hide();
		$(".pc-code").hide();
	});

	// 检测账户类型，并检测是否注册
 	function checkInput(inp)
 	{
 		if (inp == '') {
 			$('.pc_tel-err').removeClass('hide').find("em").text('请输入账户');
 			$('.pc_tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
 			status = false;
 			return false;
 		} else {
 			if((/^1[34578]\d{9}$/.test(inp))){
 				dtype = 'phone';
 				return sendAjax(inp, dtype);
 				
            }else if(/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/.test(inp)){
            	dtype = 'email';
            	return sendAjax(inp, dtype);
            }else{
            	status = false;
            	$(".pc-very").hide();
		        $(".pc-code").hide();
            	$('.pc_tel-err').removeClass('hide').find("em").text('账号不合法');
            	$('.pc_tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
            	return false;
            }
 		}
 	}

 	// 检测用户输入数据
 	function sendAjax(inp, type)
 	{
 		var url = '/checkPhone';
 		if(type == 'email'){
 			url = '/checkEmail';
 		}
		$.ajax({
            url: url,
            type: 'post',
            dataType: 'json',
            async: false,
            data: {inp:inp,type:type,phone:inp,email:inp},
            success:function(data){
                if (data.code == '0') {
                    if (type == 'phone') {
                    	status = true;
		                $(".pc-very").show();
		                $(".pc-code").show();
                    } else {
                    	status = true;
	                    $(".pc-code").show();
		                $(".pc-very").hide();
                    }
                    $('.pc_tel-err').removeClass('hide');
                    $('.pc_tel-err').find("i").attr('class', 'icon-ok');
		            $('.pc_tel-err').find("i").css("color","#84d54b");
		            $('.pc_tel-err').find("em").text("");
                } else {
                	if (dtype == 'phone') {
                		$(".pc-very").hide();
                	} else {
                		$(".pc-code").hide();
                	}
                	$('.pc_tel-err').removeClass('hide');
                	$('.pc_tel-err').find("i").attr('class', 'icon-warn');
		            $('.pc_tel-err').find("i").css("color","#d9585b");
		            $('.pc_tel-err').find("em").text(data.msg);
                	status = false;
                }
            },
            error:function(){
                status = false;
            }
        });
        return status;
 	}

 	// 检测是否输入验证码
 	function checkcode(type, code)
 	{
 		if (type == 'phone') {
 			if (code == '') {
 				$('.error').removeClass('hide').find("em").text('请输入验证码');
 				return false;
 			} else {
 				$('.error').addClass('hide');
 				return true;
 			}
 		} else {
 			if (code == '') {
 				$('.img-err').removeClass('hide').find("em").text('请输入验证码');
 				return false;
 			} else {
 				$('.img-err').addClass('hide');
 				return true;
 			}
 		}
 	}

 	// 下一步点击事件
 	$('.next').click(function(event) {
 		;
 		var inp = $("#pc_tel").val();
 		var phone = '';
 		var email = '';
 		var veri = $('#veri').val();
 		if (dtype == 'phone') {
 			var code = $('#veri-code').val();
 	 		var phone = inp;
 		} else {
 			var code = $('#veri').val();
 			email = inp;
 		}
 		if (status) {
	 		if (checkcode(dtype, code)) {
	 			$.ajax({
		            url: '/resetVerify',
		            type: 'post',
		            dataType: 'json',
		            async: true,
		            data: {phone:phone,email:email,veriCode:code,type:dtype,veri:veri},
		            success:function(data){
		            	;
		                if (data.code == '0') {
		                	$('.pc_tel-err').removeClass('hide');
		                	$('.pc_tel-err').find("i").attr('class', 'icon-ok').css("color","#84d54b");
		            		$('.pc_tel-err').find("em").text("");
		            		if (dtype == 'email') {
		            			$('.img-err').removeClass('hide');
		            			$('.img-err').find("i").attr('class', 'icon-ok').css("color","#84d54b");
		            			$('.img-err').find("em").text("");
		            		} else {
		            			$('.error').removeClass('hide');
		            			$('.error').find('i').attr('class', 'icon-ok').css("color","#84d54b");
		            			$('.error').find("em").text("");
		            		}
		                	globalTip(data.msg);
		                } else if(data.code == '1' || data.code == '3'){
		                    $('.pc_tel-err').removeClass('hide').find("em").text(data.msg);
		                    $('.pc_tel-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
		                    return false;
		                } else if(data.code == '2'){
		                	if (dtype == 'email') {
		                		$(".pc-code").find('img').attr('src','/verifyCode?type=reset&'+Math.random());
		                		$('.img-err').removeClass('hide').find("em").text(data.msg);
		                		$('.img-err').find("i").attr('class', 'icon-warn').css("color","#d9585b");
		                	} else {
		                		$('.error').removeClass('hide').find("em").text(data.msg);
		                		$('.error').find("i").attr('class', 'icon-warn').css("color","#d9585b");
		                	}
		                    
		                    return false;
		                }
		            },
		            error:function(){
		                
		            }
		        });
	 		} else {
	 			return false;
	 		}
	 	} else {
	 		return false;
	 	}
 	});


	// 重置密码验证和点击事件
    $(".finish").click(function () {
	    var oReset = $("#pc_reset").val();
	    var oReset_sure = $("#pc_reset2").val();
	    var oTip = $(".reset-err");
	    var oTip2 = $('.reset-err2');
	    var uid = $('#uid').val();
	    var verifyUUID = $('#verifyUUID').val();
	    var type = $('#type').val();
	    if (oReset == "") {
	    	oTip.removeClass('hide');
	    	oTip.find('em').text("请输入密码！");
	    	oTip.find("i").attr('class', 'icon-warn').css("color","#d9585b");
		 	return false;
	    } else {
	    	oTip.removeClass('hide');
	    	oTip.find('em').text("");
	    	oTip.find("i").attr('class', 'icon-ok').css("color","#84d54b");
	    }
	    if (oReset.length < 6 || oReset.length > 20 ) {
	    	oTip.removeClass('hide');
	    	oTip.find('em').text("密码长度为6-20位！");
	    	oTip.find("i").attr('class', 'icon-warn').css("color","#d9585b");
	    	return false;
	    } else {
	    	oTip.removeClass('hide');
	    	oTip.find('em').text("");
	    	oTip.find("i").attr('class', 'icon-ok').css("color","#84d54b");
	    }
	    if (oReset_sure == "") {
	    	oTip2.removeClass('hide');
	    	oTip2.find('em').text("请输入验证密码！");
	    	oTip2.find("i").attr('class', 'icon-warn').css("color","#d9585b");
		 	return false;
	    } else {
	    	oTip2.removeClass('hide');
	    	oTip2.find('em').text("");
	    	oTip2.find("i").attr('class', 'icon-ok').css("color","#84d54b");
	    }
	    if (oReset_sure != oReset) {
	    	oTip2.removeClass('hide');
	    	oTip2.find('em').text("两次密码不一致");
	    	oTip2.find("i").attr('class', 'icon-warn').css("color","#d9585b");
		 	return false;
	    } else {
	    	oTip2.removeClass('hide');
	    	oTip2.find('em').text("");
	    	oTip2.find("i").attr('class', 'icon-ok').css("color","#84d54b");
	    }
   		$.ajax({
            url: '/resetPassword',
            type: 'post',
            dataType: 'json',
            async: true,
            data: {passport:oReset,passport2:oReset_sure,uid:uid,verifyUUID:verifyUUID,type:type},
            success:function(data){
                if (data.code == '0') {
                	globalTip(data.msg);
                } else {
                	globalTip(data);
                }
            },
            error:function(){
                
            }
        });
    });

    // 重置密码的回车事件
    $(window).keydown(function(event) {
    	if (event.keyCode == 13) {
    		$('.finish').trigger('click');
    		$('.next').trigger('click');
    	}
    });

    // 手机验证码
    $(".form-data").delegate(".send","click",function () {
    	var inp = $("#pc_tel").val();
    	var note = $('#note').val();
    	var code = $('#veri').val();
    	if (code == '') {
    		globalTip({'msg':'请输入图形验证码!','setTime':3});
    		return false;
    	}
    	if (dtype == 'phone' && status) {
    		$.ajax({
	            url: '/getPhoneVerifyCode',
	            type: 'post',
	            dataType: 'json',
	            async: true,
	            data: {phone:inp,type:"reset",note:note,veri:code},
	            success:function(data){
	                if (data.code == '0') {
	                    var oTime = $(".form-data .time"),
						oSend = $(".form-data .send"),
						num = parseInt(oTime.text()),
						oEm = $(".form-data .time em");
					    oSend.hide();
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
	                } else if(data.code == '1'){
	                    globalTip({'msg':'验证码已发送!','setTime':3});
	                } else {
	                	$(".pc-code").find('img').attr('src','/verifyCode?type=reset&'+Math.random())
	                	globalTip({'msg':'图形验证码错误!','setTime':3});
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
});