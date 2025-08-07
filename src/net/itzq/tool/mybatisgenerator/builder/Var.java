package net.itzq.tool.mybatisgenerator.builder;

public enum Var {
    fields("fields"),
    vueFields("vueFields"),
    tableName("tableName"),
    packageName("packageName"),
    packageLastName("packageLastName"),
    className("className"),
    ClassName("ClassName"),
    permissionPrefix("permissionPrefix"),
    tableAlias("tableAlias"),
    ;

    private String var;

    private Var(String key) {

        this.var = key;
    }

    public String getKey() {
        return var;
    }
}
