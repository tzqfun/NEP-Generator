package net.itzq.tool.fileexport;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * 文件右击菜单Demo，Export按钮，位于剪切复制粘贴组最后
 */
public class FileExportAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        MainFileExportDialog dialog = new MainFileExportDialog(e);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.requestFocus();
    }
}
