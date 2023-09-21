//Name: Dhruvi Ajay Khankhoje, andrewid: dkhankho, email: dkhankho@andrew.cmu.edu, Project 4 Task 2


package ds;


//this is the LogDashboard class which has all the logs that are stored in mongoDB and used for calculating operation analytics
public class LogDashboard {


    public String User_Input; //input of the user
    public String Input_Time; //time when search began

    public String Output_Time; //time when output was sent

    public String NumberofQuotes = "0"; //number of quotes per author

    public String QuoteSent; //output to the user

    public String length; //length of the quote


    //construtor of logDashboard class
    public LogDashboard(String User_Input, String Input_Time, String Output_Time, String NumberofQuotes, String QuoteSent, String length)
    {
        this.User_Input = User_Input;
        this.NumberofQuotes = NumberofQuotes;
        this.Input_Time = Input_Time;
        this.Output_Time = Output_Time;
        this.QuoteSent = QuoteSent;
        this.length = length;
    }


    //getters for the class
    public String getUserInput() {
        return User_Input;
    }

    public String getInputTime() {
        return Input_Time;
    }

    public String getOutputTime() {
        return Output_Time;
    }

    public String getNumberofQuotes() {
        return NumberofQuotes;
    }
    public String getLength() {
        return length;
    }

    public String getQuoteSent() {
        return QuoteSent;
    }
}
