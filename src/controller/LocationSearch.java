package controller;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import utils.EditDistance;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationSearch implements SearchStrategy {

    @Override
    public ItemList search(ItemList list, String toSearch) {

        SimpleDateFormat format = new SimpleDateFormat(("dd-MM-yyyy"));
        String todayDate = format.format(new Date());
        ItemList newList = new ItemList();
        for(Item element:list){
            String dateToAnalyze = element.getEndTime().toShortDateString();
            if (dateToAnalyze.compareTo(todayDate)>=0)
                newList.add(element);
        }

        int min = 2;
        Item result = null;
        for (Item element:newList) {
            String comp = element.getLocation().toString();
            int set = EditDistance.computeLevenshteinDistance(comp,toSearch);
            if (set>min) {
                newList.remove(element);
            }
        }

        return newList;

    }

    @Override
    public String textToDisplay() {

        return "Location,future only";
    }
}
