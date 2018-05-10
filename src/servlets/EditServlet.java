package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.DTO.PackagePermissionDTO;

import DTO.RobotDTO;
import DTO.UserDTO;
import Service.GetRobotRestClientService;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.DAO.ConnectionFactory;
import com.mysql.jdbc.Statement;



/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	PreparedStatement ptmt = null;
	ResultSet resultSet = null;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**	
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Hi");
		HttpSession session = request.getSession();
		RobotDTO robotDTO = new RobotDTO();
		String selectedItem = null;
		System.out.println(request.getParameter("domain_name"));
		if (request.getParameter("domain_name") != null) {
			selectedItem = request.getParameter("domain_name");
		}
		System.out.println(selectedItem);
		String[] words = selectedItem.split("-");
		String uName=null;
		uName = (String)session.getAttribute("username");
		String userid =uName;
		String packageid = words[1];
		String robotid = words[2];
		robotid=robotid.trim();
String newstmt2=null;
ResultSet resultset2 = null;
		session.setAttribute("tenant_name", userid);
		RobotDTO robotAccessDTO = new RobotDTO();

		robotAccessDTO.setUserId(uName);
		robotAccessDTO.setRobotName(robotid);
		robotAccessDTO.setPackageId(packageid);
		
		String func= null;

		//List<String> robotList = RobotDAO.getRobotList(robotAccessDTO);

		
		System.out.println("tenentid "+userid+"packageid "+packageid+"robotName "+robotid);
		String url = "jdbc:mysql://localhost:3306/robocode";
		String user = "root";
		String password = "root";
		System.out.println("Hi");

		try {
			Connection conn = DriverManager.getConnection(url, user, password);

		
			Statement statement = (Statement) conn.createStatement();
		String newstmt = null;
		func = (String)session.getAttribute("function");
			/*if(func.equals("edit")){
				newstmt = "SELECT file,filepath,RobotCode,id from robot where robotID='"+robotid+"' and packageID='"+packageid+"'and userID='"+userid+"'";
				//newstmt2 = "SELECT file,filepath,RobotCode,id from robot where robotID='"+robotid+"' and packageID='"+packageid+"'and  writeUser Like'"+"%"+uName+"%"+"'";
		//session.setAttribute("function","view");
			}else{*/

 				newstmt = "SELECT file,filepath,RobotCode,id,userID,writeUser from robot where robotID='"+robotid+"' and packageID='"+packageid+"'";
			//}

			//String newstmt = "SELECT file,filepath,RobotCode,id from robot where robotID='"+robotid+"' and packageID='"+packageid+"'";
//' and userID = '"+userid+"'";
			System.out.println(newstmt);
			resultSet = statement.executeQuery(newstmt);

			//System.out.println("swxwxdedx");
			String Robocode = "";
			//System.out.println("Hi");
			/*Blob blob=null;
			while(resultSet.next()){
				blob=resultSet.getBlob("file");
				robotAccessDTO.setFilePath(resultSet.getString("filepath"));
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];
			InputStream in = blob.getBinaryStream();
			int n = 0;
			while ((n=in.read(buf))>=0)
				baos.write(buf, 0, n);
			in.close();
			
			byte[] bytes = baos.toByteArray();
	
			Robocode = new String(bytes);*/
			//System.out.println("Robocode is "+Robocode);
String editUser = null;
String owner = null;

			while(resultSet.next() ){
				//if(resultSet.next()){
				if(func.equals("edit")){
					owner = resultSet.getString("userID");
					editUser = resultSet.getString("writeUser");
					if(userid.equals(owner) || editUser.contains(userid)){
						Robocode=resultSet.getString("RobotCode");
						robotAccessDTO.setFilePath(resultSet.getString("filepath"));
					}
					
				
				}else{
					Robocode=resultSet.getString("RobotCode");
					robotAccessDTO.setFilePath(resultSet.getString("filepath"));

				}
					
	
				//}
				
			}
			session.setAttribute("RobObj", robotAccessDTO);
			//System.out.println("Robocode blah:"+Robocode);
			session.setAttribute("robocode", Robocode);
			request.setAttribute("robocode", Robocode);
			PrintWriter out = response.getWriter();
			out.println(Robocode);

		}
		catch (Exception e) {
			 e.printStackTrace();
response.sendRedirect("accessdenied.jsp");
		}
	}		
	static String readFile(String path, Charset encoding) 
			throws IOException 
			{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
			}
}
