package com.collaborate.middleware;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
@Override
protected class<?>[] getRootConfigClasses()
{
	return new Class[]{WebResolver.class};
}
@Override
protected class<?>[] getServletConfigClasses()
{
	return null;
}
@Override
protected String[] getServletMapping()
{
	return new String[] {"/"};
}
}
