package horton.kyle.prolificlibrary;

/**
 * Created by Kyleh on 5/20/2018.
 */

public class BookDetails {

    private String author;
    private String categories;
    private String lastCheckedOut;
    private String lastCheckedOutBy;
    private String publisher;
    private String title;
    private int bookId;

    public BookDetails(String author, String categories, String lastCheckedOut, String lastCheckedOutBy, String publisher,
                       String title){

        this.author = author;
        this.categories = categories;
        this.lastCheckedOut = lastCheckedOut;
        this.lastCheckedOutBy = lastCheckedOutBy;
        this.publisher = publisher;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategories() {
        return categories;
    }

    public String getLastCheckedOut() {
        return lastCheckedOut;
    }

    public String getLastCheckedOutBy() {
        return lastCheckedOutBy;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getBookTitle() {
        return title;
    }

    public int getBookId(){
        return bookId;
    }



}