package com.example.jmx;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class JmxAgent {
    public static void main(String[] args) throws Exception {
        // 1. 플랫폼 MBeanServer 가져오기
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        // 2. MBean 객체 생성
        SystemConfig mBean = new SystemConfig(10, "default");

        // 3. ObjectName 생성 (도메인:키=값 형식)
        ObjectName name = new ObjectName("com.example.jmx:type=SystemConfig");

        // 4. MBean 등록
        mbs.registerMBean(mBean, name);
        System.out.println("SystemConfig MBean registered...");

        // [추가됨] 현재 등록된 모든 MBean 이름 출력
        System.out.println("\n=== All Registered MBeans ===");
        java.util.Set<ObjectName> allMBeans = mbs.queryNames(null, null);
        for (ObjectName objectName : allMBeans) {
            System.out.println(objectName);
        }
        System.out.println("=============================\n");

        System.out.println("Application is running. You can connect using JConsole.");
        System.out.println("Press Enter to exit...");

        // 5. 종료 방지 (사용자 입력 대기)
        System.in.read();
    }
}
