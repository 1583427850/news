package xyz.linyh.article.service;

/**
 * 用来对freemarker生成静态文件和文件后续的管理
 */
public interface FreemarkerService {

    /**
     * 将文章内容用freemarker转换成一个文件然后保存到minio中
     */
    public void saveArticleContentToMinIO(Long apArticleId);
}
