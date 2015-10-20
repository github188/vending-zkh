package com.mc.vending.data;

import java.io.Serializable;

/**
 * <p>
 * Table: <strong>Interface 接口配置</strong>
 * <p>
 * <table class="er-mapping" cellspacing=0 cellpadding=0 style="border:solid 1 #666;padding:3px;">
 * <tr style="background-color:#ddd;Text-align:Left;">
 * <th nowrap>属性名</th>
 * <th nowrap>属性类型</th>
 * <th nowrap>字段名</th>
 * <th nowrap>字段类型</th>
 * <th nowrap>说明</th>
 * </tr>
 * <tr>
 * <td>m03Id</td>
 * <td>{@link String}</td>
 * <td>m03_id</td>
 * <td>varchar</td>
 * <td>接口配置ID</td>
 * </tr>
 * <tr>
 * <td>m03M02Id</td>
 * <td>{@link String}</td>
 * <td>m03_m02_id</td>
 * <td>varchar</td>
 * <td>事业体ID</td>
 * </tr>
 * <tr>
 * <td>m03Name</td>
 * <td>{@link String}</td>
 * <td>m03_name</td>
 * <td>varchar</td>
 * <td>接口名称</td>
 * </tr>
 * <tr>
 * <td>m03Target</td>
 * <td>{@link String}</td>
 * <td>m03_target</td>
 * <td>varchar</td>
 * <td>接口对象</td>
 * </tr>
 * <tr>
 * <td>m03Optype</td>
 * <td>{@link String}</td>
 * <td>m03_optype</td>
 * <td>varchar</td>
 * <td>接口操作</td>
 * </tr>
 * <tr>
 * <td>m03Remark</td>
 * <td>{@link String}</td>
 * <td>m03_remark</td>
 * <td>varchar</td>
 * <td>备注</td>
 * </tr>
 * <tr>
 * <td>m03ExeInterval</td>
 * <td>{@link Integer}</td>
 * <td>m03_exeInterval</td>
 * <td>int</td>
 * <td>执行频率（分）</td>
 * </tr>
 * <tr>
 * <td>m03StartTime</td>
 * <td>{@link String}</td>
 * <td>m03_startTime</td>
 * <td>varchar</td>
 * <td>开始时间</td>
 * </tr>
 * <tr>
 * <td>m03EndTime</td>
 * <td>{@link String}</td>
 * <td>m03_endTime</td>
 * <td>varchar</td>
 * <td>结束时间</td>
 * </tr>
 * <tr>
 * <td>m03ExeIndex</td>
 * <td>{@link Integer}</td>
 * <td>m03_exeIndex</td>
 * <td>int</td>
 * <td>执行顺序</td>
 * </tr>
 * <tr>
 * <td>m03RowCount</td>
 * <td>{@link Integer}</td>
 * <td>m03_rowCount</td>
 * <td>int</td>
 * <td>执行行数</td>
 * </tr>
 * <tr>
 * <td>m03CreateUser</td>
 * <td>{@link String}</td>
 * <td>m03_createUser</td>
 * <td>varchar</td>
 * <td>创建人</td>
 * </tr>
 * <tr>
 * <td>m03CreateTime</td>
 * <td>{@link String}</td>
 * <td>m03_createTime</td>
 * <td>varchar</td>
 * <td>创建时间</td>
 * </tr>
 * <tr>
 * <td>m03ModifyUser</td>
 * <td>{@link String}</td>
 * <td>m03_modifyUser</td>
 * <td>varchar</td>
 * <td>最后修改人</td>
 * </tr>
 * <tr>
 * <td>m03ModifyTime</td>
 * <td>{@link String}</td>
 * <td>m03_modifyTime</td>
 * <td>varchar</td>
 * <td>最后修改时间</td>
 * </tr>
 * <tr>
 * <td>m03RowVersion</td>
 * <td>{@link String}</td>
 * <td>m03_rowVersion</td>
 * <td>varchar</td>
 * <td>时间戳</td>
 * </tr>
 * </table>
 *
 * @author fenghu
 * @date 2015-3-16
 * @email fenghu@mightcloud.com
 */
public class InterfaceData extends BaseParseData implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8664724073361702365L;
    private String            m03Id;
    private String            m03M02Id;
    private String            m03Name;
    private String            m03Target;
    private String            m03Optype;
    private String            m03Remark;
    private int               m03ExeInterval;
    private String            m03StartTime;
    private String            m03EndTime;
    private int               m03ExeIndex;
    private int               m03RowCount;
    private String            m03CreateUser;
    private String            m03CreateTime;
    private String            m03ModifyUser;
    private String            m03ModifyTime;
    private String            m03RowVersion;

    public String getM03Id() {
        return m03Id;
    }

    public void setM03Id(String m03Id) {
        this.m03Id = m03Id;
    }

    public String getM03M02Id() {
        return m03M02Id;
    }

    public void setM03M02Id(String m03m02Id) {
        m03M02Id = m03m02Id;
    }

    public String getM03Name() {
        return m03Name;
    }

    public void setM03Name(String m03Name) {
        this.m03Name = m03Name;
    }

    public String getM03Target() {
        return m03Target;
    }

    public void setM03Target(String m03Target) {
        this.m03Target = m03Target;
    }

    public String getM03Optype() {
        return m03Optype;
    }

    public void setM03Optype(String m03Optype) {
        this.m03Optype = m03Optype;
    }

    public String getM03Remark() {
        return m03Remark;
    }

    public void setM03Remark(String m03Remark) {
        this.m03Remark = m03Remark;
    }

    public int getM03ExeInterval() {
        return m03ExeInterval;
    }

    public void setM03ExeInterval(int m03ExeInterval) {
        this.m03ExeInterval = m03ExeInterval;
    }

    public String getM03StartTime() {
        return m03StartTime;
    }

    public void setM03StartTime(String m03StartTime) {
        this.m03StartTime = m03StartTime;
    }

    public String getM03EndTime() {
        return m03EndTime;
    }

    public void setM03EndTime(String m03EndTime) {
        this.m03EndTime = m03EndTime;
    }

    public int getM03ExeIndex() {
        return m03ExeIndex;
    }

    public void setM03ExeIndex(int m03ExeIndex) {
        this.m03ExeIndex = m03ExeIndex;
    }

    public int getM03RowCount() {
        return m03RowCount;
    }

    public void setM03RowCount(int m03RowCount) {
        this.m03RowCount = m03RowCount;
    }

    public String getM03CreateUser() {
        return m03CreateUser;
    }

    public void setM03CreateUser(String m03CreateUser) {
        this.m03CreateUser = m03CreateUser;
    }

    public String getM03CreateTime() {
        return m03CreateTime;
    }

    public void setM03CreateTime(String m03CreateTime) {
        this.m03CreateTime = m03CreateTime;
    }

    public String getM03ModifyUser() {
        return m03ModifyUser;
    }

    public void setM03ModifyUser(String m03ModifyUser) {
        this.m03ModifyUser = m03ModifyUser;
    }

    public String getM03ModifyTime() {
        return m03ModifyTime;
    }

    public void setM03ModifyTime(String m03ModifyTime) {
        this.m03ModifyTime = m03ModifyTime;
    }

    public String getM03RowVersion() {
        return m03RowVersion;
    }

    public void setM03RowVersion(String m03RowVersion) {
        this.m03RowVersion = m03RowVersion;
    }

    @Override
    public String toString() {
        return "InterfaceData [m03Id=" + m03Id + ", m03M02Id=" + m03M02Id + ", m03Name=" + m03Name
                + ", m03Target=" + m03Target + ", m03Optype=" + m03Optype + ", m03Remark=" + m03Remark
                + ", m03ExeInterval=" + m03ExeInterval + ", m03StartTime=" + m03StartTime + ", m03EndTime="
                + m03EndTime + ", m03ExeIndex=" + m03ExeIndex + ", m03RowCount=" + m03RowCount
                + ", m03CreateUser=" + m03CreateUser + ", m03CreateTime=" + m03CreateTime
                + ", m03ModifyUser=" + m03ModifyUser + ", m03ModifyTime=" + m03ModifyTime
                + ", m03RowVersion=" + m03RowVersion + "]";
    }

}
