package com.bread.traveler.tools;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.tool.annotation.Tool;

import java.io.IOException;

@Slf4j
public class WebFetchTools {

    // 模拟真实的 Chrome 浏览器 User-Agent (关键：防止被反爬拦截)
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";
    private static final String REFERRER = "https://www.google.com/";
    private static final int MAX_CONTENT_LENGTH = 20000;

    /**
     * 抓取指定 URL 的内容，并清洗
     * * @param url 目标网址
     * @return 格式化后的网页标题和正文
     */
    @Tool(name = ToolNames.FETCH_TOOL_NAME, description = "Fetch the content of the given URL and return the cleaned HTML code")
    public String fetchAndClean(String url) {
        try {
            log.info("Start fetching URL: {}", url);

            // 1. 抓取
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(30000)
                    .referrer(REFERRER)
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .get();

            // 2. 预处理：移除绝对不需要的标签
            // remove() 会将节点从 DOM 树中彻底删除
            doc.select("script, style, noscript, iframe, svg, header, footer, nav").remove();

            // 3. 清理无用属性 (class, id, style)，只保留语义
            // 我们遍历所有元素，清除除了 href 和 src 之外的所有属性
            // 这样可以极大节省 Token
            Elements allElements = doc.getAllElements();
            for (Element el : allElements) {
                // 临时保存需要的属性
                String href = el.absUrl("href"); // 获取绝对路径
                String src = el.absUrl("src");

                // 清除所有属性
                el.clearAttributes();

                // 恢复关键属性
                if ("a".equals(el.tagName()) && !href.isEmpty()) {
                    el.attr("href", href);
                }
                if ("img".equals(el.tagName()) && !src.isEmpty()) {
                    el.attr("src", src);
                    // 图片通常可以用 alt 作为描述
                    // el.attr("alt", ...);
                }
            }

            // 4. 获取 Body 的 HTML
            String cleanHtml = doc.body().html();

            // 5. 压缩空白字符 (可选，进一步节省 Token)
            // 将多个换行/空格合并为一个，但保留 HTML 标签换行
            cleanHtml = cleanHtml.replaceAll("\\s+", " ").trim();

            // 6. 截断处理
            if (cleanHtml.length() > MAX_CONTENT_LENGTH) {
                // 简单的截断可能会截断 HTML 标签，更严谨的做法是统计 token 或按节点截断
                // 这里做个简单保护
                cleanHtml = cleanHtml.substring(0, MAX_CONTENT_LENGTH) + "...";
            }

            return "URL: " + url + "\n" +
                    "Title: " + doc.title() + "\n" +
                    "--- Web Content (Cleaned HTML) ---\n" +
                    cleanHtml;

        } catch (IOException e) {
            log.error("Error fetching URL: {}", url, e);
            return "Error fetching content: " + e.getMessage();
        }
    }

}
