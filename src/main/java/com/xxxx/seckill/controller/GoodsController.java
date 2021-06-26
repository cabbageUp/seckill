package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

//windows优化前5000x10QPS:962.1,1000x10qps1075
//Linux优化前5000x10QPS:1446.7/sec
    @RequestMapping(value="/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
//        if(StringUtils.isEmpty(ticket))
//            return "login";
////        User user=(User)httpsession.getAttribute(ticket);
//        User user=userService.getUserByCookie(ticket,request,response);
//        if(user==null)
//            return "login";
        ValueOperations valueOperations=redisTemplate.opsForValue();
        //redis获取页面
        String html=(String)valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());

        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);

        }
        return html;
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model,User user, @PathVariable Long goodsId){
//        ValueOperations valueOperations=redisTemplate.opsForValue();
//        String html=(String)valueOperations.get("goodsDetail:"+goodsId);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
//        model.addAttribute("user",user);
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate=goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        int remainSeconds=0;
        int secKillStatus=0;
        if(nowDate.before(startDate)){
            //秒杀未开始
            remainSeconds=(int)((startDate.getTime()-nowDate.getTime())/1000);
        }
        else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds=-1;
        }
        else{
            //秒杀进行中
            secKillStatus=1;

        }
//        model.addAttribute("secKillStatus",secKillStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
//        model.addAttribute("goods",goodsVo);

        //手动渲染
//        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
//        html=thymeleafViewResolver.getTemplateEngine().process("goodsDetail",webContext);
//        if(!StringUtils.isEmpty(html)){
//            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
//        }
//        return html;
        DetailVo detailVo=new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);

        return RespBean.success(detailVo);

    }

    @RequestMapping(value="/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user,@PathVariable Long goodsId,HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations=redisTemplate.opsForValue();
        String html=(String)valueOperations.get("goodsDetail:"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate=goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        int remainSeconds=0;
        int secKillStatus=0;
        if(nowDate.before(startDate)){
            //秒杀未开始
            remainSeconds=(int)((startDate.getTime()-nowDate.getTime())/1000);
        }
        else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds=-1;
        }
        else{
            //秒杀进行中
            secKillStatus=1;

        }
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goodsVo);

        //手动渲染
        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goodsDetail",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        return html;
    }

}
