package cc.leevi.webbase.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtils {

    private static final int DEFAULT_SCALE = 4;

    private static final RoundingMode DEFAULT_MODE = RoundingMode.HALF_UP;

    private static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(4,RoundingMode.HALF_UP);

    private static BigDecimal create(Number number){
        if(number instanceof BigDecimal){
            return (BigDecimal) number;
        }
        return new BigDecimal(String.valueOf(number));
    }

    public static BigDecimal create(String value){
        return new BigDecimal(value);
    }

    public static BigDecimal divide(BigDecimal dividend,BigDecimal divisor,int scale, RoundingMode roundingMode){
        return dividend.divide(divisor,scale,roundingMode);
    }

    public static BigDecimal divide(BigDecimal dividend,BigDecimal divisor){
        return divide(dividend,divisor,DEFAULT_SCALE,DEFAULT_MODE);
    }

    public static BigDecimal divide(Number dividend,Number divisor){
        return divide(create(dividend), create(divisor));
    }


    public static BigDecimal multiply(BigDecimal source, BigDecimal multiplicand, MathContext context){
        return source.multiply(multiplicand,context);
    }

    public static BigDecimal multiply(BigDecimal source, BigDecimal multiplicand){
        return multiply(source,multiplicand,DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal multiply(Number source, Number multiplicand){
        return multiply(create(source), create(multiplicand));
    }


    public static BigDecimal subtract(BigDecimal source, BigDecimal subtrahend, MathContext context){
        return source.subtract(subtrahend,context);
    }

    public static BigDecimal subtract(BigDecimal source, BigDecimal subtrahend){
        return subtract(source,subtrahend,DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal subtrahend(Number source, Number subtrahend){
        return subtrahend(create(source), create(subtrahend));
    }


    public static BigDecimal add(BigDecimal source, BigDecimal augend, MathContext context){
        return source.add(augend,context);
    }

    public static BigDecimal add(BigDecimal source, BigDecimal augend){
        return add(source,augend,DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal add(Number source, Number augend){
        return add(create(source), create(augend));
    }



    public static void main(String[] args) {
        //usage:
        add(Double.valueOf("1"),2);
        add(2,Integer.valueOf(10));
        add(1,2);
        add(1l,2d);
        add(3f,4d);
        add(4f,BigDecimal.valueOf(5));
        add(BigDecimal.TEN,BigDecimal.ONE);
    }
}
