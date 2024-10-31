package top.dongxiaohao;

import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.springboot.annotation.ForestScan;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.dongxiaohao.admin.security.handler.UserLogoutSuccessHandler;
import top.dongxiaohao.common.util.JwtTokenUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@ComponentScan(basePackages = {"top.dongxiaohao", "top.dongxiaohao.common"})
@MapperScan(basePackages = "top.dongxiaohao.**.dao")
@ForestScan(basePackages = {"top.dongxiaohao"})
public class AppleAdminApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(AppleAdminApplication.class, args);
        log.info(" \n _____ _    _  _____ _____ ______  _____ _____ \n" +
                " / ____| |  | |/ ____/ ____|  ____|/ ____/ ____|\n" +
                "| (___ | |  | | |   | |    | |__  | (___| (___  \n" +
                " \\___ \\| |  | | |   | |    |  __|  \\___ \\\\___ \\ \n" +
                " ____) | |__| | |___| |____| |____ ____) |___) |\n" +
                "|_____/ \\____/ \\_____\\_____|______|_____/_____/ \n");
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StrUtil.isEmpty(path)) {
            path = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "\n\t" +
                "External访问网址: \thttp://" + ip + ":" + port + path + "\n\t" +
                "Swagger文档网址: \thttp://" + ip + ":" + port + "/doc.html" + path + "\n\t" +
                "\n----------------------------------------------------------");
    }


}
