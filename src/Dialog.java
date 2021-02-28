import javax.swing.*;
import java.awt.*;

public class Dialog extends JFrame {

    JButton button;
    JLabel label;
    Container cp;

    private Dialog(){

        cp = getContentPane();
        cp.setLayout(null);
    }

    public static class Builder extends JFrame{

        JButton button;
        JLabel label;
        Color color;
        String title;
        Container container;


        public Builder(){

            this.button = new JButton();
            this.button.setText("Ok!");

        }

        public Builder setColor(Color color){

            this.color = color;

            return this;
        }

        public Builder setDialogTitle(String title){

            this.title = title;

            return this;
        }

        public Builder setLabel(String text){

            this.label = new JLabel(text,SwingConstants.CENTER);

            return this;
        }
                                                                                    //TODO: BUILDER DOVREBBE ESSERE UNA JFRAME?
        public Dialog build(){

            Dialog dialog = new Dialog();
            dialog.setSize(200,150);
            dialog.button = this.button;
            dialog.label = this.label;
            dialog.label.setForeground(color);

            dialog.label.setLocation(25,10);
            dialog.label.setSize(150,20);

            this.button.setLocation(60,80);
            this.button.setSize(80,20);

            dialog.cp.add(dialog.button);
            dialog.cp.add(dialog.label);


            return dialog;

        }


    }

}
