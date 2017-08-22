package cepBench;
import org.apache.commons.math3.distribution.*;

import java.util.StringTokenizer;

/**
 * Created by mohammadreza on 7/13/2016.
 */
public class NumberGenerator {
    double generate(String inputString){
        String[] arguments = inputString.split(" ");
        String distributionType = arguments[0];

        switch (distributionType){
            case "normal": // "normal 111 222"
                Double mean = Double.parseDouble(arguments[1]);
                Double sd = Double.parseDouble(arguments[2]);
                NormalDistribution normal = new NormalDistribution(mean,sd);
                return (normal.sample());

            case "logistic" : // logistic mu s
                Double mu = Double.parseDouble(arguments[1]);
                Double s = Double.parseDouble(arguments[2]);
                LogisticDistribution logistic = new LogisticDistribution(mu,s);
                return (logistic.sample());
            case "poisson" :
                Double p = Double.parseDouble(arguments[1]);
                Double epsilon= Double.parseDouble(arguments[2]);
                PoissonDistribution poisson = new PoissonDistribution(p,epsilon);
                return ((double) poisson.sample());

            case "triangular":
                Double a = Double.parseDouble(arguments[1]);;
                Double b = Double.parseDouble(arguments[2]);;
                Double c = Double.parseDouble(arguments[3]);;
                TriangularDistribution triangular = new TriangularDistribution(a,b,c);
                return  (triangular.sample());
        }

        return 0.0;
    }
}
