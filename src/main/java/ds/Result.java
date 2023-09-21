//Name: Dhruvi Ajay Khankhoje, andrewid: dkhankho, email: dkhankho@andrew.cmu.edu, Project 4 Task 2

package ds;

//this is the result class which has all the results store in mongoDB and sent to user
class Result {


    public String authorName; //Name of author
    public String authorBio; // Biography of author

    public String authorLink; //Link of author

    public String authorQuote; //Quote by the author


    //constructor of Result class
    public Result(String authorName, String authorBio, String authorLink, String authorQuote)
    {
        this.authorName = authorName;
        this.authorBio = authorBio;
        this.authorLink = authorLink;
        this.authorQuote = authorQuote;
    }


}
