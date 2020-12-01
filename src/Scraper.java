import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Scraper {

    //Fields
    private ArrayList<Movie> movieList;


    //Default constructor
    public Scraper(){
        movieList = new ArrayList<>();

    }

    //Public method to set movie list by scraping or reading from text file
    public void setMovieList(ArrayList<Movie> movies){
        this.movieList = movies;
    }

    //Web scraper to get movies from IMDB
    public ArrayList<Movie> scrapeMovies(){
        ArrayList<Movie> list = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.imdb.com/search/keyword/?keywords=marvel-cinematic-universe&ref_=kw_ref_typ&sort=moviemeter,asc&mode=detail&page=1&title_type=movie").timeout(6000).get();
//            System.out.println(doc.outerHtml());
            Elements body = doc.select("div.lister-item.mode-detail");
//            System.out.println(body.size());
            String title;
            String released;
            for (Element e : body.select("h3.lister-item-header")){
                title = e.select("a").text();

                released = e.select("span").text().replaceAll("[^0-9]", "");
                if (released.length() > 4){
                    released = released.substring(released.length()-4);
                }else {
                    released = "Unknown";
                }

                list.add(new Movie(title, released, -1));


            }

        }catch (IOException e){
            System.out.println("Couldn't populate movieList: ");
            e.printStackTrace();
        }
        return list;
    }

    //Return movie list
    public ArrayList<Movie> getMovies(){
        return movieList;
    }

//    public void printMovies(ArrayList<Movie> movies){
//        System.out.println("Full Movie List:");
//        for (Movie e : movies){
//            System.out.println(e.getTitle() + ": " + e.getReleased());
//        }
//
//    }

    //Write movie list to a text file
    public boolean writeMovies(ArrayList<Movie> movies){
        try (FileWriter locFile = new FileWriter("movies.txt")) {
            for (Movie e : movies){
                locFile.write(e.getTitle() + "," + e.getReleased() + "," + e.getRating() + "\n");
            }
            return true;
        }catch (IOException e){
            System.out.println("Failed to write movies");
            e.printStackTrace();
            return false;
        }

    }

    //Read movies from a text file
    public ArrayList<Movie> readMovies(){
        ArrayList<Movie> result = new ArrayList<>();
        try (BufferedReader dirFile = new BufferedReader(new FileReader("movies.txt"))){
            String input;
            while ((input = dirFile.readLine()) != null){
                String[] data = input.split(",");
                String title = data[0];
                String released = data[1];
                int rating = Integer.parseInt(data[2]);
                result.add(new Movie(title, released, rating));

            }

        }catch (IOException e){
            System.out.println("Failed to read movies");
            e.printStackTrace();
        }
        return result;
    }

    public boolean saveMovies(JTable table){
        ArrayList<Movie> movies = new ArrayList<>();
        int rating =-1;
        for (int i = 0; i < table.getRowCount(); i++){
            String title = (String) table.getValueAt(i, 0);
            String released = (String) table.getValueAt(i, 1);
            try{
                String ratingText = (String) table.getValueAt(i, 2);
                if (ratingText.equals("Not Rated")) rating = -1;
            }catch (ClassCastException e){
                rating = (int) table.getValueAt(i, 2);
            }

            movies.add(new Movie(title, released, rating));
        }
        return (writeMovies(movies));

    }



}
