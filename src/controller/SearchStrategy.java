package controller;

import com.mindfusion.scheduling.model.ItemList;

public interface SearchStrategy {

    ItemList search(ItemList list, String toSearch);

    String textToDisplay();

}
