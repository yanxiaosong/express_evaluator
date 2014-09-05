package calculator;

public class Main {

    public static void main(String[] args) {

       /*
       results:
       /usr/local/java/jdk1.7.0_45/bin/java -Didea.launcher.port=7534 -Didea.launcher.bin.path=/home/alan/Downloads/idea-IU-129.1359/bin -Dfile.encoding=UTF-8 -classpath /usr/local/java/jdk1.7.0_45/jre/lib/plugin.jar:/usr/local/java/jdk1.7.0_45/jre/lib/jfr.jar:/usr/local/java/jdk1.7.0_45/jre/lib/jfxrt.jar:/usr/local/java/jdk1.7.0_45/jre/lib/management-agent.jar:/usr/local/java/jdk1.7.0_45/jre/lib/jsse.jar:/usr/local/java/jdk1.7.0_45/jre/lib/charsets.jar:/usr/local/java/jdk1.7.0_45/jre/lib/deploy.jar:/usr/local/java/jdk1.7.0_45/jre/lib/rt.jar:/usr/local/java/jdk1.7.0_45/jre/lib/javaws.jar:/usr/local/java/jdk1.7.0_45/jre/lib/jce.jar:/usr/local/java/jdk1.7.0_45/jre/lib/resources.jar:/usr/local/java/jdk1.7.0_45/jre/lib/ext/localedata.jar:/usr/local/java/jdk1.7.0_45/jre/lib/ext/sunjce_provider.jar:/usr/local/java/jdk1.7.0_45/jre/lib/ext/sunpkcs11.jar:/usr/local/java/jdk1.7.0_45/jre/lib/ext/zipfs.jar:/usr/local/java/jdk1.7.0_45/jre/lib/ext/dnsns.jar:/usr/local/java/jdk1.7.0_45/jre/lib/ext/sunec.jar:/home/alan/Documents/projects/challenge/out/production/challenge:/home/alan/Documents/projects/challenge/lib/junit-4.9.jar:/home/alan/Documents/projects/challenge/lib/hamcrest-core-1.1.jar:/home/alan/Downloads/idea-IU-129.1359/lib/idea_rt.jar com.intellij.rt.execution.application.AppMain calculator.Main
        3
        7
        12
        10
        55
        55
        56
        40
        80

        Process finished with exit code 0
        */

        Expression e = new Expression("(add(1, 2))");
        System.out.println(e.eval());

        e = new Expression("add(1, mult(2, 3))");
        System.out.println(e.eval());

        e = new Expression("mult(add(2, 2), div(9, 3))");
        System.out.println(e.eval());

        e = new Expression("let(a, 5, add(a, a))");
        System.out.println(e.eval());

        e = new Expression("let(a, 5, let(b, mult(a, 10), add(b, a)))");
        System.out.println(e.eval());

        e = new Expression("let(a, 5, let(b, mult(a, 10), add(b, a)))");
        System.out.println(e.eval());

        e = new Expression("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
        System.out.println(e.eval());

        e = new Expression("(add(1, let(a, 5, let(b, mult(a, 10), add(b, a)))))");
        System.out.println(e.eval());

        e = new Expression("let(a, let(b, let(c, 20, add(c,10)), add(b, b)), let(b, 20, add(a, b)))");
        System.out.println(e.eval());
    }
}
