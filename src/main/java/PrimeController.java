import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.sqrt;
import static spark.Spark.*;

public class PrimeController {

    private static LoadingCache<Integer, String> cache;

    static {
        cache = CacheBuilder.newBuilder().maximumSize(100)
                .expireAfterWrite(100, TimeUnit.SECONDS)
                .build(
                        new CacheLoader<Integer, String>() {
                            @Override
                            public String load(Integer integer) throws Exception {
                                return listPrime(integer).toString();
                            }
                        }
                );
    }

    public LoadingCache<Integer, String> getCache(){
        return  this.cache;
    }


    public PrimeController() {


        get("/prime", new Route() {
            public Object handle(Request request, Response response) {
                String n = request.queryParams("n");
                try {
                    return cache.get(Integer.parseInt(n));
                } catch (NumberFormatException e) {
                    response.status(400);
                    return new ResponseError("Number format fail!");
                } catch (ExecutionException e) {
                    response.status(500);
                    return new ResponseError("Server error!");
                }
            }
        });

    }

    private static List<Integer> listPrime(int n) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 2; i < n; i++) {
            if (isPrime(i))
                list.add(i);
        }
        return list;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= sqrt(n); i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }


    public static void main(String[] args) {
        new PrimeController();
    }

}
