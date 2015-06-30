package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hepler.SeqHelper;

public class GetUrlServlet  extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		IOException {
		String url = new SeqHelper().getTop();
		System.out.println("url:"+url);
		if(url == null){
			url = "none";
		}
		PrintWriter out = response.getWriter();
		out.print(url);
		out.flush();
		out.close();
	}
	
}
