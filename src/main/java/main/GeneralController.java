package main; 


import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.aspectj.weaver.NameMangler;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;    
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bean.Comment;
import bean.Movie;
import bean.Schedule;
import bean.Seat;
import bean.User;
import dao.CommentDao;
import dao.MovieDao;
import dao.ScheduleDao;
import dao.SeatDao;
import dao.UserDao;
import net.sf.json.JSONObject;    
    
@Controller
public class GeneralController {

    @RequestMapping(value={"/index", "/"})
    public String index_jsp(Model model,  HttpSession session){
        model.addAttribute("name", session.getAttribute("username"));
        return "index";
    }
    //��¼
    @RequestMapping(value = "/login", method = RequestMethod.GET)    
    public String login_jsp(Model model){
    	return "login";
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)    
    public String login(User user,  HttpSession session) {
    	//session.setAttribute("phoneNumber", user.getPhoneNumber());
    	//ajax�첽������ȷ�����ݿ��Ƿ����ظ��û���TODO
    	
    	session.setAttribute("username", user.getUsername());
    	return "redirect:index";
    }
    
    //ע��
    @RequestMapping(value = "/regist", method = RequestMethod.GET)
    public String regist_jsp(Model model){
    	return "regist";
    }
    @RequestMapping(value="/regist", method = RequestMethod.POST)
    public String regist(User user, Model model, HttpSession session) {
    	//û�����ظ����ֵĴ���
    	System.out.println(user.getUsername() + "-" + user.getPassword() + "-" + user.getPhoneNumber());
    	UserDao userDao = new UserDao();
        userDao.addUser(user);
        session.setAttribute("username", user.getUsername());
    	return "redirect:index";
    }
    
    //�û�����
    @RequestMapping(value="/info")
    public String info_jsp(Model model,  HttpSession session) {
        model.addAttribute("name", session.getAttribute("username"));
        return "info";
    }
    @RequestMapping(value="/info", method = RequestMethod.POST)
    public String info_(HttpServletRequest request,  HttpSession session, Model model) {
    	System.out.println(request.getParameter("name"));
    	UserDao userDao = new UserDao();
        userDao.UpdateUserName(request.getParameter("name"), (String)session.getAttribute("username"));
        session.setAttribute("username", request.getParameter("name"));
        
        model.addAttribute("name", session.getAttribute("username"));
        return "redirect:info";
    }

    //��Ӱ����
    @RequestMapping(value = "/movie/{id}", method = RequestMethod.GET)
    public String movie_jsp(@PathVariable int id, Model model, HttpSession session) {
    	MovieDao movieDao = new MovieDao();
    	Movie movie = movieDao.GetMovieFromID(id);
    	//������ʾ
    	CommentDao commentDao = new CommentDao();
    	List<Comment> comments =  commentDao.getAllComments(id);
    	model.addAttribute("comments", comments);
    	model.addAttribute("movie", movie);
    	model.addAttribute("name", session.getAttribute("username"));
    	return "movie";
    }
    //��Ӱ����ҳ����������
    @ResponseBody
    @RequestMapping(value = "/movie/{id}", method = RequestMethod.POST)    
    public Object movie_comment(@PathVariable int id, Model model, @RequestParam("comment") String comment, @RequestParam("deleteId") String commentIdToDelete, HttpSession session) {
    	CommentDao commentDao = new CommentDao();
    	//����
			System.out.println(comment);
			System.out.println(commentIdToDelete);
		//Ҫɾ��ʱ
		JSONObject jsonObject = new JSONObject();
    	if (commentIdToDelete != "") {
    		commentDao.DeleteComment(Integer.parseInt(commentIdToDelete));
    		jsonObject.put("isDelete", "YES");
    		jsonObject.put("commentId", commentIdToDelete);
    		jsonObject.put("username", "");
    		jsonObject.put("mid", "");
    		jsonObject.put("comment", comment);
    		return jsonObject;
    	} else if (comment != "") {//Ҫ���ʱ
    		jsonObject.put("isDelete", "NO");
    		jsonObject.put("commentId", "");
    		Comment _comment = new Comment();
        	if (session.getAttribute("username") == null) {
        		_comment.setUserName("·��");
        		jsonObject.put("username", "·��");
        	} else {
        		_comment.setUserName((String)session.getAttribute("username"));
        		jsonObject.put("username", (String)session.getAttribute("username"));
        	}
        	_comment.setmId(id);
        	jsonObject.put("mid", id);
        	_comment.setCommentText(comment);
        	jsonObject.put("comment", comment);
        	commentDao.AddComment(_comment);
    		return jsonObject;
    	} else {
    		return null;
    	}
    }

    //���е�Ӱ
    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public String movies_jsp(Model model, HttpSession session) {
    	MovieDao movieDao = new MovieDao();
    	List<Movie> movies = movieDao.GetMovies();
    	model.addAttribute("movies", movies);
    	model.addAttribute("name", session.getAttribute("username"));
    	return "movies";
    }
    //��Ʊ
    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public String book_jsp(@PathVariable int id, Model model, HttpSession session) {
    	ScheduleDao scheduleDao = new ScheduleDao();
    	List<Schedule> schedules = scheduleDao.GetschedulesByMid(id);
    	model.addAttribute("schedules", schedules);
    	model.addAttribute("id", id);
    	model.addAttribute("name", session.getAttribute("username"));
    	session.setAttribute("movieId", id);
    	return "book";
    }
    
    //ѡ��
    @RequestMapping(value = "/choose_seat/{sId}", method = RequestMethod.GET)
    public String choose_seat_jsp(@PathVariable int sId, Model model, HttpSession session) {
    	SeatDao seatDao = new SeatDao();
    	List<Seat> seats = seatDao.GetUsedSeatsBySid(sId);
    	model.addAttribute("seats", seats);
    	model.addAttribute("sId", sId);
    	model.addAttribute("movieId", session.getAttribute("movieId"));
    	model.addAttribute("name", session.getAttribute("username"));
    	return "choose_seat";
    }
    
    //ѡ��
    @RequestMapping(value = "/choose_seat/{sId}", method = RequestMethod.POST)
    public String choose_seat(@PathVariable int sId, Model model, HttpSession session,  @RequestParam("seatRow") int seatRow, @RequestParam("seatcolumn") int seatColumn) {
    	SeatDao seatDao = new SeatDao();
    	Seat seat = new Seat();
    	seat.setsId(sId);
    	seat.setSeatRow(seatRow);
    	seat.setSeatColumn(seatColumn);
    	int seatId = seatDao.addUsedSeat(seat);//�����ݿ����һ����λ��ѡ�˵���Ϣ
    	model.addAttribute("seatId", seatId);
    	model.addAttribute("name", session.getAttribute("username"));
    	return "redirect:../order";
    }
    
  //����
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String choose_seat(Model model, HttpSession session) {
    	return "order";
    }
}