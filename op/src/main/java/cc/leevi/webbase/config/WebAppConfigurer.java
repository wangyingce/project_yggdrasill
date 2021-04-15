package cc.leevi.webbase.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * spring相关配置
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    public Map<String,String> buildCorsFilterParams(){
        Map<String,String> params = new HashMap<>();
        return params;
    }


    /**
     * 添加参数转换器，解决空字符串""的问题
     * @return
     */
    @Bean
    public ConfigurableWebBindingInitializer configurableWebBindingInitializer() {
        return new ConfigurableWebBindingInitializer() {
            @Override
            public void initBinder(WebDataBinder binder) {
                super.initBinder(binder);
                binder.registerCustomEditor(String.class, new StringTrimmerEditor(
                        true));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat,true));
            }
        };
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(fastConverter);
    }

}
