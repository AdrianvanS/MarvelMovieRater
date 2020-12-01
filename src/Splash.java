import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Splash extends JFrame implements ActionListener {

    JLabel label1;
    JLabel label2;
    JLayeredPane layeredPane;
    JFrame frame;
    Timer timer;
    JTable table;
    JButton rateButton;
    JButton updateButton;
    JButton saveButton;
    JButton exitButton;
    Scraper scraper;
    ArrayList<Movie> movies;
    DefaultTableModel model;

    //---------Constructor Start---------------------
    Splash(){

        scraper = new Scraper();

        //---------JLabels Start---------------------

        label1 = new JLabel();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("avsk.png").getImage().getScaledInstance(465, 400, Image.SCALE_DEFAULT));
        label1.setIcon(imageIcon);
        label1.setOpaque(true);
        label1.setBackground(Color.black);
        label1.setBounds(10, -20, 500, 500);

        label2 = new JLabel();
        label2.setOpaque(true);
        label2.setText("Marvel Movie Rater");
        label2.setForeground(Color.BLACK);
        label2.setBackground(Color.white);
        label2.setFont(new Font("LEMON MILK", Font.BOLD, 20));
        label2.setBounds(150, 292, 200, 50);

        //---------JLabels End---------------------
        //---------JLayeredPane Start---------------------

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 500, 500); //Same as JFrame
        layeredPane.add(label1, Integer.valueOf(0)); //Use Integer instead of actual layer name
        layeredPane.add(label2, Integer.valueOf(1));

        //---------JLayeredPane End---------------------

        frame = new JFrame("JLayeredPane");
        frame.add(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(null); //Layout manager set to null
        frame.setResizable(false);
        frame.setTitle("Welcome");
        frame.setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon("avsk.png"); //creates an image icon
        frame.setIconImage(image.getImage()); //change icon of this

        frame.getContentPane().setBackground(new Color(0,0,0));

        frame.setVisible(true);

        timer = new Timer(3000, this);
        timer.start();
    }

    //---------Constructor End---------------------

    @Override
    public void actionPerformed(ActionEvent e) {
//        Table table = new Table();
//        System.exit(0);
        displayTable();
        timer.stop();



    }

    public void displayTable(){

        scraper.setMovieList(scraper.readMovies());
        movies = scraper.getMovies();

        //--------------Frame---------------------
        //Remove old labels from splash screen
        label1.setVisible(false);
        label2.setVisible(false);

        //Set up new layout for table
        frame.setLayout(new FlowLayout());
        frame.setSize(600, 600);
        frame.getContentPane().setBackground(new Color(255,255,255));
        frame.setTitle("Marvel Movie Rater");
        //------------------------------------------

        //--------------Buttons---------------------

        rateButton = new JButton("Rate");
        rateButton.setFocusable(false);
        rateButton.setPreferredSize(new Dimension(100, 30));
        rateButton.setHorizontalTextPosition(JButton.CENTER);
        rateButton.setVerticalTextPosition(JButton.BOTTOM);
        rateButton.setFont(new Font("Comic Sans", Font.BOLD, 15));
        rateButton.setForeground(Color.white);
        rateButton.setBackground(new Color(84, 55, 148));
        rateButton.setVisible(true);
        rateButton.addActionListener(e -> {
            try{
                int selected = table.getSelectedRow();
                editRating(selected);
            }catch (Exception f){
                JOptionPane.showMessageDialog(null, "Nothing selected!", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        });

        updateButton = new JButton("Update Movies");
        updateButton.setFocusable(false);
        updateButton.setHorizontalTextPosition(JButton.CENTER);
        updateButton.setVerticalTextPosition(JButton.BOTTOM);
        updateButton.setFont(new Font("Comic Sans", Font.BOLD, 15));
        updateButton.setPreferredSize(new Dimension(140, 30));
        updateButton.setForeground(Color.white);
        updateButton.setBackground(new Color(84, 55, 148));
        updateButton.addActionListener(e ->{
            int result = updateTable();
            if (result == 0){
                JOptionPane.showMessageDialog(null, "No new movies were found!", "No Updates", JOptionPane.WARNING_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(null, result + " movies were added.", "Update", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        saveButton = new JButton("Save");
        saveButton.setFocusable(false);
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.setHorizontalTextPosition(JButton.CENTER);
        saveButton.setVerticalTextPosition(JButton.BOTTOM);
        saveButton.setFont(new Font("Comic Sans", Font.BOLD, 15));
        saveButton.setForeground(Color.white);
        saveButton.setBackground(new Color(84, 55, 148));
        saveButton.addActionListener(e -> {
            boolean result = scraper.saveMovies(this.table);
            if (result == true){
                JOptionPane.showMessageDialog(null, "Movies saved successfully", "Update", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Movies failed to save!", "Warning", JOptionPane.WARNING_MESSAGE);
            }



        });

        exitButton = new JButton("Exit");
        exitButton.setFocusable(false);
        exitButton.setPreferredSize(new Dimension(100, 30));
        exitButton.setHorizontalTextPosition(JButton.CENTER);
        exitButton.setVerticalTextPosition(JButton.BOTTOM);
        exitButton.setFont(new Font("Comic Sans", Font.BOLD, 15));
        exitButton.setForeground(Color.white);
        exitButton.setBackground(new Color(84, 55, 148));
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "Would you like to save before exiting?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (answer){
                    case 0:
                        boolean result = scraper.saveMovies(table);
                        if (result) {
                            JOptionPane.showMessageDialog(null, "Movies saved successfully", "Update", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        } else {
                            JOptionPane.showMessageDialog(null, "Movies failed to save!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        break;

                    case 1:
                        System.exit(0);
                        break;

                    case 2:
                        break;

                }
            }
        });


        //------------------------------------------

        /*
        Retrieve data from the text file and populate ArrayList
        Populate table from ArrayList
         */


        String[] columnNames = {"Title", "Released", "Rating"};
        model = new DefaultTableModel(movies.size(), columnNames.length);
        model.setColumnIdentifiers(columnNames);

        table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        for (int i = 0; i < movies.size(); i++){
            table.getModel().setValueAt(movies.get(i).getTitle(), i, 0);
            table.getModel().setValueAt(movies.get(i).getReleased(), i, 1);
            if (movies.get(i).getRating() < 0){
                table.getModel().setValueAt("Not Rated", i, 2);
            }else{
                table.getModel().setValueAt(movies.get(i).getRating(), i, 2);
            }
        }


        table.setPreferredScrollableViewportSize(new Dimension(500, 500));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setSelectionModel(new ForcedListSelectionModel());
        frame.add(rateButton);
        frame.add(updateButton);
        frame.add(saveButton);
        frame.add(exitButton);
        frame.add(scrollPane);
    }

    //Force only row to be selected at a time
    public class ForcedListSelectionModel extends DefaultListSelectionModel {

        public ForcedListSelectionModel () {
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        @Override
        public void clearSelection() {
        }

        @Override
        public void removeSelectionInterval(int index0, int index1) {
        }

    }

    private int updateTable(){
        ArrayList<Movie> newScrape = scraper.scrapeMovies();

        int count = 0;
        for (Movie m : newScrape){
            if (findMovie(m) == -1){
                model.addRow(new Object[]{m.getTitle(), m.getReleased(), "Not Rated"});
                count++;
            }
        }
        setMovies(updateMovieList());
        return count;

        //Scrape Movies and store in an arraylist
        //Compare new arraylist to old arraylist
        //If movies are already present, notify that no new movies
        //If movie is not present, add to table.
    }

    private int findMovie(Movie movie){
        String title = movie.getTitle();
        int row = -1;
        for (int i = 0; i < movies.size(); i++){
            if (movies.get(i).getTitle().equals(title)) row = i;
        }
        return row;
    }

    private void editRating(int k){
        String title = (String) table.getValueAt(k, 0);
        int result;

        boolean quit = false;
        while (!quit){
            try{
                String entry = JOptionPane.showInputDialog("Enter a number between 0 & 10 for " + title);
                if (entry.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You must enter a number", "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    try {
                        result = Integer.parseInt(entry);
                        if ((result > -1) && (result < 11)) {
                            table.getModel().setValueAt(result, k, 2);
                            quit = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Not a valid number", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Not a valid number", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

            }catch (NullPointerException e){
                quit = true;
            }
        }


    }


    //Updates ArrayList of movies based on the table
    private ArrayList<Movie> updateMovieList(){
        ArrayList<Movie> newMovieList = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++){
            String title = (String) table.getValueAt(0, 0);
            String released = (String) table.getValueAt(0, 1);
            int rating = -1;
            try{
                String ratingText = (String) table.getValueAt(i, 2);
                if (ratingText.equals("Not Rated")) rating = -1;
            }catch (ClassCastException e){
                rating = (int) table.getValueAt(i, 2);
            }

            newMovieList.add(new Movie(title, released, rating));
        }



        return newMovieList;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }


}
