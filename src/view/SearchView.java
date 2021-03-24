package view;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import controller.*;
import utils.EventCustomRenderer;
import utils.SearchCustomRenderer;

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

        setTitle("Search");
        setSize(600, 400);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(null);

        searchLabel = new JLabel("Search with filters:");
        searchLabel.setLocation(30,20);
        searchLabel.setSize(150,40);

        searchBox = new JTextField();
        searchBox.setLocation(150, 20);
        searchBox.setSize(200, 40);

        searchType = new JComboBox<>();
        searchType.addItem(new FutureNameSearch());
        searchType.addItem(new FutureLocationSearch());
        searchType.addItem(new NameSearch());
        searchType.addItem(new LocationSearch());
        searchType.setLocation(360,20);
        searchType.setSize(150,40);
        searchType.setRenderer(new SearchCustomRenderer());
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
        search.setLocation(520, 20);
        search.setSize(40, 40);

        defaultListModel = new DefaultListModel();
        list = new JList(defaultListModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setCellRenderer(new EventCustomRenderer());
        scrollPane = new JScrollPane(list);
        scrollPane.setSize(400,200);
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

        defaultListModel.clear();
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
