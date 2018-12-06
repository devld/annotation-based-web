package ld.common.response.thymeleaf;

import ld.common.response.Model;
import org.thymeleaf.context.IContext;

import java.util.Locale;
import java.util.Set;

public class ThymeleafContext implements IContext {

    private Model model;

    ThymeleafContext(Model model) {
        this.model = model;
    }

    @Override
    public Locale getLocale() {
        return Locale.CHINA;
    }

    @Override
    public boolean containsVariable(String name) {
        return model.contains(name);
    }

    @Override
    public Set<String> getVariableNames() {
        return model.keySet();
    }

    @Override
    public Object getVariable(String name) {
        return model.get(name);
    }
}
