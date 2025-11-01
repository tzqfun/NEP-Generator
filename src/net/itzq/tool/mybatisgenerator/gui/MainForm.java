package net.itzq.tool.mybatisgenerator.gui;

import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.util.containers.JBIterable;
import net.itzq.tool.mybatisgenerator.ConvertConfig;
import net.itzq.tool.mybatisgenerator.DataConvert;
import net.itzq.tool.mybatisgenerator.Generator;
import net.itzq.tool.mybatisgenerator.TemplateLoader;
import net.itzq.tool.mybatisgenerator.util.CrossPlatformAppDataUtil;
import net.itzq.tool.mybatisgenerator.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @discription
 *
 * @created 2022/11/23 21:41
 */
public class MainForm extends JFrame {

    private MainForm _this;

    private JPanel panel1;
    private JTextArea textArea1;

    private JButton copyButton;
    private JCheckBox topWinCheckBox;
    private JButton genXmlbutton;
    private JButton genMapperButton;
    private JButton genEntityButton;
    private JTextField packageNameTextField;
    private JButton pkgButton;
    private JLabel pkgLabel;
    private JTextField classNameTextField;
    private JButton copyFileNameButton;
    private JButton genControlButton;
    private JButton genServiceButton;
    private JRadioButton modeNep;
    private JButton genVueList;
    private JButton genVueForm;
    private JButton genALLButton;
    private JButton configButton;
    private JButton openDir;
    private JPanel templateSelectPanel;
    private JButton guideButton;

    private String originalStr;
    private boolean topFrame = true;

    //    main
    private AnActionEvent anActionEvent;
    private Project project;
    private PsiElement[] psiElements;
    private boolean editMode = false;
    private String editFilePath = null;

    private String winTitle = "Mybatis 开发助手 ";

    ButtonGroup group = new ButtonGroup();
    String templateGroupDirName = "nep2x";

    public MainForm(AnActionEvent anActionEvent) {

        originalStr = "";

        setAlwaysOnTop(topFrame);
        topWinCheckBox.setSelected(topFrame);

        textArea1.setText("");
        textArea1.setCaretPosition(0);

        setContentPane(panel1);

        setTitle(winTitle);
        setSize(520, 680);

        int width = getWidth();                     //获得窗口宽
        int height = getHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screen.getWidth() - width - 100);
        int y = (int) ((screen.getHeight() - height) / 2);

        //        setLocationRelativeTo(null);
        setLocation(x, y);
        setVisible(true);
        requestFocus();
        //setModal(true);

        initMain(anActionEvent);

        TextAreaMenu menu = new TextAreaMenu(textArea1);

        textArea1.addMouseListener(menu);
        textArea1.add(menu);

        initSelectPanel();

        copyButton.addActionListener(actionEvent -> {
            JTextArea t = new JTextArea();
            t.setText(originalStr);
            t.selectAll();
            t.copy();
        });

        copyFileNameButton.addActionListener(actionEvent -> {
            JTextArea t = new JTextArea();
            String fileName = getTitle();
            if (StringUtils.endsWith(fileName, "Entity")) {
                fileName = StringUtils.substringBeforeLast(fileName, "Entity");
            }
            t.setText(fileName.replaceAll(winTitle, "").trim());
            t.selectAll();
            t.copy();
        });

        topWinCheckBox.addActionListener(actionEvent -> {
            topFrame = !topFrame;
            this.setAlwaysOnTop(topFrame);
            topWinCheckBox.setSelected(topFrame);
        });

        genALLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String basePath =
                        project.getBasePath() + File.separator + "src" + File.separator + "main" + File.separator
                                + "java";
                String packagePath = (StringUtils.lowerCase(packageNameTextField.getText())).replace(".",
                        File.separator);
                basePath = basePath + File.separator + packagePath;
                File format = new File(basePath);
                basePath = format.getAbsolutePath();

                JDialog dialog = new JDialog(_this, "自动生成", true);
                dialog.setLayout(new BorderLayout());
                dialog.setSize(500, 250);

                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel("创建路径:");
                JTextArea textArea = new JTextArea(5, 40);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textArea);

                JPanel pathPanel = new JPanel(new BorderLayout());
                pathPanel.add(scrollPane, BorderLayout.CENTER);

                // 使用IDEA的PathChooser组件
                JButton browseBtn = new JButton("选择路径");
                browseBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
                                .withTitle("选择目标目录");
                        VirtualFile file = FileChooser.chooseFile(descriptor, project, null);
                        if (file != null) {
                            String path = file.getPath();
                            File format = new File(path, StringUtils.lowerCase(classNameTextField.getText()));
                            path = format.getAbsolutePath();
                            textArea.setText(path);
                        }
                    }
                });

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(browseBtn);

                panel.add(label, BorderLayout.NORTH);
                panel.add(pathPanel, BorderLayout.CENTER);
                panel.add(buttonPanel, BorderLayout.SOUTH);

                textArea.setText(basePath);

                JPanel bottomPanel = new JPanel();
                JButton confirmBtn = new JButton("确认");
                JButton cancelBtn = new JButton("取消");
                bottomPanel.add(confirmBtn);
                bottomPanel.add(cancelBtn);

                dialog.add(panel, BorderLayout.CENTER);
                dialog.add(bottomPanel, BorderLayout.SOUTH);

                confirmBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String basePath = textArea.getText();
                        if (StringUtils.isNotBlank(basePath)) {
                            createPackageStructure(basePath);
                            dialog.dispose();
                        } else {
                            Messages.showErrorDialog(dialog, "路径不能为空！", "错误");
                        }
                    }
                });

                cancelBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });

                dialog.setLocationRelativeTo(_this);
                dialog.setVisible(true);
            }
        });
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 退出编辑
                if (editMode) {

                    boolean exit = true;
                    String templateContent = textArea1.getText();

                    if (editFilePath != null && StringUtils.isNotBlank(templateContent)) {
                        // 显示自定义确认对话框
                        Object[] options = { "保存", "不保存", "取消" };
                        int result = JOptionPane.showOptionDialog(_this,
                                "模板内容已修改，是否保存？\n\n 模版文件：\n" + editFilePath,
                                "保存确认",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                                // 默认选择"保存"
                        );

                        if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                            if (result == JOptionPane.YES_OPTION) {

                                Path path = Paths.get(editFilePath);
                                try {
                                    Files.write(path, templateContent.getBytes(StandardCharsets.UTF_8));
                                    // 保存成功提示
                                    JOptionPane.showMessageDialog(_this,
                                            "模板保存成功！",
                                            "保存成功",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(_this,
                                            "保存失败：\n" + ex.getMessage(),
                                            "保存失败",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        } else {
                            exit = false;
                        }
                    }

                    if (exit) {
                        originalStr = "";
                        textArea1.setText(originalStr);
                        textArea1.setBackground(new Color(255, 255, 255));
                        editMode = false;
                        editFilePath = null;
                        configButton.setText("编辑模版");
                    }

                } else {
                    textArea1.setBackground(new Color(200, 237, 204));
                    originalStr = "";
                    textArea1.setText(originalStr);
                    editMode = true;
                    editFilePath = null;
                    configButton.setText("退出编辑");
                }
            }
        });

        openDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String dataPath = CrossPlatformAppDataUtil.getOrCreateAppDataPathDefault(null);
                    File folder = new File(dataPath);

                    // 检查文件夹是否存在
                    if (folder.exists() && folder.isDirectory()) {
                        Desktop.getDesktop().open(folder);
                    } else {
                        Messages.showErrorDialog("文件夹不存在: " + dataPath, "打开失败");
                    }
                } catch (IOException ex) {
                    Messages.showErrorDialog("无法打开文件夹: " + ex.getMessage(), "错误");
                } catch (Exception ex) {
                    Messages.showErrorDialog("系统不支持此操作: " + ex.getMessage(), "错误");
                }
            }
        });
        guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String readme = TemplateLoader.readTemplateFile("readme.txt");
                textArea1.setText(readme);
                textArea1.setCaretPosition(0);
            }
        });
    }

    private void initSelectPanel() {

        templateSelectPanel.setLayout(new GridLayout(1, 6, 10, 5));
        templateSelectPanel.setBorder(BorderFactory.createTitledBorder("模板选择"));

        templateSelectPanel.removeAll();



        String dataPath = CrossPlatformAppDataUtil.getOrCreateAppDataPathDefault(null);
        File dataDir = new File(dataPath);
        // 读取所有文件夹
        File[] folders = dataDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() && !file.isHidden() && !"nep2x".equals(file.getName());
            }
        });

        if (folders != null && folders.length > 0) {
            int totalFolders = folders.length + 1;
            int columns = 6;
            int rows = (int) Math.ceil((double) totalFolders / columns);

            templateSelectPanel.setLayout(new GridLayout(rows, columns, 10, 5));

            // 按名称排序
            Arrays.sort(folders);

            for (File folder : folders) {
                JRadioButton radioButton = new JRadioButton();
                radioButton.setText(folder.getName());
                // radioButton.setToolTipText(folder.getAbsolutePath());
                group.add(radioButton);
                templateSelectPanel.add(radioButton);
            }


        }
        group.add(modeNep);
        templateSelectPanel.add(modeNep);

        // 默认选择第一个
        if (templateSelectPanel.getComponentCount() > 0) {
            ((JRadioButton) templateSelectPanel.getComponent(0)).setSelected(true);
        }

        templateSelectPanel.revalidate();
        templateSelectPanel.repaint();
    }

    public void initMain(AnActionEvent anActionEvent) {

        this.anActionEvent = anActionEvent;
        this.project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            Document currentDoc = editor.getDocument();
            String text = currentDoc.getText();

            String reg = "(package)\\s+([^\\.\\s]+(\\.[^\\.\\s]+)*[;])";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(text);
            String pkgText = null;
            boolean find = matcher.find();
            if (find) {
                pkgText = matcher.group(2);
            }

            if (pkgText != null) {
                this.packageNameTextField.setText(pkgText.substring(0, pkgText.length() - 1));
            } else {

                PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc);

                if (psiFile != null) {
                    VirtualFile file = psiFile.getOriginalFile().getVirtualFile();
                    String canonicalPath = file.getCanonicalPath();
                    String fileName = file.getName();

                    if (canonicalPath == null) {
                        canonicalPath = "";
                    }

                    String filePath = canonicalPath.replaceAll("\\\\", "/");

                    if (fileName.endsWith(".java") && canonicalPath.contains("/src/main/java/")) {

                        int idx = filePath.indexOf("/src/main/java/");

                        String pkg = filePath.substring((idx + "/src/main/java/".length()),
                                (filePath.length() - fileName.length() - 1)).replaceAll("/", ".");

                        this.packageNameTextField.setText(pkg);

                    }
                }
            }
            String pkgInputVal = this.packageNameTextField.getText();
            if (StringUtils.isNotBlank(pkgInputVal)) {
                if (StrUtil.endsWithAnyIgnoreCase(pkgInputVal,
                        ".vo",
                        ".entity",
                        ".service",
                        ".mapper",
                        ".dao",
                        ".controller",
                        ".web",
                        ".util",
                        ".utils")) {
                    pkgInputVal = StringUtils.substringBeforeLast(pkgInputVal, ".");
                    this.packageNameTextField.setText(pkgInputVal);
                }
            }

        }

        this.psiElements = anActionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY);

        DbTable psiElement = (DbTable) psiElements[0];
        String tableName = psiElement.getName();
        classNameTextField.setText(DataConvert.toUpperCamel(tableName));

        pkgButton.addActionListener(actionEvent -> {
            if (topWinCheckBox.isSelected()) {
                this.setAlwaysOnTop(false);
            }
            final PackageChooserDialog chooser = new PackageChooserDialog("chooser model package", project);
            chooser.selectPackage(packageNameTextField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? null : psiPackage.getQualifiedName();
            if (StrUtil.isNotBlank(packageName)) {
                packageNameTextField.setText(packageName);
            }

            if (topWinCheckBox.isSelected()) {
                topFrame = true;
                this.setAlwaysOnTop(true);
            }
        });

        ActionListener genButtonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                String text = button.getText().toLowerCase();
                genButtonClick(text);
            }
        };

        genXmlbutton.addActionListener(genButtonActionListener);
        genEntityButton.addActionListener(genButtonActionListener);
        genMapperButton.addActionListener(genButtonActionListener);
        genControlButton.addActionListener(genButtonActionListener);
        genServiceButton.addActionListener(genButtonActionListener);
        genVueList.addActionListener(genButtonActionListener);
        genVueForm.addActionListener(genButtonActionListener);

    }

    public String genButtonClick(String text) {

        if (modeNep.isSelected()) {
            templateGroupDirName = "nep2x";
        } else {
            Enumeration<AbstractButton> allRadioButton = group.getElements();
            while (allRadioButton.hasMoreElements()) {
                JRadioButton button = (JRadioButton) allRadioButton.nextElement();
                if (button.isSelected()) {
                    templateGroupDirName = button.getText();
                    break;
                }
            }
        }

        return generate(text);
    }

    public String generate(String action) {
        ConvertConfig convertConfig = getConvertConfig();
        Generator g = new Generator(convertConfig);
        String txt = "";

        if (StrUtil.endsWithAnyIgnoreCase(action, "entity")) {
            setTitle(winTitle + convertConfig.getClassName() + "Entity");
        }
        if (StrUtil.endsWithAnyIgnoreCase(action, "mapper")) {
            setTitle(winTitle + convertConfig.getClassName() + "Mapper");
        }
        if (StrUtil.endsWithAnyIgnoreCase(action, "xml")) {
            setTitle(winTitle + convertConfig.getClassName() + "Mapper.xml");
        }
        if (StrUtil.endsWithAnyIgnoreCase(action, "controller")) {
            setTitle(winTitle + convertConfig.getClassName() + "Controller");
        }
        if (StrUtil.endsWithAnyIgnoreCase(action, "service")) {
            setTitle(winTitle + convertConfig.getClassName() + "Service");
        }
        if (StrUtil.endsWithAnyIgnoreCase(action, "vueform")) {
            setTitle(winTitle + getClassNameLower(convertConfig.getClassName()) + "Form.vue");
        }
        if (StrUtil.endsWithAnyIgnoreCase(action, "vuelist")) {
            setTitle(winTitle + getClassNameLower(convertConfig.getClassName()) + "List.vue");
        }

        String TemplateFileName = action + ".txt";

        String templateContent = null;
        try {
            templateContent = TemplateLoader.readLocalTemplateFile(templateGroupDirName, TemplateFileName);

            if (StringUtils.isNotBlank(templateContent)) {

                if (editMode) {
                    txt = templateContent;
                    String dataPath = CrossPlatformAppDataUtil.getOrCreateAppDataPathDefault(templateGroupDirName);
                    editFilePath = dataPath + "/" + TemplateFileName;
                } else {
                    txt = g.genCode(templateGroupDirName, TemplateFileName, templateContent);
                }
            } else {
                txt = "IO错误：模版为空";
            }

        } catch (IOException e) {
            e.printStackTrace();
            txt = "IO错误：读取模版失败。";
        }

        originalStr = txt;
        textArea1.setText(txt);
        textArea1.setCaretPosition(0);
        return txt;
    }

    public ConvertConfig getConvertConfig() {
        DbTable psiElement = (DbTable) psiElements[0];
        String tableName = psiElement.getName();

        ConvertConfig config = new ConvertConfig();
        //        config.setExportPath(pathTextField.getText());
        //        config.setDbURL(dbURLTextField.getText());
        //        config.setDbUserName(dbUserNametextField.getText());
        //        config.setDbPassword(dbPasswordField.getText());
        //        config.setDbName(dbNameTextField.getText());
        config.setTableName(tableName);
        config.setClassName(classNameTextField.getText());
        config.setPackageName(packageNameTextField.getText());

        // 处理所有列
        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(psiElement);
        List<Map<String, String>> arr = new ArrayList<>();
        for (DasColumn column : columns) {
            String columnName = column.getName();
            String columnType = column.getDataType().typeName;
            String columnComment = column.getComment();

            Map<String, String> entity = new LinkedHashMap<>();
            entity.put("column_name", columnName);
            entity.put("data_type", columnType);
            entity.put("column_comment", columnComment);
            arr.add(entity);
        }
        config.setColsList(arr);

        return config;
    }

    public String getClassNameLower(String className) {
        if (StrUtil.isNotBlank(className)) {
            return new StringBuilder(className).replace(0, 1, String.valueOf(className.charAt(0)).toLowerCase())
                    .toString();
        }
        return className;
    }

    public void setThis(MainForm _this) {
        this._this = _this;
    }

    private void createPackageStructure(String baseDir) {
        ConvertConfig convertConfig = getConvertConfig();
        String className = convertConfig.getClassName();

        File packageDir = new File(baseDir);

        String[] subPackages = { "controller", "service", "mapper", "mapper" + File.separator + "xml", "entity" };

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("包结构创建结果:\n");

        if (!packageDir.exists()) {
            if (createDir(packageDir)) {
                resultMessage.append("\n成功创建:").append(packageDir.getAbsolutePath());
            }
        }

        if (packageDir.exists()) {
            for (String subPackage : subPackages) {
                File subDir = new File(packageDir, subPackage);

                if (!subDir.exists()) {
                    if (createDir(subDir)) {
                        resultMessage.append("\n成功创建:").append(subDir.getAbsolutePath());
                    }
                }

                if (subDir.exists()) {
                    if ("entity".equals(subPackage)) {
                        String fileName = className + ".java";
                        genCodeFile(resultMessage, subDir, fileName, "entity");
                    }
                    if ("controller".equals(subPackage)) {
                        String fileName = className + "Controller.java";
                        genCodeFile(resultMessage, subDir, fileName, "controller");
                    }
                    if ("service".equals(subPackage)) {
                        String fileName = className + "Service.java";
                        genCodeFile(resultMessage, subDir, fileName, "service");
                    }
                    if ("mapper".equals(subPackage)) {
                        String fileName = className + "Mapper.java";
                        genCodeFile(resultMessage, subDir, fileName, "mapper");
                    }
                    if (("mapper" + File.separator + "xml").equals(subPackage)) {
                        String fileName = className + "Mapper.xml";
                        genCodeFile(resultMessage, subDir, fileName, "xml");
                    }
                }

            }

        }

        JOptionPane.showMessageDialog(this, resultMessage.toString(), "创建完成", JOptionPane.INFORMATION_MESSAGE);
    }

    private void genCodeFile(StringBuilder resultMessage, File subDir, String fileName, String entity) {
        String path = subDir.getAbsolutePath();
        Path fullPath = Paths.get(path, fileName);
        boolean b = createFileIfNotExists(fullPath, genButtonClick(entity));
        if (b) {
            resultMessage.append("\n成功创建:").append(fullPath.toAbsolutePath());
        } else {
            resultMessage.append("\n失败:").append(fullPath.toAbsolutePath());
        }
    }

    private boolean createDir(File dir) {
        if (dir.exists()) {
            return true;
        }
        if (dir.mkdirs()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "创建目录失败: " + dir.getAbsolutePath(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean createFileIfNotExists(Path fullPath, String content) {

        try {
            if (Files.exists(fullPath)) {
                return false;
            }
            Path parentDir = fullPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Files.write(fullPath, content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
