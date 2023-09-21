//Name: Dhruvi Ajay Khankhoje, andrewid: dkhankho, email: dkhankho@andrew.cmu.edu, Project 4 Task 2


package ds;
/*
 * @author Dhruvi Ajay Khankhoje
 *

 *
 * The servlet is acting as the controller.
 * There are three views - prompt.jsp,  result.jsp and dashboard.jsp.
 *
 * The model is provided by InterestingQuoteModel.
 */

import java.io.IOException;
import java.util.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


//the servlet has two paths /getAnInterestingQuote and /getDashboard

//Citation: My project1task3
@WebServlet(name = "InterestingQuoteServlet",
        urlPatterns = {"/getAnInterestingQuote", "/getDashboard"})
public class InterestingQuoteServlet extends HttpServlet {

    InterestingQuoteModel ipm = null;  // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        ipm = new InterestingQuoteModel();
    }

    // This servlet will reply to HTTP GET requests via this doGet method
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // get the search parameter if it exists
        String search = request.getParameter("searchWord");

        //which url it has to go too
        String urlendpoint = request.getServletPath();


        // determine what type of device our user is
        String ua = request.getHeader("User-Agent");


        String nextView;
        /*
         * Check if the search parameter is present.
         * If not, then display landing page
         * If there is a search parameter, and the urlendpoint is /getAnInterestingQuote  then do the search and return the result.
         */
        if (search != null && urlendpoint.equals("/getAnInterestingQuote")) {

            // use model to do the search and choose the result view
            String quoteInfo = ipm.printJsonData(search);


            /*
             * Attributes on the request object can be used to pass data to
             * the view.  These attributes are name/value pairs, where the name
             * is a String object.  Here the quoteInfo is passed to the view
             */

            request.setAttribute("quoteInfo",quoteInfo);

            nextView = "result.jsp";

            //if url is /getDashboard"
        } else if (urlendpoint.equals("/getDashboard")) {


            //get the records of logs from the model
            List<LogDashboard> logsInfo = ipm.logRecords();
            List<Integer> length = new ArrayList<>();

            TreeMap<String, Integer> authorCount = new TreeMap<>();

            int totalQuotes = 0;
            for (LogDashboard logDashboard : logsInfo) {
                length.add(Integer.parseInt(logDashboard.getLength()));
                if(authorCount.containsKey(logDashboard.getUserInput()))
                {
                    int count = authorCount.get(logDashboard.getUserInput());
                    count = count + 1;
                    authorCount.put(logDashboard.getUserInput(),count);
                }
                else {
                   authorCount.put(logDashboard.getUserInput(),1);
                }


            }
            int maxSearch = 0;
            String maxString = "";
//          Citation : https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map

            //Creating a map of author and it's number of quotes
            for (Map.Entry<String, Integer> searchVal : authorCount.entrySet()) {
                if (searchVal.getValue() > maxSearch) {
                    maxSearch = searchVal.getValue();
                    maxString = searchVal.getKey();
                }
            }
            totalQuotes = logsInfo.size();
            //Citation: https://stackoverflow.com/questions/8304767/how-to-get-maximum-value-from-the-collection-for-example-arraylist
            String max = String.valueOf(Collections.max(length));

            //setting attributes
            request.setAttribute("logsInfo",logsInfo);
            request.setAttribute("totalQuotes",totalQuotes);
            request.setAttribute("max",max);
            request.setAttribute("maxSearch",maxSearch);
            request.setAttribute("maxString",maxString);

            //choosing a view
            nextView = "dashboard.jsp";
        }
        else {
            // no search parameter so choose the prompt view
            nextView = "prompt.jsp";
        }
        // Transfer control over the the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }
}

