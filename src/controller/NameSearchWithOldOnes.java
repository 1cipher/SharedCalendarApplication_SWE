package controller;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import utils.EditDistance;

public class NameSearchWithOldOnes implements SearchStrategy {


    @Override
    public ItemList search(ItemList list, String toSearch) {

        int min = 2;
        Item result = null;
        for (Item element:list) {
            String comp = element.getHeaderText();
            int set = EditDistance.computeLevenshteinDistance(comp,toSearch);
            if (set>min) {
                list.remove(element);
            }
        }

        return list;
    }

    @Override
    public String textToDisplay() {
        return "Name";
    }
}
