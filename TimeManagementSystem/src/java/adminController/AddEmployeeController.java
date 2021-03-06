/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminController;

import dal.AccountDAO;
import dal.AdminDAO;
import dal.EmployeeDAO;
import dal.GroupDAO;
import dal.WorkTimeDAO;
import employeeCotroller.BaseAuthenticationController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Account;
import model.Admin;
import model.Employee;
import model.Group;
import model.WorkTime;

/**
 *  
 * @author Do Duc Duong
 */
public class AddEmployeeController extends BaseAuthenticationController {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WorkTimeDAO dbWorktime = new WorkTimeDAO();
        GroupDAO dbGroup = new GroupDAO();
        ArrayList<WorkTime> listTime = dbWorktime.getWorkTime();
        EmployeeDAO dbEmp = new EmployeeDAO();
        AdminDAO dbAdmin = new AdminDAO();
        AccountDAO dbAcc  = new AccountDAO();
        ArrayList<Account> listAcc = dbAcc.getAllAccount();
        ArrayList<Employee> listEmp = dbEmp.getEmployee();
        ArrayList<Admin> listAdmin = dbAdmin.getAllAdmin();
        ArrayList<Group> listGroup = dbGroup.getAllGroup();
        Account a = (Account) request.getSession().getAttribute("account");
        Admin admin = dbAdmin.getAdmin(a.getUsername());
        
        request.setAttribute("adminName", admin.getName());
        request.setAttribute("listGroup", listGroup);
        request.setAttribute("listAcc", listAcc);
        request.setAttribute("listAd", listAdmin);
        request.setAttribute("listEmp", listEmp);
        request.setAttribute("listTime", listTime);
        request.getRequestDispatcher("adminView/add-employee.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GroupDAO dbGroup = new GroupDAO();
        ArrayList<Group> listGroup = dbGroup.getAllGroup();
        AccountDAO dbAcc = new AccountDAO();
        String name = request.getParameter("name");
        String user = request.getParameter("user");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String pass = request.getParameter("pass");
        String userType = request.getParameter("userType");
        String gender = request.getParameter("gender");
        boolean isAdmin = true;
        int groupId = -1;
        for(int i = 0; i < listGroup.size(); i++) {
            if(listGroup.get(i).getName().equals(userType)) {
                groupId = listGroup.get(i).getId();
            }
        }
        dbAcc.insertAccount(user, pass, groupId);
        if(!userType.equals("Admin")) {
            isAdmin = false;
        }
        int timeId = -1;
        if(!isAdmin) {
            timeId = (Integer.parseInt(request.getParameter("timeId")));
            EmployeeDAO dbEmp = new EmployeeDAO();
            dbEmp.insertEmployee(name, gender, dob, email, timeId, user);
        } else {
            AdminDAO dbAd = new AdminDAO();
            dbAd.insertAdmin(name, gender, dob, email, user);
        }
        
        response.sendRedirect("add-employee");
    }

}
