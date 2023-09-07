package xyz.linyh.webmedia.service;

/**
// * 对文章的内容进行审核
 */
public interface NewsAutoAuditService {

    /**
     * 对文章的内容进行审核
     * @param newsId
     */
    public boolean auditText(Long newsId);
}
