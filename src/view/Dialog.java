package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Dialog extends JFrame {

    private JButton button;
    private JLabel label;
    private Container cp;
    public enum type {ERROR, SUCCESS, GENERIC}

    private Dialog(){

        cp = getContentPane();
        cp.setLayout(null);
        setLocationRelativeTo(null);

    }

    public void addDialogListener(ActionListener dialogListener){

        this.button.addActionListener(dialogListener);
    }

    public String getLabel(){

        return label.getText();
    }

    public static class Builder {

        JButton button;
        JLabel label;
        Color color;
        String title;

        public Builder(){

            this.button = new JButton();
            this.button.setText("Ok!");

        }

        public Builder setType(type t){

            if (t==type.ERROR) {
                this.color = Color.red;
                title = "Error!";
            }

            if (t==type.SUCCESS) {
                this.color = Color.green;
                title = "Success!";
            }

            if(t==type.GENERIC){
                this.color = Color.black;
                title = "Settings";
            }

            return this;
        }

        public Builder setLabel(String text){

            this.label = new JLabel("<html>"+text+"</html>",SwingConstants.CENTER);

            return this;
        }

        public Dialog build(){

            Dialog dialog = new Dialog();
            dialog.setTitle(title);
            dialog.setSize(200,150);
            dialog.button = this.button;
            dialog.label = this.label;
            dialog.label.setForeground(color);

            dialog.label.setLocation(25,10);
            dialog.label.setSize(150,40);

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

