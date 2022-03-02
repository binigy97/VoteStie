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
		// encoding
		response.setCharacterEncoding("UTF-8");
		
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
	
}
