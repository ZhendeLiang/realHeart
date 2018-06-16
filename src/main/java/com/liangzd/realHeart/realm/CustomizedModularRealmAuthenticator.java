package com.liangzd.realHeart.realm;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * 
 * @Description: 自定义业务继承模块化Realm认证器,并重写doAuthenticate方法
 * @author liangzd
 * @date 2018年6月16日 下午7:22:49
 */
public class CustomizedModularRealmAuthenticator extends ModularRealmAuthenticator{

	@Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        CustomizedToken customizedToken = (CustomizedToken) authenticationToken;
        // 登录类型
        String loginType = customizedToken.getLoginType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(loginType))
                typeRealms.add(realm);
        }
        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1)
            return doSingleRealmAuthentication(typeRealms.iterator().next(), customizedToken);
        else
            return doMultiRealmAuthentication(typeRealms, customizedToken);
    }
	
}
