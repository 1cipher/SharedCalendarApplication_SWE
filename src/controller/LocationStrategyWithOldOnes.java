package controller;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import utils.EditDistance;

public class LocationStrategyWithOldOnes implements SearchStrategy {
    @Override
    public Item search(ItemList list, String toSearch) {

        int min = 2;
        Item result = null;
        for (Item element:list) {
            String comp = element.getLocation().toString();
            int set = EditDistance.computeLevenshteinDistance(comp,toSearch);
            if (set<min) {
                min = set;
                result = element;
            }
        }

        return result;
    }

    @Override
    public String textToDisplay() {
        return "Location";
    }
}
