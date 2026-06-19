package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.TopicDAO;


@WebServlet("/editTopic")
public class EditTopicServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {



        int id =
        Integer.parseInt(
            request.getParameter("id")
        );


        int subjectId =
        Integer.parseInt(
            request.getParameter("subjectId")
        );


        int roadmapId =
        Integer.parseInt(
            request.getParameter("roadmapId")
        );


        String name =
        request.getParameter("name");



        TopicDAO dao =
        new TopicDAO();


        dao.updateName(
            id,
            name
        );



        response.sendRedirect(
            "topics.jsp?subjectId="
            + subjectId
            + "&roadmapId="
            + roadmapId
        );


    }


}