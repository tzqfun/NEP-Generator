package net.itzq.tool.mybatisgenerator.action;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import net.itzq.tool.mybatisgenerator.gui.MainForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void main(String[] args) {

        String txt = "package net.itzq.tool.mybatis_generator.action;import com.intellij.database.model.DasColumn;\n"
                + "import com.intellij.database.psi.DbTable;";

        String reg = "(package)\\s+([^\\.\\s]+(\\.[^\\.\\s]+)*[;])";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(txt);
        while (matcher.find()) {
            String pkg = matcher.group(2);
            System.out.println(pkg.substring(0, pkg.length() - 1));
        }

    }
}
