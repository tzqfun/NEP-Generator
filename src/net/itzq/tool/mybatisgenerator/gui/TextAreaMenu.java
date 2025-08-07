package net.itzq.tool.mybatisgenerator.gui;

;;;;;;
;import javax.swing.*;
;import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
;

/**
 *
 * @discription
 *
 * @created 2022/11/23 21:04
 */
public class TextAreaMenu extends JPopupMenu implements MouseListener {

    private static final long serialVersionUID = -2308615404205560110L;
    private JMenuItem selectAll = null;
    private JMenuItem copy = null;
    private JMenuItem paste = null;
    private JMenuItem cut = null;

    private JTextArea textArea;

    public TextAreaMenu(JTextArea textArea) {
        super();
        this.textArea = textArea;
        init();
    }

    private void init() {

        selectAll = new JMenuItem("全选");
        add(copy = new JMenuItem("复制"));
        paste = new JMenuItem("粘贴");
        cut = new JMenuItem("剪切");
        selectAll.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
        selectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });

    }

    /**
     * 菜单动作
     * @param e
     */
    public void action(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals(selectAll.getText())) { // 复制
            textArea.selectAll();
        } else if (str.equals(copy.getText())) { // 粘贴
            textArea.copy();
        } else if (str.equals(paste.getText())) { // 粘贴
            textArea.paste();
        } else if (str.equals(cut.getText())) { // 剪切
            textArea.cut();
        }
    }

    /**
     * 剪切板中是否有文本数据可供粘贴
     */
    public boolean isClipboardString() {

        boolean b = false;
        Clipboard clipboard = textArea.getToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(textArea);
        try {
            if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    /**
     * 文本组件中是否具备复制的条件
     */
    public boolean isCanCopy() {
        boolean b = false;
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        if (start != end) {
            b = true;
        }
        return b;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            copy.setEnabled(isCanCopy());
            paste.setEnabled(isClipboardString());
            cut.setEnabled(isCanCopy());
            show(textArea, e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

}
