package com.prac.spring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VoteWeb {
		
	@RequestMapping("/")
	public String HomeGet() {
		return "/index.html";
	}
	
	@RequestMapping("/dist")
	public void VoteDist(HttpServletResponse response) throws IOException {
		// url
		String url = "http://localhost:8080/vote/";
		String code = "";
		
		for(int i=0; i<5; i++) {
			// 소문자
			int num = (int)(Math.random() * 26) + 97;
			
			// 대문자
			Random random = new Random();
			boolean bool = random.nextBoolean();
			if (bool == true) {
				num -= 32;
			}
			
			code += (char)num;
		}
		url += code;
		
		// html
		String html = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"ko\">\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>투표 사이트</title>\r\n"
				+ "</head>\r\n"
				+ "<style>\r\n"
				+ "	body {\r\n"
				+ "		display: flex;\r\n"
				+ "		justify-content: center;\r\n"
				+ "		align-items: center;\r\n"
				+ "	}\r\n"
				+ "	body > div{\r\n"
				+ "		width: 300px;\r\n"
				+ "		height: 500px;\r\n"
				+ "		border: 1px solid black;\r\n"
				+ "	}\r\n"
				+ "</style>\r\n"
				+ "<body>\r\n"
				+ "<div>\r\n"
				+ "	<h2>투표 공유하기</h2>\r\n"
				+ "	<div>링크</div>\r\n"
				+ "	<div id=\"url\">"
				+ url
				+ " </div>\r\n"
				+ "	<div>\r\n"
				+ "		<a href=\"/\"><button>취소</button></a>\r\n"
				+ "		<a href=\"/vote/" + code + "\"><button>이동</button></a>\r\n"
				+ "	</div>\r\n"
				+ "</div>\r\n"
				+ "</body>\r\n"
				+ "</html>";
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(html);
		
		// save to file
		File path = new File("./src/main/resources");
		FileWriter fw = null;
		
		if (!path.exists()) {
			path.mkdirs();
			System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources 생성");
		}
		
		try {
			fw = new FileWriter(path + "\\data.txt", true);
			fw.write(url + "\n");
			fw.close();
		} catch (IOException e) {
			System.out.println("[url 데이터 저장 오류]");
			System.out.println(e.getMessage());
		}
		
	}
	
	@RequestMapping("/vote/*")
	public String VoteGet(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		File path = new File("./src/main/resources");
		BufferedReader buf = null;
		boolean check = false;
		
		try {
			buf = new BufferedReader(new FileReader(path + "\\data.txt"));
			check = false;
			String line = null;
			
			while((line = buf.readLine()) != null) {
				if (!url.equals(line)) continue;
				else {
					buf.close();
					check = true;
					break;
				}
			}
			if (check == false) {
				System.out.println("[없는 데이터] data.txt 에 " + url + "이 존재하지 않음");
				return "/error.html";
			}
			
			buf.close();
		} catch(IOException e) {
			System.out.println("[없는 파일] data.txt 라는 파일이 존재하지 않음");
			return "redirect:/";
		}
		
		return "/vote.html";
	}
	
	@RequestMapping({"/add_op", "/add_op/*"})
	public void AddOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getRequestURL().toString();
		char delNum = url.charAt(url.length() - 1);
		int delOpNum = -1;
		if(Character.isDigit(delNum)) {
			delOpNum = Character.getNumericValue(delNum); // 맨 뒤의 문자 하나만 가져오므로 2자리 숫자 이상은 코드 변경해야 됨
		}
		
		VoteDTO data = new VoteDTO();
		String[] op = new String[10];
		String html;
		
		data.setTitle(request.getParameter("title"));
		int n = 0;
		while(true) {
			if(n == delOpNum) {
				n++;
				continue;
			}
			String temp = request.getParameter("op" + n);
			if(temp == null) break;
			op[n] = temp;
			n++;
		}
		data.setOp(op);
		
		response.setContentType("text/html;charset=utf-8");
		html = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"ko\">\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>투표 사이트</title>\r\n"
				+ "</head>\r\n"
				+ "<style>\r\n"
				+ "header {\r\n"
				+ "	height: 30px;\r\n"
				+ "	border: 1px solid black;\r\n"
				+ "}\r\n"
				+ "header > a {\r\n"
				+ "	float: right;\r\n"
				+ "}\r\n"
				+ "header > nav {\r\n"
				+ "	text-align: center;\r\n"
				+ "}"
				+ "\r\n"
				+ "section {\r\n"
				+ "	width: 70%;\r\n"
				+ "	margin: 0 auto;\r\n"
				+ "}\r\n"
				+ "article {\r\n"
				+ "	border: 1px solid black;\r\n"
				+ "}\r\n"
				+ "section > article {\r\n"
				+ "	width: 100%;\r\n"
				+ "}\r\n"
				+ "body > article {\r\n"
				+ "	position: absolute;\r\n"
				+ "	top: 20%;\r\n"
				+ "	right: 5%;\r\n"
				+ "}\r\n"
				+ "div > label {\r\n"
				+ "	display: block;\r\n"
				+ "	color: black;\r\n"
				+ "}\r\n"
				+ "div > label:last-child {\r\n"
				+ "	margin-bottom: 20px;\r\n"
				+ "}\r\n"
				+ "</style>\r\n"
				+ "<body>\r\n"
				+ "<!-- 헤더 -->\r\n"
				+ "<header>\r\n"
				+ " <nav><a href='/'>GO TO HOME</a></nav>\r\n"
				+ "	<a href=\"/dist\"><button>보내기</button></a>\r\n"
				+ "</header>\r\n"
				+ "\r\n"
				+ "<!-- 투표 내용 생성 -->\r\n"
				+ "<section>\r\n"
				+ "	<article>\r\n"
				+ "		<form action=\"add_op\">\r\n"
				+ "			<input type=\"hidden\" name=\"voteNum\" value=\"0\" >\r\n"
				+ "			<input type=\"text\" name=\"title\" value='"+ data.getTitle() +"' placeholder='제목을 입력해주세요'>\r\n"
				+ "			<div>\r\n";
		int c = 0;
		while(c < n) {
			if(c == delOpNum) {
				n++;
				continue;
			}
			html = html
//					+ "			<label><input type=\"text\" name='op" + c + "' value='" + data.getOp()[c] + "' placeholder='옵션" + (c + 1) + "'><a href='del_op/" + c + "'>X</a></label>\r\n";;
					+ "			<label><input type=\"text\" name='op" + c + "' value='" + data.getOp()[c] + "' placeholder='옵션" + (c + 1) + "'></label>\r\n";
			c++;
		}
		html = html
//				+ "			<label><input type=\"text\" name='op" + c + "' placeholder='옵션" + (c + 1) + "'><a href='del_op'>X</a></label>\r\n";
				+ "			<label><input type=\"text\" name='op" + c + "' placeholder='옵션" + (c + 1) + "'></label>\r\n";
		if(n <= 8) {
			html = html
					+ "			<input type=\"submit\" value=\"옵션추가\">\r\n";
		}
		html = html
				+ "			</div>\r\n"
				+ "		</form>\r\n"
				+ "		<form action=\"del_vote\">\r\n"
				+ "			<input type=\"hidden\" name=\"delVoteNum\" value=\"0\" >\r\n"
				+ "			<input type=\"submit\" value=\"투표지 삭제\">\r\n"
				+ "		</form>"
				+ "	</article>\r\n"
				+ "</section>\r\n"
				+ "\r\n"
				+ "<!-- 투표지 추가 -->\r\n"
				+ "<article>\r\n"
				+ "		<form action=\"add_vote\">\r\n"
				+ "			<input type=\"hidden\" name=\"addVoteNum\" value=\"1\" >\r\n"
				+ "			<button>투표지 추가</button>\r\n"
				+ "		</form>"
				+ "</article>\r\n"
				+ "</body>\r\n"
				+ "</html>";
		response.getWriter().print(html);
	}
	
//	@RequestMapping("/del_op/*")
//	public String DelOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String url = request.getRequestURL().toString();
//		int delOpNum = Character.getNumericValue(url.charAt(url.length() - 1)); // 맨 뒤의 문자 하나만 가져오므로 2자리 숫자 이상은 코드 변경해야 됨
//		
//		return "redirect:/add_op/" + delOpNum;
		// url에 쿼리 스트링을 붙여서 보내는 게 아니면 화면이 초기화되버림..
//	}
	
	@RequestMapping("/del_vote")
	public void DelVote(HttpServletRequest request) {
		System.out.println(request.getParameter("title"));
	}
	
}
