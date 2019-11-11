package com.bytedance.netty.io.tomcat.servlet;

import com.bytedance.netty.io.tomcat.http.GPRequest;
import com.bytedance.netty.io.tomcat.http.GPResponse;
import com.bytedance.netty.io.tomcat.http.GPServlet;

public class FirstServlet extends GPServlet {

	@Override
	public void doGet(GPRequest request, GPResponse response) throws Exception {
		this.doPost(request, response);
	}

	@Override
	public void doPost(GPRequest request, GPResponse response) throws Exception {
		response.write("This is First Serlvet");
	}

}
