package com.ghag.rnd.security;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import static org.mockito.Mockito.*;



public class MyTest {
	
	@Test
	public void test1() throws Exception{
		System.out.println("from test1");
		HttpServletRequest req = mock(HttpServletRequest.class);
		
		ServletInputStream servletInputStream = getServletInputStream("some string");
		
		when(req.getInputStream()).thenReturn(servletInputStream);
		when(req.getMethod()).thenReturn("POST");
		when(req.getHeader("Content-Length")).thenReturn("11");
		

			BufferedHttpRequest b = new BufferedHttpRequest(req);
			 b.getInputStream();
			 b.getParameterMap();
			 b.getParameterNames();
			 b.getParameter("some");
			 b.getParameterValues("");

		
		
		assertTrue("Everything is all right", true);
		
	}
	
    private ServletInputStream getServletInputStream(String input) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes());

        return new ServletInputStream(){
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

        };
    }

	
	
    public class BufferedHttpRequest extends HttpServletRequestWrapper {
        private byte[] _body = null;
        public BufferedHttpRequest(HttpServletRequest request) throws IOException {
            super(request);
            _body = IOUtils.toByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(_body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = new HashMap<String, String[]>();
            // populates query parameters
            parameterMap.putAll(super.getParameterMap());
            if (super.getMethod().equals("POST")
                    || super.getMethod().equals("PUT")) {
                final Integer len = Integer.valueOf(this.getHeader("Content-Length"));
                //try {
                    // populates all post parameters
                    //parameterMap.putAll(IptHttpUtils.parsePostData(len, this.getInputStream(),"UTF-8"));
                //} catch (IOException e) {
                    //LOGGER.error("Error parsing parameter map - ", e);
                //}
            }
            return parameterMap;
        }

        @Override
        public String[] getParameterValues(String parameter) {
            return this.getParameterMap().get(parameter);
        	//return new String[]{};
        }

        @Override
        public String getParameter(String name) {
            String parameterValue = null;
            String[] values = this.getParameterMap().get(name);
            if (values != null && values.length > 0) {
                parameterValue = values[0];
            }
            return parameterValue;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            //return Iterators.asEnumeration(getParameterMap().keySet().iterator());
        	return null;
        }
    }
	

}
