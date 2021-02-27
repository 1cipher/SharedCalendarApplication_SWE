import javax.swing.*;

public interface LogDialogBuilder {

    void addLabel(String Label);

    void addButton();

    JFrame getDialog();
}
