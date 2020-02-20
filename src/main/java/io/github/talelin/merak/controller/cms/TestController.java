package io.github.talelin.merak.controller.cms;

import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.Logger;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.annotation.RouteMeta;
import io.github.talelin.merak.extensions.message.WsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cms/test")
public class TestController {

    @Autowired
    private WsHandler wsHandler;

    @RequestMapping("")
    public String index() {
        try {
            wsHandler.broadCast("hello every body!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<style type=\"text/css\">*{ padding: 0; margin: 0; } div{ padding: 4px 48px;} a{color:#2E5CD5;cursor:" +
                "pointer;text-decoration: none} a:hover{text-decoration:underline; } body{ background: #fff; font-family:" +
                "\"Century Gothic\",\"Microsoft yahei\"; color: #333;font-size:18px;} h1{ font-size: 100px; font-weight: normal;" +
                "margin-bottom: 12px; } p{ line-height: 1.6em; font-size: 42px }</style><div style=\"padding: 24px 48px;\"><p>" +
                "Lin <br/><span style=\"font-size:30px\">心上无垢，林间有风。</span></p></div> ";
    }

    @RequestMapping("/json")
    @RouteMeta(permission = "测试日志记录", module = "信息", mount = true)
    @LoginRequired
    @Logger(template = "{user.nickname}又皮了一波")
    public Map getTestMsg() {
        Map res = new HashMap();
        res.put("msg", "物质决定意识，经济基础决定上层建筑");
        return res;
    }

    @RequestMapping("/info")
    @RouteMeta(permission = "查看lin的信息", module = "信息", mount = true)
    @GroupRequired
    public Map getTestInfo() {
        Map res = new HashMap();
        res.put("msg", "Lin 是一套基于 Spring boot 的一整套开箱即用的后台管理系统（CMS）。Lin 遵循简洁、高效的原则，通过核心库加插件的方式来驱动整个系统高效的运行");
        return res;
    }


}
