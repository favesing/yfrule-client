package com.yf.rule.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yf.rule.test.InvokeTest;

/**
 * Servlet implementation class InvokeServlet
 */
public class InvokeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvokeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendRedirect("invoke.jsp");
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		String type = request.getParameter("type");
		InvokeTest invoke = new InvokeTest();
		try{
			if(type.equals("rule")){
				String ruleOutput = invoke.doRule(null);
				response.getWriter().append(ruleOutput);
			}else if(type.equals("flow")){
				String flowOutput = invoke.doFlow(null);
				response.getWriter().append(flowOutput);
			}
			
		}catch (Exception e) {
			response.getWriter().append(e.getMessage());
		}
	}

}
