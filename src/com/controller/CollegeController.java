package com.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dao.ApplyJobDao;
import com.dao.CollectionJobDao;
import com.dao.CollegeDao;
import com.dao.EducationDao;
import com.dao.JobListDao;
import com.po.MyCollectionJob;
import com.po.MyCollege;
import com.po.MyEducation;
import com.po.MyJobList;
import com.service.CollegeService;


@Controller
@RequestMapping("/college")
public class CollegeController {
    @Autowired
    private CollegeService collegeService;
    @Autowired
    private CollegeDao dao;
    @Autowired
    private EducationDao educationDao;
    @Autowired
    private CollectionJobDao collectionJobDao;
    @Autowired
    private JobListDao jobListdao;
    @Autowired
    private ApplyJobDao applyjobdao;


    //登陆
    @RequestMapping("/login")
    public String collegeLogin() {
        return "collegeLogin";
    }

    //退出
    @RequestMapping("/loginOut")
    public String collegeOut(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.removeAttribute("collegeId");
        session.removeAttribute("phone");
        System.out.println("退出成功");
        return "collegeLogin";
    }

    //收藏数据显示
    @RequestMapping("/collection")
    public String collection(HttpServletRequest request, Model model) {
        //先获取session中的collegeid然后将id
        HttpSession session = request.getSession();
        int collegeid = (int) session.getAttribute("collegeId");
        //从collegejob数据表中将collegeid = collegeid的所有记录记录都取出来
        List<Integer> jobids = collectionJobDao.selectListByCid(collegeid);
        //然后将enterpriseinfo中的id为jobid的记录取出 放入到model中
        List<MyJobList> myJobLists = new ArrayList<MyJobList>();
        for (int i = 0; i < jobids.size(); i++) {
            int id = jobids.get(i);
            MyJobList myJobList = jobListdao.selectJobinfoByid(id);

            myJobLists.add(myJobList);
        }
        model.addAttribute("collection", myJobLists);
        return "collection";
    }

    //删除收藏中的某些数据
    @RequestMapping("/delcollection")
    @ResponseBody
    public Map<String, String> delCollection(HttpServletRequest request, String id) {
        HttpSession session = request.getSession();
        int collegeid = (int) session.getAttribute("collegeId");
        System.out.println(id);
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("collegeid", collegeid);
        map2.put("jobid", id);
        try {
            collectionJobDao.deleteJobById(map2);
            map.put("msg", "success");
        } catch (Exception e) {
            map.put("msg", "fail");
        }

        return map;
    }

    @RequestMapping("/verificationLogin")
    public String verificationLogin(HttpServletRequest request, Model model) {

        String phone = request.getParameter("college");
        String password = request.getParameter("password");
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);

        if (collegeService.selectCollegeByphone(map, model, request)) {

            return "redirect:/job/see";
        } else {
            model.addAttribute("error", "密码或用户名错误");
            return "collegeLogin";
        }

    }

    //基本信息放入数据库并返回数据给前端
    @RequestMapping("/updatebaseinfo")
    public Map<String, Object> insertbaseinfo(HttpServletRequest request, Model model) {
        String nation = request.getParameter("nation");
        String email = request.getParameter("email");
        String hometown = request.getParameter("hometown");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        System.out.println(email);
        MyCollege myCollege = new MyCollege();
        HttpSession session = request.getSession();
        String phone = (String) session.getAttribute("phone");
        myCollege.setEmail(email);
        myCollege.setHometown(hometown);
        myCollege.setNation(nation);
        myCollege.setPhone(phone);
        dao.updatebaseinfo(myCollege);
        resultMap.put("college", myCollege);
        return resultMap;
    }

    //添加求职期望
    @RequestMapping("/updatejob_expectation")
    public Map<String, Object> insertJob_expection(HttpServletRequest request, Model model) {
        String work_loaction = request.getParameter("work_loaction");
        String distury = request.getParameter("distury");
        String position = request.getParameter("position");
        String salary = request.getParameter("salary");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        MyCollege myCollege = new MyCollege();
        HttpSession session = request.getSession();
        String phone = (String) session.getAttribute("phone");
        myCollege.setIndustry(distury);
        myCollege.setPosition(position);
        myCollege.setSalary(salary);
        myCollege.setPhone(phone);
        myCollege.setWorklocation(work_loaction);
        dao.updatejob_expectation(myCollege);
        return resultMap;

    }

    //添加自我评价
    @RequestMapping("/updateself_evaluation")
    public Map<String, Object> insertself_evaluation(HttpServletRequest request, Model model) {
        String self = request.getParameter("self");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        MyCollege myCollege = new MyCollege();
        HttpSession session = request.getSession();
        String phone = (String) session.getAttribute("phone");

        myCollege.setPhone(phone);
        myCollege.setSelfevaluation(self);
        dao.updateself_evaluation(myCollege);
        return resultMap;
    }

    //添加教育经历
    @RequestMapping("/updateeducational")
    public Map<String, Object> inserteducational(HttpServletRequest request, Model model) {
        String school = request.getParameter("school");
        String department = request.getParameter("department");
        String education = request.getParameter("education");
        String major = request.getParameter("major");
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");


        MyEducation myEducation = new MyEducation();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpSession session = request.getSession();
        String phone = (String) session.getAttribute("phone");
        System.out.println(phone);
        int id = (int) session.getAttribute("collegeId");
        System.out.println(id);
        myEducation.setSchool(school);
        myEducation.setEducation(education);
        myEducation.setMajor(major);
        myEducation.setStarttime(starttime);
        myEducation.setEndtime(endtime);
        myEducation.setCollege_id(id);
        myEducation.setDepartment(department);

        educationDao.insertinfo(myEducation);
        return resultMap;
    }

    //个人信息
    @RequestMapping("/person_info")
    public String person_info(HttpServletRequest request, Model model) {
        HttpSession session2 = request.getSession();
        int id = (int) session2.getAttribute("collegeId");
        MyCollege myCollege = dao.selectAllinfoByid(id);
        model.addAttribute("college", myCollege);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", id);

        MyEducation myEducations = educationDao.selectEducation(map2);
        model.addAttribute("education", myEducations);
        return "person_index";
    }

    //收藏职位
    @RequestMapping("/collectionJob")
    @ResponseBody
    public Map<String, String> collectionJob(String jobid, String userId) {
        System.out.println(jobid + "----------" + userId);
        int job_id = Integer.parseInt(jobid);
        int user_id = Integer.parseInt(userId);
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("collegeid", user_id);
        map2.put("jobid", job_id);
        int count = collectionJobDao.selectCountbyids(map2);
        if (count > 0) {
            map.put("msg", "该职位已收藏!");
            return map;
        } else {
            try {
                MyCollectionJob myCollectionJob = new MyCollectionJob();
                myCollectionJob.setCollegeid(user_id);
                myCollectionJob.setJobid(job_id);
                collectionJobDao.insertinfo(myCollectionJob);
                System.out.println("成功");
                map.put("msg", "success");
            } catch (Exception e) {
                System.out.println("失败");
                map.put("msg", "fail");
            }
            return map;
        }


    }


    //职位申请
    @RequestMapping("/applyposition")
    @ResponseBody
    public Map<String, String> applyposition(String jobid, HttpServletRequest request) {
        int job_id = Integer.parseInt(jobid);
        //获取当前的用户的Id值
        HttpSession session = request.getSession();
        int collegeid = (int) session.getAttribute("collegeId");
        Map<String, String> map = new HashMap<String, String>();                //返回给前端的数据
        Map<String, Object> map2 = new HashMap<String, Object>();            //提交给数据的数据 查询数据 判断是否存在该申请
        map2.put("jobid", job_id);
        map2.put("collegeid", collegeid);
        //判断该职位是否还存在
        int count_job = jobListdao.selectCountById(job_id);
        System.out.println("该职位是否存在" + "-------" + count_job);
        if (count_job > 0) {
            //如果存在就查询是否已经申请过了
            int count_apply = applyjobdao.selectCountbyids(map2);
            System.out.println("是否已经申请过该职位" + "------" + count_apply);
            if (count_apply == 0) {
                //没有申请过再插入数据库 并将当前的状态保存为1，使用数据来代表状态
                applyjobdao.insertinfo(map2);
                System.out.println("数据插入成功");
                map.put("msg", "申请成功");
            } else {
                //若果已经申请过了就返回 已申请过了
                map.put("msg", "已经申请过该职位了，请勿重复操作!");
            }
        } else {
            //不能存在就返回该职位已经结束
            map.put("msg", "该职位已结束申请");
        }


        return map;
    }

    //申请记录
    @RequestMapping("/applycollection")
    public String applycollection(HttpServletRequest request, Model model) {
        //获取页面
        //初始化的时候市将状态为1的数据提交给前端
        HttpSession session = request.getSession();
        int college_id = (int) session.getAttribute("collegeId");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("collegeid", college_id);
        map.put("status", 1);
        List<Integer> jobids = applyjobdao.selectJobIdByStatus(map);
        List<MyJobList> jobs = new ArrayList<MyJobList>();
        for (int i = 0; i < jobids.size(); i++) {
            MyJobList myJobList = jobListdao.selectJobinfoByid(jobids.get(i));
            jobs.add(myJobList);
        }
        model.addAttribute("jobs1", jobs);

        //在数据库查询
        return "applyCollection";
    }

    @RequestMapping("/applycollection/status")
    @ResponseBody
    public Map<String, Object> getUserapplycollection(HttpServletRequest request, Model model, String status) {
        //ajax获取数据
        HttpSession session = request.getSession();
        int college_id = (int) session.getAttribute("collegeId");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        map.put("collegeid", college_id);
        map.put("status", Integer.parseInt(status));

        List<Integer> jobids = applyjobdao.selectJobIdByStatus(map);
        if (jobids.size() == 0) {
            map2.put("msg", "fail");
        } else {
            List<MyJobList> jobs = new ArrayList<MyJobList>();
            for (int i = 0; i < jobids.size(); i++) {
                MyJobList myJobList = jobListdao.selectJobinfoByid(jobids.get(i));
                jobs.add(myJobList);
            }

            model.addAttribute("jobs" + status, jobs);
            map2.put("msg", "success");
            map2.put("content", jobs);
        }

        return map2;
    }
    //学生注册
    @RequestMapping("/reigster")
    public String collegeRegister(Model model) {
    	model.addAttribute("mycollege", new MyCollege());
        return "collegeRegister";
    }
    @RequestMapping("/Checkreigster")
    public String CheckRegister(@Valid @ModelAttribute("mycollege")MyCollege mycollege ,BindingResult result,Model model) {
        System.out.println("数据添加成功"+mycollege.getName());
        if(result.hasErrors()) {
        	return "collegeRegister";
        }
        //插入数据
        dao.insertintoValues(mycollege);
    	return "redirect:login";
    }


}
