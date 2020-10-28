package test.demo.controller;

import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.demo.doman.Oautch;
import test.demo.utils.HttpClientUtil;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
@RestController
@RequestMapping("/api/common")
public class TestController {
    //验证
    @RequestMapping("/oautch")
    public void token(HttpServletResponse httpServletResponse) throws Exception {
        String path= Oautch.REDIRECT_URI+"/api/common/invoke";//这里拼接你的URL地址，用户授权同意之后会调用的接口方法。
        path= URLEncoder.encode(path,"UTF-8");
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid="+Oautch.appID+
                "&redirect_uri="+path+
                "&response_type=code&" +
                "scope=snsapi_userinfo&" +
                "state=wechat_redirect";
        httpServletResponse.sendRedirect(url);
    }
    //用户同意执行回调
    @RequestMapping(value = "/invoke",produces="application/json; charset=utf-8")
    public String token1(String code, String state) throws IOException {
        /*
         * 得到code值，获取用户的openid获取token
         * */
        String path="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Oautch.appID+"&secret="+Oautch.appSecret+"&code="+code+"&grant_type=authorization_code";
        JSONObject jsonObject = new JSONObject(HttpClientUtil.doGet(path));
        String access_token = (String) jsonObject.get("access_token");//得到token
        System.out.println(access_token);
        String openid = (String) jsonObject.get("openid");//得到openid
        //根据code值和oppenid获取用户的基本信息
        String path2="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        String s = HttpClientUtil.doGet(path2);
        System.out.println(s);
        return s;
    }
}
