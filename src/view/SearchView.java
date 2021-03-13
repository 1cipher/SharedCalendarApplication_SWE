package view;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import controller.*;
import utils.EventCustomRenderer;
import utils.SearchRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SearchView extends JFrame{

    private JLabel searchLabel;
    private JButton search;
    private JTextField searchBox;
    private JComboBox<SearchStrategy> searchType;
    private DefaultListModel<Item> defaultListModel;
    private JList list;
    private JScrollPane scrollPane;

    public SearchView() {

        setName("Calendar");
        setSize(600, 400);
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

        defaultListModel = new DefaultListModel();
        list = new JList(defaultListModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setCellRenderer(new EventCustomRenderer());
        scrollPane = new JScrollPane(list);
        scrollPane.setSize(200,300);
        scrollPane.setLocation(100,100);

        c.add(searchBox);
        c.add(searchLabel);
        c.add(searchType);
        c.add(search);
        c.add(scrollPane);
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

    public void addResults(ItemList result){

        for (Item item:
             result) {
            defaultListModel.add(0,item);
        }


    }

    public void addSelectedEventListener(ListSelectionListener listener){

        this.list.addListSelectionListener(listener);
    }

    public JList getList() {
        return list;
    }
}
