package cc.leevi.webbase.vo;

import java.math.BigDecimal;

public class KgspInfoVo {
    private String nodeType;//类型
    private String nodeName;//名称
    private String nodeValue;//号码
    private BigDecimal pageRank;//风险评分
    private BigDecimal avgpageRank;//图谱平均分
    private String pageRankLevel;//风险等级

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getPageRankLevel() {
        return pageRankLevel;
    }

    public void setPageRankLevel(String pageRankLevel) {
        this.pageRankLevel = pageRankLevel;
    }

    public BigDecimal getPageRank() {
        return pageRank;
    }

    public void setPageRank(BigDecimal pageRank) {
        this.pageRank = pageRank;
    }

    public BigDecimal getAvgpageRank() {
        return avgpageRank;
    }

    public void setAvgpageRank(BigDecimal avgpageRank) {
        this.avgpageRank = avgpageRank;
    }
}