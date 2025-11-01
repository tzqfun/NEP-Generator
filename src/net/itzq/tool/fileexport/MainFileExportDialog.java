package net.itzq.tool.fileexport;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainFileExportDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton browseBtn;
    private JTextField pathEditText;
    private JPanel fileListPanel;
    private JRadioButton onlyFileRadio;
    private JRadioButton structureRadio;
    private JLabel pathLabel;
    private JTextField basePathText;
    private JLabel tipsText;
    //用于接收选中的文件
    private AnActionEvent event;
    private String exportModel;
    private JBList fieldList;

    MainFileExportDialog(AnActionEvent event) {
        this.event = event;
        this.setTitle("导出");
        //初始默认选中导出文件带目录结构
        structureRadio.setSelected(true);
        //设置内容Panel
        initListeners();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        Project project = event.getProject();
        basePathText.setText(project.getBasePath());
    }

    /**
     * ok按钮确认导出点击事件
     */
    private void onOK() {
        String outputPath = pathEditText.getText();
        if (null == outputPath || "".equals(outputPath.trim())) {
            Messages.showErrorDialog(this, "导出路径不能为空", "错误");
            return;
        }

        String prjBasePath = basePathText.getText();
        if (null == prjBasePath || "".equals(prjBasePath.trim())) {
            Messages.showErrorDialog(this, "项目根目录不能为空", "错误");
            return;
        }

        ListModel<VirtualFile> fileListModel = fieldList.getModel();
        if (fileListModel.getSize() == 0) {
            Messages.showErrorDialog(this, "导出的文件不能为空", "错误");
            return;
        }

        try {
            Project project = event.getProject();
            for (int i = 0; i < fileListModel.getSize(); i++) {
                VirtualFile file = fileListModel.getElementAt(i);
                File sourceFile = new File(file.getPath());
                //判断是否需要导出文件目录
                if (structureRadio.isSelected()) {
                    //获取当前项目路径
                    if (project != null && project.getBasePath() != null) {

                        String filePath = sourceFile.getCanonicalPath().replaceAll("\\\\", "/");
                        int subPoint = filePath.indexOf(prjBasePath);

                        if (subPoint == -1) {
                            Messages.showErrorDialog(this, "路径转换错误\r\n" + prjBasePath + "\r\n" + filePath, "错误");
                            return;
                        }

                        String endPath = filePath.substring(subPoint + 1 + prjBasePath.length());
                        String targetPath = outputPath + "/" + project.getName() + "/" + endPath;

                        File targetFile = new File(targetPath);
                        FileUtil.copy(sourceFile, targetFile);

                    }
                } else if (onlyFileRadio.isSelected()) {
                    File targetFile = new File(outputPath + "/" + project.getName() + "/" + sourceFile.getName());
                    FileUtil.copy(sourceFile, targetFile);
                }
            }
        } catch (IOException e) {
            Messages.showErrorDialog(this, "错误：" + e.getMessage(), "错误");
            return;
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void initListeners() {

        browseBtn.addActionListener(e -> {
            //添加文件选择器
            FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            descriptor.setShowFileSystemRoots(true);
            descriptor.setHideIgnored(true);
            VirtualFile virtualFile = FileChooser.chooseFile(descriptor, null, null);
            if (virtualFile != null && virtualFile.exists()) {
                pathEditText.setText(virtualFile.getCanonicalPath());
            }
        });

        //确定点击事件
        buttonOK.addActionListener(e -> onOK());

        //取消点击事件
        buttonCancel.addActionListener(e -> onCancel());

        //关闭点击事件
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // 按下ESC关闭事件
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    /**
     * 自定义Panel
     */
    private void createUIComponents() {
        Map<String, VirtualFile> fileMap = new TreeMap<>();

        //获取选中的虚拟文件
        VirtualFile[] data = event.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        List<VirtualFile> hasDirectory = new ArrayList<>();
        for (VirtualFile vf : data) {
            addAllChildren(vf, fileMap, hasDirectory, 0);
        }

        if (hasDirectory.size() > 0) {
            Messages.showInfoMessage(this, "选中项包含目录", "提示");
        }

        List<VirtualFile> fileList = new ArrayList<>();
        for (String key : fileMap.keySet()) {
            fileList.add(fileMap.get(key));
        }

        fieldList = new JBList(fileList);
        fieldList.setEmptyText("未选择文件");
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        fileListPanel = decorator.createPanel();
    }

    private void addAllChildren(VirtualFile vf, Map<String, VirtualFile> fileMap, List<VirtualFile> hasDirectory, int deep) {
        if (deep > 10) {
            Messages.showErrorDialog(this, "错误！当前递归深度超过限制（10），目录检索已停止", "错误");
            return;
        }
        deep++;
        if (vf.isDirectory()) {
            hasDirectory.add(vf);
            VirtualFile[] vfs = vf.getChildren();
            for (VirtualFile item : vfs) {
                addAllChildren(item, fileMap, hasDirectory, deep);
            }
        } else {
            fileMap.put(vf.getCanonicalPath(), vf);
        }
    }
}
