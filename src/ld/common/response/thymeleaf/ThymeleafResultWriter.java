package ld.common.response.thymeleaf;

import ld.common.context.Context;
import ld.common.response.Model;
import ld.common.response.ResponseWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import java.io.IOException;

public class ThymeleafResultWriter implements ResponseWriter {

    private TemplateEngine templateEngine;

    public ThymeleafResultWriter(ServletContext context) {
        templateEngine = new TemplateEngine();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        // TODO: enable cache
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    public void write(Object result, Class<?> resultType, Context context) throws IOException {
        String viewName = (String) result;
        Model viewModel = context.getModel();
        templateEngine.process(viewName, new ThymeleafContext(viewModel), context.getHttpServletResponse().getWriter());
    }

    @Override
    public boolean supported(Class<?> resultType) {
        return resultType == String.class;
    }
}
