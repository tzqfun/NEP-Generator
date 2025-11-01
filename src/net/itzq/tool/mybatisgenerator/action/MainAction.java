package net.itzq.tool.mybatisgenerator.action;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import net.itzq.tool.mybatisgenerator.gui.MainForm;

/**
 *
 * @discription
 *
 * @created 2022/11/22 19:44
 */
public class MainAction extends AnAction {

    /**
     * 点击后打开插件主页面
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiElement[] psiElements = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElements == null || psiElements.length != 1) {
            Messages.showMessageDialog("请选择一张表", "提示", Messages.getInformationIcon());
            return;
        }
        for (PsiElement psiElement : psiElements) {
            if (!(psiElement instanceof DbTable)) {
                Messages.showMessageDialog("请单选一张表", "提示", Messages.getInformationIcon());
                return;
            }
        }

        showDialog(e);
    }

    public static void showDialog(AnActionEvent e) {
        MainForm mainForm = new MainForm(e);
        mainForm.setThis(mainForm);
    }
}
