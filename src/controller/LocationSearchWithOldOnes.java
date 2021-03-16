package controller;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import utils.EditDistance;

public class LocationSearchWithOldOnes implements SearchStrategy {
    @Override
    public ItemList search(ItemList list, String toSearch) {

        int min = 2;
        ItemList newList = new ItemList();
        for (Item element:list) {
            String comp = element.getLocation().getName();
            int set = EditDistance.calculate(comp,toSearch);
            if (set<=min) {
                newList.add(element);
            }
        }


        return newList;
    }

    @Override
    public String textToDisplay() {
        return "Location";
    }
}
