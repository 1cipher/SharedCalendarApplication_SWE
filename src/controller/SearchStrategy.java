package controller;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;

public interface SearchStrategy {

    Item search(ItemList list, String toSearch);

    String textToDisplay();

}
