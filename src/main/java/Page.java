/**
 * Created by ikanshyn on 2017-07-11.
 */
public class Page {

    public Page(String pageName, String pageContent, String source, String pageCategory, String keywords) {
        this.pageName = pageName;
        this.pageContent = pageContent;
        this.source = source;
        this.pageCategory = pageCategory;
        this.keywords = keywords;
    }

    private String pageName;
    private int adminUserId = 1;
    private String pageContent;
    private String source;
    private String pageCategory;
    private String keywords;
    private String video;


    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public int getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(int adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getPageCategory() {
        return pageCategory;
    }

    public void setPageCategory(String pageCategory) {
        this.pageCategory = pageCategory;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageName='" + pageName + '\'' +
                ", adminUserId=" + adminUserId +
                ", pageContent='" + pageContent + '\'' +
                ", source='" + source + '\'' +
                ", pageCategory='" + pageCategory + '\'' +
                ", keywords='" + keywords + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
