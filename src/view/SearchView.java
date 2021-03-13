package view;

import controller.*;
import utils.SearchRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SearchView extends JFrame{

    private JLabel searchLabel;
    private JButton search;
    private JTextField searchBox;
    private JComboBox<SearchStrategy> searchType;

    public SearchView() {

        setName("Calendar");
        setSize(300, 200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(null);

        searchLabel = new JLabel("Search with filters:");
        searchLabel.setLocation(30,10);
        searchLabel.setSize(150,20);

        searchBox = new JTextField();
        searchBox.setLocation(150, 10);
        searchBox.setSize(200, 20);

        searchType = new JComboBox<>();
        searchType.addItem(new NameSearch());
        searchType.addItem(new LocationSearch());
        searchType.addItem(new NameSearchWithOldOnes());
        searchType.addItem(new LocationStrategyWithOldOnes());
        searchType.setLocation(360,10);
        searchType.setSize(150,20);
        searchType.setRenderer(new SearchRenderer());
        search = new JButton();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/utils/search.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert img != null;
        Image newimg = img.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH );

        search.setIcon(new ImageIcon(newimg));
        search.setLocation(520, 10);
        search.setSize(20, 20);

        c.add(searchBox);
        c.add(searchLabel);
        c.add(searchType);
        c.add(search);
    }

    public SearchStrategy getSearchType() {

        return (SearchStrategy) searchType.getSelectedItem();
    }

    public void addSearchListener(ActionListener searchListener){

        this.search.addActionListener(searchListener);
    }

    public String getSearchText(){
        return searchBox.getText();
    }

    public void close(){
        this.setVisible(false);
        this.dispose();
    }

}
