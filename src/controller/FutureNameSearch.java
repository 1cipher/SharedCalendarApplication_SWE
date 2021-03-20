package controller;

import com.mindfusion.scheduling.model.Item;
import com.mindfusion.scheduling.model.ItemList;
import utils.EditDistance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FutureNameSearch implements SearchStrategy {

    @Override
    public ItemList search(ItemList list, String toSearch) {

        SimpleDateFormat format = new SimpleDateFormat(("dd/MM/yy"));
        String todayString = format.format(new Date());
        Date todayDate = null;
        try {
            todayDate = format.parse(todayString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateToAnalyze = null;
        ItemList newList = new ItemList();
        for(Item element:list){

            try {
                dateToAnalyze = format.parse(element.getEndTime().toShortDateString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (dateToAnalyze.compareTo(todayDate)>=0)
                newList.add(element);
        }

        int min = 2;
        for (Item element:newList) {
            String comp = element.getHeaderText();
            int set = EditDistance.calculate(comp,toSearch);
            if (set>min) {
               newList.remove(element);
            }
        }

        return newList;

    }

    @Override
    public String textToDisplay() {

        return "Name,future only";
    }
}
