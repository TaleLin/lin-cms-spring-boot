package io.github.talelin.latticy.common.configuration;

import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

/**
 * @author Gadfly
 */

public class CustomServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

    public CustomServletModelAttributeMethodProcessor(final boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        Assert.state(servletRequest != null, "No ServletRequest");
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;

        // ServletModelAttributeMethodProcessor 此处使用的 servletBinder.bind(servletRequest)
        // 修改的目的是为了将 ServletRequestDataBinder 换成自定的 CustomServletRequestDataBinder
        new CustomServletRequestDataBinder(servletBinder.getTarget()).bind(servletRequest);
    }

}
