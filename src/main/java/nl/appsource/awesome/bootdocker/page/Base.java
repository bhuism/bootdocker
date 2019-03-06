package nl.appsource.awesome.bootdocker.page;

import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base for pages which do not require a user to be logged in. If a page
 * requires a login, please extend from {@link BaseHf}.
 * 
 * {@link BaseHf}
 *
 * @param <T>
 *            the page type
 */

public abstract class Base<T> extends GenericWebPage<T> {

    public Base() {
    }

    public Base(final PageParameters pageParameters) {
        super(pageParameters);
    }

    public Base(final IModel<T> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }


}
