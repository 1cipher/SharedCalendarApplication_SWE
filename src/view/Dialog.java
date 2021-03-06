package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Dialog extends JFrame {

    private JButton button;
    private JLabel label;
    private Container cp;
    public enum type {error, success};

    private Dialog(){

        cp = getContentPane();
        cp.setLayout(null);
    }

    public void addDialogListener(ActionListener dialogListener){

        this.button.addActionListener(dialogListener);
    }

    public static class Builder extends JFrame{

        JButton button;
        JLabel label;
        Color color;
        String titleToDisplay;

        public Builder(){

            this.button = new JButton();
            this.button.setText("Ok!");

        }

        public Builder setType(type t){

            if (t==type.error) {
                this.color = Color.red;
                titleToDisplay = "Error!";
            }

            if (t==type.success) {
                this.color = Color.green;
                titleToDisplay = "Success!";
            }

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

    public void close(){
        setVisible(false);
        dispose();
    }


}

